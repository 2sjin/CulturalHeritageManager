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
	private JLabel lblNewLabel;
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
		setSize(700, 500);
		setResizable(false);	// 프레임 크기 조정 불가능
	}
	
	// Panel 초기화
	public void initPanels() {
		contentPane = new JPanel();
		setContentPane(contentPane);

		radioPanel = new JPanel();
		radioPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
		radioPanel.setLocation(12, 13);
		radioPanel.setSize(662, 27);
		radioPanel.setLayout(new GridLayout(0, 3, 0, 0));
		contentPane.add(radioPanel);
	}
	
	// 라디오버튼 초기화
	public void initRadioButtons() {
		group = new ButtonGroup();
		rb1 = new JRadioButton("문화재(검색 결과)", true);
		rb2 = new JRadioButton("전체 소장기관 조회");
		rb3 = new JRadioButton("전체 관리기관 조회");
		group.add(rb1);
		group.add(rb2);
		group.add(rb3);
		radioPanel.add(rb1);
		radioPanel.add(rb2);
		radioPanel.add(rb3);
		rb1.addActionListener(new MyRadioListener());
		rb2.addActionListener(new MyRadioListener());
		rb3.addActionListener(new MyRadioListener());
	}
	
	// 검색창 및 버튼 컴포넌트 초기화
	public void initSearchComponents() {
		lblNewLabel = new JLabel("문화재 검색하기(문화재이름/소장기관/관리기관/시대) : 전체 문화재 조회는 공백 검색");
		lblNewLabel.setBounds(12, 59, 541, 29);
		contentPane.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(12, 88, 541, 27);
		textField.setColumns(10);
		textField.addKeyListener(new MyKeyListener());
		textField.setFocusable(true);
		textField.requestFocus();
		contentPane.add(textField);
		
		btnSearch = new JButton("문화재 검색");
		btnSearch.setBounds(564, 88, 110, 27);
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
		scrollPane.setBounds(12, 136, 662, 317);
		contentPane.add(scrollPane);
	}
	
	// ==============================================================================
	
	// 메소드: 검색 결과 테이블 내용 새로고침
	public void refreshtable() {
		String[] header = null;	// 테이블 헤더
		int[] width = null;
		
		String h1[] = { "문화재이름", "소장기관", "관리기관", "시대", "상태"}; // 테이블 헤더
		String h2[] = { "소장기관명", "위치", "연락처", "도난", "소장"}; // 테이블 헤더
		String h3[] = { "관리기관명", "위치", "연락처", "훼손" }; // 테이블 헤더		

		int width1[] = {100, 80, 100, 30, 15};
		int width2[] = {90, 180, 90, 15, 15};
		int width3[] = {90, 180, 90, 15};
		
		String contents[][] = ary;	// 테이블 내용
		
		// 라디오버튼에 따라 헤더가 달라짐
		switch(selectedRadioNum) {
			case 1:	header = h1; width = width1; break;
			case 2:	header = h2; width = width2; break;
			case 3:	header = h3; width = width3; break;
		}
		
		table.setModel(new DefaultTableModel(contents, header) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		});
		
		// 열의 폭 조정
		for (int i=0; i<header.length; i++)
			table.getColumnModel().getColumn(i).setPreferredWidth(width[i]);
	}
		
	// 메소드: 검색 결과를 가져와서 테이블 내용을 갱신(새로고침)함
	public void search() {
		dbconquery = new DB_Conn_Query();
		try {
			ary = dbconquery.sqlRunSearchProcedure(textField.getText());	// 저장프로시저를 실행하여 데이터 가져옴
			refreshtable();		// 테이블 내용 새로고침
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			rb1.setSelected(true);
		}
	}
	
	// 테이블 더블클릭 또는 [Enter] 키 클릭 시 실행할 명렁
	public void tableEnterCommand() {
		int row = table.getSelectedRow();
		int column = 0;
		String chName = (String) table.getValueAt(row, column); // 문화재명
		if (selectedRadioNum == 1)	// '문화재' 라디오버튼이 선택된 경우
			new FrameDetail(chName); // 상세 페이지 생성(생성자의 매개변수로 문화재이름 전달)
	}
	
	// ==============================================================================
	
	// 내부 클래스: 더블 클릭 시(테이블)의 이벤트
	class MyMouseAdapter extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() >= 2) { // 더블클릭이 감지되었을 때
				tableEnterCommand();
			}
		}
	}
	
	// 내부 클래스: [Enter] 키 입력 시의 이벤트
	class MyKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
				if (e.getSource() == table) {	// 테이블에서 [Enter] 키 입력 시
					tableEnterCommand();
				}
				else search();		// 그 외의 경우 검색 수행
			}
		}
	}
	
	// ==============================================================================
	
	// 내부 클래스: 라디오버튼이 선택될 때마다 발생하는 이벤트
	class MyRadioListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (rb1.isSelected())		selectedRadioNum = 1;
			else if (rb2.isSelected())	selectedRadioNum = 2;
			else if (rb3.isSelected())	selectedRadioNum = 3;
			try {
				ary = dbconquery.sqlRun(selectedRadioNum);
				refreshtable();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}
}
