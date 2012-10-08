package Client;
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 



/*
 * SimpleTableDemo.java requires no other files.
 */

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class SimpleTableDemo extends AbstractTableModel implements TableCellRenderer {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean DEBUG = false;
    
    int numRows;
    int numCols;
    Object[][] data = new Object [400][5];
    long [] timer = new long[400];
    int count;
    ArrayList<Integer> filled;ArrayList<LastStock> symb;String type="";
    final JTable table;
    
    
    //table cell renderer
    Border unselectedBorder = null;
    Border selectedBorder = null;
    boolean isBordered = true;
    int last = 0;
    

    public SimpleTableDemo() {
        super();

        filled = new ArrayList<Integer>();
        jumble();
        
        String[] columnNames = {"Stock",
                                "Price",
                                "Ask",
                                "Bid",
                                "Status"};

        

        table = new JTable(data, columnNames){
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);

				//  Color row based on a cell value

				if (data[row][4]== null){
					
						//maybe need to transpose this to other methods
						//timer[row] = System.currentTimeMillis();
						c.setBackground(getBackground());
						int modelRow = convertRowIndexToModel(row);
						type = (String)getModel().getValueAt(modelRow, 0);
						c.setBackground(Color.BLACK);
					}
					
				else{
				if (data[row][4].equals("Increasing") ||data[row][4].equals("Decreasing") || System.currentTimeMillis() - timer[row] >= 3000)
				{
					//maybe need to transpose this to other methods
					//timer[row] = System.currentTimeMillis();
					c.setBackground(getBackground());
					int modelRow = convertRowIndexToModel(row);
					type = (String)getModel().getValueAt(modelRow, 0);
					c.setBackground(Color.BLACK);
				}
				
				
				
				if (data[row][4].equals("Decreasing") && System.currentTimeMillis() - timer[row] <= 3000)
				{
					//timer[row] = System.currentTimeMillis();
					c.setBackground(getBackground());
					int modelRow = convertRowIndexToModel(row);
					type = (String)getModel().getValueAt(modelRow, 0);
					c.setBackground(Color.RED);
					
				}
				
				
				if (data[row][4].equals("Increasing")&& System.currentTimeMillis() - timer[row] <= 3000)
				{
					//timer[row] = System.currentTimeMillis();
					c.setBackground(getBackground());
					int modelRow = convertRowIndexToModel(row);
				    type = (String)getModel().getValueAt(modelRow, 0);
					c.setBackground(Color.GREEN);
					
					}
				
				}
				last =  -1;
				return c;
			}
		};
        table.setPreferredScrollableViewportSize(new Dimension(800, 600));
        table.setFillsViewportHeight(true);
        table.setBackground(Color.black);
        table.setGridColor(Color.black);
        table.setForeground(Color.gray);
        
        //table.getModel().addTableModelListener(this);

        if (DEBUG) {
            table.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    printDebugData(table);
                }
            });
        }

        //Create the scroll pane and add the table to it.
        //JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        //add(scrollPane);
    }

    private void printDebugData(JTable table) {
        numRows = table.getRowCount();
        numCols = table.getColumnCount();
        javax.swing.table.TableModel model = table.getModel();

        System.out.println("Value of data: ");
        for (int i=0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j=0; j < numCols; j++) {
                System.out.print("  " + model.getValueAt(i, j));
            }
            System.out.println();
        }
        System.out.println("--------------------------");
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     * @throws IOException 
     */
    public void createAndShowGUI() throws IOException {
        //Create and set up the window.
    	
        JFrame frame = new JFrame("Live Stock Display");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane);
        //Create and set up the content pane.
        //SimpleTableDemo newContentPane = new SimpleTableDemo();
        panel.setOpaque(true); //content panes must be opaque
        frame.setContentPane(panel);

        //Display the window.
        frame.pack();
		int height=600;
		int width=800;
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (screen.width - width) / 2;
		int y = (screen.height - height) / 2;
		frame.setBounds(x, y, 830, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
       
    }
    
    
    	
    	
    	
    	
    	
    		
    	private void jumble(){
    		
    		for(int i = 0;i<400;i++){
    			
    			filled.add(Integer.valueOf(i));
    			
    		}
    		Collections.shuffle(filled);
    		
    		for(int p =0;p<filled.size();p++){
    			System.out.println(filled.get(p));
    			
    		}
    	}	
    	
    public void updatePrice(String received){
    	
    	numRows = table.getRowCount();
        numCols = table.getColumnCount();
    	
    	System.out.println(numRows);
    	
    	String tempArray [] = received.split(",");
    	String symb = tempArray[0];
    	Float price = Float.valueOf(tempArray[2]);
    	Float bid = Float.valueOf(tempArray[4]);
    	Float ask = Float.valueOf(tempArray[3]);
    	
    	boolean found = false;
    	
    	for(int i= 0; i<400;i++){
    		
    		
    			
    		if(data[i][0] != null && data[i][0].equals(symb)){
    			 
    			found = true;
    			if((Float)data[i][2]<bid || (Float)data[i][1]< price){
    			
    			table.setValueAt(price,i ,1);
        		table.setValueAt(bid,i ,2);
        		table.setValueAt(ask,i ,3);
        		table.setValueAt("Increasing",i ,4);
    			
        		timer [i] = System.currentTimeMillis();
    			
    			//model.setRowColour(1, Color.RED);
    			
    			FlasherThread blink = new FlasherThread(table);
    			blink.flashRow(i);
    			blink.start();
    			}
    			
    			if((Float)data[i][2]>bid || (Float)data[i][1]> price){
        			
        			table.setValueAt(price,i ,1);
            		table.setValueAt(bid,i ,2);
            		table.setValueAt(ask,i ,3);
            		table.setValueAt("Decreasing",i ,4);
        			
            		timer [i] = System.currentTimeMillis();
        			
        			//model.setRowColour(1, Color.RED);
        			
        			FlasherThread blink = new FlasherThread(table);
        			blink.flashRow(i);
        			blink.start();
        			}
    		}
    	}
    	
    		
    	if(!found){
    		
    		int insert = filled.get(count);
    		
    		
    		table.setValueAt(symb,insert ,0);
    		table.setValueAt(price,insert ,1);
    		table.setValueAt(bid,insert ,2);
    		table.setValueAt(ask,insert ,3);
    		table.setValueAt("Loaded",insert ,4);
    		
    		timer [insert] = System.currentTimeMillis();
    		
    		count++;
    		
    		FlasherThread blink = new FlasherThread(table);
    		blink.flashRow(insert);
			blink.start();
			found = false;
    	
    	
    		}
    	
    	}
    

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public boolean isCellEditable(int row, int column){
		return false;
		
		
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Component getTableCellRendererComponent(JTable arg0, Object arg1,
			boolean arg2, boolean arg3, int arg4, int arg5) {
		// TODO Auto-generated method stub
		return null;
	}

	
    
  
}

