package com.example.todoappcompose.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoappcompose.R
import com.example.todoappcompose.ui.theme.LOGO_HEIGHT
import com.example.todoappcompose.ui.theme.ToDoAppComposeTheme
import com.example.todoappcompose.ui.theme.splashScreenBackground
import com.example.todoappcompose.util.Constants
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navigateToListScreen: () -> Unit
) {
    LaunchedEffect(key1 = true) {
        delay(Constants.SPLASH_SCREEN_DELAY)
        navigateToListScreen()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.splashScreenBackground),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier
                .size(LOGO_HEIGHT),
            painter = painterResource(id = getLogo()),
            contentDescription = stringResource(id = R.string.to_do_logo)
        )
    }
}

@Composable
fun getLogo() = if (isSystemInDarkTheme()) R.drawable.ic_logo_dark else R.drawable.ic_logo_light

@Preview
@Composable
fun PreviewSplashScreen() {
    SplashScreen(
        navigateToListScreen = {}
    )
}

@Preview
@Composable
fun PreviewDarkModeSplashScreen() {
    ToDoAppComposeTheme(darkTheme = true) {
        SplashScreen(
            navigateToListScreen = {}
        )
    }
}