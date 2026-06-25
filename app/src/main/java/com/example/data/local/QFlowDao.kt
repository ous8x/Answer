package com.example.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface QFlowDao {
    @Query("SELECT * FROM answer_sheets ORDER BY createdAt DESC")
    fun getAllSheets(): Flow<List<AnswerSheetEntity>>

    @Query("SELECT * FROM answer_sheets WHERE id = :id")
    suspend fun getSheetById(id: Int): AnswerSheetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSheet(sheet: AnswerSheetEntity): Long

    @Delete
    suspend fun deleteSheet(sheet: AnswerSheetEntity)

    @Query("SELECT * FROM sheet_rows WHERE sheetId = :sheetId ORDER BY questionNumber ASC")
    fun getRowsForSheet(sheetId: Int): Flow<List<SheetRowEntity>>
    
    @Query("SELECT * FROM sheet_rows WHERE sheetId = :sheetId ORDER BY questionNumber ASC")
    suspend fun getRowsForSheetSync(sheetId: Int): List<SheetRowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRows(rows: List<SheetRowEntity>)

    @Update
    suspend fun updateRow(row: SheetRowEntity)
}
