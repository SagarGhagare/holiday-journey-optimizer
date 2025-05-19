# Holiday Journey Optimizer

This Java application computes the cheapest return holiday journey cost including:
- Vehicle transport to/from the airport (car or taxi)
- Outbound and inbound flights (direct or connecting)

## Requirements
- Java 17+
- Maven

## How to Run

```
mvn clean compile exec:java -Dexec.mainClass="com.panintelligence.Main"
```

## Sample Output
Calculates and fills missing journey rows (8–10) using Dijkstra's algorithm for pathfinding.
