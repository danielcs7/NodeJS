package controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectionfactory.Conexao;

public class ReportOvertime {
	
	
	
	public String reportOvertime() throws SQLException{
		
		String vUserLogId;
		String vUserId;
		String vStarTime;
		String vEndTime_Ant;
		String vEndTime_Atual;
		String vTempo;
		String vTempoAtual;
		String vString;
		String vTempExec;
		int    vCount;
		int    vExc;

		// ABRE CONEXAO COM O BANCO DE DADOS de PRODUCAO
		Conexao conProd = new Conexao();  
		
		
		try {
			
		WriteReport wr = new WriteReport();
		String sqlCursor = "select userlogid,userid,startdatetime,finishdatetime_anterior,finishdatetime_atual,tempo,(finishdatetime_atual - startdatetime)  as tempo_atual  from REPORT.tb_log_ilhas_overtime";
		
		// PREPARA PARA TRAZER OS VALORES DO SCRIPT
		PreparedStatement stmtCursor = conProd.connection.prepareStatement(sqlCursor);
		// TRAZ OS RESULTADOS DO SELECT
		ResultSet rsCursor = stmtCursor.executeQuery();
		// TRAZ OS VALORES E IMPORTA NAS VARIAVEIS
		vCount = 0;
		 boolean vValidOvertime = false;
		while (rsCursor.next()) {

			vValidOvertime = true;
			vUserLogId 		= rsCursor.getString("userlogid");
			vUserId 		= rsCursor.getString("userid");
			vStarTime       = rsCursor.getString("startdatetime");
			vEndTime_Ant	= rsCursor.getString("finishdatetime_anterior");
			vEndTime_Atual	= rsCursor.getString("finishdatetime_atual");
			vTempo			= rsCursor.getString("tempo");
			vTempoAtual		= rsCursor.getString("tempo_atual");
			
			vCount = vCount+1;
			
			vString = vUserLogId+'|'+vUserId+'|'+vStarTime+'|'+vEndTime_Ant+'|'+vEndTime_Atual+'|'+vTempo+'|'+vTempoAtual;
			
			wr.writeReport(4, vString, "");
			
					
			
			
		}
		
		if (!vValidOvertime) {
				
			wr.writeReport(3, "", "");
			
		}
	  }catch(Exception e) {
		// AQUI ESCREVE O LOGO
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		
		vExc = 8;
		WriteReport wr = new WriteReport();
		wr.writeReport(vExc, sw.toString(), "0");

	}
		return "0";
		
	}
}