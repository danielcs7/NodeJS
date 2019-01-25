package controller;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import connectionfactory.Conexao;
import model.SeqTabelasRelatExec;
import model.TabRelatorioExec;

public class WriteTable {
	
	private static String vTabExecucao;
	private static String vSeqTabela;
	private static String dtIni;
	
	public void writeLog(String vCheck,String vProcess) throws SQLException {
			
			//CONECTANDO AO BD
			//===================================================================================
			
			Conexao conProd = new Conexao();  
			
			//===================================================================================
			//ENUM TRAZENDO A TABELA E A SEQUENCE
			TabRelatorioExec  tabExc = TabRelatorioExec.TAB6;
			vTabExecucao = tabExc.getTabExecucao();
			
			SeqTabelasRelatExec seqTab = SeqTabelasRelatExec.SEQ6;
			vSeqTabela = seqTab.getSeqTabela();
			
			//===================================================================================
						
				// AQUI PEGO A DATA ATUAL DO SISTEMA
				LocalDate dataManipulacao = LocalDate.now();
				System.out.println("Data :" + dataManipulacao);
				
				LocalTime time = LocalTime.now();
				System.out.println("Hora atual: "+time);
				    
				dtIni = String.valueOf(dataManipulacao);
				
			//=========================================================================


			
			if(vCheck.equals("1")) {
				
				System.out.println("RETORNO COM ERRO ...!");
				//SE ESTIVER COM ERRO IRÁ INSERIR COMO "N"
				Statement stmtInsertTable = conProd.connection.createStatement();
				
				String sql2 = "INSERT INTO report.tb_check_log_ilhas (ID,DATA,PROCESS,CHEK) VALUES (nextval('"+vSeqTabela+"'),'" + dtIni + "','"+vProcess+"','N')";

				stmtInsertTable.executeUpdate(sql2);
				stmtInsertTable.close();

				
			}else {
				
				System.out.println("RETORNO COM SUCESSO ...!");
				//SE ESTIVER COM ERRO IRÁ INSERIR COMO "S"				
				Statement stmtInsertTable = conProd.connection.createStatement();
				
				String sql2 = "INSERT INTO report.tb_check_log_ilhas (ID,DATA,PROCESS,CHEK) VALUES (nextval('"+vSeqTabela+"'),'" + dtIni + "','"+vProcess+"','S')";

				stmtInsertTable.executeUpdate(sql2);
				stmtInsertTable.close();
				
			}
			
			conProd.close();
			
		}
	}




