package com.panintelligence.service;

import java.util.*;

public class JourneyProcessor {

    public static final class FlightPath {
        List<String> flightCodes;
        double totalCost;

        FlightPath(List<String> flightCodes, double totalCost) {
            this.flightCodes = flightCodes;
            this.totalCost = totalCost;
        }
    }

    private static final class FlightNode {
        String airport;
        List<String> path;
        double cost;

        FlightNode(String airport, List<String> path, double cost) {
            this.airport = airport;
            this.path = path;
            this.cost = cost;
        }
    }

    private final Map<String, List<JourneyService.Flight>> flightGraph = new HashMap<>();

    public JourneyProcessor(List<JourneyService.Flight> flights) {
        for (JourneyService.Flight flight : flights) {
            flightGraph
                    .computeIfAbsent(flight.from, k -> new ArrayList<>())
                    .add(flight);
        }
    }

    public void processJourneys(List<JourneyService.Journey> journeys) {
        int rowNumber = 1;
        for (JourneyService.Journey journey : journeys) {

            double carCost = journey.passengers <= 4
                    ? 0.2 * journey.fromDist * 2 + 3.0
                    : Double.MAX_VALUE;
            double taxiCost = 0.4 * journey.fromDist * 2 * Math.ceil(journey.passengers / 4.0);
            String chosenVehicle = carCost <= taxiCost ? "Car" : "Taxi";
            double vehicleCost = roundToTwoDecimalPlaces(Math.min(carCost, taxiCost));

            FlightPath outbound = findCheapestFlightPath(journey.from, journey.to, journey.passengers);
            FlightPath inbound = findCheapestFlightPath(journey.to, journey.from, journey.passengers);

            String outboundRoute = outbound != null
                    ? String.join("--", outbound.flightCodes)
                    : "No outbound flight";
            double outboundCost = outbound != null ? outbound.totalCost : 0.0;

            String inboundRoute = inbound != null
                    ? String.join("--", inbound.flightCodes)
                    : "No inbound flight";
            double inboundCost = inbound != null ? inbound.totalCost : 0.0;

            double totalJourneyCost = (outbound != null && inbound != null)
                    ? roundToTwoDecimalPlaces(vehicleCost + outboundCost + inboundCost)
                    : 0.0;

            System.out.printf(
                    "|%-3d| %-7s| %19.2f| %-24s| %13.2f| %-22s| %12.2f| %10.2f|%n",
                    rowNumber++, chosenVehicle, vehicleCost,
                    outboundRoute, outboundCost,
                    inboundRoute, inboundCost,
                    totalJourneyCost
            );
        }
    }

    FlightPath findCheapestFlightPath(String start, String destination, int passengers) {
        PriorityQueue<FlightNode> queue = new PriorityQueue<>(Comparator.comparingDouble(n -> n.cost));
        Map<String, Double> visited = new HashMap<>();

        queue.add(new FlightNode(start, new ArrayList<>(), 0.0));

        while (!queue.isEmpty()) {
            FlightNode current = queue.poll();

            if (current.airport.equals(destination)) {
                return new FlightPath(current.path, roundToTwoDecimalPlaces(current.cost));
            }

            if (visited.getOrDefault(current.airport, Double.MAX_VALUE) <= current.cost) {
                continue;
            }
            visited.put(current.airport, current.cost);

            for (JourneyService.Flight flight : flightGraph.getOrDefault(current.airport, Collections.emptyList())) {
                double segmentCost = 0.1 * passengers * flight.distance;
                List<String> newPath = new ArrayList<>(current.path);
                newPath.add(flight.code);
                queue.add(new FlightNode(flight.to, newPath, current.cost + segmentCost));
            }
        }

        return null;
    }

    private double roundToTwoDecimalPlaces(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}