package com.example.myapplication.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.R;
import com.example.myapplication.model.Building;
import com.example.myapplication.view.BuildingPlanView;
import com.example.myapplication.viewmodel.BuildingViewModel;

public class BuildingPlanFragment extends Fragment {
    
    private BuildingViewModel viewModel;
    private BuildingPlanView buildingPlanView;
    private TextView tvBuildingTitle;
    private TextView tvBuildingDescription;
    
    public static BuildingPlanFragment newInstance() {
        return new BuildingPlanFragment();
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(BuildingViewModel.class);
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_building_plan, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupObservers();
        setupListeners();
    }
    
    private void initViews(View view) {
        buildingPlanView = view.findViewById(R.id.buildingPlanView);
        tvBuildingTitle = view.findViewById(R.id.tvBuildingTitle);
        tvBuildingDescription = view.findViewById(R.id.tvBuildingDescription);
    }
    
    private void setupObservers() {
        // Observar los datos de la edificación
        viewModel.getBuildingLiveData().observe(getViewLifecycleOwner(), building -> {
            if (building != null) {
                updateBuildingInfo(building);
                buildingPlanView.setBuilding(building);
            }
        });
        
        // Observar cuando se debe mostrar los detalles de un ambiente
        viewModel.getShowRoomDetailsLiveData().observe(getViewLifecycleOwner(), showDetails -> {
            if (showDetails != null && showDetails) {
                showRoomDetailsDialog();
            }
        });
    }
    
    private void setupListeners() {
        // Configurar el listener para clicks en ambientes
        buildingPlanView.setOnRoomClickListener(room -> {
            viewModel.onRoomClicked(room);
        });
    }
    
    private void updateBuildingInfo(Building building) {
        tvBuildingTitle.setText(building.getName());
        tvBuildingDescription.setText(building.getDescription() + "\\n\\nToque sobre cualquier ambiente para ver más información");
    }
    
    private void showRoomDetailsDialog() {
        RoomDetailsFragment dialogFragment = RoomDetailsFragment.newInstance();
        dialogFragment.show(getParentFragmentManager(), "RoomDetailsFragment");
    }
}