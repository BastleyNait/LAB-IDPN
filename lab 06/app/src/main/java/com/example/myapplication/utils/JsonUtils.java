package com.example.myapplication.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.myapplication.model.Building;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class JsonUtils {
    
    public static Building loadBuildingFromAssets(Context context, String fileName) {
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName);
            
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            
            String jsonString = new String(buffer, StandardCharsets.UTF_8);
            
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
            
            // Extraer el objeto "building" del JSON
            JsonObject buildingObject = jsonObject.getAsJsonObject("building");
            
            return gson.fromJson(buildingObject, Building.class);
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}