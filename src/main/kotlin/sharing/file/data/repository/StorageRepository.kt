package sharing.file.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sharing.file.data.Resource
import sharing.file.data.manager.CryptoManager
import sharing.file.data.model.OpenKey
import sharing.file.data.model.OpenKeyFailedSigned
import sharing.file.data.model.OpenKeyNotFound
import sharing.file.data.model.SignedOpenKey

object StorageRepository {
    suspend fun exportKey(path: String): Resource<Any> {
        return withContext(Dispatchers.IO) {
            try {
                val user = UserRepository.user.value.data!!
                val name = user.name.toByteArray()
                val openKey = OpenKey(name.size, user.publicKey.size, name, user.publicKey)
                openKey.write(path)
                Resource.SuccessResource(Any())
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.FailedResource(e)
            }
        }
    }

    suspend fun importKey(path: String): Resource<Any> {
        return withContext(Dispatchers.IO) {
            try {
                val user = UserRepository.user.value.data!!
                OpenKey.read(path).run {
                    val signedOpenKey = SignedOpenKey(
                        nameLen = nameLen,
                        blobLen = blobLen,
                        name = name,
                        blob = blob,
                        sign = CryptoManager.createSignature(
                            data = name + blob,
                            user.privateKey
                        )
                    )
                    signedOpenKey.write("PK/${user.name}/${String(signedOpenKey.name)}.${SignedOpenKey.type}")
                }
                Resource.SuccessResource(Any())
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.FailedResource(e)
            }
        }
    }

    suspend fun getSignedOpenKey(name: String): Resource<SignedOpenKey> {
        return withContext(Dispatchers.IO) {
            try {
                val user = UserRepository.user.value.data!!
                val signedOpenKey = SignedOpenKey.read("PK/${user.name}/${name}.${SignedOpenKey.type}")
                if (CryptoManager.sign(
                        user.publicKey,
                        data = signedOpenKey.name + signedOpenKey.blob,
                        signedOpenKey.sign
                    )
                ) {
                    Resource.SuccessResource(signedOpenKey)
                } else {
                    Resource.FailedResource(OpenKeyFailedSigned())
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.FailedResource(OpenKeyNotFound())
            }
        }
    }
}