package controller;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import connectionfactory.Conexao;
import model.SeqTabelasRelatExec;
import model.TabRelatorioExec;
import sendmail.EnviaMail;

public class GeraRelatDet {
	
	private static int vCountAlunos; 
	private static int vCountAlunos2;
	private static int vQtdeJogada;
	private static int vCountVl7dias;
	private static int vQtdilhasLetras;
	private static int vQtdilhasSons;
	private static int vQtdilhasPalavras;
	private static int vQtdilhasTextos;
	private static int vQtdilhasCastelo;
	private static int vQtdilhasFormMaius;
	private static int vQtdilhasFormMinus;
	private static int vQtdilhasCursMaius;
	private static int vQtdilhasCursMinus;
	private static int vQtdeProcessadas = 0;
	
	private static String vSeqTabela;
	private static String vTabExecucao;
	private static String vDtAtual;
	private static String vDtSemana;
	private static String vIdEscola;
	private static String vEscola;
	private static String VMunicipio;
	private static String vTurma_id;
	private static String vTurma;
	private static String vProfessor;
	private static String vJogo;
	private static String vAluno;
	private static String vUserId;
	private static String vIdMunicipio;
	private static String vIdEstado;
	private static String vNameEstado;
	private static String vSiglaEstado;
	private static String vSerieId;
	private static String vDtIni;
	private static String vProcess;
	
	private static long start;
	
	
	
	// AQUI PARA GERAR O RELATORIO DO PROCESSO
	GeraRelatorioTxt grTxt = new GeraRelatorioTxt();
		
	public static void deleteLog() {

		File arquivo = new File("/tmp/DETALHADOscriptreportlote.sql");

		arquivo.delete();
		
		
	}
	
	
	public String vGeraRelat() throws ParseException, SQLException, IOException {
		
		
		 deleteLog(); 
		 WriteReport wr = new WriteReport();
  		 WriteLog gr = new WriteLog();
		 ExecLote exc = new ExecLote();
		 
		 start = System.currentTimeMillis();
		
		 // AQUI PEGO A DATA ATUAL DO SISTEMA
		 LocalDate dataManipulacao = LocalDate.now();
		 LocalTime time = LocalTime.now();
			 
		 vDtIni = String.valueOf(time);
		 vProcess = "DETALHADO";
		 
		//**********************************************************************************8
		// ABRE CONEXAO COM O BANCO DE DADOS de PRODUCAO
		Conexao conProd = new Conexao();  
		//**********************************************************************************8
		
		try {
			
			// VARIAVEL PARA A DATA INICIO E FINAL
			vCountAlunos = 0;
			vCountAlunos2 = 0;

			
			
			//**********************************************************************************8
		
			TabRelatorioExec  tabExc = TabRelatorioExec.TAB1;
			vTabExecucao = tabExc.getTabExecucao();
			
			
			SeqTabelasRelatExec seqTab = SeqTabelasRelatExec.SEQ1;
			vSeqTabela = seqTab.getSeqTabela();
			
			
			//**********************************************************************************8
			
			// PREPARA PARA EXECUTAR O SCRIPT
			Statement stmt1 = conProd.connection.createStatement();
			String sql1 = "truncate table "+vTabExecucao;
   		    stmt1.executeUpdate(sql1);
			stmt1.close();
			
			
			//**********************************************************************************8
			
			// PREPARA PARA EXECUTAR O SCRIPT
			Statement stmt2 = conProd.connection.createStatement();
			String sqlTrunc = "ALTER SEQUENCE " +vSeqTabela+ " RESTART WITH 1";
			stmt2.executeUpdate(sqlTrunc);
			stmt2.close();
			
			//**********************************************************************************8
			
			Date data = new Date();
			String dt = null;
			SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

			dt = formatador.format(data);
			Calendar c = new GregorianCalendar();

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			// c.add(Calendar.DATE, 1);
			vDtAtual = format.format(c.getTime());

			c.add(Calendar.DATE, -7);

			vDtSemana = format.format(c.getTime());
			//**********************************************************************************8
			
			
				//PEGA OS DADOS DE ESCOLA , MUNICIPIO , PROFESSOR E TURMA DO ALUNO
				String sqlDAlunos = "SELECT  ESCOLA_ID,ESCOLA,ID_MUNICIPIO,MUNICIPIO,ID_ESTADO,SIGLA_ESTADO,ESTADO,TURMA_ID,TURMA,SERIE_ID,PROFESSOR,JOGO,ALUNO,USERID,QTDE FROM report.materializada_report_ilhas";

				// PREPARA PARA TRAZER OS VALORES DO SCRIPT
				PreparedStatement stmtTblx = conProd.connection.prepareStatement(sqlDAlunos);
				// TRAZ OS RESULTADOS DO SELECT
				ResultSet rsTblx = stmtTblx.executeQuery();
				// TRAZ OS VALORES E IMPORTA NAS VARIAVEIS
				
				
				
				while (rsTblx.next()) {

					vIdEscola 			= rsTblx.getString("ESCOLA_ID");
					vEscola 			= rsTblx.getString("ESCOLA");
					vIdMunicipio 		= rsTblx.getString("ID_MUNICIPIO");
					VMunicipio 			= rsTblx.getString("MUNICIPIO");
					vIdEstado 			= rsTblx.getString("ID_ESTADO");
					vSiglaEstado 		= rsTblx.getString("SIGLA_ESTADO");
					vNameEstado 		= rsTblx.getString("ESTADO");
					vTurma_id 			= rsTblx.getString("TURMA_ID");
					vTurma 				= rsTblx.getString("TURMA");
					vSerieId			= rsTblx.getString("SERIE_ID");
					vProfessor 			= rsTblx.getString("PROFESSOR");
					vJogo 				= rsTblx.getString("JOGO");
					vAluno 				= rsTblx.getString("ALUNO");
					vUserId 			= rsTblx.getString("USERID");
					vQtdeJogada			= rsTblx.getInt("QTDE");

				
				
				vAluno = vAluno.replaceAll("'", "''");
				vCountAlunos2 = vCountAlunos2+1;
				

				String sqlTempo =   "SELECT to_char(UL.startdatetime, 'YYYY-MM-DD') AS DATA, SUM(UL.finishdatetime - UL.startdatetime) AS TIME\r\n" + 
						"FROM ILHAS.USERLOG UL\r\n" + 
						"WHERE UL.USERID = "+vUserId+" AND UL.userlogtypeid >= 5000\r\n" + 
						"GROUP BY to_char(UL.startdatetime, 'YYYY-MM-DD')\r\n" + 
						"ORDER BY 1";
				
				// PREPARA PARA TRAZER OS VALORES DO SCRIPT
				PreparedStatement stmtTempo = conProd.connection.prepareStatement(sqlTempo);
				// TRAZ OS RESULTADOS DO SELECT
				ResultSet rsTempo = stmtTempo.executeQuery();
				
				
				String sqlAcertos = "SELECT to_char(startdatetime, 'YYYY-MM-DD') as DATA, SUM(HITSCOUNT) ACERTOS, SUM(ERRORSCOUNT) ERROS\r\n" + 
									"FROM ILHAS.USERMATCH\r\n" + 
									"WHERE USERID = "+vUserId+" \r\n" + 
									"GROUP BY to_char(startdatetime, 'YYYY-MM-DD')\r\n";
				
				// PREPARA PARA TRAZER OS VALORES DO SCRIPT
				PreparedStatement stmtAcertos= conProd.connection.prepareStatement(sqlAcertos);
				// TRAZ OS RESULTADOS DO SELECT
				ResultSet rsAcertos = stmtAcertos.executeQuery();
				
				String sqlIlhas = "SELECT DATA, PATHID, COUNT(*)\n" + 
						"FROM (\n" + 
						"    SELECT to_char(APPCREATIONDATETIME, 'YYYY-MM-DD') AS DATA, S.PATHID, UP.STAGEID, SUM(starscount) AS STARS, 3 AS MAX\n" + 
						"    FROM ILHAS.USERPROGRESS UP\n" + 
						"    INNER JOIN ILHAS.STAGE S ON S.ID = UP.STAGEID\n" + 
						"    WHERE USERID = "+vUserId+"\n" + 
						"    GROUP BY to_char(APPCREATIONDATETIME, 'YYYY-MM-DD'), S.PATHID, UP.STAGEID, ABILITYID\n" + 
						") CONSULTA\n" + 
						"WHERE STARS >= (MAX/3)*2 \n" + 
						"GROUP BY DATA, PATHID\n" + 
						"ORDER BY 1,2";
				
				// PREPARA PARA TRAZER OS VALORES DO SCRIPT
				PreparedStatement stmtIlhas = conProd.connection.prepareStatement(sqlIlhas);
				// TRAZ OS RESULTADOS DO SELECT
				ResultSet rsIlhas = stmtIlhas.executeQuery();
				
				// MONTA HASHMAP
				HashMap<String, ArrayList<String>> hashmap = new HashMap<String, ArrayList<String>>();
				
				//add o tempo no hashmap
				while (rsTempo.next()) {
					ArrayList<String> arraylist = new ArrayList<String>();
					arraylist.add(rsTempo.getString("DATA"));
					arraylist.add(rsTempo.getString("TIME"));
					hashmap.put(rsTempo.getString("DATA"), arraylist);
				}
				
				//add o acerto no hashmap
				while (rsAcertos.next()) {
					if(hashmap.get(rsAcertos.getString("DATA")) != null) {
						hashmap.get(rsAcertos.getString("DATA")).add(rsAcertos.getString("ACERTOS"));
						hashmap.get(rsAcertos.getString("DATA")).add(rsAcertos.getString("ERROS"));
					}
				}
				
				//add as ilhas no hashmap
				while (rsIlhas.next()) {
					if(hashmap.get(rsIlhas.getString("DATA")) != null) {
						//adiciona zeros nas ilhas anteriores
						int qtd = (rsIlhas.getInt("PATHID") + 3) - hashmap.get(rsIlhas.getString("DATA")).size();
						for(int i=0; i<qtd; i++) {
							hashmap.get(rsIlhas.getString("DATA")).add("0");
						}
						hashmap.get(rsIlhas.getString("DATA")).add(rsIlhas.getString("COUNT"));
					}
				}
				ArrayList<String> dates = new ArrayList<String>(hashmap.keySet());
				//adiciona zeros nas ilhas faltantes
				for (int i=0; i < dates.size(); i++) {
					int qtd = 10 - hashmap.get(dates.get(i)).size();
					for(int j=0; j<qtd; j++) {
						hashmap.get(dates.get(i)).add("0");
					}
				}
				
				for (int i=0; i< dates.size(); i++) {
					
					
					
					String sql2 = "INSERT INTO "+vTabExecucao+" (id , escola_id ,escola ,id_municipio,municipio ,id_estado,sigla_estado,estado,turma_id , turma , serie_id, professor , id_aluno, aluno , creationdatetime , tempo_total , tempo_total_hr, acertos , erros , ilha_das_letras, ilha_dos_sons,ilha_das_palavras,ilha_dos_textos,ilha_do_castelo) VALUES (nextval('"+vSeqTabela+"'),'"+ vIdEscola + "','"+ vEscola + "','"+ vIdMunicipio +"','"+ VMunicipio + "','"+ vIdEstado +"','"+ vSiglaEstado +"','"+ vNameEstado +"','"+ vTurma_id + "','"+ vTurma + "','"+ vSerieId + "','"+ vProfessor + "','"+ vUserId + "','"+ vAluno + "','"+ hashmap.get(dates.get(i)).get(0) + "','"+ timeToSecond(hashmap.get(dates.get(i)).get(1)) + "','"+ hashmap.get(dates.get(i)).get(1) + "','"+ hashmap.get(dates.get(i)).get(2) + "','"+ hashmap.get(dates.get(i)).get(3) + "','"+ hashmap.get(dates.get(i)).get(4) + "','"+ hashmap.get(dates.get(i)).get(5) + "','"+ hashmap.get(dates.get(i)).get(6) + "','"+ hashmap.get(dates.get(i)).get(7) + "','"+ hashmap.get(dates.get(i)).get(8)+"');";
					
					gr.writeLog(vProcess,sql2);
					vQtdeProcessadas = vQtdeProcessadas+1;
				}
				
		
				
				

			}

			
		
			
		} catch (SQLException e) {
			System.out.println(vUserId);
			System.out.println(e.getMessage());
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			
			String vErros = sw.toString()+"USERID = "+vUserId+ " ALUNO = "+vAluno;
			wr.writeReport(11, vErros, String.valueOf(vQtdeProcessadas));
			

		}
		
		
		
		
		
		try {

		// ABRE CONEXAO COM O BANCO DE DADOS PROD
				
		//******************************************************************
		// ABRE CONEXAO COM O BANCO DE DADOS DEV
		//Connection conProd = ConnectionFactoryDev.createConnection();
		//******************************************************************
		
		String sqlCount = "select count(distinct(id_aluno))as total from "+vTabExecucao+" where dias_jogados_periodo_7dias <> 0";

		// PREPARA PARA TRAZER OS VALORES DO SCRIPT
		PreparedStatement stmtCount = conProd.connection.prepareStatement(sqlCount);
		// TRAZ OS RESULTADOS DO SELECT
		ResultSet rsCount = stmtCount.executeQuery();
		// TRAZ OS VALORES E IMPORTA NAS VARIAVEIS
		while (rsCount.next()) {

			vCountVl7dias = Integer.parseInt(rsCount.getString("TOTAL"));
		}
		stmtCount.close();
		rsCount.close();

		Statement stmtUp = conProd.connection.createStatement();
		// String sqlUp = "UPDATE report.tb_relatorio_gerencial_ilhas SET total_alunos_jogaram_7dias = '" + vCountVl7dias+ "' ";

		// EXECUTA O SCRIPT CONFORME ACIMA
		//x stmtUp.executeUpdate(sqlUp);
		stmtUp.close();

		String sqlQtdFasesporIlhas = "SELECT ID, QTDE , \n" + 
				"NAME  \n" + 
				"FROM(\n" + 
				"select '1' as id,\n" + 
				"	count(*) as qtde, \n" + 
				"	pt.name \n" + 
				"	from  ilhas.stage st ,ilhas.path pt , ilhas.stageability sa\n" + 
				"	where st.pathid = 1 \n" + 
				"	and st.pathid = pt.id \n" + 
				"	and sa.stageid = st.id\n" + 
				"	group by pt.name\n" + 
				"     union  \n" + 
				"        select '2' as id,\n" + 
				"	 count(*) as qtde, \n" + 
				"	 pt.name \n" + 
				"	 from  ilhas.stage st ,ilhas.path pt , ilhas.stageability sa\n" + 
				"	 where st.pathid = 2 \n" + 
				"	 and st.pathid = pt.id \n" + 
				"	 and sa.stageid = st.id\n" + 
				"	 group by pt.name\n" + 
				"     union \n" + 
				"        select '3' as id,\n" + 
				"          count(*) as qtde, \n" + 
				"          pt.name \n" + 
				"          from  ilhas.stage st ,ilhas.path pt , ilhas.stageability sa\n" + 
				"          where st.pathid = 3 \n" + 
				"          and st.pathid = pt.id \n" + 
				"          and sa.stageid = st.id\n" + 
				"          group by pt.name\n" + 
				"     union \n" + 
				"       select '4' as id,\n" + 
				"         count(*) as qtde, \n" + 
				"         pt.name \n" + 
				"         from  ilhas.stage st ,ilhas.path pt , ilhas.stageability sa\n" + 
				"         where st.pathid = 4 \n" + 
				"         and st.pathid = pt.id \n" + 
				"         and sa.stageid = st.id\n" + 
				"         group by pt.name\n" + 
				"    union \n" + 
				"       select '5' as id,\n" + 
				"        count(*) as qtde, \n" + 
				"        pt.name \n" + 
				"        from  ilhas.stage st ,ilhas.path pt , ilhas.stageability sa\n" + 
				"        where st.pathid = 5 \n" + 
				"        and st.pathid = pt.id \n" + 
				"        and sa.stageid = st.id\n" + 
				"        group by pt.name\n" + 
				"    UNION \n" + 
				"      select '1000' as id,\n" + 
				"        count(*) as qtde, \n" + 
				"        pt.name \n" + 
				"        from ilhas.stage st , ilhas.path pt , ilhas.stageability sa\n" + 
				"        where st.pathid = 1000\n" + 
				"        and st.pathid = pt.id \n" + 
				"        and sa.stageid = st.id\n" + 
				"        group by pt.name \n" + 
				"    UNION\n" + 
				"      select '2000' as id,\n" + 
				"        count(*) as qtde, \n" + 
				"        pt.name \n" + 
				"        from  ilhas.stage st ,ilhas.path pt , ilhas.stageability sa\n" + 
				"        where st.pathid = 2000 \n" + 
				"        and st.pathid = pt.id\n" + 
				"        and sa.stageid = st.id\n" + 
				"        group by pt.name \n" + 
				"     UNION \n" + 
				"        select '3000' as id,\n" + 
				"        count(*) as qtde, \n" + 
				"        pt.name\n" + 
				"	from  ilhas.stage st , ilhas.path pt , ilhas.stageability sa\n" + 
				"	where st.pathid = 3000\n" + 
				"	and st.pathid = pt.id \n" + 
				"	and sa.stageid = st.id\n" + 
				"	group by pt.name \n" + 
				"     UNION\n" + 
				"	select '4000' as id,\n" + 
				"	count(*) as qtde, \n" + 
				"	pt.name \n" + 
				"	from ilhas.stage st ,ilhas.path pt , ilhas.stageability sa\n" + 
				"	where st.pathid = 4000 \n" + 
				"	and st.pathid = pt.id\n" + 
				"	and sa.stageid = st.id\n" + 
				"	group by pt.name) \n" + 
				"	DADOS ORDER BY DADOS.NAME";
		
		// PREPARA PARA TRAZER OS VALORES DO SCRIPT
		PreparedStatement stmtQtdIlhas = conProd.connection.prepareStatement(sqlQtdFasesporIlhas);
		// TRAZ OS RESULTADOS DO SELECT
		ResultSet rsQtdIlhas = stmtQtdIlhas.executeQuery();
		// TRAZ OS VALORES E IMPORTA NAS VARIAVEIS
		while (rsQtdIlhas.next()) {

			String vVerificaId;

			vVerificaId = rsQtdIlhas.getString("ID");

			switch (vVerificaId) {
			case "1":
				vQtdilhasLetras = rsQtdIlhas.getInt("QTDE");
				break;

			case "2":
				vQtdilhasSons = rsQtdIlhas.getInt("QTDE");
				break;

			case "3":
				vQtdilhasPalavras = rsQtdIlhas.getInt("QTDE");
				break;

			case "4":
				vQtdilhasTextos = rsQtdIlhas.getInt("QTDE");
				break;

			case "5":
				vQtdilhasCastelo = rsQtdIlhas.getInt("QTDE");
				break;

			case "1000":
				vQtdilhasFormMaius = rsQtdIlhas.getInt("QTDE");
				break;

			case "2000":
				vQtdilhasFormMinus = rsQtdIlhas.getInt("QTDE");
				break;

			case "3000":
				vQtdilhasCursMaius = rsQtdIlhas.getInt("QTDE");
				break;

			case "4000":
				vQtdilhasCursMinus = rsQtdIlhas.getInt("QTDE");
				break;
			default:
				System.out.printf("Você digitou uma operação inválida.");

			}

		}
		stmtQtdIlhas.close();
		rsQtdIlhas.close();

		String sqlUp2 = "UPDATE "+vTabExecucao+" " + "SET qtde_fases_iletras = '" + vQtdilhasLetras+ "', " + "qtde_fases_isons = '" + vQtdilhasSons + "', " + "qtde_fases_ipalavras = '"+ vQtdilhasPalavras + "'," + "qtde_fases_itextos = '" + vQtdilhasTextos + "', "+ "qtde_fases_icastelo = '" + vQtdilhasCastelo + "', " + "qtde_fases_forma_maius = '"+ vQtdilhasFormMaius + "', " + "qtde_fases_forma_minus = '" + vQtdilhasFormMinus + "', "+ "qtde_fases_cursiva_maius = '" + vQtdilhasCursMaius + "', " + "qtde_fases_cursiva_minus = '"+ vQtdilhasCursMinus + "' " + ";";

		
		gr.writeLog(vProcess,sqlUp2);

		//**********************************************************************8
      	//AQUI FAÇO O INSERT EM LOTE
      	
      	exc.execLote(vProcess);
      	//**********************************************************************8

      		
		
      	// FECHA CONEXAO COM O BANCO DE DADOS
		conProd.close();
		

		// ENVIA EMAIL
		EnviaMail env = new EnviaMail();
		env.sendMail();
       
    	
		
		} catch (SQLException e) {
			System.out.println(vUserId);
			System.out.println(e.getMessage());
			
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			
			String vErros = sw.toString()+"USERID = "+vUserId+ " ALUNO = "+vAluno;
			wr.writeReport(11, vErros, String.valueOf(vQtdeProcessadas));
			
			return "1";
	  }
		
		return "0";
	
		
	}
	
	
	public long timeToSecond(String time) {
		String tmp[] = time.split(":");
		
		long vHra = Integer.parseInt(tmp[0]) * 3600;
		long vMnt = Integer.parseInt(tmp[1]) * 60;
		long vSgn = Integer.parseInt(tmp[2]);
		
		return vHra+vMnt+vSgn;
	}

}