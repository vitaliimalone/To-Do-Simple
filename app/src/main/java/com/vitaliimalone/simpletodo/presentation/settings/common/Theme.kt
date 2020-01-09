package com.vitaliimalone.simpletodo.presentation.settings.common

import com.vitaliimalone.simpletodo.R
import com.vitaliimalone.simpletodo.presentation.utils.Res

enum class Theme {
    PALE_GREEN {
        override fun getStyleResId() = R.style.AppTheme_PaleGreen
        override fun getTitle() = Res.string(R.string.theme_name_pale_green)
    },
    DARK {
        override fun getStyleResId() = R.style.AppTheme_Dark
        override fun getTitle() = Res.string(R.string.theme_name_dark)
    };

    abstract fun getStyleResId(): Int
    abstract fun getTitle(): String
}