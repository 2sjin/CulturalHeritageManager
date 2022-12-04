
public class App {
	public static void main(String[] args) {
		DBConn dbconquery = new DBConn();
		dbconquery.loadDriver();
		new FrameSearch();
	}
}