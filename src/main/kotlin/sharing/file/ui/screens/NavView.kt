package sharing.file.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import sharing.file.ui.navigation.Navigation
import sharing.file.ui.screens.about.AboutScreen
import sharing.file.ui.screens.about.AboutViewModel
import sharing.file.ui.screens.edit.EditDocumentView
import sharing.file.ui.screens.edit.EditDocumentViewModel
import sharing.file.ui.screens.login.LoginView
import sharing.file.ui.screens.login.LoginViewModel
import sharing.file.ui.screens.main.MainScreen
import sharing.file.ui.screens.main.MainViewModel
import sharing.file.ui.screens.splash.SplashView

@Composable
fun NavView() {
    var state by mutableStateOf(Navigation.Splash)
    Scaffold(topBar = {
        TopAppBar {
            Icon(
                painter = painterResource("img/arrow_back.svg"),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { /* TODO */ },
                contentDescription = "Back"
            )
            Box(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource("img/info.svg"),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clickable { state = Navigation.About },
                contentDescription = "About"
            )
            Row(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .border(2.dp, Color.Black, shape = RoundedCornerShape(8.dp))
                    .clickable { state = Navigation.Login }
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource("img/login.svg"),
                    modifier = Modifier
                        .padding(end = 4.dp),
                    contentDescription = "Login"
                )
                Text("Войти", modifier = Modifier.align(Alignment.CenterVertically))
            }
        }
    }) {
        Box(modifier = Modifier.padding(it)) {
            when (state) {
                Navigation.Main -> {
                    MainScreen(MainViewModel())
                }
                Navigation.About -> {
                    AboutScreen(AboutViewModel())
                }
                Navigation.Splash -> SplashView()
                Navigation.Login -> {
                    LoginView(LoginViewModel())
                }
                Navigation.EditDocument -> {
                    EditDocumentView(EditDocumentViewModel())
                }
            }
        }
    }
}