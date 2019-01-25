package connectionfactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactoryOrigem {

	public static Connection createConnection() { 
        String stringDeConexao = "jdbc:postgresql://52.44.52.63:5432/abs"; //url do driver jdbc
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