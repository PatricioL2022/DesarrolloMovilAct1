package viu.wearables.speechtotext.data

import androidx.room.Database
import androidx.room.RoomDatabase
import viu.wearables.speechtotext.presentation.models.History

/**
 * Base de datos para almacenar el historial
 * de audios procesados
 */
@Database(entities = [History::class], version = 1)
abstract class HistoryDatabase : RoomDatabase(){
    abstract val dao:HistoryDAO

    companion object {
        const val DATABASE_NAME = "history.db"
    }
}