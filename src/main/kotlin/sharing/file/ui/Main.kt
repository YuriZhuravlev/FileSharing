package sharing.file.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import sharing.file.ui.screens.NavView

@Composable
@Preview
fun App() {
    MaterialTheme {
        NavView()
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "File Sharing",
        icon = painterResource("img/icon.svg")
    ) {
        App()
    }
}
