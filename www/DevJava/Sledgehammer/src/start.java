import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import connectionfactory.ConnectionFactoryHM;

public class start {
	
	
	private static int vId;
	private static int vCount;
	private static int vDD;
	private static int vHH;
	private static int vMM;
	private static int vSS;
	private static String vRetornDt;
	private static String vVerifTempo;

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		
		
		Connection conProd = ConnectionFactoryHM.createConnection();
		Statement stmt = conProd.createStatement();
		
		vCount = 0;
		
		String sqlDAlunos = "SELECT LOG.id, LOG.STARTDATETIME, LOG.FINISHDATETIME\n" + 
				"	      FROM ilhas.USERLOG LOG  \n" + 
				"		     INNER JOIN web.user U ON U.ID = LOG.USERID  \n" + 
				"		     INNER JOIN web.studentuser su ON su.userid = u.ID  \n" + 
				"		     INNER JOIN web.school sc ON sc.ID = su.schoolid  \n" + 
				"	      WHERE LOG.USERLOGTYPEID >= 5000  \n" + 
				"		AND (FINISHDATETIME - STARTDATETIME) >= '01:00:00'";

		// PREPARA PARA TRAZER OS VALORES DO SCRIPT
		PreparedStatement stmtTblx = conProd.prepareStatement(sqlDAlunos);
		// TRAZ OS RESULTADOS DO SELECT
		ResultSet rsTblx = stmtTblx.executeQuery();
		// TRAZ OS VALORES E IMPORTA NAS VARIAVEIS
		
		
		while (rsTblx.next()) {
			
			vId			= rsTblx.getInt("id");
			
			
			String sqlHoras = "\n" + 
					"	SELECT SUBSTR(to_char((um.FINISHDATETIME - um.STARTDATETIME), 'YYYY-MM-DD HH24:MI:SS'), 9, 2) AS DD,  \n" + 
					"	   SUBSTR(to_char((um.FINISHDATETIME - um.STARTDATETIME), 'HH24:MI:SS'), 1, 2)            as HH,  \n" + 
					"	   SUBSTR(to_char((um.FINISHDATETIME - um.STARTDATETIME), 'HH24:MI:SS'), 4, 2)            as MM,  \n" + 
					"	   SUBSTR(to_char((um.FINISHDATETIME - um.STARTDATETIME), 'HH24:MI:SS'), 7, 10)           as SS  ,\n" + 
					"	   um.STARTDATETIME,\n" + 
					"	   um.FINISHDATETIME,\n" + 
					"	   um.STARTDATETIME + interval '5min' as vrVerifHH \n" + 
					"	--INTO vDD, vHH, vMM, vSS  \n" + 
					"    FROM ILHAS.USERLOG um  \n" + 
					"    WHERE id = "+vId+"";  
			
					// PREPARA PARA TRAZER OS VALORES DO SCRIPT
					PreparedStatement stmTHor = conProd.prepareStatement(sqlHoras);
					// TRAZ OS RESULTADOS DO SELECT
					ResultSet rsHor = stmTHor.executeQuery();
					// TRAZ OS VALORES E IMPORTA NAS VARIAVEIS
					
					while (rsHor.next()) {
						vDD			= rsHor.getInt("DD");
						vHH			= rsHor.getInt("HH");
						vMM			= rsHor.getInt("MM");
						vSS			= rsHor.getInt("SS");
						vRetornDt	= rsHor.getString("vrVerifHH");
						
						
					}
					
					
					vVerifTempo = vDD +" DIAS < = > "+ vHH +"h : "+ vMM + "m : "+ vSS + "s "; 
					//System.out.println("Linha <=> Qtde :: "+vCount+" -- "+vVerifTempo);
					
					
					if(vHH > 00 & vMM >= 05) {
					
						Statement stmtUp2 = conProd.createStatement();
						String sqlUp1 = "UPDATE ILHAS.USERLOG SET FINISHDATETIME = '"+vRetornDt+"' WHERE id = "+vId+"";  
						
						System.out.println("vHH > 00 & vMM >= 05");
						// EXECUTA O SCRIPT CONFORME ACIMA
						//conProd.commit();
						stmtUp2.executeUpdate(sqlUp1);
						stmtUp2.close();

											  
						
					}else if(vHH == 00 & vMM > 05) {
						Statement stmtUp2 = conProd.createStatement();
						String sqlUp1 = "UPDATE ILHAS.USERLOG SET FINISHDATETIME = '"+vRetornDt+"' WHERE id = "+vId+"";  
						
						System.out.println("vHH = 00 & vMM > 05");
						// EXECUTA O SCRIPT CONFORME ACIMA
						conProd.commit();
						stmtUp2.executeUpdate(sqlUp1);
						stmtUp2.close();
						  
					}else if(vHH > 00 & vMM == 00) {
						Statement stmtUp2 = conProd.createStatement();
						String sqlUp1 = "UPDATE ILHAS.USERLOG SET FINISHDATETIME = '"+vRetornDt+"' WHERE id = "+vId+"";  
						
						System.out.println("vHH > 00 & vMM = 00");
						// EXECUTA O SCRIPT CONFORME ACIMA
						//conProd.commit();
						stmtUp2.executeUpdate(sqlUp1);
						stmtUp2.close();
						
					}else if(vHH > 00 & vMM < 05) {
						Statement stmtUp2 = conProd.createStatement();
						String sqlUp1 = "UPDATE ILHAS.USERLOG SET FINISHDATETIME = '"+vRetornDt+"' WHERE id = "+vId+"";  
						
						System.out.println("vHH > 00 & vMM < 05");
						// EXECUTA O SCRIPT CONFORME ACIMA
						//conProd.commit();
						stmtUp2.executeUpdate(sqlUp1);
						stmtUp2.close();
					}else {
						
					}
					
					if(vDD > 00) {
						
						System.out.println("correcao quando for mais de 1 dia rodado...");
						
						Statement stmtUp2 = conProd.createStatement();
						String sqlUp1 = "UPDATE ILHAS.USERLOG SET FINISHDATETIME = '"+vRetornDt+"' WHERE id = "+vId+"";  
						
						System.out.println("vDD > 00");
						
						// EXECUTA O SCRIPT CONFORME ACIMA
						//conProd.commit();
						stmtUp2.executeUpdate(sqlUp1);
						stmtUp2.close();
					}
				
					
					vCount = vCount+1;
				}
					
			
			
		
		
		
		System.out.println("QUANTIDADE <=> "+vCount);
		stmt.close();
		conProd.close();
		
	}

}
