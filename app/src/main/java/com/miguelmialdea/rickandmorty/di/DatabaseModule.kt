package com.miguelmialdea.rickandmorty.di

import android.content.Context
import androidx.room.Room
import com.miguelmialdea.rickandmorty.data.local.RickMortyDatabase
import com.miguelmialdea.rickandmorty.data.local.dao.CharacterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideRickMortyDatabase(
        @ApplicationContext context: Context
    ): RickMortyDatabase {
        return Room.databaseBuilder(
            context,
            RickMortyDatabase::class.java,
            "rick_morty_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideCharacterDao(database: RickMortyDatabase): CharacterDao {
        return database.characterDao()
    }
}