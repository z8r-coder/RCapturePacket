import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

public class mainFrame extends JFrame implements Runnable{
	private JPanel jp;
	private JPanel TablePanel;
	private JPanel desPanel;
	private JButton btn = new JButton("begin");
	JTable table;
	String []entry = {"No.","Time","Source","Destination","Protocol","Length"};
	String [][]Data = {{"1","0.0000","172.24.2.224","224.0.0.252","TCP","66"}};
	String []d = {"1","0.0000","172.24.2.224","224.0.0.252","TCP","66"};
	private DefaultTableModel model = new DefaultTableModel(null, entry);
	private CatchPacket cp = new CatchPacket();
	public mainFrame() {
		// TODO Auto-generated constructor stub
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Toolkit toolkit = getToolkit();
		Dimension dim = toolkit.getScreenSize();
		setLocation((int)(dim.getWidth() - LenthAll.WINDOW_WIDTH) / 2,
				(int)(dim.getHeight() - LenthAll.WINDOW_HEIGHT) / 2);
		setSize(LenthAll.WINDOW_WIDTH, LenthAll.WINDOW_HEIGHT);
		jp = (JPanel) getContentPane();
		jp.setLayout(new GridLayout(2, 1));
		
		cp.getDevices();//初始化网卡
		cp.desNetworkInterface();//获取网卡描述
		//打开一个网卡接口，捕获该网卡的包
		cp.getCap(cp.devices[0], true, "");
		
		//将cell设置成不可编辑
		table = new JTable(model){
			public boolean isCellEditable(int row,int column) {
				return false;
			}
		};
		//创建表格
		createTable();
		//创建具体描述窗口
		createDes();
		jp.add(TablePanel);
		jp.add(desPanel);
		setVisible(true);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addRow(d);
		table.setModel(model);
	}
	
	//捕捉包列表
	public void createTable() {
		TablePanel = new JPanel();
		table.setModel(model);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		TablePanel.setLayout(new BoxLayout(TablePanel, BoxLayout.Y_AXIS));//垂直排列
		TablePanel.add(Box.createVerticalStrut(10));//与顶的距离
		TablePanel.add(scrollPane);
		TablePanel.add(Box.createVerticalStrut(5));
	}
	
	//包的具体描述
	public void createDes() {
		desPanel = new JPanel();
		JTextPane jtp = new JTextPane();
		JScrollPane scrollPane = new JScrollPane(jtp);
		jtp.setEditable(false);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		desPanel.setLayout(new BoxLayout(desPanel, BoxLayout.Y_AXIS));
		desPanel.setBackground(Color.WHITE);
		desPanel.setSize(0, 400);
		jtp.setText("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
		desPanel.add(scrollPane);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		int count = 0;//
	}
}
