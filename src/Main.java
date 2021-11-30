
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		String databaseURL = "jdbc:ucanaccess://Lab 4 Database1.accdb";
		
		// Print Menu
		System.out.println("Welcome to Pomona Transit System");
		int choice = showMenu();
		
		
		// Connect to MS Access Database
		Connection connection;
		try {
			connection = DriverManager.getConnection(databaseURL);
			if(choice != 9) System.out.println("Connected to MS Access Database.");
				
			// Perform action according to user input
			optionSwitch(choice, connection);
				
			connection.close();
				
		} catch (SQLException e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
		System.out.println("Thank you for using Pomona Transit System.");		
	}
	
	
	@SuppressWarnings("resource")
	public static int showMenu() {
		
		System.out.println("\n------------------------- MENU -------------------------");
		System.out.println("1. Look up trip info\n"
							+ "2. Edit trip schedule\n"
							+ "3. Display stops of a given trip\n"
							+ "4. Display weekly schedule of a given driver and date\n"
							+ "5. Add a driver\n"
							+ "6. Add a bus\n"
							+ "7. Delete a bus\n"
							+ "8. Record the actual data of a given trip offering\n"
							+ "9. Quit\n");
		
		System.out.print("> Please enter the number of the action you would like to perfrom: ");
		Scanner scan = new Scanner(System.in);
		int choice = scan.nextInt();
		
		return choice;
		
	}


	@SuppressWarnings("resource")
	public static void optionSwitch(int choice, Connection connection) throws SQLException {
		
		Scanner scan = new Scanner(System.in);
		while (choice > 9 || choice <= 0 ) {
			System.out.println("Invalid input. Please try again:");
			choice = scan.nextInt();
		}
		
		while(choice < 9 && choice > 0) {
			
			switch(choice) {
				case 1:
					showTripInfo(connection);
					break;
				case 2:
					editTripOffering(connection);
					break;
				case 3:
					showStop(connection);
					break;
				case 4:
					showDriverWeeklySchedule(connection);
					break;
				case 5:
					addADriver(connection);
					break;
				case 6: 
					addABus(connection);
					break;
				case 7:
					deleteABus(connection);
					break;
				case 8:
					recordActualTrip(connection);
					break;
				case 9:
					return;
				default:
					System.out.println("Invalid input. Please try again.");
			}
			
			choice = showMenu();
			
		}
		
		
	}
	
	
	@SuppressWarnings("resource")
	public static void showTripInfo(Connection connection) {
		
		Scanner scan = new Scanner(System.in);
		System.out.print("\n> Please enter the start location: ");
		String startLocation = scan.nextLine();
		System.out.print("> Please enter the destination: ");
		String destination = scan.nextLine();
		System.out.print("> Please enter the date: ");
		String date = scan.nextLine();
		
		
		String sql = "SELECT StartLocationName, DestinationName, Tripdate, "
				+ "ScheduledStartTime, ScheduledArrivalTime, DriverName, BusID "
				+ "FROM Trip, TripOffering "
				+ "WHERE Trip.TripNumber = TripOffering.TripNumber "
				+ "AND StartLocationName = \"" + startLocation + "\" "
				+ "AND DestinationName = \"" + destination + "\" "
				+ "AND Tripdate = \"" + date + "\"";
		
//		System.out.println(sql);
		
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			System.out.println("\n--------------- Schedule on " + date + " ---------------");
			System.out.println(String.format("%-15s %-15s %-15s %-10s", "Start Time", "Arrival Time", "Driver Name", "Bus ID"));
		
			while(result.next()) {
				String startTime = result.getString("ScheduledStartTime");
				String arrivalTime = result.getString("ScheduledArrivalTime");
				String driver = result.getString("DriverName");
				int busID = result.getInt("BusID");
				
				System.out.println(String.format("%-15s %-15s %-15s %-10s",
								startTime, arrivalTime, driver, busID));
			}
		
			statement.close();
		} catch (SQLException e) {
			System.out.println("Schedule searching error.");
		}
		
	}
	
	
	@SuppressWarnings("resource")
	public static void editTripOffering(Connection connection) throws SQLException {
		
		Scanner scan = new Scanner(System.in);
		boolean keepEditing = true;
		
		showTripOffering(connection);
		
		while (keepEditing) {
			System.out.print("\nNow do you want to ...?\n"
					+ "\t(1) Delete a trip offering\n"
					+ "\t(2) Add trips\n"
					+ "\t(3) Change driver for a trip\n"
					+ "\t(4) Change bus for a trip\n"
					+ "\t(5) Show all trips offering\n"
					+ "\t(6) Go back to main menu\n> ");
			int choice = scan.nextInt();
			switch(choice) {
				case 1:
					deleteTrip(connection);
					break;
				case 2:
					addTrips(connection);
					break;
				case 3:
					changeDriver(connection);
					break;
				case 4:
					changeBus(connection);
					break;
				case 5:
					showTripOffering(connection);
					break;
				case 6:
					return;
				default:
					System.out.println("Invalid input. Please try again.");
			}			
		}
		
	}
	
	public static void showTripOffering(Connection connection) {
		
		String sql = "SELECT * FROM TripOffering";
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			
			System.out.println("\n----------------------------------- Trip Offering -----------------------------------");
			System.out.println(String.format("%-15s %-15s %-15s %-15s %-15s %-15s", "Trip Number", "Date", "Start Time", "Arrival Time", "Driver Name", "Bus ID"));
			while(result.next()) {
				String tripNum = result.getString("TripNumber");
				String date = result.getString("TripDate");
				String startTime = result.getString("ScheduledStartTime");
				String arrivalTime = result.getString("ScheduledArrivalTime");
				String driver = result.getString("DriverName");
				int bus = result.getInt("BusID");
				
				System.out.println(String.format("%-15s %-15s %-15s %-15s %-15s %-15s", tripNum, date, startTime, arrivalTime, driver, bus));
				
			}
		} catch (SQLException e){
			System.out.println("Unable to show trip offering info.");
		}
	}
	
	@SuppressWarnings("resource")
	public static void deleteTrip(Connection connection) {
				
		Scanner scan = new Scanner(System.in);
		System.out.print("\n> Please enter the trip number you want to delete: ");
		String tripNum = scan.nextLine();
		System.out.print("> Please enter the date: ");
		String date = scan.nextLine();
		System.out.print("> Please enter the start time: ");
		String startTime = scan.nextLine();
		
		String sql_delete = "DELETE * FROM TripOffering "
				+ "WHERE TripNumber = \"" + tripNum + "\" "
				+ "AND TripDate = \"" + date + "\" "
				+ "AND ScheduledStartTime = \"" + startTime + "\" ";
		
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql_delete);
			statement.execute(sql_delete);
		} catch (SQLException e) {
			System.out.println("Record deletion unsuccessful.");
		}
		
		System.out.println("Record deleted.");
	}
	
	
	@SuppressWarnings("resource")
	public static void addTrips(Connection connection) {
		
		Scanner scan = new Scanner(System.in);
		System.out.print("> Please enter the trip number you want to add: ");
		String tripNum = scan.nextLine();
		System.out.print("> Please enter the date: ");
		String date = scan.nextLine();
		System.out.print("> Please enter the start time: ");
		String startTime = scan.nextLine();
		System.out.print("> Please enter the arrival time: ");
		String arrivalTime = scan.nextLine();
		System.out.print("> Please enter the driver name: ");
		String driver = scan.nextLine();
		System.out.print("> Please enter the bus ID: ");
		int bus = scan.nextInt();
		
		String sql = "INSERT INTO TripOffering VALUES (\"" + tripNum + "\", "
				+ "\"" + date + "\", "
				+ "\"" + startTime + "\", "
				+ "\"" + arrivalTime + "\", "
				+ "\"" + driver + "\", "
				+ bus + ")";
		
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			statement.execute(sql);
		} catch (SQLException e) {
			// Exception is thrown even when data are inserted successfully
		}
		
	}
	
	public static void showDriver(Connection connection) {
		
		String sql = "SELECT * FROM Driver";
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			
			System.out.println("\n------- Driver List -------");
			while(result.next()) {
				String name = result.getString("DriverName");
				System.out.println(" - " + name);
			}
			
		} catch (SQLException e){
			System.out.println("Unable to show diver info.");
		}
		
	}
	
	
	@SuppressWarnings("resource")
	public static void changeDriver(Connection connection) {
		
		Scanner scan = new Scanner(System.in);
		
		showDriver(connection);
		System.out.println("\nPlease specify which trip offering you want to edit ");
		System.out.print("> Please enter the trip number: ");
		String tripNum = scan.nextLine();
		System.out.print("> Please enter the date: ");
		String date = scan.nextLine();
		System.out.print("> Please enter the start time: ");
		String startTime = scan.nextLine();
		
		System.out.print("> Now enter the new dirver name: ");
		String name = scan.nextLine();
		
		String sql = "UPDATE TripOffering "
				+ "SET DriverName = \"" + name + "\" "
				+ "WHERE TripNumber = \"" + tripNum + "\" "
				+ "AND TripDate = \"" + date + "\" "
				+ "AND ScheduledStartTime = \"" + startTime + "\" ";
		
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			statement.execute(sql);
			
			System.out.println("Driver updated.");
			
		} catch (SQLException e) {
			System.out.println("Driver update unsuccessful");
		}
		
		showTripOffering(connection);
		
	}
	
	
	@SuppressWarnings("resource")
	public static void changeBus(Connection connection) {
		
		Scanner scan = new Scanner(System.in);
		
		System.out.println("\nPlease specify which trip offering you want to edit ");
		System.out.print("> Please enter the trip number: ");
		String tripNum = scan.nextLine();
		System.out.print("> Please enter the date: ");
		String date = scan.nextLine();
		System.out.print("> Please enter the start time: ");
		String startTime = scan.nextLine();
		
		System.out.print("> Now enter the new bus ID: ");
		int bus = scan.nextInt();
		
		String sql = "UPDATE TripOffering "
				+ "SET BusID = \"" + bus + "\" "
				+ "WHERE TripNumber = \"" + tripNum + "\" "
				+ "AND TripDate = \"" + date + "\" "
				+ "AND ScheduledStartTime = \"" + startTime + "\" ";
		
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			statement.execute(sql);
			
			System.out.println("Bus ID updated.");
			
		} catch (SQLException e) {
			System.out.println("Bus ID update unsuccessful");
		}
		
		showTripOffering(connection);
	}
	
	
	@SuppressWarnings("resource")
	public static void showStop(Connection connection) {
		
		Scanner scan = new Scanner(System.in);
		System.out.print("\nPlease enter the trip number: ");
		String tripNum = scan.nextLine();
		
		String sql = "SELECT TS.TripNumber, S.StopNumber, S.StopAddress "
				+ "FROM Stop AS S, TripStopInfo AS TS, Trip AS T "
				+ "WHERE T.TripNumber = TS.TripNumber "
				+ "AND S.StopNumber = TS.StopNumber "
				+ "AND T.TripNumber = \"" + tripNum + "\" ";
		
		System.out.println("\n--------------- Stops in Trip " + tripNum + " ---------------");
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			
			while(result.next()) {
				int stopNum = result.getInt("StopNumber");
				String stopAddr = result.getString("StopAddress");
				
				System.out.println(stopNum + " " + stopAddr);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
			
	}
	
	
	@SuppressWarnings("resource")
	public static void showDriverWeeklySchedule(Connection connection) {
		Scanner scan = new Scanner(System.in);
		System.out.print("\n> Please enter the name of the driver: ");
		String driverName = scan.nextLine();
		System.out.print("> Please enter the date: ");
		String driverDate = scan.nextLine();
		
		String sql = "SELECT DISTINCT T1.DriverName, T1.TripNumber, T1.TripDate, "
				+ "T1.ScheduledStartTime, T1.ScheduledArrivalTime, T1.BusID "
				+ "FROM TripOffering AS T1 "
				+ "WHERE T1.DriverName = \"" + driverName + "\" "
				+ "AND T1.TripDate >= \"" + driverDate + "\" ";
		
//		System.out.println(sql);
		
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			
			System.out.println("\n----------------------------------- Trip Offering -----------------------------------");
			System.out.println(String.format("%-15s %-15s %-15s %-15s %-15s %-15s", "Date", "Driver Name", "Trip Number", "Start Time", "Arrival Time", "Bus ID"));
			while(result.next()) {
				String tripNum = result.getString("TripNumber");
				String date = result.getString("TripDate");
				String startTime = result.getString("ScheduledStartTime");
				String arrivalTime = result.getString("ScheduledArrivalTime");
				String driver = result.getString("DriverName");
				int bus = result.getInt("BusID");
				
				System.out.println(String.format("%-15s %-15s %-15s %-15s %-15s %-15s", date,driver, tripNum, startTime, arrivalTime, bus));
				
			}
		} catch (SQLException e){
			System.out.println("Schedule searching error.");
		}
	}

	@SuppressWarnings("resource")
	public static void addADriver(Connection connection) {
		Scanner scan = new Scanner(System.in);
		System.out.print("> Please enter the name of the new bus driver: ");
		String newDriverName = scan.nextLine();
		System.out.print("> Please enter their phone number: ");
		String newDriverPhoneNumber = scan.nextLine();
		
		String sql = "INSERT INTO Driver VALUES (\"" + newDriverName + "\", "
				+ "\"" + newDriverPhoneNumber + "\")";
		
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			statement.execute(sql);
		} catch (SQLException e) {
			// Exception is thrown even when data are inserted successfully
		}
		
		showDriver(connection);
		
	}
	
	public static void showBus(Connection connection) {
		
		String sql = "SELECT * FROM Bus";
		
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			
			System.out.println("\n-------------- Bus List --------------");
			System.out.println(String.format("%-5s %-15s %-15s", "BusID", "Model", "Year"));
			while(result.next()) {
				String busID = result.getString("BusID");
				String model = result.getString("Model");
				int year = result.getInt("Year");
				
				System.out.println(String.format("%-5s %-15s %-15s", busID, model, year));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@SuppressWarnings("resource")
	public static void addABus(Connection connection) {
		
		Scanner scan = new Scanner(System.in);
		System.out.print("> Please enter the bus ID: ");
		int bus = scan.nextInt();
		scan.nextLine();
		System.out.print("> Please enter the bus Model: ");
		String model = scan.nextLine();
		System.out.print("> Please enter the bus Year: ");
		int year = scan.nextInt();
		
		String sql = "INSERT INTO Bus VALUES (" + bus + ", "
				+ "\"" + model + "\", "
				+ year + ")";
		
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			statement.execute(sql);
		} catch (SQLException e) {
			// Exception is thrown even when data are inserted successfully
		}
		
		showBus(connection);
		
	}
	
	
	@SuppressWarnings("resource")
	public static void deleteABus(Connection connection) {
		
		Scanner scan = new Scanner(System.in);
		System.out.print("> Please enter the bus ID: ");
		int bus = scan.nextInt();
		
		String sql = "DELETE * FROM Bus "
				+ "WHERE BusID = " + bus;
		
		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			statement.execute(sql);
		} catch (SQLException e) {
			// Exception is thrown even when data are inserted successfully
		}
		
		showBus(connection);
		
	}
	
	
	@SuppressWarnings("resource")
	public static void recordActualTrip(Connection connection) {
		
		Scanner scan = new Scanner(System.in);
		System.out.print("> Please enter the Trip Number: ");
		String tripNum = scan.nextLine();
		System.out.print("> Please enter the Date: ");
		String date = scan.nextLine();
		System.out.print("> Please enter the Scheduled Start Time: ");
		String scheduledStartTime = scan.nextLine();
		System.out.print("> Please enter the Stop Number: ");
		int stopNum = scan.nextInt();
		scan.nextLine();
		System.out.print("> Please enter Scheduled Arrival Time: ");
		String scheduledArrivalTime = scan.nextLine();
		System.out.print("> Please enter the Actual Start Time: ");
		String actualStartTime = scan.nextLine();
		System.out.print("> Please enter the Actual Arrival Time: ");
		String actualArrivalTime = scan.nextLine();
		System.out.print("> Please enter the Number of Passenger In: ");
		int numOfPassengerIn = scan.nextInt();
		scan.nextLine();
		System.out.print("> Please enter the Number of Passenger Out:  ");
		int numOfPassengerOut = scan.nextInt();
		
		String sql = "INSERT INTO ActualTripStopInfo VALUES (\"" + tripNum + "\", "
				+ "\"" + date + "\", "
				+ "\"" + scheduledStartTime + "\", "
				+ stopNum + ", "
				+ "\"" + scheduledArrivalTime + "\", "
				+ "\"" + actualStartTime + "\", "
				+ "\"" + actualArrivalTime + "\", "
				+ numOfPassengerIn + ", "
				+ numOfPassengerOut + ")";

		Statement statement;
		try {
			statement = connection.createStatement();
			statement.executeUpdate(sql);
			statement.execute(sql);
		} catch (SQLException e) {
			// Exception is thrown even when data are inserted successfully
		}
		
		showActualTrip(connection);
		
	}
	
	
	public static void showActualTrip(Connection connection) {
		
		String sql = "SELECT * FROM ActualTripStopInfo";
		
		Statement statement;
		try {
			statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);
			
			System.out.println("\n---------------------------- Actual Trip Info ----------------------------");
			
			while(result.next()) {
				String tripNum = result.getString("TripNumber");
				String date = result.getString("TripDate");
				String sStart = result.getString("ScheduledStartTime");
				int stopNum = result.getInt("StopNumber");
				String sArrival = result.getString("ScheduledArrivalTime");
				String aStart = result.getString("ActualStartTime");
				String aArrival = result.getString("ActualArrivalTime");
				int in = result.getInt("NumberOfPassengerIn");
				int out = result.getInt("NumberOfPassengerOut");
				
				System.out.println(String.format("%-8s %-15s %-10s %-8s %-10s %-10s %-10s %-5s %-5s",
						tripNum, date, sStart, stopNum, sArrival, aStart, aArrival, in, out));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
