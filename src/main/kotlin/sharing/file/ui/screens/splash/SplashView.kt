package sharing.file.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import sharing.file.ui.view.BigText

@Composable
fun SplashView() {
    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        BigText(
            "Для использования приложения войдите",
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 8.dp)
        )
        Text("Приложение позволяет редактировать и подписывать текстовые документы, обмениваться открытыми ключами между пользователями")
        Icon(
            painterResource("img/icon.svg"),
            "icon",
            modifier = Modifier.padding(16.dp).size(200.dp).background(Color.LightGray, RoundedCornerShape(16.dp))
                .padding(8.dp).align(Alignment.CenterHorizontally)
        )
    }
}