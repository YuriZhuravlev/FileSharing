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
    private val keyManager = KeyManager
    val user = _user.asStateFlow()

    suspend fun login(name: String) {
        _user.emit(Resource.LoadingResource())
        val result = withContext(Dispatchers.IO) {
            try {
                Resource.SuccessResource<User?>(keyManager.getUser(name))
            } catch (e: Exception) {
                e.printStackTrace()
                Resource.FailedResource(e)
            }
        }
        _user.emit(result)
    }

    suspend fun logout() {
        _user.emit(Resource.SuccessResource(null))
    }

    suspend fun deleteKeys() {
        _user.emit(Resource.LoadingResource(_user.value.data))
        withContext(Dispatchers.IO) {
            try {
                val name = _user.value.data!!.name
                Resource.SuccessResource(keyManager.deletePair(name))
                _user.emit(Resource.SuccessResource(null))
            } catch (e: Exception) {
                _user.emit(Resource.FailedResource(e, _user.value.data))
                e.printStackTrace()
            }
        }
    }
}