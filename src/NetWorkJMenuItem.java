import java.awt.GridBagLayout;
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
	private boolean isSelected = false;
	
	public NetWorkJMenuItem(String text) {
		// TODO Auto-generated constructor stub
		super(text);
		jPanel = new JPanel();
		ImageIcon image = new ImageIcon("src/imageResources/นด.png");
		jLabel_image = new JLabel(image);
		jLabel_text = new JLabel(text);
		add(jPanel);
		GridBagLayout gbl = new GridBagLayout();
		jPanel.setLayout(gbl);
		jPanel.add(jLabel_text);
		
	}
	
	public void addImage() {
		jPanel.add(jLabel_image);
		isSelected = true;
	}
	
	public void removeImage() {
		jPanel.remove(jLabel_image);
	}
	
	public void setIsSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public boolean getIsSelected() {
		return isSelected;
	}
}
