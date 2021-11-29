
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
			if(choice != 9) System.out.println("Connected to MS Access Database.\n");
				
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
		
		while(choice < 9 && choice > 0) {
			
			switch(choice) {
				case 1:
					showTripInfo(connection);
					break;
				case 2:
					
				case 3:
					
				case 4:
					
				case 5:
					
				case 6: 
					
				case 7:
					
				case 8:
					
				case 9:
					return;
				default:
					System.out.println("Invalid input. Please try again.");
			}
			
			choice = showMenu();
			
		}
		
		
	}
	
	
	@SuppressWarnings("resource")
	public static void showTripInfo(Connection connection) throws SQLException {
		
		Scanner scan = new Scanner(System.in);
		System.out.print("> Please enter the start location: ");
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
		
		
		Statement statement = connection.createStatement();
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
	}
}
