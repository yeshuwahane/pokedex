package com.yeshuwahane.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yeshuwahane.pokedex.presentation.feature.home.HomeScreen
import com.yeshuwahane.pokedex.presentation.navigation.MainNav
import com.yeshuwahane.pokedex.presentation.ui.theme.PokedexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokedexTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize().statusBarsPadding()
                ) { padding ->

                    Box(modifier = Modifier.padding(padding).fillMaxSize(),contentAlignment = Alignment.Center){
                        MainNav()

                    }

                }
            }
        }
    }
}

