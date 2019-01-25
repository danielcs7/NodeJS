package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeraRelatorioTxt {

	private static int vExc;
	private static int vCountOvertime = 0;
	
	public static void writeLog(int vExcute, String vDados, String vQtde) {

		Date data = new Date();
		String dt = null;
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		dt = formatador.format(data);

		File arquivo = new File("/tmp/CheckRelatorioIlhasdoAlfaeBeto.txt");
		//File arquivo = new File("/home/idados-daniel/Documentos/tstAjuda/LogRelatorioIlhasdoAlfaeBeto.txt");

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

			switch (vExcute) {
			case 0:
				// AQUI ESCREVE O LOGO
				bw.write("********** INICIANDO O PROCESSO DO RELATORIO !!! **********");
				bw.newLine();
				bw.write("Hora => " + dt);
				bw.newLine();
				bw.write("###########################################################");
				bw.newLine();
				break;
			case 1:
				// AQUI ESCREVE O LOGO
				
				int vQtdDados = vQtde.length();
			
				
				if(vCountOvertime == 0){
				
					String[] TempQtde = vQtde.split("\\|");
					
					String vTempEXec = TempQtde[0];
					String vQte    	 = TempQtde[1];
					
					
					
				bw.write("Corrige a tabela ilhas.userlog!!!");
				bw.newLine();
				bw.write("Tempo de Execucao => " + vTempEXec);
				bw.newLine();
				bw.write("Quantidade de usuarios com o tempo acima de 01:00hr :: "+ vQte);
				bw.newLine();
				bw.write("*****************************************************************");
				vCountOvertime = 1;
				}else{
					
				
				if (vDados == null || vDados.isEmpty()) {

							
							bw.newLine();
							bw.write("NAO EXISTE ALUNOS COM O TEMPO ACIMA DE 01:00HR...");
							bw.newLine();
							bw.write("*****************************************************************");
							
							

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
				   }
				}
				break;

			case 2:
				// AQUI ESCREVE O LOGO
				bw.newLine();
				bw.newLine();
				bw.write("###############################################################################################################");
				bw.newLine();
				bw.newLine();
				bw.write("Relatorio Detalhado executado com sucesso!!!");
				bw.newLine();
				bw.write("Tempo de Execucao => " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;

			case 3:
				// AQUI ESCREVE O LOGO
				bw.write("Relatorio Pedagogico executado com sucesso!!!");
				bw.newLine();
				bw.write("Tempo de Execucao => " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;

			case 4:
				bw.write("Relatorio Letras executado com sucesso!!!");
				bw.newLine();
				bw.write("Tempo de Execucao => " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;

			case 5:
				bw.write("Relatorio Fonemas executado com sucesso!!!");
				bw.newLine();
				bw.write("Tempo de Execucao => " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;

			case 6:
				bw.write("Relatorio Caligrafia executado com sucesso!!!");
				bw.newLine();
				bw.write("Tempo de Execucao => " + vDados); 
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;

			case 7:
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

			case 8:
				bw.newLine();
				bw.write("Processo finalizado com Erros e Sucesso!!!");
				bw.newLine();
				bw.write("Tempo total de Processamento => " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;
				
			case 9:
				bw.newLine();
				bw.write("E-mail enviado com Sucesso!!!");
				bw.newLine();
				bw.write("Tempo total de Processamento => " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;	
				
			case 10:
				bw.newLine();
				bw.write("==> " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;		

			case 11:
				bw.newLine();
				bw.write("ERRO => " + vDados);
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
			writeLog(vExc, sw.toString(), "0");

		}

	}


}
