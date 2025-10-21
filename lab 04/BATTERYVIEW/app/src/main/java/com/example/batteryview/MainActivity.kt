package com.example.batteryview

import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.batteryview.ui.theme.BATTERYVIEWTheme

class MainActivity : ComponentActivity() {
    
    companion object {
        private const val TAG = "MainActivity"
    }
    
    private lateinit var batteryReceiver: BatteryReceiver
    private var batteryInfo by mutableStateOf(
        BatteryInfo(
            percentage = 0,
            status = "Desconocido",
            powerSource = "Desconocido",
            temperature = 0.0,
            voltage = 0.0,
            health = "Desconocida",
            technology = "Desconocida"
        )
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Inicializar el BroadcastReceiver
        batteryReceiver = BatteryReceiver { newBatteryInfo ->
            batteryInfo = newBatteryInfo
        }
        
        setContent {
            BATTERYVIEWTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BatteryInfoScreen(
                        batteryInfo = batteryInfo,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        
        // Registrar el BroadcastReceiver
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        
        registerReceiver(batteryReceiver, intentFilter)
        Log.d(TAG, "BroadcastReceiver registrado satisfactoriamente en onResume()")
    }
    
    override fun onPause() {
        super.onPause()
        
        // Desregistrar el BroadcastReceiver
        try {
            unregisterReceiver(batteryReceiver)
            Log.d(TAG, "BroadcastReceiver desregistrado satisfactoriamente en onPause()")
        } catch (e: IllegalArgumentException) {
            Log.w(TAG, "BroadcastReceiver ya estaba desregistrado")
        }
    }
}

@Composable
fun BatteryInfoScreen(batteryInfo: BatteryInfo, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Monitor de Batería",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Card(
            modifier = Modifier.fillMaxSize(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BatteryInfoItem("Nivel de Batería", "${batteryInfo.percentage}%")
                BatteryInfoItem("Estado", batteryInfo.status)
                BatteryInfoItem("Fuente de Energía", batteryInfo.powerSource)
                BatteryInfoItem("Temperatura", "${batteryInfo.temperature}°C")
                BatteryInfoItem("Voltaje", "${batteryInfo.voltage}V")
                BatteryInfoItem("Salud", batteryInfo.health)
                BatteryInfoItem("Tecnología", batteryInfo.technology)
            }
        }
    }
}

@Composable
fun BatteryInfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}