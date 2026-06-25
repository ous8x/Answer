package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

enum class LabelStyle { ABCD, NUMBERS, TRUE_FALSE }

@Entity(tableName = "answer_sheets")
data class AnswerSheetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val questionCount: Int,
    val optionsCount: Int,
    val labelStyle: LabelStyle,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "sheet_rows",
    foreignKeys = [ForeignKey(entity = AnswerSheetEntity::class, parentColumns = ["id"], childColumns = ["sheetId"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("sheetId")]
)
data class SheetRowEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val sheetId: Int,
    val questionNumber: Int,
    val correctAnswer: String? = null,
    val userAnswer: String? = null
)
