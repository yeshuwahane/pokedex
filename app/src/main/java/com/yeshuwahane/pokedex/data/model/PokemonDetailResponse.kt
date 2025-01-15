package com.yeshuwahane.pokedex.data.model


data class PokemonDetailResponse(
    val id: Int,
    val name: String,
    val types: List<Type>,
    val weight: Int,
    val height: Int,
    val stats: List<Stat>,
    val sprites: Sprites
)

data class Type(val type: TypeName)
data class TypeName(val name: String)
data class Stat(val stat: StatName, val base_stat: Int)
data class StatName(val name: String)
data class Sprites(val other: OtherSprites)
data class OtherSprites(val official_artwork: Artwork)
data class Artwork(val front_default: String)




