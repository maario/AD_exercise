package com.shortestroute;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ShortestPathCalculator {
	
	public static void main(String... args) {
		try {
			long startTime = System.currentTimeMillis();
			ShortestRoute sr = new ShortestRoute(new GraphToMatrix("graphs/graph1.txt").getMatrix());
			ShortestRoute sr2 = new ShortestRoute(new GraphToMatrix("graphs/graph2.txt").getMatrix());
			
			System.out.println("\n\n");
			System.out.println("=====================================");
			System.out.println("||                                 ||");
			System.out.println("||    SHORTEST PATH CALCULATOR     ||");
			System.out.println("||                                 ||");
			System.out.println("=====================================");
			System.out.println("\n\n");
			
			if (args == null || args.length < 2) {
				System.out.println("Searching the shortest path containing the most vertexes...\n");
				System.out.println("... from graph1:");
				findLongestShortestPath(sr);
				System.out.println("\n... from graph2:");
				findLongestShortestPath(sr2);
			}
			else {
				System.out.printf("Searching the shortest path between %s and %s...\n", args[0], args[1]);
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
		System.out.println("... in graph1:\n");
		System.out.printf("\nShortest distance from %s to %s is %s\n", startVertex, destinationVertex, sr.getDistanceToVertex(destinationVertex));			
		System.out.printf("Shortest path is %s\n",sr.getPath().toString());
		System.out.println("\n");
		sr2.shortestPathBetweenNodes(startVertex, destinationVertex);
		System.out.println("... in graph2:\n");
		System.out.printf("\nShortest distance from %s to %s is %s\n", startVertex, destinationVertex, sr2.getDistanceToVertex(destinationVertex));			
		System.out.printf("Shortest path is %s\n",sr2.getPath().toString());
		System.out.println("\n");
	}

	
	private static void findLongestShortestPath(ShortestRoute sr) {
		ArrayList<Integer> longestAndShortestPath = new ArrayList<>();
		Integer cost = 0;
		ShortestRoute srNew = sr;
		for (int i = 0; i < 128; i++) {
			for (int j = 0; j < 128; j++) {
				if (i != j) {
					srNew = sr;
					srNew.shortestPathBetweenNodes(i + 1, j + 1);
					if(longestAndShortestPath.size() < srNew.getPath().size()) {
						longestAndShortestPath = srNew.getPath();
						cost = srNew.getDistanceToVertex(j + 1);
					}
				}
			}
		}
		System.out.printf("\nShortest distance from %s to %s is %s\n", longestAndShortestPath.get(0), longestAndShortestPath.get(longestAndShortestPath.size() - 1), cost);			
		System.out.printf("Shortest path is %s\n",longestAndShortestPath.toString());
	}
	
	
	private static Integer parseInt(String number) {
		if(number == null || number.isEmpty())
			return null;
		else
			return Integer.parseInt(number);
	}
	
}
