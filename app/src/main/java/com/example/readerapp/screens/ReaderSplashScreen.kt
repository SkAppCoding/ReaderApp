package com.example.readerapp.screens

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.readerapp.components.ReaderLogo
import com.example.readerapp.navigation.ReaderScreens
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavHostController) {

    val scale = remember {
        Animatable(0f)
    }
    LaunchedEffect(key1 = true) {
        scale.animateTo(
            0.9f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(4f).getInterpolation(it)
                })
        )
        delay(1000L)

        navController.navigate(ReaderScreens.LoginScreen.name)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center)
        {


            Surface(
                modifier = Modifier
                    .padding(15.dp)
                    .size(330.dp)
                    .scale(scale.value),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)
            ) {
                Column(
                    modifier = Modifier.padding(1.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    ReaderLogo(color = MaterialTheme.colorScheme.onPrimary)
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(
                        text = "\"Read. Change. Yourself\"",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Thin),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}