package com.yeshuwahane.pokedex.data.network

import com.yeshuwahane.pokedex.data.model.PokemonResponse
import com.yeshuwahane.pokedex.data.model.PokemonSpeciesResponse
import com.yeshuwahane.pokedex.data.model.pokemondetailresponse.PokemonDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface PokemonApi {
    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int, @Query("offset") offset: Int): PokemonResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(@Path("id") id: Int): PokemonDetailResponse

    @GET("pokemon-species/{id}")
    suspend fun getPokemonSpecies(@Path("id") id: Int): PokemonSpeciesResponse
}