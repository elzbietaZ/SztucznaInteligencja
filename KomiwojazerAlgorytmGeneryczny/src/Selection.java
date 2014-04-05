import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Selection {

	List<Integer>[] tableOfParents;
	List<Integer>[] tableOfChildren;
	private Map<Integer, Double> eval;
	private int populationSize;
	
	Selection(GenericAlgorithm g) {
		eval = g.getEvaluation();
		tableOfParents = GenericAlgorithm.tableOfPopulation;
		populationSize = tableOfParents.length;
		tableOfChildren =new List[populationSize];
	}

	// zamieniæ, bo teraz tablela rodziców siê zastêpuje Ÿle
	public void turnament() {
		int k = 0;
		while (k < populationSize) {
			int[] turnamentGroup = new int[StartProgram.participantsNumber];
			for (int i = 0; i < StartProgram.participantsNumber; i++) {
				int randomNr = (int) (Math.random() * populationSize);
				turnamentGroup[i] = randomNr;
			}
			// walka
			double min = Integer.MAX_VALUE;
			int bestFighter = turnamentGroup[0];
			for (int i = 0; i < StartProgram.participantsNumber; i++) {
				int FighterNumber = turnamentGroup[i];
//				System.out.println("walczy³: "+FighterNumber);
				double tempMin = eval.get(FighterNumber);
				if (tempMin < min) {
					min = tempMin;
					bestFighter = FighterNumber;
				}
			}
//			System.out.println("wygra³: "+bestFighter);
			tableOfChildren[k] = tableOfParents[bestFighter];
//			System.out.println("children: "+ tableOfChildren[k].toString());
//			System.out.println("parents: "+tableOfParents[k].toString());

			k++;
		}
		
		GenericAlgorithm.tableOfPopulation=copyTable(tableOfChildren);
	}

	
	public List<Integer>[] copyTable(List<Integer>[] oldList) {
		List<Integer>[] newList= new List[populationSize];
		for(int i=0;i<oldList.length;i++) {
			List<Integer> copy=new ArrayList();
			for(int j=0;j<oldList[i].size();j++) {
				copy.add(oldList[i].get(j));
			}
			newList[i]=copy;
		}
		return newList;
	}
}
