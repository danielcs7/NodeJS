package connectionfactory;

import java.sql.Connection;
import java.sql.SQLException;

public class Conexao {
public Connection connection;
	
	public Conexao(){
		
		
		connection = new ConnectionFactoryProd().createConnection(); //PRODUCAO
		//connection = new ConnectionFactoryDev().createConnection(); //DEV
		//connection = new ConnectionFactoryHM().createConnection(); //HMG
        
	}

	public void close() throws SQLException {
		// TODO Auto-generated method stub
		connection.close();
	}
	
}
	
