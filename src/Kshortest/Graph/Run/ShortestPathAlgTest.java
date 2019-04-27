
package Kshortest.Graph.Run;


import Kshortest.Graph.SGraph;
import Kshortest.Graph.Algs.DijShortestPathAlg;


public class ShortestPathAlgTest {
	private SGraph graph = null;
	
	
	public void setUp() throws Exception {
		// Import the graph from a file
		graph = new SGraph("data/test_1");
	}

	public void main()	{
		System.out.println("Testing Dijkstra Algorithm.");
		DijShortestPathAlg alg = new DijShortestPathAlg(graph);
		System.out.println(alg.getShortestPath(graph.getVertex(0), graph.getVertex(38)));
	}
        public static void main(String args[])
        {
            ShortestPathAlgTest ob=new ShortestPathAlgTest();
            ob.main();
        }
}
