package sharing.file.ui.screens.login

import kotlinx.coroutines.launch
import sharing.file.base.ViewModel
import sharing.file.data.repository.UserRepository

class LoginViewModel : ViewModel() {
    private val userRepository = UserRepository
    val user get() = userRepository.user

    fun login(name: String) {
        viewModelScope.launch {
            userRepository.login(name)
        }
    }
}