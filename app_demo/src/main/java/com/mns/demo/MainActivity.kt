package com.mns.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

/**
 * Ponto de entrada do app de demonstração.
 *
 * Faz exatamente duas coisas: liga o modo edge-to-edge (os componentes do MNS
 * já lidam com os insets) e monta [MnsDemoApp]. Nenhuma lógica de tema mora
 * aqui — quem manda no tema é o `ThemeController` dentro da composição.
 */
public class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent { MnsDemoApp() }
    }
}
