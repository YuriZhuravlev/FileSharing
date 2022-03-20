package sharing.file.ui.screens.main

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sharing.file.base.ViewModel
import sharing.file.data.Resource
import sharing.file.data.repository.StorageRepository
import sharing.file.data.repository.UserRepository

class MainViewModel : ViewModel() {
    private val userRepository = UserRepository
    private val storageRepository = StorageRepository

    val user get() = userRepository.user

    private val _loading = MutableStateFlow<Resource<Any>>(Resource.SuccessResource(Any()))
    val loading = _loading.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun delete() {
        viewModelScope.launch {
            userRepository.deleteKeys()
        }
    }

    fun exportKey(path: String) {
        viewModelScope.launch {
            _loading.value = Resource.LoadingResource()
            _loading.value = storageRepository.exportKey(path)
        }
    }

    fun importKey(path: String) {
        viewModelScope.launch {
            _loading.value = Resource.LoadingResource()
            _loading.value = storageRepository.importKey(path)
        }
    }

}