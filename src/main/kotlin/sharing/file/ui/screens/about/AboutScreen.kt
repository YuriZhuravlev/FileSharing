package sharing.file.ui.screens.about

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import sharing.file.ui.view.BigText
import sharing.file.ui.view.HeaderText

@Composable
fun AboutScreen() {
    Column(Modifier.fillMaxSize().padding(8.dp)) {
        HeaderText("О программе", modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 8.dp))
        BigText("Лабораторная работа №1")
        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp)) {
            Text("Журавлев Юрий", textAlign = TextAlign.Start)
            Box(modifier = Modifier.weight(1f))
            Text("гр. А-05-18", textAlign = TextAlign.End)
        }
        Text("Прототип системы электронного документооборота.")
        Text(
            "Вариант 9:\n" +
                    "\tSHA1 - Алгоритм хеширования документа\n" +
                    "\tRSA - Алгоритм подписи документа\n" +
                    "\tSHA1 - Алгоритм хеширования открытого ключа\n" +
                    "\tRSA - Алгоритм подписи открытого ключа"
        )
    }
}