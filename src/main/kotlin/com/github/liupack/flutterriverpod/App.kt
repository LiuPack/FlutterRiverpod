package com.github.liupack.flutterriverpod

import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*

@Composable
fun app() {
    var text by remember { mutableStateOf("") }
    MaterialTheme {
        Column {
            Text(text)
            TextField(value = text, onValueChange = { text = it })
        }
    }
}