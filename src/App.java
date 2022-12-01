

public class App {
	public static void main(String[] args) {
		DB_Conn_Query dbconquery = new DB_Conn_Query();
		dbconquery.loadDriver();
		new FrameSearch();
	}
}	