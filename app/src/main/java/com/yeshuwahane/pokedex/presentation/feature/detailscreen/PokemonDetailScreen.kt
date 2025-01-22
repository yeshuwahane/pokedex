package com.yeshuwahane.pokedex.presentation.feature.detailscreen

import android.annotation.SuppressLint
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.TabRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch


@Composable
fun PokemonDetailScreen(
    id: Int,
    onBackClick: () -> Unit
) {
    val viewModel = hiltViewModel<PokemonDetailViewModel>()
    val pokemonDetail by viewModel.pokemonDetailState.collectAsStateWithLifecycle()
    val soundError by viewModel.soundError.collectAsStateWithLifecycle()
    val uiState = pokemonDetail.detailState.data

    // State to manage whether GIF or static sprite is displayed
    var showGif by remember { mutableStateOf(false) }

    // Snackbar Host State
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(soundError) {
        if (soundError != null) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(soundError!!)
                viewModel.clearError()
            }
        }
    }

    LaunchedEffect(id) {
        viewModel.getPokemonDetail(id)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues->

        // Main layout
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            // Top Bar
            DetailTopBar(onBackClick, showGif) { showGif = !showGif }

            if (uiState != null) {
                // Pokémon Detail Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    PokemonDetailContent(
                        uiState,
                        showGif,
                        playSound = {
                            viewModel.playSound(it)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                }
            } else {
                // Placeholder content while loading or if data is not available
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            }
        }
    }


}

@Composable
fun DetailTopBar(
    onBackClick: () -> Unit,
    showGif: Boolean,
    onToggleGif: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = { onBackClick() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Toggle button for GIF/Static Sprite
        IconButton(onClick = { onToggleGif() }) {
            Icon(
                imageVector = if (showGif) Icons.Filled.Image else Icons.Filled.VideoLibrary,
                contentDescription = if (showGif) "Show Static Sprite" else "Show Animation",
                tint = Color.White
            )
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun PokemonDetailContent(detailState: DetailState, showGif: Boolean, playSound: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val coroutineScope = rememberCoroutineScope()
        var selectedTabIndex by remember { mutableIntStateOf(0) }
        val tabTitles = listOf("Stats", "Moves", "Sounds")
        // Pager State for swipeable tabs
        val pagerState = rememberPagerState(initialPage = 0, pageCount = { tabTitles.size })

        val gifUrl =
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/showdown/${detailState.id}.gif"

        // Pokémon Image
        Image(
            painter = rememberAsyncImagePainter(if (showGif) gifUrl else detailState.spriteUrl),
            contentDescription = "${detailState.name} Sprite",
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pokémon Name
        Text(
            text = detailState.name,
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Pokémon Types
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            detailState.types.forEach { type ->
                Box(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Gray)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = type, color = Color.White)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pokémon Description
        Text(
            text = detailState.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Tabs
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = Color.Black,
            contentColor = Color.White
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(title, style = MaterialTheme.typography.bodySmall) }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            // Tab Content
            when (page) {
                0 -> StatsTab(
                    detailState.stats,
                    weight = detailState.weight,
                    height = detailState.height
                )

                1 -> MovesTab(detailState.moves)
                2 -> SoundsTab(
                    legacySoundUrl = detailState.legacySoundUrl,
                    latestSoundUrl = detailState.latestSoundUrl,
                    playSound = { playSound.invoke(it) }
                )
            }
        }


        // Stats Section
//        PokemonStats(detailState.stats)

        Spacer(modifier = Modifier.height(16.dp))

        // Weight and Height

    }
}

@Composable
fun PokemonStats(stats: List<StatDetail>, weight: Float, height: Float) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Base Stats",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))

        stats.forEach { stat ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = stat.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Animating progress and value together
                val animatedProgress = remember { Animatable(0f) }

                LaunchedEffect(stat.value) {
                    animatedProgress.animateTo(
                        targetValue = stat.value / 100f, // Assuming max value is 100
                        animationSpec = tween(
                            durationMillis = 1500,
                            easing = FastOutSlowInEasing
                        )
                    )
                }

                // Progress Bar
                LinearProgressIndicator(
                    progress = animatedProgress.value,
                    color = Color.Green,
                    backgroundColor = Color.DarkGray,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .weight(3f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Stat Value
                Text(
                    text = (animatedProgress.value * 100).toInt().toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            DetailMetric("Weight", "$weight kg")
            DetailMetric("Height", "$height m")
        }

    }
}

@Composable
fun DetailMetric(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = Color.White
        )
    }
}


@Composable
fun AboutTab(detailState: DetailState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "About ${detailState.name}",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = detailState.description,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )


    }
}

@Composable
fun StatsTab(stats: List<StatDetail>, weight: Float, height: Float) {
    PokemonStats(stats, weight, height)
}

@Composable
fun MovesTab(moves: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Moves",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        moves.forEach { move ->
            Text(
                text = move,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun SoundsTab(legacySoundUrl: String, latestSoundUrl: String, playSound: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sounds",
            style = MaterialTheme.typography.titleLarge,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Legacy Sound",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Button(
            onClick = { playSound(legacySoundUrl) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text(text = "Play Legacy Sound", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Latest Sound",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Button(
            onClick = { playSound(latestSoundUrl) },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
        ) {
            Text(text = "Play Latest Sound", color = Color.White)
        }
    }
}

