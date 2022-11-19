import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class FrameSearch extends JFrame {
	private static final long serialVersionUID = 1L;

	JTable table = new JTable();	// 테이블 객체(테이블 껍데기?)
	String header[] = {"이름", "값A", "값B", "값C"};		// 테이블 헤더
	String contents[][] = { {"서울 숭례문", "10", "20", "30"},
							{"서울 원각사지 십층석탑", "40", "50", "60"} };	// 테이블 내용
	
	// 테이블 모델(테이블 알맹이를 모아놓은 것?)
	DefaultTableModel tableModel = new DefaultTableModel(contents, header) {
		private static final long serialVersionUID = 1L;
		public boolean isCellEditable(int row, int column) {
			return false;
		}
	};
	
	public FrameSearch() {
		setTitle("문화재 관리 프로그램");
		setSize(600, 300);

		add(new JScrollPane(table));
		table.addMouseListener(new MyMouseAdapter());
		table.setModel(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);	// 하나만 선택 가능
		
		setVisible(true);
	}

	// 내부 클래스: 상세 페이지 생성자의 매개변수로 문화재명(기본키)을 전달함
	class MyMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int row = table.getSelectedRow();
			int column = 0;
			String chName = (String) table.getValueAt(row, column);	// 문화재명
			
			if (e.getClickCount() >= 2)		// 더블클릭이 감지되면
				new FrameDetail(chName);	// 상세 페이지 생성
		}
	}
}
