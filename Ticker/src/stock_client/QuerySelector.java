package stock_client;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import javax.swing.WindowConstants;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class QuerySelector extends javax.swing.JFrame  {
	/**
	 * 
	 */

	private static final long serialVersionUID = 2506595431334939435L;
	private JPanel jPanel1;
	private JLabel jLabel4;
	private JLabel jLabel5;
	private JTextArea jTextArea1;
	private JScrollPane jScrollPane1;
	private JSeparator jSeparator1;
	private JButton jButton1;
	private JLabel jLabel1;
	private JSeparator jSeparator2;
	private JLabel jLabel6;
	private JPanel jPanel2;
	private JComboBox jComboBox1;
	DBconnect dbconn;Connection con;AccessingXmlFile axf;
	private JButton jButton3;
	private JButton jButton2;

	public QuerySelector() {
		super();
		initGUI();
		axf = new AccessingXmlFile();
		String path = "quries.xml";
		URL url = this.getClass().getResource(path);
		axf.parseQueries(url.getPath());
		
		for(String query : axf.ht.keySet()){
			jComboBox1.addItem(query);
		}
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Query Selector");
			this.setResizable(false);
			{
				jPanel1 = new JPanel();
				getContentPane().add(jPanel1, BorderLayout.WEST);
				jPanel1.setLayout(null);
				jPanel1.setPreferredSize(new java.awt.Dimension(398, 367));
				{
					
					jComboBox1 = new JComboBox();
					jPanel1.add(jComboBox1);
					jComboBox1.setBounds(12, 136, 265, 22);
				}
				{
					jPanel2 = new JPanel();
					jPanel1.add(jPanel2);
					jPanel2.setBounds(0, 0, 398, 78);
					jPanel2.setBackground(new java.awt.Color(98,25,246));
					jPanel2.setLayout(null);
					jPanel2.setEnabled(false);
					
					{
						jLabel6 = new JLabel();
						jPanel2.add(jLabel6);
						jLabel6.setText("Welcome to Query Selector ");
						jLabel6.setFont(new java.awt.Font("Dialog",1,18));
						jLabel6.setBounds(12, 4, 354, 31);
						jLabel6.setForeground(new java.awt.Color(254,255,254));
					}
					{
						jLabel5 = new JLabel();
						jPanel2.add(jLabel5);
						jLabel5.setText(" Please select the query to run from the drop down menu below");
						jLabel5.setBounds(4, 41, 386, 15);
						jLabel5.setForeground(new java.awt.Color(254,255,254));
					}
					{
						jLabel4 = new JLabel();
						jPanel2.add(jLabel4);
						jLabel4.setText(" and click on \"Execute\" to see the output!");
						jLabel4.setBounds(4, 56, 354, 15);
						jLabel4.setForeground(new java.awt.Color(254,255,254));
					}
					{
						jSeparator2 = new JSeparator();
						jPanel2.add(jSeparator2);
						jSeparator2.setBounds(4, 35, 386, 10);
					}
				}
				{
					jLabel1 = new JLabel();
					jPanel1.add(jLabel1);
					jLabel1.setText("Select the query from the menu below");
					jLabel1.setBounds(12, 121, 265, 15);
					jLabel1.setFont(new java.awt.Font("Dialog",1,12));
				}
				{
					jButton1 = new JButton();
					jPanel1.add(jButton1);
					jButton1.setText("Execute");
					jButton1.setBounds(289, 84, 90, 75);
					jButton1.setVerticalTextPosition(SwingConstants.BOTTOM);
					jButton1.setHorizontalTextPosition(SwingConstants.CENTER);
					String path = "runIcon.png";
					URL url = this.getClass().getResource(path);
					ImageIcon icon = new ImageIcon(url);
					Icon screenicon = icon;
					jButton1.setIcon(screenicon);
					jButton1.setFont(new java.awt.Font("Dialog",1,12));
					jButton1.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dbconn = new DBconnect();con = dbconn.getCon();
							Statement s;
							try {
								s = con.createStatement();
								ResultSet rs = s.executeQuery(axf.ht.get(jComboBox1.getSelectedItem().toString()).toString());
								ResultSetMetaData rsmd = rs.getMetaData();

							    int numCols = rsmd.getColumnCount();
							    
							    jTextArea1.append("Results for query: " + axf.ht.get(jComboBox1.getSelectedItem().toString()).toString()+"\n");
								while (rs.next()) {
									jTextArea1.append("====================================\n");
									for(int i=1; i<numCols; i++){
									
										jTextArea1.append(("Line  " + i +" - " + rsmd.getColumnName(i)+ ": " + rs.getString(i)+"\n"));
									}
									jTextArea1.append("\n");
					
							        
								}
								
								s.close();
								
							} catch (SQLException e) {
								
								e.printStackTrace();
							}
						
							
						}
					});
				}
				{
					jSeparator1 = new JSeparator();
					jPanel1.add(jSeparator1);
					jSeparator1.setBounds(12, 173, 367, 10);
				}
				{
					jScrollPane1 = new JScrollPane();
					jPanel1.add(jScrollPane1);
					jScrollPane1.setBounds(12, 182, 367, 130);
					jScrollPane1.setBorder(BorderFactory.createTitledBorder("Output"));
					{
						jTextArea1 = new JTextArea();
						jScrollPane1.setViewportView(jTextArea1);
					}
				}
				{
					jButton2 = new JButton();
					jPanel1.add(jButton2);
					jButton2.setText("< Back");
					jButton2.setBounds(12, 324, 131, 22);
					jButton2.setFont(new java.awt.Font("Dialog",1,12));
					jButton2.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							dispose();
						}
					});
				}
				{
					jButton3 = new JButton();
					jPanel1.add(jButton3);
					jButton3.setText("Clear Output");
					jButton3.setBounds(223, 324, 156, 22);
					jButton3.setFont(new java.awt.Font("Dialog",1,12));
					jButton3.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent evt) {
							jTextArea1.setText("");
						}
					});
				}
			}
			pack();
			this.setSize(395, 388);
		} catch (Exception e) {
		   
			e.printStackTrace();
		}
	}

}
