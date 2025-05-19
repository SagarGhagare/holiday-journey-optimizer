package com.panintelligence.service;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JourneyProcessorTest {

    @Test
    void testFindFlightPath() {
        List<JourneyService.Flight> flights = List.of(
                new JourneyService.Flight("AB100"),
                new JourneyService.Flight("BC100"),
                new JourneyService.Flight("CD100"),
                new JourneyService.Flight("AD400")
        );

        JourneyProcessor processor = new JourneyProcessor(flights);
        JourneyProcessor.FlightPath path = processor.findCheapestFlightPath("A", "D", 1);

        assertNotNull(path);
        assertEquals(List.of("AB100", "BC100", "CD100"), path.flightCodes);
        assertEquals(30.0, path.totalCost);
    }

    @Test
    void testProcessJourneys_output() {
        List<JourneyService.Flight> flights = List.of(
                new JourneyService.Flight("AB100"),
                new JourneyService.Flight("BC100"),
                new JourneyService.Flight("CA100")
        );

        List<JourneyService.Journey> journeys = List.of(
                new JourneyService.Journey(2, "A", 10, "C")
        );

        JourneyProcessor processor = new JourneyProcessor(flights);

        // Just run, no assertion on System.out for now
        processor.processJourneys(journeys);
    }
}