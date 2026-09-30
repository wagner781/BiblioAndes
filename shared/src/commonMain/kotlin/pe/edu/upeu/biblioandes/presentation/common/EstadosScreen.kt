package pe.edu.upeu.biblioandes.presentation.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CargandoScreen(modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun VacioScreen(mensaje: String, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Text(mensaje, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun ErrorScreen(mensaje: String, reintentar: (() -> Unit)? = null, modifier: Modifier = Modifier) {
    Box(modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(mensaje, color = MaterialTheme.colorScheme.error)
            if (reintentar != null) Button(onClick = reintentar) { Text("Reintentar") }
        }
    }
}
