package stock_client;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.io.IOException;

import javax.swing.*;

public class NewSwingApp extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	DBconnect dbconn;Connection con;NewJDialog login;
	private JSeparator jSeparator1;
	private JSpinner jSpinnerQty;
	JTextArea jTextArea2;
	private JLabel jLabel1,jLabel2,jLabel3;
	private JButton jButton7,jButton8,jButton11, jButton12;
	private JSeparator jSeparator2;
	ArrayList<String> stockShortCodes, pNames, transactions;
	ListModel jList1Model,jList2Model, jList3Model;
	String selectedPortfolio, acType, broker;
	static NewSwingApp inst;
	
	
	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private JMenuItem helpMenuItem,exitMenuItem,openFileMenuItem,newFileMenuItem;
	private JMenu jMenu3,jMenu5;
	private JMenuBar jMenuBar1;
	private JPanel jPanel1;
	private JScrollPane jScrollPane1,jScrollPane2,jScrollPane3,jScrollPane4;
	private JList jList1,jList2,jList3;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				 inst = new NewSwingApp();
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
			}
		});
	}
	
	public NewSwingApp() {
		super();
		dbconn = new DBconnect();con = dbconn.getCon();
		
		//the 3 JLists
		pNames = new ArrayList <String>();stockShortCodes = new ArrayList <String>();transactions=new ArrayList <String>();
		
		//Populate the first JList (Portfolios) 
		ResultSet rs = runSQL("select J_PORTFOLIO.PORTFOLIONAME as PORTFOLIONAME, J_PORTFOLIO.USER_NAME as USER_NAME from J_PORTFOLIO WHERE J_PORTFOLIO.USER_NAME='" + NewJDialog.inst.uType + "'");
		

		try {
			while (rs.next()) {
		        String portfolio = rs.getString("PORTFOLIONAME");
		        pNames.add(portfolio);
			}
		
		} catch (SQLException e ) {
		    e.printStackTrace();
		} 
			
			String [] strArray = new String[pNames.size()];
			pNames.toArray(strArray);
			jList1Model = new DefaultComboBoxModel(strArray);
			initGUI();
	
			
	
	}
			

	
	private void initGUI() {
		
		try {
			
			{ 
				
				//position the GUI in the middle of the screen
				int height=600;
				int width=511;
				Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
				int x = (screen.width - width) / 2;
				int y = (screen.height - height) / 2;
				this.setBounds(x, y, 515, 447);
				  
		
				setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
				this.setTitle("DBMS Coursework\t\t\t[Logged in as " + NewJDialog.inst.uType+"]");
				this.setPreferredSize(new java.awt.Dimension(515, 447));
				{
					jPanel1 = new JPanel();
					getContentPane().add(jPanel1, BorderLayout.WEST);
					jPanel1.setLayout(null);
					jPanel1.setPreferredSize(new java.awt.Dimension(511, 382));
					{
						jScrollPane1 = new JScrollPane();
						jPanel1.add(jScrollPane1, "1, 0");
						jScrollPane1.setBounds(24, 30, 156, 74);
						{
							
							jList1 = new JList();
							jScrollPane1.setViewportView(jList1);
							jList1.setModel(jList1Model);
							jList1.setToolTipText("Click to select a portfolio");
							jList1.setPreferredSize(new java.awt.Dimension(153, 71));
							jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
							jList1.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent evt) {

									try {
										
										jList2.removeAll();
										
										selectedPortfolio=jList1.getSelectedValue().toString();
										ResultSet rs2 = runSQL("select PORTFOLIOSHARELISTING.PORTFOLIONAME as PORTFOLIONAME, PORTFOLIOSHARELISTING.SHORTNAME as SHORTNAME from PORTFOLIOSHARELISTING WHERE PORTFOLIONAME='" + selectedPortfolio + "'");

										while (rs2.next()) {
									        String share = rs2.getString("SHORTNAME");
									        stockShortCodes.add(share);
									   
										}
										
										if(stockShortCodes.isEmpty()){
											ListModel emptyModel = 
											new DefaultComboBoxModel(
													new String[] { "" });
											jList2.setModel(emptyModel);
											JOptionPane.showMessageDialog(null, "Your Portfolio is empty! " + jList2.getSelectedValue().toString(), "Oops!", JOptionPane.INFORMATION_MESSAGE , null);
											jList2.setEnabled(false);
											return;
										}
										
										String []strArray2 = new String[stockShortCodes.size()];
										stockShortCodes.toArray(strArray2);
										
										jList2.setEnabled(true);
										jList2Model = new DefaultComboBoxModel(strArray2);
										jList2.setModel(jList2Model);
										jList2.setToolTipText("Click to select a share");
										stockShortCodes.clear();
									
											jButton7.setVisible(true);
											jButton8.setVisible(true);
											jSpinnerQty.setVisible(true);
									    
										} catch (SQLException e ) {
									    	e.printStackTrace();
									    } 
								}
							});
						}
					}
					{
						jScrollPane2 = new JScrollPane();
						jPanel1.add(jScrollPane2, "2, 0");
						jScrollPane2.setBounds(185, 30, 149, 74);
						{
						
							jList2 = new JList();
							
							jScrollPane2.setViewportView(jList2);
							jList2.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent evt) {
									
									//query to show all the transactions related to the selected Share within the selected portfolio;
									ResultSet rs3 = runSQL("select J_TRANSACTIONS.TSHARE as TSHARE, J_TRANSACTIONS.PORTFOLIONAME as PORTFOLIONAME from J_TRANSACTIONS WHERE PORTFOLIONAME='" + selectedPortfolio.trim() + "' AND TSHARE='"+jList2.getSelectedValue().toString().trim()+"'");

									try {
							
										jList3.removeAll();
										while (rs3.next()) {	
									      String tshare = rs3.getString("TSHARE");
									      transactions.add(tshare);
										}
										
										if(transactions.isEmpty()){
											ListModel emptyModel = 
											new DefaultComboBoxModel(
													new String[] { "" });
											jList3.setModel(emptyModel);
											jTextArea2.setText("");
											JOptionPane.showMessageDialog(null, "There is no transaction for the share " + jList2.getSelectedValue().toString(), "Oops!", JOptionPane.INFORMATION_MESSAGE , null);
											jList3.setEnabled(false);
											return;
										}
										
										String[] transToArray = new String[transactions.size()];
										transactions.toArray(transToArray);
										jList3.setEnabled(true);
										jList3Model = new DefaultComboBoxModel(transToArray);
										jList3.setModel(jList3Model);
										transactions.clear();
									
										} catch (SQLException e ) {
									    	e.printStackTrace();
									    } 
								}
							});
						}
						
					}
					{
						jScrollPane3 = new JScrollPane();
						jPanel1.add(jScrollPane3, "3, 0");
						jScrollPane3.setBounds(339, 30, 150, 74);
						{
				
							jList3 = new JList();
							jScrollPane3.setViewportView(jList3);
							jList3.addMouseListener(new MouseAdapter() {
								public void mouseClicked(MouseEvent evt) {
									
									//query to show all the transactions related to the selected Share within the selected portfolio;
									ResultSet rs4 = runSQL("select J_TRANSACTIONS.TSHARE as TSHARE, J_TRANSACTIONS.PORTFOLIONAME as PORTFOLIONAME, J_TRANSACTIONS.SELLER as SELLER, J_TRANSACTIONS.BUYER as BUYER, J_TRANSACTIONS.BROKER_BROKERNAME as BROKER_BROKERNAME, J_TRANSACTIONS.EXCHANGE_NAME as EXCHANGE_NAME, J_TRANSACTIONS.AMOUNT as AMOUNT  from J_TRANSACTIONS WHERE PORTFOLIONAME='" + selectedPortfolio.trim() + "' AND TSHARE='"+jList2.getSelectedValue().toString().trim()+"'");
								
									try {
							
										jList3.removeAll();
										jTextArea2.setText("");
										//rs4.next();
										
										while (rs4.next()) {	
											String pname = rs4.getString("PORTFOLIONAME");
											String broker = rs4.getString("BROKER_BROKERNAME");
									      String tshare = rs4.getString("TSHARE");
									      
									      String seller = rs4.getString("SELLER");
									      String buyer = rs4.getString("BUYER");
									      
									      String exchange = rs4.getString("EXCHANGE_NAME");
									      String amount = rs4.getString("AMOUNT");
									      jTextArea2.append("Selected Portfolio: " + pname + "\t Your Broker is " + broker+"\n\n");
									      jTextArea2.append("Current Exchange: " + exchange + "\n" + tshare +" transaction details\n" + "\n\tSELLER: " + seller + "\n\tBUYER: " + buyer + "\n\tAMOUNT: " + amount);
									      jTextArea2.append("\n===========================================\n");
										}
										
										
									
										} catch (SQLException e ) {
									    	e.printStackTrace();
									    } 
								}
							});
						}
					}
					
					
					{
						//Buy button
						jButton7 = new JButton();
						jPanel1.add(jButton7);
						jButton7.setText("B");
						jButton7.setToolTipText("Buy");
						jButton7.setFont(new java.awt.Font("Arial",1,10));
						jButton7.setBounds(300, 110, 34, 19);
						jButton7.setVisible(false);
						jButton7.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								
										try {
											String broker="",exchange="";
											if(jList2.getSelectedValue()==null){
												jList2.setSelectedIndex(0);
											}
											ResultSet rs5 = runSQL("select J_TRANSACTIONS.TSHARE as TSHARE, J_TRANSACTIONS.PORTFOLIONAME as PORTFOLIONAME, J_TRANSACTIONS.BROKER_BROKERNAME as BROKER_BROKERNAME, J_TRANSACTIONS.EXCHANGE_NAME as EXCHANGE_NAME  from J_TRANSACTIONS WHERE PORTFOLIONAME='" + selectedPortfolio.trim() + "' AND TSHARE='"+jList2.getSelectedValue().toString().trim()+"'");
											while (rs5.next()) {	

										       exchange = rs5.getString("EXCHANGE_NAME");
										    
										     
											}
											ResultSet broQuery = runSQL("select J_USER.NAME, J_USER.BROKER_BROKERNAME FROM J_USER WHERE J_USER.NAME='"+NewJDialog.inst.uType+"'");
											while (broQuery.next()) {	
												
											   broker = broQuery.getString("BROKER_BROKERNAME");
										  
											}
											
											
											if(exchange.isEmpty()){
												System.out.println("test7");
												ResultSet rs55 = runSQL("SELECT EXCHANGEINDUSTRIES.EXCHANGE_NAME, EXCHANGEINDUSTRIES.INDUSTRY_INDUSTRYTYPE FROM EXCHANGEINDUSTRIES INNER JOIN J_SHARE ON(J_SHARE.INDUSTRY_INDUSTRYTYPE=EXCHANGEINDUSTRIES.INDUSTRY_INDUSTRYTYPE) WHERE J_SHARE.SHORTNAME='"+jList2.getSelectedValue().toString().trim()+"'");

												while (rs55.next()) {	
													
											
											       exchange = rs55.getString("EXCHANGE_NAME");
											       
											    
											     
												}
												
												
												System.out.println(exchange);
												if(exchange.isEmpty()){
													JOptionPane.showMessageDialog(null, "The Share does not exist in the database!","Error",JOptionPane.ERROR_MESSAGE);
													return;
												}
											}
											
											System.out.println("7 - broker is: " + broker);
											String st="INSERT INTO J_TRANSACTIONS(J_TRANSACTIONS.TSHARE,J_TRANSACTIONS.SELLER,J_TRANSACTIONS.BUYER,J_TRANSACTIONS.PORTFOLIONAME,J_TRANSACTIONS.BROKER_BROKERNAME,J_TRANSACTIONS.EXCHANGE_NAME,J_TRANSACTIONS.AMOUNT)VALUES('"+jList2.getSelectedValue().toString().trim()+"','you','me','"+selectedPortfolio.trim()+"','"+broker+"','"+exchange+"','"+jSpinnerQty.getValue().toString()+"')";
											Statement stmt = con.createStatement();
								            stmt.executeQuery(st);
								            jList2.requestFocus();
								            JOptionPane.showMessageDialog(null,"Purchase completed successfully","Buying...",JOptionPane.INFORMATION_MESSAGE);
								            
								           
								        } catch (Exception e) {
								        	JOptionPane.showMessageDialog(null,"xError: " + e.getMessage());
		
								            
								        }
							}
						});
					}
					
					{
						//Sell button
						jButton8 = new JButton();
						jPanel1.add(jButton8);
						jButton8.setText("S");
						jButton8.setToolTipText("Sell");
						jButton8.setFont(new java.awt.Font("Arial",1,10));
						jButton8.setBounds(185, 110, 35, 19);
						//jButton7.setBounds(300, 110, 34, 19);
						jButton8.setVisible(false);
						jButton8.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								
										try {
											String broker="",exchange="";
											if(jList2.getSelectedValue()==null){
												jList2.setSelectedIndex(0);
											}
											ResultSet rs5 = runSQL("select J_TRANSACTIONS.TSHARE as TSHARE, J_TRANSACTIONS.PORTFOLIONAME as PORTFOLIONAME, J_TRANSACTIONS.BROKER_BROKERNAME as BROKER_BROKERNAME, J_TRANSACTIONS.EXCHANGE_NAME as EXCHANGE_NAME  from J_TRANSACTIONS WHERE PORTFOLIONAME='" + selectedPortfolio.trim() + "' AND TSHARE='"+jList2.getSelectedValue().toString().trim()+"'");
											while (rs5.next()) {	
												
										       exchange = rs5.getString("EXCHANGE_NAME");
										    
										     
											}
											ResultSet broQuery = runSQL("select J_USER.NAME, J_USER.BROKER_BROKERNAME FROM J_USER WHERE J_USER.NAME='"+NewJDialog.inst.uType+"'");
											while (broQuery.next()) {	
												
											   broker = broQuery.getString("BROKER_BROKERNAME");
										  
											}
											
											if(exchange.isEmpty()){
												System.out.println("test8");
												ResultSet rs55 = runSQL("SELECT EXCHANGEINDUSTRIES.EXCHANGE_NAME, EXCHANGEINDUSTRIES.INDUSTRY_INDUSTRYTYPE FROM EXCHANGEINDUSTRIES INNER JOIN J_SHARE ON(J_SHARE.INDUSTRY_INDUSTRYTYPE=EXCHANGEINDUSTRIES.INDUSTRY_INDUSTRYTYPE) WHERE J_SHARE.SHORTNAME='"+jList2.getSelectedValue().toString().trim()+"'");

												while (rs55.next()) {	
													
											
											       exchange = rs55.getString("EXCHANGE_NAME");
											    
											     
												}
												if(exchange.isEmpty()){
													JOptionPane.showMessageDialog(null, "The Share does not exist in the database!","Error",JOptionPane.ERROR_MESSAGE);
													return;
												}
											
											}
											System.out.println("8 - broker is: " + broker);
											String st="INSERT INTO J_TRANSACTIONS(J_TRANSACTIONS.TSHARE,J_TRANSACTIONS.SELLER,J_TRANSACTIONS.BUYER,J_TRANSACTIONS.PORTFOLIONAME,J_TRANSACTIONS.BROKER_BROKERNAME,J_TRANSACTIONS.EXCHANGE_NAME,J_TRANSACTIONS.AMOUNT)VALUES('"+jList2.getSelectedValue().toString().trim()+"','me','you','"+selectedPortfolio.trim()+"','"+broker+"','"+exchange+"','"+jSpinnerQty.getValue().toString()+"')";
											Statement stmt = con.createStatement();
								            stmt.executeQuery(st);
								            JOptionPane.showMessageDialog(null,"Transaction completed successfully","Selling...",JOptionPane.INFORMATION_MESSAGE);
								           
								        } catch (Exception e) {
								        	JOptionPane.showMessageDialog(null,"Error: " + e.getMessage());
		
								            
								        }
								
							
								
							}
						});
					}
					{
						jButton11 = new JButton();
						jPanel1.add(jButton11);
						jButton11.setText("S H O W    L I V E     F E E D");
						jButton11.setToolTipText("Click to show the live feed");
						jButton11.setBounds(24, 345, 465, 25);
						jButton11.setFont(new java.awt.Font("Cooper Std",1,14));
						jButton11.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								
										try {
											
											QuoteClient ticker = new QuoteClient();
											ticker.start();
											
											
								        } catch (Exception e) {
								        	JOptionPane.showMessageDialog(null,"Error: " + e.getMessage());
		
								            
								        }
								
							
								
							}
						});
					}
					//*******
					{
						jButton12 = new JButton();
						jPanel1.add(jButton12);
						jButton12.setText("OPEN QUERIES");
						jButton12.setToolTipText("OPEN STANDARD QUERIES");
						jButton12.setBounds(24, 375, 465, 25);
						jButton12.setFont(new java.awt.Font("Cooper Std",1,14));
						jButton12.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								
										try {
											
											QueryThread query = new QueryThread();
											query.run();
											
											
								        } catch (Exception e) {
								        	JOptionPane.showMessageDialog(null,"Error: " + e.getMessage());
		
								            
								        }
								
							
								
							}
						});
					}
					
					
					//*****
					{
						jLabel1 = new JLabel();
						jPanel1.add(jLabel1);
						jLabel1.setText("(1) Select Your Portfolio");
						jLabel1.setBounds(30, 12, 139, 15);
					}
					{
						jLabel2 = new JLabel();
						jPanel1.add(jLabel2);
						jLabel2.setText("(2) Your Stocks");
						jLabel2.setBounds(200, 12, 93, 15);
					}
					{
						jLabel3 = new JLabel();
						jPanel1.add(jLabel3);
						jLabel3.setText("(3) Transaction History");
						jLabel3.setBounds(345, 12, 134, 15);
					}
					{
						jScrollPane4 = new JScrollPane();
						jPanel1.add(jScrollPane4);
						jScrollPane4.setBounds(24, 136, 465, 200);
						{
							jTextArea2 = new JTextArea();
							jScrollPane4.setViewportView(jTextArea2);
						}
					}
					{
					
						SpinnerNumberModel qty = new SpinnerNumberModel(1, 1, 999,1);
						jSpinnerQty = new JSpinner();
						jPanel1.add(jSpinnerQty);
						jSpinnerQty.setVisible(false);
						jSpinnerQty.setModel(qty);
						jSpinnerQty.setBounds(225, 110, 69, 19);
						
					}
					{
						jSeparator1 = new JSeparator();
						jPanel1.add(jSeparator1);
						jSeparator1.setBounds(24, 129, 465, 10);
					}

				}
			}
			this.setSize(515, 447);
			{
				jMenuBar1 = new JMenuBar();
				setJMenuBar(jMenuBar1);
				{
					jMenu3 = new JMenu();
					jMenuBar1.add(jMenu3);
					jMenu3.setText("File");
						{
							newFileMenuItem = new JMenuItem();
							jMenu3.add(newFileMenuItem);
							newFileMenuItem.setText("Input SQL");
							//only admins can see that
							if(!NewJDialog.inst.users.toString().contains(NewJDialog.inst.uType+"=ADMIN")){
								newFileMenuItem.setVisible(false);
							}
							newFileMenuItem.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent evt) {
									
											try {
												
												
											            Statement stmt = con.createStatement();		            
											            String st = JOptionPane.showInputDialog(null,"Please enter the SQL statement below");
											            ResultSet rs=runSQL(st);
											            String resultString="";
												            for(int i=1; i<rs.getFetchSize() && rs.next(); i++){
												            	
												            	resultString=resultString + " " + rs.getStatement();
												            }
											            JOptionPane.showMessageDialog(null,"Result: " + resultString);
											            stmt.executeQuery(st);

									        } catch (Exception e) {
									        	JOptionPane.showMessageDialog(null,"Error: " + e.getMessage());
			
									            
									        }
								}
							});
					}
					
					{
						openFileMenuItem = new JMenuItem();
						jMenu3.add(openFileMenuItem);
						openFileMenuItem.setText("Query Selector");
						openFileMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								QueryThread qt;
								try {
									qt = new QueryThread();
									qt.run();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								;
							}
						});

					}
					{
						jSeparator2 = new JSeparator();
						jMenu3.add(jSeparator2);
					}
					{
						exitMenuItem = new JMenuItem();
						jMenu3.add(exitMenuItem);
						exitMenuItem.setText("Logout");
						exitMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								
										try {
											
											if(JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "DBMS Coursework",
			                                      JOptionPane.YES_NO_OPTION) == 0){
												con.close();
												System.exit(0);
											}
	         
								        } catch (Exception e) {
								        	JOptionPane.showMessageDialog(null,"Error: " + e.getMessage());    
								        }
								}
						});
					}
				}
				{
					jMenu5 = new JMenu();
					jMenuBar1.add(jMenu5);
					jMenu5.setText("?");
					{
						helpMenuItem = new JMenuItem();
						jMenu5.add(helpMenuItem);
						helpMenuItem.setText("About this...");
						helpMenuItem.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent evt) {
								JOptionPane.showMessageDialog(null, "DBMS Coursework - Part 2\nDeveloped by:\nLewis Mclean\nAlessio Failla\nGraeme Tinsdale\nRachel McQueen\n\nLast Revision: 08/03/2011","About this...", JOptionPane.INFORMATION_MESSAGE);
							}
						});
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public ResultSet runSQL(String st){
		ResultSet rs=null;
		try {
			
            Statement stmt = con.createStatement();
            stmt.executeQuery(st);
            rs = stmt.executeQuery(st);
		}	
		catch (Exception e) {
			JOptionPane.showMessageDialog(null,"Error: " + e.getMessage());
    
		}
     
		return rs;	
	}
	
	public void setTextArea(String string){
		jTextArea2.append(string);
	}

}
