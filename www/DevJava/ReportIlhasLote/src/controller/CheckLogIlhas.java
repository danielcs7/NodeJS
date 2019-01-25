package controller;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;

import connectionfactory.Conexao;
import model.SeqTabelasRelatExec;
import model.TabRelatorioExec;
import sendmail.EnviaMailCheck;


public class CheckLogIlhas {
	
	private static long start;
	
	private static String vSeqTabela;
	private static String vTabExecucao;
	private static String vRetornProc;
	private static String vProcesso;
	private static String vCorrig;
	private static int vIdCheck;
	private static int vCount;
	private static int vCount2;
	
	private static String vProcess;
	private static String vChek;
	private static String vValidaProcHammer;
	private static String vValidaProcDet;
	private static String vValidaProcPed;
	private static String vValidaProcLetr;
	private static String vValidaProcFone;
	private static String vValidaProcCali;
	
	public static void variableClear() {

		vValidaProcHammer  = "0";
		vValidaProcDet	   = "0";
		vValidaProcPed	   = "0";
		vValidaProcLetr	   = "0";
		vValidaProcFone    = "0";
		vValidaProcCali    = "0";
		

	}
	
	
	
	
	public static void vProcessIn(String vProcess,String vChek,String vCorrig, int vId) throws SQLException, ParseException, IOException {
		
		//=========================================================================
		CheckLog cl = new CheckLog();
		GeraRelatorioCheckTxt wl = new GeraRelatorioCheckTxt();
		
		//=========================================================================
		
		
			
		switch (vProcess) {
		case "SLEDGEHAMMER":
			
			if(vChek.equals("N")) {
												
				Sledgehammer gr = new Sledgehammer();
				
				//RODA O SISTEMA NOVAMENTE
				
				vRetornProc = gr.mallet();  
				
				vProcesso = "SLEDGEHAMMER";
				
				//RODA A VERIFICACAO DO SLEDGEHAMMER
				
				ReportOvertime ro = new ReportOvertime();
				ro.reportOvertime();
				
				if(vCorrig.equals("Update")) {
					
					vRetornProc = "1";
			
				cl.checkLog(vRetornProc, vProcesso,vId);
		
				
				}else {
				
					cl.checkLog(vRetornProc, vProcesso,vId);
				}
				
				if(vRetornProc.equals("0")) {
					
					int vExc1 = 1;
					//wl.writeLog(vExc1, vProcesso, "");
					
					vValidaProcHammer = "1";
				
					
				}
			}						
			
			break;
		case "DETALHADO":
			
			if(vChek.equals("N")) {
				
				
				
				GeraRelatDet gr = new GeraRelatDet();
				vRetornProc = gr.vGeraRelat();
				
				vProcesso = "DETALHADO";
				
				if(vCorrig.equals("Update")) {
					
					vRetornProc = "1";
			
				cl.checkLog(vRetornProc, vProcesso,vId);
				
				}else {
				
					cl.checkLog(vRetornProc, vProcesso,vId);
				}
				
				if(vRetornProc.equals("0")) {
					
					int vExc1 = 1;
					//wl.writeLog(vExc1, vProcesso, "");
					
					vValidaProcDet = "1";
					
					
				}
				
			}
								
								
			break;
		case "PEDAGOGICO":
			if(vChek.equals("N")) {
				
				
				
				RelatorioPedagogico rp = new RelatorioPedagogico();
				vRetornProc = rp.vRelatPedag();
				
				vProcesso = "PEDAGOGICO";
						
				if(vCorrig.equals("Update")) {
					
					vRetornProc = "1";
			
				cl.checkLog(vRetornProc, vProcesso,vId);
				
				}else {
				
					cl.checkLog(vRetornProc, vProcesso,vId);
				}
				
				if(vRetornProc.equals("0")) {
					
					int vExc1 = 1;
					//wl.writeLog(vExc1, vProcesso, "");
					

					vValidaProcPed = "1";
				}
				
			}
			
			
			break;
		case "LETRAS":
			
				if(vChek.equals("N")) {
				
				
					
				GeraRelatorioLetras grl = new GeraRelatorioLetras();
				vRetornProc = grl.vGRL();
				
				vProcesso = "LETRAS";
				
				if(vCorrig.equals("Update")) {
					
					vRetornProc = "1";
			
				cl.checkLog(vRetornProc, vProcesso,vId);
				
				}else {
				
					cl.checkLog(vRetornProc, vProcesso,vId);
				}
				
				
				if(vRetornProc.equals("0")) {
					
					int vExc1 = 1;
					//wl.writeLog(vExc1, vProcesso, "");
					

					vValidaProcLetr = "1";
					
					
				}
				
			}
			
			
			
			break;
		case "FONEMA":
			
			if(vChek.equals("N")) {
				
			
				
				GeraRelatFon grf = new GeraRelatFon();
				vRetornProc = grf.vGRF();
				
				vProcesso = "FONEMA";
					
				if(vCorrig.equals("Update")) {
					
					vRetornProc = "1";
			
				cl.checkLog(vRetornProc, vProcesso,vId);
				
				}else {
				
					cl.checkLog(vRetornProc, vProcesso,vId);
				}
				
				
				if(vRetornProc.equals("0")) {
					
					int vExc1 = 1;
					//wl.writeLog(vExc1, vProcesso, "");
					
					vValidaProcFone = "1";
					
					
					
				}
				
			}

			
			break;
		case "CALIGRAFIA":
			
			if(vChek.equals("N")) {
				
				
				
				GeraRelatorioCaligrafia grc = new GeraRelatorioCaligrafia();
				vRetornProc = grc.vGRC();
				
				vProcesso = "CALIGRAFIA";
				
				if(vCorrig.equals("Update")) {
					
					vRetornProc = "1";
			
				cl.checkLog(vRetornProc, vProcesso,vId);
				
				}else {
				
					cl.checkLog(vRetornProc, vProcesso,vId);
				}
				
				
				if(vRetornProc.equals("0")) {
					
					int vExc1 = 1;
					//wl.writeLog(vExc1, vProcesso, "");
					
					vValidaProcCali = "1";
					
							
					
				}
				
			}
			
			
			break;
		
		default:
			System.out.printf("Processo foi executado com sucesso");

		}
		
	}
		
	
	

	
	public void vCheck() throws ParseException, SQLException, IOException {
		
		
				// ABRE CONEXAO COM O BANCO DE DADOS
				Conexao conProd = new Conexao();  
				//=========================================================================
				
				Statement stmt = conProd.connection.createStatement();
				
				
				
				
				//=========================================================================				
				//ENUM TRAZENDO A TABELA E A SEQUENCE
				TabRelatorioExec  tabExc = TabRelatorioExec.TAB6;
				vTabExecucao = tabExc.getTabExecucao();
				
				SeqTabelasRelatExec seqTab = SeqTabelasRelatExec.SEQ6;
				vSeqTabela = seqTab.getSeqTabela();

				//=========================================================================				
				variableClear();
				
				//=========================================================================
				//PEGA OS DADOS DE ESCOLA , MUNICIPIO , PROFESSOR E TURMA DO ALUNO
				
								   
				//String[] toppings = {"SLEDGEHAMMER", "DETALHADO", "PEDAGOGICO","LETRAS","FONEMA","CALIGRAFIA"};
				String[] toppings = {"SLEDGEHAMMER", "DETALHADO"};
				
				  for (String s: toppings) {
			          System.out.println(s);
			          vProcess = s;

			        
							vChek = "N";
							vCorrig = "insert";
							
							vProcessIn(vProcess,vChek,vCorrig,1);
	
									
							
		          } 
						
					
						
						if (vValidaProcHammer.equals("1") || vValidaProcDet.equals("1") || vValidaProcPed.equals("1") || vValidaProcLetr.equals("1") || vValidaProcFone.equals("1") || vValidaProcCali.equals("1")) {
							
							int vExc2 = 2;
							
							//EnviaMailCheck emck = new EnviaMailCheck();
							
							//emck.sendMail();

						}
						
						
					
			

				  conProd.close();	
					
	}

}
