package com.example.recicleview.utils

import android.content.Context
import com.example.recicleview.R
import com.example.recicleview.model.Building
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * Utility class to load building data from assets
 */
object DataLoader {
    
    /**
     * Loads buildings from the buildings_data.txt file in assets
     * Format: id|title|category|description|imageName
     */
    fun loadBuildings(context: Context): List<Building> {
        val buildings = mutableListOf<Building>()
        
        try {
            val inputStream = context.assets.open("buildings_data.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))
            
            reader.forEachLine { line ->
                if (line.isNotBlank()) {
                    val parts = line.split("|")
                    if (parts.size == 5) {
                        val id = parts[0].toIntOrNull() ?: 0
                        val title = parts[1]
                        val category = parts[2]
                        val description = parts[3]
                        val imageName = parts[4]
                        
                        // Map image name to drawable resource
                        val imageResId = getDrawableResourceId(context, imageName)
                        
                        buildings.add(
                            Building(
                                id = id,
                                title = title,
                                category = category,
                                description = description,
                                imageResId = imageResId
                            )
                        )
                    }
                }
            }
            
            reader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        
        return buildings
    }
    
    /**
     * Gets drawable resource ID from name
     */
    private fun getDrawableResourceId(context: Context, imageName: String): Int {
        return context.resources.getIdentifier(
            imageName,
            "drawable",
            context.packageName
        ).takeIf { it != 0 } ?: R.drawable.ic_launcher_foreground
    }
}
