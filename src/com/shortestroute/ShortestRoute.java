package com.shortestroute;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
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
	private HashMap<Integer,Integer> reverseSteps;
	
	
	public ShortestRoute(int[][] matrix) {
		this.matrix = matrix;
		handled = new HashSet<Integer>();
		unHandled = new VertexPriorityQueue<Vertex>(matrix.length, new Vertex());
		matrixSize = matrix.length;
		distances = new int[matrixSize];
		reverseSteps = new HashMap<Integer,Integer>();
	}
	
	
	protected int getSizeOfMatrix() {
		return this.matrixSize;
	}
	
	
	protected int getDistanceToVertex(int vertex) {
		return distances[vertex - 1];
	}
	
	
	protected ArrayList<Integer> getPath() {
		return this.shortestPath;
	}
	
	
	protected void shortestPathBetweenNodes(int vertexA, int vertexB) {		
		startVertex = vertexA;
		destinationVertex = vertexB;
		
		for (int i = 0; i < matrixSize; i++)
			distances[i] = Integer.MAX_VALUE;
		
		unHandled.add(new Vertex(startVertex, 0));
		distances[startVertex - 1] = 0;
		calculateShortestDistanceFromVertex();
		shortestPath = findShortestRoutePath();
	}
	
	private ArrayList<Integer> findShortestRoutePath() {
		ArrayList<Integer> shortestPath = new ArrayList<>();
		shortestPath = buildPath(destinationVertex);
		return shortestPath;
	}
	
	private ArrayList<Integer> buildPath(int vertex) {
		ArrayList<Integer> path = new ArrayList<>();
		if(vertex == startVertex){
			path.add(startVertex);
		}
		else {
			path = buildPath(reverseSteps.get(vertex));
			path.add(vertex);
		}
		return path;
	}
	
	private void calculateShortestDistanceFromVertex() {
		while (!unHandled.isEmpty()) {
			int nextVertex = getVertexWithMinDistance();
			if (nextVertex == destinationVertex)
				break;
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
						reverseSteps.put(i, vertex);
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
			else {
				super.remove(vertex);
				return super.add(vertex);
			}
		}	
	}
	
	
	public static void main(String... args) {
		try {			
			ShortestRoute sr = null; 
			System.out.println("Matrix: \n");
			new GraphToMatrix("graphs/graph1.txt").printMatrix();
			System.out.println('\n');
			
			ArrayList<Integer> longestAndShortestPath = new ArrayList<>();
			Integer cost = 0;
			long start = System.currentTimeMillis();
			/*for (int i = 0; i < 128; i++) {
				for (int j = 0; j < 128; j++) {
					if (i != j) {
						sr = new ShortestRoute(new GraphToMatrix("graphs/graph1.txt").getMatrix());
						sr.shortestPathBetweenNodes(i + 1, j + 1);
						if(longestAndShortestPath.size() < sr.shortestPath.size()) {
							longestAndShortestPath = sr.shortestPath;
							cost = distances[j];
						}
						//System.out.printf("\nShortest distance from %s to %s is %s\n", i + 1, j + 1, distances[j]);			
						//System.out.printf("Shortest path is %s\n",sr.shortestPath.toString());
					}
				}
			}
			System.out.printf("\nShortest distance from %s to %s is %s\n", longestAndShortestPath.get(0), longestAndShortestPath.get(longestAndShortestPath.size() - 1), cost);			
			System.out.printf("Shortest path is %s\n",longestAndShortestPath.toString());
			*/
			
			
			sr = new ShortestRoute(new GraphToMatrix("graphs/graph1.txt").getMatrix());
			sr.shortestPathBetweenNodes(2, 1);
			System.out.printf("\nShortest distance from %s to %s is %s\n", 2, 81, distances[80]);			
			System.out.printf("Shortest path is %s\n",sr.shortestPath.toString());
			
			System.out.printf("%s",sr.reverseSteps.toString());
			System.out.println(System.currentTimeMillis() - start);
		} 
		catch (FileNotFoundException e) {
			System.out.println("File not found\n");
			e.printStackTrace();
		}
	}
}
