package sharing.file.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sharing.file.data.Resource
import sharing.file.data.manager.CryptoManager
import sharing.file.data.model.*
import java.io.*

object DocumentRepository {
    // Если документ с заданным именем не найден,
    // отсутствует открытый ключ автора документа (он не был предварительно импортирован),
    // не подтверждаются подписи под открытым ключом
    // или  документом,
    // приложение должно выводить соответствующие сообщения
    suspend fun openDocument(path: String): Resource<Document> {
        return try {
            Resource.SuccessResource(read(path).apply {
                val signedOpenKey = StorageRepository.getSignedOpenKey(name)
                val signed = signedOpenKey.data
                verify = when {
                    (signedOpenKey.error is OpenKeyNotFound) -> Document.Verify.NotFoundOpenKey
                    (signedOpenKey.error is OpenKeyFailedSigned) -> Document.Verify.FailedSignedOpenKey
                    else -> {
                        if (signed != null && CryptoManager.sign(
                                publicKeyEncoded = signed.blob,
                                data = (name + text).toByteArray(),
                                digitalSignature = sign
                            )
                        )
                            Document.Verify.Success
                        else
                            Document.Verify.FailedSigned
                    }
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.FailedResource(e)
        }
    }


    suspend fun saveDocument(text: String, path: String): Resource<Any> {
        return try {
            val user = UserRepository.user.value.data!!
            val document = Document(
                0,
                0,
                user.name,
                ByteArray(0),
                text
            )
            Resource.SuccessResource(write(document, path))
        } catch (e: Exception) {
            Resource.FailedResource(e)
        }
    }

    suspend fun saveDocument(document: Document, path: String): Resource<Any> {
        return try {
            Resource.SuccessResource(write(document, path))
        } catch (e: Exception) {
            Resource.FailedResource(e)
        }
    }

    private suspend fun read(path: String): Document {
        return withContext(Dispatchers.IO) {
            val file = File(path)
            if (!file.exists())
                throw DocumentNotFound(path)
            try {
                DataInputStream(FileInputStream(file)).run {
                    val nameLen = readInt()
                    val signLen = readInt()
                    Document(
                        nameLen = nameLen,
                        signLen = signLen,
                        name = String(readNBytes(nameLen)),
                        sign = readNBytes(signLen),
                        text = readUTF()
                    )
                }
            } catch (e: Exception) {
                throw DocumentFailed()
            }
        }
    }


    private suspend fun write(document: Document, path: String) {
        withContext(Dispatchers.IO) {
            val file = File(path)
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            DataOutputStream(FileOutputStream(path)).run {
                val name = document.name.toByteArray()
                val sign = CryptoManager.createSignature(
                    data = name + document.text.toByteArray(),
                    privateKeyEncoded = UserRepository.user.value.data!!.privateKey
                )
                writeInt(name.size)
                writeInt(sign.size)
                write(name)
                write(sign)
                writeUTF(document.text)
            }
        }
    }

}