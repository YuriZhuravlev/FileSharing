package sharing.file.ui.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.AwtWindow
import sharing.file.data.Resource
import sharing.file.data.model.Document
import sharing.file.data.model.OpenKey
import java.awt.FileDialog
import java.awt.Frame
import java.io.FilenameFilter

@Composable
fun MainScreen(viewModel: MainViewModel, onOpenDocument: (String?) -> Unit) {
    val user by viewModel.user.collectAsState()
    val loading by viewModel.loading.collectAsState()
    var dialog by remember { mutableStateOf<(@Composable () -> Unit)?>(null) }
    if (dialog != null) {
        dialog?.invoke()
        dialog = null
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            Box(modifier = Modifier.height(24.dp)) {
                when (user.status) {
                    Resource.Status.Loading -> CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center).size(16.dp)
                    )
                    Resource.Status.Failed -> Text(user.error?.message ?: "Произошла ошибка", color = Color.Red)
                    else -> {
                        when (loading.status) {
                            Resource.Status.Loading -> CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center).size(16.dp)
                            )
                            Resource.Status.Failed -> Text(user.error?.message ?: "Произошла ошибка", color = Color.Red)
                            else -> {}
                        }
                    }
                }
            }
            Button({ onOpenDocument(null) }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Создать документ")
            }
            Button({
                dialog = @Composable {
                    SelectFile(type = Document.type) {
                        if (it != null)
                            onOpenDocument(it)
                    }
                }
            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Загрузить документ")
            }
            Button({
                dialog = @Composable {
                    SaveFile(name = user.data?.name.orEmpty(), type = OpenKey.type) {
                        if (it != null)
                            viewModel.exportKey(it)
                    }
                }
            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Экспорт ключа")
            }
            Button({
                dialog = @Composable {
                    SelectFile(type = OpenKey.type) {
                        if (it != null)
                            viewModel.importKey(it)
                    }
                }
            }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
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

@Composable
fun SaveFile(
    parent: Frame? = null,
    name: String = "*",
    type: String = "*",
    onCloseRequest: (result: String?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Выберите место для экспорта", SAVE) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (!value && directory != null && file != null) {
                    onCloseRequest(directory + file)
                }
            }
        }.apply {
            isMultipleMode = false
            file = "${name}.${type}"
            filenameFilter = FilenameFilter { _, name ->
                name.endsWith(".${type}", true)
            }
        }
    },
    dispose = { }
)

@Composable
fun SelectFile(
    parent: Frame? = null,
    type: String = "*",
    onCloseRequest: (result: String?) -> Unit
) = AwtWindow(
    create = {
        object : FileDialog(parent, "Выберите файл", LOAD) {
            override fun setVisible(value: Boolean) {
                super.setVisible(value)
                if (!value && directory != null && file != null) {
                    onCloseRequest(directory + file)
                }
            }
        }.apply {
            isMultipleMode = false
            file = "*.${type}"
        }
    },
    dispose = { }
)
