package controller;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import exceptions.DisasterException;
import exceptions.UnitException;
import model.disasters.Collapse;
import model.disasters.Disaster;
import model.disasters.GasLeak;
import model.disasters.Injury;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.Unit;
import model.units.UnitState;
import simulation.Rescuable;
import simulation.Simulator;
import view.MainWindow;

public class CommandCenter implements SOSListener,ActionListener {
	private MainWindow view;
	private Simulator engine;
	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private ArrayList<Unit> emergencyUnits;
	private JButton Ambulance;
	private JButton Gas;
	private JButton Fire;
	private JButton Evac;
	private JButton Infection;
	public JButton[][] gridWorld = new JButton [10][10];
	public String[][] gridWorldInfo = new String [10][10];
	public ArrayList<ImageIcon> emptybuildings;
	public ArrayList<ImageIcon> buildingsOcc;
	public ArrayList<ImageIcon> citizens;
	public JList list;
	public ArrayList<String> avaUnits;
	public ArrayList<String> resUnits;
	public ArrayList<String> treatUnits;
	public ArrayList<JButton> unitButtons;
	public int q=-1;
	public int p=-1;
	public int r=-1;
	public boolean flag;

	public CommandCenter() throws Exception {
		view = new MainWindow();
		engine= new Simulator(this);
		visibleBuildings= new ArrayList<ResidentialBuilding>();
		visibleCitizens= new ArrayList<Citizen>() ;
		emergencyUnits = engine.getEmergencyUnits();
		view.nxtCycle.addActionListener(this);

		view.currCycle.setText(view.s+0);
		view.causalities.setText(view.c+0);

		avaUnits = new ArrayList<String>();
		avaUnits.add("Ambulance");
		avaUnits.add("GasControlUnit");
		avaUnits.add("FireTruck");
		avaUnits.add("Evacuator");
		avaUnits.add("DiseaseControlUnit");
		resUnits = new ArrayList<String>();
		treatUnits = new ArrayList<String>();

		emptybuildings = new ArrayList<ImageIcon>();
		buildingsOcc = new ArrayList<ImageIcon>();
		citizens = new ArrayList<ImageIcon>();

		emptybuildings.add(new ImageIcon("Building1.jpeg"));
		emptybuildings.add(new ImageIcon("Building2.png"));
		emptybuildings.add(new ImageIcon("Building3.png"));
		emptybuildings.add(new ImageIcon("Building4.jpg"));
		buildingsOcc.add(new ImageIcon("BuildingOcc1.png"));
		buildingsOcc.add(new ImageIcon("BuildingOcc2.png"));
		citizens.add(new ImageIcon("Citizen1.png"));
		citizens.add(new ImageIcon("Citizen2.png"));
		citizens.add(new ImageIcon("Citizen3.png"));
		citizens.add(new ImageIcon("Citizen4.png"));
		citizens.add(new ImageIcon("Citizen5.png"));

		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				JButton tmp = new JButton();
				view.rescuePanel.add(tmp);
				gridWorld[i][j] = tmp;
				gridWorld[i][j].addActionListener(this);
			}
		}

		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				gridWorldInfo[i][j] = "";
			}
		}

		/*		for(int i=0;i<engine.getBuildings().size();i++) {
			ran = (int) (Math.random()*(emptybuildings.size())); 
			ran2 = (int) (Math.random()*(buildingsOcc.size()));
			JButton tmp = gridWorld[engine.getBuildings().get(i).getLocation().getY()][engine.getBuildings().get(i).getLocation().getX()];
			if(engine.getBuildings().get(i).getOccupants().size()==0) {
				tmp.setIcon(emptybuildings.get(ran));
				tmp.setName("EmptyBuilding");
			}
			else {
				tmp.setIcon(buildingsOcc.get(ran2));
				tmp.setName("BuildingOcc");
			}
			//tmp.addMouseListener(this);
		}*/

		for(int i=0;i<engine.getBuildings().size();i++) {
			int x = engine.getBuildings().get(i).getLocation().getX();
			int y = engine.getBuildings().get(i).getLocation().getY();
			gridWorld[y][x].setName("NonEmpty");
			gridWorldInfo[y][x]+="Building ";
		}

		//		for(int i=0;i<engine.getCitizens().size();i++) {
		//			ran3 = (int) (Math.random()*(citizens.size()));
		//			JButton tmp =gridWorld[engine.getCitizens().get(i).getLocation().getY()][engine.getCitizens().get(i).getLocation().getX()];
		//			if(tmp.getName()==null) {
		//				tmp.setIcon(citizens.get(ran3));
		//				tmp.setName("Citizen");
		//			}
		//			//tmp.addMouseListener(this);
		//		}

		for(int i=0;i<engine.getCitizens().size();i++) {
			int x = engine.getCitizens().get(i).getLocation().getX();
			int y = engine.getCitizens().get(i).getLocation().getY();
			gridWorld[y][x].setName("NonEmpty");
			gridWorldInfo[y][x]+="Citizen ";
		}

		Ambulance = new JButton(new ImageIcon("Ambulance.png"));
		Ambulance.setName("Ambulance");
		//Ambulance.addMouseMotionListener(this);
		Ambulance.addActionListener(this);
		view.availableUnits.add(Ambulance);

		Gas = new JButton(new ImageIcon("GCU.png"));
		Gas.setName("GasControlUnit");
		//Gas.addMouseMotionListener(this);
		Gas.addActionListener(this);
		view.availableUnits.add(Gas);

		Fire =  new JButton(new ImageIcon("FireTruck.png"));
		Fire.setName("FireTruck");
		//Fire.addMouseMotionListener(this);
		Fire.addActionListener(this);
		view.availableUnits.add(Fire);

		Evac =  new JButton(new ImageIcon("Evacuator.png"));
		Evac.setName("Evacuator");
		//Evac.addMouseMotionListener(this);
		Evac.addActionListener(this);
		view.availableUnits.add(Evac);

		Infection =  new JButton(new ImageIcon("DCU.png"));
		Infection.setName("DiseaseControlUnit");
		//Infection.addMouseMotionListener(this);
		Infection.addActionListener(this);
		view.availableUnits.add(Infection);

		unitButtons = new ArrayList<JButton>();
		unitButtons.add(Ambulance);
		unitButtons.add(Evac);
		unitButtons.add(Fire);
		unitButtons.add(Infection);
		unitButtons.add(Gas);

		//		for(int i=0;i<emergencyUnits.size();i++) {
		//			JButton tmp =  gridWorld[emergencyUnits.get(i).getLocation().getY()][emergencyUnits.get(i).getLocation().getX()];
		//			if(emergencyUnits.get(i) instanceof Evacuator)
		//				tmp.setIcon(new ImageIcon("Evacuator.png"));
		//			else if(emergencyUnits.get(i) instanceof FireTruck) 
		//				tmp.setIcon(new ImageIcon("FireTruck.png"));
		//			else if(emergencyUnits.get(i) instanceof Ambulance)
		//				tmp.setIcon(new ImageIcon("Ambulance.png"));
		//			else if(emergencyUnits.get(i) instanceof GasControlUnit)
		//				tmp.setIcon(new ImageIcon("GCU.png"));
		//			else if(emergencyUnits.get(i) instanceof DiseaseControlUnit)
		//				tmp.setIcon(new ImageIcon("DCU.png"));
		//			tmp.setName("Unit");	
		//		}

		for(int i=0;i<emergencyUnits.size();i++) {
			int x = emergencyUnits.get(i).getLocation().getX();
			int y = emergencyUnits.get(i).getLocation().getY();
			JButton tmp =  gridWorld[y][x];
			tmp.setName("NonEmpty");
			if(emergencyUnits.get(i) instanceof Evacuator)
				gridWorldInfo[y][x]+="Evacuator ";
			else if(emergencyUnits.get(i) instanceof FireTruck) 
				gridWorldInfo[y][x]+="FireTruck ";
			else if(emergencyUnits.get(i) instanceof Ambulance)
				gridWorldInfo[y][x]+="Ambulance ";
			else if(emergencyUnits.get(i) instanceof GasControlUnit)
				gridWorldInfo[y][x]+="GasControlUnit ";
			else if(emergencyUnits.get(i) instanceof DiseaseControlUnit)
				gridWorldInfo[y][x]+="DiseaseControlUnit ";
		}

		//		for(int v=0;v<unitButtons.size();v++) {
		//			view.treatingUnits.add(unitButtons.get(v));
		//			view.availableUnits.add(unitButtons.get(v));
		//		}

		view.setDefaultCloseOperation(view.EXIT_ON_CLOSE);
		view.setVisible(true);
		view.validate();
		//view.repaint();
	}

	public void receiveSOSCall(Rescuable r) {
		if (r instanceof ResidentialBuilding) {
			if (!visibleBuildings.contains(r))
				visibleBuildings.add((ResidentialBuilding) r);
		} else {
			if (!visibleCitizens.contains(r))
				visibleCitizens.add((Citizen) r);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		if(b.getName()==null) {
			JOptionPane.showMessageDialog(view,
					"You are not choosing anything");
		}
		else if(b.getName().equals("nxtCycle")) {
			try {
				engine.nextCycle();
			}catch (DisasterException d){
				if(engine.checkGameOver()) {
					view.setEnabled(false);
					view.txtAreaHistory.invalidate();
					view.txtAreaInfo.invalidate();
					for(int i=0;i<unitButtons.size();i++) {
						unitButtons.get(i).setEnabled(false);
					}
					for(int i=0;i<10;i++) {
						for(int j=0;j<10;j++) {
							gridWorld[i][j].setEnabled(false);
						}
					}
					view.invalidate();
					JOptionPane.showMessageDialog(view,
							"Number of Causalities:"+ engine.calculateCasualties());
				}
			}
			if(engine.checkGameOver()) {
				b.setEnabled(false);
				for(int i=0;i<unitButtons.size();i++) {
					unitButtons.get(i).setEnabled(false);
				}
				for(int i=0;i<10;i++) {
					for(int j=0;j<10;j++) {
						gridWorld[i][j].setEnabled(false);
					}
				}
				view.invalidate();
				JOptionPane.showMessageDialog(view,
						"Number of Causalities:"+ engine.calculateCasualties());
			}
			//view.curr++;
			view.currCycle.setText(view.s+engine.getCurrentCycle());
			view.causalities.setText(view.c+engine.calculateCasualties());
			updateTreatingUnits(emergencyUnits);
			updateUnitss(unitButtons, treatUnits);
			updateGridInfo();
			updateGrid();
			updateDisasters(engine.getExecutedDisasters());
			view.revalidate();
			view.repaint();
		}
		else if(b.getName().equals("NonEmpty")) {
			Object[] optionsAll = {"Get info",
			"Rescue!!"};
			int n = JOptionPane.showOptionDialog(view,
					"Would you like to rescue a target, or only get a specific information ??",
					"Choose!!",
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE,
					null,     
					optionsAll,  
					optionsAll[0]);
			int y = getButtonX(b); 
			int x = getButtonY(b); 
			String s = gridWorldInfo[y][x];
			String[] sm = s.split(" ");
			String o = "";
			if(n==0) {
				Object[] options = {"Get all info",
						"Citizen","Building"};
				int t = JOptionPane.showOptionDialog(view,
						"Would you like to get the info of the content of the cell, or only the citizen, or the building??",
						"Choose!!",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,     
						options,  
						options[2]);
				if(t==0) {
					for(int i=0;i<sm.length;i++) {
						if(sm[i].equals("Building")) {
							ResidentialBuilding rb = engine.getBuilding(x,y);
							o+=rb.updateInfo()+"\n\n";
							view.txtAreaInfo.setText(rb.updateInfo());
						}else if(sm[i].equals("Citizen")) {
							Citizen c = engine.getCitizen(x, y);
							o+=c.updateInfo()+"\n\n";
						}else if(sm[i].equals("Evacuator")||sm[i].equals("FireTruck")||sm[i].equals("GasControlUnit")||
								sm[i].equals("DiseaseControlUnit")||sm[i].equals("Ambulance")) {
							o+=getUnit(sm[i],emergencyUnits).updateInfo()+"\n\n";
						}	
					}
					view.txtAreaInfo.setText(o);
				}else if(t==1) {
					if(!(contains(sm,"Citizen"))){
						JOptionPane.showMessageDialog(view,
								"There is no citizens in that location");
					}else {
						//Citizen c = engine.getCitizen(getButtonY(b), getButtonX(b));
						JFrame j = new JFrame("Choose the Citizen");
						//j.setPreferredSize(new Dimension(150, 100));
						j.setBounds(400, 150, 300, 300);
						Object[] data = engine.getCitizens(x,y);
						list = new JList(data);
						list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						list.setLayoutOrientation(JList.VERTICAL);
						list.setVisibleRowCount(-1);

						JScrollPane listScroller = new JScrollPane(list);
						listScroller.setPreferredSize(new Dimension(100, 80));
						j.getContentPane().add(listScroller);
						list.addListSelectionListener(
								new ListSelectionListener(){
									public void valueChanged(ListSelectionEvent e) {
										Citizen c = engine.getCitizen((String)data[list.getSelectedIndex()]);
										view.txtAreaInfo.setText(c.updateInfo());
										j.dispose();
									}				
								});
						j.setVisible(true);
						j.validate();
						j.setDefaultCloseOperation(j.DISPOSE_ON_CLOSE);
						//view.txtAreaInfo.setText(c.updateInfo());
					}}else if(t==2) {
						if(!(contains(sm,"Building"))){
							JOptionPane.showMessageDialog(view,
									"There is no buildings in that location");
						}else {
							ResidentialBuilding rb = engine.getBuilding(x,y);
							view.txtAreaInfo.setText(rb.updateInfo());
						}
					}
			}else if(n==1) {
				Object[] optionsres = {"Citizen",
				"Building"};
				int i = JOptionPane.showOptionDialog(view,
						"Would you like to rescue a Citizen, or a building??",
						"Choose!!",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,     
						optionsres,  
						optionsres[0]);
				if(i==0) {
					if(!(contains(sm,"Citizen"))){
						JOptionPane.showMessageDialog(view,
								"There is no citizens in that location");
					}else {
						//Citizen c = engine.getCitizen(getButtonY(b), getButtonX(b));
						JFrame u = new JFrame("Choose the Citizen");
						//j.setPreferredSize(new Dimension(150, 100));
						u.setBounds(400, 150, 300, 300);
						Object[] data2 = engine.getCitizens(x, y);
						JList list2 = new JList(data2);
						list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
						list2.setLayoutOrientation(JList.VERTICAL);
						list2.setVisibleRowCount(-1);

						JScrollPane listScroller2 = new JScrollPane(list2);
						listScroller2.setPreferredSize(new Dimension(100, 80));
						u.getContentPane().add(listScroller2);
						list2.addListSelectionListener(
								new ListSelectionListener(){
									public void valueChanged(ListSelectionEvent e) {
										Citizen c = engine.getCitizen((String) data2[list2.getSelectedIndex()]);
										try {
											if(q>=0) {
												Unit tmp = getUnit(avaUnits.get(q), emergencyUnits);
												tmp.respond(c);
											}else if(p>=0) {
												Unit tmp = getUnit(resUnits.get(p), emergencyUnits);
												tmp.respond(c);
											}else if(r>=0) {
												Unit tmp = getUnit(treatUnits.get(r), emergencyUnits);
												tmp.respond(c);
											}
										}catch (UnitException e0) {
											flag = true;
											JOptionPane.showMessageDialog(view,
													"You must choose the correct Citizen");
										} catch (DisasterException e1) {
											flag=true;
											JOptionPane.showMessageDialog(view,
													"The citizen is already dead");
										}finally {
											if(!flag) {
												if(q>=0) {
													String s = avaUnits.remove(q);
													Unit tmp = getUnit(s, emergencyUnits);
													resUnits.add(s);
													q = -1;
													view.respondingUnits.add(findButton(unitButtons, s));
													view.availableUnits.revalidate();
													view.respondingUnits.revalidate();
													view.availableUnits.repaint();
													view.respondingUnits.repaint();
												}else if(p>=0) {
													String s = resUnits.remove(p);
													Unit tmp = getUnit(s, emergencyUnits);
													resUnits.add(s);
													p = -1;
													view.respondingUnits.add(findButton(unitButtons, s));
													view.respondingUnits.revalidate();
													view.respondingUnits.repaint();
												}else if(r>=0) {
													String s = treatUnits.remove(r);
													Unit tmp = getUnit(s, emergencyUnits);
													resUnits.add(s);
													r = -1;
													view.respondingUnits.add(findButton(unitButtons, s));
													view.treatingUnits.revalidate();
													view.respondingUnits.revalidate();
													view.treatingUnits.repaint();
													view.respondingUnits.repaint();
												}
											}else {
												flag = false;
											}
										}
										u.dispose();
									}});
						u.setVisible(true);
						u.validate();
						u.setDefaultCloseOperation(u.DISPOSE_ON_CLOSE);
					}}else if(i==1) {
						if(!(contains(sm,"Building"))){
							JOptionPane.showMessageDialog(view,
									"There is no buildings in that location");
						}
						else{ResidentialBuilding rb = engine.getBuilding(x, y);
						try {
							if(q>=0) {
								Unit tmp = getUnit(avaUnits.get(q), emergencyUnits);
								tmp.respond(rb);
							}else if(p>=0) {
								Unit tmp = getUnit(resUnits.get(p), emergencyUnits);
								tmp.respond(rb);
							}else if(r>=0) {
								Unit tmp = getUnit(treatUnits.get(r), emergencyUnits);
								tmp.respond(rb);
							}
						}catch (UnitException e0) {
							flag = true;
							JOptionPane.showMessageDialog(view,
									"You must choose the correct Building");
						} catch (DisasterException e1) {
							flag = true;
							JOptionPane.showMessageDialog(view,
									"The building is already destroyed");
						}
						finally{
							if(!flag) {
								if(q>=0) {
									String s1 = avaUnits.remove(q);
									Unit tmp = getUnit(s1, emergencyUnits);
									resUnits.add(s1);
									q = -1;
									view.respondingUnits.add(findButton(unitButtons, s1));
									view.availableUnits.revalidate();
									view.respondingUnits.revalidate();
									view.availableUnits.repaint();
									view.respondingUnits.repaint();
								}else if(p>=0) {
									String s1 = resUnits.remove(p);
									Unit tmp1 = getUnit(s1, emergencyUnits);
									resUnits.add(s1);
									p = -1;
									view.respondingUnits.add(findButton(unitButtons, s1));
									view.respondingUnits.revalidate();
									view.respondingUnits.repaint();
								}else if(r>=0) {
									String s11 = treatUnits.remove(r);
									Unit tmp11 = getUnit(s11, emergencyUnits);
									resUnits.add(s11);
									r = -1;
									view.respondingUnits.add(findButton(unitButtons, s11));
									view.treatingUnits.revalidate();
									view.respondingUnits.revalidate();
									view.treatingUnits.repaint();
									view.respondingUnits.repaint();
								}
							}else {
								flag = false;
							}
						}
						}
					}
			}}else if(b.getName().equals("Ambulance") || b.getName().equals("DiseaseControlUnit")||
					b.getName().equals("GasControlUnit")||b.getName().equals("FireTruck")||b.getName().equals("Evacuator")) {	
				Object[] options = {"Get the info",
				"Respond to a target!!"};
				int n = JOptionPane.showOptionDialog(view,
						"Would you like to respond to a target, or only get their information ??",
						"Choose!!",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,     
						options,  
						options[0]); 
				if(n==0) { 
					if(b.getName().equals("Evacuator") &&((Evacuator)getUnit(b.getName(),emergencyUnits)).getPassengers().size()>0) {
						Object[] optionsE = {"Unit",
						"Passenger"};
						int m = JOptionPane.showOptionDialog(view,
								"Would you like to get the information of the evacuator, or a passenger in it??",
								"Unit, or passenger??",
								JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE,
								null,     
								optionsE,  
								optionsE[0]); 
						if(m==0) {
							view.txtAreaInfo.setText(getUnit(b.getName(),emergencyUnits).updateInfo());
						}else if(m==1) {
							JFrame k = new JFrame("Choose the Citizen");
							//j.setPreferredSize(new Dimension(150, 100));
							k.setBounds(400, 150, 300, 300);
							Object[] data3 = getPassengerNames(((Evacuator)getUnit(b.getName(),emergencyUnits)).getPassengers());
							JList list3 = new JList(data3);
							list3.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
							list3.setLayoutOrientation(JList.VERTICAL);
							list3.setVisibleRowCount(-1);

							JScrollPane listScroller3 = new JScrollPane(list3);
							listScroller3.setPreferredSize(new Dimension(100, 80));
							k.getContentPane().add(listScroller3);
							k.validate();

							list3.addListSelectionListener(
									new ListSelectionListener(){
										public void valueChanged(ListSelectionEvent e) {
											Citizen c = engine.getCitizen((String)data3[list3.getSelectedIndex()], ((Evacuator)getUnit(b.getName(),emergencyUnits)).getPassengers());
											view.txtAreaInfo.setText(c.updateInfo());
											k.dispose();
										}
									});
							k.setVisible(true);
							k.validate();
							k.setDefaultCloseOperation(k.DISPOSE_ON_CLOSE);

						}}else {
							view.txtAreaInfo.setText(getUnit(b.getName(),emergencyUnits).updateInfo());
						}
				}
				else if(n==1) {
					if((getUnit(b.getName(),emergencyUnits)).getState()==UnitState.IDLE){
						q=avaUnits.indexOf(b.getName());
					}else if((getUnit(b.getName(),emergencyUnits)).getState()==UnitState.RESPONDING){
						p=resUnits.indexOf(b.getName());
					}else if((getUnit(b.getName(),emergencyUnits)).getState()==UnitState.TREATING){
						r=treatUnits.indexOf(b.getName());
					}
				}
				//view.txtAreaInfo.setText(getUnit(b.getName(),emergencyUnits).updateInfo());
			}
		// TODO Auto-generated method stub
	}

	private Unit getUnit(String name, ArrayList<Unit> emergencyUnits2) {
		for(int i=0;i<emergencyUnits2.size();i++) {
			if((name.equals("Ambulance") && (emergencyUnits2.get(i) instanceof Ambulance)) ||
					(name.equals("Evacuator") && (emergencyUnits2.get(i) instanceof Evacuator)) ||
					(name.equals("FireTruck") && (emergencyUnits2.get(i) instanceof FireTruck)) ||
					(name.equals("GasControlUnit") && (emergencyUnits2.get(i) instanceof GasControlUnit)) ||
					(name.equals("DiseaseControlUnit") && (emergencyUnits2.get(i) instanceof DiseaseControlUnit)))
				return emergencyUnits2.get(i);
		}
		return null;
		// TODO Auto-generated method stub

	}

	public void updateGrid(){
		int ran; 
		int ran2;
		int ran3;
		for(int i=0;i<visibleBuildings.size();i++) {
			ran = (int) (Math.random()*(emptybuildings.size())); 
			ran2 = (int) (Math.random()*(buildingsOcc.size()));
			if(visibleBuildings.get(i).getDisaster().getStartCycle()==engine.getCurrentCycle()) {
				if(visibleBuildings.get(i).getOccupants().size()>0) {
					gridWorld[visibleBuildings.get(i).getLocation().getY()][visibleBuildings.get(i).getLocation().getX()].setIcon(buildingsOcc.get(ran2));
				}else {
					gridWorld[visibleBuildings.get(i).getLocation().getY()][visibleBuildings.get(i).getLocation().getX()].setIcon(emptybuildings.get(ran));
				}
			}if(visibleBuildings.get(i).getStructuralIntegrity()==0) {
				gridWorld[visibleBuildings.get(i).getLocation().getY()][visibleBuildings.get(i).getLocation().getX()].setIcon(new ImageIcon("Destroyed.png"));
			}
		}

		for(int j=0;j<visibleCitizens.size();j++) {
			ran3 = (int) (Math.random()*(citizens.size()));
			if(visibleCitizens.get(j).getDisaster().getStartCycle()==engine.getCurrentCycle()) {
				gridWorld[visibleCitizens.get(j).getLocation().getY()][visibleCitizens.get(j).getLocation().getX()].setIcon(citizens.get(ran3));

			}if(visibleCitizens.get(j).getState()==CitizenState.DECEASED) {
				gridWorld[visibleCitizens.get(j).getLocation().getY()][visibleCitizens.get(j).getLocation().getX()].setIcon(new ImageIcon("Dead.png"));
			}
		}
		view.rescuePanel.revalidate();
		view.rescuePanel.repaint();
	}

	public int getButtonX(JButton b) {
		for(int i =0;i<10;i++) {
			for(int j=0;j<10;j++) {
				if(gridWorld[i][j]==b) {
					return i;
				}
			}
		}
		return -1;
	}

	public int getButtonY(JButton b) {
		for(int i =0;i<10;i++) {
			for(int j=0;j<10;j++) {
				if(gridWorld[i][j]==b) {
					return j;
				}
			}
		}
		return -1;
	}

	public void updateDisasters(ArrayList<Disaster> d) {
		String s = "Disasters were struck in this cycle:\n---------------------------------------------------------";
		for(int i=0;i<d.size();i++) {
			if(d.get(i).getStartCycle()==engine.getCurrentCycle()) {
				if(d.get(i) instanceof Collapse) {
					s+="\nCollapse";
				}else if(d.get(i) instanceof GasLeak) {
					s+="\nGas Leak";
				}else if(d.get(i) instanceof model.disasters.Infection) {
					s+="\nInfection";
				}else if(d.get(i) instanceof Injury) {
					s+="\nInjury";
				}else if(d.get(i) instanceof model.disasters.Fire) {
					s+="\nFire";
				}
				s+=" ,and the Target is: ";
				if(d.get(i).getTarget() instanceof Citizen) {
					s+="Citizen: "+((Citizen)d.get(i).getTarget()).getName();
				}else if(d.get(i).getTarget() instanceof ResidentialBuilding) {
					s+="Building";
				}
			}
		}
		s+="\n\nThe active disasters are:\n---------------------------------------------------------";
		for(int i =0;i<engine.getExecutedDisasters().size();i++) {
			if(engine.getExecutedDisasters().get(i).isActive()) {
				if(d.get(i) instanceof Collapse) {
					s+="\nCollapse";
				}else if(d.get(i) instanceof GasLeak) {
					s+="\nGas Leak";
				}else if(d.get(i) instanceof model.disasters.Infection) {
					s+="\nInfection";
				}else if(d.get(i) instanceof Injury) {
					s+="\nInjury";
				}else if(d.get(i) instanceof model.disasters.Fire) {
					s+="\nFire";
				}
				s+=" ,and the Target is: ";
				if(d.get(i).getTarget() instanceof Citizen) {
					s+="Citizen: "+((Citizen)d.get(i).getTarget()).getName();
				}else if(d.get(i).getTarget() instanceof ResidentialBuilding) {
					s+="Building";
				}
			}
		}

		if(!(areAlive(visibleCitizens))) {
			s+="\n\nDeseased Citizen(s):\n-------------------------------------------------------------";
			for(int i=0;i<visibleCitizens.size();i++) {
				if(visibleCitizens.get(i).getState()==CitizenState.DECEASED) {
					s+="\nCitizen "+ visibleCitizens.get(i).getName() +" is deceased in location: X= "+visibleCitizens.get(i).getLocation().getX()+", Y= "+visibleCitizens.get(i).getLocation().getY();
				}
			}
		}
		for(int i=0;i<visibleBuildings.size();i++) {
			ArrayList<Citizen> tmp = visibleBuildings.get(i).getOccupants();
			if(!areAlive(tmp)) {
				for(int j=0;j<tmp.size();j++) {
					if(tmp.get(j).getState()==CitizenState.DECEASED) {
						s+="\nCitizen "+tmp.get(j).getName() +" is deceased in location: X= "+tmp.get(j).getLocation().getX()+", Y= "+tmp.get(j).getLocation().getY();
					}
				}
			}
		}
		view.txtAreaHistory.setText(s);
	}

	private Boolean areAlive(ArrayList<Citizen> c) {
		for(int i=0;i<c.size();i++) {
			if(c.get(i).getState()==CitizenState.DECEASED) {
				return false;
			}
		}
		return true;
	}

	private JButton findButton(ArrayList<JButton> b,String name) {
		for(int i=0;i<b.size();i++) {
			if((b.get(i).getName()).equals(name)) {
				return b.get(i);
			}
		}
		return null;
	}

	private void updateTreatingUnits(ArrayList<Unit> u) {
		for(int i=0;i<u.size();i++) {
			Unit w = u.get(i);
			if(w.getState()==UnitState.TREATING) {
				String y = "";
				if(w instanceof Ambulance)
					y+= "Ambulance";
				else if(w instanceof FireTruck)
					y+= "FireTruck";
				else if(w instanceof GasControlUnit)
					y+= "GasControlUnit";
				else if(w instanceof Evacuator)
					y+= "Evacuator";
				else if(w instanceof DiseaseControlUnit)
					y+= "DiseaseControlUnit";
				//view.respondingUnits.remove(findButton(unitButtons, y));
				view.treatingUnits.add(findButton(unitButtons, y));
				treatUnits.add(y);
			}
		}
		view.treatingUnits.revalidate();
		view.respondingUnits.revalidate();
		view.treatingUnits.repaint();
		view.respondingUnits.repaint();
	}


	private void updateUnitss(ArrayList<JButton> b,ArrayList<String>s) {
		for(int i=0;i<s.size();i++) {
			String tmp = s.get(i);
			if(getUnit(tmp, emergencyUnits).getState()==UnitState.IDLE && s.remove(tmp) ) {
				//view.treatingUnits.remove(findButton(unitButtons, tmp));
				view.availableUnits.add(findButton(unitButtons, tmp));
				avaUnits.add(tmp);
			}
		}
		view.treatingUnits.revalidate();
		view.availableUnits.revalidate();
		view.treatingUnits.repaint();
		view.availableUnits.repaint();
	}

	public Object[] getPassengerNames(ArrayList<Citizen> c) {
		Object[] o= new Object[c.size()];
		for(int i=0;i<c.size();i++) 
			o[i] = c.get(i).getName();
		return o;
	}

	public static void main(String[] args) throws Exception {
		new CommandCenter();
	}

	private boolean contains(String[] list,String name) {
		for(int i=0;i<list.length;i++) {
			if(list[i].equals(name))
				return true;
		}
		return false;
	}

	private void updateGridInfo() {
		//boolean flag = false;
//		
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				gridWorld[i][j].setName(null);
				gridWorldInfo[i][j] =""; 
			}
	}
//		
//		for(int i=0;i<engine.getBuildings().size();i++) {
//			int x = engine.getBuildings().get(i).getLocation().getX();
//			int y = engine.getBuildings().get(i).getLocation().getY();
//			if(gridWorld[y][x].getName()==null) {
//				gridWorld[y][x].setName("NonEmpty");
//			}if(!(contains(gridWorldInfo[y][x].split(" "),"Building")))
//				gridWorldInfo[y][x]+="Building ";
//		}
//		for(int i=0;i<emergencyUnits.size();i++) {
//			int x = emergencyUnits.get(i).getLocation().getX();
//			int y = emergencyUnits.get(i).getLocation().getY();
//			JButton tmp =  gridWorld[y][x];
//			if(tmp.getName()==null) {
//				tmp.setName("NonEmpty");
//			}
//			if(emergencyUnits.get(i) instanceof Evacuator && !(contains(gridWorldInfo[y][x].split(" "),"Evacuator")))
//				gridWorldInfo[y][x]+="Evacuator ";
//			else if(emergencyUnits.get(i) instanceof FireTruck && !(contains(gridWorldInfo[y][x].split(" "),"FireTruck"))) 
//				gridWorldInfo[y][x]+="FireTruck ";
//			else if(emergencyUnits.get(i) instanceof Ambulance && !(contains(gridWorldInfo[y][x].split(" "),"Ambulance")))
//				gridWorldInfo[y][x]+="Ambulance ";
//			else if(emergencyUnits.get(i) instanceof GasControlUnit && !(contains(gridWorldInfo[y][x].split(" "),"GasControlUnit")))
//				gridWorldInfo[y][x]+="GasControlUnit ";
//			else if(emergencyUnits.get(i) instanceof DiseaseControlUnit && !(contains(gridWorldInfo[y][x].split(" "),"DiseaseControlUnit")))
//				gridWorldInfo[y][x]+="DiseaseControlUnit ";
//		}
//		for(int i=0;i<engine.getCitizens().size();i++) {
//			int x = engine.getCitizens().get(i).getLocation().getX();
//			int y = engine.getCitizens().get(i).getLocation().getY();
//			if(gridWorld[y][x].getName()==null)
//				gridWorld[y][x].setName("NonEmpty");
//			if(!(contains(gridWorldInfo[y][x].split(" "),"Citizen")))
//				gridWorldInfo[y][x]+="Citizen ";
//
//		}
//		for(int i=0;i<10;i++) {
//			for(int j=0;j<10;j++) {
//				if(gridWorld[i][j].getName()!=null && gridWorld[i][j].getName().equals("NonEmpty")  && gridWorldInfo[i][j].length()==0) {
//					gridWorld[i][j].setName(null);
//				}
//			}
//		}
		
		for(int i=0;i<engine.getBuildings().size();i++) {
			int x = engine.getBuildings().get(i).getLocation().getX();
			int y = engine.getBuildings().get(i).getLocation().getY();
			gridWorld[y][x].setName("NonEmpty");
			gridWorldInfo[y][x]+="Building ";
		}

		for(int i=0;i<engine.getCitizens().size();i++) {
			int x = engine.getCitizens().get(i).getLocation().getX();
			int y = engine.getCitizens().get(i).getLocation().getY();
			gridWorld[y][x].setName("NonEmpty");
			gridWorldInfo[y][x]+="Citizen ";
		}
		
		for(int i=0;i<emergencyUnits.size();i++) {
			int x = emergencyUnits.get(i).getLocation().getX();
			int y = emergencyUnits.get(i).getLocation().getY();
			JButton tmp =  gridWorld[y][x];
			tmp.setName("NonEmpty");
			if(emergencyUnits.get(i) instanceof Evacuator)
				gridWorldInfo[y][x]+="Evacuator ";
			else if(emergencyUnits.get(i) instanceof FireTruck) 
				gridWorldInfo[y][x]+="FireTruck ";
			else if(emergencyUnits.get(i) instanceof Ambulance)
				gridWorldInfo[y][x]+="Ambulance ";
			else if(emergencyUnits.get(i) instanceof GasControlUnit)
				gridWorldInfo[y][x]+="GasControlUnit ";
			else if(emergencyUnits.get(i) instanceof DiseaseControlUnit)
				gridWorldInfo[y][x]+="DiseaseControlUnit ";
		}

	}
}
