package com.yeshuwahane.pokedex.data.repositoryimpl

import com.yeshuwahane.pokedex.data.mapper.toDetailState
import com.yeshuwahane.pokedex.data.mapper.toPokemonUiState
import com.yeshuwahane.pokedex.data.model.Pokemon
import com.yeshuwahane.pokedex.data.network.PokemonApi
import com.yeshuwahane.pokedex.data.util.DataResource
import com.yeshuwahane.pokedex.data.util.safeApiCall
import com.yeshuwahane.pokedex.domain.repository.PokemonRepository
import com.yeshuwahane.pokedex.presentation.feature.detailscreen.DetailState
import com.yeshuwahane.pokedex.presentation.feature.detailscreen.PokemonDetailState
import com.yeshuwahane.pokedex.presentation.feature.home.PokemonState
import retrofit2.Response
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(private val api: PokemonApi): PokemonRepository {

    override suspend fun getPokemonList(
        limit: Int,
        offset: Int
    ): DataResource<List<PokemonState>> {
        return safeApiCall{
            val response = api.getPokemonList(limit, offset).results.map { it.toPokemonUiState() }
            Response.success(response)
        }
    }

    override suspend fun getPokemonDetails(id: Int): DataResource<DetailState> {
        return safeApiCall {
            val detailResponse = api.getPokemonDetails(id)
            val speciesResponse = api.getPokemonSpecies(id)
            val pokemonDetail = detailResponse.toDetailState(speciesResponse)
            Response.success(pokemonDetail)
        }
    }
}