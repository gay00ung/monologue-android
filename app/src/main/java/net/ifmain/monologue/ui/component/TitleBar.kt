package net.ifmain.monologue.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.ifmain.monologue.ui.theme.Honey

@Composable
fun TitleBar() {
    Text(
        text = "모노로그 📝",
        modifier = Modifier.fillMaxWidth().padding(top = 32.dp),
        style = MaterialTheme.typography.headlineLarge,
        textAlign = TextAlign.Center,
        color = Honey,
        fontWeight = FontWeight.Bold
    )
}
