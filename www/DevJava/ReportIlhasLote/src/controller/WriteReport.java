package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteReport {
	
	private static int vExc;
	private static int vCountOvertime = 0;
	
	
	
	
	public  void writeReport(int vProc, String vDados,String vQtde ) {
		
		Date data = new Date();
		String dt = null;
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		dt = formatador.format(data);

		File arquivo = new File("/tmp/LogRelatorioIlhasdoAlfaeBeto.txt");

		try {

			if (!arquivo.exists()) {

				// cria um arquivo (vazio)
				arquivo.createNewFile();

			}

			// caso seja um diretório, é possível listar seus arquivos e
			// diretórios
			File[] arquivos = arquivo.listFiles();

			// escreve no arquivo
			FileWriter fw = new FileWriter(arquivo, true);

			BufferedWriter bw = new BufferedWriter(fw);
		
			switch (vProc) {
			case 0:
				// AQUI ESCREVE O LOGO
				bw.write("********** INICIANDO O PROCESSO DO RELATORIO !!! **********");
				bw.newLine();
				bw.write("Data e Hora => " + dt);
				bw.newLine();
				bw.write("************************************************************");
				bw.newLine();
				break;
			case 1:
				
				String[] vSplipts = vDados.split("\\|");
				String vProces 		= vSplipts[0];
				String vIni    	 	= vSplipts[1];
				
				bw.write("*****************************************************************");
				bw.newLine();
				bw.write("********** INICIANDO O PROCESSO "+vProces+ "  **********");
				bw.newLine();
				bw.write("Hora Inicio => " + dt);
				bw.newLine();
				
				break;
				
			case 2:
				
				String[] vSplipts2 = vDados.split("\\|");
				
				String vProces2 		= vSplipts2[0];
				String vIni2    	 	= vSplipts2[1];
				String vFim2    	 	= vSplipts2[2];
				bw.newLine();
				bw.write("Hora Inicio: "+vIni2 );
				bw.newLine();
				bw.newLine();
				bw.write("Processo executado com sucesso... ");
				bw.newLine();
				bw.write("Hora Fim   : "+vFim2 );
				bw.newLine();
				bw.newLine();
				bw.write("Tempo de Execucao => " + vQtde);
				bw.newLine();
				bw.newLine();
				break;


			case 3:
				// AQUI ESCREVE O LOGO
				// AQUI ESCREVE O LOGO
				
				if(vCountOvertime == 0){
				
								
					
				bw.newLine();
				bw.write("Corrige a tabela ilhas.userlog!!!");
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				}
				vCountOvertime = 1;
			  break;	
			case 4:	
					
				
				if (vDados == null || vDados.isEmpty()) {

							
							bw.newLine();	
							
							bw.write("NAO EXISTE ALUNOS COM O TEMPO ACIMA DE 01:00HR...");
							bw.newLine();
							bw.write("*****************************************************************");
							bw.newLine();
							
							

						}else {
				
							String[] textoSeparado = vDados.split("\\|");
							
							String vUserLogId		= textoSeparado[0];
							String vUserId 			= textoSeparado[1];
							String vStarTime       	= textoSeparado[2];
							String vEndTime_Ant		= textoSeparado[3];
							String vEndTime_Atual	= textoSeparado[4];
							String vTempo			= textoSeparado[5];
							String vTempoAtual		= textoSeparado[6];
							
							bw.newLine();
							bw.write("USERLOGID                :"+vUserLogId);
							bw.newLine();
							bw.write("USERID                   :"+vUserId);
							bw.newLine();
							bw.write("STARTDATETIME            :"+vStarTime);
							bw.newLine();
							bw.write("FINISHDATETIME-ANTERIOR  :"+vEndTime_Ant);
							bw.newLine();
							bw.write("FINISHDATETIME-CORRIGIDO :"+vEndTime_Atual);
							bw.newLine();
							bw.write("TEMPO ANTERIOR           :"+vTempo);
							bw.newLine();
							bw.write("TEMPO CORRIGIDO          :"+vTempoAtual);
							bw.newLine();
							bw.write("*****************************************************************");
							bw.newLine();
				   }
				
				break;

			case 5:
				
				String[] vSplipt5 = vDados.split("\\|");
				String   vProces5 		= vSplipt5[0];
				String   vIni5    	 	= vSplipt5[1];
				String   vFim5    	 	= vSplipt5[2];
				
				bw.newLine();
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				bw.write(vProces5+ " <=> Executado com sucesso!!!");
				bw.newLine();
				bw.write("Tempo de Execucao => " + vQtde);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				bw.newLine();
				break;

			case 7:
				bw.write("Relatorio Fonemas executado com sucesso!!!");
				bw.newLine();
				bw.write("Tempo de Execucao => " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;

			case 8:
				bw.write("Relatorio Caligrafia executado com sucesso!!!");
				bw.newLine();
				bw.write("Tempo de Execucao => " + vDados); 
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;

			case 9:
				bw.newLine();
				bw.write("#################################################################");
				bw.newLine();
				bw.write("Processo finalizado com sucesso!!!");
				bw.newLine();
				bw.write("Tempo total de Processamento => " + vDados);
				bw.newLine();
				bw.write("#################################################################");
				bw.newLine();
				break;

			case 10:
				bw.newLine();
				bw.write("Processo finalizado com Erros e Sucesso!!!");
				bw.newLine();
				bw.write("Tempo total de Processamento => " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;
				
			case 11:
				bw.newLine();
				bw.write("E-mail enviado com Sucesso!!!");
				bw.newLine();
				bw.write("Tempo total de Processamento => " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;	
				
			case 12:
				bw.newLine();
				bw.write("Erro");
				bw.newLine();
				bw.write("==> " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;		

			case 13:
				bw.newLine();
				bw.write("ERRO => " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;	
			
			case 14:
				bw.newLine();
				bw.write("==> " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;			
			default:
				bw.newLine();
				bw.write("Erro !!! " + vDados);
				bw.newLine();
				bw.write("*************************************************************************************************");
				bw.newLine();

			}

			bw.close();
			fw.close();

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			vExc = 8;
			//writeLog(vExc, sw.toString(), "0");

		}

	}


}
