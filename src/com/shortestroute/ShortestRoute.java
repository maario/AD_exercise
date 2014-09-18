package com.shortestroute;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class ShortestRoute {
	private Set<Vertex> visited;
	private int[][] matrix;
	
	public ShortestRoute(int[][] matrix) {
		this.matrix = matrix;
	}
	
	private void shortestPathBetweenNodes(int vertexA, int vertexB) {
		visited = new HashSet<Vertex>();
		Vertex start = new Vertex(vertexA, 0);
		Vertex goal = new Vertex(vertexB, 0);
		visited.add(start);
		int currentBest = matrix[0][80];
	}
	
	private void shortestPathWithMostNodes() {
		
	}
	
	private static class Vertex {
		private int id;
		private int cost;
		
		public Vertex(int id, int cost) {
			this.id = id;
			this.cost = cost;
		}
	}
	
	public static void main(String... args) {
		try {
			ShortestRoute sr = new ShortestRoute(new GraphToMatrix("../AD_exercise/graphs/graph1.txt").getMatrix());
			sr.shortestPathBetweenNodes(1, 81);
		} 
		catch (FileNotFoundException e) {
			System.out.println("File not found");
			e.printStackTrace();
		}
	}
}
