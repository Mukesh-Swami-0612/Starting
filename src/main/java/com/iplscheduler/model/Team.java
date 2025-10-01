package com.iplscheduler.model;

/**
 * Represents an IPL cricket team with basic information
 */
public class Team {
    private String name;
    private String city;
    private String captain;
    private String homeVenue;
    
    public Team(String name, String city, String captain, String homeVenue) {
        this.name = name;
        this.city = city;
        this.captain = captain;
        this.homeVenue = homeVenue;
    }
    
    // Getters
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getCaptain() { return captain; }
    public String getHomeVenue() { return homeVenue; }
    
    // Setters
    public void setName(String name) { this.name = name; }
    public void setCity(String city) { this.city = city; }
    public void setCaptain(String captain) { this.captain = captain; }
    public void setHomeVenue(String homeVenue) { this.homeVenue = homeVenue; }
    
    @Override
    public String toString() {
        return name + " (" + city + ") - Captain: " + captain + ", Home: " + homeVenue;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Team team = (Team) obj;
        return name.equals(team.name);
    }
    
    @Override
    public int hashCode() {
        return name.hashCode();
    }
} 