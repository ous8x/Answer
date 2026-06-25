package com.example

import android.app.Application
import androidx.room.Room
import com.example.data.QFlowRepository
import com.example.data.local.AppDatabase
import com.example.data.local.SettingsRepository
import com.example.data.local.dataStore

class QFlowApplication : Application() {
    
    lateinit var database: AppDatabase
    lateinit var repository: QFlowRepository
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate() {
        super.onCreate()
        
        database = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "qflow_database"
        )
        .fallbackToDestructiveMigration()
        .build()
        
        repository = QFlowRepository(database.qFlowDao())
        settingsRepository = SettingsRepository(dataStore)
    }
}
