package com.photoai.editor.di

import com.photoai.editor.data.repository.AiImageProcessingRepositoryImpl
import com.photoai.editor.data.repository.ImageExportRepositoryImpl
import com.photoai.editor.data.repository.UserPreferencesRepositoryImpl
import com.photoai.editor.domain.repository.AiImageProcessingRepository
import com.photoai.editor.domain.repository.ImageExportRepository
import com.photoai.editor.domain.repository.UserPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for binding repository implementations
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserPreferencesRepository(
        impl: UserPreferencesRepositoryImpl
    ): UserPreferencesRepository

    @Binds
    @Singleton
    abstract fun bindAiImageProcessingRepository(
        impl: AiImageProcessingRepositoryImpl
    ): AiImageProcessingRepository

    @Binds
    @Singleton
    abstract fun bindImageExportRepository(
        impl: ImageExportRepositoryImpl
    ): ImageExportRepository
}
