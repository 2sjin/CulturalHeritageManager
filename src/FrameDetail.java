import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class FrameDetail extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// 생성자
	public FrameDetail(String chName) {
		setTitle(chName);
		setBounds(100, 100, 450, 300);
		
		contentPane = new JPanel();
		setContentPane(contentPane);

		contentPane.add(new JLabel("문화재명: " + chName));
		
		setVisible(true);
	}
}
