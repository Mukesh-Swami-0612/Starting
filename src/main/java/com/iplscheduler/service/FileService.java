package com.iplscheduler.service;

import com.iplscheduler.model.Schedule;
import com.iplscheduler.model.Match;
import com.iplscheduler.model.Team;
import com.iplscheduler.model.Venue;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for file operations (save/load schedules)
 */
public class FileService {
    private static final String SCHEDULE_FILE_EXTENSION = ".csv";
    private static final String TEAMS_FILE = "teams.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Save schedule to CSV file
     */
    public boolean saveScheduleToFile(Schedule schedule, String filename) {
        if (schedule == null || filename == null || filename.trim().isEmpty()) {
            return false;
        }
        
        // Add .csv extension if not present
        if (!filename.endsWith(SCHEDULE_FILE_EXTENSION)) {
            filename += SCHEDULE_FILE_EXTENSION;
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write header
            writer.println("Match Number,Team 1,Team 2,Venue,Date,Match Type");
            
            // Write match data
            List<Match> sortedMatches = schedule.getMatchesSortedByDate();
            for (Match match : sortedMatches) {
                writer.printf("%d,%s,%s,%s,%s,%s%n",
                    match.getMatchNumber(),
                    match.getTeam1().getName(),
                    match.getTeam2().getName(),
                    match.getVenue().getName(),
                    match.getDate().format(DATE_FORMATTER),
                    match.getMatchType()
                );
            }
            
            return true;
        } catch (IOException e) {
            System.err.println("Error saving schedule to file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load schedule from CSV file
     */
    public Schedule loadScheduleFromFile(String filename, TeamService teamService) {
        if (filename == null || filename.trim().isEmpty() || teamService == null) {
            return null;
        }
        
        // Add .csv extension if not present
        if (!filename.endsWith(SCHEDULE_FILE_EXTENSION)) {
            filename += SCHEDULE_FILE_EXTENSION;
        }
        
        File file = new File(filename);
        if (!file.exists()) {
            System.err.println("File not found: " + filename);
            return null;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Skip header
            
            // Extract season from filename
            String season = filename.replace(SCHEDULE_FILE_EXTENSION, "");
            Schedule schedule = new Schedule(season);
            
            int matchNumber = 1;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    try {
                        Team team1 = teamService.getTeamByName(parts[1].trim()).orElse(null);
                        Team team2 = teamService.getTeamByName(parts[2].trim()).orElse(null);
                        
                        if (team1 != null && team2 != null) {
                            Venue venue = new Venue(parts[3].trim(), "Unknown", 50000);
                            LocalDate date = LocalDate.parse(parts[4].trim(), DATE_FORMATTER);
                            String matchType = parts[5].trim();
                            
                            Match match = new Match(team1, team2, venue, date, matchType, matchNumber++);
                            schedule.addMatch(match);
                        }
                    } catch (Exception e) {
                        System.err.println("Error parsing line: " + line + " - " + e.getMessage());
                    }
                }
            }
            
            return schedule;
        } catch (IOException e) {
            System.err.println("Error loading schedule from file: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Save teams to text file
     */
    public boolean saveTeamsToFile(List<Team> teams, String filename) {
        if (teams == null || filename == null || filename.trim().isEmpty()) {
            return false;
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            for (Team team : teams) {
                writer.printf("%s|%s|%s|%s%n",
                    team.getName(),
                    team.getCity(),
                    team.getCaptain(),
                    team.getHomeVenue()
                );
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error saving teams to file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load teams from text file
     */
    public List<Team> loadTeamsFromFile(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return new ArrayList<>();
        }
        
        File file = new File(filename);
        if (!file.exists()) {
            System.err.println("File not found: " + filename);
            return new ArrayList<>();
        }
        
        List<Team> teams = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null && !line.trim().isEmpty()) {
                String[] parts = line.split("\\|");
                if (parts.length >= 4) {
                    Team team = new Team(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim()
                    );
                    teams.add(team);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading teams from file: " + e.getMessage());
        }
        
        return teams;
    }
    
    /**
     * Export schedule to readable text format
     */
    public boolean exportScheduleToText(Schedule schedule, String filename) {
        if (schedule == null || filename == null || filename.trim().isEmpty()) {
            return false;
        }
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println(schedule.toString());
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting schedule to text: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get list of available schedule files
     */
    public List<String> getAvailableScheduleFiles() {
        List<String> files = new ArrayList<>();
        File currentDir = new File(".");
        File[] fileList = currentDir.listFiles((dir, name) -> name.endsWith(SCHEDULE_FILE_EXTENSION));
        
        if (fileList != null) {
            for (File file : fileList) {
                files.add(file.getName());
            }
        }
        
        return files;
    }
} 