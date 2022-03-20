package sharing.file.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import sharing.file.data.Resource
import sharing.file.data.model.Document

@Composable
fun MainScreen(viewModel: MainViewModel, onOpenDocument: (Document?) -> Unit) {
    val user by viewModel.user.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            Box(modifier = Modifier.height(24.dp)) {
                when (user.status) {
                    Resource.Status.Loading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center).size(16.dp)
                    )
                    Resource.Status.Failed -> Text(user.error?.message ?: "Произошла ошибка", color = Color.Red)
                    else -> {}
                }
            }
            Button({ onOpenDocument(null) }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Создать документ")
            }
            Button({ onOpenDocument(null) }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Загрузить документ")
            }
            Button({}, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Экспорт ключа")
            }
            Button({}, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Импорт ключа")
            }
            Button({ viewModel.delete() }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Удалить пару ключей")
            }
            Button({ viewModel.logout() }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Выйти")
            }
        }
    }
}