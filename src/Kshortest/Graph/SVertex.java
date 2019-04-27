
package Kshortest.Graph;

import Kshortest.Graph.Utils.BaseVertex;


public class SVertex implements BaseVertex, Comparable<SVertex> {
	
	private static int currentVertexNum = 0; // Uniquely identify each vertex
	private int id = currentVertexNum++;
	private double weight = 0;
	
	public int getId() {
		return id;
	}

	public String toString() {
		return "" + id;
	}

	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double status) {
		weight = status;
	}
	
	public int compareTo(SVertex rVertex) {
		double diff = this.weight - rVertex.weight;
		if (diff > 0) {
			return 1;
		} else if (diff < 0) {
			return -1;
		} else { 
			return 0;
		}
	}
	
	public static void reset() {
		currentVertexNum = 0;
	}
}
