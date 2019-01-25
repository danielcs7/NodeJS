package view;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;

import controller.CheckLogIlhas;
import controller.WriteReport;

public class StartValidProcess {
	
	
	public static void deleteLog() {

		File arquivo = new File("/tmp/scriptreportlote.sql");

		arquivo.delete();
		
		
		File arquivox = new File("LogRelatorioIlhasdoAlfaeBeto.txt");
		arquivox.delete();
		

	}
			
			
			

	public static void main(String[] args) throws ParseException, SQLException, IOException {
		// TODO Auto-generated method stub

		//AQUI VALIDO SE ALGUM PROCESSO FOI EXECUTADO COM ERRO;
		WriteReport wr = new WriteReport();
		
		deleteLog();
		wr.writeReport(0, "","");
		
		
		System.out.println("⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕");
		
		
		// AQUI PEGO A DATA ATUAL DO SISTEMA
		 LocalDate dataManipulacao = LocalDate.now();
		 LocalTime time = LocalTime.now();
		 
		 System.out.println("Data Inicio :" + dataManipulacao + " <-> " +time);
		
		 CheckLogIlhas chk = new CheckLogIlhas();
		 chk.vCheck();
		
		
		
		// AQUI PEGO A DATA ATUAL DO SISTEMA
		 LocalDate dataManipulacao2 = LocalDate.now();
		 LocalTime time2 = LocalTime.now();
		 
		 System.out.println("Data Fim :" + dataManipulacao2 + " <-> " +time2);
				
		
		
	}

}
