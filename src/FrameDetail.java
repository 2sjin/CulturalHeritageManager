import javax.swing.*;
import java.awt.*;

public class FrameDetail extends JFrame {
	private static final long serialVersionUID = 1L;

	public FrameDetail(String chName) {
		setTitle(chName);
		setSize(400, 300);
		
		Container c = getContentPane();
		c.setLayout(new FlowLayout());
		c.add(new JLabel("문화재명: " + chName));
		
		setVisible(true);
	}
}