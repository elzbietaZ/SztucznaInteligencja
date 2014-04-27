import java.io.IOException;

import core.ProjectCloner;
import core.conflicts.ConflictFixer;
import core.eval.Eval;

import net.sf.mpxj.MPXJException;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.mspdi.MSPDIWriter;


public class Main {

	public static void main(String[] args) throws MPXJException, IOException {
		// TODO Auto-generated method stub
		
		PrepareData pd=new PrepareData();
		pd.readFromFile();

		
		Greedy g=new Greedy();
//		g.basicGreedyWithCo();
//		g.basicGreedyWithDo();
	//	PrepareData.iterateThroughtTasks(PrepareData.resultProjectDO);
//		pd.iterateThroughtP(PrepareData.resultProject);
//		pd.iterateThroughtP(PrepareData.resultProjectDO);
		BNB bnb=new BNB();		
		bnb.makeMyTree();
//		bnb.makeMyTreeDO();


		
//		pd.writeToFile();
	}

}
