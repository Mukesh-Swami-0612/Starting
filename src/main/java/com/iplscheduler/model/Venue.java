package com.iplscheduler.model;

/**
 * Represents a cricket venue/stadium
 */
public class Venue {
    private String name;
    private String city;
    private int capacity;
    
    public Venue(String name, String city, int capacity) {
        this.name = name;
        this.city = city;
        this.capacity = capacity;
    }
    
    // Getters
    public String getName() { return name; }
    public String getCity() { return city; }
    public int getCapacity() { return capacity; }
    
    // Setters
    public void setName(String name) { this.name = name; }
    public void setCity(String city) { this.city = city; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    
    @Override
    public String toString() {
        return name + " (" + city + ") - Capacity: " + capacity;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Venue venue = (Venue) obj;
        return name.equals(venue.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
} 