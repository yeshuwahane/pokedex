package com.yeshuwahane.pokedex.data.mapper

import com.yeshuwahane.pokedex.data.model.Pokemon
import com.yeshuwahane.pokedex.data.model.PokemonDetailResponse
import com.yeshuwahane.pokedex.data.model.PokemonSpeciesResponse
import com.yeshuwahane.pokedex.presentation.feature.detailscreen.DetailState
import com.yeshuwahane.pokedex.presentation.feature.detailscreen.PokemonDetailState
import com.yeshuwahane.pokedex.presentation.feature.detailscreen.StatDetail
import com.yeshuwahane.pokedex.presentation.feature.home.PokemonState


fun Pokemon.toPokemonUiState(): PokemonState {
    val id = url.split("/").dropLast(1).last()

    return PokemonState(
        name = name,
        url = url,
        icon = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png",
        gif = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/showdown/$id.gif",
        id = id.toInt()
    )

}

// Mapper to convert API response to PokemonDetail
fun PokemonDetailResponse.toDetailState(species: PokemonSpeciesResponse): DetailState {
    val englishFlavorText = species.flavor_text_entries.firstOrNull { it.language.name == "en" }?.flavor_text ?: ""
    val englishGenus = species.genera.firstOrNull { it.language.name == "en" }?.genus ?: "Unknown Pokémon"

    return DetailState(
        id = id,
        name = name.replaceFirstChar { it.uppercase() },
        types = types.map { it.type.name.replaceFirstChar { it.uppercase() } },
        weight = weight / 10f,
        height = height / 10f,
        stats = stats.map {
            StatDetail(
                name = it.stat.name.replaceFirstChar { it.uppercase() },
                value = it.base_stat
            )
        },
        spriteUrl = sprites.other.official_artwork.front_default,
        species = englishGenus,
        description = englishFlavorText.replace("\n", " ")
    )
}



/*
* val detail image = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/dream-world/1.svg"
*  https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/showdown/1.gif
* */