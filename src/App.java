

public class App {
	public static void main(String[] args) {
		DB_Conn_Query dbconquery = new DB_Conn_Query();
		dbconquery.loadDriver();
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
