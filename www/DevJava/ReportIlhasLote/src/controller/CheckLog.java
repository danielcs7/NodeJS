package controller;

import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import connectionfactory.Conexao;
import model.SeqTabelasRelatExec;
import model.TabRelatorioExec;

public class CheckLog {

	private static String vSeqTabela;
	private static String vTabExecucao;
	private static String dtIni;
	
	public void checkLog(String vCheck,String vProcess, int vId) throws SQLException {
		
		//CONECTANDO AO BD
		//=========================================================================
		Conexao conProd = new Conexao();  
		
		//ENUM TRAZENDO A TABELA E A SEQUENCE
		TabRelatorioExec  tabExc = TabRelatorioExec.TAB6;
		vTabExecucao = tabExc.getTabExecucao();
		
		SeqTabelasRelatExec seqTab = SeqTabelasRelatExec.SEQ6;
		vSeqTabela = seqTab.getSeqTabela();
		
		//=========================================================================
		
		// AQUI PEGO A DATA ATUAL DO SISTEMA
			LocalDate dataManipulacao = LocalDate.now();
			System.out.println("Data :" + dataManipulacao);
			
			 LocalTime time = LocalTime.now();
			 System.out.println("Hora atual: "+time);
			    
			dtIni = String.valueOf(dataManipulacao);
			
		//=========================================================================

		
		if(vCheck.equals("0")) {
			
			//SE ESTIVER COM ERRO IRÁ INSERIR COMO "N"
			Statement stmtInsertTable = conProd.connection.createStatement();
			
			String sql2 = "INSERT INTO report.tb_check_log_ilhas (ID,DATA,PROCESS,CHEK) VALUES (nextval('"+vSeqTabela+"'),'" + dtIni + "','"+vProcess+"','S')";

			stmtInsertTable.executeUpdate(sql2);
			stmtInsertTable.close();

			
		}else {
			
			//SE ESTIVER COM ERRO IRÁ INSERIR COMO "S"				
			Statement stmtInsertTable = conProd.connection.createStatement();
			
			//String sql2 = "INSERT INTO report.tb_check_log_ilhas (ID,DATA,PROCESS,CHEK) VALUES (nextval('"+vSeqTabela+"'),'" + dtIni + "','"+vProcess+"','S')";
			
			String sql2 = "UPDATE report.tb_check_log_ilhas SET chek = 'S' where id = "+vId+" ";

			stmtInsertTable.executeUpdate(sql2);
			stmtInsertTable.close();
			
		}
		
		conProd.close();
		
	}
}