package com.yeshuwahane.pokedex.presentation.feature.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.yeshuwahane.pokedex.R
import com.yeshuwahane.pokedex.data.util.DataResource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun HomeScreen(navController: NavController) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val featuredPokemon by viewModel.featuredPokemonState.collectAsStateWithLifecycle()
    val newestPokemon by viewModel.newestPokemonState.collectAsStateWithLifecycle()
    val trendingPokemon by viewModel.trendingPokemonState.collectAsStateWithLifecycle()

    val tabs = listOf("Popular", "Trending", "Newest")
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { tabs.size }
    )
    val coroutineScope = rememberCoroutineScope()

    var selectedPokemon by remember { mutableStateOf<PokemonState?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "PokeDex",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            color = Color.White
        )
        Image(
            painter = painterResource(R.drawable.pokeball),
            contentDescription = "Pokeball",
            modifier = Modifier
                .size(64.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Crop
        )

        // Tabs with Swipable Pager
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Black,
            contentColor = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            color = if (pagerState.currentPage == index) Color.White else Color.Gray
                        )
                    }
                )
            }
        }

        // Pager Content
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            when (page) {
                0 -> PokemonListScreen(
                    featuredPokemon.pokemonList,
                    selectedPokemon,
                    { selectedPokemon = it },
                    navigateToDetailScreen = {
                        Log.d("homeScreen","id: $it")
                        navController.navigate("detail/$it")

                    }
                )

                1 -> PokemonListScreen(
                    trendingPokemon.pokemonList,
                    selectedPokemon,
                    { selectedPokemon = it },
                    navigateToDetailScreen = {
                        navController.navigate("detail/$it")
                    })

                2 -> PokemonListScreen(
                    newestPokemon.pokemonList,
                    selectedPokemon,
                    { selectedPokemon = it },
                    navigateToDetailScreen = {
                        navController.navigate("detail/$it")
                    })
            }
        }
    }
}


@Composable
fun PokemonListScreen(
    pokemonListState: DataResource<List<PokemonState>>,
    selectedPokemon: PokemonState?,
    onPokemonClick: (PokemonState) -> Unit,
    navigateToDetailScreen: (id: Int) -> Unit
) {
    val resource = pokemonListState

    if (resource.isLoading()) {
        // Loading state
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color.White)
        }
    } else if (resource.isSuccess()) {
        // Success state
        val pokemonList = resource.data ?: emptyList()
        if (pokemonList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                items(pokemonList) { pokemon ->
                    PokemonListItem(
                        pokemon = pokemon,
                        isSelected = pokemon == selectedPokemon,
                        onClick = { onPokemonClick(pokemon) },
                        navigateToDetailScreen = {
                            navigateToDetailScreen.invoke(it)
                        }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No Pokémon found!",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    } else if (resource.isError()) {
        // Error state
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = resource.error?.localizedMessage ?: "An error occurred!",
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

