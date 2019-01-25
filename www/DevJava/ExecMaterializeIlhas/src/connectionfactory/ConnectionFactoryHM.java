package connectionfactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactoryHM {
	//private static String url = "postgres.alfaebeto.org.br";
    private static String url = "rdspsg.iab.udi";
	
	public static Connection createConnection() { 
	        //String stringDeConexao = "jdbc:postgresql://dbiab.cw3m4w3pejlb.sa-east-1.rds.amazonaws.com:5432/abs-hm"; //url do driver jdbc
	        
	        String stringDeConexao = "jdbc:postgresql://"+url+":5432/abs-hm"; //url do driver jdbc
	        
	        String usuario = "abs"; //seu usuario do banco de dados
	        String senha = "@b$2016"; //sua senha do banco de dados
	        Connection conexao = null;
	         
	        try{
	            conexao = DriverManager.getConnection(stringDeConexao, usuario, senha); //cria uma conexão
	        } catch(Exception e ){
	            e.printStackTrace();
	        }
	        return conexao; //retorna a conexão
	    }
	}