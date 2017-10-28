package tpe;



import java.io.IOException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class AirportNetwork {

	private List<Airport> airports;
	private Map<String, Airport> map;

	private static class Airport {

		private String name;
		private double latitude;
		private double length;
		private List<Flight> flights;
		private int tag; // Chequear si esta bien que esten aca
		private boolean visited;

		public Airport(String name, double latitude, double length) {
			this.name = name;
			this.latitude = latitude;
			this.length = length;
			this.tag = 0;
			this.visited = false;
			this.flights = new LinkedList<Flight>();
		}

	}

	private static class Flight {

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

		public Flight(String air, int nof, List<String> dd, Airport f, Airport t, int hod, int mod, int dm, int dh,
				double p) {

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

	public AirportNetwork() {
		this.airports = new LinkedList<Airport>();
		this.map = new HashMap<String, Airport>();
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

	public void findRoute(String from, String to, String priority, List<String> days)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		if (from == null || to == null || priority == null || days == null)
			System.out.println("Invalid command: At least one parameter is not respecting the format ");
		else {
			List<PQNode> ls = findRoute(this.map.get(from), this.map.get(to), priority, days);

		}
	}

	/**
	 * 
	 * @param from
	 *            Aeropuerto de origen
	 * @param to
	 *            Aeropuerto de destino
	 * @param priority
	 *            es la prioridad para ordenar los viajes. Visto de otra forma el
	 *            peso que vamos a usar en las aristas al aplicar Djikstra.
	 * @param days
	 *            String con los dias. Hay que sacarles el guion.
	 * 
	 *            Encuentra la mejor ruta teniendo en cuanta la prioridad. Se aplica
	 *            el algoritmo de Djikstra.
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	// A este metodo hay que llamarlo con los datos ya en sus respectivas clases
	// (Me refiero a pasar el string de aeropuertos a la clase Airport)
	// Y a cada uno de los dias ponerlos en un ArrayList
	private List<PQNode> findRoute(Airport from, Airport to, String priority, List<String> days)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// COMPLETAR

		return null;
	}

	/**
	 * Borra todos los vuelos de la red.
	 */
	public void clearFlights() {
		for (Airport a : this.airports) {
			a.flights.clear();
		}
	}

	private String getDay(Flight f, String day) {
		int mins;
		int hour = (f.durationHours + f.hourOfDeparture < 24) ? f.durationHours + f.hourOfDeparture
				: f.durationHours + f.hourOfDeparture - 24;
		boolean flag = false;
		if (f.durationMinutes + f.minuteOfDeparture < 60) {
			mins = f.durationMinutes + f.minuteOfDeparture;
		} else {
			mins = f.durationMinutes + f.minuteOfDeparture - 60;
			hour++;
			flag = true;
		}
		if (flag) {

		} else {

		}
		return "";
	}

	public void clearMarks() {
		for (Airport a : airports) {
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

		public int compareTo(PQNode other) {
			return Double.valueOf(parameter).compareTo(other.parameter);
		}

		public PQNode(Airport a, double parameter, String cd, List<PQNode> prev, Flight flightUsed) {
			this.airport = a;
			this.parameter = parameter;
			this.currentDay = cd;
			this.flightUsed = flightUsed;
			this.previousPQ = prev;
		}

		private void printPQ() {
			System.out.println("Precio#" + this.parameter);
			for (PQNode pq : previousPQ) {
				// System.out.println("[" + pq.flightUsed.from.name + "]" + "#" + "[" +
				// f.airline+ "]"+ "#"
				// + "[" + f.numberOfFlight + "]" + "#" + "[" + f.daysDeparture + "]" + "#" +
				// "[" + f.to.name + "]");
			}
		}

	}
}
