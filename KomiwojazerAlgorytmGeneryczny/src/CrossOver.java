import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class CrossOver {

	List<Integer>[] tableOfParents;
	private int populationSize;
	private int CityCount; // liczba miast
	
	
	CrossOver(GenericAlgorithm g){
		tableOfParents = GenericAlgorithm.tableOfPopulation;
		populationSize = tableOfParents.length;
		CityCount=tableOfParents[0].size();
		
	}
	
	// PMX opisane u Michalewicza
	public void partialyMappedCrossOver() {
		
//		int stop=(int)(StartProgram.crossOverProbability*populationSize);
		int k=0;
		while(k<(populationSize/2)) {
			double shoulICrossOver=Math.random();
			if(shoulICrossOver<StartProgram.crossOverProbability) {

			int randomParent1Nr = (int) (Math.random() * populationSize);
			int randomParent2Nr = (int) (Math.random() * populationSize);

			int randomCut1Nr=(int)(Math.random()*(CityCount-4))+4; 
			int randomCut2Nr=(int)(Math.random()*(CityCount-4))+4; 
			
			int cut1; // pocz¹tek œrodkowego segmentu
			int cut2; // koniec œrodkowego segmentu
			
			if(randomCut1Nr<randomCut2Nr) {
				cut1=randomCut1Nr;
				cut2=randomCut2Nr;
			}
			else {
				cut1=randomCut2Nr;
				cut2=randomCut1Nr;
			}
//			System.out.println("Cut :"+cut1+"  "+cut2);

			List<Integer> parent1Part2=new ArrayList<Integer>();
			List<Integer> parent2Part2=new ArrayList<Integer>();
			
			List<Integer> parent1=tableOfParents[randomParent1Nr];
			List<Integer> parent2=tableOfParents[randomParent2Nr];

			parent1Part2.addAll(parent1.subList(cut1, cut2));
			parent2Part2.addAll(parent2.subList(cut1, cut2));

			// mapa wymiany œrodkowych segmentów
			
			Map<Integer,Integer> swaped1 =new Hashtable<>();
			Map<Integer,Integer> swaped2 =new Hashtable<>();

			for(int i=0; i<parent1Part2.size();i++) {
				int temp1=parent1Part2.get(i);
				int temp2=parent2Part2.get(i);
				swaped1.put(temp1, temp2);
				swaped2.put(temp2,temp1);
			}
//			System.out.println(swaped1.toString());
//			System.out.println(swaped2.toString());
//
//			
//			System.out.println(tableOfChildren[randomParent1Nr].toString());
//			System.out.println(tableOfChildren[randomParent2Nr].toString());

			for(int i=0; i<parent1.size();i++) {
				int element=parent1.get(i);
				int element2=parent2.get(i);
				if(i<cut1 || i>=cut2) {
					if(parent2Part2.contains(element)) {
						int toSwap=swaped2.get(element);
						while(parent2Part2.contains(toSwap)) toSwap=swaped2.get(toSwap);
//						System.out.println("zawiera "+element+"  wstaw:"+swaped2.get(element));
						tableOfParents[randomParent1Nr].set(i, toSwap);
					}
		//			System.out.println("znowu : "+parent1Part2.toString());
					if(parent1Part2.contains(element2)) {
						int toSwap=swaped1.get(element2);
						while(parent1Part2.contains(toSwap)) toSwap=swaped1.get(toSwap);
//						System.out.println("zawiera* "+element2+"  wstaw:"+swaped1.get(element2));
						tableOfParents[randomParent2Nr].set(i, toSwap);
					}
				}
				if(i>=cut1 && i<cut2) {
					tableOfParents[randomParent1Nr].set(i,swaped1.get(element));
					tableOfParents[randomParent2Nr].set(i,swaped2.get(element2));
				}

			
				}
			
			// wykrywanie b³êdu
			for(int i=0; i<tableOfParents[randomParent1Nr].size();i++) {
				if(!tableOfParents[randomParent1Nr].contains(i))System.out.println("jest b³¹d w 1");
				if(!tableOfParents[randomParent2Nr].contains(i))System.out.println("jest b³¹d w 2");
			}
			}
			k++;
		}
	}
	
}
