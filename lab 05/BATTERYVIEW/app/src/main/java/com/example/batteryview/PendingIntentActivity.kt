package com.example.batteryview

import android.app.PendingIntent
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
import androidx.compose.material3.Button
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

class PendingIntentActivity : ComponentActivity() {
    
    companion object {
        private const val TAG = "PendingIntentActivity"
        private const val REQUEST_CODE = 1001
    }
    
    private lateinit var batteryReceiver: BatteryReceiver
    private var pendingIntent: PendingIntent? = null
    private var isReceiverRegistered = false
    
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
        
        // Crear el PendingIntent
        createPendingIntent()
        
        setContent {
            BATTERYVIEWTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PendingIntentBatteryScreen(
                        batteryInfo = batteryInfo,
                        isReceiverRegistered = isReceiverRegistered,
                        onRegisterClick = { registerReceiverWithPendingIntent() },
                        onUnregisterClick = { unregisterReceiverWithPendingIntent() },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
    
    private fun createPendingIntent() {
        val intent = Intent(this, BatteryReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(
            this,
            REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        Log.d(TAG, "PendingIntent creado con REQUEST_CODE: $REQUEST_CODE")
    }
    
    private fun registerReceiverWithPendingIntent() {
        if (!isReceiverRegistered) {
            val intentFilter = IntentFilter().apply {
                addAction(Intent.ACTION_BATTERY_CHANGED)
                addAction(Intent.ACTION_POWER_CONNECTED)
                addAction(Intent.ACTION_POWER_DISCONNECTED)
            }
            
            registerReceiver(batteryReceiver, intentFilter)
            isReceiverRegistered = true
            Log.d(TAG, "BroadcastReceiver registrado satisfactoriamente usando PendingIntent")
        } else {
            Log.w(TAG, "BroadcastReceiver ya está registrado")
        }
    }
    
    private fun unregisterReceiverWithPendingIntent() {
        if (isReceiverRegistered) {
            try {
                unregisterReceiver(batteryReceiver)
                isReceiverRegistered = false
                Log.d(TAG, "BroadcastReceiver desregistrado satisfactoriamente usando PendingIntent")
            } catch (e: IllegalArgumentException) {
                Log.w(TAG, "BroadcastReceiver ya estaba desregistrado")
                isReceiverRegistered = false
            }
        } else {
            Log.w(TAG, "BroadcastReceiver no está registrado")
        }
    }
    
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() - PendingIntent Activity")
    }
    
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() - PendingIntent Activity")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Asegurar que el receiver se desregistre al destruir la actividad
        if (isReceiverRegistered) {
            unregisterReceiverWithPendingIntent()
        }
        Log.d(TAG, "onDestroy() - PendingIntent Activity")
    }
}

@Composable
fun PendingIntentBatteryScreen(
    batteryInfo: BatteryInfo,
    isReceiverRegistered: Boolean,
    onRegisterClick: () -> Unit,
    onUnregisterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Monitor de Batería con PendingIntent",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        // Botones de control
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = onRegisterClick,
                enabled = !isReceiverRegistered
            ) {
                Text("Registrar BroadcastReceiver")
            }
            
            Button(
                onClick = onUnregisterClick,
                enabled = isReceiverRegistered
            ) {
                Text("Desregistrar BroadcastReceiver")
            }
            
            Text(
                text = if (isReceiverRegistered) "Estado: Registrado" else "Estado: No Registrado",
                fontSize = 14.sp,
                color = if (isReceiverRegistered) 
                    MaterialTheme.colorScheme.primary 
                else 
                    MaterialTheme.colorScheme.error
            )
        }
        
        // Información de la batería
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