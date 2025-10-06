package com.example.batteryview

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import android.util.Log

class BatteryReceiver(private val onBatteryInfoReceived: (BatteryInfo) -> Unit) : BroadcastReceiver() {
    
    companion object {
        private const val TAG = "BatteryReceiver"
    }
    
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "BroadcastReceiver recibió un Intent: ${intent?.action}")
        
        when (intent?.action) {
            Intent.ACTION_BATTERY_CHANGED -> {
                val batteryInfo = extractBatteryInfo(intent)
                onBatteryInfoReceived(batteryInfo)
                Log.d(TAG, "Información de batería actualizada: $batteryInfo")
            }
            Intent.ACTION_POWER_CONNECTED -> {
                Log.d(TAG, "Cargador conectado")
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                Log.d(TAG, "Cargador desconectado")
            }
        }
    }
    
    private fun extractBatteryInfo(intent: Intent): BatteryInfo {
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        val status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1)
        val plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)
        val temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1)
        val voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1)
        val health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
        val technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY) ?: "Desconocida"
        
        val batteryPercentage = if (level != -1 && scale != -1) {
            (level * 100 / scale.toFloat()).toInt()
        } else {
            -1
        }
        
        val chargingStatus = when (status) {
            BatteryManager.BATTERY_STATUS_CHARGING -> "Cargando"
            BatteryManager.BATTERY_STATUS_DISCHARGING -> "Descargando"
            BatteryManager.BATTERY_STATUS_FULL -> "Completa"
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> "No cargando"
            BatteryManager.BATTERY_STATUS_UNKNOWN -> "Desconocido"
            else -> "Estado desconocido"
        }
        
        val powerSource = when (plugged) {
            BatteryManager.BATTERY_PLUGGED_AC -> "Corriente AC"
            BatteryManager.BATTERY_PLUGGED_USB -> "USB"
            BatteryManager.BATTERY_PLUGGED_WIRELESS -> "Inalámbrico"
            0 -> "Batería"
            else -> "Desconocido"
        }
        
        val healthStatus = when (health) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Buena"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Sobrecalentada"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Muerta"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Sobrevoltaje"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Falla no especificada"
            BatteryManager.BATTERY_HEALTH_COLD -> "Fría"
            else -> "Desconocida"
        }
        
        return BatteryInfo(
            percentage = batteryPercentage,
            status = chargingStatus,
            powerSource = powerSource,
            temperature = temperature / 10.0, // La temperatura viene en décimas de grado Celsius
            voltage = voltage / 1000.0, // El voltaje viene en milivoltios
            health = healthStatus,
            technology = technology
        )
    }
}

data class BatteryInfo(
    val percentage: Int,
    val status: String,
    val powerSource: String,
    val temperature: Double,
    val voltage: Double,
    val health: String,
    val technology: String
)