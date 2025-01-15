package com.yeshuwahane.pokedex.presentation.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeshuwahane.pokedex.data.repositoryimpl.PokemonRepositoryImpl
import com.yeshuwahane.pokedex.data.util.DataResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.take
import kotlin.collections.takeLast


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repositoryImpl: PokemonRepositoryImpl
): ViewModel()  {
    private val _featuredPokemonState = MutableStateFlow(PokemonListState(DataResource.initial()))
    val featuredPokemonState = _featuredPokemonState
        .onStart {
            loadPokemon()
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PokemonListState(DataResource.initial())
        )
    private val _trendingPokemonState = MutableStateFlow(PokemonListState(DataResource.initial()))
    val trendingPokemonState = _trendingPokemonState
        .onStart {
            loadPokemon()
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PokemonListState(DataResource.initial())
        )
    private val _newestPokemonState = MutableStateFlow(PokemonListState(DataResource.initial()))
    val newestPokemonState = _newestPokemonState
        .onStart {
            loadPokemon()
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PokemonListState(DataResource.initial())
        )





    private fun loadPokemon(){
        viewModelScope.launch{
            val allPokemon = repositoryImpl.getPokemonList(90, 0)
            val trendingPokemon = DataResource.success(allPokemon.data?.take(30) ?: emptyList())
            val featuredPokemon = DataResource.success(allPokemon.data?.takeLast(30) ?: emptyList())
            val newestPokemon = DataResource.success(allPokemon.data?.shuffled()?.take(30) ?: emptyList())

            _trendingPokemonState.update {
                it.copy(trendingPokemon)
            }
            _featuredPokemonState.update {
                it.copy(featuredPokemon)
            }
            _newestPokemonState.update {
                it.copy(newestPokemon)
            }
        }
    }







}

data class PokemonListState(val pokemonList: DataResource<List<PokemonState>> = DataResource.initial())



data class PokemonState(val name: String, val url: String,val icon: String,val gif: String,val id: Int)
