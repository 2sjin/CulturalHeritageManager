import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FrameDetail extends JFrame {
	private static final long serialVersionUID = 1L;
	private String chName;
	
	private JPanel contentPane, panelNorth, panelCenter;
	private JLabel caption1, caption2, caption3;
	private JTable tableOfOverview, tableOfManager, tableOfCollector;
	
	// 생성자
	public FrameDetail(String chName) {
		this.chName = chName;
		initFrame();
		initPanel();
		initLabelOfTableCaption();
		initTableOfOverview();
		initTableOfManager();
		initTableOfCollector();
		
		JLabel lblName = new JLabel(chName);
		lblName.setFont(new Font("맑은 고딕", Font.BOLD, 20));
		panelNorth.add(lblName);
	}
	
	// Frame 초기화
	public void initFrame() {
		setTitle(chName);
		setBounds(100, 100, 634, 433);
		setVisible(true);
	}
	
	// Panel 초기화
	public void initPanel() {
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		panelNorth = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panelNorth.getLayout();
		contentPane.add(panelNorth, BorderLayout.NORTH);
		
		panelCenter = new JPanel();
		panelCenter.setSize(310, 360);
		panelCenter.setLayout(null);
		contentPane.add(panelCenter, BorderLayout.CENTER);
	}
	
	// 레이블(테이블 제목) 초기화
	public void initLabelOfTableCaption() {
		caption1 = new JLabel("개요");
		caption1.setBounds(12, 10, 275, 15);
		panelCenter.add(caption1);
		
		caption2 = new JLabel("관리기관 정보");
		caption2.setBounds(12, 123, 275, 15);
		panelCenter.add(caption2);
		
		caption3 = new JLabel("소장기관 정보");
		caption3.setBounds(316, 10, 292, 15);
		panelCenter.add(caption3);
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
		tableOfOverview = new JTable(contents, header);
		tableOfOverview.setEnabled(false);
		tableOfOverview.setBounds(12, 35, 275, 64);
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
		tableOfCollector = new JTable(contents, header);
		tableOfCollector.setEnabled(false);
		tableOfCollector.setBounds(12, 148, 275, 64);
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
				{ "박물관 유형", "" },
				{ "박물관 규정", "" }
		};
		tableOfManager = new JTable(contents, header);
		tableOfManager.setEnabled(false);
		tableOfManager.setBounds(316, 35, 292, 160);
		panelCenter.add(tableOfManager);
	}
}
