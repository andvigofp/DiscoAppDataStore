package com.example.discoappdatastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.discoappdatastore.ui.DiscoViewModelFactory
import com.example.discoappdatastore.ui.navigation.ListaDiscosApp
import com.example.discoappdatastore.ui.state.DiscoViewModel
import com.example.discoappdatastore.ui.theme.DiscoAppDataStoreTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: DiscoViewModel = viewModel(
                factory = DiscoViewModelFactory(application)
            )
            DiscoAppDataStoreTheme {
                ListaDiscosApp(viewModel = viewModel)
            }
        }
    }
}