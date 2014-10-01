package com.shortestroute;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class ShortestPathCalculator {
	private static final String NUMBER_REGEX = "\\d+";
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
			
			if (args == null || args.length < 2) {
				printHeader();
				System.out.println("Searching the shortest path containing the most vertexes...\n");
				System.out.println("\n... from graph1 (complete graph):");
				findLongestShortestPath(new GraphToMatrix("graphs/graph1.txt").getMatrix());
				System.out.println("\n... from graph2 (incomplete graph):");
				findLongestShortestPath(new GraphToMatrix("graphs/graph2.txt").getMatrix());
			}
			else if (args.length == 2 && isNumber(args[0]) && isNumber(args[1])) {
				printHeader();
				System.out.printf("Searching the shortest path between %s and %s...\n\n", args[0], args[1]);
				System.out.println("\n... from graph1 (complete graph):");
				shortestPathBetweenNodes(new GraphToMatrix("graphs/graph1.txt").getMatrix(), args[0], args[1]);
				System.out.println("\n... from graph2 (incomplete graph):");
				shortestPathBetweenNodes(new GraphToMatrix("graphs/graph2.txt").getMatrix(), args[0], args[1]);
			}
			else {
				System.out.printf("ERROR! Invalid parameters given %s. Parameter validation:\nCorrect amount: 0 or 2\nFormat: numeric\n\n", Arrays.toString(args));
				return;
			}
			
			System.out.printf("\nTotal running time: %s (ms)\n\n\n", System.currentTimeMillis() - startTime);
		} 
		catch (FileNotFoundException e) {
			System.out.println("File not found\n");
		} 
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	
	private static void printHeader() {
		System.out.println(HEAD_TEXT);
	}

	
	private static boolean isNumber(String text) {
		return text.matches(NUMBER_REGEX);
	}
	
	
	private static void shortestPathBetweenNodes(int[][] matrix, String start, String destination) throws Exception {
		ShortestRoute sr = new ShortestRoute(matrix);
		int maxSize = sr.getSizeOfMatrix();
		int startVertex = parseInt(start);
		int destinationVertex = parseInt(destination);
		if (startVertex < 1 || destinationVertex < 1 || startVertex > maxSize || destinationVertex > maxSize)
			throw new Exception("Given arguments (" + start + ", " + destination + ") are invalid: must be 1 to " + maxSize + "\n");
		
		sr.shortestPathBetweenNodes(startVertex, destinationVertex);
		System.out.printf("\nShortest distance from %s to %s is %s\n", startVertex, destinationVertex, sr.getDistanceToVertex(destinationVertex));			
		System.out.printf("Route: %s",sr.getPath().toString());
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
