package com.gorbachevs.foodly.data.di

import android.content.Context
import androidx.room.Room
import com.gorbachevs.foodly.data.local.FoodlyDao
import com.gorbachevs.foodly.data.local.FoodlyDatabase
import com.gorbachevs.foodly.data.remote.FoodlyApi
import com.gorbachevs.foodly.data.remote.interceptor.loggingInterceptor
import com.gorbachevs.foodly.data.repository.BookmarkRepositoryImpl
import com.gorbachevs.foodly.data.repository.RecipeRepositoryImpl
import com.gorbachevs.foodly.domain.repository.BookmarkRepository
import com.gorbachevs.foodly.domain.repository.RecipeRepository
import com.gorbachevs.foodly.utils.Constants.BASE_URL
import com.gorbachevs.foodly.utils.Constants.RETROFIT_CACHE_FOLDER
import com.gorbachevs.foodly.utils.Constants.RETROFIT_CACHE_SIZE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .cache(Cache(context.cacheDir.resolve(RETROFIT_CACHE_FOLDER), RETROFIT_CACHE_SIZE))
            .addInterceptor(loggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideFoodlyApi(retrofit: Retrofit): FoodlyApi = retrofit.create(FoodlyApi::class.java)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): FoodlyDatabase =
        Room.databaseBuilder(
            context,
            FoodlyDatabase::class.java,
            FoodlyDatabase.NAME
        ).build()

    @Provides
    @Singleton
    fun provideFoodlyDao(foodlyDatabase: FoodlyDatabase): FoodlyDao = foodlyDatabase.dao

    @Provides
    @Singleton
    fun provideRecipeRepository(foodlyApi: FoodlyApi): RecipeRepository =
        RecipeRepositoryImpl(foodlyApi = foodlyApi)

    @Provides
    @Singleton
    fun provideBookmarkRepository(foodlyDao: FoodlyDao): BookmarkRepository =
        BookmarkRepositoryImpl(foodlyDao = foodlyDao)
}
