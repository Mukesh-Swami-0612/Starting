package com.iplscheduler;

import com.iplscheduler.model.*;
import com.iplscheduler.service.*;
import com.iplscheduler.util.ScheduleGenerator;
import java.time.LocalDate;
import java.util.List;

/**
 * Test class for IPL Scheduling System
 * Demonstrates testing capabilities and validates core functionality
 */
public class SchedulingServiceTest {
    
    public static void main(String[] args) {
        System.out.println("=== IPL Scheduling System - Test Suite ===\n");
        
        try {
            testTeamService();
            testScheduleGeneration();
            testScheduleValidation();
            testFileOperations();
            
            System.out.println("All tests passed successfully! ✅");
            
        } catch (Exception e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Test TeamService functionality
     */
    private static void testTeamService() {
        System.out.println("Testing TeamService...");
        
        TeamService teamService = new TeamService();
        
        // Test team count
        int teamCount = teamService.getTotalTeams();
        if (teamCount != 10) {
            throw new AssertionError("Expected 10 teams, got " + teamCount);
        }
        
        // Test team retrieval
        if (!teamService.teamExists("Mumbai Indians")) {
            throw new AssertionError("Mumbai Indians team not found");
        }
        
        // Test team by city
        List<Team> mumbaiTeams = teamService.getTeamsByCity("Mumbai");
        if (mumbaiTeams.size() != 1) {
            throw new AssertionError("Expected 1 Mumbai team, got " + mumbaiTeams.size());
        }
        
        System.out.println("✓ TeamService tests passed");
    }
    
    /**
     * Test schedule generation
     */
    private static void testScheduleGeneration() {
        System.out.println("Testing Schedule Generation...");
        
        TeamService teamService = new TeamService();
        List<Team> teams = teamService.getAllTeams();
        
        // Test basic schedule generation
        Schedule schedule = ScheduleGenerator.generateSchedule(teams, "IPL 2024", LocalDate.now());
        
        if (schedule.getTotalMatches() != 45) { // 10 teams = 9 matches each = 45 total
            throw new AssertionError("Expected 45 matches, got " + schedule.getTotalMatches());
        }
        
        // Test that each team has correct number of matches
        for (Team team : teams) {
            List<Match> teamMatches = schedule.getMatchesForTeam(team);
            if (teamMatches.size() != 9) {
                throw new AssertionError(team.getName() + " has " + teamMatches.size() + " matches, expected 9");
            }
        }
        
        System.out.println("✓ Schedule Generation tests passed");
    }
    
    /**
     * Test schedule validation
     */
    private static void testScheduleValidation() {
        System.out.println("Testing Schedule Validation...");
        
        TeamService teamService = new TeamService();
        List<Team> teams = teamService.getAllTeams();
        Schedule schedule = ScheduleGenerator.generateSchedule(teams, "IPL 2024", LocalDate.now());
        
        // Test schedule fairness
        for (Team team : teams) {
            List<Match> teamMatches = schedule.getMatchesForTeam(team);
            int homeMatches = (int) teamMatches.stream()
                .filter(match -> match.isHomeMatch(team))
                .count();
            int awayMatches = teamMatches.size() - homeMatches;
            
            // Check reasonable distribution (not all home or all away)
            if (homeMatches == 0 || awayMatches == 0) {
                throw new AssertionError(team.getName() + " has no " + 
                    (homeMatches == 0 ? "home" : "away") + " matches");
            }
        }
        
        System.out.println("✓ Schedule Validation tests passed");
    }
    
    /**
     * Test file operations
     */
    private static void testFileOperations() {
        System.out.println("Testing File Operations...");
        
        TeamService teamService = new TeamService();
        List<Team> teams = teamService.getAllTeams();
        Schedule schedule = ScheduleGenerator.generateSchedule(teams, "IPL 2024", LocalDate.now());
        
        FileService fileService = new FileService();
        
        // Test saving schedule
        String testFilename = "test_schedule";
        if (!fileService.saveScheduleToFile(schedule, testFilename)) {
            throw new AssertionError("Failed to save schedule to file");
        }
        
        // Test loading schedule
        Schedule loadedSchedule = fileService.loadScheduleFromFile(testFilename, teamService);
        if (loadedSchedule == null) {
            throw new AssertionError("Failed to load schedule from file");
        }
        
        if (loadedSchedule.getTotalMatches() != schedule.getTotalMatches()) {
            throw new AssertionError("Loaded schedule has different number of matches");
        }
        
        // Test text export
        if (!fileService.exportScheduleToText(schedule, "test_schedule.txt")) {
            throw new AssertionError("Failed to export schedule to text");
        }
        
        System.out.println("✓ File Operations tests passed");
    }
} 