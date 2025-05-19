package com.panintelligence;

import com.panintelligence.service.JourneyProcessor;
import com.panintelligence.service.JourneyService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        JourneyService journeyService = new JourneyService();

        // Load flights and journeys from files
        List<JourneyService.Flight> flights = journeyService.loadFlights("src/main/resources/flights.txt");
        List<JourneyService.Journey> journeys = journeyService.loadJourneys("src/main/resources/journeys.txt");

        // Create processor with flight data
        JourneyProcessor processor = new JourneyProcessor(flights);

        // Process and print journey cost summaries
        processor.processJourneys(journeys);
    }
}
