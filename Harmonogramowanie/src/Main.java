import java.io.IOException;

import net.sf.mpxj.MPXJException;


public class Main {

	public static void main(String[] args) throws MPXJException, IOException {
		// TODO Auto-generated method stub

		PrepareData pd=new PrepareData();
		pd.readFromFile();
	//	pd.printAllTasks();
	//	pd.iterateThroughtP();
	//	pd.iterateThroughtTasks(PrepareData.project);
	//	pd.writeToFile();
		
		Greedy g=new Greedy();
		g.basicGreedyWithCo();
		g.basicGreedyWithDo();
	//	PrepareData.iterateThroughtTasks(PrepareData.resultProjectDO);
//		pd.iterateThroughtP(PrepareData.resultProject);
//		pd.iterateThroughtP(PrepareData.resultProjectDO);


		
		pd.writeToFile();
	}

}
