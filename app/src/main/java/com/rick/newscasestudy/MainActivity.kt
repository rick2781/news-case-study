package com.rick.newscasestudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rick.newscasestudy.ui.navigation.NewsNavGraph
import com.rick.newscasestudy.ui.theme.NewsCaseStudyTheme
import com.rick.newscasestudy.ui.theme.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsStateWithLifecycle()

            NewsCaseStudyTheme(darkTheme = isDarkTheme) {
                NewsNavGraph(
                    onThemeToggle = { themeViewModel.toggleTheme() },
                    isDarkTheme = isDarkTheme
                )
            }
        }
    }
}