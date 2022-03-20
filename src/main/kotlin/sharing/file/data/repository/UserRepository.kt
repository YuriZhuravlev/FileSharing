package sharing.file.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import sharing.file.data.Resource
import sharing.file.data.manager.KeyManager
import sharing.file.data.model.User

object UserRepository {
    private val _user = MutableStateFlow<Resource<User?>>(Resource.SuccessResource(null))
    val user = _user.asStateFlow()
    val keyManager = KeyManager

    suspend fun login(name: String) {
        _user.emit(Resource.LoadingResource())
        val result = withContext(Dispatchers.IO) {
            try {
                Resource.SuccessResource<User?>(keyManager.getUser(name))
            } catch (e: Exception) {
                Resource.FailedResource(e)
            }
        }
        println("${result.status} login ${result.data?.name ?: result.error?.message}")
        _user.emit(result)
    }

    suspend fun logout() {
        _user.emit(Resource.SuccessResource(null))
    }

    suspend fun deleteKeys(name: String) {
        _user.emit(Resource.LoadingResource(_user.value.data))
        withContext(Dispatchers.IO) {
            try {
                Resource.SuccessResource(keyManager.deletePair(name))
                _user.emit(Resource.SuccessResource(null))
            } catch (e: Exception) {
                _user.emit(Resource.FailedResource(e, _user.value.data))
                println("Failed deleteKeys ${e.message}")
            }
        }
    }
}