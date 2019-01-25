package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GeraRelatorioCheckTxt {

	private static int vExc;
	private static int vCountOvertime = 0;
	
	public static void writeLog(String vDados) {

		Date data = new Date();
		String dt = null;
		SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		dt = formatador.format(data);

		
		
		
		File arquivo = new File("/tmp/scriptreportcaligraphia.sql");
	
		try {

			// caso seja um diretório, é possível listar seus arquivos e
			// diretórios
			
			if (!arquivo.exists()) {

				// cria um arquivo (vazio)
				arquivo.createNewFile();

			}

			
			File[] arquivos = arquivo.listFiles();

			// escreve no arquivo
			FileWriter fw = new FileWriter(arquivo, true);

			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(vDados);
			bw.newLine();
			
			
			bw.close();
			fw.close();

		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);

			

		}

	}


}
