package com.example.myapplication.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapter.FeaturesAdapter;
import com.example.myapplication.model.Room;
import com.example.myapplication.viewmodel.BuildingViewModel;

public class RoomDetailsFragment extends DialogFragment {
    
    private BuildingViewModel viewModel;
    private TextView tvRoomName;
    private TextView tvRoomType;
    private TextView tvRoomDescription;
    private TextView tvRoomArea;
    private TextView tvRoomCapacity;
    private ImageView ivRoomImage;
    private RecyclerView rvFeatures;
    private Button btnClose;
    private FeaturesAdapter featuresAdapter;
    
    public static RoomDetailsFragment newInstance() {
        return new RoomDetailsFragment();
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(BuildingViewModel.class);
    }
    
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, 
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_room_details, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initViews(view);
        setupRecyclerView();
        setupObservers();
        setupListeners();
    }
    
    private void initViews(View view) {
        tvRoomName = view.findViewById(R.id.tvRoomName);
        tvRoomType = view.findViewById(R.id.tvRoomType);
        tvRoomDescription = view.findViewById(R.id.tvRoomDescription);
        tvRoomArea = view.findViewById(R.id.tvRoomArea);
        tvRoomCapacity = view.findViewById(R.id.tvRoomCapacity);
        ivRoomImage = view.findViewById(R.id.ivRoomImage);
        rvFeatures = view.findViewById(R.id.rvFeatures);
        btnClose = view.findViewById(R.id.btnClose);
    }
    
    private void setupRecyclerView() {
        featuresAdapter = new FeaturesAdapter(null);
        rvFeatures.setLayoutManager(new LinearLayoutManager(getContext()));
        rvFeatures.setAdapter(featuresAdapter);
    }
    
    private void setupObservers() {
        viewModel.getSelectedRoomLiveData().observe(getViewLifecycleOwner(), room -> {
            if (room != null) {
                updateRoomDetails(room);
            }
        });
    }
    
    private void setupListeners() {
        btnClose.setOnClickListener(v -> {
            viewModel.hideRoomDetails();
            dismiss();
        });
    }
    
    private void updateRoomDetails(Room room) {
        tvRoomName.setText(room.getName());
        tvRoomType.setText("Tipo: " + capitalizeFirst(room.getType()));
        tvRoomDescription.setText(room.getDescription());
        tvRoomArea.setText(room.getArea());
        tvRoomCapacity.setText(room.getCapacity());
        
        // Asignar imagen según el nombre del ambiente
        setRoomImage(room.getName());
        
        if (room.getFeatures() != null) {
            featuresAdapter.updateFeatures(room.getFeatures());
        }
    }
    
    private void setRoomImage(String roomName) {
        int imageResource = 0;
        
        switch (roomName.toLowerCase()) {
            case "biblioteca":
                imageResource = R.drawable.biblioteca;
                break;
            case "patio central":
                imageResource = R.drawable.patio_central;
                break;
            case "patio de los naranjos":
                imageResource = R.drawable.patio_de_los_naranjos;
                break;
            case "salón de conferencias":
                imageResource = R.drawable.salon_de_conferencias;
                break;
            case "salón de exposiciones":
                imageResource = R.drawable.salon_de_exposiciones;
                break;
            default:
                // Imagen por defecto si no se encuentra coincidencia
                imageResource = R.drawable.ic_launcher_background;
                break;
        }
        
        if (imageResource != 0) {
            ivRoomImage.setImageResource(imageResource);
        }
    }
    
    private String capitalizeFirst(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}