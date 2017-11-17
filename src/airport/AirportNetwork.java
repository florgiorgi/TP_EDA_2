package airport;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class AirportNetwork {
	private List<Airport> airports;
	private Map<String, Airport> map;

	public static class Airport {
		private String name;
		private double latitude;
		private double length;
		private List<Flight> flights;
		private boolean visited;

		public Airport(String name, double latitude, double length) {
			this.name = name;
			this.latitude = latitude;
			this.length = length;
			this.visited = false;
			this.flights = new ArrayList<Flight>();
		}

		public boolean equals(Airport a) {
			return this.name.equals(a.name);
		}

		public String getName() {
			return name;
		}

		public List<Flight> getFlights() {
			return flights;
		}

	}

	static class Flight {
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

		public Flight() {
			daysDeparture = new LinkedList<String>();
		}

		public int getTotalDuration() {
			return durationHours * 60 + durationMinutes;
		}

		public String getFrom() {
			return from.name;
		}

		public double getPrice() {
			return price;
		}

		public String getTo() {
			return to.name;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;

			if (obj == null)
				return false;

			if (getClass() != obj.getClass())
				return false;

			final Flight other = (Flight) obj;
			if (this.airline.equals(other.airline) && this.numberOfFlight == other.numberOfFlight)
				return true;
			else
				return false;
		}

	}

	public AirportNetwork() {
		this.map = new HashMap<String, Airport>();
		this.airports = new LinkedList<Airport>();
	}

	public void printNetwork() {
		if (airports.isEmpty()) {
			System.out.println("There is no airport loaded in our network");
			return;
		}

		for (Airport a : airports) {
			System.out.println("Aiport: " + a.name);
			for (Flight f : a.flights) {
				System.out.println("Flight: " + f.numberOfFlight + " - Airline: " + f.airline + " - From: "
						+ f.from.name + " - To: " + f.to.name + " - Price: " + f.price);
			}
			System.out.println();
		}

	}

	/**
	 * Agrega un aeropuerto a la red, si este no existe en la misma
	 * 
	 * @param name
	 *            Nombre del aeropuerto segun su codigo IATA o null si el formato
	 *            ingresado era incorrecto
	 * @param latitude
	 *            Latitud geografica del aeropuerto o null si el formato ingresado
	 *            era incorrecto
	 * @param length
	 *            Longitud geografica del aeropuerto o null si el formato ingresado
	 *            era incorrecto
	 */
	public void addAirport(String name, Double latitude, Double length) {
		if (name == null || latitude == null || length == null)
			System.out.println("Invalid command: At least one parameter is not respecting the format ");
		else
			add(name, latitude, length);

	}

	/**
	 * 
	 * @param name
	 *            Nombre del aeropuerto segun su codigo IATA
	 * @param latitude
	 *            Latitud geografica del aeropuerto
	 * @param length
	 *            Longitud geografica del aeropuerto
	 */
	private void add(String name, double latitude, double length) {
		if (!map.containsKey(name)) {
			Airport a = new Airport(name, latitude, length);
			this.airports.add(a);
			this.map.put(name, a);
			System.out.println("The airport " + a.name + " has been added to our network");
		} else
			System.out.println("The airport you are trying to add already belongs to our network");
	}

	/**
	 * Remueve el aeropuerto correspodiente al nombre si este existe
	 * 
	 * @param name
	 *            Nombre del aeropuerto a eliminar segun su codigo IATA o null si el
	 *            formato ingresado era incorrecto
	 */
	public void removeAirport(String name) {
		if (name != null)
			remove(name);
		else
			System.out.println("Invalid command: Name is not respecting the format ");
	}

	/**
	 * 
	 * @param name
	 *            Nombre del aeropuerto a eliminar segun su codigo IATA
	 */
	private void remove(String name) {
		Airport airport = this.map.get(name);

		if (airport == null) {
			System.out.println("The airport you are trying to delete does not exist");
			return;
		}

		for (Airport a : airports) {
			Iterator<Flight> iter = a.flights.iterator();
			while (iter.hasNext()) {
				Flight f = iter.next();
				if (f.to.name.equals(name)) {
					iter.remove();
				}
			}
		}

		System.out.println("The airport " + name + " has been removed from our network");
		airports.remove(airport);
		map.remove(name);
	}

	/**
	 * Borra todos los aeropuertos de la red.
	 */
	public void clearAirports() {
		if (airports.isEmpty()) {
			System.out.println("There are no airports to delete, our network is empty");
			return;
		}

		this.airports.clear();
		this.map.clear();

		System.out.println("All airports have been removed from our network");
	}

	/**
	 * Agrega un vuelo a la red, si este no existe en la misma
	 * 
	 * @param air
	 *            Nombre de la aerolinea del vuelo segun su codigo IATA o null si el
	 *            formato ingresado era incorrecto
	 * @param number
	 *            Numero de vuelo o null si el formato ingresado era incorrecto
	 * @param days
	 *            Lista de dias de salida del vuelo o null si el formato ingresado
	 *            era incorrecto
	 * @param from
	 *            Aeropuerto de salida del vuelo o null si el formato ingresado era
	 *            incorrecto
	 * @param to
	 *            Aeropuerto de llegada del vuelo o null si el formato ingresado era
	 *            incorrecto
	 * @param departure
	 *            Horario de salida del vuelo o null si el formato ingresado era
	 *            incorrecto
	 * @param duration
	 *            Duracion del vuelo o null si el formato ingresado era incorrecto
	 * @param p
	 *            Precio del vuelo en dolares o null si el formato ingresado era
	 *            incorrecto
	 */
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

	/**
	 * 
	 * @param air
	 *            Nombre de la aerolinea del vuelo segun su codigo IATA
	 * @param number
	 *            Numero de vuelo
	 * @param days
	 *            Lista de dias de salida del vuelo
	 * @param from
	 *            Aeropuerto de salida del vuelo
	 * @param to
	 *            Aeropuerto de llegada del vuelo
	 * @param hod
	 *            Hora de salida del vuelo
	 * @param mod
	 *            Minuto de salida del vuelo
	 * @param dM
	 *            Minutos que dura el vuelo
	 * @param dH
	 *            Horas que dura el vuelo
	 * @param p
	 *            Precio del vuelo
	 */
	private void add(String air, int number, List<String> days, String from, String to, int hod, int mod, int dM,
			int dH, double p) {
		Airport f = this.map.get(from);
		Airport t = this.map.get(to);

		if (f == t && f != null) {
			System.out.println("The origin and destination of a flight can not be the same");
			return;
		}

		if (f == null || t == null) {
			System.out.println("The airport does not exist. Please insert the airport before inserting the flight.");
			return;
		}

		for (Airport a : airports) {
			for (Flight flight : a.flights) {
				if (flight.airline.equals(air) && flight.numberOfFlight == number) {
					System.out.println("The flight you are trying to add alredy exist.");
					return;
				}
			}
		}

		Flight newFlight = new Flight(air, number, days, f, t, hod, mod, dM, dH, p);
		System.out.println("The flight " + newFlight.numberOfFlight + " going from " + newFlight.from.name + " to "
				+ newFlight.to.name + " has been added to our network");
		f.flights.add(newFlight);
	}

	/**
	 * Remueve el vuelo correspodiente al nombre si este existe
	 * 
	 * @param air
	 *            Nombre de la aerolinea del vuelo a eliminar segun su codigo IATA o
	 *            null si el formato ingresado era incorrecto
	 * @param number
	 *            Numero del vuelo a eliminar o null si el formato ingresado era
	 *            incorrecto
	 */
	public void removeFlight(String air, Integer number) {
		if (air == null || number == null)
			System.out.println("Invalid command: At least one parameter is not respecting the format ");
		else
			remove(air, number);
	}

	/**
	 * 
	 * @param air
	 *            Nombre de la aerolinea del vuelo a eliminar segun su codigo IATA
	 * @param number
	 *            Numero del vuelo a eliminar
	 */
	private void remove(String air, int number) {
		for (Airport a : airports) {
			Iterator<Flight> iter = a.flights.iterator();
			while (iter.hasNext()) {
				Flight f = iter.next();
				if (f.numberOfFlight == number && f.airline.equals(air)) {
					iter.remove();
					System.out.println("The flight " + f.numberOfFlight + " of " + f.airline
							+ " has been removed from our network");
					return;
				}
			}
		}

		System.out.println("The flight " + number + " of " + air + " that you are trying to remove does not exist");
	}

	/**
	 * Encuentra la ruta entre el aeropuerto de origen y el de destino especificados
	 * 
	 * @param from
	 *            Aeropuerto de salida de la ruta a encontrar o null si el formato
	 *            ingresado era incorrecto
	 * @param to
	 *            Aeropuerto de llegada de la ruta a encontrar o null si el formato
	 *            ingresado era incorrecto
	 * @param priority
	 *            Prioridad pedida para buscar la ruta o null si el formato
	 *            ingresado era incorrecto Visto de otra forma es el peso que vamos
	 *            a usar en las aristas al aplicar Djikstra.
	 * @param days
	 *            Lista de dias en los que se busca comenzar el viaje o null si el
	 *            formato ingresado era incorrecto
	 * @param type
	 *            ​Indica si el formato es tipo texto o tipo KML.
	 * @param output
	 *            ​Indica el destino de la salida
	 * 
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public void findRoute(String from, String to, String priority, List<String> days, String type, String output)
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException,
			IOException {
		if (from == null || to == null || priority == null || days == null) {
			System.out.println("Invalid command: At least one parameter is not respecting the format");
			return;
		}
		List<PQNode> nodes = findRoute(this.map.get(from), this.map.get(to), priority, days, type, output);
		if (nodes == null) {
			System.out.println("Not found");
		} else if (type.equals("KML")) {
			Map<String, List<Double>> route = new LinkedHashMap<>();
			for (PQNode node : nodes) {
				List<Double> aux = new ArrayList<>();
				aux.add(node.airport.latitude);
				aux.add(node.airport.length);
				route.put(node.airport.name, aux);
			}

			KMLFormatter formatter = new KMLFormatter();
			formatter.createKML(route, output);
		}
	}

	/**
	 * 
	 * @param from
	 *            Aeropuerto de salida de la ruta a encontrar
	 * @param to
	 *            Aeropuerto de llegada de la ruta a encontrar
	 * @param priority
	 *            Prioridad pedida para buscar la ruta
	 * @param days
	 *            Lista de dias en los que se busca comenzar el viaje
	 * 
	 * @throws SecurityException
	 * @throws NoSuchFieldException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	private List<PQNode> findRoute(Airport from, Airport to, String priority, List<String> days, String type,
			String output) throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, IOException {
		if (from == null || to == null) {
			return null; // error
		}
		clearMarks();

		PriorityQueue<PQNode> pq = new PriorityQueue<>();
		List<PQNode> ls = new ArrayList<>();

		pq.offer(new PQNode(from, 0, 0, 0, 0, null, "", new ArrayList<>()));

		while (!pq.isEmpty()) {
			PQNode aux = pq.poll();
			if (aux.airport.equals(to)) {
				aux.ls.add(aux);
				if (type.equals("text")) {
					if (output.equals("stdout"))
						System.out.println(aux);
					else {
						List<String> lines = Arrays.asList(aux.toString());
						Path file = Paths.get(output);
						Files.write(file, lines, Charset.forName("UTF-8"));
						System.out.println("File saved!");
					}
				}
				return aux.ls;
			}

			if (!aux.airport.visited || (aux.airport.name.equals(from.name) && priority.equals("tt"))) {
				aux.airport.visited = true;
				for (Flight f : aux.airport.flights) {
					for (String day : f.daysDeparture) {
						if (!aux.airport.name.equals(from.name) || days.isEmpty() || days.contains(day)) {
							ls.addAll(aux.ls);
							ls.add(aux);

							if (priority.equals("ft")) {
								if (aux.flight != null)
									pq.offer(new PQNode(f.to, aux.weight + f.durationHours * 60 + f.durationMinutes,
											aux.price + f.price,
											aux.flightTime + f.durationHours * 60 + f.durationMinutes,
											aux.totalTime + getTimeDiference(aux.flight, aux.currentDay, f, day), f,
											day, ls));
								else {
									pq.offer(new PQNode(f.to, aux.weight + f.durationHours * 60 + f.durationMinutes,
											aux.price + f.price,
											aux.flightTime + f.durationHours * 60 + f.durationMinutes,
											aux.totalTime + f.durationHours * 60 + f.durationMinutes, f, day, ls));
								}
							} else if (priority.equals("pr")) {
								if (aux.flight != null)
									pq.offer(new PQNode(f.to, aux.weight + f.price, aux.price + f.price,
											aux.flightTime + f.durationHours * 60 + f.durationMinutes,
											aux.totalTime + getTimeDiference(aux.flight, aux.currentDay, f, day), f,
											day, ls));
								else
									pq.offer(new PQNode(f.to, aux.weight + f.price, aux.price + f.price,
											aux.flightTime + f.durationHours * 60 + f.durationMinutes,
											aux.totalTime + f.durationHours * 60 + f.durationMinutes, f, day, ls));

							} else {
								if (aux.flight != null)
									pq.offer(new PQNode(f.to,
											aux.weight + getTimeDiference(aux.flight, aux.currentDay, f, day),
											aux.price + f.price,
											aux.flightTime + f.durationHours * 60 + f.durationMinutes,
											aux.totalTime + getTimeDiference(aux.flight, aux.currentDay, f, day), f,
											day, ls));
								else
									pq.offer(new PQNode(f.to,
											aux.weight + aux.totalTime + f.durationHours * 60 + f.durationMinutes,
											aux.price + f.price,
											aux.flightTime + f.durationHours * 60 + f.durationMinutes,
											aux.totalTime + f.durationHours * 60 + f.durationMinutes, f, day, ls));
							}
							ls = new ArrayList<>();
						}
					}

				}
			}

		}
		return null;
	}

	/**
	 * Borra todos los vuelos de la red
	 */
	public void clearFlights() {
		for (Airport a : airports)
			a.flights.clear();
	}

	/**
	 * Retorna la diferencia de tiempo entre un vuelo y otro
	 * 
	 * @param f1
	 *            Vuelo de llegada
	 * @param day1
	 *            Dia que llega el vuelo f1
	 * @param f2
	 *            Vuelo de salida
	 * @param day2
	 *            Dia que sale el vuelo f2
	 * @return
	 */
	private double getTimeDiference(Flight f1, String day1, Flight f2, String day2) {
		double totalMins = 0;
		boolean changeHour, changeDay;

		int minArrival, hourArrival;

		if (f1.minuteOfDeparture + f1.durationMinutes > 60) {
			minArrival = f1.minuteOfDeparture + f1.durationMinutes - 60;
			changeHour = true;
		} else {
			minArrival = f1.minuteOfDeparture + f1.durationMinutes;
			changeHour = false;
		}

		if (changeHour) {
			if (f1.hourOfDeparture + f1.durationHours > 23) {
				hourArrival = f1.hourOfDeparture + f1.durationHours - 23;
				changeDay = true;
			} else {
				hourArrival = f1.hourOfDeparture + f1.durationHours + 1;
				changeDay = false;
			}
		} else {
			if (f1.hourOfDeparture + f1.durationHours > 24) {
				hourArrival = f1.hourOfDeparture + f1.durationHours - 24;
				changeDay = true;
			} else {
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

		if (d1 == -1 || d2 == -1)
			throw new RuntimeException();

		if (d2 > d1) {
			if (f2.hourOfDeparture > hourArrival) {
				if (f2.minuteOfDeparture >= minArrival) {
					totalMins += (d2 - d1) * 24 * 60 + (f2.hourOfDeparture - hourArrival) * 60
							+ (f2.minuteOfDeparture - minArrival);
				} else {
					totalMins += (d2 - d1) * 24 * 60 + (f2.hourOfDeparture - hourArrival - 1) * 60
							+ (60 - (minArrival - f2.minuteOfDeparture));
				}
			} else if (f2.hourOfDeparture < hourArrival) {
				if (f2.minuteOfDeparture >= minArrival) {
					totalMins += (d2 - d1 - 1) * 24 * 60 + (24 - (hourArrival - f2.hourOfDeparture)) * 60
							+ (f2.minuteOfDeparture - minArrival);
				} else {
					totalMins += (d2 - d1 - 1) * 24 * 60 + (23 - (hourArrival - f2.hourOfDeparture)) * 60
							+ (60 - (minArrival - f2.minuteOfDeparture));
				}
			} else {
				if (f2.minuteOfDeparture >= minArrival) {
					totalMins += (d2 - d1) * 24 * 60 + (f2.minuteOfDeparture - minArrival);
				} else {
					totalMins += (d2 - d1 - 1) * 24 * 60 + 23 * 60 + (60 - (minArrival - f2.minuteOfDeparture));
				}
			}
		} else if (d1 > d2) {
			if (f2.hourOfDeparture > hourArrival) {
				if (f2.minuteOfDeparture >= minArrival) {
					totalMins += (7 - (d1 - d2)) * 24 * 60 + (f2.hourOfDeparture - hourArrival) * 60
							+ (f2.minuteOfDeparture - minArrival);
				} else {
					totalMins += (7 - (d1 - d2)) * 24 * 60 + (f2.hourOfDeparture - hourArrival - 1) * 60
							+ (60 - (minArrival - f2.minuteOfDeparture));
				}
			} else if (hourArrival > f2.hourOfDeparture) {
				if (f2.minuteOfDeparture >= minArrival) {
					totalMins += (6 - (d1 - d2)) * 24 * 60 + (24 - (hourArrival - f2.hourOfDeparture)) * 60
							+ (f2.minuteOfDeparture - minArrival);
				} else {
					totalMins += (6 - (d1 - d2)) * 24 * 60 + (23 - (hourArrival - f2.hourOfDeparture)) * 60
							+ (60 - (minArrival - f2.minuteOfDeparture));
				}
			} else {
				if (f2.minuteOfDeparture >= minArrival) {
					totalMins += (7 - (d1 - d2)) * 24 * 60 + (f2.minuteOfDeparture - minArrival);
				} else {
					totalMins += (6 - (d1 - d2)) * 24 * 60 + 23 * 60 + (60 - (minArrival - f2.minuteOfDeparture));
				}
			}
		} else {
			if (f2.hourOfDeparture > hourArrival) {
				if (f2.minuteOfDeparture >= minArrival) {
					totalMins += (f2.hourOfDeparture - hourArrival) * 60 - (f2.minuteOfDeparture - minArrival);
				} else {
					totalMins += (f2.hourOfDeparture - hourArrival - 1) * 60
							- (60 - (minArrival - f2.minuteOfDeparture));
				}
			} else if (f2.hourOfDeparture < hourArrival) {
				if (f2.minuteOfDeparture >= minArrival) {
					totalMins += 6 * 24 * 60 + (24 - (hourArrival - f2.hourOfDeparture)) * 60
							+ (f2.minuteOfDeparture - minArrival);
				} else {
					totalMins += 6 * 24 * 60 + (23 - (hourArrival - f2.hourOfDeparture)) * 60
							+ (60 - (minArrival - f2.minuteOfDeparture));
				}
			} else {
				if (f2.minuteOfDeparture >= minArrival) {
					totalMins += f2.minuteOfDeparture - minArrival;
				} else {
					totalMins += 6 * 24 * 60 + 23 * 60 + (60 - (minArrival - f2.minuteOfDeparture));
				}
			}
		}

		totalMins += f2.durationHours * 60 + f2.durationMinutes;
		return totalMins;
	}

	private int getDay(String d) {
		switch (d) {
		case ("Lu"):
			return 0;
		case ("Ma"):
			return 1;
		case ("Mi"):
			return 2;
		case ("Ju"):
			return 3;
		case ("Vi"):
			return 4;
		case ("Sa"):
			return 5;
		case ("Do"):
			return 6;
		default:
			return -1;
		}
	}

	private String getStringDay(int d) {
		switch (d) {
		case (0):
			return "Lu";
		case (1):
			return "Ma";
		case (2):
			return "Mi";
		case (3):
			return "Ju";
		case (4):
			return "Vi";
		case (5):
			return "Sa";
		case (6):
			return "Do";
		default:
			return "Unknown";
		}
	}

	public void clearMarks() {
		for (Airport a : airports)
			a.visited = false;
	}

	@SuppressWarnings("hiding")
	private class PQNode implements Comparable<PQNode> {
		Airport airport;
		double weight;
		double price;
		double flightTime;
		double totalTime;
		String currentDay;
		Flight flight;
		List<PQNode> ls;

		public int compareTo(PQNode other) {
			return Double.valueOf(weight).compareTo(other.weight);
		}

		public PQNode(Airport a, double weight, double price, double flightTime, double totalTime, Flight f,
				String currentDay, List<PQNode> ls) {
			this.airport = a;
			this.weight = weight;
			this.price = price;
			this.flightTime = flightTime;
			this.totalTime = totalTime;
			this.flight = f;
			this.currentDay = currentDay;
			this.ls = ls;
		}

		@Override
		public String toString() {
			StringBuffer ans = new StringBuffer();
			ans.append("Precio#" + this.price);
			ans.append('\n');
			ans.append("TiempoVuelo#" + (int) this.flightTime / 60 + "h" + (int) this.flightTime % 60 + "m");
			ans.append('\n');
			ans.append("TiempoTotal#" + (int) this.totalTime / 60 + "h" + (int) this.totalTime % 60 + "m");
			ans.append('\n');
			ans.append('\n');

			for (PQNode pq : ls) {
				if (pq.flight != null)
					ans.append(pq.flight.from.name + "#" + pq.flight.airline + "#" + pq.flight.numberOfFlight + "#"
							+ pq.currentDay + "#" + pq.flight.to.name + '\n');
			}

			return ans.toString();
		}
	}

	/**
	 * La funcion revisa que el grafo dirigido sea fuertemente conexo
	 * 
	 * @return true si el grafo dirigido es fuertemente conexo, false en otro caso
	 */
	public boolean isStronglyConnected(String initial) {
		clearMarks();
		if (oneToAll(this.map.get(initial), 1) != this.airports.size())
			return false;
		for (int i = 0; i < this.airports.size(); i++) {
			clearMarks();
			if (!canReach(this.airports.get(i), this.map.get(initial))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * La funcion revisa que el nodo inicial que va a iniciar el ciclo hamiltoniano
	 * pueda llegar a todos a traves de un DFS sencillo
	 * 
	 * @param current
	 *            Es el nodo de la recursion
	 * @param vertexAmount
	 *            Lleva la cuenta de la cantidad de vertices visitados
	 * @return Retorna la cantidad de vertices visitados
	 */
	private int oneToAll(Airport current, int vertexAmount) {
		if (current.flights.size() == 0)
			return 0;

		current.visited = true;

		for (Flight edge : current.flights) {
			if (edge.to.visited != true) {
				vertexAmount = oneToAll(edge.to, vertexAmount + 1);
				if (vertexAmount == 0)
					return 0;
			}
		}

		return vertexAmount;
	}

	/**
	 * La funcion revisa que cualquier nodo que no sea el inicial del ciclo
	 * hamiltoniano pueda regresar al nodo inicial del ciclo
	 * 
	 * @param current
	 *            Es el nodo de la recursion
	 * @param target
	 *            Es el nodo que debe poder alcanzar
	 * @return true si puede alcanzar el vertice target, false en otro caso
	 */
	private boolean canReach(Airport current, Airport target) {
		if (existConnection(current, target))
			return true;

		current.visited = true;
		for (Flight edge : current.flights) {
			if (edge.to.visited != true)
				return canReach(edge.to, target);
		}
		return false;

	}

	/**
	 * La funcion revisa que el grafo no dirigido del mapa de conexiones de
	 * aeropuertos no tenga vertices de corte
	 * 
	 * @return true, si tiene vertices de corte, false en otro caso
	 */
	public boolean areCutVertex() {
		int index;
		clearMarks();
		AirportNetwork notDirected = new AirportNetwork();
		for (int i = 0; i < this.airports.size(); i++) {
			Airport current = this.airports.get(i);
			notDirected.addAirportNotDirected(current.name, current.latitude, current.length);
		}
		notDirected.createNotDirected(this.airports.get(0));
		for (int visited = 0; visited < notDirected.airports.size(); visited++) {
			if (visited > 0)
				index = visited - 1;
			else
				index = visited + 1;
			if (notDirected.areCutVertex(index, visited))
				return true;
		}
		return false;
	}

	private void addAirportNotDirected(String name, double latitude, double length) {
		Airport a = new Airport(name, latitude, length);
		this.airports.add(a);
		this.map.put(name, a);

	}

	/**
	 * Revisa que los nodos del grafo no dirigido no sean vertices de corte
	 * 
	 * @param index
	 *            Indice del vertice a verificar
	 * @return true, si el vertice a verificar es de corte, false en otro caso
	 */
	private boolean areCutVertex(int index, int visited) {
		clearMarks();
		this.airports.get(visited).visited = true;
		if (isCutVertex(this.airports.get(index), 1) == this.airports.size())
			return false;
		else
			return true;
	}

	/**
	 * Crea un grafo identico al dirigido, pero con aristas no dirigidas
	 * 
	 * @param current
	 *            Es el nodo actual de la recusion
	 */
	private void createNotDirected(Airport current) {
		current.visited = true;
		for (Flight edge : current.flights) {
			this.addFlightNotDirected(edge.airline, edge.numberOfFlight, edge.daysDeparture, edge.from.name,
					edge.to.name, "12:00", "12h14m", edge.price);
			this.addFlightNotDirected(edge.airline, -edge.numberOfFlight, edge.daysDeparture, edge.to.name,
					edge.from.name, "12:00", "12h14m", edge.price);
			if (edge.to.visited != true) {
				createNotDirected(edge.to);
			}
		}
	}

	private void addFlightNotDirected(String air, Integer number, List<String> days, String from, String to,
			String departure, String duration, Double p) {

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

		addNotDirected(air, number, days, from, to, Integer.parseInt(dep[0]), Integer.parseInt(dep[1]),
				Integer.parseInt(dM), Integer.parseInt(dH), p);

	}

	private void addNotDirected(String air, int number, List<String> days, String from, String to, int hod, int mod,
			int dM, int dH, double p) {

		Airport f = this.map.get(from);
		Airport t = this.map.get(to);

		Flight newFlight = new Flight(air, number, days, f, t, hod, mod, dM, dH, p);
		f.flights.add(newFlight);
	}

	/**
	 * Funcion recursiva de areCutVertex()
	 * 
	 * @param current
	 *            Nodo actual de la recursion
	 * @param airports
	 *            Cantidad de aeropuertos visitados
	 * @return true si es vertice de corte o false en caso contrario
	 */
	private int isCutVertex(Airport current, int airports) {
		current.visited = true;
		airports++;
		for (Flight edge : current.flights) {
			if (edge.to.visited != true)
				airports = isCutVertex(edge.to, airports);
		}
		return airports;
	}

	/**
	 * Revisa si existe vuelo dirigido entre 2 aeropuertos
	 * 
	 * @param first
	 *            Aeropuerto inicio
	 * @param second
	 *            Aeropuerto destino
	 * @return true si existe dicho vuelo o false en caso contrario
	 */
	private boolean existConnection(Airport first, Airport second) {
		for (Flight edge : first.flights) {
			if (edge.to == second)
				return true;
		}
		return false;
	}

	/**
	 * Genera una copia de la lista pedida
	 * 
	 * @param list
	 *            Lista a copiar
	 * @return Una nueva lista, que es copia de la del parametro
	 */
	private List<Flight> copyList(List<Flight> list) {
		List<Flight> newList = new LinkedList<Flight>();
		for (Flight edge : list)
			newList.add(edge);
		return newList;
	}

	private List<String> copyListDays(List<String> list) {
		List<String> newList = new LinkedList<String>();
		for (String day : list)
			newList.add(day);
		return newList;
	}

	public List<Airport> getAirports() {
		return this.airports;
	}

	/**
	 * @param connection
	 *            Vuelo que va a realizar
	 * @param arrivalHour
	 *            Hora de llegada al primer aeropuerto
	 * @param arrivalMinute
	 *            Minuto de llegada al primer aeropuerto
	 * @param arrivalDay
	 *            Dia de llegada al primer aeropuerto
	 * @return el tiempo total desde que el pasajero sale del aeropuerto origen
	 *         hasta que llega al destino
	 */
	public int calculateTotalTime(HamiltonCicle cycle, Flight connection, int arrivalHour, int arrivalMinute,
			String arrivalDay) {
		int arrivalDayInt = getDay(arrivalDay);
		int differenceDay = 50;
		int totalArrivalMinutes = arrivalHour * 60 + arrivalMinute;
		int totalDepartureMinutes = connection.hourOfDeparture * 60 + connection.minuteOfDeparture;
		int result;
		for (String day : connection.daysDeparture) {
			int departureDay = getDay(day);
			if (departureDay > arrivalDayInt) {
				if (departureDay - arrivalDayInt < differenceDay) {
					differenceDay = departureDay - arrivalDayInt;
					cycle.departureDay = day;

				}
			} else if (departureDay < arrivalDayInt) {
				if (7 - arrivalDayInt + departureDay < differenceDay) {
					differenceDay = 7 - arrivalDayInt + departureDay;
					cycle.departureDay = day;
				}
			} else {
				if (totalDepartureMinutes > totalArrivalMinutes) {
					differenceDay = 0;
					cycle.departureDay = day;
				}
			}

		}
		int flightDuration = connection.durationHours * 60 + connection.durationMinutes;
		if (totalDepartureMinutes > totalArrivalMinutes)
			result = differenceDay * 24 * 60 + (totalDepartureMinutes - totalArrivalMinutes) + flightDuration;
		else
			result = differenceDay * 24 * 60 - (totalArrivalMinutes - totalDepartureMinutes) + flightDuration;

		return result;
	}

	/**
	 * Actualiza la hora, minuto y dia de llegada de un pasajero a un aeropuerto
	 * 
	 * @param totalMinutes
	 *            Cantidad de minutos, calculados en calculateTotalTime(...)
	 * @param current
	 *            Objeto que tiene la informacion necesaria
	 */
	private void calculateArrivalTime(int totalMinutes, HamiltonCicle current) {
		int days = totalMinutes / 60 / 24;
		totalMinutes -= days * 24 * 60;
		int hours = totalMinutes / 60;
		totalMinutes -= hours * 60;
		int minutes = totalMinutes;
		int finalDay;

		current.arrivalMinutes += minutes;
		if (current.arrivalMinutes >= 60) {
			current.arrivalMinutes -= 60;
			hours += 1;
		} else if (current.arrivalMinutes < 0) {
			current.arrivalMinutes += 60;
			hours -= 1;
		}

		current.arrivalHours += hours;
		if (current.arrivalHours >= 24) {
			current.arrivalHours -= 24;
			days += 1;

		} else if (current.arrivalHours < 0) {
			current.arrivalHours += 24;
			days -= 1;
		}
		finalDay = getDay(current.arrivalDay) + days;
		if (finalDay >= 7) {
			finalDay -= 7;
		} else if (finalDay < 0) {
			finalDay += 7;
		}

		current.arrivalDay = getStringDay(finalDay);
	}

	/**
	 * Funcion wrapper que chequea si el formato de los parametros recibido es
	 * correcto
	 * 
	 * @param initial
	 *            Nombre del aeropuerto donde comenzara la vuelta al mundo segun su
	 *            codigo IATA o null si el formato ingresado era incorrecto
	 * @param priority
	 *            Prioridad pedida para buscar la vuelta al mundo o null si el formato ingresado era incorrecto
	 * @param days
	 *            Lista de dias posibles de inicio de la vuelta al mundo o null si el formato ingresado era incorrecto
	 * @throws IOException
	 * 
	 */
	public void worldTrip(String from, String priority, List<String> days, String typeFormat, String outputFormat)
			throws IOException {
		if (from == null || priority == null || days == null) {
			System.out.println("Invalid command: At least one parameter is not respecting the format");
			return;
		} else {
			if (priority.equals("pr"))
				priority = "Price";
			if (priority.equals("tt"))
				priority = "totalDuration";
			if (priority.equals("ft"))
				priority = "Duration";
			worldTripEfficient(from, priority, days, typeFormat, outputFormat);
		}
	}

	/**
	 * 
	 * @param initial
	 *            Nombre del aeropuerto donde comenzara la vuelta al mundo segun su
	 *            codigo IATA
	 * @param priority
	 *            Prioridad pedida para buscar la vuelta al mundo
	 * @param days
	 *            Lista de dias posibles de inicio de la vuelta al mundo
	 * @throws IOException
	 */
	private void worldTripEfficient(String initial, String priority, List<String> days, String typeFormat,
			String outputFormat) throws IOException {
		clearMarks();
		if (!this.isStronglyConnected(initial)) {
			System.out.println("There is not a hamiltonian cycle");
			return;
		}
		clearMarks();
		if (this.areCutVertex()) {
			System.out.println("There is not a hamiltonian cycle");
			return;
		}
		HamiltonCicle result = new HamiltonCicle(0, 0, "Lu");
		if (priority == "totalDuration") {
			for (int i = 0; i < days.size(); i++) {
				HamiltonCicle current = worldTripEfficient(initial, priority, days.get(i), days);
				substractExtraTime(current);
				if (!isPassed(result, current, priority))
					result = current;
				if (result.flightDuration == 0)
					System.out.println("There is not a hamiltonian cycle");
			}
		} else
			result = worldTripEfficient(initial, priority, days.get(0), days);

		if (result.flightDuration == 0)
			System.out.println("There is not a hamiltonian cycle");

		if (typeFormat.equals("text")) {
			if (outputFormat.equals("stdout")) {
				System.out.println(result);
			} else {
				List<String> lines = Arrays.asList(result.toString());
				Path file = Paths.get(outputFormat);
				Files.write(file, lines, Charset.forName("UTF-8"));
				System.out.println("File saved!");
			}
		} else if (typeFormat.equals("KML")) {
			Map<String, List<Double>> route = new LinkedHashMap<>();
			for (Flight f : result.flights) {
				List<Double> aux = new ArrayList<>();
				aux.add(f.from.latitude);
				aux.add(f.from.length);
				route.put(f.from.name + " ", aux);
			}
			Flight last = result.flights.get(result.flights.size() - 1);
			List<Double> aux = new ArrayList<>();
			aux.add(last.to.latitude);
			aux.add(last.to.length);
			route.put(last.to.name, aux);

			// System.out.println(route);
			KMLFormatter formatter = new KMLFormatter();
			formatter.createKML(route, outputFormat);
		}
	}

	/**
	 * 
	 * @param initial
	 *            Nodo inicial del ciclo hamiltoniano
	 * @param priority
	 *            Prioridad a tomar en cuenta
	 * @param day
	 *            Dia de inicio del ciclo hamiltoniano
	 * @return Retorna el mejor ciclo segun la prioridad pedida
	 */

	private HamiltonCicle worldTripEfficient(String initial, String priority, String day, List<String> days) {
		clearMarks();
		HamiltonCicle hamiltonCicle = new HamiltonCicle(0, 0, day);
		HamiltonCicle current = new HamiltonCicle(0, 0, day);
		hamiltonCicle = getHamiltonCiclesEfficient(this.map.get(initial), this.map.get(initial), 1, hamiltonCicle,
				current, null, priority, days);
		return hamiltonCicle;
	}

	/**
	 * Funcion recursiva que obtiene el ciclo hamiltoniano
	 * 
	 * @param initial
	 *            Nodo inicial del ciclo
	 * @param current
	 *            Nodo actual de la recursion
	 * @param nodesCount
	 *            Cantidad de nodos visitados
	 * @param efficient
	 *            Ciclo eficiente actual
	 * @param currentList
	 *            Ciclo que se esta revisando
	 * @param connection
	 *            Vuelo que conecta 2 aeropuertos
	 * @param priority
	 *            Prioridad a tomar en cuenta para definir que ciclo es mejor
	 * @return Retorna el mejor ciclo en funcion de los parametros pedidos
	 */

	private HamiltonCicle getHamiltonCiclesEfficient(Airport initial, Airport current, int nodesCount,
			HamiltonCicle efficient, HamiltonCicle currentList, Flight connection, String priority, List<String> days) {
		if (isPassed(efficient, currentList, priority)) {
			return efficient;
		}

		current.visited = true;
		if (nodesCount == this.airports.size() + 1) {
			int totalTime = calculateTotalTime(currentList, connection, currentList.arrivalHours,
					currentList.arrivalMinutes, currentList.arrivalDay);
			calculateArrivalTime(totalTime, currentList);
			currentList.flights.add(connection);
			currentList.daysDeparture.add(currentList.departureDay);
			currentList.price += connection.price;
			currentList.flightDuration += connection.getTotalDuration();
			currentList.totalDuration += totalTime;
			if (!isPassed(efficient, currentList, priority)) {
				efficient.flights = copyList(currentList.flights);
				efficient.daysDeparture = copyListDays(currentList.daysDeparture);
				efficient.price = currentList.price;
				efficient.flightDuration = currentList.flightDuration;
				efficient.totalDuration = currentList.totalDuration;
			}
			currentList.flights.remove(connection);
			currentList.daysDeparture.remove(currentList.daysDeparture.size() - 1);
			currentList.price -= connection.price;
			currentList.flightDuration -= connection.getTotalDuration();
			currentList.totalDuration -= totalTime;
			calculateArrivalTime(-totalTime, currentList);
			return efficient;
		}
		int totalTime = 0;
		if (connection != null) {
			totalTime = calculateTotalTime(currentList, connection, currentList.arrivalHours,
					currentList.arrivalMinutes, currentList.arrivalDay);
			calculateArrivalTime(totalTime, currentList);
			currentList.daysDeparture.add(currentList.departureDay);
			currentList.flights.add(connection);
			currentList.price += connection.price;
			currentList.flightDuration += connection.getTotalDuration();
			currentList.totalDuration += totalTime;
		}

		List<Airport> visited = new LinkedList<Airport>();
		for (Flight edge : current.flights) {
			if ((((edge.to.visited != true || (edge.to == initial && nodesCount == this.airports.size()))
					&& !visited.contains(edge.to)))) {
				edge = existBetterFlight(current, edge.to, priority, currentList, days, initial);
				if (edge != null) {
					efficient = getHamiltonCiclesEfficient(initial, edge.to, nodesCount + 1, efficient, currentList,
							edge, priority, days);
					visited.add(edge.to);
				}
			}
		}
		if (nodesCount != this.airports.size() + 1
				|| (nodesCount == this.airports.size() + 1 && !existConnection(current, initial))) {

			current.visited = false;
			if (connection != null) {
				currentList.flights.remove(connection);
				currentList.daysDeparture.remove(currentList.daysDeparture.size() - 1);
				currentList.price -= connection.price;
				currentList.flightDuration -= connection.getTotalDuration();
				currentList.totalDuration -= totalTime;
				calculateArrivalTime(-totalTime, currentList);
			}
		}
		return efficient;
	}

	/**
	 * Permite cortar el backtracking antes, si el ciclo que se esta construyendo
	 * (current) ya es peor que efficient
	 * 
	 * @param efficient
	 *            Ciclo mas eficiente actual
	 * @param current
	 *            Ciclo que se esta revisando
	 * @param priority
	 *            Prioridad a tener en cuenta
	 * @return true, si el current es peor que el efficient, false en otro caso
	 */
	private boolean isPassed(HamiltonCicle efficient, HamiltonCicle current, String priority) {
		if (efficient.price == 0 || efficient.flightDuration == 0)
			return false;

		if (priority == "Price") {
			if (efficient.price < current.price)
				return true;
			else
				return false;
		} else if (priority == "Duration") {
			if (efficient.flightDuration < current.flightDuration)
				return true;
			else
				return false;
		}

		else {
			if (efficient.totalDuration < current.totalDuration)
				return true;
			else
				return false;
		}

	}

	/**
	 * 
	 * @param start
	 *            Aeropuerto inicio
	 * @param end
	 *            Aeropuerto destino
	 * @param priority
	 *            Prioridad a tener en cuenta
	 * @param current
	 *            Ciclo que se esta construyendo
	 * @return el mejor vuelo, en funcion de la prioridad, entre inicio y destino
	 */
	private Flight existBetterFlight(Airport start, Airport end, String priority, HamiltonCicle current,
			List<String> days, Airport initial) {
		Flight result = new Flight();
		result.price = 0;
		result.hourOfDeparture = 0;
		result.minuteOfDeparture = 0;
		for (Flight edge : start.flights) {
			if (edge.to == end) {
				if (start == initial) {
					if (flightContainsDays(edge, days))
						if (checkPriority(result, edge, priority, current))
							result = edge;
				} else {
					if (checkPriority(result, edge, priority, current))
						result = edge;
				}

			}
		}

		if (result.price == 0)
			return null;

		return result;
	}

	/**
	 * Revisa que el vuelo pueda comenzar en ese dia
	 */
	private boolean flightContainsDays(Flight flight, List<String> days) {
		for (String daysFlight : flight.daysDeparture)
			if (days.contains(daysFlight))
				return true;
		return false;
	}

	/**
	 * 
	 * @param result
	 *            Mejor vuelo encontrado hasta el momento
	 * @param edge
	 *            Vuelo que se esta revisando
	 * @param priority
	 *            Prioridad a tener en cuenta
	 * @param currentList
	 *            Ciclo que se esta construyendo
	 * @return true, si el vuelo edge, es mejor que result, en funcion de la
	 *         prioridad, false en otro caso
	 */

	private boolean checkPriority(Flight result, Flight edge, String priority, HamiltonCicle currentList) {
		int resultDuration = result.durationHours * 60 + result.durationMinutes;
		int edgeDuration = edge.durationHours * 60 + edge.durationMinutes;
		if (result.price == 0 || resultDuration == 0)
			return true;
		if (priority == "Price") {
			if (result.price < edge.price)
				return false;
			else
				return true;
		}
		if (priority == "Duration") {
			if (resultDuration < edgeDuration)
				return false;
			else
				return true;
		} else {
			resultDuration = calculateTotalTime(currentList, result, currentList.arrivalHours,
					currentList.arrivalMinutes, currentList.arrivalDay);
			edgeDuration = calculateTotalTime(currentList, edge, currentList.arrivalHours, currentList.arrivalMinutes,
					currentList.arrivalDay);
			if (resultDuration < edgeDuration)
				return false;
			else
				return true;
		}
	}

	/**
	 * Elimina el tiempo extra, puesto que al iniciar el ciclo inicia con el horario
	 * 00:00. Esta funcion hace que la diferencia entre 00:00 y el primer vuelo se
	 * elimine
	 * 
	 * @param cycle
	 *            Ciclo hamiltoniano construido
	 */

	private void substractExtraTime(HamiltonCicle cycle) {
		if (cycle != null && cycle.flights.size() > 0) {
			Flight first = cycle.flights.get(0);
			int hourDeparture = first.hourOfDeparture;
			int minuteDeparture = first.minuteOfDeparture;
			cycle.totalDuration -= (hourDeparture * 60 + minuteDeparture);
		}
	}

	private class HamiltonCicle {
		private List<Flight> flights;
		private List<String> daysDeparture;
		private double price;
		private int totalDuration;
		private int flightDuration;
		private int arrivalMinutes;
		private int arrivalHours;
		private String arrivalDay;
		private String departureDay;

		public HamiltonCicle(int arrivalMinutes, int arrivalHours, String arrivalDay) {
			flights = new LinkedList<Flight>();
			daysDeparture = new LinkedList<String>();
			price = 0;
			totalDuration = 0;
			flightDuration = 0;
			this.arrivalMinutes = arrivalMinutes;
			this.arrivalHours = arrivalHours;
			this.arrivalDay = arrivalDay;
		}

		@Override
		public String toString() {
			StringBuffer ans = new StringBuffer();
			ans.append("Precio#" + this.price);
			ans.append('\n');
			ans.append("TiempoVuelo#" + (int) this.flightDuration / 60 + "h" + (int) this.flightDuration % 60 + "m");
			ans.append('\n');
			ans.append("TiempoTotal#" + (int) this.totalDuration / 60 + "h" + (int) this.totalDuration % 60 + "m");
			ans.append('\n');
			ans.append('\n');
			int i = 0;

			for (Flight f : flights) {

				if (f != null)
					ans.append(f.from.name + "#" + f.airline + "#" + f.numberOfFlight + "#" + daysDeparture.get(i) + "#"
							+ f.to.name + '\n');
				i++;
			}

			return ans.toString();
		}
	}

	public void printConnections() {
		clearMarks();
		printConnections(this.airports.get(0));
	}

	private void printConnections(Airport current) {
		current.visited = true;
		for (Flight edge : current.flights)
			System.out.println(edge.from.name + " -> " + edge.to.name);
		for (Flight edge : current.flights)
			if (edge.to.visited != true)
				printConnections(edge.to);
	}
}