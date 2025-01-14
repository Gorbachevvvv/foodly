package com.gorbachevs.foodly.presentation.di

import com.gorbachevs.foodly.domain.repository.BookmarkRepository
import com.gorbachevs.foodly.domain.repository.RecipeRepository
import com.gorbachevs.foodly.presentation.screen.bookmarks.BookmarksViewModel
import com.gorbachevs.foodly.presentation.screen.details.DetailsViewModel
import com.gorbachevs.foodly.presentation.screen.home.HomeViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {

    @Provides
    @ViewModelScoped
    fun provideHomeViewModel(recipeRepository: RecipeRepository): HomeViewModel =
        HomeViewModel(recipeRepository)

    @Provides
    @ViewModelScoped
    fun provideDetailsViewModel(bookmarkRepository: BookmarkRepository): DetailsViewModel =
        DetailsViewModel(bookmarkRepository)

    @Provides
    @ViewModelScoped
    fun provideBookmarksViewModel(bookmarkRepository: BookmarkRepository): BookmarksViewModel =
        BookmarksViewModel(bookmarkRepository)
}
