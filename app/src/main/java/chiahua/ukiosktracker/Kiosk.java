import java.util.ArrayList;

public class Kiosk {
	
	//Coordinates of the Kiosks
	private double lattitude;
	private double longitude;

	
	//Index in allKiosk list
	private int kioskNumber;
	
	private String description;
	public ArrayList<Poster> posterList;
	
	//Constructor method for Kiosks
	public Kiosk(int kioskNumber, String description, double lattitude, double longitude) {
		this.lattitude = lattitude;
		this.longitude = longitude;
		this.kioskNumber = kioskNumber;
		this.description = description;
		posterList = new ArrayList<Poster>();
	}
	
	//Get lattitude coordinate
	public double lattit() {
		return this.lattitude;
	}
	
	//Get longitude coordinate
	public double longit() {
		return this.longitude;
	}
	
	
	//Adds a poster to this kiosk's list of posters
	//Only call this from the Poster class
	public boolean add(Poster poster) {
		return posterList.add(poster);
	}
	
	//Removes a poster from this kiosk's list of posters
	//Only call this from the Poster class
	public boolean remove(Poster poster) {
		return posterList.remove(poster);
	}
	
	//Returns the list of posters posted at this kiosk
	public ArrayList<Poster> getPosterList() {
		return posterList;
	}
	
	public void printAsString() {
		System.out.println("Kiosk number " + kioskNumber + ": "+ description);
		System.out.println(posterList.size() + " Posters are here:");
		for (Poster poster: posterList) {
			System.out.println("   "+poster.title());
		}
	}
	
	//Calculates distance from a given location in meters
	public double distance(double userLattitude, double userLongitude) {
		//Distance = sqrt{ (y2-y1)^2 + (x2-x1)^2 }
		double lattitudeDelta = lattitude - userLattitude;
		double longitudeDelta = longitude - userLongitude;
		double result = (lattitudeDelta * lattitudeDelta) +
				(longitudeDelta * longitudeDelta);
		return result = Math.sqrt(result) * 111000;
	}

}
