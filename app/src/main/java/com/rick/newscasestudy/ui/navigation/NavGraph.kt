package com.rick.newscasestudy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rick.newscasestudy.ui.screens.article.ArticleScreen
import com.rick.newscasestudy.ui.screens.news.NewsScreen
import com.rick.newscasestudy.ui.screens.news.NewsViewModel
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import androidx.hilt.navigation.compose.hiltViewModel

sealed class Screen(val route: String) {
    object News : Screen("news")
    object Article : Screen("article/{url}") {
        fun createRoute(url: String): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return "article/$encodedUrl"
        }
    }
}

@Composable
fun NewsNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.News.route) {
        composable(Screen.News.route) {
            NewsScreen(
                onArticleClick = { url ->
                    navController.navigate(Screen.Article.createRoute(url))
                }
            )
        }
        composable(
            route = Screen.Article.route,
            arguments = listOf(navArgument("url") { type = NavType.StringType })
        ) { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            ArticleScreen(url = url)
        }
    }
}
