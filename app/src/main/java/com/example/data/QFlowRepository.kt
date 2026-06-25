package com.example.data

import com.example.data.local.*
import kotlinx.coroutines.flow.Flow

class QFlowRepository(private val dao: QFlowDao) {

    val allSheets = dao.getAllSheets()

    suspend fun getSheetById(id: Int) = dao.getSheetById(id)

    suspend fun createSheet(title: String, qCount: Int, oCount: Int, labelStyle: LabelStyle): Int {
        val sheet = AnswerSheetEntity(
            title = title,
            questionCount = qCount,
            optionsCount = oCount,
            labelStyle = labelStyle
        )
        val sheetId = dao.insertSheet(sheet).toInt()
        
        val rows = (1..qCount).map { i ->
            SheetRowEntity(
                sheetId = sheetId,
                questionNumber = i
            )
        }
        dao.insertRows(rows)
        return sheetId
    }

    suspend fun deleteSheet(sheet: AnswerSheetEntity) = dao.deleteSheet(sheet)

    fun getRowsForSheet(sheetId: Int) = dao.getRowsForSheet(sheetId)
    
    suspend fun getRowsForSheetSync(sheetId: Int) = dao.getRowsForSheetSync(sheetId)

    suspend fun updateRow(row: SheetRowEntity) = dao.updateRow(row)
}
