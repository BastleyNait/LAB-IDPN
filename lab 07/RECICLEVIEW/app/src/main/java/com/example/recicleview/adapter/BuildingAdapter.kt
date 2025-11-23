package com.example.recicleview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recicleview.databinding.ItemBuildingBinding
import com.example.recicleview.model.Building
import java.util.Locale

/**
 * RecyclerView adapter for displaying buildings with filtering capability
 */
class BuildingAdapter(
    private val allBuildings: List<Building>
) : RecyclerView.Adapter<BuildingAdapter.BuildingViewHolder>() {
    
    // Filtered list that will be displayed
    private var filteredBuildings: List<Building> = allBuildings
    
    /**
     * ViewHolder using ViewBinding
     */
    class BuildingViewHolder(
        private val binding: ItemBuildingBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(building: Building) {
            binding.apply {
                imageBuilding.setImageResource(building.imageResId)
                textTitle.text = building.title
                textCategory.text = building.category
                textDescription.text = building.description
            }
        }
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingViewHolder {
        val binding = ItemBuildingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BuildingViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: BuildingViewHolder, position: Int) {
        holder.bind(filteredBuildings[position])
    }
    
    override fun getItemCount(): Int = filteredBuildings.size
    
    /**
     * Filter the buildings list based on search query
     * Filters by title (case-insensitive)
     */
    fun filter(query: String) {
        filteredBuildings = if (query.isEmpty()) {
            allBuildings
        } else {
            allBuildings.filter { building ->
                building.title.lowercase(Locale.getDefault())
                    .contains(query.lowercase(Locale.getDefault()))
            }
        }
        notifyDataSetChanged()
    }
}
