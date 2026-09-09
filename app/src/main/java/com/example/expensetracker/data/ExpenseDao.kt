package com.example.expensetracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses ORDER BY date DESC, id DESC")
    fun getAll(): Flow<List<Expense>>

    @Insert
    suspend fun insert(expense: Expense): Long

    @Update
    suspend fun update(expense: Expense)

    @Delete
    suspend fun delete(expense: Expense)

    @Query("SELECT COALESCE(SUM(amount), 0) FROM expenses WHERE date BETWEEN :start AND :end")
    fun getTotalBetween(start: Long, end: Long): Flow<Double>

    @Query("SELECT category, SUM(amount) as total FROM expenses WHERE date BETWEEN :start AND :end GROUP BY category ORDER BY total DESC")
    fun getCategoryTotalsBetween(start: Long, end: Long): Flow<List<CategoryTotal>>
}

data class CategoryTotal(
    val category: String,
    val total: Double
)
