package airport;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

	public static void main(String[] args) throws NoSuchFieldException, SecurityException, IllegalArgumentException,
			IllegalAccessException, IOException {
		AirportNetwork red = new AirportNetwork();
		Scanner scan = new Scanner(System.in);

		String outputFormat = null;
		String typeFormat = null;

		while (scan.hasNext()) {
			String command = scan.nextLine();
			
			if (command.toLowerCase().equals("exit"))
				break;

			if (command.length() > 16 && command.toLowerCase().substring(0, 15).equals("insert airport ")) {
				String characteristics = command.substring(15);
				String[] c = characteristics.split(" ");
				if (c.length == 3) {
					String name = checkName(c[0]);
					Double latitude = checkLatitude(c[1]);
					Double length = checkLength(c[2]);

					red.addAirport(name, latitude, length);
				} else {
					System.out.println(
							"Invalid Command: You are inserting a different amount of parameters than the required");
				}
			} else if (command.length() > 21 && command.toLowerCase().substring(0, 20).equals("insert all airports ")) {
				String characteristics = command.substring(20);
				String[] c = characteristics.split(" ");

				if (c.length == 2) {
					FileInputStream fileName = checkFile(c[0]);
					String option = checkOption(c[1]);

					String strLine;
					if (fileName != null) {
						BufferedReader br = new BufferedReader(new InputStreamReader(fileName));

						while ((strLine = br.readLine()) != null) {
							String[] str = strLine.split("#");
							if (str.length == 3) {
								String name = checkName(str[0]);
								Double latitude = checkLatitude(str[1]);
								Double length = checkLength(str[2]);
								red.massAddA(option, name, latitude, length);
							}
						}
					}
				} else {
					System.out.println(
							"Invalid Command: You are inserting a different amount of parameters than the required");
				}

			} else if (command.length() > 16 && command.toLowerCase().substring(0, 15).equals("delete airport ")) {

				String name = checkName(command.substring(15));
				red.removeAirport(name);

			} else if (command.length() == 18 && command.toLowerCase().substring(0, 18).equals("delete all airport")) {
				red.clearAirports();
			} else if (command.length() > 15 && command.toLowerCase().substring(0, 14).equals("insert flight ")) {
				String characteristics = command.substring(14);
				String[] c = characteristics.split(" ");

				if (c.length == 8) {
					String air = checkAir(c[0]);
					Integer number = checkNumber(c[1]);
					List<String> days = checkDays(c[2]);
					String from = checkName(c[3]);
					String to = checkName(c[4]);
					String departure = checkDeparture(c[5]);
					String duration = checkDuration(c[6]);
					Double price = checkPrice(c[7]);
					
					red.addFlight(air, number, days, from, to, departure, duration, price);
				} else {
					System.out.println(
							"Invalid Command: You are inserting a different amount of parameters than the required");
				}
			} else if (command.length() > 20 && command.toLowerCase().substring(0, 19).equals("insert all flights ")) {
				String characteristics = command.substring(19);
				String[] c = characteristics.split(" ");

				if (c.length == 2) {
					FileInputStream fileName = checkFile(c[0]);
					String option = checkOption(c[1]);

					String strLine;
					if (fileName != null) {
						BufferedReader br = new BufferedReader(new InputStreamReader(fileName));

						while ((strLine = br.readLine()) != null) {
							String[] str = strLine.split("#");
							if (str.length == 8) {
								String air = checkAir(str[0]);
								Integer number = checkNumber(str[1]);
								List<String> days = checkDays(str[2]);
								String from = checkName(str[3]);
								String to = checkName(str[4]);
								String departure = checkDeparture(str[5]);
								String duration = checkDuration(str[6]);
								Double price = checkPrice(str[7]);
								
								red.massAddF(option, air, number, days, from, to, departure, duration, price);
							}
						}
					}
				} else {
					System.out.println(
							"Invalid Command: You are inserting a different amount of parameters than the required");
				}
			} else if (command.length() > 15 && command.toLowerCase().substring(0, 14).equals("delete flight ")) {
				String characteristics = command.substring(14);
				String[] c = characteristics.split(" ");

				if (c.length == 2) {
					String air = checkAir(c[0]);
					Integer number = checkNumber(c[1]);

					red.removeFlight(air, number);
				} else {
					System.out.println(
							"Invalid Command: You are inserting a different amount of parameters than the required");
				}
			} else if (command.length() == 17 && command.toLowerCase().substring(0, 17).equals("delete all flight")) {
				red.clearFlights();
			} else if (command.length() > 11 && command.substring(0, 10).equals("findRoute ")) {
				String characteristics = command.substring(10);
				String[] c = characteristics.split(" ");
				if (c.length == 4 || c.length == 3) {
					String from = checkName(c[0]);
					String to = checkName(c[1]);
					String priority = checkPriority(c[2]);
					List<String> days;
					if(c.length == 4)
						days = checkDays(c[3]);
					else
						days = new LinkedList<String>();
					
					if (typeFormat != null && outputFormat != null)
						red.findRoute(from, to, priority, days, typeFormat, outputFormat);
					else
						System.out.println("Invalid Command: You must call outputFormat first");
				} else {
					System.out.println(
							"Invalid Command: You are inserting a different amount of parameters than the required");
				}
			} else if (command.length() > 14 && command.substring(0, 13).equals("outputFormat ")) {
				String characteristics = command.substring(13);
				String[] c = characteristics.split(" ");
				if (c.length == 2) {
					String type = checkType(c[0]);
					String output = checkOutput(c[1]);
					if (output != null && type != null) {
						typeFormat = type;
						outputFormat = output;
					} else {
						System.out.println("Invalid command: At least one parameter is not respecting the format");
					}
				} else {
					System.out.println(
							"Invalid Command: You are inserting a different amount of parameters than the required");
				}
			} else if (command.length() > 11 && command.substring(0, 10).equals("worldTrip ")) {
				String characteristics = command.substring(10);
				String[] c = characteristics.split(" ");
				if (c.length == 3) {
					String from = checkName(c[0]);
					String priority = checkPriority(c[1]);
					List<String> days = checkDays(c[2]);

					red.worldTrip(from, priority, days, typeFormat, outputFormat);
				} else {
					System.out.println(
							"Invalid Command: You are inserting a different amount of parameters than the required");
				}
			} else {
				System.out.println("Invalid Command: It does not exist");
			}
			red.print();
		}

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
		String PATTERN_AIR = "^[A-Z]{1,3}$";
		Pattern pattern = Pattern.compile(PATTERN_AIR);

		Matcher matcher = pattern.matcher(airline);

		if (matcher.matches())
			return airline;

		return null;
	}

	public static List<String> checkDays(String days) {

		String[] d;

		if (days.contains("-"))
			d = days.split("-");
		else {
			d = new String[1];
			d[0] = days;
		}

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

	public static Integer checkNumber(String number) {
		String PATTERN_NUMBER = "^[1-9][0-9]{2,3}$";
		Pattern pattern = Pattern.compile(PATTERN_NUMBER);

		Matcher matcher = pattern.matcher(number);

		if (matcher.matches())
			return Integer.parseInt(number);

		return null;
	}

	public static String checkName(String name) {
		String PATTERN_NAME = "^[A-Z]{1,3}$";
		Pattern pattern = Pattern.compile(PATTERN_NAME);

		Matcher matcher = pattern.matcher(name);

		if (matcher.matches())
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
		String PATTERN_PRICE = "^[1-9][0-9]{2}[0-9]*(.[0-9]{1,2})?$";
		Pattern pattern = Pattern.compile(PATTERN_PRICE);

		Matcher matcher = pattern.matcher(price);
		if (matcher.matches())
			return Double.parseDouble(price);
		return null;
	}

	public static FileInputStream checkFile(String file) {
		String PATTERN_FILE = "^[a-zA-Z0-9]+.txt$";
		Pattern pattern = Pattern.compile(PATTERN_FILE);

		Matcher matcher = pattern.matcher(file);
		if (matcher.matches()) {
			FileInputStream fstream;
			try {
				fstream = new FileInputStream(file);
				return fstream;
			} catch (FileNotFoundException e) {
				System.out.println("Invalid Command: The file does not exist");
			}

		}

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
		if (output.equals("stdout"))
			return output;
		else {
			String PATTERN_FILE = "^[a-zA-Z0-9]+.txt$";
			Pattern pattern = Pattern.compile(PATTERN_FILE);

			Matcher matcher = pattern.matcher(output);
			if (matcher.matches()) {
				return output.substring(0, output.indexOf('.'));
			}
		}
		return null;
	}
}
