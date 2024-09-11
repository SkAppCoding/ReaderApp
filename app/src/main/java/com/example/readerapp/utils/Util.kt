package com.example.readerapp.utils

fun getHttpsImageUrl(thumbnailLink: String): String {
    if(thumbnailLink.contains("https"))
        return thumbnailLink
    else
        return thumbnailLink.replace("http", "https")
}