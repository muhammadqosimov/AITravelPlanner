package com.example.aitravelplanner.di

import android.content.Context
import androidx.room.Room
import com.example.aitravelplanner.data.local.dao.DestinationDao
import com.example.aitravelplanner.data.local.dao.TripDao
import com.example.aitravelplanner.data.local.database.AppDatabase
import com.example.aitravelplanner.util.Constants
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
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            Constants.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideTripDao(database: AppDatabase): TripDao = database.tripDao()

    @Provides
    fun provideDestinationDao(database: AppDatabase): DestinationDao = database.destinationDao()
}
