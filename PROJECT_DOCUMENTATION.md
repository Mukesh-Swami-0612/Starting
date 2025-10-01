# IPL Match Scheduling System - Project Documentation

## Table of Contents
1. [System Overview](#system-overview)
2. [Architecture](#architecture)
3. [Core Algorithms](#core-algorithms)
4. [Data Models](#data-models)
5. [Services](#services)
6. [Usage Guide](#usage-guide)
7. [Technical Features](#technical-features)
8. [Resume Points](#resume-points)

## System Overview

The IPL Match Scheduling System is a comprehensive Java application that generates fair and optimized cricket match schedules for the Indian Premier League (IPL). The system ensures balanced fixture distribution, fair home/away game allocation, and optimal venue utilization.

### Key Features
- **Round-Robin Scheduling**: Ensures each team plays every other team exactly once
- **Fair Distribution**: Balanced home and away matches for all teams
- **Venue Optimization**: Intelligent venue assignment based on team home grounds
- **Constraint Management**: Configurable match distribution and timing
- **File Operations**: Save/load schedules in CSV and text formats
- **Validation**: Comprehensive schedule validation and fairness checking

## Architecture

The system follows a layered architecture pattern with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│                     (Main.java)                            │
├─────────────────────────────────────────────────────────────┤
│                    Service Layer                            │
│        (SchedulingService, TeamService, FileService)       │
├─────────────────────────────────────────────────────────────┤
│                    Utility Layer                            │
│                 (ScheduleGenerator)                         │
├─────────────────────────────────────────────────────────────┤
│                    Model Layer                              │
│           (Team, Match, Venue, Schedule)                   │
└─────────────────────────────────────────────────────────────┘
```

### Design Principles
- **Single Responsibility**: Each class has one clear purpose
- **Open/Closed**: Easy to extend without modifying existing code
- **Dependency Inversion**: High-level modules don't depend on low-level modules
- **Interface Segregation**: Clients only depend on methods they use

## Core Algorithms

### 1. Round-Robin Scheduling Algorithm

The system implements a modified round-robin algorithm to generate fixtures:

```java
// Pseudo-code for round-robin generation
for (round = 0; round < numTeams - 1; round++) {
    for (i = 0; i < numTeams / 2; i++) {
        team1 = teams[i]
        team2 = teams[numTeams - 1 - i]
        createFixture(team1, team2)
    }
    rotateTeams(teams) // Keep first team fixed, rotate others
}
```

**Algorithm Complexity**: O(n²) where n is the number of teams
**Fairness**: Each team plays every other team exactly once

### 2. Venue Assignment Algorithm

```java
private static Venue findBestVenue(MatchFixture fixture, List<Venue> venues) {
    // Prefer home venue of one of the teams
    for (Venue venue : venues) {
        if (venue.getName().equals(fixture.getTeam1().getHomeVenue()) ||
            venue.getName().equals(fixture.getTeam2().getHomeVenue())) {
            return venue;
        }
    }
    return venues.get(0); // Fallback
}
```

### 3. Constraint Management

The system can enforce constraints like maximum matches per day:

```java
public static Schedule generateConstrainedSchedule(
    List<Team> teams, String season, 
    LocalDate startDate, int maxMatchesPerDay) {
    
    Schedule schedule = generateSchedule(teams, season, startDate);
    
    // Redistribute matches if needed
    Map<LocalDate, List<Match>> matchesByDate = groupByDate(schedule);
    for (Entry<LocalDate, List<Match>> entry : matchesByDate.entrySet()) {
        if (entry.getValue().size() > maxMatchesPerDay) {
            redistributeMatches(entry.getValue(), entry.getKey(), maxMatchesPerDay);
        }
    }
    return schedule;
}
```

## Data Models

### 1. Team Class
```java
public class Team {
    private String name;        // Team name
    private String city;        // Home city
    private String captain;     // Team captain
    private String homeVenue;   // Home stadium
}
```

### 2. Match Class
```java
public class Match {
    private Team team1;         // First team
    private Team team2;         // Second team
    private Venue venue;        // Match venue
    private LocalDate date;     // Match date
    private String matchType;   // Home/Away/Neutral
    private int matchNumber;    // Sequential match number
}
```

### 3. Schedule Class
```java
public class Schedule {
    private List<Match> matches;  // All matches
    private String season;        // Tournament season
    
    // Methods for schedule analysis and validation
    public List<Match> getMatchesForTeam(Team team)
    public List<Match> getMatchesOnDate(LocalDate date)
    public boolean hasConsecutiveMatches(Team team, int maxDays)
}
```

## Services

### 1. SchedulingService
- **Purpose**: Main orchestrator for scheduling operations
- **Responsibilities**: 
  - Generate new schedules
  - Manage current schedule state
  - Provide schedule analysis methods
  - Coordinate between other services

### 2. TeamService
- **Purpose**: Manage IPL teams
- **Responsibilities**:
  - Add/remove teams
  - Team retrieval and search
  - Team validation
  - Default team initialization

### 3. FileService
- **Purpose**: Handle file I/O operations
- **Responsibilities**:
  - Save schedules to CSV
  - Load schedules from files
  - Export to text format
  - File management utilities

## Usage Guide

### 1. Running the Application
```bash
# Windows
build.bat

# Linux/Mac
chmod +x build.sh
./build.sh
```

### 2. Main Menu Options
1. **View Teams**: Display all IPL teams with details
2. **Generate New Schedule**: Create a new tournament schedule
3. **View Current Schedule**: Display the current schedule
4. **Save Schedule**: Export schedule to CSV file
5. **Load Schedule**: Import schedule from file
6. **Export to Text**: Save schedule in readable format
7. **View Team Schedule**: Show matches for specific team
8. **View Matches on Date**: Show matches on specific date
9. **Check Fairness**: Analyze schedule balance
10. **Validate Schedule**: Verify schedule integrity

### 3. Example Workflow
```
1. Start application → Choose option 2 (Generate Schedule)
2. Enter season: "IPL 2024"
3. Enter start date: "2024-03-15" (or press Enter for today)
4. Enter max matches per day: 2 (or press Enter for default)
5. View generated schedule → Choose option 3
6. Save schedule → Choose option 4, enter filename
7. Check fairness → Choose option 9
8. Validate schedule → Choose option 10
```

## Technical Features

### 1. Data Structures Used
- **ArrayList**: Dynamic team and match collections
- **HashMap**: Efficient venue and date lookups
- **Stream API**: Functional programming for data processing
- **LocalDate**: Modern date handling (Java 8+)

### 2. Exception Handling
```java
try {
    schedule = schedulingService.generateNewSchedule(season, startDate);
} catch (IllegalArgumentException e) {
    // Handle invalid input
} catch (IllegalStateException e) {
    // Handle system state issues
} catch (RuntimeException e) {
    // Handle unexpected errors
}
```

### 3. File I/O Operations
- **CSV Export**: Structured data export for analysis
- **Text Export**: Human-readable format
- **File Validation**: Error handling for file operations
- **Cross-platform**: Works on Windows, Linux, and Mac

### 4. Performance Optimizations
- **Lazy Loading**: Teams loaded only when needed
- **Efficient Algorithms**: O(n²) complexity for schedule generation
- **Memory Management**: Proper object lifecycle management
- **Stream Processing**: Efficient data filtering and transformation

## Resume Points

### Technical Skills Demonstrated
1. **Java Programming**: Advanced Java 8+ features, OOP principles
2. **Data Structures & Algorithms**: Custom implementations, optimization
3. **System Design**: Layered architecture, design patterns
4. **File I/O**: CSV processing, data persistence
5. **Exception Handling**: Robust error management
6. **Testing**: Comprehensive test suite with validation

### Project Highlights
1. **Real-world Application**: Practical sports scheduling system
2. **Algorithm Implementation**: Round-robin scheduling algorithm
3. **Data Management**: Complex data relationships and validation
4. **User Interface**: Interactive console-based application
5. **Documentation**: Professional project documentation
6. **Code Quality**: Clean, maintainable, well-documented code

### Learning Outcomes
1. **Problem Solving**: Complex scheduling constraints and optimization
2. **System Architecture**: Scalable, maintainable design
3. **Data Modeling**: Efficient data representation and relationships
4. **User Experience**: Intuitive interface design
5. **Project Management**: Complete project lifecycle management

## Conclusion

This IPL Match Scheduling System demonstrates advanced Java programming skills, algorithmic thinking, and software engineering best practices. The project showcases:

- **Technical Proficiency**: Advanced Java features and algorithms
- **System Design**: Well-architected, scalable solution
- **Problem Solving**: Complex real-world problem implementation
- **Code Quality**: Professional-grade, maintainable code
- **Documentation**: Comprehensive project documentation

The system is production-ready and can be extended for other sports leagues or scheduling requirements. It serves as an excellent portfolio piece demonstrating both technical skills and practical problem-solving abilities. 