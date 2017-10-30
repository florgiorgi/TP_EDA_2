import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import javax.swing.plaf.synth.SynthSpinnerUI;


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
			this.flights = new ArrayList<Flight>();
		}
		
		public boolean equals(Airport a){
			return this.name.equals(a.name);
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
	
	public void addAirport(String name, Double latitude, Double length) {
		if (name == null || latitude == null || length == null)
			System.out.println("Invalid command: At least one parameter is not respecting the format ");
		else
			add(name, latitude, length);
	}

	private boolean add(String name, double latitude, double length) {

		boolean flag = true;
		if (this.airports == null) {
			this.airports = new LinkedList<Airport>();
		}
		if (this.map == null) {
			this.map = new HashMap<String, Airport>();
		}

		for (Airport a : this.airports) {
			if ((a.latitude == latitude && a.length == length) || a.name.equals(name)) {
				flag = false;
			}
		}

		if (flag) {
			Airport a = new Airport(name, latitude, length);
			this.airports.add(a);
			this.map.put(name, a);
		}

		return flag;

	}

	public void massAddA(String option, String name, double latitude, double length) throws IOException {
		if (option == null)
			System.out.println("Invalid command: Option is not respecting the format");
		else {
			if (option.equals("replace"))
				clearAirports();

			add(name, latitude, length);
		}
	}

	public void removeAirport(String name) {
		if (name != null)
			remove(name);
		else
			System.out.println("Invalid command: Name is not respecting the format ");
	}

	private void remove(String name) {
		Airport airport = this.map.remove(name);

		for (Airport a : airports) {
			Iterator<Flight> iter = a.flights.iterator();
			while (iter.hasNext()) {
				Flight f = iter.next();
				if (f.to.name.equals(name)) {
					iter.remove();
				}
			}
		}

		if (airport != null)
			airports.remove(airport);

	}

	public void printAirports() {
		if (airports != null) {
			for (Airport a : this.airports) {
				System.out.println("Aiport " + a.name + ":");
				for (Flight f : a.flights)
					System.out
							.println("Flight:" + f.numberOfFlight + " - From: " + f.from.name + " - To: " + f.to.name);
			}
		}
	}

	/**
	 * Borra todos los aeropuertos de la red.
	 */
	public void clearAirports() {
		this.airports.clear();
		this.map.clear();
	}

	public void addFlight(String air, Integer number, List<String> days, String from, String to, String departure,
			String duration, Double p) {

		if (air == null || number == null || days == null || from == null || to == null || departure == null
				|| duration == null || p == null)
			System.out.println("Invalid command: At least one parameter is not respecting the format ");
		else {
			String[] dep = departure.split(":");
			String dH;
			String dM;
			
			if (duration.contains("h")) {
				dH = duration.substring(0, duration.indexOf("h"));
				dM = duration.substring(duration.indexOf("h") + 1, duration.indexOf("m"));
			} else {
				dH = "0";
				dM = duration.substring(0, duration.indexOf("m"));
			}

			add(air, number, days, from, to, Integer.parseInt(dep[0]), Integer.parseInt(dep[1]), Integer.parseInt(dM),
					Integer.parseInt(dH), p);
		}
	}
	

	private void add(String air, int number, List<String> days, String from, String to, int hod, int mod, int dM,
			int dH, double p) {

		Airport f = this.map.get(from);
		Airport t = this.map.get(to);

		if (f != null && t != null) {
			// EXISTINGFLIGHT()
			Flight newFlight = new Flight(air, number, days, f, t, hod, mod, dM, dH, p);
			f.flights.add(newFlight);

		} else {
			System.out.println("The airport does not exist. Please insert the airport before inserting the flight.");
		}

	}

	private void existingFlight() {
		// CHEQUEAR QUE NO EXISTA EL VUELO CON EL MISMO NRO Y AEROLINEA
	}

	public void massAddF(String option, String air, Integer number, List<String> days, String from, String to,
			String departure, String duration, Double p) throws IOException {
		if (option == null)
			System.out.println("Invalid command: At least one parameter is not respecting the format ");
		else {
			if (option.equals("replace"))
				clearAirports();

			addFlight(air, number, days, from, to, departure, duration, p);
		}

	}

	public void removeFlight(String air, Integer number) {
		if (air == null || number == null)
			System.out.println("Invalid command: At least one parameter is not respecting the format ");
		else
			remove(air, number);
	}

	private void remove(String air, int number) {
		for (Airport a : airports) {
			Iterator<Flight> iter = a.flights.iterator();
			while (iter.hasNext()) {
				Flight f = iter.next();
				if (f.numberOfFlight == number && f.airline.equals(air)) {
					iter.remove();
					return;
				}
			}
		}

	}
	
	public void findRoute(String from, String to, String priority, List<String> days) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		if(findRoute(this.map.get(from), this.map.get(to), priority, days) == null){
			System.out.println("Not found");
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
			return null; //error
		 }
		 clearMarks();
		 
		 PriorityQueue<PQNode> pq = new PriorityQueue<>();
		 List<PQNode> ls = new ArrayList<>();
		
		 pq.offer(new PQNode(from, 0, 0, 0, 0, null, "", new ArrayList<>()));

		 
		 while(!pq.isEmpty()){
			PQNode aux = pq.poll();
			if(aux.airport.equals(to)){
				aux.ls.add(aux);
				aux.printPQ();
				return aux.ls;
			}
			
			if(!aux.airport.visited || (aux.airport.name.equals(from) && priority.equals("tt"))){
				
				aux.airport.visited = true;
				for(Flight f: aux.airport.flights){
						
					for(String day : f.daysDeparture){

						if(!aux.airport.name.equals(from.name) || days == null || days.contains(day)){
								
							ls.addAll(aux.ls);
							ls.add(aux);
							
							if(priority.equals("ft")) {
								if(aux.flight != null)
									pq.offer(new PQNode(f.to, aux.weight + f.durationHours * 60 + f.durationMinutes, aux.price + f.price, aux.flightTime + f.durationHours * 60 + f.durationMinutes, aux.totalTime + getTimeDiference(aux.flight, aux.currentDay, f, day), f, day, ls));
								else{
									pq.offer(new PQNode(f.to, aux.weight + f.durationHours * 60 + f.durationMinutes, aux.price + f.price, aux.flightTime + f.durationHours * 60 + f.durationMinutes, aux.totalTime + f.durationHours * 60 + f.durationMinutes, f, day, ls));
								}
							}
							else if(priority.equals("p")) {
								if(aux.flight != null)
									pq.offer(new PQNode(f.to, aux.weight + f.price, aux.price + f.price, aux.flightTime + f.durationHours * 60 + f.durationMinutes, aux.totalTime + getTimeDiference(aux.flight, aux.currentDay, f, day), f, day, ls));
								else
									pq.offer(new PQNode(f.to, aux.weight + f.price, aux.price + f.price, aux.flightTime + f.durationHours * 60 + f.durationMinutes, aux.totalTime + f.durationHours * 60 + f.durationMinutes, f, day, ls));
									
							}
							else {
								if(aux.flight != null)
									pq.offer(new PQNode(f.to, aux.weight + getTimeDiference(aux.flight, aux.currentDay, f, day), aux.price + f.price, aux.flightTime + f.durationHours * 60 + f.durationMinutes, aux.totalTime + getTimeDiference(aux.flight, aux.currentDay, f, day), f, day, ls));
								else
									pq.offer(new PQNode(f.to, aux.weight + aux.totalTime + f.durationHours * 60 + f.durationMinutes, aux.price + f.price, aux.flightTime + f.durationHours * 60 + f.durationMinutes, aux.totalTime + f.durationHours * 60 + f.durationMinutes, f, day, ls));
							}
							ls = new ArrayList<>();
						}
					}
							
				}
			}
				
		}
		 return null;
	}
	
	private double getTimeDiference(Flight f1, String day1, Flight f2, String day2) {
		
		double totalMins = 0;
		boolean changeHour, changeDay;
		
		int minArrival, hourArrival;
		
		if(f1.minuteOfDeparture + f1.durationMinutes > 60){
			minArrival = f1.minuteOfDeparture + f1.durationMinutes - 60;
			changeHour = true;
		}
		else{
			minArrival = f1.minuteOfDeparture + f1.durationMinutes;
			changeHour = false;
		}
		
		if(changeHour){
			if(f1.hourOfDeparture + f1.durationHours > 23){
				hourArrival = f1.hourOfDeparture + f1.durationHours - 23;
				changeDay = true;
			}
			else{
				hourArrival = f1.hourOfDeparture + f1.durationHours + 1;
				changeDay = false;
			}
		}
		else{
			if(f1.hourOfDeparture + f1.durationHours > 24){
				hourArrival = f1.hourOfDeparture + f1.durationHours - 24;
				changeDay = true;
			}
			else{
				hourArrival = f1.hourOfDeparture + f1.durationHours;
				changeDay = false;
			}
		}
		
		int d1 = getDay(day1);
		if (changeDay)
			d1 += 1;
		else
			d1 += 0;
		int d2 = getDay(day2);
		
		if(d1 == -1 || d2 == -1)
			throw new RuntimeException();
		
		if(d2 > d1){
			if(f2.hourOfDeparture > hourArrival){
				if(f2.minuteOfDeparture >= minArrival){
					totalMins += (d2 - d1) * 24 * 60 + (f2.hourOfDeparture - hourArrival) * 60 + (f2.minuteOfDeparture - minArrival);
				}
				else{
					totalMins += (d2 - d1) * 24 * 60 + (f2.hourOfDeparture - hourArrival - 1) * 60 + (60 - (minArrival - f2.minuteOfDeparture));
				}
			}
			else if(f2.hourOfDeparture < hourArrival){
				if(f2.minuteOfDeparture >= minArrival){
					totalMins += (d2 - d1 - 1) * 24 * 60 + (24 - (hourArrival - f2.hourOfDeparture)) * 60 + (f2.minuteOfDeparture - minArrival);
				}
				else{
					totalMins += (d2 - d1 - 1) * 24 * 60 + (23 - (hourArrival - f2.hourOfDeparture)) * 60 + (60 - (minArrival - f2.minuteOfDeparture));
				}
			}
			else{
				if(f2.minuteOfDeparture >= minArrival){
					totalMins += (d2 - d1) * 24 * 60 + (f2.minuteOfDeparture - minArrival);
				}
				else{
					totalMins += (d2 - d1 - 1) * 24 * 60 + 23 * 60 + (60 - (minArrival - f2.minuteOfDeparture));
				}
			}
		}
		else if(d1 > d2){
			if(f2.hourOfDeparture > hourArrival){
				if(f2.minuteOfDeparture >= minArrival){
					totalMins += (7 - (d1 - d2)) * 24 * 60 + (f2.hourOfDeparture - hourArrival) * 60 +  (f2.minuteOfDeparture - minArrival);
				}
				else{
					totalMins += (7 - (d1 - d2)) * 24 * 60 + (f2.hourOfDeparture - hourArrival - 1) * 60 + (60 - (minArrival - f2.minuteOfDeparture));
				}
			}
			else if(hourArrival > f2.hourOfDeparture){
				if(f2.minuteOfDeparture >= minArrival){
					totalMins += (6 - (d1 - d2)) * 24 * 60 + (24 - (hourArrival - f2.hourOfDeparture)) * 60 + (f2.minuteOfDeparture - minArrival);
				}
				else{
					totalMins += (6 - (d1 - d2)) * 24 * 60 + (23 - (hourArrival - f2.hourOfDeparture)) * 60 + (60 - (minArrival - f2.minuteOfDeparture));
				}
			}
			else{
				if(f2.minuteOfDeparture >= minArrival){
					totalMins += (7 - (d1 - d2)) * 24 * 60 + (f2.minuteOfDeparture - minArrival);
				}
				else{
					totalMins += (6 - (d1 - d2)) * 24 * 60 + 23 * 60 + (60 - (minArrival - f2.minuteOfDeparture));
				}
			}
		}
		else{
			if(f2.hourOfDeparture > hourArrival){
				if(f2.minuteOfDeparture >= minArrival){
					totalMins += (f2.hourOfDeparture - hourArrival) * 60 - (f2.minuteOfDeparture - minArrival);
				}
				else{
					totalMins += (f2.hourOfDeparture - hourArrival - 1) * 60 - (60 - (minArrival - f2.minuteOfDeparture));
				}
			}
			else if(f2.hourOfDeparture < hourArrival){
				if(f2.minuteOfDeparture >= minArrival){
					totalMins += 6 * 24 * 60 + (24 - (hourArrival - f2.hourOfDeparture)) * 60 + (f2.minuteOfDeparture - minArrival);
				}
				else{
					totalMins += 6 * 24 * 60 + (23 - (hourArrival - f2.hourOfDeparture)) * 60 + (60 - (minArrival - f2.minuteOfDeparture));
				}
			}
			else{
				if(f2.minuteOfDeparture >= minArrival){
					totalMins += f2.minuteOfDeparture - minArrival;
				}
				else{
					totalMins += 6 * 24 * 60 + 23 * 60 + (60 - (minArrival - f2.minuteOfDeparture));
				}
			}
		}
		
		totalMins += f2.durationHours * 60 + f2.durationMinutes;
		
		return totalMins;
	}
	
	private int getDay(String d) {
		switch(d){
			case("Lu"):
				return 0;
			case("Ma"):
				return 1;
			case("Mi"):
				return 2;
			case("Ju"):
				return 3;
			case("Vi"):
				return 4;
			case("Sa"):
				return 5;
			case("Dom"):
				return 6;
			default:
				return -1;
		}
	}
	
	public void clearMarks(){
		for(Airport a: airports){
			a.tag = 0;
			a.visited = false;
		}
	}
	
	private class PQNode <V> implements Comparable<PQNode<V>> {
	 	Airport airport;
	 	double weight; //Es el criterio q vamos a usar
	 	double price; //OJO va a ser el precio total
	 	double flightTime; //Tiempo TOTAL de vuelo
	 	double totalTime; 	//Tiempo total de viaje
	 	String currentDay;
	 	Flight flight; //Son los vuelos que tiene acumulado ese nodo
	 	List<PQNode> ls;

	 	public int compareTo (PQNode other){
	 		return Double.valueOf(weight).compareTo(other.weight);
	 	}
	 	
	 	public PQNode(Airport a, double weight, double price, double flightTime, double totalTime, Flight f, String currentDay, List<PQNode> ls){
	 		this.airport = a;
	 		this.weight = weight;
	 		this.price = price;
	 		this.flightTime = flightTime;
	 		this.totalTime = totalTime;
	 		this.flight = f;
	 		this.currentDay = currentDay;
	 		this.ls = ls;
	 	}

		private void printPQ() {
			System.out.println("Precio# "+this.price);
			System.out.println("TiempoVuelo# "+ (int)this.flightTime/60 + "h " + (int)this.flightTime%60 + "m");
			System.out.println("TiempoTotal# "+ (int)this.totalTime/60 + "h " + (int)this.totalTime%60 + "m");
			
			for(PQNode pq : ls) {
				
				if(pq.flight != null)
					System.out.println("[" + pq.flight.from.name + "] # [" + pq.flight.airline + "] # [" + pq.flight.numberOfFlight +
							"] # [" + pq.currentDay + "] # [" + pq.flight.to.name + "]");
				
			}
		
			
			
		}
	 	
	 	

	 }
	
}
