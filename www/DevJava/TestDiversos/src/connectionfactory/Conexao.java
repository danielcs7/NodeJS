package connectionfactory;

import java.sql.Connection;
import java.sql.SQLException;

public class Conexao {
public Connection connection;
	
	public Conexao(){
		connection = new ConnectionFactoryDev().createConnection();
        
	}

	public void close() throws SQLException {
		// TODO Auto-generated method stub
		connection.close();
	}
	
}
	
