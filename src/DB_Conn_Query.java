import java.sql.*;

public class DB_Conn_Query {
	Connection con = null;
	String url = "jdbc:oracle:thin:@localhost:1521:XE"; String id = "heritage"; String password = "1234";

	public DB_Conn_Query() {
	try { Class.forName("oracle.jdbc.driver.OracleDriver");
		System.out.println("드라이버 적재 성공");
	} catch (ClassNotFoundException e) {
		System.out.println("No Driver."); }
	}

	private void DB_Connect() {
		try {
			con = DriverManager.getConnection(url, id, password);
			System.out.println("DB 연결 성공");
		} catch (SQLException e) {
			System.out.println("Connection Fail");
		}
	}

	public void sqlRun() throws SQLException {	 // 단순 검색
		String keyword1 = "문화재"; //이부분이 라디오 버튼으로 들어갈 것 같습니다.
		String query = "select * from" + keyword1;
		try { DB_Connect();
			Statement stmt = con.createStatement(); 
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				System.out.print(rs.getString("문화재이름")+"\t"+rs.getString("시대")+"\t"+rs.getString("소재지및출토지")+"\t"+rs.getString("상태")); //이부분은 제생각엔 인덱스로 들어갈 것 같습니다.-- DB로 실험을 하지 못해 그대로 둠
			}
			stmt.close(); 
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { con.close(); }
	}

	public String[] sqlRun1(String chName) throws SQLException {	 // 상세정보 조회
		String[] rsArray = new String[26];		// SELECT 결과를 리턴하기 위한 배열
		String query = "select * from 문화재, 관리기관, 소장기관, 박물관규정 "
				+ "where 문화재.문화재이름 = ? "
				+ "and 문화재.관리단체기관명 = 관리기관.기관명 "
				+ "and 문화재.소장기관기관명 = 소장기관.기관명 "
				+ "and 소장기관.박물관규정 = 박물관규정.종류ID";
		try {
			DB_Connect();
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, chName);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				for (int i=0; i<rsArray.length; i++) {				
					rsArray[i] = rs.getString(i+1);
				}
			}
			pstmt.close(); 
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { con.close(); }
		return rsArray;
	}
}
