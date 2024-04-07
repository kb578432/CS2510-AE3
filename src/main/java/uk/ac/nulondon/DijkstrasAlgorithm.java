package uk.ac.nulondon;

import java.util.*;

import static java.util.Comparator.comparing;
import static uk.ac.nulondon.CollectionUtilities.getSetDifference;
import static uk.ac.nulondon.CollectionUtilities.initializeMap;

public class DijkstrasAlgorithm {
    public static List<Node> getShortestPath(Node startNode, Node endNode, Set<Node> graph) {
        assert graph.contains(startNode) && graph.contains(endNode);

        // distances from the startNode to every other node in the graph
        Map<Node, Double> distanceMap = initializeMap(graph, node -> Double.MAX_VALUE);
        distanceMap.put(startNode, 0D);

        Set<Node> unvisitedNodes = new HashSet<>(graph);
        Node currentNode = startNode;

        while (!unvisitedNodes.isEmpty()) {
            Map<Node, Double> currentNodeDistanceMap = currentNode.getDistanceMap();
            Set<Node> adjacentNodes = currentNodeDistanceMap.keySet();
            Node closestNode = getClosestNode(currentNode, adjacentNodes);
        }
        return List.of();
    }

    private static Node getClosestNode(Node currentNode, Set<Node> adjacentNodes) {
        return adjacentNodes.stream()
                            .min(comparing(currentNode::getDistanceTo))
                            .orElseThrow();
    }
}
