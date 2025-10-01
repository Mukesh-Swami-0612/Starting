package com.iplscheduler.service;

import com.iplscheduler.model.Team;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing IPL teams
 */
public class TeamService {
    private List<Team> teams;
    
    public TeamService() {
        this.teams = new ArrayList<>();
        initializeDefaultTeams();
    }
    
    /**
     * Initialize with default IPL teams
     */
    private void initializeDefaultTeams() {
        teams.add(new Team("Mumbai Indians", "Mumbai", "Hardik Pandya", "Wankhede Stadium"));
        teams.add(new Team("Chennai Super Kings", "Chennai", "MS Dhoni", "M.A. Chidambaram Stadium"));
        teams.add(new Team("Royal Challengers Bangalore", "Bangalore", "Faf du Plessis", "M. Chinnaswamy Stadium"));
        teams.add(new Team("Kolkata Knight Riders", "Kolkata", "Shreyas Iyer", "Eden Gardens"));
        teams.add(new Team("Delhi Capitals", "Delhi", "Rishabh Pant", "Arun Jaitley Stadium"));
        teams.add(new Team("Punjab Kings", "Mohali", "Shikhar Dhawan", "IS Bindra Stadium"));
        teams.add(new Team("Rajasthan Royals", "Jaipur", "Sanju Samson", "Sawai Mansingh Stadium"));
        teams.add(new Team("Sunrisers Hyderabad", "Hyderabad", "Pat Cummins", "Rajiv Gandhi Stadium"));
        teams.add(new Team("Gujarat Titans", "Ahmedabad", "Shubman Gill", "Narendra Modi Stadium"));
        teams.add(new Team("Lucknow Super Giants", "Lucknow", "KL Rahul", "BRSABV Ekana Stadium"));
    }
    
    /**
     * Add a new team
     */
    public boolean addTeam(Team team) {
        if (team == null || team.getName() == null || team.getName().trim().isEmpty()) {
            return false;
        }
        
        // Check if team already exists
        if (teams.stream().anyMatch(t -> t.getName().equalsIgnoreCase(team.getName()))) {
            return false;
        }
        
        teams.add(team);
        return true;
    }
    
    /**
     * Remove a team by name
     */
    public boolean removeTeam(String teamName) {
        if (teamName == null || teamName.trim().isEmpty()) {
            return false;
        }
        
        return teams.removeIf(team -> team.getName().equalsIgnoreCase(teamName));
    }
    
    /**
     * Get team by name
     */
    public Optional<Team> getTeamByName(String teamName) {
        if (teamName == null || teamName.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return teams.stream()
            .filter(team -> team.getName().equalsIgnoreCase(teamName))
            .findFirst();
    }
    
    /**
     * Get all teams
     */
    public List<Team> getAllTeams() {
        return new ArrayList<>(teams);
    }
    
    /**
     * Get teams by city
     */
    public List<Team> getTeamsByCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        return teams.stream()
            .filter(team -> team.getCity().equalsIgnoreCase(city))
            .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Get total number of teams
     */
    public int getTotalTeams() {
        return teams.size();
    }
    
    /**
     * Check if team exists
     */
    public boolean teamExists(String teamName) {
        return getTeamByName(teamName).isPresent();
    }
    
    /**
     * Get teams as formatted string
     */
    public String getTeamsAsString() {
        if (teams.isEmpty()) {
            return "No teams available";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== IPL Teams ===\n");
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            sb.append(String.format("%d. %s\n", i + 1, team.toString()));
        }
        return sb.toString();
    }
    
    /**
     * Clear all teams
     */
    public void clearTeams() {
        teams.clear();
    }
} 