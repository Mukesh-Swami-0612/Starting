package com.iplscheduler.util;

import com.iplscheduler.model.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Utility class for generating IPL match schedules using round-robin algorithm
 */
public class ScheduleGenerator {
    
    /**
     * Generate a complete IPL schedule for the given teams
     */
    public static Schedule generateSchedule(List<Team> teams, String season, LocalDate startDate) {
        if (teams == null || teams.size() < 2) {
            throw new IllegalArgumentException("At least 2 teams are required to generate a schedule");
        }
        
        Schedule schedule = new Schedule(season);
        List<Venue> venues = createVenuesFromTeams(teams);
        
        // Generate round-robin fixtures
        List<MatchFixture> fixtures = generateRoundRobinFixtures(teams);
        
        // Assign dates and venues to fixtures
        LocalDate currentDate = startDate;
        int matchNumber = 1;
        
        for (MatchFixture fixture : fixtures) {
            // Find appropriate venue
            Venue venue = findBestVenue(fixture, venues);
            
            // Determine match type
            String matchType = determineMatchType(fixture, venue);
            
            // Create match
            Match match = new Match(
                fixture.getTeam1(),
                fixture.getTeam2(),
                venue,
                currentDate,
                matchType,
                matchNumber++
            );
            
            schedule.addMatch(match);
            
            // Move to next date (every 2 days to avoid consecutive matches)
            currentDate = currentDate.plusDays(2);
        }
        
        return schedule;
    }
    
    /**
     * Generate round-robin fixtures ensuring each team plays every other team
     */
    private static List<MatchFixture> generateRoundRobinFixtures(List<Team> teams) {
        List<MatchFixture> fixtures = new ArrayList<>();
        int numTeams = teams.size();
        
        // If odd number of teams, add a "bye" team
        if (numTeams % 2 == 1) {
            teams.add(new Team("BYE", "BYE", "BYE", "BYE"));
            numTeams++;
        }
        
        // Generate fixtures using round-robin algorithm
        for (int round = 0; round < numTeams - 1; round++) {
            for (int i = 0; i < numTeams / 2; i++) {
                int team1Index = i;
                int team2Index = numTeams - 1 - i;
                
                // Skip if either team is "BYE"
                if (!teams.get(team1Index).getName().equals("BYE") && 
                    !teams.get(team2Index).getName().equals("BYE")) {
                    
                    fixtures.add(new MatchFixture(teams.get(team1Index), teams.get(team2Index)));
                }
            }
            
            // Rotate teams for next round (keep first team fixed)
            rotateTeams(teams);
        }
        
        // Remove "BYE" team if it was added
        if (teams.size() > numTeams - 1) {
            teams.removeIf(team -> team.getName().equals("BYE"));
        }
        
        return fixtures;
    }
    
    /**
     * Rotate teams for round-robin algorithm
     */
    private static void rotateTeams(List<Team> teams) {
        if (teams.size() <= 2) return;
        
        Team lastTeam = teams.remove(teams.size() - 1);
        teams.add(1, lastTeam);
    }
    
    /**
     * Create venues from team home venues
     */
    private static List<Venue> createVenuesFromTeams(List<Team> teams) {
        List<Venue> venues = new ArrayList<>();
        Set<String> venueNames = new HashSet<>();
        
        for (Team team : teams) {
            String venueName = team.getHomeVenue();
            if (!venueNames.contains(venueName)) {
                venues.add(new Venue(venueName, team.getCity(), 50000));
                venueNames.add(venueName);
            }
        }
        
        return venues;
    }
    
    /**
     * Find the best venue for a match
     */
    private static Venue findBestVenue(MatchFixture fixture, List<Venue> venues) {
        // Prefer home venue of one of the teams
        for (Venue venue : venues) {
            if (venue.getName().equals(fixture.getTeam1().getHomeVenue()) ||
                venue.getName().equals(fixture.getTeam2().getHomeVenue())) {
                return venue;
            }
        }
        
        // Fallback to first available venue
        return venues.get(0);
    }
    
    /**
     * Determine match type based on venue
     */
    private static String determineMatchType(MatchFixture fixture, Venue venue) {
        if (venue.getName().equals(fixture.getTeam1().getHomeVenue())) {
            return "Home";
        } else if (venue.getName().equals(fixture.getTeam2().getHomeVenue())) {
            return "Away";
        } else {
            return "Neutral";
        }
    }
    
    /**
     * Generate schedule with specific constraints
     */
    public static Schedule generateConstrainedSchedule(List<Team> teams, String season, 
                                                     LocalDate startDate, int maxMatchesPerDay) {
        Schedule schedule = generateSchedule(teams, season, startDate);
        
        // Group matches by date and ensure max matches per day
        Map<LocalDate, List<Match>> matchesByDate = new HashMap<>();
        for (Match match : schedule.getMatches()) {
            matchesByDate.computeIfAbsent(match.getDate(), k -> new ArrayList<>()).add(match);
        }
        
        // Redistribute matches if needed
        for (Map.Entry<LocalDate, List<Match>> entry : matchesByDate.entrySet()) {
            if (entry.getValue().size() > maxMatchesPerDay) {
                redistributeMatches(entry.getValue(), entry.getKey(), maxMatchesPerDay, schedule);
            }
        }
        
        return schedule;
    }
    
    /**
     * Redistribute matches to avoid too many matches per day
     */
    private static void redistributeMatches(List<Match> matches, LocalDate originalDate, 
                                          int maxMatchesPerDay, Schedule schedule) {
        int extraMatches = matches.size() - maxMatchesPerDay;
        LocalDate newDate = originalDate.plusDays(1);
        
        for (int i = 0; i < extraMatches; i++) {
            Match match = matches.get(matches.size() - 1 - i);
            match.setDate(newDate);
            newDate = newDate.plusDays(1);
        }
    }
    
    /**
     * Inner class to represent a match fixture
     */
    private static class MatchFixture {
        private final Team team1;
        private final Team team2;
        
        public MatchFixture(Team team1, Team team2) {
            this.team1 = team1;
            this.team2 = team2;
        }
        
        public Team getTeam1() { return team1; }
        public Team getTeam2() { return team2; }
    }
} 