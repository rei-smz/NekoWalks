package com.example.nekowalks

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.nekowalks.ui.theme.NekoWalksTheme

@Composable
fun Cat() {
    Text(text = "This is Cat Activity")
}

@Preview(showBackground = true)
@Composable
fun CatPreview() {
    NekoWalksTheme {
        Cat()
    }
}
