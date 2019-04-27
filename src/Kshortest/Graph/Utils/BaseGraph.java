
package Kshortest.Graph.Utils;

import java.util.List;
import java.util.Set;



public interface BaseGraph {
	
	List<BaseVertex> getVertexList();
	
	double getEdgeWeight(BaseVertex source, BaseVertex sink);
	Set<BaseVertex> getAdjacentVertices(BaseVertex vertex);
	Set<BaseVertex> getPrecedentVertices(BaseVertex vertex);
	
}
