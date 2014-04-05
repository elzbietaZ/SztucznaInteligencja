import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;


public class InitialOperations {
	
	public static ArrayList<City> allCities=new ArrayList<City>();
	public static int numberOfCities;

	
	
	public void readFromFile(){
		try {
			Scanner in = new Scanner(new FileReader("C://Users/El¿bieta/Desktop/Sztuczna/Sahara" +
					".txt"));
			while(in.hasNextLine()){
				String line=in.nextLine();
				String [] split=line.split(" ");
				City c=new City(Integer.valueOf(split[0])-1, Double.valueOf(split[1]), Double.valueOf(split[2]));	
				allCities.add(c);
			}
			in.close();
			numberOfCities=allCities.size();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void prepareData(){
		City.fillDistanceMatrix();
	}



}
