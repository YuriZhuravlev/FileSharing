package sharing.file.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import sharing.file.data.repository.UserRepository
import sharing.file.ui.navigation.Navigation
import sharing.file.ui.screens.about.AboutScreen
import sharing.file.ui.screens.edit.EditDocumentView
import sharing.file.ui.screens.edit.EditDocumentViewModel
import sharing.file.ui.screens.login.LoginView
import sharing.file.ui.screens.login.LoginViewModel
import sharing.file.ui.screens.main.MainScreen
import sharing.file.ui.screens.main.MainViewModel
import sharing.file.ui.screens.splash.SplashView

@Composable
fun NavView() {
    var state by remember { mutableStateOf(Navigation.Splash) }
    var documentPath by remember { mutableStateOf<String?>(null) }
    val user by UserRepository.user.collectAsState()
    if (user.data == null && state == Navigation.Main)
        state = Navigation.Splash
    Scaffold(topBar = {
        TopAppBar {
            if (!(state == Navigation.Splash || state == Navigation.Main)) {
                Icon(
                    painter = painterResource("img/arrow_back.svg"),
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable {
                            state = if (user.data == null)
                                Navigation.Splash
                            else
                                Navigation.Main
                        },
                    contentDescription = "Back"
                )
            }
            Box(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource("img/info.svg"),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { state = Navigation.About },
                contentDescription = "About"
            )
            val userItem = user.data
            Row(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                    .clickable {
                        state = if (userItem == null)
                            Navigation.Login
                        else
                            Navigation.Main
                    }
                    .padding(8.dp)
            ) {
                if (userItem == null) {
                    Icon(
                        painter = painterResource("img/login.svg"),
                        modifier = Modifier
                            .padding(end = 4.dp),
                        contentDescription = "Login"
                    )
                    Text("Войти", modifier = Modifier.align(Alignment.CenterVertically))
                } else {
                    Text(
                        userItem.name, modifier = Modifier.height(24.dp)
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }) {
        Box(modifier = Modifier.padding(it)) {
            when (state) {
                Navigation.Main -> {
                    MainScreen(MainViewModel()) {
                        documentPath = it
                        state = Navigation.EditDocument
                    }
                }
                Navigation.About -> {
                    AboutScreen()
                }
                Navigation.Splash -> SplashView()
                Navigation.Login -> {
                    LoginView(LoginViewModel()) {
                        state = Navigation.Main
                    }
                }
                Navigation.EditDocument -> {
                    val doc = documentPath
                    if (doc != null)
                        EditDocumentView(EditDocumentViewModel(doc))
                    else
                        state = Navigation.Main
                }
            }
        }
    }
}