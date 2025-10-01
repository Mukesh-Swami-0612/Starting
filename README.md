# IPL Match Scheduling System

## Project Overview
A comprehensive Java-based IPL (Indian Premier League) match scheduling system that efficiently generates fair and optimized match fixtures for cricket teams.

## Features
- **Team Management**: Add, remove, and manage IPL teams
- **Match Scheduling**: Generate balanced fixtures with home/away considerations
- **Venue Management**: Assign matches to appropriate stadiums
- **Schedule Optimization**: Ensure fair distribution of matches across teams
- **Export Functionality**: Save schedules to files
- **Interactive Console Interface**: User-friendly command-line interface

## Technical Highlights
- **Data Structures**: Efficient use of ArrayList, HashMap, and custom classes
- **Algorithms**: Round-robin scheduling algorithm for fair fixture distribution
- **Object-Oriented Design**: Clean, maintainable code structure
- **Exception Handling**: Robust error handling throughout the application
- **File I/O**: Schedule export and import capabilities

## Project Structure
```
src/
├── main/
│   └── java/
│       └── com/
│           └── iplscheduler/
│               ├── model/
│               │   ├── Team.java
│               │   ├── Match.java
│               │   ├── Venue.java
│               │   └── Schedule.java
│               ├── service/
│               │   ├── SchedulingService.java
│               │   ├── TeamService.java
│               │   └── FileService.java
│               ├── util/
│               │   └── ScheduleGenerator.java
│               └── Main.java
├── test/
│   └── java/
│       └── com/
│           └── iplscheduler/
│               └── SchedulingServiceTest.java
└── resources/
    └── teams.txt
```

## How to Run
1. Compile: `javac -d bin src/main/java/com/iplscheduler/*.java src/main/java/com/iplscheduler/*/*.java`
2. Run: `java -cp bin com.iplscheduler.Main`

## Sample Output
The system generates balanced fixtures ensuring:
- Each team plays equal number of matches
- Fair distribution of home and away games
- No team plays consecutive days
- Optimal venue utilization

## Technologies Used
- Java 8+
- Object-Oriented Programming
- Data Structures & Algorithms
- File I/O Operations
- Console-based User Interface

## Learning Outcomes
- Enhanced understanding of data structures and algorithms
- Practical application of object-oriented design principles
- Real-world problem-solving skills
- Project organization and documentation 