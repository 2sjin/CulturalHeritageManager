import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.EmptyBorder;

import java.sql.SQLException;

public class FrameSearch extends JFrame {
	private static final long serialVersionUID = 1L;
	private int selectedRadioNum = 1;	// 선택된 라디오버튼의 번호
	private String[][] ary = null;
	private DB_Conn_Query dbconquery;
	
	private JPanel contentPane, radioPanel;
	public ButtonGroup group;
	public JRadioButton rb1, rb2, rb3, rb4;
	private JTextField textField;
	private JButton btnSearch;
	private JScrollPane scrollPane;
	private JTable table = new JTable();

	// ==============================================================================
	
	// 생성자
	public FrameSearch() {
		DB_Connect();	// UI가 생성되면서 DB에 연결함
		initFrame();
		initPanels();
		initRadioButtons();
		initSearchComponents();
		initTable();	// 테이블 외형 초기화
		search();		// 검색을 수행하여 데이터 가져옴(초기에는 공백을 검색하여 전체 데이터를 가져옴)
		refreshtable();	// 검색 결과를 바탕으로 테이블 내용 새로고침
		setVisible(true);
	}
	
	// DB에 연결하여 데이터를 가져옴
	public void DB_Connect() {

	}
	
	// Frame 초기화
	public void initFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		// 해당 프레임을 종료하면 프로그램 전체 종료
		setTitle("문화재 관리 프로그램");
		setSize(600, 400);
	}
	
	// Panel 초기화
	public void initPanels() {
		contentPane = new JPanel();
		setContentPane(contentPane);

		radioPanel = new JPanel();
		radioPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
		radioPanel.setLocation(12, 13);
		radioPanel.setSize(562, 27);
		radioPanel.setLayout(new GridLayout(0, 4, 0, 0));
		contentPane.add(radioPanel);
	}
	
	// 라디오버튼 초기화
	public void initRadioButtons() {
		group = new ButtonGroup();
		rb1 = new JRadioButton("문화재", true);
		rb2 = new JRadioButton("소장기관");
		rb3 = new JRadioButton("관리기관");
		rb4 = new JRadioButton("시대");
		group.add(rb1);
		group.add(rb2);
		group.add(rb3);
		group.add(rb4);
		radioPanel.add(rb1);
		radioPanel.add(rb2);
		radioPanel.add(rb3);
		radioPanel.add(rb4);
		rb1.addActionListener(new MyRadioListener());
		rb2.addActionListener(new MyRadioListener());
		rb3.addActionListener(new MyRadioListener());
		rb4.addActionListener(new MyRadioListener());
	}
	
	// 검색창 및 버튼 컴포넌트 초기화
	public void initSearchComponents() {
		textField = new JTextField();
		textField.setBounds(12, 50, 459, 27);
		textField.setColumns(10);
		textField.addKeyListener(new MyKeyListener());
		textField.setFocusable(true);
		textField.requestFocus();
		contentPane.add(textField);
		
		btnSearch = new JButton("검색");
		btnSearch.setBounds(483, 50, 91, 27);
		btnSearch.addKeyListener(new MyKeyListener());
		btnSearch.setFocusable(true);
		btnSearch.requestFocus();
		contentPane.add(btnSearch);
		
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				search();
			}
		});
	}
	
	// 검색 결과 테이블 초기화(테이블 외형 초기화)
	public void initTable() {
		table.addMouseListener(new MyMouseAdapter());
		table.addKeyListener(new MyKeyListener());
		contentPane.setLayout(null);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 하나만 선택 가능
		
		scrollPane = new JScrollPane(table);
		scrollPane.setBounds(12, 92, 562, 261);
		contentPane.add(scrollPane);
	}
	
	// ==============================================================================
	
	// 메소드: 검색 결과 테이블 내용 새로고침
	public void refreshtable() {
		String header[] = null;	// 테이블 헤더
		String h1[] = { "문화재이름", "소장기관", "관리기관", "시대" }; // 테이블 헤더
		String h2[] = { "소장기관명", "위치", "연락처", "도난", "소장"}; // 테이블 헤더
		String h3[] = { "관리기관명", "위치", "연락처", "훼손" }; // 테이블 헤더		
		String h4[] = { "시대명", "???", "???", "???" }; // 테이블 헤더
		String contents[][] = ary;	// 테이블 내용
		
		// 라디오버튼에 따라 헤더가 달라짐
		switch(selectedRadioNum) {
			case 1:	header = h1; break;
			case 2:	header = h2; break;
			case 3:	header = h3; break;
			case 4:	header = h4; break;
		}
		
		table.setModel(new DefaultTableModel(contents, header) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
	}
		
	// 메소드: 검색 결과를 가져와서 테이블 내용을 갱신(새로고침)함
	public void search() {
		dbconquery = new DB_Conn_Query();
		try {
			ary = dbconquery.sqlRunSearchProcedure(textField.getText());	// 저장프로시저를 실행하여 데이터 가져옴
			refreshtable();		// 테이블 내용 새로고침
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	// ==============================================================================
	
	// 내부 클래스: 더블 클릭 시(테이블)의 이벤트
	class MyMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			int row = table.getSelectedRow();
			int column = 0;
			String chName = (String) table.getValueAt(row, column); // 문화재명

			if (e.getClickCount() >= 2) // 더블클릭이 감지되면
				new FrameDetail(chName); // 상세 페이지 생성(생성자의 매개변수로 문화재이름 전달)
		}
	}
	
	// 내부 클래스: [Enter] 키 입력 시의 이벤트
	class MyKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				if (e.getSource() == table) {	// 테이블에서 [Enter] 키 입력 시
					int row = table.getSelectedRow();
					int column = 0;
					String chName = (String) table.getValueAt(row, column); // 문화재명
					new FrameDetail(chName); // 상세 페이지 생성
				}
				else search();		// 그 외의 경우 검색 수행
			}
		}
	}
	
	// ==============================================================================
	
	// 내부 클래스: 각 버튼 클릭 시의 이벤트
	class MyRadioListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (rb1.isSelected())		selectedRadioNum = 1;
			else if (rb2.isSelected())	selectedRadioNum = 2;
			else if (rb3.isSelected())	selectedRadioNum = 3;
			else if (rb4.isSelected())	selectedRadioNum = 4;
			
			search();
			refreshtable();
		}
	}
}
