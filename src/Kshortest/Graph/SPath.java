
package Kshortest.Graph;

import java.util.List;
import java.util.Vector;

import Kshortest.Graph.Utils.BaseElementWithWeight;
import Kshortest.Graph.Utils.BaseVertex;


public class SPath implements BaseElementWithWeight {
	
	private List<BaseVertex> vertexList = new Vector<BaseVertex>();
	private double weight = -1;
	
	public SPath() { }
	
	public SPath(List<BaseVertex> vertexList, double weight) {
		this.vertexList = vertexList;
		this.weight = weight;
	}

	public double getWeight() {
		return weight;
	}
	
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public List<BaseVertex> getVertexList() {
		return vertexList;
	}
	
	@Override
	public boolean equals(Object right) {
		
		if (right instanceof SPath) {
			SPath rPath = (SPath) right;
			return vertexList.equals(rPath.vertexList);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return vertexList.hashCode();
	}
	
	public String toString() {
		return vertexList.toString() + ":" + weight;
	}
}
