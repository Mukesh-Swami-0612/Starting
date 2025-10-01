package com.iplscheduler.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a complete match schedule for the IPL tournament
 */
public class Schedule {
    private List<Match> matches;
    private String season;
    
    public Schedule(String season) {
        this.season = season;
        this.matches = new ArrayList<>();
    }
    
    // Getters
    public List<Match> getMatches() { return matches; }
    public String getSeason() { return season; }
    
    /**
     * Adds a match to the schedule
     */
    public void addMatch(Match match) {
        matches.add(match);
    }
    
    /**
     * Gets all matches for a specific team
     */
    public List<Match> getMatchesForTeam(Team team) {
        return matches.stream()
            .filter(match -> match.getTeam1().equals(team) || match.getTeam2().equals(team))
            .collect(Collectors.toList());
    }
    
    /**
     * Gets all matches on a specific date
     */
    public List<Match> getMatchesOnDate(java.time.LocalDate date) {
        return matches.stream()
            .filter(match -> match.getDate().equals(date))
            .collect(Collectors.toList());
    }
    
    /**
     * Gets all matches at a specific venue
     */
    public List<Match> getMatchesAtVenue(Venue venue) {
        return matches.stream()
            .filter(match -> match.getVenue().equals(venue))
            .collect(Collectors.toList());
    }
    
    /**
     * Gets the total number of matches
     */
    public int getTotalMatches() {
        return matches.size();
    }
    
    /**
     * Gets matches sorted by date
     */
    public List<Match> getMatchesSortedByDate() {
        return matches.stream()
            .sorted((m1, m2) -> m1.getDate().compareTo(m2.getDate()))
            .collect(Collectors.toList());
    }
    
    /**
     * Checks if a team has consecutive matches
     */
    public boolean hasConsecutiveMatches(Team team, int maxDays) {
        List<Match> teamMatches = getMatchesForTeam(team);
        teamMatches.sort((m1, m2) -> m1.getDate().compareTo(m2.getDate()));
        
        for (int i = 0; i < teamMatches.size() - 1; i++) {
            long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(
                teamMatches.get(i).getDate(), 
                teamMatches.get(i + 1).getDate()
            );
            if (daysBetween <= maxDays) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Gets schedule statistics
     */
    public String getScheduleStats() {
        if (matches.isEmpty()) {
            return "No matches scheduled";
        }
        
        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(
            matches.stream().map(Match::getDate).min(java.time.LocalDate::compareTo).get(),
            matches.stream().map(Match::getDate).max(java.time.LocalDate::compareTo).get()
        ) + 1;
        
        return String.format("Season: %s | Total Matches: %d | Duration: %d days", 
            season, getTotalMatches(), totalDays);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== IPL Schedule ").append(season).append(" ===\n");
        sb.append(getScheduleStats()).append("\n\n");
        
        List<Match> sortedMatches = getMatchesSortedByDate();
        for (Match match : sortedMatches) {
            sb.append(match.toString()).append("\n");
        }
        
        return sb.toString();
    }
} 