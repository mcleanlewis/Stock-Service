//package stock_server;
//	import java.io.BufferedReader;
//	import java.io.FileNotFoundException;
//	import java.io.FileReader;
//	import java.io.IOException;
//	import java.util.ArrayList;
//import java.util.StringTokenizer;
//
//
//public class getCodes {
//
//
//	//CODE TAKEN FROM WEB!!
//
//
//		private String fileName;
//		//private BufferedReader br;
//		//private FileReader fr;
//		private ArrayList <String> symbols = new ArrayList<String>();
//		private ArrayList<LastStock> symbHist = new ArrayList<LastStock>();
//		
//		public getCodes(String FileName)
//		{
//			this.fileName=FileName;
//		}
//		
//		public void ReadFile()
//		{
//			try {
//				symbols.clear();//just in case this is the second call of the ReadFile Method./
//				//br = new FileReader("airports.txt");
//				BufferedReader br = new BufferedReader(new FileReader("stockcodes.txt"));
//			
//				StringTokenizer st = null;
//				int lineNumber = 0, tokenNumber = 0;
//	 
//				while( (fileName = br.readLine()) != null)
//				{	
//					fileName.toString();
//					lineNumber++;
//					//System.out.println(fileName);
//					
//					//break comma separated line using ","
//					st = new StringTokenizer(fileName, "/n");
//
//					
//						String token = st.nextToken();
//						symbHist.add(new LastStock(token));
//						
//							
//						
//							
//						
//					//reset token number
//					tokenNumber = 0;
//					
//				}
//				//fr.close();
//			} catch (FileNotFoundException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			
//		}
//		
//		
//		
//		//mutators and accesors 
//		public void setFileName(String newFileName)
//		{
//			this.fileName=newFileName;
//		}
//		public String getFileName()
//		{
//			return fileName;
//		}
//		public ArrayList getList()
//		{
//			System.out.println(symbHist.size());
//			return symbHist;
//		}
//		//public void displayArrayList()
//		//{
//		//	for(int x=0;x<this.storeValues.size();x++)
//		//	{
//		//		System.out.println(storeValues.get(x));
//		//	}
//		//}
//		
//	
//	
//	
//}
