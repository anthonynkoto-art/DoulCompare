package com.doulcompare.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.doulcompare.app.data.DataRepository
import com.doulcompare.app.ui.screens.DoulCompareApp
import com.doulcompare.app.ui.theme.DoulCompareTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Chargement du catalogue complet (2313 produits, 5 enseignes,
        // ~8800 offres de prix) depuis les fichiers JSON embarqués.
        val catalogue = DataRepository.load(applicationContext)

        setContent {
            DoulCompareTheme {
                DoulCompareApp(catalogue)
            }
        }
    }
}
