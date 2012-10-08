package Client;
import javax.swing.JTable;

public class FlasherThread extends Thread  
{  
  private JTable table;  
  int row = 0;  
  public FlasherThread(JTable jt)  
  {  
    table = jt;  
  }  
  public void flashRow(int i)  
  {  
    row = i;  
  }  
  public void run()  
  {  
	  
	  long start = System.currentTimeMillis();
	  boolean blink = true;
    while(blink)  
    {  
    	if(System.currentTimeMillis() - start >= 5000){
    		blink = false;
    	}
    	
      try  
      {  
if(table.getRowCount() > row)  
{  
  table.addRowSelectionInterval(row, row);  
  Thread.sleep(500);  
  table.removeRowSelectionInterval(row, row);  
  Thread.sleep(500);  
}  

      }  
      catch(Exception e)  
      {  
      }  
    }  
  }  
}  