
package Kshortest.Graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import Kshortest.Graph.Utils.BaseVertex;
import Kshortest.Graph.Algs.DijShortestPathAlg;
import Kshortest.Graph.Base.Pair;


public class VarGraph extends SGraph {
	private Set<Integer> remVertexIdSet = new HashSet<Integer>();
	private Set<Pair<Integer, Integer>> remEdgeSet = new HashSet<Pair<Integer, Integer>>();

	/**
	 * Default constructor
	 */
	public VarGraph() { }
	
	/**
	 * Constructor 1
	 * 
	 * @param dataFileName
	 */
	public VarGraph(String dataFileName)	{
		super(dataFileName);
	}
	
	/**
	 * Constructor 2
	 * 
	 * @param graph
	 */
	public VarGraph(SGraph graph) {
		super(graph);
	}

	/**
	 * Set the set of vertices to be removed from the graph
	 * 
	 * @param remVertexList
	 */
	public void setDelVertexIdList(Collection<Integer> remVertexList) {
		this.remVertexIdSet.addAll(remVertexList);
	}

	/**
	 * Set the set of edges to be removed from the graph
	 * 
	 * @param _rem_edge_hashcode_set
	 */
	public void setDelEdgeHashcodeSet(Collection<Pair<Integer, Integer>> remEdgeCollection) {
		remEdgeSet.addAll(remEdgeCollection);
	}
	
	/**
	 * Add an edge to the set of removed edges
	 * 
	 * @param edge
	 */
	public void deleteEdge(Pair<Integer, Integer> edge) {
		remEdgeSet.add(edge);
	}
	
	/**
	 * Add a vertex to the set of removed vertices
	 * 
	 * @param vertexId
	 */
	public void deleteVertex(Integer vertexId) {
		remVertexIdSet.add(vertexId);
	}
	
	public void recoverDeletedEdges() {
		remEdgeSet.clear();
	}

	public void recoverDeletedEdge(Pair<Integer, Integer> edge)	{
		remEdgeSet.remove(edge);
	}
	
	public void recoverDeletedVertices() {
		remVertexIdSet.clear();
	}
	
	public void recoverDeletedVertex(Integer vertexId) {
		remVertexIdSet.remove(vertexId);
	}
	
	/**
	 * Return the weight associated with the input edge.
	 * 
	 * @param source
	 * @param sink
	 * @return
	 */
	public double getEdgeWeight(BaseVertex source, BaseVertex sink)	{
		int sourceId = source.getId();
		int sinkId = sink.getId();
		
		if (remVertexIdSet.contains(sourceId) || remVertexIdSet.contains(sinkId) ||
		   remEdgeSet.contains(new Pair<Integer, Integer>(sourceId, sinkId))) {
			return SGraph.DISCONNECTED;
		}
		return super.getEdgeWeight(source, sink);
	}

	/**
	 * Return the weight associated with the input edge.
	 * 
	 * @param source
	 * @param sink
	 * @return
	 */
	public double getEdgeWeightOfGraph(BaseVertex source, BaseVertex sink) {
		return super.getEdgeWeight(source, sink);
	}
	
	/**
	 * Return the set of fan-outs of the input vertex.
	 * 
	 * @param vertex
	 * @return
	 */
	public Set<BaseVertex> getAdjacentVertices(BaseVertex vertex) {
		Set<BaseVertex> retSet = new HashSet<BaseVertex>();
		int startingVertexId = vertex.getId();
		if (!remVertexIdSet.contains(startingVertexId))	{
			Set<BaseVertex> adjVertexSet = super.getAdjacentVertices(vertex);
			for (BaseVertex curVertex : adjVertexSet) {
				int endingVertexId = curVertex.getId();
				if (remVertexIdSet.contains(endingVertexId) ||
					remEdgeSet.contains(new Pair<Integer,Integer>(startingVertexId, endingVertexId))) {
					continue;
				}
				// 
				retSet.add(curVertex);
			}
		}
		return retSet;
	}

	/**
	 * Get the set of vertices preceding the input vertex.
	 * 
	 * @param vertex
	 * @return
	 */
	public Set<BaseVertex> getPrecedentVertices(BaseVertex vertex) {
		Set<BaseVertex> retSet = new HashSet<BaseVertex>();
		if (!remVertexIdSet.contains(vertex.getId())) {
			int endingVertexId = vertex.getId();
			Set<BaseVertex> preVertexSet = super.getPrecedentVertices(vertex);
			for (BaseVertex curVertex : preVertexSet) {
				int startingVertexId = curVertex.getId();
				if (remVertexIdSet.contains(startingVertexId) ||
					remEdgeSet.contains(new Pair<Integer, Integer>(startingVertexId, endingVertexId))) {
					continue;
				}
				//
				retSet.add(curVertex);
			}
		}
		return retSet;
	}

	/**
	 * Get the list of vertices in the graph, except those removed.
	 * @return
	 */
	public List<BaseVertex> getVertexList() {
		List<BaseVertex> retList = new Vector<BaseVertex>();
		for (BaseVertex curVertex : super.getVertexList()) {
			if (remVertexIdSet.contains(curVertex.getId())) {
				continue;
			}
			retList.add(curVertex);
		}
		return retList;
	}

	/**
	 * Get the vertex corresponding to the input 'id', if exist. 
	 * 
	 * @param id
	 * @return
	 */
	public BaseVertex getVertex(int id)	{
		if (remVertexIdSet.contains(id)) {
			return null;
		} else {
			return super.getVertex(id);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Welcome.....");
		
		VarGraph graph = new VarGraph("data/test_1");
		graph.deleteVertex(13);
		graph.deleteVertex(12);
		graph.deleteVertex(10);
		graph.deleteVertex(23);
		graph.deleteVertex(47);
		graph.deleteVertex(49);
		graph.deleteVertex(3);
		graph.deleteEdge(new Pair<Integer, Integer>(26, 41));
		DijShortestPathAlg alg = new DijShortestPathAlg(graph);
		System.out.println(alg.getShortestPath(graph.getVertex(0), graph.getVertex(20)));
	}
}
