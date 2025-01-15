package com.yeshuwahane.pokedex.data.model


data class PokemonSpeciesResponse(
    val flavor_text_entries: List<FlavorText>,
    val genera: List<Genus>
)

data class FlavorText(val flavor_text: String, val language: Language)
data class Language(val name: String)
data class Genus(val genus: String, val language: Language)