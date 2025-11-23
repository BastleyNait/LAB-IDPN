package com.example.recicleview

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recicleview.adapter.BuildingAdapter
import com.example.recicleview.databinding.ActivityMainBinding
import com.example.recicleview.utils.DataLoader

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: BuildingAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Load buildings data from assets
        val buildings = DataLoader.loadBuildings(this)
        
        // Setup RecyclerView
        adapter = BuildingAdapter(buildings)
        binding.recyclerViewBuildings.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
            setHasFixedSize(true)
        }
        
        // Setup search filter
        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed
            }
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Filter as user types
                adapter.filter(s.toString())
            }
            
            override fun afterTextChanged(s: Editable?) {
                // Not needed
            }
        })
    }
}