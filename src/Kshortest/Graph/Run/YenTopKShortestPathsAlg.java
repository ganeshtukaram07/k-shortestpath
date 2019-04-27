
package Kshortest.Graph.Run;

import java.util.List;


import Kshortest.Graph.SGraph;
import Kshortest.Graph.SPath;
import Kshortest.Graph.VarGraph;
import Kshortest.Graph.Algs.DijShortestPathAlg;
import Kshortest.Graph.Algs.KshortestPathMain;
import javax.swing.JOptionPane;


public class YenTopKShortestPathsAlg {
	 
	private static SGraph graph = new VarGraph("data/test_1");

	
//	@Test
	public static void testYenShortestPathsAlg()
	{		
		System.out.println("TOP-k shortest paths");
		KshortestPathMain yenAlg = new KshortestPathMain(graph);
                 int NP=Integer.parseInt(JOptionPane.showInputDialog("Enter Required No of Top K Patterns"));
             
                 for(int i=0;i<5;i++)
                {
                int s=Integer.parseInt(JOptionPane.showInputDialog("Enter Source Node"));
                int d=Integer.parseInt(JOptionPane.showInputDialog("Enter Destination Node"));
		List<SPath> shortest_paths_list = yenAlg.getShortestPaths(graph.getVertex(s), graph.getVertex(d), 100);
		for(int j=0;j<NP;j++)
                System.out.println((j+1)+" "+shortest_paths_list.get(j));
                }
		
	}
	
//	@Test
	public static void testYenShortestPathsAlg2()
	{
		System.out.println("Obtain all paths in increasing order! - updated!");
		KshortestPathMain yenAlg = new KshortestPathMain(
				graph, graph.getVertex(4), graph.getVertex(5));
		int i=0;
		while(yenAlg.hasNext())
		{
			System.out.println("Path "+i+++" : "+yenAlg.next());
		}
		
		System.out.println("Result # :"+i);
		System.out.println("Candidate # :"+yenAlg.getCadidateSize());
		System.out.println("All generated : "+yenAlg.getGeneratedPathSize());
	}
	
	public static void main(String args[])
	{
		
                
                testYenShortestPathsAlg();
          //     testYenShortestPathsAlg2();
	}
}
