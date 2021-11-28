
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		String databaseURL = "jdbc:ucanaccess://Lab 4 Database1.accdb";
		System.out.println("Welcome to Pomona Transit System");
		showMenu();
		
		try
		{
			Connection connection = DriverManager.getConnection(databaseURL);
			System.out.println("Connected to MS Access Database.");
			
			// SQL code goes here
			String sql = "";
			
			connection.close();
		} catch (SQLException e)
		{
			System.out.println("Given database file does not exist.");
		}
		
	}
	
	
	public static void showMenu() {
		
		System.out.println("------------------------- MENU -------------------------");
		System.out.println("1. Look up all trip info of a given start location name\n" + 
							"2. Edit trip schedule\n" +
							"3. Display stops of a given trip\n" + 
							"4. Display weekly schedule of a given driver and date\n" + 
							"5. Add a driver\n" + 
							"6. Add a bus\n" + 
							"7. Delete a bus\n" +
							"8. Record the actual data of a given trip offering\n");
		System.out.println("> Please enter the number of the action you would like to perfrom:");
		Scanner scan = new Scanner(System.in);
		int action = scan.nextInt();
		
	}


}
