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

import connectionfactory.Conexao;
import model.SeqTabelasRelatExec;
import model.TabRelatorioExec;


public class GeraRelatFon {
	private static String vIdHability;
	private static String vNomeHability;
	private static String vUserId;
	private static String vContentName;
	private static int vStarsCount;
	private static int vCountHab;
	
	
	private static int vVerificAlun;
	
	private static String v1valida;
	private static String v2valida;
	private static String v3valida;
	private static String v4valida;
	private static String v5valida;
	private static String v6valida;
	private static String v7valida;
	private static String v8valida;
	private static String v9valida;
	private static String v10valida;
	private static String v11valida;
	private static String v12valida;
	private static String v13valida;
	private static String v14valida;
	private static String v15valida;
	private static String v16valida;
	private static String v17valida;
	private static String v18valida;
	private static String v19valida;
	private static String v20valida;
	private static String v21valida;
	private static String v22valida;
	private static String v23valida;
	private static String v24valida;
	private static String v25valida;
	private static String v26valida;
	private static String v27valida;
	private static String v28valida;
	private static String v29valida;
	private static String v30valida;
	private static String v31valida;
	private static String vProcess;

	private static double vQtdLinhas = 0;
	private static int vQtdeProcessos= 0;

	private static double vCalculadora;
	private static String vTurma_id;
	private static String vTurma;
	private static String vAluno;

	private static String dtFim;
	private static String dtIni;
	
	private static String vSeqTabela;
	private static String vTabExecucao;
	private static int vQtdeProcessadas = 0;

	public static String validaContentName(double vContName) {
		String rec = "";

		rec = (vCalculadora > 1 || vCalculadora >= 3) ? "S" : "F";

		if (rec == null || rec.isEmpty()) {
			rec = "";
		}

		return rec;

	}
	
	public static void deleteLog() {

		File arquivo = new File("/tmp/FONEMAscriptreportlote.sql");

		arquivo.delete();
		
		
	}
	
	public static void iniciaVariaveis() {
		
		v1valida = "";
		v2valida = "";
		v3valida = "";
		v4valida = "";
		v5valida = "";
		v6valida = "";
		v7valida = "";
		v8valida = "";
		v9valida = "";
		v10valida = "";
		v11valida = "";
		v12valida = "";
		v13valida = "";
		v14valida = "";
		v15valida = "";
		v16valida = "";
		v17valida = "";
		v18valida = "";
		v19valida = "";
		v20valida = "";
		v21valida = "";
		v22valida = "";
		v23valida = "";
		v24valida = "";
		v25valida = "";
		v26valida = "";
		v27valida = "";
		v28valida = "";
		v29valida = "";
		v30valida = "";
		v31valida = "";
	}

	// public static void main(String[] args) throws ParseException, SQLException {

	public String vGRF() throws ParseException {
		
		try {
			
		iniciaVariaveis();
				
    	// AQUI PARA GERAR O RELATORIO DO PROCESSO
		vProcess = "FONEMA";
		  
		 GeraRelatorioTxt grTxt = new GeraRelatorioTxt();
		 deleteLog(); 
		 WriteLog gr = new WriteLog();
		 ExecLote exc = new ExecLote();
		// *****************************************************************************8
		
		// ABRE CONEXAO COM O BANCO DE DADOS
		Conexao conProd = new Conexao();  
		
		//******************************************************************
		System.out.println("Conexao aberta no Postgres...Relatorio Fonema");
		Statement stmt = conProd.connection.createStatement();
		// *****************************************************************************8
		//ENUM TRAZENDO A TABELA E A SEQUENCE
		TabRelatorioExec  tabExc = TabRelatorioExec.TAB4;
		vTabExecucao = tabExc.getTabExecucao();
		//System.out.println("********"+vTabExecucao);
		//System.out.println(tabExc.getTabExecucao());	
		
		
		SeqTabelasRelatExec seqTab = SeqTabelasRelatExec.SEQ4;
		vSeqTabela = seqTab.getSeqTabela();
		//System.out.println("********"+vSeqTabela);
		//System.out.println(seqTab.getSeqTabela());
		
		
		
		// *****************************************************************************8
		
		Statement stmt1 = conProd.connection.createStatement();
		String sql1 = "truncate table "+vTabExecucao;
		stmt1.executeUpdate(sql1);
		stmt1.close();
		// *****************************************************************************8
		
		Statement stmt2 = conProd.connection.createStatement();
		String sqlTrunc = "ALTER SEQUENCE " +vSeqTabela+ " RESTART WITH 1";
		stmt2.executeUpdate(sqlTrunc);
		stmt2.close();
		// *****************************************************************************8
		
		LocalDate dataManipulacao = LocalDate.now();
		// System.out.println("Data Original:" + dataManipulacao);
		dtFim = String.valueOf(dataManipulacao);

		// *****************************************************************************8
	
		String sqlCursor = "SELECT  ESCOLA_ID,ESCOLA,ID_MUNICIPIO,MUNICIPIO,ID_ESTADO,SIGLA_ESTADO,ESTADO,TURMA_ID,TURMA,PROFESSOR,JOGO,ALUNO,USERID,QTDE FROM report.materializada_report_ilhas";

		PreparedStatement stmtCursor = conProd.connection.prepareStatement(sqlCursor);
		ResultSet rsCursor = stmtCursor.executeQuery();
		
		
		 while (rsCursor.next()) {

			vUserId = rsCursor.getString("USERID");
			vTurma_id = rsCursor.getString("TURMA_ID");
			vTurma = rsCursor.getString("TURMA");
			vAluno = rsCursor.getString("ALUNO");
			vVerificAlun = rsCursor.getInt("QTDE");

			vAluno = vAluno.replaceAll("'", "''");
		
			if (vVerificAlun >= 1) {

				String sqlFonema = "select sa.abilityid," + "ab.name AS ability_name\n" + "from  ilhas.stage st ,"
						+ "ilhas.stagetype stgy," + "ilhas.stageability sa, " + "ilhas.ability ab\n"
						+ "where sa.abilityid in (6,7,8,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35) \n"
						+ "and stgy.id = st.stagetypeid\n" + "and sa.stageid = st.id\n" + "and sa.abilityid = ab.id\n"
						+ "group by sa.abilityid,ab.name\n" + "order by 1      \n" + "";

				PreparedStatement stmtFonema = conProd.connection.prepareStatement(sqlFonema);
				ResultSet rsFonema = stmtFonema.executeQuery();
				while (rsFonema.next()) {

					vIdHability = rsFonema.getString("abilityid");
					vNomeHability = rsFonema.getString("ability_name");

					String sqlLtr = "SELECT CONTENT_NAME,STARS_COUNT,ability_id,ability_name from report.materializada_hab_ilhas	where userid = "
							+ vUserId + " and ability_id = " + vIdHability + "";

					boolean vVerifAlun = false;
					PreparedStatement stmtLtr = conProd.connection.prepareStatement(sqlLtr);
					ResultSet rsLtr = stmtLtr.executeQuery();
		
					while (rsLtr.next()) {

						vContentName = rsLtr.getString("CONTENT_NAME");
						vStarsCount = rsLtr.getInt("STARS_COUNT");
						vIdHability = rsLtr.getString("ability_id");
						vNomeHability = rsLtr.getString("ability_name");

						vCountHab = vStarsCount + vCountHab;

						vQtdLinhas = vQtdLinhas + 1;
						vVerifAlun = true;

						if (vCountHab == 0 & vQtdLinhas == 0) {

							vCalculadora = 0;

						} else {

							vCalculadora = vCountHab / vQtdLinhas;

							switch (vContentName) {
							case "/a/":

								v1valida = validaContentName(vCalculadora);

								vCalculadora = 0;
								break;
							case "/é/":

								v2valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/ê/":

								v3valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/i/":

								v4valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/ó/":

								v5valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/ô/":

								v6valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/u/":

								v7valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/l/":

								v8valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/m/":

								v9valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/n/":

								v10valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/am/":

								v11valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/em/":

								v12valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/im/":

								v13valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/om/":

								v14valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/um/":

								v15valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/lh/":

								v16valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/nh/":

								v17valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/s/":

								v18valida = validaContentName(vCalculadora);
								vCalculadora = 0;
								break;
							case "/z/":

								v19valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/f/":

								v20valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/v/":

								v21valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/R/":

								v22valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/r/":

								v23valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/ch/":

								v24valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/j/":

								v25valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/k/":

								v26valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/d/":

								v27valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/b/":

								v28valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/p/":

								v29valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/t/":

								v30valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;
							case "/gh/":

								v31valida = validaContentName(vCalculadora);
								vCalculadora = 0;

								break;

							default:
								// System.out.println("Este não é válido!");
							}
						}

						vCountHab = 0;
						vQtdLinhas = 0;

					}
					
					rsLtr.close();
					stmtLtr.close();
					
					try {

						Statement stmtInsertTable = conProd.connection.createStatement();

						String sqlInsert = "INSERT INTO "+vTabExecucao+" (id ,userid ,aluno , turma_id ,turma ,abilityid ,ability_name , content_1,content_2 ,content_3,content_4,content_5,content_6 ,content_7 ,content_8 ,content_9,content_10 ,content_11 ,content_12,content_13,content_14 ,content_15,content_16,content_17,content_18 ,content_19,content_20 ,content_21 ,content_22,content_23,content_24,content_25,content_26,content_27,content_28,content_29,content_30,content_31) VALUES (nextval('"+vSeqTabela+"'),'" + vUserId + "','"+ vAluno + "','" + vTurma_id + "','" + vTurma + "','" + vIdHability + "','"+ vNomeHability + "','" + v1valida + "','" + v2valida + "','" + v3valida + "','"+ v4valida + "','" + v5valida + "','" + v6valida + "','" + v7valida + "','" + v8valida+ "','" + v9valida + "','" + v10valida + "','" + v11valida + "','" + v12valida + "','"+ v13valida + "','" + v14valida + "','" + v15valida + "','" + v16valida + "','"+ v17valida + "','" + v18valida + "','" + v19valida + "','" + v20valida + "','"+ v21valida + "','" + v22valida + "','" + v23valida + "','" + v24valida + "','"+ v25valida + "','" + v26valida + "','" + v27valida + "','" + v28valida + "','"+ v29valida + "','" + v30valida + "','" + v31valida + "'" + ");";

						
						gr.writeLog(vProcess,sqlInsert);
						
						iniciaVariaveis();
						vQtdeProcessos = vQtdeProcessos+1;

					} catch (SQLException e) {
						System.out.println(vUserId);
						// AQUI ESCREVE O LOGO
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						GeraRelatorioTxt.writeLog(11, sw.toString() + "USERID = " + vUserId + " ALUNO = " + vAluno, "0");
					}

					

				}
				stmtFonema.close();
				rsFonema.close();
				
				
			}
		}
		 
		 
	  //**********************************************************************8
      	//AQUI FAÇO O INSERT EM LOTE
      	
      	exc.execLote(vProcess);
      	//**********************************************************************8
		
		
		 stmtCursor.close();
		 rsCursor.close();
		 conProd.close();
		 stmt.close();
		
	} catch (Exception e) {
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
