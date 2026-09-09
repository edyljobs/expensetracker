# Expenses — Minimalist Offline Expense Tracker

A simple, fully offline Android expense tracker. Built with Kotlin, Jetpack Compose,
and Room (SQLite) — no internet permission, no accounts, no ads.

## Features
- Add an expense in one tap: amount, category chip, optional note — saved instantly to a local SQLite database (via Room).
- Scrollable expense list with this month's running total at the top.
- Swipe-free delete via a trash icon on each row.
- Summary tab: spending broken down by category with simple progress bars, for the current month.
- Minimal Material 3 UI: one accent color, generous whitespace, no clutter.
- 100% offline — all data lives in `expense_tracker.db` on the device. No network permission is even declared.

## Project structure
```
app/src/main/java/com/example/expensetracker/
  MainActivity.kt              — app shell, bottom nav (Expenses / Summary), FAB
  data/
    Expense.kt                 — Room entity (the expense row)
    ExpenseDao.kt               — SQL queries (insert/update/delete/totals)
    AppDatabase.kt              — Room database singleton
    Categories.kt               — fixed category list used by the add sheet
  ui/
    ExpenseViewModel.kt         — exposes expenses/totals as StateFlow, no UI logic touches SQLite directly
    screens/
      ExpenseListScreen.kt      — main list + month total
      AddExpenseSheet.kt        — bottom sheet form for adding an expense
      SummaryScreen.kt          — category breakdown for the month
    theme/                      — Color.kt, Type.kt, Theme.kt (Material 3 theming)
```

## How to build and run

1. **Install Android Studio** (Koala/2024.1 or newer) if you don't have it: https://developer.android.com/studio
2. **Open the project**: `File > Open`, select the `ExpenseTracker` folder (the one containing `settings.gradle.kts`).
3. Android Studio will offer to generate the Gradle wrapper automatically on first open — accept it, then let Gradle sync (this downloads the Android Gradle Plugin, Kotlin, Compose, and Room — needs internet the first time only, for tooling, not for the app itself).
4. Plug in an Android phone (USB debugging on) or start an emulator (API 26+).
5. Click **Run ▶**. The app installs and launches immediately.

No backend, no API keys, no configuration needed.

## Customizing
- **Categories**: edit the list in `data/Categories.kt`.
- **Currency format**: `NumberFormat.getCurrencyInstance()` uses the device's locale automatically. To force a specific currency, replace it with `NumberFormat.getCurrencyInstance(Locale("en", "PH"))` (or your locale of choice) in `ExpenseListScreen.kt` and `SummaryScreen.kt`.
- **Accent color**: change `Accent` in `ui/theme/Color.kt`.
- **App name/icon**: `res/values/strings.xml` (name) and `res/drawable/ic_launcher_foreground.xml` + `res/values/colors.xml` (icon).

## Notes on data
- The database file is private to the app (Android's standard app-sandboxed storage), so no other app can read it.
- Uninstalling the app deletes the data (standard Android behavior). If you want backup/restore or CSV export later, that's a straightforward addition to `ExpenseDao` + a share-intent — ask if you'd like that added.
