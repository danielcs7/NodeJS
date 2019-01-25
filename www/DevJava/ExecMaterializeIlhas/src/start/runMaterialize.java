package start;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import connectionfactory.ConnectionFactoryProd;
import sendmail.EnviaMail;


public class runMaterialize {
	
	
	private static long time1;
	private static long time2;
	private static long timeX;
	private static long time22;
	private static long tempo;
	private static long segundos;
	private static long minutos;
	private static long horas;

	private static int vExc;
	private static int vCountOvertime = 0;
	
	private static String vHoraRun;
	private static String vDt;
	private static String vErroProc;
	private static String tempo2;
	private static String vRetornProc;
	private static String vRuntime;
	private static String vQtdLog;
	
	public static void reportStart() {

		deleteLog();

		vExc = 0;

		writeLog(vExc, "", "");

	}
	//※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
	
	public static void variableClear() {

		time2 		= 0;
		time1 		= 0;
		tempo 		= 0;
		segundos 	= 0;
		minutos 	= 0;
		horas 		= 0;
		tempo2 		= "";

	}
	//※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
	
	public static String runTime(long vStart, long vEnd) {

		long tempo = vEnd - vStart;

		long segundos 	= tempo / 1000;
		long minutos 	= segundos / 60;
		segundos 		= segundos % 60;
		long horas 		= minutos / 60;
		minutos 		= minutos % 60;

		String tempo2 = String.format("%02d:%02d:%02d", horas, minutos,segundos); // Exemplo: "12:34:56"
		
		return tempo2;

	}
	//※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
	
	public static void deleteLog() {

		File arquivo = new File("/tmp/LogExecMaterializeILhas.txt");

		arquivo.delete();

	}
	//※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
	
	public static void writeLog(int vExcute, String vDados, String vQtde) {

		Date data = new Date();
		String dt = null;
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		dt = formatador.format(data);

		File arquivo = new File("/tmp/LogExecMaterializeILhas.txt");
		
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
				
				// AQUI ESCREVE O LOGO
				bw.newLine();
				bw.newLine();
				bw.write("view_materializada_report_ilhas executado com sucesso!!!");
				bw.newLine();
				bw.write("Tempo de Execucao => " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;
			case 2:
				// AQUI ESCREVE O LOGO
				
				// AQUI ESCREVE O LOGO
				bw.newLine();
				bw.newLine();
				bw.write("view_mater_hability_ilhas executado com sucesso!!!");
				bw.newLine();
				bw.write("Tempo de Execucao => " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;	
			case 3:
				// AQUI ESCREVE O LOGO
				
				// AQUI ESCREVE O LOGO
				bw.newLine();
				bw.newLine();
				bw.write("view_mater_hability_ilhas_calig executado com sucesso!!!");
				bw.newLine();
				bw.write("Tempo de Execucao => " + vDados);
				bw.newLine();
				bw.write("*****************************************************************");
				bw.newLine();
				break;	
			case 4:
				// AQUI ESCREVE O LOGO
				
				// AQUI ESCREVE O LOGO
				bw.newLine();
				bw.newLine();
				bw.write("view_mater_hability_ilhas_ltr executado com sucesso!!!");
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
	
	//※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
	
	public static String tabMaterializeIlhas() {
		vExc = 1;
		time1 = System.currentTimeMillis();
		
		try {
			
			Connection conProd = ConnectionFactoryProd.createConnection();
			
			
			//REFRESH MATERIALIZED VIEW 
			//*******************************************************************
			String sqlFresh = "REFRESH MATERIALIZED VIEW report.view_materializada_report_ilhas";
			Statement stmtRefresh = conProd.createStatement();
			stmtRefresh.executeUpdate(sqlFresh);
			stmtRefresh.close();
			
			//*******************************************************************
			time2 = System.currentTimeMillis();
			vRuntime = runTime(time1, time2);

			//*******************************************************************
			
			writeLog(vExc, vRuntime, "0");
			variableClear();

			//*******************************************************************
			
			conProd.close();
			return vErroProc = "7";

			//*******************************************************************
			
			}catch(Exception e) {
				
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);

				vExc = 8;
				writeLog(vExc, sw.toString(), "0");
				return vErroProc = "1";
			}
		
		
		
	}
	
	//※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
	
	public static String tabMaterializeHabilityIlhas() {
		vExc = 2;
		time1 = System.currentTimeMillis();
		
		try {
			
			Connection conProd = ConnectionFactoryProd.createConnection();
			
			
			//REFRESH MATERIALIZED VIEW 
			//*******************************************************************
			String sqlhabilty = "REFRESH MATERIALIZED VIEW report.view_mater_hability_ilhas";
			Statement stmtCreatHabiltyTemp = conProd.createStatement();
			stmtCreatHabiltyTemp.executeUpdate(sqlhabilty);
			stmtCreatHabiltyTemp.close();
			
			//*******************************************************************
			time2 = System.currentTimeMillis();
			vRuntime = runTime(time1, time2);

			//*******************************************************************
			writeLog(vExc, vRuntime, "0");
			variableClear();

			//*******************************************************************
			
			conProd.close();
			return vErroProc = "7";

			//*******************************************************************
			
			}catch(Exception e) {
				
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);

				vExc = 8;
				writeLog(vExc, sw.toString(), "0");
				return vErroProc = "1";
			}
		
		
	}
	
	//※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
	
	public static String tabMaterializeCaligrafiaIlhas() {
		vExc = 3;
		time1 = System.currentTimeMillis();
		
		try {
			
			Connection conProd = ConnectionFactoryProd.createConnection();
			
			
			//REFRESH MATERIALIZED VIEW view_materializada_funcionario;
			//*******************************************************************
			String sqlhabilty = "REFRESH MATERIALIZED VIEW report.view_mater_hability_ilhas_calig";
			Statement stmtCreatHabiltyTemp = conProd.createStatement();
			stmtCreatHabiltyTemp.executeUpdate(sqlhabilty);
			stmtCreatHabiltyTemp.close();
			
			//*******************************************************************
			time2 = System.currentTimeMillis();
			vRuntime = runTime(time1, time2);

			//*******************************************************************
			writeLog(vExc, vRuntime, "0");
			variableClear();

			//*******************************************************************
			conProd.close();
			return vErroProc = "7";

			//*******************************************************************			
			}catch(Exception e) {
				
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);

				vExc = 8;
				writeLog(vExc, sw.toString(), "0");
				return vErroProc = "1";
			}
		
		
	}

	//※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
	
	public static String tabMaterializeLetrasIlhas() {
		vExc = 4;
		time1 = System.currentTimeMillis();
		
		try {
			
			Connection conProd = ConnectionFactoryProd.createConnection();
			
			
			//REFRESH MATERIALIZED VIEW 
			//*******************************************************************
			String sqlhabilty = "REFRESH MATERIALIZED VIEW report.view_mater_hability_ilhas_ltr";
			Statement stmtCreatHabiltyTemp = conProd.createStatement();
			stmtCreatHabiltyTemp.executeUpdate(sqlhabilty);
			stmtCreatHabiltyTemp.close();
			//*******************************************************************
			
			time2 = System.currentTimeMillis();
			vRuntime = runTime(time1, time2);

			//*******************************************************************
			
			writeLog(vExc, vRuntime, "0");
			variableClear();

			//*******************************************************************
			conProd.close();
			
			return vErroProc = "7";

			
			}catch(Exception e) {
				
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);

				vExc = 8;
				writeLog(vExc, sw.toString(), "0");
				return vErroProc = "1";
			}
		
		
	}

	//※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
	
	
	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		// ABRE CONEXAO COM O BANCO DE DADOS de PRODUCAO
		//Connection conProd = ConnectionFactoryDev.createConnection();
		long timeX = System.currentTimeMillis();
		// INICIA O LOG
		System.out.println("Iniciando...");
		reportStart();
		
		System.out.println("view_materializada_report_ilhas...");
		vRetornProc = tabMaterializeIlhas();
		
		System.out.println("view_mater_hability_ilhas...");
		vRetornProc = tabMaterializeHabilityIlhas();
		
		System.out.println("view_mater_hability_ilhas_calig...");
		vRetornProc = tabMaterializeCaligrafiaIlhas();
		
		System.out.println("view_mater_hability_ilhas_ltr...");
		vRetornProc = tabMaterializeLetrasIlhas();
		
		//*******************************************************************
		
		
		if (vRetornProc == "1") {
			vExc = 8;
			time2 = System.currentTimeMillis();

			vRuntime = runTime(timeX, time2);

			// AQUI ESCREVE O LOGO
			writeLog(vExc, vRuntime, "0");

		} else {
			vExc = 7;
			time22 = System.currentTimeMillis();
			vRuntime = runTime(timeX, time22);

			// AQUI ESCREVE O LOGO
			writeLog(vExc, vRuntime, "0");

		}

		//*******************************************************************
		
		try {
			System.out.println("envia email...");
			// ENVIA E-MAIL
			EnviaMail env = new EnviaMail();
			env.sendMail();

			// Aqui excluir o arquivo da pasta
			deleteLog();

		} catch (Exception e) {
			// AQUI ESCREVE O LOGO
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			vExc = -1;
			writeLog(vExc, sw.toString(), "0");

		}
		
		

	}

}
