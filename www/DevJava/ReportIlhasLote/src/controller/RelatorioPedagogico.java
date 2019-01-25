package controller;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;

import connectionfactory.Conexao;
import model.SeqTabelasRelatExec;
import model.TabRelatorioExec;



public class RelatorioPedagogico {

	private static String dtFim;
	private static String dtIni;
	private static String vSeqTabela;
	private static String vTabExecucao;
	private static String vIdEscola;
	private static String vEscola;
	private static String VMunicipio;
	private static String vTurma_id;
	private static String vTurma;
	private static String vProfessor;
	private static String vJogo;
	private static String vDtAtual;
	private static String vDtSemana;
	private static String vTempTotal;
	private static String vTempTotalAnt;
	private static String vUserId;
	private static String vAluno;
	private static String vIdMunicipio;
	private static String vIdEstado;
	private static String vNameEstado;
	private static String vSiglaEstado;
	private static String vSerieId;
	private static String vSql1_userid;
	private static String vSql1_abiliname;
	private static String vSql1_abilidt;
	private static String vSql1_percentual;
	private static String vSql1_ilhas;
	private static String vCreationDt;
	private static String vProcess;
	
	private static int vSql1_abilityid;
	private static int vSql1_qtdestrelas;
	private static int vSql1_qtdjogadas;
	private static int vSql1_ilhasId;
	private static int vCountStars;
	private static int vCount2_3;
	private static int vCountAcima2_3;
	private static int vQtd_start_hability;
	private static int vVerificAlun;
	private static int vQtdeProcessadas = 0;
	
		
	private static float vperc2;
	private static long start;
	
	public static void deleteLog() {

		File arquivo = new File("/tmp/PEDAGOGICOscriptreportlote.sql");

		arquivo.delete();
		
		
	}
	
	public String vRelatPedag() throws ParseException, SQLException {
		
    	// AQUI PARA GERAR O RELATORIO DO PROCESSO
		 GeraRelatorioTxt grTxt = new GeraRelatorioTxt();
		 deleteLog(); 
 		 WriteLog gr = new WriteLog();
		 ExecLote exc = new ExecLote();
		 vProcess = "PEDAGOGICO";
		// *****************************************************************************8
		
		try {

			// VARIAVEL PARA A DATA INICIO E FINAL
			dtIni = "2018-01-01";
			
			LocalDate dataManipulacao = LocalDate.now();
			//System.out.println("Data Original:" + dataManipulacao);
			dtFim = String.valueOf (dataManipulacao);
			// *****************************************************************************8
			
			// ABRE CONEXAO COM O BANCO DE DADOS
			Conexao conProd = new Conexao();
			// *************************************************************
			
			
			System.out.println("Conexao aberta no Postgres...Relatorio Pedagogico");
			Statement stmt = conProd.connection.createStatement();
			// *****************************************************************************8
			
			//ENUM TRAZENDO A TABELA E A SEQUENCE
			TabRelatorioExec  tabExc = TabRelatorioExec.TAB2;
			vTabExecucao = tabExc.getTabExecucao();
			//System.out.println("********"+vTabExecucao);
			//System.out.println(tabExc.getTabExecucao());	
			
			
			SeqTabelasRelatExec seqTab = SeqTabelasRelatExec.SEQ2;
			vSeqTabela = seqTab.getSeqTabela();
			//System.out.println("********"+vSeqTabela);
			//System.out.println(seqTab.getSeqTabela());
			
			
			
			// *****************************************************************************8
			// PREPARA PARA EXECUTAR O SCRIPT
			Statement stmt1 = conProd.connection.createStatement();
			String sqlTrunc = "truncate table "+vTabExecucao;
			stmt1.executeUpdate(sqlTrunc);
			stmt1.close();
			
			// *****************************************************************************8
			
			Statement stmt2 = conProd.connection.createStatement();
			String sqlSeq = "ALTER SEQUENCE " +vSeqTabela+ " RESTART WITH 1";
			stmt2.executeUpdate(sqlSeq);
			stmt2.close();
			
			// *****************************************************************************8
			

					
			String sqlCursor = "SELECT  ESCOLA_ID,ESCOLA,ID_MUNICIPIO,MUNICIPIO,ID_ESTADO,SIGLA_ESTADO,ESTADO,TURMA_ID,TURMA,SERIE_ID,PROFESSOR,JOGO,ALUNO,USERID,QTDE FROM report.materializada_report_ilhas";

			// PREPARA PARA TRAZER OS VALORES DO SCRIPT
			PreparedStatement stmtCursor = conProd.connection.prepareStatement(sqlCursor);
			// TRAZ OS RESULTADOS DO SELECT
			ResultSet rsCursor = stmtCursor.executeQuery();
			// TRAZ OS VALORES E IMPORTA NAS VARIAVEIS
			
			
			while (rsCursor.next()) {

					
				vIdEscola 			= rsCursor.getString("ESCOLA_ID");
				vEscola 			= rsCursor.getString("ESCOLA");
				vIdMunicipio 		= rsCursor.getString("ID_MUNICIPIO");
				VMunicipio 			= rsCursor.getString("MUNICIPIO");
				vIdEstado 			= rsCursor.getString("ID_ESTADO");
				vSiglaEstado 		= rsCursor.getString("SIGLA_ESTADO");
				vNameEstado 		= rsCursor.getString("ESTADO");
				vTurma_id 			= rsCursor.getString("TURMA_ID");
				vTurma 				= rsCursor.getString("TURMA");
				vSerieId			= rsCursor.getString("SERIE_ID");
				vProfessor 			= rsCursor.getString("PROFESSOR");
				vJogo 				= rsCursor.getString("JOGO");
				vAluno 				= rsCursor.getString("ALUNO");
				vUserId 			= rsCursor.getString("USERID");
				vVerificAlun		= rsCursor.getInt("QTDE");
					
					
					if (vVerificAlun >= 1) {
					vAluno = vAluno.replaceAll("'", "''");
					

						// -#
						String sql1 = "select distinct pr.userid," + "pr.abilityid , " + "ab.name,"
						// + "cast(pr.creationdatetime as date) as dt, "
								+ "sum(pr.starscount)as qtd_estrelas," + "count(pr.starscount)*3 as qtd_jogadas, "
								+ "'0' as perc," + "st.pathid," + "pt.name as ilhas \r\n"
								+ "from ilhas.userprogress pr,\r\n" + "ilhas.ability ab,\r\n" + "ilhas.stage st,\r\n"
								+ "ilhas.stageability sta,\r\n" + "ilhas.path pt\r\n" + "where pr.userid = " + vUserId
								+ " \r\n" + "and ab.id = pr.abilityid \r\n" + "and sta.abilityid = ab.id \r\n"
								+ "and sta.stageid = st.id \r\n" + "--and pr.abilityid = 1 \r\n"
								+ "and st.id = pr.stageid \r\n" + "and st.pathid = pt.id \r\n" + "--and pt.id = 1 \r\n"
								+ "and cast(pr.creationdatetime as date) >= '" + dtIni + "'  \r\n"
								+ "and cast(pr.creationdatetime as date) < '" + dtFim + "'  \r\n"
								+ "group by pr.userid,pr.abilityid , ab.name,pt.name,pr.userid," + "st.pathid \r\r"
								+ "order by pr.abilityid 	";

						// PREPARA PARA TRAZER OS VALORES DO SCRIPT
						PreparedStatement stmtSql1 = conProd.connection.prepareStatement(sql1);
						// TRAZ OS RESULTADOS DO SELECT
						ResultSet rsSql1 = stmtSql1.executeQuery();
						// TRAZ OS VALORES E IMPORTA NAS VARIAVEIS
						while (rsSql1.next()) {

							vSql1_userid 		= rsSql1.getString("userid");
							vSql1_abilityid 	= rsSql1.getInt("abilityid");
							vSql1_abiliname 	= rsSql1.getString("name");
							vSql1_qtdestrelas 	= rsSql1.getInt("qtd_estrelas");
							vSql1_qtdjogadas 	= rsSql1.getInt("qtd_jogadas");
							vSql1_percentual 	= rsSql1.getString("perc");
							vSql1_ilhasId 		= rsSql1.getInt("pathid");
							vSql1_ilhas 		= rsSql1.getString("ilhas");

							// percentual
							float Vperc1 = (vSql1_qtdestrelas * 100);
							vperc2 = (Vperc1 / vSql1_qtdjogadas);

							if (vSql1_qtdestrelas == 1 & vSql1_userid.equals(vUserId) & vCountStars == 0) {
								vCount2_3 = vCount2_3 + 1;
								vCountStars = 1;
							}

							if ((vSql1_qtdjogadas - vSql1_qtdestrelas == 0 || vSql1_qtdjogadas - vSql1_qtdestrelas == 1)
									& vSql1_userid.equals(vUserId)) {

								vCountAcima2_3 = vCountAcima2_3 + 1;
							}

		
							String sqlQtdHab = "select count(sta.stageid) *3 as qtd_start_hability\n"
									+ "					from  ilhas.stage st,\n"
									+ "					ilhas.stageability sta\n"
									+ "					where sta.stageid = st.id\n"
									+ "					and st.pathid = '" + vSql1_ilhasId + "'\n"
									+ "					and sta.abilityid = '" + vSql1_abilityid + "'\n"
									+ "					group by st.pathid, sta.abilityid\n"
									+ "					order by st.pathid";

							// PREPARA PARA TRAZER OS VALORES DO SCRIPT
							PreparedStatement stmtQtdHab = conProd.connection.prepareStatement(sqlQtdHab);
							// TRAZ OS RESULTADOS DO SELECT
							ResultSet rsQtdHab = stmtQtdHab.executeQuery();
							// TRAZ OS VALORES E IMPORTA NAS VARIAVEIS
							while (rsQtdHab.next()) {

								vQtd_start_hability = rsQtdHab.getInt("qtd_start_hability");

							}

							try {

								
								String sql2 = "INSERT INTO "+vTabExecucao+" (id , escola_id , escola , id_municipio,municipio , id_estado , estado , sigla_estado , turma_id , turma , serie_id, professor , id_aluno, aluno , abilityid, abilityname, qtd_win_stars , qtd_disput_star, qtd_start_hability,percentual, total_alunos_abaixos_2_3, total_hability_acima_2_3 , pathid,ilha)VALUES (nextval('"+vSeqTabela+"'),'" + vIdEscola + "','" + vEscola+"','" + vIdMunicipio + "','" + VMunicipio + "','" + vIdEstado + "','"+ vNameEstado + "','" + vSiglaEstado + "','" + vTurma_id + "','" + vTurma + "','"+ vSerieId + "','"+ vProfessor + "','" + vUserId + "','" + vAluno + "','" + vSql1_abilityid + "','"+ vSql1_abiliname + "','" + vSql1_qtdestrelas + "','" + vSql1_qtdjogadas + "','"+ vQtd_start_hability + "','" + vperc2 + "','" + vCount2_3 + "','" + vCountAcima2_3+"','" + vSql1_ilhasId + "','" + vSql1_ilhas + "');";
								gr.writeLog(vProcess,sql2);
								
								vQtdeProcessadas = vQtdeProcessadas+1;
								
							} catch (Exception e) {
								System.out.println(vUserId);
								// AQUI ESCREVE O LOGO
								StringWriter sw = new StringWriter();
								PrintWriter pw = new PrintWriter(sw);
								e.printStackTrace(pw);
								GeraRelatorioTxt.writeLog(12, sw.toString() + "USERID = " + vUserId + " ALUNO = " + vAluno, "0");
							}

							vCreationDt = null;
							vSql1_abilityid = 0;
							vSql1_abiliname = null;
							vSql1_qtdestrelas = 0;
							vSql1_qtdjogadas = 0;
							vperc2 = 0;
							vCount2_3 = 0;
							vCountAcima2_3 = 0;
							vSql1_ilhas = null;
							vSql1_ilhasId = 0;
							vQtd_start_hability = 0;
							vVerificAlun = 0;

						}
					}
			}
			  
			  //**********************************************************************8
		      	//AQUI FAÇO O INSERT EM LOTE
		      	
		      	exc.execLote(vProcess);
		      	//**********************************************************************8
			    
			    conProd.close();
				stmtCursor.close();
				
				// conDev.close ( );
				stmt.close();

	}catch (Exception e) {
		
		// AQUI ESCREVE O LOGO
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);
				GeraRelatorioTxt.writeLog(12, sw.toString() + "USERID = " + vUserId + " ALUNO = " + vAluno, "0");
				return "1";
	}
		return "0";

}
}