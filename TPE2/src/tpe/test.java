package tpe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, IOException {
		AirportNetwork red = new AirportNetwork();
		Scanner scan = new Scanner(System.in);

		while (scan.hasNext()) {
			String command = scan.nextLine();
			
			if (command.toLowerCase().equals("exit"))
				break;

			if (command.length() > 16 && command.toLowerCase().substring(0, 15).equals("insert airport ")) {
				String characteristics = command.substring(15);
				String[] c = characteristics.split(" ");

				String name = checkName(c[0]);
				Double latitude = checkLatitude(c[1]);
				Double length = checkLength(c[2]);
				
				red.addAirport(name, latitude, length);
			} else if (command.length() > 21 && command.toLowerCase().substring(0, 20).equals("insert all airports ")) {
				String characteristics = command.substring(20);
				String[] c = characteristics.split(" ");
				String fileName = checkFile(c[0]);
				String option = checkOption(c[1]);

				red.massAddA(fileName, option);
			} else if (command.length() > 16 && command.toLowerCase().substring(0, 15).equals("delete airport ")) {
				String name = command.substring(15, 18);

				red.removeAirport(name);
			} else if (command.length() == 18 && command.toLowerCase().substring(0, 18).equals("delete all airport")) {
				red.clearAirports();
			} else if (command.toLowerCase().substring(0, 14).equals("insert flight ")) {
				String characteristics = command.substring(14);
				String[] c = characteristics.split(" ");
				String air = checkAir(c[0]);
				Integer number = Integer.parseInt(c[1]);
				List<String> days = checkDays(c[2]);
				String from = checkName(c[3]);
				String to = checkName(c[4]);
				String departure = checkDeparture(c[5]);
				String duration = checkDuration(c[6]);
				Double price = checkPrice(c[7]);
				
				red.addFlight(air, number, days, from, to, departure, duration, price);
			} else if (command.toLowerCase().substring(0, 18).equals("insert all flight ")) {
				String characteristics = command.substring(18);
				String[] c = characteristics.split(" ");
				String fileName = checkFile(c[0]);
				String option = checkOption(c[1]);

				red.massAddF(fileName, option);
			} else if (command.toLowerCase().substring(0, 14).equals("delete flight ")) {
				String characteristics = command.substring(14);
				String[] c = characteristics.split(" ");
				String air = checkAir(c[0]);
				Integer number = Integer.parseInt(c[1]);

				red.removeFlight(air, number);
			} else if (command.length() == 18 && command.toLowerCase().substring(0, 18).equals("delete all flight")) {
				red.clearFlights();
			} else if (command.length() > 11 && command.substring(0, 10).equals("findRoute ")) {
				String characteristics = command.substring(10);
				String[] c = characteristics.split(" ");

				String from = checkName(c[0]);
				String to = checkName(c[1]);
				String priority = checkPriority(c[2]);
				List<String> days = checkDays(c[3]);
				
				red.findRoute(from, to, priority, days);
			} else if (command.length() > 14 && command.substring(0, 13).equals("outputFormat ")) {
				String characteristics = command.substring(13);
				String[] c = characteristics.split(" ");

				String type = checkType(c[0]);
				String output = checkOutput(c[1]);
				
				// LLAMAR A KML.. Tener una funcion wrapper y chequear que los parametros no sean null
				//Si son null tirar invalid command, y sino llamar a la funcion q corrresponda(privada)
			} else if (command.length() > 11 && command.substring(0, 10).equals("worldTrip ")) {
				String characteristics = command.substring(10);
				String[] c = characteristics.split(" ");

				String from = checkName(c[0]);
				String priority = checkPriority(c[1]);
				List<String> days = checkDays(c[2]);
				
				// red.worldTrip(from, priority, days) 
				// Tener una funcion wrapper y chequear que los parametros no sean null
				// Si son null tirar invalid command, y sino llamar a la funcion q corrresponda(privada)
			} else {
				System.out.println("Invalid Command");
			}

		}

		red.printAirports();

		scan.close();

	}

	public static Double checkLatitude(String latitude) {
		String PATTERN_LATITUDE = "^(-)?(?:90(?:(?:\\.0{1,7})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,7})?))$";
		Pattern pattern = Pattern.compile(PATTERN_LATITUDE);

		Matcher matcher = pattern.matcher(latitude);
		if (matcher.matches())
			return Double.parseDouble(latitude);

		return null;
	}

	public static Double checkLength(String length) {
		String PATTERN_LENGTH = "^(-)?(?:180(?:(?:\\.0{1,7})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,7})?))$";
		Pattern pattern = Pattern.compile(PATTERN_LENGTH);

		Matcher matcher = pattern.matcher(length);
		if (matcher.matches())
			return Double.parseDouble(length);

		return null;
	}

	public static String checkAir(String airline) {
		if (airline.length() <= 3)
			return airline;

		return null;
	}

	public static List<String> checkDays(String days) {
		String[] d = days.split("-");
		List<String> list = new LinkedList<String>();

		for (String each : d) {
			if (!each.equals("Lu") && !each.equals("Ma") && !each.equals("Mi") && !each.equals("Ju")
					&& !each.equals("Vi") && !each.equals("Sa") && !each.equals("Do"))
				return null;
		else 
				list.add(each);
		}
		return list;
	}

	public static String checkName(String name) {
		if (name.length() <= 3)
			return name;

		return null;
	}

	public static String checkDeparture(String departure) {
		String PATTERN_TIME = "^([0-1][0-9]|[2][0-3]):[0-5][0-9]$";
		Pattern pattern = Pattern.compile(PATTERN_TIME);

		Matcher matcher = pattern.matcher(departure);
		if (matcher.matches())
			return departure;
		return null;
	}

	public static String checkDuration(String duration) {
		String PATTERN_DURATION = "^(1[0-9]h|[0-9]h)?[0-5][0-9]m";
		Pattern pattern = Pattern.compile(PATTERN_DURATION);

		Matcher matcher = pattern.matcher(duration);
		if (matcher.matches())
			return duration;
		return null;
	}

	public static Double checkPrice(String price) {
		String PATTERN_PRICE = "^[1-9][0-9]{3}[0-9]*(.[0-9]{1,2})?$";
		Pattern pattern = Pattern.compile(PATTERN_PRICE);

		Matcher matcher = pattern.matcher(price);
		if (matcher.matches())
			return Double.parseDouble(price);
		return null;
	}

	public static String checkFile(String file) {
		String PATTERN_FILE = "^[a-zA-Z0-9]+.txt$";
		Pattern pattern = Pattern.compile(PATTERN_FILE);

		Matcher matcher = pattern.matcher(file);
		if (matcher.matches())
			return file;
		return null;

	}

	public static String checkOption(String option) {
		if (option.equals("append") || option.equals("replace"))
			return option;

		return null;
	}

	public static String checkPriority(String priority) {
		if (priority.equals("ft") || priority.equals("pr") || priority.equals("tt"))
			return priority;

		return null;
	}

	public static String checkType(String type) {
		if (type.equals("text") || type.equals("KML"))
			return type;

		return null;
	}

	public static String checkOutput(String output) {
		if (output.equals("archivo") || output.equals("stdout"))
			return output;

		return null;
	}
}
