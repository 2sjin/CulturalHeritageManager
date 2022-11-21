import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class FrameSearch extends JFrame {
	private static final long serialVersionUID = 1L;

	JTable table = new JTable(); // 테이블 객체(테이블 껍데기?)
	String header[] = { "문화재이름", "시대", "소재지/출토지", "상태" }; // 테이블 헤더
	String contents[][] = {
			{ "서울 숭례문", "조선시대", "서울 중구", "보존" },
			{ "서울 원각사지 십층석탑", "조선시대", "서울 종로구", "보존" },
			{ "서울 흥인지문", "조선시대", "서울 종로구", "보존" },
			{ "옛 보신각 동종", "조선시대", "서울 용산구", "보존" } }; // 테이블 내용

	// 테이블 모델(테이블 알맹이를 모아놓은 것?)
	DefaultTableModel tableModel = new DefaultTableModel(contents, header) {
		private static final long serialVersionUID = 1L;

		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};

	// 생성자
	public FrameSearch() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// 해당 프레임을 종료하면 프로그램 전체 종료
		setTitle("문화재 관리 프로그램");
		setSize(600, 300);

		add(new JScrollPane(table));
		table.addMouseListener(new MyMouseAdapter());
		table.setModel(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택 가능

		setVisible(true);
	}

	// 내부 클래스: 상세 페이지 생성자의 매개변수로 문화재명(기본키)을 전달함
	class MyMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int row = table.getSelectedRow();
			int column = 0;
			String chName = (String) table.getValueAt(row, column); // 문화재명

			if (e.getClickCount() >= 2) // 더블클릭이 감지되면
				new FrameDetail(chName); // 상세 페이지 생성
		}
	}
}
