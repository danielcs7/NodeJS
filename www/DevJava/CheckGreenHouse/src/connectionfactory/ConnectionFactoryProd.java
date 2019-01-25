package connectionfactory;

import java.sql.Connection;

public class ConnectionFactoryProd {
	public ConnectionFactoryProd() {}
	  
	  //PARA CONECTAR INTERNO NO SERVIDOR
  	  //rdspsg.iab.udi
	  //PARA CONECTAR EXTERNO
	  //postgres.alfaebeto.org.br
	
	//private static String url = "postgres.alfaebeto.org.br";
    private static String url = "rdspsg.iab.udi";
	  
	
	  public static Connection createConnection() { 
		String stringDeConexao = "jdbc:postgresql://"+url+":5432/mvhf_arduino";
	    String usuario = "abs";
	    String senha = "@b$2016";
	    Connection conexao = null;
	    try
	    {
	      conexao = java.sql.DriverManager.getConnection(stringDeConexao, usuario, senha);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return conexao;
	  }
	}
