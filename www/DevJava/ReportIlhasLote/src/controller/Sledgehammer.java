package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

import connectionfactory.Conexao;


public class Sledgehammer {
private static String vQtde;
private static long start;
private static long end;
private static String vRuntime;
private static String vDtIni;
private static String vDtFim;
private static String vDados;

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

private String calculaTempo(long l) {
	String vRet;
	long sec = l / 1000;
    long min = l / (60 * 1000);
    long hour = l / (60 * 60 * 1000);
    
    

    if (hour > 0) {
        System.out.println("Total da operacao " + hour + "hs");
    } else if (min > 0) {
        System.out.println("Total da operacao " + min + "min");
    } else if (sec > 0) {
        System.out.println("Total da operacao " + sec + "s");
   }
    
    vRet =  hour + ":" +min+ ":"+sec;
    
    return vRet;
}


	public String mallet() throws SQLException {
		
		vQtde = "0";
		
		start = System.currentTimeMillis();
    	// AQUI PEGO A DATA ATUAL DO SISTEMA
		 LocalDate dataManipulacao = LocalDate.now();
		 LocalTime time = LocalTime.now();
		 WriteReport wr = new WriteReport();
			 
		 vDtIni = String.valueOf(time);
		 
		 
		try {
		
			
		//PRODUCAO
		// *************************************************************
		Conexao conProd = new Conexao();  
		
		Statement stmtUp2 = conProd.connection.createStatement();
		
		// PREPARA PARA EXECUTAR O SCRIPT
		Statement stmtLog = conProd.connection.createStatement();
		String sqlLog = "truncate table report.tb_check_log_ilhas";
		stmtLog.executeUpdate(sqlLog);
		
		
		// PREPARA PARA EXECUTAR O SCRIPT
		Statement stmt2 = conProd.connection.createStatement();
		String sqlTrunc = "truncate table report.tb_log_ilhas_overtime";
		stmt2.executeUpdate(sqlTrunc);
		
		// PREPARA PARA EXECUTAR O SCRIPT
		String sqlAlter = "ALTER SEQUENCE report.seq_tb_log_ilhas_obertime RESTART WITH 1";
		stmt2.executeUpdate(sqlAlter);
		stmt2.close();
		
		String sqlVerif = "\n" + 
				" SELECT count(distinct userid)qtde \n" + 
				" FROM ilhas.USERLOG\n" + 
				" WHERE USERLOGTYPEID >= 5000\n" + 
				" AND  (finishdatetime - STARTDATETIME) >= '01:00:00'";
		
		PreparedStatement stmtCursor = conProd.connection.prepareStatement(sqlVerif);
		ResultSet rsCursor = stmtCursor.executeQuery();
		while (rsCursor.next()) {

			vQtde = rsCursor.getString("qtde");
			System.out.println("QTDE => "+vQtde);

		}
		
		
		
		if(vQtde.equals("0") ) {
			
			return vQtde;
			
		}else {
			
			System.out.println("Executando a marreta...!");
				
		String sql= "DO $$DECLARE   \n" + 
				"				  vrTempo      character varying(8);  \n" + 
				"				  vrTempoF     character varying(8);  \n" + 
				"				  vrCont       integer := 0;  \n" + 
				"				  vrDif        character varying(400);  \n" + 
				"				  v1           timestamp;  \n" + 
				"				  v2           timestamp;  \n" + 
				"				  vrVerifHH    timestamp without time zone;  \n" + 
				"				  vrVerifCorr  timestamp without time zone;  \n" + 
				"				  vrVerifStart timestamp without time zone;  \n" + 
				"				  vrVerifEnd   timestamp without time zone;  \n" + 
				"				  vrVerifDt    timestamp without time zone;  \n" + 
				"				  vrVerifDt5   timestamp without time zone;  \n" + 
				"				  vVerifTempo  character varying(80);  \n" + 
				"				   \n" + 
				"				   \n" + 
				"				  vDD         integer;  \n" + 
				"				  vHH         integer;  \n" + 
				"				  vMM         integer;  \n" + 
				"				  vSS         integer;  \n" + 
				"				  vANO	      integer; 	 \n" + 
				"				  V_UP        INTEGER;  \n" + 
				"				  v_rec       character varying(400);  \n" + 
				"				  \n" + 
				"				  \n" + 
				"				  rec_film    RECORD;  \n" + 
				"				    cur_films CURSOR  \n" + 
				"				  FOR SELECT *  \n" + 
				"				      FROM ilhas.USERLOG LOG  \n" + 
				"					     INNER JOIN web.user U ON U.ID = LOG.USERID  \n" + 
				"					     INNER JOIN web.studentuser su ON su.userid = u.ID  \n" + 
				"					     INNER JOIN web.school sc ON sc.ID = su.schoolid  \n" + 
				"				      WHERE LOG.USERLOGTYPEID >= 5000  \n" + 
				"					AND (FINISHDATETIME - STARTDATETIME) >= '01:00:00';  \n" + 
				"				  \n" + 
				"				  \n" + 
				"				BEGIN  \n" + 
				"				  \n" + 
				"				  \n" + 
				"				  V_UP := 0;  \n" + 
				"				  \n" + 
				"				  IF pg_trigger_depth() <> 1  \n" + 
				"				  THEN  \n" + 
				"				  \n" + 
				"				  END IF;  \n" + 
				"				  \n" + 
				"				  OPEN cur_films;  \n" + 
				"				  \n" + 
				"				  LOOP  \n" + 
				"				    -- fetch row into the film  \n" + 
				"				    FETCH cur_films INTO rec_film;  \n" + 
				"				    -- exit when no more row to fetch  \n" + 
				"				    EXIT WHEN NOT FOUND;  \n" + 
				"				  \n" + 
				"				   -- RAISE NOTICE 'ID %', rec_film.id;  \n" + 
				"				  \n" + 
				"				    SELECT SUBSTR(to_char((um.FINISHDATETIME - um.STARTDATETIME), 'YYYY-MM-DD HH24:MI:SS'), 9, 2) AS DD,  \n" + 
				"					   SUBSTR(to_char((um.FINISHDATETIME - um.STARTDATETIME), 'HH24:MI:SS'), 1, 2)            as HH,  \n" + 
				"					   SUBSTR(to_char((um.FINISHDATETIME - um.STARTDATETIME), 'HH24:MI:SS'), 4, 2)            as MM,  \n" + 
				"					   SUBSTR(to_char((um.FINISHDATETIME - um.STARTDATETIME), 'HH24:MI:SS'), 7, 10)           as SS  \n" + 
				"					INTO vDD, vHH, vMM, vSS  \n" + 
				"				    FROM ILHAS.USERLOG um  \n" + 
				"				    WHERE id = rec_film.id;  \n" + 
				"				  \n" + 
				"				  \n" + 
				"				    --RAISE NOTICE 'ID % hh % mm % ss %', rec_film.id, vHH, vMM, vSS;  \n" + 
				"				  \n" + 
				"				    vrVerifStart = rec_film.STARTDATETIME;  \n" + 
				"				    vrVerifEnd = rec_film.FINISHDATETIME;  \n" + 
				"				    vVerifTempo = vDD || ' DIAS < = > ' || vHH || 'h : ' || vMM || 'm : ' || vSS || 's ';  \n" + 
				"				 \n" + 
				"					vANO	 = SUBSTR(to_char(vrVerifStart, 'YYYY-MM-DD HH24:MI:SS'), 0, 5); \n" + 
				"				    --RAISE NOTICE 'ANO%', vANO;  \n" + 
				"				 \n" + 
				"				     IF vANO = '1970' THEN  \n" + 
				"				      RAISE NOTICE 'entrou 1970%',vANO; \n" + 
				"					    SELECT (um.FINISHDATETIME) - interval '5min'  \n" + 
				"						INTO vrVerifHH FROM ILHAS.USERLOG um WHERE id = rec_film.id;  \n" + 
				"				  \n" + 
				"				  \n" + 
				"					    RAISE NOTICE '=X2';  \n" + 
				"					    RAISE NOTICE 'DATA CORRETA%', vrVerifHH;  \n" + 
				"					    UPDATE ILHAS . USERLOG SET STARTDATETIME = vrVerifHH WHERE id = rec_film.id;  \n" + 
				"				  \n" + 
				"					    insert into report.tb_log_ilhas_overtime  \n" + 
				"					    values (nextval('report.seq_tb_log_ilhas_obertime'),  \n" + 
				"								 rec_film.id, \n" + 
				"							 rec_film.userid,  \n" + 
				"						    vrVerifStart,  \n" + 
				"						    vrVerifEnd,  \n" + 
				"						    vrVerifHH,  \n" + 
				"						    vVerifTempo);  \n" + 
				"				     END IF;                \n" + 
				"				 \n" + 
				"				  \n" + 
				"				    IF vDD = 0  \n" + 
				"				    THEN  \n" + 
				"				      IF vrVerifStart = '01/01/0001 00:00:00' THEN \n" + 
				"				  \n" + 
				"					RAISE NOTICE 'ID % hh % mm % ss %', rec_film.id, vHH, vMM, vSS;  \n" + 
				"				  \n" + 
				"				      ELSE  \n" + 
				"				  \n" + 
				"				  \n" + 
				"					CASE  \n" + 
				"				 \n" + 
				"					  WHEN vHH > 00 AND vMM >= 05  \n" + 
				"					  THEN  \n" + 
				"					    RAISE NOTICE 'X1';  \n" + 
				"					    SELECT (um.STARTDATETIME) + interval '5min'  \n" + 
				"						INTO vrVerifHH FROM ILHAS.USERLOG um WHERE id = rec_film.id;  \n" + 
				"				  \n" + 
				"				  \n" + 
				"					    RAISE NOTICE '=X2';  \n" + 
				"					    RAISE NOTICE 'DATA CORRETA%', vrVerifHH;  \n" + 
				"					    UPDATE ILHAS . USERLOG SET FINISHDATETIME = vrVerifHH WHERE id = rec_film.id;  \n" + 
				"				  \n" + 
				"					    insert into report.tb_log_ilhas_overtime  \n" + 
				"					    values (nextval('report.seq_tb_log_ilhas_obertime'),  \n" + 
				"								 rec_film.id, \n" + 
				"							 rec_film.userid,  \n" + 
				"						    vrVerifStart,  \n" + 
				"						    vrVerifEnd,  \n" + 
				"						    vrVerifHH,  \n" + 
				"						    vVerifTempo);  \n" + 
				"					    -------------------------------------------------------------  \n" + 
				"				  \n" + 
				"					    RAISE NOTICE '=X3';  \n" + 
				"				  \n" + 
				"					  WHEN vHH = 00 AND vMM > 05  \n" + 
				"					  THEN  \n" + 
				"					    SELECT (um.STARTDATETIME) + interval '5min'  \n" + 
				"						INTO vrVerifHH FROM ILHAS.USERLOG um WHERE id = rec_film.id;  \n" + 
				"				  \n" + 
				"				  \n" + 
				"					    RAISE NOTICE '2';  \n" + 
				"				  \n" + 
				"					    UPDATE ILHAS . USERLOG SET FINISHDATETIME = vrVerifHH WHERE id = rec_film.id;  \n" + 
				"				  \n" + 
				"					    insert into report.tb_log_ilhas_overtime  \n" + 
				"					    values (nextval('report.seq_tb_log_ilhas_obertime'),  \n" + 
				"									 rec_film.id, \n" + 
				"						    rec_film.userid,  \n" + 
				"						    vrVerifStart,  \n" + 
				"						    vrVerifEnd,  \n" + 
				"						    vrVerifHH,  \n" + 
				"						    vVerifTempo);  \n" + 
				"					-------------------------------------------------------------  \n" + 
				"				  \n" + 
				"				  \n" + 
				"					  WHEN vHH > 00 AND vMM = 00  \n" + 
				"					  THEN  \n" + 
				"					    SELECT (um.STARTDATETIME) + interval '5min'  \n" + 
				"						INTO vrVerifHH FROM ILHAS.USERLOG um WHERE id = rec_film.id;  \n" + 
				"					  \n" + 
				"				  \n" + 
				"					    RAISE NOTICE '3';  \n" + 
				"					  \n" + 
				"					    UPDATE ILHAS . USERLOG SET FINISHDATETIME = vrVerifHH WHERE id = rec_film.id;  \n" + 
				"				  \n" + 
				"					    insert into report.tb_log_ilhas_overtime  \n" + 
				"					    values (nextval('report.seq_tb_log_ilhas_obertime'),  \n" + 
				"									 rec_film.id, \n" + 
				"						    rec_film.userid,  \n" + 
				"						    vrVerifStart,  \n" + 
				"						    vrVerifEnd,  \n" + 
				"						    vrVerifHH,  \n" + 
				"						    vVerifTempo);  \n" + 
				"					-------------------------------------------------------------  \n" + 
				"				  \n" + 
				"				  \n" + 
				"					  WHEN vHH > 00 AND vMM < 05  \n" + 
				"					  THEN  \n" + 
				"					    SELECT (um.STARTDATETIME) + interval '5min'  \n" + 
				"						INTO vrVerifHH FROM ILHAS.USERLOG um WHERE id = rec_film.id;  \n" + 
				"				  \n" + 
				"					    RAISE NOTICE '4';  \n" + 
				"				  \n" + 
				"					    UPDATE ILHAS . USERLOG SET FINISHDATETIME = vrVerifHH WHERE id = rec_film.id;  \n" + 
				"				  \n" + 
				"				  \n" + 
				"					    insert into report.tb_log_ilhas_overtime  \n" + 
				"					    values (nextval('report.seq_tb_log_ilhas_obertime'),  \n" + 
				"									 rec_film.id, \n" + 
				"						    rec_film.userid,  \n" + 
				"						    vrVerifStart,  \n" + 
				"						    vrVerifEnd,  \n" + 
				"						    vrVerifHH,  \n" + 
				"						    vVerifTempo);  \n" + 
				"					-------------------------------------------------------------  \n" + 
				"					  \n" + 
				"				  \n" + 
				"					ELSE  \n" + 
				"				  \n" + 
				"					  RAISE NOTICE 'ID % Hora Inicial % Hora Final %', rec_film.id, rec_film.STARTDATETIME, rec_film.FINISHDATETIME;  \n" + 
				"				  \n" + 
				"					END CASE;  \n" + 
				"				  \n" + 
				"				  \n" + 
				"				      END IF;  \n" + 
				"				  \n" + 
				"				    ELSE  \n" + 
				"				  \n" + 
				"				      CASE  \n" + 
				"					  \n" + 
				"				  \n" + 
				"					WHEN vDD > 00  \n" + 
				"					THEN  \n" + 
				"					  RAISE NOTICE 'X1';  \n" + 
				"					  SELECT (um.STARTDATETIME) + interval '5min'  \n" + 
				"					      INTO vrVerifHH FROM ILHAS.USERLOG um WHERE id = rec_film.id;  \n" + 
				"				  \n" + 
				"					  RAISE NOTICE '=X2';  \n" + 
				"					  RAISE NOTICE 'DATA CORRETA%', vrVerifHH;  \n" + 
				"					 UPDATE ILHAS . USERLOG SET FINISHDATETIME = vrVerifHH WHERE id = rec_film.id;  \n" + 
				"				  \n" + 
				"					  insert into report.tb_log_ilhas_overtime  \n" + 
				"					  values (nextval('report.seq_tb_log_ilhas_obertime'),  \n" + 
				"									 rec_film.id, \n" + 
				"						  rec_film.userid,  \n" + 
				"						  vrVerifStart,  \n" + 
				"						  vrVerifEnd,  \n" + 
				"						  vrVerifHH,  \n" + 
				"						  vVerifTempo);  \n" + 
				"				      -------------------------------------------------------------  \n" + 
				"					  \n" + 
				"				  \n" + 
				"				      ELSE  \n" + 
				"				  \n" + 
				"					RAISE NOTICE 'ID % Hora Inicial % Hora Final %', rec_film.id, rec_film.STARTDATETIME, rec_film.FINISHDATETIME;  \n" + 
				"					  \n" + 
				"				      END CASE;  \n" + 
				"				  \n" + 
				"				  \n" + 
				"				    END IF;  \n" + 
				"				  \n" + 
				"				  \n" + 
				"				  END LOOP;  \n" + 
				"				  \n" + 
				"				  \n" + 
				"				END$$;";
		
		
		
		
		
		// EXECUTA O SCRIPT CONFORME ACIMA
		stmtUp2.executeUpdate(sql);
		stmtUp2.close();
		
		end = System.currentTimeMillis();
		
		//vRuntime = runTime(end , start);
		vRuntime = calculaTempo(System.currentTimeMillis() - start);
		
		
		// AQUI PEGO A DATA ATUAL DO SISTEMA
		 LocalDate dataManipulacao2 = LocalDate.now();
		 LocalTime time2 = LocalTime.now();
			 
		 vDtFim = String.valueOf(time2);
		 String vProc = "SLEDGEHAMMER";
		 vDados = vProc+"|"+vDtIni+"|"+vDtFim;
		
		// wr.writeReport(1, vDados,vRuntime);
		// wr.writeReport(2, vDados,vQtde);
		
		System.out.println("Término da marreta...!");
	}
		
		
	}catch(Exception e) {
		e.printStackTrace();
		//precisa colocar o erro do script
		
	}
		return vQtde;
	
	}

	

}
