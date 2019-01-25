## PROJETO RELATÓRIO ILHAS POR LOTE

> ---

O objetivo do programa é rodar de forma mais rápida todo o processo.

O sistema é dividido em 8 Processos:

- Processo [MATERIALIZEILHAS](#MATERIALIZEILHAS)

- Processo [SLEDGEHAMMER](#SLEDGEHAMMER)
- Processo [DETALHADO](#DETALHADO)
- Processo [PEDAGOGICO](#PEDAGOGICO)
- Processo [LETRAS](#LETRAS)
- Processo [FONEMA](#FONEMA)
- Processo [CALIGRAFIA](#CALIGRAFIA)
- [Entendimento do Código Java](#ENTENDIMENTO)
- Processo [EMAIL](#EMAIL)

> ---

## Processo [MATERIALIZEILHAS](MATERIALIZEILHAS)

> <strong>Processo [MATERIALIZEILHAS](MATERIALIZEILHAS)</strong>

Esse processo consiste em criar uma view materializada, para que a consulta seja mais rápida.  
Para que os 7 processos abaixos possam ter êxito é <strong>NECESSÁRIO</STRONG> que esse processo seja executado na madrugada.

O processo precisa sempre dar um <strong>REFRESH</strong> para que possa pegar a data atual. O código para criar as views estão no banco de dados, e para que possa dar refresh basta colocar no banco o seguinte script:

> Esse <strong>Processo de Materialize de views</strong> Foi substituído por um processo mais eficiente e rápido.
> Ver a documentação do processo de Materialize.

## [SLEDGEHAMMER](SLEDGEHAMMER)

- <strong>Processo [SLEDGEHAMMER](SLEDGEHAMMER)</strong>

  Esse processo consistem em corrigir as informações da tabela <strong>ILHAS.USERLOG</strong>. Alguns usuários estão passando o tempo jogado 1 hora, muitas vezes deixando o tablet em standby.

  <strong>Exemplo:</strong>

  ```javascript
    USERLOGID                :910324
    USERID                   :27635
    STARTDATETIME            :2018-11-12 17:12:47
    FINISHDATETIME-ANTERIOR  :2018-11-13 10:53:37
    FINISHDATETIME-CORRIGIDO :2018-11-12 17:17:47
    TEMPO ANTERIOR           :0 DIAS < = > 17h : 40m : 50s
    TEMPO CORRIGIDO          :00:05:00
  ```

  Como pode ver o <strong>STARTDATETIME</strong> está com a data <strong>2018-11-12 17:12:47 </strong> e o <strong>FINISHDATETIME-ANTERIOR</strong> esta com um dia á mais, ou seja o aluno , não jogaria dois dias direto.  
  Ao rodar esse processo, ele envia o id da tabela e o id do aluno para poder conferir as informaçõe se necessário.

## [DETALHADO](/cgi-bin/view/Main/DETALHADO)

- <strong>Processo [DETALHADO](/cgi-bin/view/Main/DETALHADO)</strong>

  Esse processo é o principal.  
  É o gerencial, onde contem todas as informações dos alunos, classes, turmas e escolas.

## [PEDAGOGICO](/cgi-bin/view/Main/PEDAGOGICO)

- <strong>Processo [PEDAGOGICO](/cgi-bin/view/Main/PEDAGOGICO)</strong>

  Esse processo pega os dados dos jogos pedagógicos.

## [LETRAS](/cgi-bin/view/Main/LETRAS)

- <strong>Processo [LETRAS](/cgi-bin/view/Main/LETRAS)</strong>

  Esse processo pega os dados dos jogos Letras.

## [FONEMA](/cgi-bin/view/Main/FONEMA)

- <strong>Processo [FONEMA](/cgi-bin/view/Main/FONEMA)</strong>

  Esse processo pega os dados dos jogos Fonema.

## [CALIGRAFIA](/cgi-bin/view/Main/CALIGRAFIA)

- <strong>Processo [CALIGRAFIA](/cgi-bin/view/Main/CALIGRAFIA)</strong>

  Esse processo pega os dados dos jogos Caligrafia

## [ENTENDIMENTO](/cgi-bin/view/Main/ENTENDIMENTO)

- <strong>Entendimento</strong>

  Aqui vou colocar um breve entendimento do código.  
  O projeto se encontra no [GIT](/cgi-bin/view/Main/GIT) versionado com o nome <strong>[ReportIlhasLote](/cgi-bin/view/Main/ReportIlhasLote).</strong>  
  O projeto consistem em 5 packages:

  - connectionfactory
  - controller
  - model
  - sendmail
  - view

  A package <strong>connectionfactory</strong> contêm as classes de conexão com o banco de dados, seja Homologação, Desenvolvimento ou Produção.  
  Para isso,basta mudar a conexão do banco desejado na classe <strong>Conexao</strong>.

  Aqui irei colocar os exemplos de classes de Conexão e Connectionfactory.

```javascript
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

```

Essa classe abaixo é que solicitamos a conexão com o banco

```javascript
package connectionfactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactoryProd {

	/*
	 * ## PARA CONECTAR INTERNO NO SERVIDOR
	 * rdspsg.iab.udi
	 * ## PARA CONECTAR EXTERNO
	 * postgres.alfaebeto.org.br
	*/

	//private static String url = "postgres.alfaebeto.org.br";
    private static String url = "rdspsg.iab.udi";


	public static Connection createConnection() {
        String stringDeConexao = "jdbc:postgresql://"+url+":5432/abs"; //url do driver jdbc
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

```

    Para que possamos chamar a conexão com o banco, basta colocar nas classes o seguinte código.

```javascript
  Conexao conProd = new Conexao();
```

A package <strong>[CONTROLLER](/cgi-bin/view/Main/CONTROLLER)</strong> fica todas as classes de regras e execução dos processos

- Roda o Processo sledgehammer | <strong>Classe Sledgehammer</strong>
- Roda o Processo detalhado | <strong>Classe GeraRelatDet</strong>
- Roda o Processo pedagógico | <strong>Classe RelatorioPedagogico</strong>
- Roda o Processo letras | <strong>Classe GeraRelatorioLetras</strong>
- Roda o Processo fonema | <strong>Classe GeraRelatFon</strong>
- Roda o Processo caligrafia | <strong>Classe GeraRelatorioCaligrafia</strong>
- Roda o Processo WriteTable | <strong>Classe WriteTable</strong>

  - Faz o insert na tabela <strong>report.tb_check_log_ilhas</strong>

- Roda o Processo WriteReport | <strong>Classe WriteReport</strong>
  - Gera o arquivo de logo <strong>LogRelatorioIlhasdoAlfaeBeto.txt</strong> esse arquivo é gravado na tabela <strong>"/tmp"</strong>
- Roda o Process WriteLog | <strong>Classe WriteLog</strong>
  - Esse processo consiste em gerar os arquivos <strong>".sql"</strong>

A package <strong>MODEL</strong> fica todas as classes de ENUM

Dentro dessa package existe duas classes:

- TabRelatorioExec

```javascript
package model;

public enum TabRelatorioExec {

	// PRODUCAO
	TAB1("report.tb_relatorio_gerencial_ilhas"),
	TAB2("report.tb_relatorio_pedagogico_ilhas"),
	TAB3("report.tb_relatorio_ilhas_ltr"),
	TAB4("report.tb_relatorio_ilhas_fonema"),
	TAB5("report.tb_relatorio_ilhas_caligrf"),
	TAB6("report.tb_check_log_ilhas");

	private final String tabExecucao;

	TabRelatorioExec(String tabExecucao) {
		this.tabExecucao = tabExecucao;
	}

	public String getTabExecucao() {
		return this.tabExecucao;
	}

}
```

- <strong>SeqTabelasRelatExec</strong>

```javascript
package model;

public enum SeqTabelasRelatExec {

	// PRODUCAO
	SEQ1("report.seq_tb_relat_ger_ilhas"),
	SEQ2("report.seq_tb_relat_ped_ilhas"),
	SEQ3("report.seq_tb_relat_ilhas_ltr"),
	SEQ4("report.seq_tb_relat_ilhas_fon"),
	SEQ5("report.seq_tb_relat_ilhas_caligrf"),
	SEQ6("report.seq_tb_check_log_ilhas");

	private final String seqTabela;

	SeqTabelasRelatExec(String seqTabela) {
		this.seqTabela = seqTabela;
	}

	public String getSeqTabela() {
		return this.seqTabela;
	}

}

```

Essas classes tem o nome de todas as tabelas e sequências do projeto, caso haja alteração do nome , basta apenas trocar o nome nessas classes, sem a necessidade de trocar em todas as classes ou scripts do projeto.

## [EMAIL](EMAIL)

A package <strong>[SENDMAIL](/cgi-bin/view/Main/SENDMAIL)</strong> fica a classe para envio de email.  
Foi criado um email para utilização de envio de relatórios:

usuário: [logrelatoriosiab@gmail.com](mailto:logrelatoriosiab@gmail.com)

senha: I@b2018#

Ela consegue pegar o arquivo em anexo e enviar para os e-mails já estabelecidos, que são:

- [diego@idados.org.br](mailto:diego@idados.org.br)
- [daniel@idados.org.br](mailto:daniel@idados.org.br)
- [joas@alfaebeto.org.br](mailto:joas@alfaebeto.org.br)
- [ludimila@alfaebeto.org.br](mailto:ludimila@alfaebeto.org.br)
- [renato@alfaebeto.org.br](mailto:renato@alfaebeto.org.br)
- [maicon@idados.org.br](mailto:maicon@idados.org.br)
