package com.sanxmon.ceki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import com.sanxmon.ceki.data.local.DataStoreThemeRepository
import com.sanxmon.ceki.ui.screen.CekiApp
import com.sanxmon.ceki.ui.theme.AppThemeProvider
import com.sanxmon.ceki.ui.theme.ThemeManager

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeManager = remember {
                ThemeManager(DataStoreThemeRepository(applicationContext))
            }
            AppThemeProvider(themeManager = themeManager) {
                CekiApp()
            }
        }
    }
}
