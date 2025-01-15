package com.yeshuwahane.pokedex.presentation.feature.detailscreen

import com.yeshuwahane.pokedex.data.util.DataResource

data class PokemonDetailState(
    val detailState: DataResource<DetailState> = DataResource.initial()
)

data class DetailState(
    val id: Int,
    val name: String,
    val types: List<String>, // List of Pokémon types (e.g., "Grass", "Poison")
    val weight: Float,       // Pokémon weight in kilograms
    val height: Float,       // Pokémon height in meters
    val stats: List<StatDetail>, // List of stats like HP, Attack, Defense
    val spriteUrl: String,   // URL to Pokémon image
    val species: String,     // Pokémon species description
    val description: String  // Pokémon description
)

data class StatDetail(
    val name: String,        // Stat name (e.g., "HP", "Attack")
    val value: Int           // Base stat value
)
