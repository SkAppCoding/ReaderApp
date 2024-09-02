package com.example.readerapp.utils

fun getHttpsImageUrl(thumbnailLink: String): String {
    return thumbnailLink.replace("http", "https")
}