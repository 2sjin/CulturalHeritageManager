import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

import java.sql.SQLException;

public class FrameDetail extends JFrame {
	private static final long serialVersionUID = 1L;
	private DBConn dbconquery;
	Tranjection tranjection;
	private String[] arr = new String[33];

	private String chName;

	private JPanel contentPane;
	private JPanel panelNorth, panelCenter, panelSouth;
	private JTable tableOfOverview, tableOfManager, tableOfCollector;

	// ==============================================================================

	// 생성자
	public FrameDetail(String chName) {
		tranjection = new Tranjection(); // 트랜잭션 수행을 위한 객체 생성
		this.chName = chName;
		DB_Connect(); // 상세정보 창이 열릴 때 DB에 연결함
		initFrame();
		initPanels();
		initLabels();
		initTables();
		refreshAllTables();
		initTextAreas();
		initButtons();
		setVisible(true);
	}

	// DB에 연결하여 데이터를 가져옴
	public void DB_Connect() {
		dbconquery = new DBConn();
		try {
			arr = dbconquery.sqlRunDetail(chName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Frame 초기화
	public void initFrame() {
		setTitle(chName);
		setBounds(100, 100, 1000, 600);
		setResizable(false); // 프레임 크기 조정 불가능
	}

	// Panel 초기화
	public void initPanels() {
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		panelNorth = new JPanel();
		panelNorth.setBackground(new Color(135, 206, 235));
		panelNorth.setBorder(new EmptyBorder(5, 0, 10, 0));
		contentPane.add(panelNorth, BorderLayout.NORTH);

		panelCenter = new JPanel();
		panelCenter.setSize(310, 360);
		panelCenter.setLayout(null);
		contentPane.add(panelCenter, BorderLayout.CENTER);

		panelSouth = new JPanel();
		panelSouth.setBorder(new EmptyBorder(5, 0, 15, 0));
		contentPane.add(panelSouth, BorderLayout.SOUTH);
	}

	// 레이블 초기화
	public void initLabels() {
		JLabel lblName = new JLabel(chName);
		lblName.setFont(new Font("맑은 고딕", Font.BOLD, 24));
		panelNorth.add(lblName);

		JLabel caption1 = new JLabel("개요");
		caption1.setBounds(12, 10, 275, 15);
		panelCenter.add(caption1);

		JLabel caption2 = new JLabel("관리단체 정보");
		caption2.setBounds(12, 138, 275, 15);
		panelCenter.add(caption2);

		JLabel caption3 = new JLabel("소장기관 정보");
		caption3.setBounds(506, 58, 292, 15);
		panelCenter.add(caption3);

		JLabel caption4 = new JLabel("문화재 분류");
		caption4.setBounds(506, 10, 87, 15);
		panelCenter.add(caption4);

		JLabel labelDesc1 = new JLabel("문화재 설명");
		labelDesc1.setBounds(12, 253, 292, 15);
		panelCenter.add(labelDesc1);

		JLabel labelDesc2 = new JLabel("박물관 규정");
		labelDesc2.setBounds(506, 253, 292, 15);
		panelCenter.add(labelDesc2);
	}

	// 테이블 초기화
	public void initTables() {
		tableOfOverview = new JTable();
		tableOfOverview.setEnabled(false);
		tableOfOverview.setBounds(12, 35, 468, 78);
		panelCenter.add(tableOfOverview);

		tableOfCollector = new JTable();
		tableOfCollector.setEnabled(false);
		tableOfCollector.setBounds(12, 163, 468, 64);
		panelCenter.add(tableOfCollector);

		tableOfManager = new JTable();
		tableOfManager.setEnabled(false);
		tableOfManager.setBounds(506, 83, 468, 144);
		panelCenter.add(tableOfManager);
	}

	// 모든 테이블 새로고침
	public void refreshAllTables() {
		DB_Connect();
		setTableModelOfOverview();
		setTableModelOfManager();
		setTableModelOfCollector();
	}

	// 개요 테이블 모델 설정
	public void setTableModelOfOverview() {
		DB_Connect();
		String header[] = { "", "" }; // 테이블 헤더
		String contents[][] = { { "재질", arr[1] }, { "수량/면적/크기", arr[3] }, { "상태", arr[4] },
				{ "시대", arr[25] + " (" + arr[26] + ", " + arr[27] + ")" }, { "소재지/출토지", arr[29] + " " + arr[30] } };
		tableOfOverview.setModel(new DefaultTableModel(contents, header));
		tableOfOverview.getColumnModel().getColumn(0).setPreferredWidth(15);
		tableOfOverview.getColumnModel().getColumn(1).setPreferredWidth(280);
	}

	// 관리단체 테이블 모델 설정
	public void setTableModelOfManager() {
		String header[] = { "", "" };
		String contents[][] = { { "기관명", arr[10] }, { "위치", arr[11] }, { "연락처", arr[12] }, { "문화재 훼손 개수", arr[13] } };
		tableOfCollector.setModel(new DefaultTableModel(contents, header));
		tableOfCollector.getColumnModel().getColumn(0).setPreferredWidth(15);
		tableOfCollector.getColumnModel().getColumn(1).setPreferredWidth(280);
	}

	// 소장기관 테이블 모델 설정
	public void setTableModelOfCollector() {
		String header[] = { "", "" };
		String contents[][] = { { "기관명", arr[14] }, { "위치", arr[15] }, { "소장품번호", arr[5] }, { "연락처", arr[16] },
				{ "문화재 도난 개수", arr[17] }, { "문화재 소장 개수", arr[18] }, { "박물관 분류", arr[21] }, { "설립 및 운영 주체", arr[22] },
				{ "박물관 유형", arr[23] } };
		tableOfManager.setModel(new DefaultTableModel(contents, header));
		tableOfManager.getColumnModel().getColumn(0).setPreferredWidth(15);
		tableOfManager.getColumnModel().getColumn(1).setPreferredWidth(280);
	}

	// 텍스트 영역 초기화
	public void initTextAreas() {
		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(12, 278, 468, 161);
		panelCenter.add(scrollPane1);

		JTextArea textArea1 = new JTextArea();
		textArea1.setLineWrap(true);
		textArea1.setText(arr[2]);
		textArea1.setEditable(false);
		scrollPane1.setViewportView(textArea1);

		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(506, 278, 468, 161);
		panelCenter.add(scrollPane2);

		JTextArea textArea2 = new JTextArea();
		textArea2.setLineWrap(true);
		textArea2.setText(arr[24]);
		textArea2.setEditable(false);
		scrollPane2.setViewportView(textArea2);

		JTextArea textArea3 = new JTextArea();
		textArea3.setText(arr[31]);
		textArea3.setEditable(false);
		textArea3.setBounds(506, 30, 468, 18);
		panelCenter.add(textArea3);
	}

	// 버튼 초기화(이벤트 포함)
	public void initButtons() {
		panelSouth.setLayout(new GridLayout(1, 1, 0, 0));

		JPanel subpanel1 = new JPanel();
		panelSouth.add(subpanel1);

		JPanel subpanel2 = new JPanel();
		panelSouth.add(subpanel2);

		JButton btn1 = new JButton("문화재 훼손");
		btn1.addActionListener(new MyActionListener1());
		subpanel1.add(btn1);

		JButton btn2 = new JButton("훼손 복구");
		btn2.addActionListener(new MyActionListener2());
		subpanel1.add(btn2);

		JButton btn3 = new JButton("문화재 도난");
		btn3.addActionListener(new MyActionListener3());
		subpanel2.add(btn3);

		JButton btn4 = new JButton("도난 복구");
		btn4.addActionListener(new MyActionListener4());
		subpanel2.add(btn4);
	}

	// ==============================================================================

	// 내부 클래스: 각 버튼 클릭 시의 이벤트
	class MyActionListener1 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!arr[4].equals("보존")) {
				JOptionPane.showMessageDialog(null, "보존 상태인 문화재만 훼손 처리 가능합니다.", "문화재 훼손 실패", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				dbconquery.sqlRunTrigger(chName, "보존", "훼손");
				refreshAllTables();
				JOptionPane.showMessageDialog(null, "문화재가 훼손 처리되었습니다.", "문화재 훼손", JOptionPane.WARNING_MESSAGE);
			} catch (SQLException err) {
				err.printStackTrace();
			}
		}
	}

	class MyActionListener2 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!arr[4].equals("훼손")) {
				JOptionPane.showMessageDialog(null, "훼손된 문화재만 훼손 복구 가능합니다.", "훼손 복구 실패", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				dbconquery.sqlRunTrigger(chName, "훼손", "보존");
				refreshAllTables();
				JOptionPane.showMessageDialog(null, "훼손된 문화재가 보존 상태로 복구되었습니다.", "훼손 복구",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (SQLException err) {
				err.printStackTrace();
			}
		}
	}

	class MyActionListener3 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!arr[4].equals("보존")) {
				JOptionPane.showMessageDialog(null, "보존 상태인 문화재만 도난 처리 가능합니다.", "문화재 도난 실패", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				tranjection.RunTranjection(chName, true); // 문화재
				refreshAllTables();
				JOptionPane.showMessageDialog(null, "문화재가 도난 처리되었습니다.", "문화재 도난", JOptionPane.WARNING_MESSAGE);
			} catch (SQLException err) {
				err.printStackTrace();
			}
		}
	}

	class MyActionListener4 implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (!arr[4].equals("도난")) {
				JOptionPane.showMessageDialog(null, "도난된 문화재만 도난 복구 가능합니다.", "도난 복구 실패", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				tranjection.RunTranjection(chName, false);
				refreshAllTables();
				JOptionPane.showMessageDialog(null, "도난된 문화재가 보존 상태로 복구되었습니다.", "도난 복구",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (SQLException err) {
				err.printStackTrace();
			}
		}
	}
}
