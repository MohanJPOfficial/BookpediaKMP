package com.mohanjp.bookPediaKmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform