package com.shortestroute;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

public class ShortestRoute {
	private int[][] matrix;
	private int matrixSize;
	private Set<Integer> handled;
	private PriorityQueue<Vertex> unHandled;
	private static int[] distances;
	private int startVertex;
	private int destinationVertex;
	private ArrayList<Integer> shortestPath;
	private int destinationDistance;
	
	
	public ShortestRoute(int[][] matrix) {
		this.matrix = matrix;
		handled = new HashSet<Integer>();
		unHandled = new VertexPriorityQueue<Vertex>(matrix.length, new Vertex());
		matrixSize = matrix.length;
		distances = new int[matrixSize];
	}
	
	
	private void shortestPathBetweenNodes(int vertexA, int vertexB) {
		startVertex = vertexA;
		destinationVertex = vertexB;
		
		for (int i = 0; i < matrixSize; i++)
			distances[i] = Integer.MAX_VALUE;
		
		unHandled.add(new Vertex(startVertex, 0));
		distances[vertexA - 1] = 0;
		
		calculateShortestDistanceFromVertex();
		destinationDistance = distances[destinationVertex -1];

		shortestPath = findShortestRoutePath();
	}
	
	
	private ArrayList<Integer> findShortestRoutePath() {
		handled.clear();
		ArrayList<Integer> shortestPath = new ArrayList<Integer>();
		shortestPath = buildPath(destinationVertex);
		return shortestPath;
	}
	
	private ArrayList<Integer> buildPath(int vertex) {
		handled.add(vertex);
		ArrayList<Integer> path = new ArrayList<Integer>();
		if(vertex == startVertex) {
			path.add(startVertex);
		}
		else {
			unHandled.clear();
			evaluateParentVertexes(vertex);
			while(!unHandled.isEmpty()){
				int nextVertex = getVertexWithMinDistance();
				if(matrix[nextVertex - 1][vertex -1] != -1 && distances[nextVertex - 1] != -1){
					if(distances[nextVertex - 1] + matrix[nextVertex - 1][vertex - 1] == distances[vertex - 1]){
						path = buildPath(nextVertex);
						path.add(vertex);
						unHandled.clear();
					}
				}
			}
		}
		return path;
	}
	
	private void evaluateParentVertexes(int vertex) {
		for (int i = 1; i <= matrixSize; i++) {
			if (!handled.contains(i)) {
				if (matrix[i - 1][vertex - 1] != -1) {
					unHandled.add(new Vertex(i, distances[i - 1]));
				}
			}
		}
	}
	
	
	private void calculateShortestDistanceFromVertex() {
		while (!unHandled.isEmpty()) {
			int nextVertex = getVertexWithMinDistance();
			/**if (nextNode == destinationNode)
				break;**/
			handled.add(nextVertex);
			evaluateNeighbourVertexes(nextVertex);
		}
	}
	
	
	private int getVertexWithMinDistance() {
		return unHandled.remove().id;
	}
	
	
	private void evaluateNeighbourVertexes(int vertex) {
		for (int i = 1; i <= matrixSize; i++) {
			if (!handled.contains(i)) {
				if (matrix[vertex - 1][i - 1] != -1) {
					int newDistance = distances[vertex - 1] + matrix[vertex - 1][i - 1];
					if (newDistance < distances[i - 1]) {
						distances[i - 1] = newDistance;
					}
					unHandled.add(new Vertex(i, distances[i - 1]));
				}
			}
		}
	}
	
	
	private static class Vertex implements Comparator<Vertex> {
		private int id;
		private int cost;
		
		public Vertex() {}
		
		public Vertex(int id, int cost) {
			this.id = id;
			this.cost = cost;
		}
	   
		@Override
	    public int compare(Vertex vertexA, Vertex vertexB) {
			return (vertexA.cost < vertexB.cost) ? -1 : (vertexA.cost > vertexB.cost) ? 1 : 0;
	    }
		
		@Override
		public boolean equals(Object o) {
			if (o == null)
				return false;
			if (!(o instanceof Vertex))
				return false;				
			Vertex v = (Vertex)o;
			if (this.id == v.id)
				return true;
			return false;
		}
		
	}
	
	
	@SuppressWarnings({ "serial", "hiding" })
	private static class VertexPriorityQueue<Vertex> extends PriorityQueue<Vertex> {
		
		public VertexPriorityQueue(int size, Comparator<? super Vertex> vertex) {
			super(size, vertex);
		}
		
		@Override
		public boolean add(Vertex vertex) {
			if (!super.contains(vertex)) {
				return super.add(vertex);
			}
			return false;
		}	
	}
	
	
	public static void main(String... args) {
		try {			
			ShortestRoute sr = new ShortestRoute(new GraphToMatrix("../AD_exercise/graphs/graph1.txt").getMatrix());
			System.out.println("Matrix: \n");
			new GraphToMatrix("../AD_exercise/graphs/graph1.txt").printMatrix();
			System.out.println('\n');
			sr.shortestPathBetweenNodes(1, 81);
			
			System.out.printf("Shortest distance from %s to %s is %s\n\n", 1 , 81, distances[80]);

			
			for (int i : distances)
				System.out.println(i);
			
			System.out.printf("\n\nShortest path is %s",sr.shortestPath.toString());
			
		} 
		catch (FileNotFoundException e) {
			System.out.println("File not found\n");
			e.printStackTrace();
		}
	}
}
