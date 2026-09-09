package com.example.expensetracker.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.AppDatabase
import com.example.expensetracker.data.CategoryTotal
import com.example.expensetracker.data.Expense
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).expenseDao()

    val expenses: StateFlow<List<Expense>> = dao.getAll()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val monthRange: Pair<Long, Long> = run {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_MONTH, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val start = cal.timeInMillis
        cal.add(Calendar.MONTH, 1)
        cal.add(Calendar.MILLISECOND, -1)
        val end = cal.timeInMillis
        start to end
    }

    val monthTotal: StateFlow<Double> = dao.getTotalBetween(monthRange.first, monthRange.second)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val categoryTotals: StateFlow<List<CategoryTotal>> =
        dao.getCategoryTotalsBetween(monthRange.first, monthRange.second)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addExpense(amount: Double, category: String, note: String, date: Long) {
        viewModelScope.launch {
            dao.insert(Expense(amount = amount, category = category, note = note, date = date))
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch { dao.delete(expense) }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch { dao.update(expense) }
    }
}
