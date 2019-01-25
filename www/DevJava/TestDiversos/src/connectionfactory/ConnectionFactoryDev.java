package connectionfactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactoryDev {
	public static Connection createConnection() { 
        String stringDeConexao = "jdbc:postgresql://localhost:5432/abs"; //url do driver jdbc
        String usuario = "postgres"; //seu usuario do banco de dados
        String senha = "idados"; //sua senha do banco de dados
        Connection conexao = null;
         
        try{
            conexao = DriverManager.getConnection(stringDeConexao, usuario, senha); //cria uma conexão
        } catch(Exception e ){
            e.printStackTrace();
        }
        return conexao; //retorna a conexão
    }
}