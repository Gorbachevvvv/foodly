package com.gorbachevs.foodly.presentation.screen.root

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gorbachevs.foodly.presentation.common.theme.FoodlyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodlyTheme {
                RootScreen()
            }
        }
    }
}
