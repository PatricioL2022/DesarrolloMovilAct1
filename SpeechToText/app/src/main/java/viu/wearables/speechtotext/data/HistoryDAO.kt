package viu.wearables.speechtotext.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import viu.wearables.speechtotext.presentation.models.History

@Dao
interface HistoryDAO {
    @Query("SELECT * FROM History")
    fun getHistories() : Flow<List<History>>

    @Query("SELECT * FROM History WHERE ID = :id")
    suspend fun getHistory(id:Int) : History?

    @Upsert
    suspend fun upsertHistory(history : History)

    @Delete
    suspend fun deleteHistory(history : History)
}