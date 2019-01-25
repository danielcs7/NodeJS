package view;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.NtpUtils;
import org.apache.commons.net.ntp.NtpV3Packet;
import org.apache.commons.net.ntp.TimeInfo;
import org.apache.commons.net.ntp.TimeStamp;

import connectionfactory.ConnectionFactoryProd;
import model.SequenciaTab;
import model.TabExecucao;


public class CheckGreen {
	
	private static int vId;
	private static String vGreenhouseId;
	private static String vHor1;
	private static String vHor2;
	private static String vCheckDate;
	private static String vDtAtual;
	private static Connection conProd;
	private static String vSeqTabela;
	private static String vTabExecucao;
	private static String vStatus;
	
	
	public static void writeLog() {

		Date data = new Date();
		String dt = null;
		SimpleDateFormat formatador = new SimpleDateFormat("yyyy-MM-dd");
		dt = formatador.format(data);
		
		
		 LocalTime time = LocalTime.now();
		 System.out.println("Hora atual: "+time);
		 
		 System.out.println("Hour : " + time.getHour());
	      System.out.println("Minute : " + time.getMinute());
	      
	      String vHH = String.valueOf(time.getHour());
	      String vMM = String.valueOf(time.getMinute());
	      
	      String vHoras = vHH+":"+vMM;
		 
		
		String vLog = "/tmp/Log_checkGreen.txt";
		
		System.out.println("Arquivo Criado => "+vLog);

		File arquivo = new File(vLog);
	
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

			// AQUI ESCREVE O LOGO
				
				
				bw.write("**************************************\n");
				bw.write("Hora  =>  "  + dt+" - "+vHoras+"\n");
				
		
			bw.close();
			fw.close();

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			System.out.println("Erro => "+pw);
			System.out.println("Erro e => "+e);
			

		}
	}
	
	
	
	public static void insertOper(String vEstufa,String vSttus) throws SQLException {
		
		//ENUM TRAZENDO A TABELA E A SEQUENCE
		
		TabExecucao  tabExc = TabExecucao.TAB1;
		vTabExecucao = tabExc.getTabExecucao();
		
		SequenciaTab seqTab = SequenciaTab.SEQ1;
		vSeqTabela = seqTab.getSeqTabela();
		
		if(vSttus.equals("1")) {
			vStatus = "true";
		}else {
			vStatus = "false";
		}
		
		Statement stmtInsertTable = conProd.createStatement();
		
		String sqlInsert = "insert into "+vTabExecucao+" (id,greenhouse_id, status) values (nextval('"+vSeqTabela+"'),'"+vEstufa+"','"+vStatus+"')";
		
		stmtInsertTable.executeUpdate(sqlInsert);
		stmtInsertTable.close();
		
	}
	
	
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
	
	
	public void vCheck() throws SQLException {
		
		//CONEXAO COM O BANCO DE DADOS
		conProd = ConnectionFactoryProd.createConnection();
		
		//*****************************************************************
		//*****************************************************************
		
		//ENUM TRAZENDO A TABELA E A SEQUENCE
		TabExecucao  tabExc = TabExecucao.TAB1;
		vTabExecucao = tabExc.getTabExecucao();
		
		SequenciaTab seqTab = SequenciaTab.SEQ1;
		vSeqTabela = seqTab.getSeqTabela();

		//*****************************************************************
		//*****************************************************************
		
		// PREPARA PARA EXECUTAR O SCRIPT
		Statement stmt1 = conProd.createStatement();
		String sql1 = "truncate table "+vTabExecucao;
		stmt1.executeUpdate(sql1);
		stmt1.close();
		
		//*****************************************************************
		//*****************************************************************
		
		// PREPARA PARA EXECUTAR O SCRIPT
		Statement stmt2 = conProd.createStatement();
		String sqlTrunc = "ALTER SEQUENCE " +vSeqTabela+ " RESTART WITH 1";
		stmt2.executeUpdate(sqlTrunc);
		stmt2.close();
		
		//*****************************************************************
		//*****************************************************************
		
		 LocalDate dataManipulacao = LocalDate.now();
		 vDtAtual = String.valueOf(dataManipulacao);
		 
			//*****************************************************************
			//*****************************************************************
		 
			GregorianCalendar teste = new GregorianCalendar();
			SimpleDateFormat teste2 = new SimpleDateFormat("HH:mm:h");
			String vHor1 = teste2.format(teste.getTime());
			
			//*****************************************************************
			//*****************************************************************

			//VERIFICA SE A DATA ESTA NO HORARIO DE VERÃO
			Calendar c = Calendar.getInstance(TimeZone.getDefault()); // omit timezone for default tz
			c.setTime(new Date()); // your date; omit this line for current date
			int offset = c.get(Calendar.DST_OFFSET);
        
			//*****************************************************************
			//*****************************************************************
        
        
			if(offset == 0) {
				
				 
				    System.out.println("Mais 1 dias:" + dataManipulacao.plusDays(1));
				    System.out.println("Menos 1 dia: " + dataManipulacao.minusDays(1));
				    System.out.println("Menos 7 dia: " + dataManipulacao.minusDays(7));
				    System.out.println("Menos 30 dia: " + dataManipulacao.minusDays(30));
				    System.out.println("Data Original:" + dataManipulacao);
				    System.out.println("Menos 8 dia: " + dataManipulacao.minusDays(6));
				    System.out.println("Menos 31 dia: " + dataManipulacao.minusDays(29));
				    
				    
				
			}else {
				
					//HORARIO DE VERÃO
				
				   LocalTime l = LocalTime.now();
				   LocalTime s = l.minusHours(1).minusMinutes(1);
				   System.out.println(s);
				   
				   vHor1 = String.valueOf(l.minusHours(1).minusMinutes(1));
				
			}
			
		
		String vcursor = "select id from greenhouses order by id";
		PreparedStatement stmtCursor;
		stmtCursor = conProd.prepareStatement(vcursor);
		
		
		ResultSet rsCursor = stmtCursor.executeQuery();
		
		while (rsCursor.next()) {

		
			vId = rsCursor.getInt("id");
			System.out.println("***********************************************************");
			System.out.println("\n");
			System.out.println("ESTUFA => "+vId);
		
		
		
		
		String vVerif = "SELECT greenhouse_id,hour,check_date FROM temp_measures \n" + 
				"WHERE GREENHOUSE_ID = "+vId+" \n" + 
				"AND CHECK_DATE = (SELECT MAX(CHECK_DATE) FROM temp_measures WHERE GREENHOUSE_ID = "+vId+" )\n" + 
				"ORDER BY HOUR DESC\n" + 
				"LIMIT 1";
		
		
		PreparedStatement stmtVerif;
		try {
			stmtVerif = conProd.prepareStatement(vVerif);
			
			
			ResultSet rsVerif = stmtVerif.executeQuery();
			
			int vstatus = 0;
			
			 while (rsVerif.next()) {

				 vGreenhouseId 	= rsVerif.getString("greenhouse_id");
				 vHor2 			= rsVerif.getString("hour");
				 vCheckDate 	= rsVerif.getString("check_date");
			    	 			    
			   
			 }
			 
			 
			 if(vCheckDate.equals(vDtAtual)) {
			 
			 
			 rsVerif.close();
			 stmtVerif.close();
			 System.out.println("Data Original:" + dataManipulacao);
			 System.out.println("Hora Sistena       : "+vHor1);
			 System.out.println("Ultima Verificacao : "+vHor2);
			 
			 
			    String horaOrigem = vHor1;
			    

			    String tst = horaOrigem.substring(3,5);
			    
			    
				int hora = new Integer(horaOrigem.substring(0, 2)) * 3600000; //1 hora = 3.600.000 milisegundos
				int minuto = Integer.parseInt(horaOrigem.substring(3,5)) * 60000; //1 minuto = 60.000 milisegundos 
				int v1 = (hora + minuto);
				//System.out.println(hora + minuto);
				
				
				String horaOrigem2 = vHor2;
				int hora2 = new Integer(horaOrigem2.substring(0, 2)) * 3600000; //1 hora = 3.600.000 milisegundos
				int minuto2 = Integer.parseInt(horaOrigem2.substring(3,5)) * 60000; //1 minuto = 60.000 milisegundos 
				int v2 = (hora2 + minuto2);
				//System.out.println(hora2 + minuto2);

				System.out.println("V1 => "+v1);
				System.out.println("V2 => "+v2);
				
				System.out.println("*****************************");
				
				int v3 = (v1) - (v2);
								
				int vResult = v3;
				
				System.out.println("Resultado : "+vResult);
				
				System.out.println("*****************************");
				
				String vRetorno = runTime(v2, v1);
				
				System.out.println("Retorno => "+vRetorno);
				
				if(vResult > -130000 && vResult <= 180000  ) {
					
					//faz insert
					insertOper(vGreenhouseId,"1");
					System.out.println("Operando");
					
				}else {
					
					insertOper(vGreenhouseId,"0");
					System.out.println("Não está Operando");
					
				}
				
				
				
			 }else {
				 insertOper(vGreenhouseId,"0");
				System.out.println("Não está Operando"); 
			 }
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		
		
		}
		
		rsCursor.close();
		stmtCursor.close();
		writeLog();
		conProd.close();
		
		
	}

}
