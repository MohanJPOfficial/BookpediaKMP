package com.mohanjp.bookPediaKmp

import androidx.compose.ui.window.ComposeUIViewController
import com.mohanjp.bookPediaKmp.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }
