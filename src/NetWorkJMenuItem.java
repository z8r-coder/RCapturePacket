import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class NetWorkJMenuItem extends JMenuItem{
	private JLabel jLabel_image;
	private JLabel jLabel_text;
	private JPanel jPanel;
	public NetWorkJMenuItem(String text) {
		// TODO Auto-generated constructor stub
		super(text);
		jPanel = new JPanel();
		ImageIcon image = new ImageIcon("src/imageResources/นด.png");
		jLabel_image = new JLabel(image);
		jLabel_text = new JLabel(text);
		add(jPanel);
		jPanel.setLayout(new GridLayout(2, 1));
		jPanel.add(jLabel_text);
		jPanel.add(jLabel_image);
	}
}
