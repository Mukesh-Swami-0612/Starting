package com.iplscheduler;

import com.iplscheduler.model.*;
import com.iplscheduler.service.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class for IPL Match Scheduling System
 * Provides a user-friendly console interface
 */
public class Main {
    private static SchedulingService schedulingService;
    private static Scanner scanner;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("    IPL Match Scheduling System");
        System.out.println("==========================================");
        System.out.println();
        
        schedulingService = new SchedulingService();
        scanner = new Scanner(System.in);
        
        try {
            showMainMenu();
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
    
    /**
     * Display main menu and handle user input
     */
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. View Teams");
            System.out.println("2. Generate New Schedule");
            System.out.println("3. View Current Schedule");
            System.out.println("4. Save Schedule to File");
            System.out.println("5. Load Schedule from File");
            System.out.println("6. Export Schedule to Text");
            System.out.println("7. View Team Schedule");
            System.out.println("8. View Matches on Date");
            System.out.println("9. Check Schedule Fairness");
            System.out.println("10. Validate Schedule");
            System.out.println("11. Exit");
            System.out.print("\nEnter your choice (1-11): ");
            
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                
                switch (choice) {
                    case 1:
                        viewTeams();
                        break;
                    case 2:
                        generateNewSchedule();
                        break;
                    case 3:
                        viewCurrentSchedule();
                        break;
                    case 4:
                        saveScheduleToFile();
                        break;
                    case 5:
                        loadScheduleFromFile();
                        break;
                    case 6:
                        exportScheduleToText();
                        break;
                    case 7:
                        viewTeamSchedule();
                        break;
                    case 8:
                        viewMatchesOnDate();
                        break;
                    case 9:
                        checkScheduleFairness();
                        break;
                    case 10:
                        validateSchedule();
                        break;
                    case 11:
                        System.out.println("\nThank you for using IPL Match Scheduling System!");
                        return;
                    default:
                        System.out.println("Invalid choice! Please enter a number between 1 and 11.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
            }
            
            System.out.println("\nPress Enter to continue...");
            scanner.nextLine();
        }
    }
    
    /**
     * View all IPL teams
     */
    private static void viewTeams() {
        System.out.println("\n" + schedulingService.getTeamService().getTeamsAsString());
    }
    
    /**
     * Generate a new IPL schedule
     */
    private static void generateNewSchedule() {
        try {
            System.out.print("Enter season (e.g., IPL 2024): ");
            String season = scanner.nextLine().trim();
            
            if (season.isEmpty()) {
                season = "IPL 2024";
            }
            
            System.out.print("Enter start date (YYYY-MM-DD) or press Enter for today: ");
            String dateInput = scanner.nextLine().trim();
            
            LocalDate startDate;
            if (dateInput.isEmpty()) {
                startDate = LocalDate.now();
            } else {
                startDate = LocalDate.parse(dateInput, DATE_FORMATTER);
            }
            
            System.out.print("Enter maximum matches per day (press Enter for default): ");
            String maxMatchesInput = scanner.nextLine().trim();
            
            Schedule schedule;
            if (maxMatchesInput.isEmpty()) {
                schedule = schedulingService.generateNewSchedule(season, startDate);
            } else {
                int maxMatches = Integer.parseInt(maxMatchesInput);
                schedule = schedulingService.generateConstrainedSchedule(season, startDate, maxMatches);
            }
            
            System.out.println("\nSchedule generated successfully!");
            System.out.println(schedule.getScheduleStats());
            
        } catch (Exception e) {
            System.err.println("Error generating schedule: " + e.getMessage());
        }
    }
    
    /**
     * View current schedule
     */
    private static void viewCurrentSchedule() {
        Schedule schedule = schedulingService.getCurrentSchedule();
        if (schedule == null) {
            System.out.println("No schedule available. Please generate a schedule first.");
            return;
        }
        
        System.out.println("\n" + schedule.toString());
    }
    
    /**
     * Save schedule to file
     */
    private static void saveScheduleToFile() {
        if (schedulingService.getCurrentSchedule() == null) {
            System.out.println("No schedule to save. Please generate a schedule first.");
            return;
        }
        
        System.out.print("Enter filename (without extension): ");
        String filename = scanner.nextLine().trim();
        
        if (filename.isEmpty()) {
            filename = "ipl_schedule";
        }
        
        if (schedulingService.saveSchedule(filename)) {
            System.out.println("Schedule saved successfully to " + filename + ".csv");
        } else {
            System.err.println("Failed to save schedule.");
        }
    }
    
    /**
     * Load schedule from file
     */
    private static void loadScheduleFromFile() {
        List<String> availableFiles = schedulingService.getFileService().getAvailableScheduleFiles();
        
        if (availableFiles.isEmpty()) {
            System.out.println("No schedule files found in current directory.");
            return;
        }
        
        System.out.println("Available schedule files:");
        for (int i = 0; i < availableFiles.size(); i++) {
            System.out.println((i + 1) + ". " + availableFiles.get(i));
        }
        
        System.out.print("Enter file number or filename: ");
        String input = scanner.nextLine().trim();
        
        try {
            Schedule schedule;
            if (input.matches("\\d+")) {
                int fileNumber = Integer.parseInt(input);
                if (fileNumber > 0 && fileNumber <= availableFiles.size()) {
                    schedule = schedulingService.loadSchedule(availableFiles.get(fileNumber - 1));
                } else {
                    System.out.println("Invalid file number.");
                    return;
                }
            } else {
                schedule = schedulingService.loadSchedule(input);
            }
            
            if (schedule != null) {
                System.out.println("Schedule loaded successfully!");
                System.out.println(schedule.getScheduleStats());
            } else {
                System.err.println("Failed to load schedule.");
            }
        } catch (Exception e) {
            System.err.println("Error loading schedule: " + e.getMessage());
        }
    }
    
    /**
     * Export schedule to text format
     */
    private static void exportScheduleToText() {
        if (schedulingService.getCurrentSchedule() == null) {
            System.out.println("No schedule to export. Please generate a schedule first.");
            return;
        }
        
        System.out.print("Enter filename (without extension): ");
        String filename = scanner.nextLine().trim();
        
        if (filename.isEmpty()) {
            filename = "ipl_schedule";
        }
        
        if (schedulingService.exportScheduleToText(filename + ".txt")) {
            System.out.println("Schedule exported successfully to " + filename + ".txt");
        } else {
            System.err.println("Failed to export schedule.");
        }
    }
    
    /**
     * View schedule for a specific team
     */
    private static void viewTeamSchedule() {
        if (schedulingService.getCurrentSchedule() == null) {
            System.out.println("No schedule available. Please generate a schedule first.");
            return;
        }
        
        System.out.print("Enter team name: ");
        String teamName = scanner.nextLine().trim();
        
        try {
            List<Match> teamMatches = schedulingService.getMatchesForTeam(teamName);
            if (teamMatches.isEmpty()) {
                System.out.println("No matches found for " + teamName);
                return;
            }
            
            System.out.println("\n=== Matches for " + teamName + " ===");
            for (Match match : teamMatches) {
                System.out.println(match.toString());
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * View matches on a specific date
     */
    private static void viewMatchesOnDate() {
        if (schedulingService.getCurrentSchedule() == null) {
            System.out.println("No schedule available. Please generate a schedule first.");
            return;
        }
        
        System.out.print("Enter date (YYYY-MM-DD): ");
        String dateInput = scanner.nextLine().trim();
        
        try {
            List<Match> matches = schedulingService.getMatchesOnDate(dateInput);
            if (matches.isEmpty()) {
                System.out.println("No matches found on " + dateInput);
                return;
            }
            
            System.out.println("\n=== Matches on " + dateInput + " ===");
            for (Match match : matches) {
                System.out.println(match.toString());
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    /**
     * Check schedule fairness
     */
    private static void checkScheduleFairness() {
        if (schedulingService.getCurrentSchedule() == null) {
            System.out.println("No schedule available. Please generate a schedule first.");
            return;
        }
        
        System.out.println("\n" + schedulingService.checkScheduleFairness());
    }
    
    /**
     * Validate schedule
     */
    private static void validateSchedule() {
        if (schedulingService.getCurrentSchedule() == null) {
            System.out.println("No schedule available. Please generate a schedule first.");
            return;
        }
        
        System.out.println("\n" + schedulingService.validateSchedule());
    }
} 