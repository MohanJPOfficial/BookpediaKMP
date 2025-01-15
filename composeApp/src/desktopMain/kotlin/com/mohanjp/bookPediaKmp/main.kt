package com.mohanjp.bookPediaKmp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.mohanjp.bookPediaKmp.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "BookpediaKMP",
        ) {
            App()
        }
    }
}
