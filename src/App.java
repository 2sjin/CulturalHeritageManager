import java.sql.SQLException;

public class App {
	public static void main(String[] args) {
		DB_Conn_Query dbconquery = new DB_Conn_Query();
		/*
		try {
			dbconquery.sqlRun();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		*/
		FrameSearch frame = new FrameSearch();
	}
}
