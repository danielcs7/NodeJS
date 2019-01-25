package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import connectionfactory.Conexao;

public class ExecLote {
	
	private static long start;
	
	
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

	
	public void execLote(String vProcess) throws IOException, SQLException {
		
		 Conexao conn = new Conexao();  
         
		 
		calculaTempo(System.currentTimeMillis() - start);
		 
		 // AQUI PEGO A DATA ATUAL DO SISTEMA
		 LocalDate dataManipulacao2 = LocalDate.now();
		 LocalTime time2 = LocalTime.now();
		 
		 System.out.println("Data Fim :" + dataManipulacao2 + " <-> " +time2);
		 System.out.println("⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕");
		 
		 File file = new File("/tmp/"+vProcess+"scriptreportlote.sql");
			ArrayList<String> calig = new ArrayList();
			
			FileReader fileReader = new FileReader(file);
           BufferedReader bufferedReader = new BufferedReader(fileReader);
           String linha = "";
           while ((linha = bufferedReader.readLine()) != null) {
           	calig.add(linha);
           }
           fileReader.close();
           bufferedReader.close();
           
            
           try {
           //Abre a conexao
           //Connection conn = ConnectionDataBase.getConnection();
           Statement stmt = null;
           //Cria um lista para receber os inserts do arquivo
           
          //inicializa o objeto statement
           stmt = conn.connection.createStatement();
           start = System.currentTimeMillis();
           //faz um for na lista e adiciona no método addBatch()
           // cada insert que veio do arquivo
           for (String s : calig) {
               stmt.addBatch(s);
           }
           //faz o insert em lote no banco pelo método executeBatch()
           stmt.executeBatch();
           //limpa o objeto stmt
           stmt.clearBatch();

           calculaTempo(System.currentTimeMillis() - start);
           
           
           } catch (SQLException e) {
               e.printStackTrace();
           } finally {
               conn.close();
           }

	}

}
