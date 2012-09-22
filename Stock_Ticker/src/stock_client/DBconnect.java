package stock_client;
//package server;
//	import java.sql.Connection;
//	import java.sql.SQLException;
//	import oracle.jdbc.pool.OracleDataSource;
//	
//	import java.sql.ResultSet;
//	
//	import java.sql.Statement;
//	import java.io.BufferedWriter;
//	import java.io.FileWriter;
//	
//	//this is the class that administrates the connection to the oracle DB, should you need to change the 
//	// database, the URL is where you change the address.
//	// it retrun the connection for use by other classes by getConn() method
//	
//
//public class DBconnect {
//	
//	Connection con;
//
//	  public DBconnect() {
//	    con = null;
//	    try {
//	      OracleDataSource ods = new OracleDataSource();
//	      ods.setURL("jdbc:oracle:thin:java/java@//46.137.111.155:1521/XE"); //updated ip
//	      con = ods.getConnection();
//	    }
//	    catch (Exception x) {
//	      x.printStackTrace();
//	    }
//	    
//	    
//	  }
//	  
//	  public Connection getCon(){
//		  
//		  return con;
//	  }
//	  
//	  //For the "Export Data" menu
//	  //To Comment...
//	  public void exportData(Connection con,String filename) {
//	        Statement stmt;
//	        String query;
//	        String theData="";
//	        try {
//	            stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
//	                    ResultSet.CONCUR_UPDATABLE);
//	            
//	            //For comma separated file - CSV
//	            query="select J_USER.NAME as NAME, J_USER.PASSWORD as PASSWORD from J_USER";
//	           //query = "SELECT * into OUTFILE  '"+filename+
//	          //          "' FIELDS TERMINATED BY ',' FROM JAVA_USER";
//	            stmt.executeQuery(query);
//	            Statement s;
//	    		
//	    			
//	    			System.out.println(query);
//	    			s = con.createStatement();
//	    			ResultSet rs = s.executeQuery(query);
//	    			
//	    			
//	    			//build up a string to write out to a file
//	    			while (rs.next()) {
//	    		         
//	    		        	theData+=rs.getString("NAME") + ";" + rs.getString("PASSWORD") + "\n";
//	    		     }
//	    		       
//	    			BufferedWriter out = new BufferedWriter(new FileWriter(filename));
//	    			out.write(theData);
//	    			out.close();
//	    			//System.out.println(theData); 
//	    			//close the query/statement
//	    			s.close();
//	    		 
//	    			
//	    			
//	        } catch(Exception e) {
//	            e.printStackTrace();
//	            stmt = null;
//	        }
//	        
//	    }
//	    
//	    
//	    public void closeCon(){
//	      if (con != null) {
//	        try {
//	          con.close();
//	        }
//	        catch (SQLException sqlEx) {
//	          System.err.println("Failed to close [database] connection.");
//	        }
//	      }
//	    }
//	  }
//	
//	
//	
//
