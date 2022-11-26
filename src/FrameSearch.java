import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.EmptyBorder;

import java.sql.SQLException;

public class FrameSearch extends JFrame {
	private static final long serialVersionUID = 1L;
	private String[][] ary = null;
	private DB_Conn_Query dbconquery;
	
	private JPanel contentPane, radioPanel;
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
		initTable();
		search();
		refreshtable();
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
		ButtonGroup group = new ButtonGroup();
		JRadioButton rb1 = new JRadioButton("문화재", true);
		JRadioButton rb2 = new JRadioButton("소장기관");
		JRadioButton rb3 = new JRadioButton("관리기관");
		JRadioButton rb4 = new JRadioButton("시대");
		group.add(rb1);
		group.add(rb2);
		group.add(rb3);
		group.add(rb4);
		radioPanel.add(rb1);
		radioPanel.add(rb2);
		radioPanel.add(rb3);
		radioPanel.add(rb4);
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
	
	// 검색 결과 테이블 내용 새로고침
	public void refreshtable() {
		String header[] = { "문화재이름", "소장기관", "관리기관", "시대" }; // 테이블 헤더
		String contents[][] = ary;	// 테이블 내용
		table.setModel(new DefaultTableModel(contents, header) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
	}
	
	// ==============================================================================
	
	// 메소드: 검색 결과를 테이블에 반환
	public void search() {
		System.out.println("****** 검색 명령 수행 ******");
		dbconquery = new DB_Conn_Query();
		try {
			ary = dbconquery.sqlRunSearchProcedure(textField.getText());
			refreshtable();
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
}
