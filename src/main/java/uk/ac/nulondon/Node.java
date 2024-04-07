package uk.ac.nulondon;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Node {
    private final Map<Node, Double> distanceMap = new HashMap<>();

    public void addAdjacentNode(Node adjacentNode, double distance) {
        distanceMap.put(adjacentNode, distance);
    }

    public double getDistanceTo(Node adjacentNode) {
        assert distanceMap.containsKey(adjacentNode);

        return distanceMap.get(adjacentNode);
    }

    public Map<Node, Double> getDistanceMap() {return new HashMap<>(distanceMap);}
}
