
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) {
		String databaseURL = "jdbc:ucanaccess://Lab 4 Database1.accdb";
		
		try {
			Connection connection = DriverManager.getConnection(databaseURL);
			System.out.println("Connected to MS Access Database.");
			
			// SQL code goes here
			String sql = "";
			
			connection.close();
		} catch (SQLException e) {
			System.out.println("Given database file does not exist.");
		}
	}

}
