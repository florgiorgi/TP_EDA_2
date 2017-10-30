import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class AirportNetwork{
	
	private List<Airport> airports;
	private Map<String, Airport> map;
	
	private static class Airport{
		
		private String name;
		private double latitude;
		private double length;
		private List<Flight> flights;
		private int tag; //Chequear si esta bien que esten aca
		private boolean visited;
		
		public Airport(String name, double latitude, double length){
			this.name = name;
			this.latitude = latitude;
			this.length = length;
			this.tag = 0;
			this.visited = false;
			this.flights = new LinkedList<Flight>();
		}
		
	}
	
	private static class Flight{
		
		
		private String airline;
		private int numberOfFlight;
		private List<String> daysDeparture;
		private Airport from;
		private Airport to;
		private int hourOfDeparture;
		private int minuteOfDeparture;
		private int durationMinutes;
		private int durationHours;
		private double price;
		
		public Flight(String air, int nof, List<String> dd, Airport f, Airport t, int hod, int mod, int dm, int dh, double p){
			
			this.airline = air;
			this.numberOfFlight = nof;
			this.daysDeparture = dd;
			this.from = f;
			this.to = t;
			this.hourOfDeparture = hod;
			this.minuteOfDeparture = mod;
			this.durationMinutes = dm;
			this.durationHours = dh;
			this.price = p;
			
		}
		
	}
	
	public Boolean addAirport(String name, double latitude, double length){
		
		boolean flag = true;
		if(this.airports == null){
			this.airports = new LinkedList<Airport>();
		}
		if(this.map == null){
			this.map = new HashMap<String, Airport>();
		}
		
		for(Airport a : this.airports){
			if((a.latitude == latitude && a.length == length) || a.name.equals(name)){
				flag = false;
			}
		}
		
		if(flag){
			Airport a = new Airport(name, latitude, length);
			this.airports.add(a);
			this.map.put(name, a);
		}
		
		return flag;
		
	}
	
	public void addFlight(String air, int number, String from, String to, List<String> days, int hod, int mod, int dM, int dH, double p){
		
		Airport f =  this.map.get(from);
		Airport t = this.map.get(to);
		
		if(f != null && t != null){		
			Flight newFlight = new Flight(air, number, days, f, t, hod, mod, dM, dH, p);
			f.flights.add(newFlight);
		}
		
	}
	
	public void findRoute(String from, String to, String priority, List<String> days) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		
		List<Flight> ls = findRoute(this.map.get(from), this.map.get(to), priority, days);
		
		for(Flight f : ls){
			
		}
		
	}
	/**
	 * 
	 * @param from Aeropuerto de origen
	 * @param to Aeropuerto de destino
	 * @param priority es la prioridad para ordenar los viajes. Visto de otra forma
	 * el peso que vamos a usar en las aristas al aplicar Djikstra. 
	 * @param days String con los dias. Hay que sacarles el guion.
	 * 
	 * Encuentra la mejor ruta teniendo en cuanta la prioridad. Se aplica el algoritmo
	 * de Djikstra.
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	//A este metodo hay que llamarlo con los datos ya en sus respectivas clases
	// (Me refiero a pasar el string de aeropuertos a la clase Airport)
	// Y a cada uno de los dias ponerlos en un ArrayList
	private List<PQNode> findRoute(Airport from, Airport to, String priority, List<String> days) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		
		if(from == null || to == null){
			return null; 
		 }
		
		 clearMarks();

		 PriorityQueue<PQNode> pq = new PriorityQueue<>();
		 List<PQNode> prevPQ = new LinkedList<PQNode>(); //Asumo q se puede usar ArrayList
		 pq.offer(new PQNode(from, 0, "", prevPQ, null));
		 List<Flight> flightsAux = new LinkedList<Flight>();

		 while(!pq.isEmpty()){
			PQNode aux = pq.poll();
			if(aux.airport == to){
				return aux.previousPQ;
			}	
			if(!aux.airport.visited){
				
				aux.airport.visited = true;
				for(Flight f: aux.airport.flights){
					if(aux.airport != from || !Collections.disjoint(f.daysDeparture, days)){
						
						if(priority.equals("p")){
							List<PQ>
							pq.offer(new PQNode(f.to, aux.parameter + f.price, getDay(f, aux.currentDay), f));
						}
						else if(priority.equals("ft")){
							pq.offer(new PQNode(f.to, aux.parameter + f.durationHours * 60 + f.durationMinutes, f));
						}
						else{
							pq.offer(new PQNode(f.to, 0, getDay(f, aux.currentDay), f));
						}
					}
					
				}
				
			}
		 }
		 return null;
	}
	
	private String getDay(Flight f, String day){
		int mins;
		int hour = (f.durationHours + f.hourOfDeparture < 24) ? f.durationHours + f.hourOfDeparture : f.durationHours + f.hourOfDeparture - 24;
		boolean flag = false;
		if(f.durationMinutes + f.minuteOfDeparture < 60){
			mins = f.durationMinutes + f.minuteOfDeparture;
		}
		else{
			mins = f.durationMinutes + f.minuteOfDeparture - 60;
			hour++;
			flag = true;
		}
		if(flag){
			
		}
		else{
			
		}
		return "";
	}
	
	public void clearMarks(){
		for(Airport a: airports){
			a.tag = 0;
			a.visited = false;
		}
	}
	
	private class PQNode implements Comparable<PQNode> {
	 	private Airport airport;
	 	private double parameter; 
	 	private String currentDay;
	 	private List<PQNode> previousPQ;
	 	private Flight flightUsed; 

	 	public int compareTo (PQNode other){
	 		return Double.valueOf(parameter).compareTo(other.parameter);
	 	}
	 	
	 	
	 	public PQNode(Airport a, double parameter, String cd, List<PQNode> prev, Flight flightUsed){
	 		this.airport = a;
	 		this.parameter = parameter;
	 		this.currentDay = cd;
	 		this.flightUsed = flightUsed;
	 		this.previousPQ = prev;
	 	}

		private void printPQ() {
			System.out.println("Precio#"+this.parameter);
			for(PQNode pq: previousPQ){
				System.out.println("[" + pq.flightUsed.from.name + "]" + "#" + "[" + f.airline+ "]"+ "#"
			+ "[" + f.numberOfFlight + "]" + "#" + "[" + f.daysDeparture + "]" + "#" + "[" + f.to.name + "]");
			}
		}
	 	
	 	

	 }
	
}
