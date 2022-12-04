import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class Culture {
	int getCount1(String name, Connection con) { // 소장 개수 구하는 함수
		int num = 0;
		String query = "select 소장기관.소장개수 from 소장기관, 문화재 where 소장기관.기관명 = 문화재.소장기관기관명 and 문화재.문화재이름 = ?";
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				num = rs.getInt("소장개수");
			return num;
		} catch (SQLException e) {
			return 11;
		}
	}

	int getCount2(String name, Connection con) { // 도난 개수 구하는 함수
		int num = 0;
		String query = "select 소장기관.도난개수 from 소장기관, 문화재 where 소장기관.기관명 = 문화재.소장기관기관명 and 문화재.문화재이름 = ?";
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				num = rs.getInt("도난개수");
			return num;
		} catch (SQLException e) {
			return 22;
		}
	}

	String getName(String name, Connection con) { // 기관명 구하는 함수
		String v_name = "";
		String query = "select 소장기관.기관명 from 소장기관, 문화재 where 소장기관.기관명 = 문화재.소장기관기관명 and 문화재.문화재이름 = ?";
		try {
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
				v_name = rs.getString("기관명");
			return v_name;
		} catch (SQLException e) {
			return "Error";
		}
	}

	// 도난 처리할지(true) 보존 처리할지(false)를 결정하는 정수형 매개변수 isLost 추가함
	int transfer(String 이름, Connection con, boolean isLost) {
		// boolean 매개변수인 isLost에 따라 flag 값이 결정됨
		int signLost;		// 증감 처리를 위한 부호
		String statusAfter;	// 트랜잭션 실행 후의 상태
		if (isLost) {
			signLost = 1;	// 도난이면 1
			statusAfter = "도난";
		}
		else {
			signLost = -1;	// 보존(복구)이면 -1
			statusAfter = "보존";
		}
		
		int num1 = getCount1(이름, con); // 소장개수
		int num2 = getCount2(이름, con); // 도난개수
		System.out.println(num1);
		System.out.println(num2);
		String name = getName(이름, con); // 기관명
		System.out.println(name);

		// 원래는 소장기관내 소장개수와 도난개수를 따로 batch를 넣었는데, 그렇게 하니 처음 소장개수의 값이 변하지 않아 예상하기로는
		// 일괄처리의 batch문은 다른 테이블에서 가능한 것 같습니다.

		try {
			con.setAutoCommit(false); // commit명령어를 사용하기 위해서, autocommit을 처음 false

			Statement stmt = con.createStatement(); // statement 선언
			
			stmt.addBatch("update 문화재 set 상태 = '" + statusAfter + "' where 문화재이름 = " + "'" + 이름 + "'"); // batch 추가
			stmt.addBatch("update 소장기관 set 소장개수 = " + (num1 - signLost) + " where 기관명 = " + "'" + name + "'"); // batch 추가
			stmt.addBatch("update 소장기관 set 도난개수 = " + (num2 + signLost) + " where 기관명 = " + "'" + name + "'"); // batch 추가

			stmt.executeBatch(); // 일괄수행
			con.commit(); // 커밋
			con.setAutoCommit(true); // 오토커밋 on -> 위 명령어를 사용하기위해서는 autocommit을 처음 false, 이후 true
			System.out.println("성공");
			stmt.close();
			return 0;
		} catch (SQLException e) {
			System.out.println("Update 오류. 도난 처리 실패");
			return -1;
		}
	}
}

// 중복된 DBConn 코드는 DBConn 클래스로 이동하였음

public class Tranjection {
	public void RunTranjection(String chName, boolean isLost) throws SQLException {
		Connection con = null;

		DBConn db = new DBConn();
		con = db.connect();
		if (con == null)
			return;

		Culture B1 = new Culture();
		B1.transfer(chName, con, isLost);

		con.close();
	}
}
