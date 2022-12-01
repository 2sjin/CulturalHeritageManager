import java.sql.*;
import java.util.ArrayList;

import oracle.jdbc.OracleTypes;

public class DB_Conn_Query {
	Connection con = null;
	String url = "jdbc:oracle:thin:@localhost:1521:XE"; String id = "heritage"; String password = "1234";

	// 드라이버 적재
	public void loadDriver() {
	try { Class.forName("oracle.jdbc.driver.OracleDriver");
		System.out.println("드라이버 적재 성공");
	} catch (ClassNotFoundException e) {
		System.out.println("No Driver."); }
	}
	
	// DB 연결(SQL 실행하기 전에 호출됨)
	private void DB_Connect() {
		try {
			con = DriverManager.getConnection(url, id, password);
			System.out.println("DB 연결 성공");
		} catch (SQLException e) {
			System.out.println("Connection Fail");
		}
	}

	// SQL 실행: 단순 검색(전체 검색)
	public String[][] sqlRun(int rbNum) throws SQLException {
		ArrayList<String[]> tempRowArrayList = new ArrayList<>();
		String[] tempRow = null;

		int dbColumnCount = 0;
		String dbColumns = "";		// SELECT에 사용할 속성들
		String dbTableName = "";	// SELECT에 사용할 테이블명

		// 선택된 라디오버튼에 따라 테이블 및 속성 결정
		switch(rbNum) {
			case 1:
				dbColumnCount = 5;
				dbColumns = "문화재이름, 소장기관기관명, 관리단체기관명, 시대시대명, 상태";
				dbTableName = "문화재";
				break;
			case 2:
				dbColumnCount = 5;
				dbColumns = "기관명, 기관위치, 연락처, 도난개수, 소장개수";
				dbTableName = "소장기관";
				break;
			case 3:
				dbColumnCount = 4;
				dbColumns = "기관명, 기관위치, 연락처, 훼손개수";
				dbTableName = "관리단체";
				break;
		}
		tempRow = new String[dbColumnCount];
		
		String query = "select " + dbColumns + " from " + dbTableName;
		try {
			DB_Connect();
			Statement stmt = con.createStatement(); 
			ResultSet rs = stmt.executeQuery(query);
			
			// 각 행을 배열 형태로 저장한 뒤, 배열을 ArrayList에 저장함 (2차원 구조)
			while (rs.next()) {
				for (int i=0; i<tempRow.length; i++)
					tempRow[i] = rs.getString(i+1);
				tempRowArrayList.add(tempRow.clone());	// clone() 메소드가 없으면 깊은 복사가 되지 않음
			}
			
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { con.close(); }
		
		return arrayListTo2DArray(tempRowArrayList);
	}

	// SQL 실행: 상세정보 조회
	public String[] sqlRunDetail(String chName) throws SQLException {
		String[] rsArray = new String[29];		// SELECT 결과를 리턴하기 위한 배열
		String query = "select * from 문화재, 관리단체, 소장기관, 박물관규정, 시대 "
				+ "where 문화재.문화재이름 = ? "
				+ "and 문화재.관리단체기관명 = 관리단체.기관명 "
				+ "and 문화재.소장기관기관명 = 소장기관.기관명 "
				+ "and 소장기관.박물관규정 = 박물관규정.종류ID "
				+ "and 문화재.시대시대명 = 시대.시대명";
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
	
	// SQL 실행: 문화재 검색을 위한 저장프로시저 호출
	public String[][] sqlRunSearchProcedure(String keyword) throws SQLException {
		ArrayList<String[]> tempRowArrayList = new ArrayList<>();
		String[] tempRow = new String[5];

		try {
			DB_Connect();
			CallableStatement cstmt = con.prepareCall(" {call SP_문화재검색(?, ?)}");
			cstmt.setString(1, keyword);
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			cstmt.executeQuery();
			ResultSet rs = (ResultSet) cstmt.getObject(2);	// 저장프로시저의 커서를 리턴받음
			
			// 각 행을 배열 형태로 저장한 뒤, 배열을 ArrayList에 저장함 (2차원 구조)
			while (rs.next()) {
				for (int i=0; i<tempRow.length; i++)
					tempRow[i] = rs.getString(i+1);
				tempRowArrayList.add(tempRow.clone());	// clone() 메소드가 없으면 깊은 복사가 되지 않음
			}

			cstmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { con.close(); }
		
		return arrayListTo2DArray(tempRowArrayList);
	}
	
	// SQL 실행: 트리거를 실행하기 위한 UPDATE문
	public void sqlRunTrigger(String chName, String beforeStatus, String afterStatus) throws SQLException {
		String query = "UPDATE 문화재 SET 상태 = ? WHERE 상태 = ? and 문화재이름 = ?";
		try {
			DB_Connect();
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, afterStatus);
			pstmt.setString(2, beforeStatus);
			pstmt.setString(3, chName);
			pstmt.executeUpdate();
			pstmt.close(); 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally { con.close(); }
	}
	
	// 배열 ArrayList를 2차원 배열로 변환함(DefaultTableModel에서 사용하기 위함)
	public String[][] arrayListTo2DArray(ArrayList<String[]> aryList) {
		String array2D[][] = null;
		array2D = new String[aryList.size()][4];
		for (int i=0; i<array2D.length; i++) {
			array2D[i] = aryList.get(i);
		}
		return array2D;
	}
}
