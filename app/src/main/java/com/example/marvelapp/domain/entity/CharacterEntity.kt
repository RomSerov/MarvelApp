package com.example.marvelapp.domain.entity

data class CharacterEntity(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val thumbnailPath: String = "",
    val thumbnailExtension: String = "",
    val modified: String = ""
)
