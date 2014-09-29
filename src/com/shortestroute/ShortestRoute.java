package com.shortestroute;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class ShortestRoute {
	private int[][] matrix;
	private int matrixSize;
	private HashMap<Integer,Vertex> handled;
	private PriorityQueue<Vertex> unHandled;
	private int startVertexId;
	private int destinationVertexId;
	private ArrayList<Integer> shortestPath;
	private int[] distances;
	
	
	public ShortestRoute(int[][] matrix) {
		this.matrix = matrix;
		handled = new HashMap<Integer,Vertex>();
		unHandled = new VertexPriorityQueue<Vertex>(matrix.length, new Vertex());
		matrixSize = matrix.length;
	}
	
	protected int getSizeOfMatrix() {
		return this.matrixSize;
	}
	
	protected int getDistanceToVertex(int vertexId) {
		if(handled.get(vertexId) != null){
			return handled.get(vertexId).costFromStart;
		}
		return distances[vertexId - 1];
	}
	
	protected ArrayList<Integer> getPath() {
		return this.shortestPath;
	}
	
	protected void shortestPathBetweenNodes(int vertexA, int vertexB) {		
		startVertexId = vertexA;
		destinationVertexId = vertexB;
		distances = new int[matrixSize];
		
		for(int i = 0; i<matrixSize; i++)
			distances[i] = Integer.MAX_VALUE;
		
		distances[startVertexId - 1] = 0;
		unHandled.add(new Vertex(startVertexId, 0));
		calculateShortestDistanceFromVertex();
		shortestPath = findShortestRoutePath();
	}
		
	private void calculateShortestDistanceFromVertex() {
		while (!unHandled.isEmpty()) {
			Vertex nextVertex = getVertexWithMinDistance();
			handled.put(nextVertex.id,nextVertex);
			if (nextVertex.id == destinationVertexId)
				break;
			evaluateNeighbourVertexes(nextVertex);
		}
	}
	
	
	private Vertex getVertexWithMinDistance() {
		return unHandled.remove();
	}
	
	
	private void evaluateNeighbourVertexes(Vertex vertex) {
		for (int i = 1; i <= matrixSize; i++) {
			if (handled.get(i) == null) {
				if (matrix[vertex.id - 1][i - 1] != -1) {
					int newDistance = distances[vertex.id - 1] + matrix[vertex.id - 1][i - 1];
					if (newDistance < distances[i - 1]) {
						distances[i - 1] = newDistance;
						unHandled.add(new Vertex(i, distances[i - 1],vertex));
					}
				}
			}
		}
	}
	
	private ArrayList<Integer> findShortestRoutePath() {
		ArrayList<Integer> shortestPath = new ArrayList<>();
		Vertex destinationVertex = handled.get(destinationVertexId);
		shortestPath = buildPath(destinationVertex);
		return shortestPath;
	}
	
	private ArrayList<Integer> buildPath(Vertex vertex) {
		ArrayList<Integer> path = new ArrayList<>();
		if(vertex.id == startVertexId){
			path.add(vertex.id);
		}
		else {
			path = buildPath(vertex.previousVertex);
			path.add(vertex.id);
		}
		return path;
	}
	
	private static class Vertex implements Comparator<Vertex> {
		private int id;
		private int cost;
		private int costFromStart;
		private Vertex previousVertex;
		
		public Vertex() {}
		
		public Vertex(int id, int cost) {
			this(id,cost,null);
		}
		
		public Vertex(int id, int cost, Vertex vertex) {
			this.id = id;
			this.cost = cost;
			this.costFromStart = cost;
			this.previousVertex = vertex;
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
			
			//ArrayList<Integer> longestAndShortestPath = new ArrayList<>();
			//Integer cost = 0;
			//long start = System.currentTimeMillis();
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
			//System.out.printf("\nShortest distance from %s to %s is %s\n", 2, 81, distances[80]);			
			//System.out.printf("Shortest path is %s\n",sr.shortestPath.toString());
			
			//System.out.printf("%s",sr.reverseSteps.toString());
			//System.out.println(System.currentTimeMillis() - start);
		} 
		catch (FileNotFoundException e) {
			System.out.println("File not found\n");
			e.printStackTrace();
		}
	}
}
