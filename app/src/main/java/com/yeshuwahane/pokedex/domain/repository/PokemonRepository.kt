package com.yeshuwahane.pokedex.domain.repository

import com.yeshuwahane.pokedex.data.model.Pokemon
import com.yeshuwahane.pokedex.data.util.DataResource
import com.yeshuwahane.pokedex.presentation.feature.detailscreen.DetailState
import com.yeshuwahane.pokedex.presentation.feature.detailscreen.PokemonDetailState
import com.yeshuwahane.pokedex.presentation.feature.home.PokemonState


interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): DataResource<List<PokemonState>>

    suspend fun getPokemonDetails(id: Int): DataResource<DetailState>


}