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


public class GeraRelatorioCaligrafia {
	private static String vIdHability;
	private static String vIdIlha;
	private static String vNomeHability;
	private static String vUserId;
	private static String vContentName;
	private static int vStarsCount;
	private static int vCountHab;
	private static int vRun = 0;
	
	
	private static String vA;
	private static String vB;
	private static String vC;
	private static String vD;
	private static String vE;
	private static String vF;
	private static String vG;
	private static String vH;
	private static String vI;
	private static String vJ;
	private static String vL;
	private static String vM;
	private static String vN;
	private static String vO;
	private static String vP;
	private static String vQ;
	private static String vR;
	private static String vS;
	private static String vT;
	private static String vU;
	private static String vV;
	private static String vX;
	private static String vZ;
	private static String vW;
	private static String vY;
	private static String vK;
	
	
	private static int vACount;
	private static int vBCount;
	private static int vCCount;
	private static int vDCount;
	private static int vECount;
	private static int vFCount;
	private static int vGCount;
	private static int vHCount;
	private static int vICount;
	private static int vJCount;
	private static int vLCount;
	private static int vMCount;
	private static int vNCount;
	private static int vOCount;
	private static int vPCount;
	private static int vQCount;
	private static int vRCount;
	private static int vSCount;
	private static int vTCount;
	private static int vUCount;
	private static int vVCount;
	private static int vXCount;
	private static int vZCount;
	private static int vWCount;
	private static int vYCount;
	private static int vKCount;
	
	
	
	private static String vAvalida;
	private static String vBvalida;
	private static String vCvalida;
	private static String vDvalida;
	private static String vEvalida;
	private static String vFvalida;
	private static String vGvalida;
	private static String vHvalida;
	private static String vIvalida;
	private static String vJvalida;
	private static String vLvalida;
	private static String vMvalida;
	private static String vNvalida;
	private static String vOvalida;
	private static String vPvalida;
	private static String vQvalida;
	private static String vRvalida;
	private static String vSvalida;
	private static String vTvalida;
	private static String vUvalida;
	private static String vVvalida;
	private static String vXvalida;
	private static String vZvalida;
	private static String vWvalida;
	private static String vYvalida;
	private static String vKvalida;
	private static String vProcess;
	
	private static double vQtdLinhas=0;
	
	private  static double vCalculadora;
	private static String vTurma_id;
	private static String vTurma;
	private static String vAluno;


	private static Connection conProd;
	private static Connection conDev;
	
	private static String dtFim;
	private static String dtIni;
	
	private static int vVerificAlun;

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
	
	//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
	//※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
	
	public static void iniciaVariaveis() {
		
		 vAvalida = "";
		 vBvalida ="";
		 vCvalida ="";
		 vDvalida ="";
		 vEvalida ="";
		 vFvalida ="";
		 vGvalida ="";
		 vHvalida ="";
		 vIvalida ="";
		 vJvalida ="";
		 vKvalida ="";
		 vLvalida =""; 
		 vMvalida ="";
		 vNvalida ="";
		 vOvalida ="";
		 vPvalida ="";	
		 vQvalida ="";
		 vRvalida ="";
		 vSvalida ="";
		 vTvalida ="";
		 vUvalida ="";
		 vVvalida ="";	
		 vWvalida ="";
		 vXvalida ="";
		 vYvalida =""; 
		 vZvalida ="";
		
	}

	public static void deleteLog() {

		File arquivo = new File("/tmp/CALIGRAFIAscriptreportlote.sql");

		arquivo.delete();
		
		
	}
	
	//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
	//※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
	//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆

	public  String vGRC() throws ParseException, SQLException {
		
	 // AQUI PARA GERAR O RELATORIO DO PROCESSO
	 vProcess = "CALIGRAFIA";
	  
	 GeraRelatorioTxt grTxt = new GeraRelatorioTxt();
	 deleteLog(); 
	 WriteLog gr = new WriteLog();
	 ExecLote exc = new ExecLote();
	 
	    //⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
	
		try {
		//INICIA AS VARIAVEIS
		iniciaVariaveis();
		
		dtIni = "2018-01-01";
		
		LocalDate dataManipulacao = LocalDate.now();
		dtFim = String.valueOf (dataManipulacao);
		
		//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
		// ABRE CONEXAO COM O BANCO DE DADOS
		Conexao conProd = new Conexao();  
		
		//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
		
		
		Statement stmt = conProd.connection.createStatement();
		
		//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
		
		//ENUM TRAZENDO A TABELA E A SEQUENCE
		TabRelatorioExec  tabExc = TabRelatorioExec.TAB5;
		vTabExecucao = tabExc.getTabExecucao();
		
		
		SeqTabelasRelatExec seqTab = SeqTabelasRelatExec.SEQ5;
		vSeqTabela = seqTab.getSeqTabela();
		
		
		//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕			
		
		// PREPARA PARA EXECUTAR O SCRIPT
		Statement stmt1 = conProd.connection.createStatement();
		String sqlTrunc = "truncate table "+vTabExecucao;
		stmt1.executeUpdate(sqlTrunc);
		stmt1.close();
		
		
		//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
		
		// PREPARA PARA EXECUTAR O SCRIPT
		Statement stmt2 = conProd.connection.createStatement();
		String sqlSeq = "ALTER SEQUENCE " +vSeqTabela+ " RESTART WITH 1";
		stmt2.executeUpdate(sqlSeq);
		stmt2.close();
		
		
		//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
		
		String sqlCursor = "SELECT  ESCOLA_ID,ESCOLA,ID_MUNICIPIO,MUNICIPIO,ID_ESTADO,SIGLA_ESTADO,ESTADO,TURMA_ID,TURMA,PROFESSOR,JOGO,ALUNO,USERID,QTDE FROM report.materializada_report_ilhas";
		// PREPARA PARA TRAZER OS VALORES DO SCRIPT
		PreparedStatement stmtCursor1 = conProd.connection.prepareStatement(sqlCursor);
		// TRAZ OS RESULTADOS DO SELECT
		ResultSet rsCursor1 = stmtCursor1.executeQuery();
		// TRAZ OS VALORES E IMPORTA NAS VARIAVEIS
		
		while (rsCursor1.next()) {

			vUserId 		= rsCursor1.getString("USERID");
			vTurma_id 		= rsCursor1.getString("TURMA_ID");
			vTurma 			= rsCursor1.getString("TURMA");
			vAluno 			= rsCursor1.getString("ALUNO");
			vVerificAlun    = rsCursor1.getInt("QTDE");
		
		
		if (vVerificAlun >= 1) {
		
		
	
			String sqlCaligrafia = "select sa.abilityid,st.pathid,\n" + 
					" ab.name ||' '|| substr(p.name, 12, 20) as ability_name\n" + 
					"from  ilhas.stage st ,\n" + 
					"     ilhas.stagetype stgy,\n" + 
					"     ilhas.stageability sa, \n" + 
					"     ilhas.ability ab,\n" + 
					"     ilhas.path p\n" + 
					"where st.pathid in(1000,2000,3000,4000)\n" + 
					"and stgy.id = st.stagetypeid\n" + 
					"and sa.stageid = st.id\n" + 
					"and sa.abilityid = ab.id\n" + 
					"and st.pathid = p.id\n" + 
					"--and st.stagetypeid not in (20)\n" + 
					"group by sa.abilityid,ab.name,p.name,st.pathid\n" + 
					"order by 2    \n" + 
					";";
			
			   // PREPARA PARA TRAZER OS VALORES DO SCRIPT
			PreparedStatement stmtCaligrf = conProd.connection.prepareStatement(sqlCaligrafia);
			// TRAZ OS RESULTADOS DO SELECT
			ResultSet rsCaligrf = stmtCaligrf.executeQuery();
			// TRAZ OS VALORES E IMPORTA NAS VARIAVEIS
			while (rsCaligrf.next()) {

		
			vIdHability 		= rsCaligrf.getString("abilityid");
			vIdIlha				= rsCaligrf.getString("pathid");
			vNomeHability 		= rsCaligrf.getString("ability_name");
			
				 
			 String sqlLtr = "SELECT CONTENT_NAME,STARS_COUNT from report.materializada_report_calig where pathid = '"+vIdIlha+"' and userid = "+ vUserId + " and ability_id = " + vIdHability + "";
			 
			    
			    
			    // PREPARA PARA TRAZER OS VALORES DO SCRIPT
				PreparedStatement stmtLtr = conProd.connection.prepareStatement(sqlLtr);
				// TRAZ OS RESULTADOS DO SELECT
				ResultSet rsLtr = stmtLtr.executeQuery();
				// TRAZ OS VALORES E IMPORTA NAS VARIAVEIS
			    
			    while (rsLtr.next()) {

					
			    	vContentName 		= rsLtr.getString("CONTENT_NAME");
			    	vStarsCount 		= rsLtr.getInt("STARS_COUNT");
					
			    	vCountHab = vStarsCount+vCountHab;
					vQtdLinhas = vQtdLinhas+1;
					
				
			
			//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
			    
			    if(vCountHab ==0 & vQtdLinhas == 0 ) {
			    	
			    	vCalculadora = 0;
			    	
			    }else {
			    	
			    	vCalculadora = vCountHab / vQtdLinhas;
			    	
			    
			    
			    switch (vContentName) {
					case "a":
						
							vAvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
							
						break;
					case "b":
						
							vBvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
							
						break;
					case "c":
						
							vCvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
							
						break;
					case "d":
						
							vDvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
							
						break;
					case "e":
						
							vEvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
							
						break;
					case "f":
						
							vFvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
							
						break;
					case "g":
						
							vGvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
							break;
					case "h":
						
							vHvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
							
						break;
					case "i":
						
							vIvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
						
						break;
					case "j":
						
							vJvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
						
						break;
					case "l":
						
							vLvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
						
						break;
					case "m":
						
							vMvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
						
						break;
					case "n":
						
							vNvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
						
						break;
					case "o":
						
							vOvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
							
						break;
					case "p":
						
							vPvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
						
						break;
					case "q":
						
							vQvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
						
						break;
					case "r":
						
							vRvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
						
						break;
					case "s":
						
							vSvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
							
						break;
					case "t":
						
							vTvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
						
						break;
					case "u":
						
							vUvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
						
						break;
					case "v":
						
							vVvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
						
						break;
					case "x":
						
							vXvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
						
						break;
					case "z":
						
							vZvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
						
						break;
					case "w":
						
							vWvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
							
						break;
					case "k":
						
							vKvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
						
						break;
					case "y":
						
							vYvalida = validaContentName(vCalculadora); 
							vCalculadora = 0;
						
						break;
																							
							
					default:
					//System.out.println("Este não é válido!");
					}
		    	
		    
			    
			    vCountHab = 0;
			    vQtdLinhas = 0;
		    
			    }
		    
			    }
			    
			  //⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
			    
			    try {
			
					
					String sqlInsert = "INSERT INTO "+vTabExecucao+" (id , userid, aluno, turma_id, turma , abilityid, ability_name , content_a, content_b, content_c,content_d, content_e,content_f, content_g , content_h, content_i,content_j, content_k , content_l, content_m, content_n, content_o , content_p, content_q, content_r, content_s , content_t, content_u ,content_v, content_w , content_x, content_y, content_z ) VALUES (nextval('"+vSeqTabela+"'),'"+ vUserId + "','"+ vAluno + "','"+ vTurma_id + "','"+ vTurma + "','"+ vIdHability + "','"+ vNomeHability + "','"+ vAvalida +"','"+ vBvalida + "','"+ vCvalida +"','"+ vDvalida +"','"+ vEvalida +"','"+ vFvalida + "','"+ vGvalida + "','"+ vHvalida + "','"+ vIvalida + "','"+ vJvalida + "','"+ vKvalida +"','"+ vLvalida + "','"+ vMvalida + "','"+ vNvalida+"','"+ vOvalida + "','"+ vPvalida+"','"+ vQvalida + "','"+ vRvalida+"','"+ vSvalida + "','"+ vTvalida+"','"+ vUvalida + "','"+ vVvalida+"','"+ vWvalida+"','"+ vXvalida+"','"+ vYvalida+"','"+ vZvalida+"');";
			           
					gr.writeLog(vProcess,sqlInsert);    
					    
					    
					    iniciaVariaveis();
					    vQtdeProcessadas = vQtdeProcessadas+1;
			
						}catch (Exception e) {
							//System.out.println(vUserId);
							StringWriter sw = new StringWriter();
							PrintWriter pw = new PrintWriter(sw);
							e.printStackTrace(pw);
							GeraRelatorioTxt.writeLog(12, sw.toString() + "USERID = " + vUserId + " ALUNO = " + vAluno, "0");
							}
							
							
							}
			       }
			
         }
		
		//⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕⁕
		//※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※※
		//◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆
		
		
		//**********************************************************************8
      	//AQUI FAÇO O INSERT EM LOTE
      	
      	exc.execLote(vProcess);
      	//**********************************************************************8
      	
      	conProd.close ( );
		stmt.close ( );
		
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
