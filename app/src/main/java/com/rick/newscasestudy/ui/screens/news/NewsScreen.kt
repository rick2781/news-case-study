package com.rick.newscasestudy.ui.screens.news

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey

import com.rick.newscasestudy.ui.components.ArticleItem
import com.rick.newscasestudy.model.Article

import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsScreen(
    viewModel: NewsViewModel = hiltViewModel(),
    onArticleClick: (String) -> Unit,
    onThemeToggle: () -> Unit,
    isDarkTheme: Boolean
) {
    val articles: LazyPagingItems<Article> = viewModel.articles.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("News App") },
                actions = {
                    IconButton(onClick = onThemeToggle) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }
                    IconButton(onClick = {
                        viewModel.getTopHeadlines()
                        articles.refresh()
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    viewModel.updateSearchQuery(it)
                    if (it.isNotEmpty()) {
                        viewModel.searchNews(it)
                    } else {
                        viewModel.getTopHeadlines()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                placeholder = { Text("Search news by keword") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            Box(modifier = Modifier.fillMaxSize()) {
                if (articles.loadState.refresh is LoadState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(
                            count = articles.itemCount,
                            key = articles.itemKey { it.url },
                            contentType = articles.itemContentType { "article" }
                        ) { index ->
                            val article = articles[index]
                            if (article != null) {
                                ArticleItem(
                                    article = article,
                                    onClick = onArticleClick
                                )
                            }
                        }


                        if (articles.loadState.append is LoadState.Loading) {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .wrapContentWidth(Alignment.CenterHorizontally)
                                )
                            }
                        }

                        if (articles.loadState.append is LoadState.Error) {
                            val error = articles.loadState.append as LoadState.Error
                            item {
                                Text(
                                    text = error.error.localizedMessage ?: "Error loading more",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }

                    if (articles.loadState.refresh is LoadState.Error) {
                        val error = articles.loadState.refresh as LoadState.Error
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = error.error.localizedMessage ?: "Error loading news",
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(onClick = { articles.retry() }) {
                                Text("Retry")
                            }
                        }
                    }
                }
            }
        }
    }
}
