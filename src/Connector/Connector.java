package Connector;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connector {
	public static Connection getConnection() {
		Connection conn;
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/prueba?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
			return conn;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getConn() {
		String conn;
		try {
			conn ="jdbc:mysql://localhost/pruebas?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
			return conn;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
