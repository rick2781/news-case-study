package com.rick.newscasestudy.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun NewsHeader(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit,
    onRefresh: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "News App",
            style = MaterialTheme.typography.titleLarge
        )
        Row {
            IconButton(onClick = onThemeToggle) {
                AnimatedContent(
                    targetState = isDarkTheme,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(300)) + scaleIn() togetherWith
                                fadeOut(animationSpec = tween(300)) + scaleOut()
                    },
                    label = "Theme Toggle"
                ) { dark ->
                    Icon(
                        imageVector = if (dark) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Toggle Theme"
                    )
                }
            }
            val rotation = remember { Animatable(0f) }
            val scope = rememberCoroutineScope()

            IconButton(onClick = {
                scope.launch {
                    rotation.animateTo(
                        targetValue = 360f,
                        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
                    )
                    rotation.snapTo(0f)
                }
                onRefresh()
            }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    modifier = Modifier.rotate(rotation.value)
                )
            }
        }
    }
}
