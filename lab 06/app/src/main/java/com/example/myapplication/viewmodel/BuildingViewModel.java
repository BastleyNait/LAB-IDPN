package com.example.myapplication.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.model.Building;
import com.example.myapplication.model.Room;
import com.example.myapplication.utils.JsonUtils;

public class BuildingViewModel extends AndroidViewModel {
    
    private MutableLiveData<Building> buildingLiveData;
    private MutableLiveData<Room> selectedRoomLiveData;
    private MutableLiveData<Boolean> showRoomDetailsLiveData;
    
    public BuildingViewModel(@NonNull Application application) {
        super(application);
        buildingLiveData = new MutableLiveData<>();
        selectedRoomLiveData = new MutableLiveData<>();
        showRoomDetailsLiveData = new MutableLiveData<>();
        
        loadBuildingData();
    }
    
    private void loadBuildingData() {
        Building building = JsonUtils.loadBuildingFromAssets(getApplication(), "building_data.json");
        buildingLiveData.setValue(building);
    }
    
    public LiveData<Building> getBuildingLiveData() {
        return buildingLiveData;
    }
    
    public LiveData<Room> getSelectedRoomLiveData() {
        return selectedRoomLiveData;
    }
    
    public LiveData<Boolean> getShowRoomDetailsLiveData() {
        return showRoomDetailsLiveData;
    }
    
    public void onRoomClicked(Room room) {
        selectedRoomLiveData.setValue(room);
        showRoomDetailsLiveData.setValue(true);
    }
    
    public void hideRoomDetails() {
        showRoomDetailsLiveData.setValue(false);
    }
    
    public Building getBuilding() {
        return buildingLiveData.getValue();
    }
    
    public Room getSelectedRoom() {
        return selectedRoomLiveData.getValue();
    }
}