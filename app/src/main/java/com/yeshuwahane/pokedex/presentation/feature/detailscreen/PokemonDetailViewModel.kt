package com.yeshuwahane.pokedex.presentation.feature.detailscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeshuwahane.pokedex.data.repositoryimpl.PokemonRepositoryImpl
import com.yeshuwahane.pokedex.data.util.DataResource
import com.yeshuwahane.pokedex.domain.usecase.PlaySoundUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    val repository: PokemonRepositoryImpl,
    val playSoundUseCase: PlaySoundUseCase
) : ViewModel() {

    private val _pokemonDetailState = MutableStateFlow(PokemonDetailState(DataResource.initial()))
    val pokemonDetailState = _pokemonDetailState.asStateFlow()


    fun getPokemonDetail(id: Int) {
        viewModelScope.launch {
            val pokemonDetail = repository.getPokemonDetails(id)
            Log.d("PokemonDetailViewModel", "Pokemon Detail: $pokemonDetail")
            _pokemonDetailState.update {
                it.copy(pokemonDetail)
            }
        }
    }

    fun playSound(soundUrl: String) {
        playSoundUseCase.execute(soundUrl)
    }


}