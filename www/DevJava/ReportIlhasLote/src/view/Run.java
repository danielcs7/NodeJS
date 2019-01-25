package view;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import controller.CheckLog;
import controller.CheckSendMail;
import controller.GeraRelatDet;
import controller.GeraRelatFon;
import controller.GeraRelatorioCaligrafia;
import controller.GeraRelatorioCheckTxt;
import controller.GeraRelatorioLetras;
import controller.GeraRelatorioTxt;
import controller.RelatorioPedagogico;
import controller.ReportOvertime;
import controller.Sledgehammer;
import controller.WriteReport;

public class Run {

	private static String vRetornProc;
	private static String vProcesso;
	
	private static String vProcDet;
	private static String vProcPed;
	private static String vProcLet;
	private static String vProcFon;
	private static String vProcCali;
	
	private static String vCorrig;
	private static String vHrIni;
	private static String vHrFim;
	private static int vIdCheck;
	private static int vId;
	private static long vStart;
	private static long vEnd;
	
	static CheckLog cl = new CheckLog();
	static GeraRelatorioCheckTxt wl = new GeraRelatorioCheckTxt();
	static WriteReport wr = new WriteReport();
	
	
	public static String runTime(long vStart, long vEnd) {

		long tempo = vEnd - vStart;

		long segundos 	= tempo / 1000;
		long minutos 	= segundos / 60;
		segundos 		= segundos % 60;
		long horas 		= minutos / 60;
		minutos 		= minutos % 60;

		String tempo2 = String.format("%02d:%02d:%02d", horas, minutos,segundos); // Exemplo: "12:34:56"
		
		return tempo2;

	}
	
	public static void deleteLog() {

		//File arquivo = new File("/tmp/DETALHADOscriptreportlote.sql");

		//arquivo.delete();
		
		File pasta = new File("/tmp");    
		File[] arquivos = pasta.listFiles();    
		for(File arquivo : arquivos) {
		    if(arquivo.getName().endsWith("txt") || arquivo.getName().endsWith("sql") || arquivo.getName().endsWith("out")) {
		        arquivo.delete();
		    }
		}
		
		
	}
	
	
	public static void Hammer() throws SQLException {

				//"SLEDGEHAMMER", "DETALHADO", "PEDAGOGICO","LETRAS","FONEMA","CALIGRAFIA"
		
				//Aqui executo o SLEDGEHAMMER primeiramente, antes de rodar as Thread
				
				 // AQUI PEGO A DATA ATUAL DO SISTEMA
				 LocalDate dataManipulacao = LocalDate.now();
				 LocalTime time = LocalTime.now();
					 
				 vHrIni = String.valueOf(time);
				 vStart = System.currentTimeMillis();
				 
				vProcesso = "SLEDGEHAMMER";
				String vTxt = vProcesso+"|"+vHrIni;
				
				wr.writeReport(1, vTxt, "");
		
				try {
				Sledgehammer gr = new Sledgehammer();
				
				vRetornProc = gr.mallet();  
				
				}catch(Exception e) {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					String vTXt = vProcesso+" <----> "+sw.toString();
					wr.writeReport(12, vTXt, "");
					System.out.println(sw.toString());	
				}
					
				
				
				ReportOvertime ro = new ReportOvertime();
				ro.reportOvertime();
						
				vRetornProc = "0";
			
				cl.checkLog(vRetornProc, vProcesso,vId);
				
				 // AQUI PEGO A DATA ATUAL DO SISTEMA
				 LocalDate dataManipulacao2 = LocalDate.now();
				 LocalTime time2 = LocalTime.now();
					 
				 vHrFim = String.valueOf(time2);
				
				String vTxtx = vProcesso+"|"+vHrIni+"|"+vHrFim;
				
				vEnd = System.currentTimeMillis();
				String vTemp = runTime(vStart,vEnd);
				
				wr.writeReport(5, vTxtx, vTemp);

		
	}
	
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		int i =0;
		deleteLog();
		
		wr.writeReport(0, "", "");
		
		
		Hammer();
		
		
		for(i=0;i< 5000; i++) {
			//System.out.println(i);
		}
		
		
		
	
	//***************************************************************************************************
		
		
		new Thread(t1).start();
        new Thread(t2).start();
        new Thread(t3).start();
        new Thread(t4).start();
        new Thread(t5).start();
        
        
        
		

	}
	
	
	//***************************************************************************************************
	 private static Runnable t1 = new Runnable() {
	        public void run() {
	            try{
	            	
	            	vProcDet = "DETALHADO";
	            	 
	            		
	            	 
	            	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	            	 // AQUI PEGO A DATA ATUAL DO SISTEMA
					 LocalDate dataManipulacao = LocalDate.now();
					 LocalTime time = LocalTime.now();
						 
					 vHrIni = String.valueOf(time);
					 vStart = System.currentTimeMillis();
					 
					 String vDetIni = vProcDet+"|"+vHrIni;
						
					 wr.writeReport(1, vDetIni, "");
						
					//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	            	
	            	  GeraRelatDet gr = new GeraRelatDet();
					  vRetornProc = gr.vGeraRelat();
					
					  vRetornProc = "0";
				
					  cl.checkLog(vRetornProc, vProcDet,vId);
					
					 //◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
					  
					 LocalDate dataManipulacao2 = LocalDate.now();
					 LocalTime time2 = LocalTime.now();
						 
					 vHrFim = String.valueOf(time2);
					
					 String vDetFim = vProcDet+"|"+vHrIni+"|"+vHrFim;
					
					
					 vEnd = System.currentTimeMillis();
					 String vTemp = runTime(vStart,vEnd);
						
					 wr.writeReport(5, vDetFim, vTemp);
					//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
					
	            } catch (Exception e){
	            	
	            	StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					System.out.println(sw.toString());
					
	            	
	            }
	 
	        }
	    };
	 
	  //***************************************************************************************************
	    
	    private static Runnable t2 = new Runnable() {
	        public void run() {
	            try{
	            	
	            	vProcPed = "PEDAGOGICO";
	            	 
	            	
	            	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	            	 // AQUI PEGO A DATA ATUAL DO SISTEMA
	            	 LocalDate dataManipulacao = LocalDate.now();
					 LocalTime time = LocalTime.now();
						 
					 vHrIni = String.valueOf(time);
					 vStart = System.currentTimeMillis();
					 
					 String vPedIni = vProcPed+"|"+vHrIni;
						
					 wr.writeReport(1, vPedIni, "");
					
					//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
						
					 vHrIni = String.valueOf(time);
	            
	            	RelatorioPedagogico rp = new RelatorioPedagogico();
					vRetornProc = rp.vRelatPedag();
					
							
					vRetornProc = "0";
				
					cl.checkLog(vRetornProc, vProcPed,vId);
					
					// AQUI PEGO A DATA ATUAL DO SISTEMA
					 LocalDate dataManipulacao2 = LocalDate.now();
					 LocalTime time2 = LocalTime.now();
						 
					 vHrFim = String.valueOf(time2);
					
					 String vPedFim = vProcPed+"|"+vHrIni+"|"+vHrFim;
					 
					 vEnd = System.currentTimeMillis();
					 String vTemp = runTime(vStart,vEnd);
					
					wr.writeReport(5, vPedFim, vTemp);
					
					
	            } catch (Exception e){
	            	StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					System.out.println(sw.toString());
	            }
	       }
	    };
	    
	  //***************************************************************************************************
	    
	    private static Runnable t3 = new Runnable() {
	        public void run() {
	            try{
	            	
	            	vProcLet = "LETRAS";
	            	
	            
	            		
	            	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	            	 // AQUI PEGO A DATA ATUAL DO SISTEMA
	            	 LocalDate dataManipulacao = LocalDate.now();
					 LocalTime time = LocalTime.now();
						 
					 vHrIni = String.valueOf(time);
					 vStart = System.currentTimeMillis();
					 
					 String vLetrIni = vProcLet+"|"+vHrIni;
						
					 wr.writeReport(1, vLetrIni, "");
					
					//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
						 
					 
	            	GeraRelatorioLetras grl = new GeraRelatorioLetras();
					vRetornProc = grl.vGRL();
					
					
					vRetornProc = "0";
				
					cl.checkLog(vRetornProc, vProcLet,vId);
					
					// AQUI PEGO A DATA ATUAL DO SISTEMA
					 LocalDate dataManipulacao2 = LocalDate.now();
					 LocalTime time2 = LocalTime.now();
						 
					 vHrFim = String.valueOf(time2);
					
					String vLetFim = vProcLet+"|"+vHrIni+"|"+vHrFim;
					
					vEnd = System.currentTimeMillis();
					 String vTemp = runTime(vStart,vEnd);
					
					wr.writeReport(5, vLetFim, vTemp);
					
	            } catch (Exception e){
	            	StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					System.out.println(sw.toString());
	            }
	       }
	    };

	  //***************************************************************************************************
	    
	    private static Runnable t4 = new Runnable() {
	        public void run() {
	            try{
	            	
	            	vProcFon = "FONEMA";
	           
	            	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	            	 // AQUI PEGO A DATA ATUAL DO SISTEMA
	            	 LocalDate dataManipulacao = LocalDate.now();
					 LocalTime time = LocalTime.now();
						 
					 vHrIni = String.valueOf(time);
					 vStart = System.currentTimeMillis();
					 
					 String vFonIni = vProcFon+"|"+vHrIni;
						
					 wr.writeReport(1, vFonIni, "");
					
					//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
					 
	            	GeraRelatFon grf = new GeraRelatFon();
					vRetornProc = grf.vGRF();
					
					
						
					vRetornProc = "0";
				
					cl.checkLog(vRetornProc, vProcFon,vId);
					
					// AQUI PEGO A DATA ATUAL DO SISTEMA
					 LocalDate dataManipulacao2 = LocalDate.now();
					 LocalTime time2 = LocalTime.now();
						 
					 vHrFim = String.valueOf(time2);
					
					String vFonFim = vProcFon+"|"+vHrIni+"|"+vHrFim;
					
					vEnd = System.currentTimeMillis();
					 String vTemp = runTime(vStart,vEnd);
					
					wr.writeReport(5, vFonFim, vTemp);
					
	            } catch (Exception e){
	            	StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					System.out.println(sw.toString());
	            }
	       }
	    };
	    
	  //***************************************************************************************************	    
	    
	    private static Runnable t5 = new Runnable() {
	        public void run() {
	            try{
	            	
	            	vProcCali = "CALIGRAFIA";
	            	
	      
	            	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	            	 // AQUI PEGO A DATA ATUAL DO SISTEMA
	            	 LocalDate dataManipulacao = LocalDate.now();
					 LocalTime time = LocalTime.now();
						 
					 vHrIni = String.valueOf(time);
					 vStart = System.currentTimeMillis();
					 
					 String vCalIni = vProcCali+"|"+vHrIni;
						
					 wr.writeReport(1, vCalIni, "");
					
					//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
					 
	            	GeraRelatorioCaligrafia grc = new GeraRelatorioCaligrafia();
					vRetornProc = grc.vGRC();
								
					vRetornProc = "0";
				
					cl.checkLog(vRetornProc, vProcCali,vId);
					
					// AQUI PEGO A DATA ATUAL DO SISTEMA
					 LocalDate dataManipulacao2 = LocalDate.now();
					 LocalTime time2 = LocalTime.now();
						 
					 vHrFim = String.valueOf(time2);
					
					String vCalFim = vProcCali+"|"+vHrIni+"|"+vHrFim;
					
					vEnd = System.currentTimeMillis();
					String vTemp = runTime(vStart,vEnd);
					
					wr.writeReport(5, vCalFim, vTemp);
					
					
	            } catch (Exception e){
	            	StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);
					System.out.println(sw.toString());
	            }
	       }
	    };
	    
	    

}
