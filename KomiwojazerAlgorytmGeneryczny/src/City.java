public class City {
	
	int nr;
	double x;
	double y;
	static double [][] distanceMatrix; // distanceMatrix[2][3]= odleg³oœæ miêdzy miastem 2 a 3
	
	City( int nr, double x, double y){
		this.x=x;
		this.y=y;
		this.nr=nr;
	}
	
	public static double countDistance(City c1, City c2){
		return Math.sqrt((c1.x-c2.x)*(c1.x-c2.x)+(c1.y-c2.y)*(c1.y-c2.y));
	}
	
	public static void fillDistanceMatrix(){
		
		distanceMatrix= new double[InitialOperations.allCities.size()][InitialOperations.allCities.size()];
		
		for(int i=0; i<distanceMatrix.length;i++){
			for(int j=0; j<distanceMatrix.length;j++){
				City firstCity=InitialOperations.allCities.get(i);
				City secondCity=InitialOperations.allCities.get(j);
				distanceMatrix[i][j]=countDistance(firstCity, secondCity);
//				firstCity.printSring();
//				secondCity.printSring();
//				System.out.println("odleg³oœæ: "+distanceMatrix[i][j]);
			}
		}
		
	}
	
	public void printSring(){
		System.out.println("Nr:"+this.nr+"    x: "+this.x+"    y:"+this.y);		
	}
	
	
	
	


}
