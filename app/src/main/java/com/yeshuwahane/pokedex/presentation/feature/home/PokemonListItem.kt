package com.yeshuwahane.pokedex.presentation.feature.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PokemonListItem(
    pokemon: PokemonState,
    isSelected: Boolean,
    onClick: () -> Unit,
    navigateToDetailScreen: (id: String) -> Unit
) {
    // Apply selected background color
    val cardBackgroundColor = if (isSelected) {
        Color(0xFF3D5AFE) // Selected card color (Blue, adjust as needed)
    } else {
        Color.Black // Default background for unselected items
    }

    val pokemonImage = if (isSelected){
        pokemon.gif
    } else{
        pokemon.icon
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .combinedClickable(
                enabled = true,
                onClick = { onClick() },
                onLongClick = {
                    navigateToDetailScreen.invoke(pokemon.id)
                }
            )
            .background(cardBackgroundColor)
            .clip(RoundedCornerShape(8.dp)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Pokémon Image
            Image(
                painter = rememberAsyncImagePainter(pokemonImage),
                contentDescription = "${pokemon.name} Icon",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Pokémon name and URL
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = pokemon.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (isSelected) Color.White else Color.Gray
                )
                Text(
                    text = pokemon.url, // Assuming this could be some additional detail
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) Color.White else Color.Gray
                )
            }
        }
    }
}
