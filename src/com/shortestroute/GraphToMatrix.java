package com.shortestroute;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


public class GraphToMatrix {
	private BufferedReader br;
	private String line;
	private int[][] matrix;
	private static int size = 0;
	
	public GraphToMatrix(String pathToFile) throws FileNotFoundException{
		br = new BufferedReader(new FileReader(pathToFile));
		int x = 0;
		int y = 0;
		try {
			line = br.readLine();
			size = Integer.parseInt(line);
			matrix = new int[size][size];
			line = br.readLine();
			int count = 0;
			while(count < size*size){
				if(parseInt(line) == null){
					line = br.readLine();
				}else{
					matrix[x][y] = parseInt(line);
					if(y == 127){
						x++;
						y = 0;
					}else{
						y++;
					}
					line = br.readLine();
					count++;
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static Integer parseInt(String line){
		if(line == null || line.isEmpty()){
			return null;
		}else{
			return Integer.parseInt(line);
		}
	}
	public int[][] getMatrix(){
		return matrix;
	}
	public int getSizeOfMatrix(){
		return size;
	}
	
	public static void main(String[] args){
		try {
			GraphToMatrix g = new GraphToMatrix("../AD_exercise/graphs/graph1.txt");
			int[][] matrix = g.getMatrix();
			for(int[] array: matrix){
				System.out.println(Arrays.toString(array));
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
