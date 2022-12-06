package com.example.marvelapp.data.mapper

import com.example.marvelapp.data.model.charactersdto.CharactersDto
import com.example.marvelapp.domain.entity.CharacterEntity
import javax.inject.Inject

class MarvelMapper @Inject constructor() {

    fun mapDtoToEntity(dto: CharactersDto) = CharacterEntity(
        id = dto.id,
        name = dto.name,
        description = dto.description,
        thumbnailPath = dto.thumbnail.path,
        thumbnailExtension = dto.thumbnail.extension,
        modified = dto.modified
    )
}
