package controller;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import connectionfactory.Conexao;
import sendmail.EnviaMailCheck;

public class CheckSendMail {
	
	
	private static int vQtde;
	
	public static void deleteLog() {

		File pasta = new File("/tmp");    
		File[] arquivos = pasta.listFiles();    
		for(File arquivo : arquivos) {
		    if(arquivo.getName().endsWith("sql") || arquivo.getName().endsWith("out")) {
		        arquivo.delete();
		    }
		}
		
		
	}
	
	
	
	public void vCheckMail() {
		
				//**********************************************************************************8
				// ABRE CONEXAO COM O BANCO DE DADOS de PRODUCAO
				Conexao conProd = new Conexao();  
				//**********************************************************************************8
				
				//PEGA OS DADOS DE ESCOLA , MUNICIPIO , PROFESSOR E TURMA DO ALUNO
				String sqlMail = "select count(*) as qtde FROM report.view_materializada_report_ilhas";

				// PREPARA PARA TRAZER OS VALORES DO SCRIPT
				PreparedStatement stmtMail;
				try {
					
					
					stmtMail = conProd.connection.prepareStatement(sqlMail);
					
					ResultSet rsMail = stmtMail.executeQuery();
					
					while (rsMail.next()) {

						vQtde 			= rsMail.getInt("qtde");
				
					}
					
					
					if(vQtde == 6) {
						
						EnviaMailCheck emck = new EnviaMailCheck();
						
						emck.sendMail();
						
						System.out.println("Email enviado com sucesso...");
					}
					
					
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		
		
		
		
	}

}
