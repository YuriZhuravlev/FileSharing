package sharing.file.ui.screens.main

import kotlinx.coroutines.launch
import sharing.file.base.ViewModel
import sharing.file.data.repository.UserRepository

class MainViewModel : ViewModel() {
    private val userRepository = UserRepository
    val user get() = userRepository.user

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
}