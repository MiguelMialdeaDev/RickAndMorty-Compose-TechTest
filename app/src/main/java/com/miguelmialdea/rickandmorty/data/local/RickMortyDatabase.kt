package com.miguelmialdea.rickandmorty.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.miguelmialdea.rickandmorty.data.local.dao.CharacterDao
import com.miguelmialdea.rickandmorty.data.local.entity.CharacterEntity

@Database(
    entities = [CharacterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RickMortyDatabase: RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}