package com.vitaliimalone.simpletodo.presentation.settings.common

import android.graphics.drawable.Drawable

// todo maybe change to enum
data class Settings(val settingsType: SettingsType, val icon: Drawable, val title: String, var subtitile: String = "")

enum class SettingsType { THEME, LANGUAGE, OVERDUE, ARCHIVE, RATE, INFO }
