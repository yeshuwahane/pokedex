package com.yeshuwahane.pokedex.presentation.feature.detailscreen

import com.yeshuwahane.pokedex.data.util.DataResource


data class EvoluationDetailState(val evolutions: DataResource<EvolutionChainState>)


data class EvolutionChainState(
    val id: Int,
    val babyTriggerItem: Any?,
    val chain: EvolutionChainNodeState
)

data class EvolutionChainNodeState(
    val speciesName: String,
    val isBaby: Boolean,
    val evolutionDetails: List<EvolutionState>,
    val evolvesTo: List<EvolutionChainNodeState>
)

data class EvolutionState(
    val minLevel: Int,
    val triggerName: String,
    val timeOfDay: String
)
