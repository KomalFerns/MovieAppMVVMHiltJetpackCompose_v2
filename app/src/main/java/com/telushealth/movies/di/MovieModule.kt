package com.telushealth.movies.di

import android.content.Context
import com.telushealth.movies.BuildConfig
import com.telushealth.movies.data.movie.MovieRemoteDataSource
import com.telushealth.movies.data.movie.MovieRepository
import com.telushealth.movies.data.movie.TmdbApiService

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

// Dagger Hilt module for movie feature dependencies

@Module
@InstallIn(SingletonComponent::class)
object MovieModule {
    private const val TMDB_BASE_URL = "https://api.themoviedb.org/3/"
    
    // NOTE: In production, this should be stored securely (e.g., BuildConfig, local.properties, or secrets)
    // For the challenge, users need to add their TMDB API key to local.properties as: tmdb.api.key=YOUR_API_KEY
  //  private const val API_KEY_PLACEHOLDER = "cffb8ee3c9d5b92aed87d70e5a07fb09"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    @TmdbApiKey
    fun provideTmdbApiKey(): String {
        return BuildConfig.TMDB_API_KEY
    }

    @Provides
    @Singleton
    fun provideTmdbApiService(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): TmdbApiService {
        return Retrofit.Builder()
            .baseUrl(TMDB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
            .create(TmdbApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideMovieRemoteDataSource(
        apiService: TmdbApiService,
        @TmdbApiKey apiKey: String,
    ): MovieRemoteDataSource {
        return MovieRemoteDataSource(apiService, apiKey)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(
        remoteDataSource: MovieRemoteDataSource,
    ): MovieRepository {
        return MovieRepository(remoteDataSource)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TmdbApiKey
