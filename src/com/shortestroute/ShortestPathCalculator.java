package com.shortestroute;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ShortestPathCalculator {
	private static final String HEAD_TEXT = "\n\n"
			+ "\t====================================\n"
			+ "\t||                                ||\n"
			+ "\t||    SHORTEST PATH CALCULATOR    ||\n"
			+ "\t||     (Dijkstras algorithm)      ||\n"
			+ "\t||                                ||\n"
			+ "\t====================================\n";
	
	public static void main(String... args) {
		try {
			long startTime = System.currentTimeMillis();
			ShortestRoute sr = new ShortestRoute(new GraphToMatrix("graphs/graph1.txt").getMatrix());
			ShortestRoute sr2 = new ShortestRoute(new GraphToMatrix("graphs/graph2.txt").getMatrix());
			
			System.out.println(HEAD_TEXT);
			
			if (args == null || args.length < 2) {
				System.out.println("Searching the shortest path containing the most vertexes...\n");
				System.out.println("\n... from graph1:");
				findLongestShortestPath(new GraphToMatrix("graphs/graph1.txt").getMatrix());
				System.out.println("\n... from graph2:");
				findLongestShortestPath(new GraphToMatrix("graphs/graph2.txt").getMatrix());
			}
			else {
				System.out.printf("Searching the shortest path between %s and %s...\n\n", args[0], args[1]);
				shortestPathBetweenNodes(sr, sr2, args[0], args[1]);
			}
			System.out.printf("\nTotal running time: %s (ms)\n\n\n", System.currentTimeMillis() - startTime);
		} 
		catch (FileNotFoundException e) {
			System.out.println("File not found\n");
			e.printStackTrace();
		} 
		catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}

	
	private static void shortestPathBetweenNodes(ShortestRoute sr, ShortestRoute sr2, String start, String destination) throws Exception {
		int maxSize = sr.getSizeOfMatrix();
		if (maxSize > sr2.getSizeOfMatrix())
			maxSize = sr2.getSizeOfMatrix();
		
		int startVertex = parseInt(start);
		int destinationVertex = parseInt(destination);
		if (startVertex < 1 || destinationVertex < 1 || startVertex > maxSize || destinationVertex > maxSize)
			throw new Exception("Given arguments (" + start + ", " + destination + ") are invalid: must be 1 to " + maxSize + "\n");
		
		sr.shortestPathBetweenNodes(startVertex, destinationVertex);
		System.out.println("\n... in graph1:");
		System.out.printf("\nShortest distance from %s to %s is %s\n", startVertex, destinationVertex, sr.getDistanceToVertex(destinationVertex));			
		System.out.printf("Route: %s",sr.getPath().toString());
		System.out.println("\n");
		sr2.shortestPathBetweenNodes(startVertex, destinationVertex);
		System.out.println("\n... in graph2:");
		System.out.printf("\nShortest distance from %s to %s is %s\n", startVertex, destinationVertex, sr2.getDistanceToVertex(destinationVertex));			
		System.out.printf("Route: %s",sr2.getPath().toString());
		System.out.println("\n");
	}

	
	private static void findLongestShortestPath(int[][] matrix) {
		ArrayList<Integer> longestAndShortestPath = new ArrayList<>();
		Integer cost = 0;
		ShortestRoute srNew = null;
		for(int i = 0 ; i < 128; i++) {
			srNew = new ShortestRoute(matrix);
			srNew.shortestPathBetweenNodes(i + 1);
			if(longestAndShortestPath.size() < srNew.getPath().size()) {
				longestAndShortestPath = srNew.getPath();
				cost = srNew.getDistanceToVertex(srNew.getPath().get(srNew.getPath().size() - 1));
			}
		}
		System.out.printf("\nShortest path with the most vertexes is from %s to %s, distance is %s\n", longestAndShortestPath.get(0), longestAndShortestPath.get(longestAndShortestPath.size() - 1), cost);			
		System.out.printf("Route: %s\n\n",longestAndShortestPath.toString());
	}
	
	
	private static Integer parseInt(String number) {
		if (number == null || number.isEmpty())
			return null;
		else
			return Integer.parseInt(number);
	}
	
}
