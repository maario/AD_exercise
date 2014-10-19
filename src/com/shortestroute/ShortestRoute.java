package com.shortestroute;

import java.util.ArrayList;
import java.util.Collections;
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
		return distances[vertexId - 1];
	}
	
	
	protected ArrayList<Integer> getPath() {
		return this.shortestPath;
	}
	
	protected void shortestPathBetweenNodes(int startVertex) {
		shortestPathBetweenNodes(startVertex,-1);
	}
	
	protected void shortestPathBetweenNodes(int vertexA, int vertexB) {		
		startVertexId = vertexA;
		destinationVertexId = vertexB;
		distances = new int[matrixSize];
		
		for (int i = 0; i<matrixSize; i++)
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
		if(destinationVertexId == -1){
			for(Integer key: handled.keySet()) {
				ArrayList<Integer> tempPath = buildPath(handled.get(key));
				if(shortestPath.size() < tempPath.size()) {
					shortestPath = tempPath;
				}
			}
		}else{
			Vertex destinationVertex = handled.get(destinationVertexId);
			shortestPath = buildPath(destinationVertex);
		}
		return shortestPath;
	}
	
	private ArrayList<Integer> buildPath(Vertex vertex) {
		ArrayList<Integer> path = new ArrayList<>();
		Vertex v1 = vertex;
		while(v1.id != startVertexId) {
			path.add(v1.id);
			v1 = v1.previousVertex;
		}
		path.add(v1.id);
		Collections.reverse(path);
		return path;
	}

	
	
	private static class Vertex implements Comparator<Vertex> {
		private int id;
		private int cost;
		private Vertex previousVertex;
		
		public Vertex() {}
		
		public Vertex(int id, int cost) {
			this(id,cost,null);
		}
		
		public Vertex(int id, int cost, Vertex vertex) {
			this.id = id;
			this.cost = cost;
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

		public int getId() {
			return this.id;
		}
	}
	
	
	@SuppressWarnings({ "serial", "hiding" })
	private static class VertexPriorityQueue<Vertex> extends PriorityQueue<Vertex> {
		private boolean[] vertexInQueue;
		public VertexPriorityQueue(int size, Comparator<? super Vertex> vertex) {
			super(size, vertex);
			vertexInQueue = new boolean[size+1];
		}
		
		@Override
		public boolean add(Vertex vertex) {
			if(vertexInQueue[((com.shortestroute.ShortestRoute.Vertex) vertex).getId()] == true) {
				super.remove(vertex);
			}
			else {
				vertexInQueue[((com.shortestroute.ShortestRoute.Vertex) vertex).getId()] = true;
			}
			return super.add(vertex);
		}
		public Vertex remove() {
			Vertex v = super.remove();
			vertexInQueue[((com.shortestroute.ShortestRoute.Vertex) v).getId()] = false;
			return v;
		}
	}
}