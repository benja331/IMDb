package com.globant.imdb.domain.moviesUseCases

import com.globant.imdb.data.IMDbRepository
import com.globant.imdb.data.database.entities.movie.CategoryType
import com.globant.imdb.data.database.entities.movie.toDatabase
import com.globant.imdb.domain.model.MovieItem
import javax.inject.Inject

class GetNowPlayingMoviesUseCase @Inject constructor( private val repository:IMDbRepository ) {
    suspend operator fun invoke():List<MovieItem> {
        val movies = repository.getNowPlayingMoviesFromApi()

        val category = CategoryType.NOW_PLAYING_MOVIES
        return if ( movies.isNotEmpty() ) {
            repository.clearMoviesByCategory(category)
            repository.insertMoviesToCategory( movies.map { it.toDatabase() }, category )
            movies
        } else {
            repository.getMoviesByCategoryFromDatabase(category)
        }
    }
}