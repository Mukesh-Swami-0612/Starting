package com.iplscheduler.service;

import com.iplscheduler.model.*;
import com.iplscheduler.util.ScheduleGenerator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Main service class for managing IPL match scheduling operations
 */
public class SchedulingService {
    private TeamService teamService;
    private FileService fileService;
    private Schedule currentSchedule;
    
    public SchedulingService() {
        this.teamService = new TeamService();
        this.fileService = new FileService();
        this.currentSchedule = null;
    }
    
    /**
     * Generate a new IPL schedule
     */
    public Schedule generateNewSchedule(String season, LocalDate startDate) {
        List<Team> teams = teamService.getAllTeams();
        
        if (teams.size() < 2) {
            throw new IllegalStateException("At least 2 teams are required to generate a schedule");
        }
        
        try {
            currentSchedule = ScheduleGenerator.generateSchedule(teams, season, startDate);
            return currentSchedule;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate schedule: " + e.getMessage(), e);
        }
    }
    
    /**
     * Generate schedule with constraints
     */
    public Schedule generateConstrainedSchedule(String season, LocalDate startDate, int maxMatchesPerDay) {
        List<Team> teams = teamService.getAllTeams();
        
        if (teams.size() < 2) {
            throw new IllegalStateException("At least 2 teams are required to generate a schedule");
        }
        
        try {
            currentSchedule = ScheduleGenerator.generateConstrainedSchedule(teams, season, startDate, maxMatchesPerDay);
            return currentSchedule;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate constrained schedule: " + e.getMessage(), e);
        }
    }
    
    /**
     * Save current schedule to file
     */
    public boolean saveSchedule(String filename) {
        if (currentSchedule == null) {
            throw new IllegalStateException("No schedule to save. Generate a schedule first.");
        }
        
        return fileService.saveScheduleToFile(currentSchedule, filename);
    }
    
    /**
     * Load schedule from file
     */
    public Schedule loadSchedule(String filename) {
        currentSchedule = fileService.loadScheduleFromFile(filename, teamService);
        return currentSchedule;
    }
    
    /**
     * Export schedule to text format
     */
    public boolean exportScheduleToText(String filename) {
        if (currentSchedule == null) {
            throw new IllegalStateException("No schedule to export. Generate a schedule first.");
        }
        
        return fileService.exportScheduleToText(currentSchedule, filename);
    }
    
    /**
     * Get current schedule
     */
    public Schedule getCurrentSchedule() {
        return currentSchedule;
    }
    
    /**
     * Get schedule statistics
     */
    public String getScheduleStatistics() {
        if (currentSchedule == null) {
            return "No schedule available";
        }
        
        return currentSchedule.getScheduleStats();
    }
    
    /**
     * Get matches for a specific team
     */
    public List<Match> getMatchesForTeam(String teamName) {
        if (currentSchedule == null) {
            throw new IllegalStateException("No schedule available");
        }
        
        Optional<Team> team = teamService.getTeamByName(teamName);
        if (team.isEmpty()) {
            throw new IllegalArgumentException("Team not found: " + teamName);
        }
        
        return currentSchedule.getMatchesForTeam(team.get());
    }
    
    /**
     * Get matches on a specific date
     */
    public List<Match> getMatchesOnDate(String dateString) {
        if (currentSchedule == null) {
            throw new IllegalStateException("No schedule available");
        }
        
        try {
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
            return currentSchedule.getMatchesOnDate(date);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD");
        }
    }
    
    /**
     * Check schedule fairness
     */
    public String checkScheduleFairness() {
        if (currentSchedule == null) {
            return "No schedule available";
        }
        
        List<Team> teams = teamService.getAllTeams();
        StringBuilder report = new StringBuilder();
        report.append("=== Schedule Fairness Report ===\n\n");
        
        for (Team team : teams) {
            List<Match> teamMatches = currentSchedule.getMatchesForTeam(team);
            int homeMatches = (int) teamMatches.stream()
                .filter(match -> match.isHomeMatch(team))
                .count();
            int awayMatches = teamMatches.size() - homeMatches;
            
            report.append(String.format("%s:\n", team.getName()));
            report.append(String.format("  Total Matches: %d\n", teamMatches.size()));
            report.append(String.format("  Home Matches: %d\n", homeMatches));
            report.append(String.format("  Away Matches: %d\n", awayMatches));
            
            // Check for consecutive matches
            boolean hasConsecutive = currentSchedule.hasConsecutiveMatches(team, 1);
            report.append(String.format("  Consecutive Matches: %s\n", hasConsecutive ? "Yes" : "No"));
            report.append("\n");
        }
        
        return report.toString();
    }
    
    /**
     * Validate schedule
     */
    public String validateSchedule() {
        if (currentSchedule == null) {
            return "No schedule available";
        }
        
        List<String> issues = new java.util.ArrayList<>();
        
        // Check if all teams have equal number of matches
        List<Team> teams = teamService.getAllTeams();
        int expectedMatches = (teams.size() - 1); // Round-robin format
        
        for (Team team : teams) {
            int actualMatches = currentSchedule.getMatchesForTeam(team).size();
            if (actualMatches != expectedMatches) {
                issues.add(String.format("%s has %d matches, expected %d", 
                    team.getName(), actualMatches, expectedMatches));
            }
        }
        
        // Check for date conflicts
        List<Match> sortedMatches = currentSchedule.getMatchesSortedByDate();
        for (int i = 0; i < sortedMatches.size() - 1; i++) {
            if (sortedMatches.get(i).getDate().equals(sortedMatches.get(i + 1).getDate())) {
                issues.add(String.format("Multiple matches on %s", 
                    sortedMatches.get(i).getDate().toString()));
            }
        }
        
        if (issues.isEmpty()) {
            return "Schedule validation passed! No issues found.";
        } else {
            StringBuilder report = new StringBuilder();
            report.append("Schedule validation failed! Issues found:\n\n");
            for (String issue : issues) {
                report.append("• ").append(issue).append("\n");
            }
            return report.toString();
        }
    }
    
    /**
     * Get team service
     */
    public TeamService getTeamService() {
        return teamService;
    }
    
    /**
     * Get file service
     */
    public FileService getFileService() {
        return fileService;
    }
    
    /**
     * Clear current schedule
     */
    public void clearSchedule() {
        currentSchedule = null;
    }
} 