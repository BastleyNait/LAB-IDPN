package com.example.myapplication.model;

import java.util.List;

public class Room {
    private int id;
    private String name;
    private String type;
    private List<Point> vertices;
    private Point center;
    private String description;
    private String area;
    private String capacity;
    private List<String> features;

    // Constructor vacío
    public Room() {}

    // Constructor completo
    public Room(int id, String name, String type, List<Point> vertices, Point center, 
                String description, String area, String capacity, List<String> features) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.vertices = vertices;
        this.center = center;
        this.description = description;
        this.area = area;
        this.capacity = capacity;
        this.features = features;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<Point> getVertices() { return vertices; }
    public void setVertices(List<Point> vertices) { this.vertices = vertices; }

    public Point getCenter() { return center; }
    public void setCenter(Point center) { this.center = center; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getArea() { return area; }
    public void setArea(String area) { this.area = area; }

    public String getCapacity() { return capacity; }
    public void setCapacity(String capacity) { this.capacity = capacity; }

    public List<String> getFeatures() { return features; }
    public void setFeatures(List<String> features) { this.features = features; }

    // Método para verificar si un punto está dentro del ambiente
    public boolean containsPoint(float x, float y) {
        if (vertices == null || vertices.size() < 3) return false;
        
        int intersections = 0;
        int vertexCount = vertices.size();
        
        for (int i = 0; i < vertexCount; i++) {
            Point vertex1 = vertices.get(i);
            Point vertex2 = vertices.get((i + 1) % vertexCount);
            
            if (((vertex1.getY() > y) != (vertex2.getY() > y)) &&
                (x < (vertex2.getX() - vertex1.getX()) * (y - vertex1.getY()) / 
                 (vertex2.getY() - vertex1.getY()) + vertex1.getX())) {
                intersections++;
            }
        }
        
        return (intersections % 2) == 1;
    }
}