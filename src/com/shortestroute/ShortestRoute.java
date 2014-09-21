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
	private Set<Integer> handled;
	private PriorityQueue<Vertex> unHandled;
	private static int[] distances;
	private int destinationVertex;
	private ArrayList<Integer> shortestPath;
	
	
	public ShortestRoute(int[][] matrix) {
		this.matrix = matrix;
		handled = new HashSet<Integer>();
		unHandled = new PriorityQueue<Vertex>(matrix.length, new Vertex());
		distances = new int[matrix.length];
	}
	
	
	private void shortestPathBetweenNodes(int vertexA, int vertexB) {
		destinationVertex = vertexB;
		
		for (int i = 0; i < matrix.length; i++)
			distances[i] = Integer.MAX_VALUE;
		
		unHandled.add(new Vertex(vertexA, 0));
		distances[vertexA - 1] = 0;
		
		calculateShortestDistanceFromVertex();
		shortestPath = findShortestRoutePath(distances[vertexB - 1]);
	}
	
	
	private ArrayList<Integer> findShortestRoutePath(int destinationDistance) {
		handled.clear();
		ArrayList<Integer> shortestPath = new ArrayList<Integer>();
		func1(1); //!!!
		return shortestPath;
	}
	
	private boolean func1(int vertex){
		unHandled.clear();
		handled.add(vertex);
		shortestPath.add(vertex);
		evaluateNeighbourVertexes(vertex);
		boolean loop = true;
		while(loop){
			if(unHandled.isEmpty()){
				loop=false;
			}
			else {
				int nextVertex = getVertexWithMinDistance();
				if(distances[nextVertex -1] <= matrix[vertex-1][nextVertex-1] && distances[destinationVertex - 1] > distances[nextVertex -1]){
					if(nextVertex == destinationVertex){
						loop = false;
					}else{
						func1(nextVertex);
					}
				}
			}
		}
		shortestPath.remove(shortestPath.size() - 1 );
		return false;
		
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
		for (int i = 1; i <= matrix.length; i++) {
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
	}
	
	
	public static void main(String... args) {
		try {			
			ShortestRoute sr = new ShortestRoute(new GraphToMatrix("../AD_exercise/graphs/test_graph.txt").getMatrix());
			System.out.println("Matrix: \n");
			new GraphToMatrix("../AD_exercise/graphs/test_graph.txt").printMatrix();
			System.out.println('\n');
			sr.shortestPathBetweenNodes(1, 3);
			
			System.out.printf("Shortest distance to %s is %s\n\n", 3, distances[2]);
			
			for (int i : distances)
				System.out.println(i);
		} 
		catch (FileNotFoundException e) {
			System.out.println("File not found\n");
			e.printStackTrace();
		}
	}
}
