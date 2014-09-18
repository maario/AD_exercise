package com.shortestroute;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ShortestRoute {
	private Set<Vertex> visited;
	private ArrayList<Vertex> accepted;
	private int[][] matrix;
	
	public ShortestRoute(int[][] matrix) {
		this.matrix = matrix;
	}
	
	private void shortestPathBetweenNodes(int vertexA, int vertexB) {
		visited = new HashSet<Vertex>();
		accepted = new ArrayList<Vertex>();
		Vertex start = new Vertex(vertexA, 0);
		Vertex goal = new Vertex(vertexB, 0);
		visited.add(start);
		int currentBest = matrix[vertexA - 1][vertexB - 1];
		
		for (int i = 0; i < matrix.length; ++i) {
			if (matrix[vertexA - 1][i] != 0 && i+1 != vertexA)
				accepted.add(new Vertex(i + 1, matrix[vertexA - 1][i]));
				//accepted.add(i + 1, matrix[vertexA - 1][i]);
		}
		Collections.sort(accepted);
		visited.add(accepted.get(0));
		
		for (int i = 0; i < accepted.size(); i++)
			System.out.println(accepted.get(i).cost);
	}
	
	private void shortestPathWithMostNodes() {
		
	}
	
	private static class Vertex implements Comparable<Vertex> {
		private int id;
		private int cost;
		
		public Vertex(int id, int cost) {
			this.id = id;
			this.cost = cost;
		}
		
		public int compareTo(Vertex vertex) {
		       return (this.cost < vertex.cost ) ? -1 : (this.cost > vertex.cost) ? 1 : 0;
		}
	}
	
	public static void main(String... args) {
		try {
			ShortestRoute sr = new ShortestRoute(new GraphToMatrix("../AD_exercise/graphs/testGraph.txt").getMatrix());
//			ShortestRoute sr = new ShortestRoute(new GraphToMatrix("../AD_exercise/graphs/graph1.txt").getMatrix());
			sr.shortestPathBetweenNodes(1, 3);
		} 
		catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
	}
}
