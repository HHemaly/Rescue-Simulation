package view;

import javax.swing.*;

import controller.CommandCenter;

import java.awt.*;


public class MainWindow  extends JFrame{
	public JPanel rescuePanel ;
	public JPanel infoPanel;
	public JPanel unitPanel;
	public JPanel availableUnits;
	public JPanel respondingUnits;
	public JPanel treatingUnits;
	public JPanel generalInfo;
	public JPanel cyclePanel;
	public JTextArea txtAreaInfo;
	public JTextArea txtAreaHistory;
	public JScrollPane scroll;
	public JScrollPane scroll2;
	public JLabel causalities;
	public JLabel currCycle;
	public JButton nxtCycle;
	public String s;
	//public int curr=0;
	public String c;
//	public JButton[][] gridWorld;


	public MainWindow() {
		this.setTitle("Rescue Game");
		setIconImage(new ImageIcon("Icon1.png").getImage());
		this.setBounds(0, 0, 1400, 720);
		this.getContentPane().setLayout(new BorderLayout());
		
		//infoPanel
		infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(2, 1));
		infoPanel.setPreferredSize(new Dimension(getWidth()/5, getHeight()));
		//infoPanel.setBackground(Color.RED);
		this.getContentPane().add(infoPanel,BorderLayout.EAST);
		
		//The components inside the info panel	
		  //1-information about the selected object
		txtAreaInfo = new JTextArea();
		txtAreaInfo.setEditable(false);
		scroll = new JScrollPane(txtAreaInfo,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setPreferredSize(new Dimension(infoPanel.getWidth(),infoPanel.getHeight()/2));
		//scroll.setVisible(true);
		//scroll.setLocation(1250, 0);
		infoPanel.add(scroll);
		
		  //2-information about the active disasters and all the disasters that have been struck
		txtAreaHistory = new JTextArea();
		//txtAreaHistory.setVisible(true);
		txtAreaHistory.setEditable(false);
		scroll2 = new JScrollPane(txtAreaHistory,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll2.setPreferredSize(new Dimension(infoPanel.getWidth(),infoPanel.getHeight()/2));
		//scroll2.setVisible(true);
		//scroll2.setLocation(1250, 360);
		infoPanel.add(scroll2);
		
		//unit panel
		unitPanel = new JPanel();
		unitPanel.setPreferredSize(new Dimension(getWidth()/5, getHeight()));
		unitPanel.setLayout(new GridLayout(4,0));
		this.getContentPane().add(unitPanel,BorderLayout.WEST);
		
		//The components of the unit panel
		 //1-The panel of the general info of the game
		 generalInfo = new JPanel();
		 generalInfo.setPreferredSize(new Dimension(unitPanel.getWidth(),unitPanel.getHeight()/4));
		 //generalInfo.setLayout(new GridLayout(2, 0));
		 unitPanel.add(generalInfo);
		 //Components of the general info panel
		 currCycle = new JLabel();
		 s = "Current Cycle: ";
		 currCycle.setText(s);
		 currCycle.setPreferredSize(new Dimension(100,100));
		 currCycle.setLocation(10, 10);
		 generalInfo.add(currCycle);
		 nxtCycle = new JButton("Next Cycle");
		 nxtCycle.setName("nxtCycle");
		 nxtCycle.setPreferredSize(new Dimension(100,100));
		 //nxtCycle.setLocation(150, 150);
		 generalInfo.add(nxtCycle);
		 causalities = new JLabel();
		 c = "Number of causalities: ";
		 causalities.setText(c);
		 causalities.setPreferredSize(new Dimension(207,80));
		 causalities.setLocation(10, 100);
		 generalInfo.add(causalities);
		
		 //2-The panel of the available units
		 availableUnits = new JPanel(); 
		 availableUnits.setPreferredSize(new Dimension(unitPanel.getWidth(),unitPanel.getHeight()/4));
		 //availableUnits.setBackground(Color.BLUE);
		 availableUnits.setLayout(new GridLayout(2, 3));
		 unitPanel.add(availableUnits);
		 
		 //3-The panel of the responding units
		 respondingUnits = new JPanel();
		 respondingUnits.setPreferredSize(new Dimension(unitPanel.getWidth(),unitPanel.getHeight()/4));
		 //respondingUnits.setBackground(Color.GREEN);
		 respondingUnits.setLayout(new GridLayout(2, 3));
		 unitPanel.add(respondingUnits);
		 
		 //4-The panel of the treating units
		 treatingUnits = new JPanel();
		 treatingUnits.setPreferredSize(new Dimension(unitPanel.getWidth(),unitPanel.getHeight()/4));
		 //treatingUnits.setBackground(Color.ORANGE);
		 treatingUnits.setLayout(new GridLayout(2, 3));
		 unitPanel.add(treatingUnits);
		
		//rescue panel
		rescuePanel = new JPanel();
		rescuePanel.setPreferredSize(new Dimension(getWidth()*(3/5),getHeight()));
		rescuePanel.setLayout(new GridLayout(10, 10));
		//rescuePanel.setBackground(Color.BLACK);
		this.getContentPane().add(rescuePanel,BorderLayout.CENTER);
//		gridWorld = new JButton[10][10];
//		for(int i=0;i<10;i++) {
//			for(int j=0;j<10;j++) {
//				JButton tmp = new JButton();
//				rescuePanel.add(tmp);
//				gridWorld[i][j] = tmp;
//			}
//		}
	}


	//public static void main(String[] args) {
		//new MainWindow();
	//}
}
