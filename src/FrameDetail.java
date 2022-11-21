import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.*;


public class FrameDetail extends JFrame {
	private static final long serialVersionUID = 1L;
	private String chName;
	
	private JPanel contentPane;
	private JPanel panelNorth, panelCenter, panelSouth;
	private JTable tableOfOverview, tableOfManager, tableOfCollector;
	
	// 생성자
	public FrameDetail(String chName) {
		this.chName = chName;
		initFrame();
		initPanels();
		initLabels();
		initTableOfOverview();
		initTableOfManager();
		initTableOfCollector();
		initTextAreas();
		initButtons();
	}
	
	// Frame 초기화
	public void initFrame() {
		setTitle(chName);
		setBounds(100, 100, 750, 600);
		setVisible(true);
	}
	
	// Panel 초기화
	public void initPanels() {
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		panelNorth = new JPanel();
		panelNorth.setBackground(new Color(135, 206, 235));
		panelNorth.setBorder(new EmptyBorder(5, 0, 10, 0));
		FlowLayout flowLayout = (FlowLayout) panelNorth.getLayout();
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
		
		JLabel caption2 = new JLabel("관리기관 정보");
		caption2.setBounds(12, 123, 275, 15);
		panelCenter.add(caption2);
		
		JLabel caption3 = new JLabel("소장기관 정보");
		caption3.setBounds(382, 10, 292, 15);
		panelCenter.add(caption3);
		
		JLabel labelDesc1 = new JLabel("문화재 설명");
		labelDesc1.setBounds(12, 237, 292, 15);
		panelCenter.add(labelDesc1);
		
		JLabel labelDesc2 = new JLabel("박물관 규정");
		labelDesc2.setBounds(382, 237, 292, 15);
		panelCenter.add(labelDesc2);
	}
	
	// 개요 테이블 초기화
	public void initTableOfOverview() {
		String header[] = { "", "" }; // 테이블 헤더
		String contents[][] = {
				{ "재질", "" },
				{ "수량/면적/크기", "" },
				{ "상태", "" },
				{ "시대", ""}
		};
		tableOfOverview = new JTable();
		tableOfOverview.setModel(new DefaultTableModel(contents, header));
		tableOfOverview.getColumnModel().getColumn(0).setPreferredWidth(15);
		tableOfOverview.setEnabled(false);
		tableOfOverview.setBounds(12, 35, 335, 64);
		panelCenter.add(tableOfOverview);
	}
	
	// Table3 초기화
	public void initTableOfManager() {
		String header[] = { "", "" };
		String contents[][] = {
				{ "관리단체", "" },
				{ "위치", "" },
				{ "연락처", "" },
				{ "문화재 훼손 개수", "" }
		};
		tableOfCollector = new JTable();
		tableOfCollector.setModel(new DefaultTableModel(contents, header));
		tableOfCollector.getColumnModel().getColumn(0).setPreferredWidth(15);
		tableOfCollector.setEnabled(false);
		tableOfCollector.setBounds(12, 148, 335, 64);
		panelCenter.add(tableOfCollector);
	}
	
	// 소장기관 테이블 초기화
	public void initTableOfCollector() {
		String header[] = { "", "" };
		String contents[][] = {
				{ "소장기관", "" },
				{ "위치", "" },
				{ "소장품번호", "" },
				{ "연락처", "" },
				{ "문화재 소장 개수", "" },
				{ "문화재 도난 개수", "" },
				{ "박물관 분류", "" },
				{ "설립 및 운영 주체", "" },
				{ "박물관 유형", "" }
		};
		tableOfManager = new JTable();
		tableOfManager.setModel(new DefaultTableModel(contents, header));
		tableOfManager.getColumnModel().getColumn(0).setPreferredWidth(15);
		tableOfManager.setEnabled(false);
		tableOfManager.setBounds(382, 35, 335, 144);
		panelCenter.add(tableOfManager);
	}
	
	// 텍스트 영역 초기화
	public void initTextAreas() {
		JScrollPane scrollPane1 = new JScrollPane();
		scrollPane1.setBounds(12, 262, 335, 177);
		panelCenter.add(scrollPane1);
		
		JTextArea textArea1 = new JTextArea();
		textArea1.setText("문화재 설명\r\n문화재 설명\r\n문화재 설명");
		textArea1.setEditable(false);
		scrollPane1.setViewportView(textArea1);
		
		JScrollPane scrollPane2 = new JScrollPane();
		scrollPane2.setBounds(382, 262, 335, 177);
		panelCenter.add(scrollPane2);
		
		JTextArea textArea2 = new JTextArea();
		textArea2.setText("박물관 규정\r\n박물관 규정\r\n박물관 규정");
		textArea2.setEditable(false);
		scrollPane2.setViewportView(textArea2);
	}
	
	// 버튼 초기화
	public void initButtons() {
		panelSouth.setLayout(new GridLayout(1, 1, 0, 0));
		
		JPanel subpanel1 = new JPanel();
		panelSouth.add(subpanel1);
		
		JPanel subpanel2 = new JPanel();
		panelSouth.add(subpanel2);

		JButton btn1 = new JButton("문화재 훼손");
		subpanel1.add(btn1);
		
		JButton btn2 = new JButton("훼손 취소");
		subpanel1.add(btn2);
		
		JButton btn3 = new JButton("문화재 분실");
		subpanel2.add(btn3);
		
		JButton btn4 = new JButton("분실 취소");
		subpanel2.add(btn4);
	}
}
