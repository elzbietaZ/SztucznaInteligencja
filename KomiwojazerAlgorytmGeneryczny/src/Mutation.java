import java.util.Collections;
import java.util.List;


public class Mutation {

	
	List<Integer>[] tableOfParents;
	List<Integer>[] tableOfChildren;
	private int populationSize;
	private int CityCount; // liczba miast
	
	Mutation(GenericAlgorithm g){

		tableOfParents = GenericAlgorithm.tableOfPopulation;
		populationSize = tableOfParents.length;
		CityCount=tableOfParents[0].size();
		
	}
	
	public void mutate() {
//		int stop=(int)(StartProgram.mutationProbability*populationSize);
		int k=0;
		while(k<populationSize) {
			double shoulIMutate=Math.random();
			if(shoulIMutate<StartProgram.mutationProbability) {
	//		int randomNr=(int)(Math.random()*populationSize);
			int swapPos1=(int)(Math.random()*CityCount);
			int swapPos2=(int)(Math.random()*CityCount);
			Collections.swap(tableOfParents[k], swapPos1, swapPos2);	
	//		Collections.swap(tableOfParents[randomNr], swapPos1, swapPos2);	
			}
			k++;
			
		}
		
	}

}
