
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
					break;
				case 4:
					break;
				case 5:
					break;
				case 6: 
					break;
				case 7:
					break;
				case 8:
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
				+ "WHERE StartLocationName = \"" + startLocation + "\" "
				+ "AND DestinationName = \"" + destination + "\" "
				+ "AND Tripdate = \"" + date + "\"";
		
		
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
					break;
				case 4:
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
}
