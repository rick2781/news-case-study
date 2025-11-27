package com.rick.newscasestudy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.rick.newscasestudy.ui.navigation.NewsNavGraph
import com.rick.newscasestudy.ui.theme.NewsCaseStudyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            NewsCaseStudyTheme {
                NewsNavGraph()
            }
        }
    }
}