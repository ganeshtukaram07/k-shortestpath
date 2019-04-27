
package Kshortest.Graph.Algs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import Kshortest.Graph.SGraph;
import Kshortest.Graph.SPath;
import Kshortest.Graph.VarGraph;
import Kshortest.Graph.Utils.BaseGraph;
import Kshortest.Graph.Utils.BaseVertex;
import Kshortest.Graph.Base.Pair;
import Kshortest.Graph.Base.QPriorityQueue;


public class KshortestPathMain
{
	private VarGraph graph = null;

	// intermediate variables
	private List<SPath> resultList = new Vector<SPath>();
	private Map<SPath, BaseVertex> pathDerivationVertexIndex = new HashMap<SPath, BaseVertex>();
	private QPriorityQueue<SPath> pathCandidates = new QPriorityQueue<SPath>();
	
	// the ending vertices of the paths
	private BaseVertex sourceVertex = null;
	private BaseVertex targetVertex = null;
	
	// variables for debugging and testing
	private int generatedPathNum = 0;
	
	/**
	 * Default constructor.
	 * 
	 * @param graph
	 * @param k
	 */
	public KshortestPathMain(BaseGraph graph)	{
        this(graph, null, null);
	}
	
	/**
	 * Constructor 2
	 * 
	 * @param graph
	 * @param sourceVertex
	 * @param targetVertex
	 */
	public KshortestPathMain(BaseGraph graph, 
			BaseVertex sourceVertex, BaseVertex targetVertex)	{
		if (graph == null) {
			throw new IllegalArgumentException("A NULL graph object occurs!");
		}
		this.graph = new VarGraph((SGraph)graph);
		this.sourceVertex = sourceVertex;
		this.targetVertex = targetVertex;
		init();
	}
	
	/**
	 * Initiate members in the class. 
	 */
	private void init()	{
		clear();
		// get the shortest path by default if both source and target exist
		if (sourceVertex != null && targetVertex != null) {
			SPath shortestPath = getShortestPath(sourceVertex, targetVertex);
			if (!shortestPath.getVertexList().isEmpty()) {
				pathCandidates.add(shortestPath);
				pathDerivationVertexIndex.put(shortestPath, sourceVertex);
			}
		}
	}
	
	/**
	 * Clear the variables of the class. 
	 */
	public void clear()	{
		pathCandidates = new QPriorityQueue<SPath>();
		pathDerivationVertexIndex.clear();
		resultList.clear();
		generatedPathNum = 0;
	}
	
	/**
	 * Obtain the shortest path connecting the source and the target, by using the
	 * classical Dijkstra shortest path algorithm. 
	 * 
	 * @param sourceVertex
	 * @param targetVertex
	 * @return
	 */
	public SPath getShortestPath(BaseVertex sourceVertex, BaseVertex targetVertex)	{
		DijShortestPathAlg dijkstraAlg = new DijShortestPathAlg(graph);
		return dijkstraAlg.getShortestPath(sourceVertex, targetVertex);
	}
	
	/**
	 * Check if there exists a path, which is the shortest among all candidates.  
	 * 
	 * @return
	 */
	public boolean hasNext() {
        return !pathCandidates.isEmpty();
	}
	
	/**
	 * Get the shortest path among all that connecting source with targe. 
	 * 
	 * @return
	 */
	public SPath next() {
		//3.1 prepare for removing vertices and arcs
		SPath curPath = pathCandidates.poll();
		resultList.add(curPath);

		BaseVertex curDerivation = pathDerivationVertexIndex.get(curPath);
		int curPathHash =
			curPath.getVertexList().subList(0, curPath.getVertexList().indexOf(curDerivation)).hashCode();
		
		int count = resultList.size();
		
		//3.2 remove the vertices and arcs in the graph
		for (int i = 0; i < count-1; ++i) {
			SPath curResultPath = resultList.get(i);
							
			int curDevVertexId =
				curResultPath.getVertexList().indexOf(curDerivation);
			
			if (curDevVertexId < 0) {
                continue;
            }

			// Note that the following condition makes sure all candidates should be considered. 
			/// The algorithm in the paper is not correct for removing some candidates by mistake. 
			int pathHash = curResultPath.getVertexList().subList(0, curDevVertexId).hashCode();
			if (pathHash != curPathHash) {
                continue;
            }
			
			BaseVertex curSuccVertex =
				curResultPath.getVertexList().get(curDevVertexId + 1);
			
			graph.deleteEdge(new Pair<Integer, Integer>(
                    curDerivation.getId(), curSuccVertex.getId()));
		}
		
		int pathLength = curPath.getVertexList().size();
		List<BaseVertex> curPathVertexList = curPath.getVertexList();
		for (int i = 0; i < pathLength-1; ++i) {
			graph.deleteVertex(curPathVertexList.get(i).getId());
			graph.deleteEdge(new Pair<Integer, Integer>(
                    curPathVertexList.get(i).getId(),
                    curPathVertexList.get(i + 1).getId()));
		}
		
		//3.3 calculate the shortest tree rooted at target vertex in the graph
		DijShortestPathAlg reverseTree = new DijShortestPathAlg(graph);
		reverseTree.getShortestPathFlower(targetVertex);
		
		//3.4 recover the deleted vertices and update the cost and identify the new candidate results
		boolean isDone = false;
		for (int i=pathLength-2; i>=0 && !isDone; --i)	{
			//3.4.1 get the vertex to be recovered
			BaseVertex curRecoverVertex = curPathVertexList.get(i);
			graph.recoverDeletedVertex(curRecoverVertex.getId());
			
			//3.4.2 check if we should stop continuing in the next iteration
			if (curRecoverVertex.getId() == curDerivation.getId()) {
				isDone = true;
			}
			
			//3.4.3 calculate cost using forward star form
			SPath subPath = reverseTree.updateCostForward(curRecoverVertex);
			
			//3.4.4 get one candidate result if possible
			if (subPath != null) {
				++generatedPathNum;
				
				//3.4.4.1 get the prefix from the concerned path
				double cost = 0; 
				List<BaseVertex> prePathList = new Vector<BaseVertex>();
				reverseTree.correctCostBackward(curRecoverVertex);
				
				for (int j=0; j<pathLength; ++j) {
					BaseVertex curVertex = curPathVertexList.get(j);
					if (curVertex.getId() == curRecoverVertex.getId()) {
						j=pathLength;
					} else {
						cost += graph.getEdgeWeightOfGraph(curPathVertexList.get(j),
								curPathVertexList.get(j+1));
						prePathList.add(curVertex);
					}
				}
				prePathList.addAll(subPath.getVertexList());

				//3.4.4.2 compose a candidate
				subPath.setWeight(cost + subPath.getWeight());
				subPath.getVertexList().clear();
				subPath.getVertexList().addAll(prePathList);
				
				//3.4.4.3 put it in the candidate pool if new
				if (!pathDerivationVertexIndex.containsKey(subPath)) {
					pathCandidates.add(subPath);
					pathDerivationVertexIndex.put(subPath, curRecoverVertex);
				}
			}
			
			//3.4.5 restore the edge
			BaseVertex succVertex = curPathVertexList.get(i + 1);
			graph.recoverDeletedEdge(new Pair<Integer, Integer>(
                    curRecoverVertex.getId(), succVertex.getId()));
			
			//3.4.6 update cost if necessary
			double cost1 = graph.getEdgeWeight(curRecoverVertex, succVertex)
				+ reverseTree.getStartVertexDistanceIndex().get(succVertex);
			
			if (reverseTree.getStartVertexDistanceIndex().get(curRecoverVertex) >  cost1) {
				reverseTree.getStartVertexDistanceIndex().put(curRecoverVertex, cost1);
				reverseTree.getPredecessorIndex().put(curRecoverVertex, succVertex);
				reverseTree.correctCostBackward(curRecoverVertex);
			}
		}
		
		//3.5 restore everything
		graph.recoverDeletedEdges();
		graph.recoverDeletedVertices();
		
		return curPath;
	}
	
	/**
	 * Get the top-K shortest paths connecting the source and the target.  
	 * This is a batch execution of top-K results.
	 * 
	 * @param source
	 * @param sink
	 * @param k
	 * @return
	 */
	public List<SPath> getShortestPaths(BaseVertex source,
                                       BaseVertex target, int k) {
		sourceVertex = source;
		targetVertex = target;
		
		init();
		int count = 0;
		while (hasNext() && count < k) {
			next();
			++count;
		}
		
		return resultList;
	}
		
	/**
	 * Return the list of results generated on the whole.
	 * (Note that some of them are duplicates)
	 * @return
	 */
	public List<SPath> getResultList() {
        return resultList;
	}

	/**
	 * The number of distinct candidates generated on the whole. 
	 * @return
	 */
	public int getCadidateSize() {
		return pathDerivationVertexIndex.size();
	}

	public int getGeneratedPathSize() {
		return generatedPathNum;
	}
}
