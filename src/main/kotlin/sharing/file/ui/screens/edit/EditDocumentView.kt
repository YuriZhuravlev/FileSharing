package sharing.file.ui.screens.edit

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import sharing.file.data.Resource
import sharing.file.data.model.Document
import sharing.file.ui.screens.main.SaveFile
import sharing.file.ui.view.BigText

@Composable
fun EditDocumentView(viewModel: EditDocumentViewModel) {
    val document by viewModel.document.collectAsState()
    if (document.status == Resource.Status.Loading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            val doc = document.data
            if (doc != null) {
                var text by remember { mutableStateOf(doc.text) }
                val result by viewModel.result.collectAsState()
                var dialog by remember { mutableStateOf<(@Composable () -> Unit)?>(null) }
                if (dialog != null) {
                    dialog?.invoke()
                    dialog = null
                }

                Column(modifier = Modifier.width(160.dp).padding(8.dp)) {
                    Text("Владелец", fontSize = 12.sp)
                    OutlinedTextField(doc.name, {}, readOnly = true)
                    Text(
                        when (doc.verify) {
                            Document.Verify.FailedSigned -> "Подпись документа не подтверждена"
                            Document.Verify.NotFoundOpenKey -> "Отсутствует открытый ключ автора документа"
                            Document.Verify.FailedSignedOpenKey -> "Подпись открытого ключа не подтверждена"
                            Document.Verify.Success -> "Подписанный документ"
                            else -> ""
                        },
                        modifier = Modifier.align(Alignment.End),
                        color = if (doc.verify != Document.Verify.Success) Color.Red else Color.Black
                    )
                    Box(Modifier.weight(1f))
                    if (result.status == Resource.Status.Loading)
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    if (result.status == Resource.Status.Failed)
                        Text("Произошла ошибка")
                    Button({
                        dialog = @Composable {
                            SaveFile(type = Document.type) {
                                if (it != null)
                                    viewModel.save(text, it)
                            }
                        }
                    }) {
                        Text("Сохранить")
                    }
                }
                OutlinedTextField(value = text, onValueChange = {
                    text = it
                }, modifier = Modifier.weight(1f).fillMaxHeight().padding(8.dp))
            } else {
                BigText(document.error?.message ?: "Error :(", Modifier.padding(16.dp))
            }
        }
    }
}