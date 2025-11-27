package com.rick.newscasestudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.rick.newscasestudy.ui.navigation.NewsNavGraph
import com.rick.newscasestudy.ui.theme.NewsCaseStudyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            val systemDarkTheme = isSystemInDarkTheme()
            var isDarkTheme by rememberSaveable { mutableStateOf(systemDarkTheme) }

            NewsCaseStudyTheme(darkTheme = isDarkTheme) {
                NewsNavGraph(
                    onThemeToggle = { isDarkTheme = !isDarkTheme },
                    isDarkTheme = isDarkTheme
                )
            }
        }
    }
}