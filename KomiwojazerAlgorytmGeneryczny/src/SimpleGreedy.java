import java.util.ArrayList;

public class SimpleGreedy {

	private ArrayList<City> allCities = InitialOperations.allCities;
	double[][] distanceMatrix = City.distanceMatrix;
	public ArrayList<City> resultList = new ArrayList<City>();
	

	// dla danego miasta zwraca miasto najbli¿sze wystêpuj¹ce na liœcie
	public int getNearestCityNumber(City c, ArrayList<City> availableCities) {
		int nr = c.nr;
		int otherNr = -1;
		double minDistance = Integer.MAX_VALUE;
		int minNr = -1; // numer najbli¿szego miasta
		double actualDistance;

		for (int i = 0; i < availableCities.size(); i++) {
			City another = availableCities.get(i);
			otherNr = another.nr;
			if (otherNr != nr) {
				actualDistance = distanceMatrix[nr][otherNr];
				if (actualDistance < minDistance) {
					minDistance = actualDistance;
					minNr = otherNr;
				}
			}

		}
		return minNr;
	}


	public void greedyAlgorithm() {
		ArrayList<City> allCitiesTemp = new ArrayList<City>();
		allCitiesTemp.addAll(allCities);
		City actualCity = allCities.get(StartProgram.greedyStartPoint);
		allCitiesTemp.remove(StartProgram.greedyStartPoint);
		resultList.add(actualCity);
		while (allCitiesTemp.size()>0) {
			int nextCityNr = getNearestCityNumber(actualCity, allCitiesTemp);
			actualCity = allCities.get(nextCityNr);
			resultList.add(actualCity);
			allCitiesTemp.remove(actualCity);
		}
		
	}
	
	public void countTotalRoute(){
		double traceLength=0;
		for(int i=0; i<resultList.size();i++){
			if(i!=resultList.size()-1){
			City c1=resultList.get(i);
			City c2=resultList.get(i+1);
			traceLength+=distanceMatrix[c1.nr][c2.nr];
			}
			else{
				traceLength+=distanceMatrix[resultList.get(i).nr][0];
			}
		}
		System.out.println();
		System.out.println("Ca³kowita trasa policzona zach³annie wynosi: "+traceLength);
	}
	
	public void printResultList(){
		greedyAlgorithm();
		for(int i=0; i<resultList.size();i++){
			System.out.print(resultList.get(i).nr+" - ");
		}
	}

}
