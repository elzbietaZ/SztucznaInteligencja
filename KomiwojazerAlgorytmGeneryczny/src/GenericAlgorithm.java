import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;


public class GenericAlgorithm {

	PrintWriter writer;

	private ArrayList<City> allCities = InitialOperations.allCities;
	double[][] distanceMatrix = City.distanceMatrix;
	public static List<Integer> [] tableOfPopulation;
	private Map<Integer,Double> eval= new TreeMap<Integer, Double>();
	double min=Double.MAX_VALUE;
	double max=0;
	double average=0;

	
	public void genericAlg() throws FileNotFoundException, UnsupportedEncodingException{
		writer = new PrintWriter("C:/Users/El¿bieta/Desktop/Sztuczna/Saharawyniki100.csv");
		writer.println("srednia;max;min");
//		inicializeUsingGreedyAlg();
		initcializePopulation();
		evaluatePopulation();
		printPopulation();
		int i=0;
			while(i<StartProgram.t_max){
				System.out.println("Krok algorytmu: "+i);
				Selection selection=new Selection(this);
				selection.turnament();
				Mutation mutation=new Mutation(this);
				mutation.mutate();
				CrossOver crossover=new CrossOver(this);
				crossover.partialyMappedCrossOver();		
				evaluatePopulation();
				i++;
			}
			printPopulation();
			writer.write("populacja: "+StartProgram.populationSize);
			writer.write("  mutacja: "+StartProgram.mutationProbability);
			writer.write("  selekcja: "+StartProgram.participantsNumber);
			writer.write("  krzy¿owanie: "+StartProgram.crossOverProbability);
			writer.close();
	}

	private void initcializePopulation() {
		
		tableOfPopulation=new List [StartProgram.populationSize];
		List<Integer> tempList=new ArrayList<Integer>();
		for(int i=0; i<allCities.size();i++){
			tempList.add(i);
		}
		for(int i=0;i<StartProgram.populationSize;i++){			
			Collections.shuffle(tempList);
			List<Integer> shuffeledList=new ArrayList<Integer>();
			shuffeledList.addAll(tempList);
			tableOfPopulation[i]=shuffeledList;
		}	
	}
	
	private void inicializeUsingGreedyAlg(){
		int k=0;
		
		tableOfPopulation=new List [StartProgram.populationSize];
		while(k<StartProgram.populationSize) {
		StartProgram.greedyStartPoint=k;
		SimpleGreedy greedy=new SimpleGreedy();
		greedy.greedyAlgorithm();
		List<Integer> tempList=new ArrayList<Integer>();
		for(int i=0; i<allCities.size();i++){
			tempList.add(greedy.resultList.get(i).nr);
		}
		tableOfPopulation[k]=tempList;
		k++;
		}

	}
	
	private void evaluatePopulation() throws FileNotFoundException, UnsupportedEncodingException{
		
		for(int i=0; i<tableOfPopulation.length;i++){
			double count=countTotalRoute(tableOfPopulation[i]);
			if(count<min)min=count;
			if(count>max)max=count;
			eval.put(i, count);		
			average+=count;
		}
		average/=tableOfPopulation.length;
	
		System.out.println("Œrednia: "+average);
		System.out.println("Minimalna trasa: "+min);
		System.out.println("Maksymalna trasa: "+max);
		
		saveToFile();


		min=Double.MAX_VALUE;
		max=0;
		average=0;
	}
	
	private double countTotalRoute(List<Integer> creature){
		double traceLength=0;
		for(int i=0; i<creature.size();i++){
			if(i!=creature.size()-1){
				traceLength+=distanceMatrix[creature.get(i)][creature.get(i+1)];
			}
			else{
				traceLength+=distanceMatrix[creature.get(i)][0];
			}
		}		
		return traceLength;
	}
	
	private void printPopulation(){
//		for(int i=0; i<tableOfPopulation.length;i++){
//			System.out.println(tableOfPopulation[i].toString());
//			System.out.println(countTotalRoute(tableOfPopulation[i]));
//		}
	//	evaluatePopulation();
		System.out.println(eval.toString());
		System.out.println(tableOfPopulation[0].toString());
		System.out.println();

		
	}
	
	public Map<Integer,Double> getEvaluation(){
		return eval;
	}
	
	public void saveToFile() throws FileNotFoundException, UnsupportedEncodingException {
		
		String averageS=String.valueOf(average).replace(".", ",");
		writer.print(averageS);
		writer.print(";");
		String maxS=String.valueOf(max).replace(".", ",");
		writer.print(maxS);
		writer.print(";");
		String minS=String.valueOf(min).replace(".", ",");
		writer.print(minS);
		writer.println();
	//	writer.close();
	}

}
