import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import connectionfactory.Conexao;


public class SelectRapd {
	
	private static long start;
	private static String vUserId;
	
		 
	
	 private static void calculaTempo(long time) {
	        long sec = time / 1000;
	        long min = time / (60 * 1000);
	        long hour = time / (60 * 60 * 1000);

	        if (hour > 0) {
	            System.out.println("Total da operacao " + hour + "hs");
	        } else if (min > 0) {
	            System.out.println("Total da operacao " + min + "min");
	        } else if (sec > 0) {
	            System.out.println("Total da operacao " + sec + "s");
	       }
	        
	 }

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		try {
		 start = System.currentTimeMillis();
			System.out.println("⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕");
			System.out.println("CHECK RELATORIOS......");
			// AQUI PEGO A DATA ATUAL DO SISTEMA
			 LocalDate dataManipulacao = LocalDate.now();
			 LocalTime time = LocalTime.now();
			 
			 System.out.println("Data Inicio :" + dataManipulacao + " <-> " +time);
	
			 
		// ABRE CONEXAO COM O BANCO DE DADOS
		Conexao conProd = new Conexao();
		
		System.out.println("Conectado...");
		
		String sqlCursor = "SELECT  ESCOLA_ID,ESCOLA,ID_MUNICIPIO,MUNICIPIO,ID_ESTADO,SIGLA_ESTADO,ESTADO,TURMA_ID,TURMA,PROFESSOR,JOGO,ALUNO,USERID,QTDE FROM report.view_materializada_report_ilhas";

		PreparedStatement stmtCursor = conProd.connection.prepareStatement(sqlCursor);
		ResultSet rsCursor = stmtCursor.executeQuery();
		//rsCursor.setFetchSize(1000);
		//stmtCursor.setExecuteBatch(100); 
		 while (rsCursor.next()) {
			 vUserId = rsCursor.getString("USERID");
/*
			vUserId = rsCursor.getString("USERID");
			vTurma_id = rsCursor.getString("TURMA_ID");
			vTurma = rsCursor.getString("TURMA");
			vAluno = rsCursor.getString("ALUNO");
			vVerificAlun = rsCursor.getInt("QTDE");

			vAluno = vAluno.replaceAll("'", "''");
		
*/
		 }
		 
		 calculaTempo(System.currentTimeMillis() - start);
		 
		 // AQUI PEGO A DATA ATUAL DO SISTEMA
		 LocalDate dataManipulacao2 = LocalDate.now();
		 LocalTime time2 = LocalTime.now();
		 
		 System.out.println("Data Fim :" + dataManipulacao2 + " <-> " +time2);
		 System.out.println("⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕");
		
	
	}catch(SQLException e) {
		e.printStackTrace();
		
		
	}
}
}

