package stock_client;

import com.jgoodies.forms.layout.CellConstraints;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JPasswordField;


public class NewJDialog extends javax.swing.JDialog {
	
	private static final long serialVersionUID = 1L;
	DBconnect dbconn;Connection con;String uType;int tries=0;
	private JLabel jLabel1,jLabel2;
	private JButton jButton1, jButton2;
	private JPasswordField jFormattedTextField1;
	private JTextField jTextField1;
	Hashtable<String, String> logins, users;
	static NewJDialog inst;NewSwingApp app;
	
	{
		//Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame();
				inst = new NewJDialog(frame);
				inst.setVisible(true);
			}
		});
	}
	
	public NewJDialog(JFrame frame) {
		super(frame);
		dbconn = new DBconnect();con = dbconn.getCon();
		
		logins=new Hashtable<String, String>();users=new Hashtable<String, String>();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String query = "select J_USER.NAME as NAME, J_USER.PASSWORD as PASSWORD, J_USER.ACCOUNTTYPE as ACCOUNTTYPE from J_USER";
		
		Statement s;
		try {
			
			s = con.createStatement();
			ResultSet rs = s.executeQuery(query);
			
			while (rs.next()) {
				
		        String username = rs.getString("NAME");String pass=rs.getString("PASSWORD");
		        logins.put(username, pass);users.put(username, rs.getString("ACCOUNTTYPE"));
		        
			}
			
			s.close();
		 
		} catch (SQLException e ) {
		    e.printStackTrace();
		} 
			
		initGUI();
		
	}
	
	
	private void initGUI() {
		try {
			{
				int width = 268;
			    int height = 105;
			    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			    int x = (screen.width - width) / 2;
			    int y = (screen.height - height) / 2;
			    this.setBounds(x, y, 268, 105);
			    
				getContentPane().setLayout(null);
				this.setTitle("DBMS Coursework - Login");
				this.setResizable(false);
				this.setLocationByPlatform(true);
				{
					jLabel1 = new JLabel();
					getContentPane().add(jLabel1, new CellConstraints("1, 1, 1, 1, default, default"));
					jLabel1.setText("UserName:");
					jLabel1.setBounds(8, 9, 68, 15);
				}
				{
					jLabel2 = new JLabel();
					getContentPane().add(jLabel2, new CellConstraints("1, 2, 1, 1, default, default"));
					jLabel2.setText("Password:");
					jLabel2.setBounds(13, 29, 68, 15);
				}
				{
					jTextField1 = new JTextField();
					getContentPane().add(jTextField1, new CellConstraints("4, 1, 1, 1, default, default"));
					jTextField1.setBounds(76, 7, 185, 19);
					
				}
				{
					jFormattedTextField1 = new JPasswordField();
					getContentPane().add(jFormattedTextField1, new CellConstraints("4, 2, 1, 1, default, default"));
					jFormattedTextField1.setBounds(76, 27, 185, 18);
					jFormattedTextField1.getPassword();
					
				}
				{
					jButton1 = new JButton();
					getContentPane().add(jButton1);
					jButton1.setText("Login");
					jButton1.setBounds(12, 51, 115, 25);
					jButton1.addActionListener(new ActionListener() {
						@SuppressWarnings("deprecation")
						public void actionPerformed(ActionEvent evt) {
							
							try {
								//login condition
								boolean loggedin=false;
								Enumeration<String> e = logins.keys();
						         while(e.hasMoreElements())
						         { 
						             String user = (String)(e.nextElement());
						             String pass = (String)logins.get(user);
						      
										if(jTextField1.getText().trim().matches(user.trim()) && jFormattedTextField1.getText().matches(pass.trim()) && tries<3*logins.size() && jTextField1.getText().length()>1 && jFormattedTextField1.getText().length()>1 ){
												uType=jTextField1.getText().trim();
													if(NewJDialog.inst.users.toString().contains(NewJDialog.inst.uType+"=BLOCKED")){
														JOptionPane.showMessageDialog(null,"Your account has been frozen. The application will now be terminated.","Account Frozen", JOptionPane.INFORMATION_MESSAGE );
														System.exit(0);
													}
												tries=0;
												loggedin=true;
												inst.setVisible(false);
												app=new NewSwingApp();
												app.setVisible(true);
												break;
								
										}else{
											tries++;
										}
										if(tries>3*logins.size()){  //3 * logins.size due to the while loop!
												
												Statement s;
												s = con.createStatement();
												String query= "UPDATE J_USER set J_USER.ACCOUNTTYPE='BLOCKED' WHERE J_USER.NAME='" + jTextField1.getText().trim() + "'";
												s.executeQuery(query);
												JOptionPane.showMessageDialog(null,"You are trying to hack me! Your account has been frozen, please contact customer services.  " );
												System.exit(0);
												
										}
									
						          }
						         if(loggedin==false){
						        	 JOptionPane.showMessageDialog(null,"You have entered an inccorect login.","Error",JOptionPane.ERROR_MESSAGE);
						         }
						         
					        } catch (Exception e) {
					        	JOptionPane.showMessageDialog(null,"Error: " + e.getMessage());

					            
					        }
					        
							
						}
					});
				}
				{
					jButton2 = new JButton();
					getContentPane().add(jButton2);
					jButton2.setText("Exit");
					jButton2.setBounds(147, 51, 115, 25);
					jButton2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							
							try {
								
								if(JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "DBMS Coursework",
                                      JOptionPane.YES_NO_OPTION) == 0){
									System.exit(0);
								}
					          
					        } catch (Exception e) {
					        	JOptionPane.showMessageDialog(null,"Error: " + e.getMessage());

					            
					        }
							
						}
					});
				}

				{
					
					
				}
			}
			this.setSize(268, 105);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
