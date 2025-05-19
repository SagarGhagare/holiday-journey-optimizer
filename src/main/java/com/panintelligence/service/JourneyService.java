package com.panintelligence.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class JourneyService {

    public static class Flight {
        public final String code;
        public final String from;
        public final String to;
        public final int distance;

        public Flight(String code) {
            this.code = code.trim();
            this.from = code.substring(0, 1);
            this.to = code.substring(1, 2);
            this.distance = Integer.parseInt(code.substring(2));
        }

        @Override
        public String toString() {
            return code;
        }
    }

    public static class Journey {
        public final int passengers;
        public final String from;
        public final int fromDist;
        public final String to;

        public Journey(int passengers, String from, int fromDist, String to) {
            this.passengers = passengers;
            this.from = from;
            this.fromDist = fromDist;
            this.to = to;
        }
    }

    /**
     * Loads flights from a file. Each line should be a code like "AB123"
     */
    public List<Flight> loadFlights(String filePath) {
        List<Flight> flights = new ArrayList<>();
        try {
            Files.lines(Path.of(filePath))
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(Flight::new)
                    .forEach(flights::add);
        } catch (IOException e) {
            System.err.println("Failed to read flights from " + filePath);
        }
        return flights;
    }

    /**
     * Loads journeys from a file.
     * Format: passengers,fromLocationCode,toAirport (e.g., "3,A25,B")
     */
    public List<Journey> loadJourneys(String filePath) {
        List<Journey> journeys = new ArrayList<>();
        try {
            Files.lines(Path.of(filePath))
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(line -> {
                        try {
                            journeys.add(parseJourney(line));
                        } catch (Exception ex) {
                            System.err.println("Invalid journey format: \"" + line + "\"");
                            ex.printStackTrace();
                        }
                    });
        } catch (IOException e) {
            System.err.println("Failed to read journeys from " + filePath);
        }
        return journeys;
    }

    private Journey parseJourney(String line) {
        String[] parts = line.split(",");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Expected 3 comma-separated values");
        }

        int passengers = Integer.parseInt(parts[0].trim());
        String location = parts[1].trim();
        String from = location.substring(0, 1);
        int fromDist = Integer.parseInt(location.substring(1));
        String to = parts[2].trim();

        return new Journey(passengers, from, fromDist, to);
    }
}