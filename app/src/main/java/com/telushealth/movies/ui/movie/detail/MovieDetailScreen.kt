package com.telushealth.movies.ui.movie.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.telushealth.movies.R
import com.telushealth.movies.data.movie.Movie
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

//Compose screen for displaying movie details Shows movie original_title, poster, overview, and similar movies

@Composable
fun MovieDetailScreen(
    state: MovieDetailState,
    onSimilarMovieClick: (Int, String) -> Unit = { _, _ -> },
) {
    when {
        state.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.semantics {
                        contentDescription = "Loading movie details"
                    },
                )
            }
        }
        state.errorMessage != null -> {
            Box(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = state.errorMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorResource(id = R.color.textLabels),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.semantics {
                            contentDescription = "Error message: ${state.errorMessage}"
                        },
                    )
                }
            }
        }
        state.movie != null -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                item {
                    MovieHeader(movie = state.movie)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                item {
                    MovieOverview(movie = state.movie)
                }
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
                if (state.similarMovies.isNotEmpty()) {
                    item {
                        Text(
                            text = stringResource(R.string.similar_movies),
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .semantics {
                                    contentDescription = "Similar movies section"
                                },
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    item {
                        SimilarMoviesRow(
                            movies = state.similarMovies,
                            onMovieClick = onSimilarMovieClick,
                            isLoading = state.isLoadingSimilar,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieHeader(movie: Movie) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = movie.original_title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Movie original_title: ${movie.original_title}"
                },
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))
        MoviePoster(posterPath = movie.poster_path)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun MoviePoster(posterPath: String?) {
    val posterUrl = if (!posterPath.isNullOrEmpty()) {
        "https://image.tmdb.org/t/p/w500$posterPath"
    } else {
        null
    }

    Box(
        modifier = Modifier
            .width(300.dp)
            .height(450.dp),
        contentAlignment = Alignment.Center,
    ) {
        if (posterUrl != null) {
            GlideImage(
                model = posterUrl,
                contentDescription = "Movie poster",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit,
                requestBuilderTransform = { requestBuilder ->
                    requestBuilder.apply(
                        RequestOptions()
                            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC),
                    )
                },
            )
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.no_overview_available),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.semantics {
                        contentDescription = "No poster available"
                    },
                )
            }
        }
    }
}

@Composable
private fun MovieOverview(movie: Movie) {
    val noOverviewText = stringResource(R.string.no_overview_available)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = stringResource(R.string.overview),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.semantics {
                contentDescription = "Movie overview section"
            },
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.overview.ifEmpty { noOverviewText },
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.semantics {
                contentDescription = "Movie overview: ${movie.overview.ifEmpty { noOverviewText }}"
            },
        )
    }
}

@Composable
private fun SimilarMoviesRow(
    movies: List<Movie>,
    onMovieClick: (Int, String) -> Unit,
    isLoading: Boolean,
) {
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier.semantics {
                    contentDescription = "Loading similar movies"
                },
            )
        }
    } else {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            items(
                items = movies,
                key = { it.id },
            ) { movie ->
                SimilarMovieItem(
                    movie = movie,
                    onClick = { onMovieClick(movie.id, movie.original_title) },
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun SimilarMovieItem(
    movie: Movie,
    onClick: () -> Unit,
) {
    val posterUrl = if (!movie.poster_path.isNullOrEmpty()) {
        "https://image.tmdb.org/t/p/w200${movie.poster_path}"
    } else {
        null
    }

    Card(
        onClick = onClick,
        modifier = Modifier
            .width(120.dp)
            .height(180.dp)
            .semantics {
                contentDescription = "Similar movie: ${movie.original_title}"
            },
        colors = CardDefaults.cardColors(),
    ) {
        Box {
            if (posterUrl != null) {
                GlideImage(
                    model = posterUrl,
                    contentDescription = "Poster for ${movie.original_title}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    requestBuilderTransform = { requestBuilder ->
                        requestBuilder.apply(
                            RequestOptions()
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC),
                        )
                    },
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = movie.original_title,
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp),
                    )
                }
            }
        }
    }
}
