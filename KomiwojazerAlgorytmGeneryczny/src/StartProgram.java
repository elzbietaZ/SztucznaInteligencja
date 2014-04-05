import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;


public class StartProgram {
	
	public static int greedyStartPoint=0;
	public static int participantsNumber = 5;
	public static double mutationProbability=0.65;
	public static double crossOverProbability=0.8;
	public static int t_max=500; 
	public static int populationSize = 1000;
	
	
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
//		long heapSize = Runtime.getRuntime().totalMemory();
//		System.out.println("Heap Size = " + heapSize);

		InitialOperations start=new InitialOperations();
		start.readFromFile();
		start.prepareData();
		
		SimpleGreedy greedy=new SimpleGreedy();
		greedy.printResultList();
		greedy.countTotalRoute();
		
		GenericAlgorithm ga=new GenericAlgorithm();
		ga.genericAlg();
		
	}








}
