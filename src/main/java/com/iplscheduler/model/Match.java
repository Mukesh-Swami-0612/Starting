package com.iplscheduler.model;

import java.time.LocalDate;

/**
 * Represents a cricket match between two teams
 */
public class Match {
    private Team team1;
    private Team team2;
    private Venue venue;
    private LocalDate date;
    private String matchType; // "Home", "Away", "Neutral"
    private int matchNumber;
    
    public Match(Team team1, Team team2, Venue venue, LocalDate date, String matchType, int matchNumber) {
        this.team1 = team1;
        this.team2 = team2;
        this.venue = venue;
        this.date = date;
        this.matchType = matchType;
        this.matchNumber = matchNumber;
    }
    
    // Getters
    public Team getTeam1() { return team1; }
    public Team getTeam2() { return team2; }
    public Venue getVenue() { return venue; }
    public LocalDate getDate() { return date; }
    public String getMatchType() { return matchType; }
    public int getMatchNumber() { return matchNumber; }
    
    // Setters
    public void setTeam1(Team team1) { this.team1 = team1; }
    public void setTeam2(Team team2) { this.team2 = team2; }
    public void setVenue(Venue venue) { this.venue = venue; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setMatchType(String matchType) { this.matchType = matchType; }
    public void setMatchNumber(int matchNumber) { this.matchNumber = matchNumber; }
    
    /**
     * Determines if this is a home match for the given team
     */
    public boolean isHomeMatch(Team team) {
        return venue.getName().equals(team.getHomeVenue());
    }
    
    /**
     * Gets the opponent team for a given team
     */
    public Team getOpponent(Team team) {
        if (team.equals(team1)) {
            return team2;
        } else if (team.equals(team2)) {
            return team1;
        }
        return null;
    }
    
    @Override
    public String toString() {
        return String.format("Match %d: %s vs %s at %s on %s (%s)", 
            matchNumber, team1.getName(), team2.getName(), 
            venue.getName(), date.toString(), matchType);
    }
} 