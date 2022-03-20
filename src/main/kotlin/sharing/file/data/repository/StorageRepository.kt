package sharing.file.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sharing.file.data.Resource
import sharing.file.data.manager.CryptoManager
import sharing.file.data.model.OpenKey
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
                Resource.FailedResource(e)
            }
        }
    }

    suspend fun importKey(path: String): Resource<Any> {
        return withContext(Dispatchers.IO) {
            try {
                OpenKey.read(path).run {
                    val signedOpenKey = SignedOpenKey(
                        nameLen = nameLen,
                        blobLen = blobLen,
                        name = name,
                        blob = blob,
                        sign = CryptoManager.createSignature(
                            data = name + blob,
                            UserRepository.user.value.data!!.privateKey
                        )
                    )
                }
                Resource.SuccessResource(Any())
            } catch (e: Exception) {
                Resource.FailedResource(e)
            }
        }
    }
}