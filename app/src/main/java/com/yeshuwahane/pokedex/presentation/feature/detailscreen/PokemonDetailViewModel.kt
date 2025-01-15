package com.yeshuwahane.pokedex.presentation.feature.detailscreen

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

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    val repository: PokemonRepositoryImpl
) : ViewModel() {

    private val _pokemonDetailState = MutableStateFlow(PokemonDetailState(DataResource.initial()))
    val pokemonDetailState = _pokemonDetailState
        .onStart {

        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PokemonDetailState(DataResource.initial())
        )


    fun getPokemonDetail(id: Int) {
        viewModelScope.launch {
            val pokemonDetail = repository.getPokemonDetails(id)
            _pokemonDetailState.update {
                it.copy(pokemonDetail)
            }
        }
    }


}