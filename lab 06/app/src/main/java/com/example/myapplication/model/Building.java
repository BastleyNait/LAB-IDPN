package com.example.myapplication.model;

import java.util.List;

public class Building {
    private String name;
    private String description;
    private List<Room> rooms;

    // Constructor vacío
    public Building() {}

    // Constructor con parámetros
    public Building(String name, String description, List<Room> rooms) {
        this.name = name;
        this.description = description;
        this.rooms = rooms;
    }

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Room> getRooms() { return rooms; }
    public void setRooms(List<Room> rooms) { this.rooms = rooms; }

    // Método para encontrar un ambiente por coordenadas
    public Room findRoomByCoordinates(float x, float y) {
        if (rooms != null) {
            for (Room room : rooms) {
                if (room.containsPoint(x, y)) {
                    return room;
                }
            }
        }
        return null;
    }

    // Método para obtener un ambiente por ID
    public Room getRoomById(int id) {
        if (rooms != null) {
            for (Room room : rooms) {
                if (room.getId() == id) {
                    return room;
                }
            }
        }
        return null;
    }
}