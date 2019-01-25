import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class test {

	private static long vStart;
	private static long vEnd;
	
	
	public static String vTemp(long vIni,long vEnd) {
		
		long tempoTotal = vEnd - vIni;
	      

	      long vSegundos = tempoTotal / 1000L % 60L;
	      long vMinutos = tempoTotal / 60000L % 60L;
	      long vHoras = tempoTotal / 3600000L % 24L;
	      long vDias = tempoTotal / 86400000L;
	      
	      String vTempoExec = String.format("%03d:%02d:%02d", new Object[] { Long.valueOf(vHoras), Long.valueOf(vMinutos), Long.valueOf(vSegundos) });
	      
	      return vTempoExec;
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		 vStart = System.currentTimeMillis();
		
		LocalDate dataManipulacao = LocalDate.now();

		System.out.println("Mais 1 dias:" + dataManipulacao.plusDays(1));
		System.out.println("Menos 1 dia: " + dataManipulacao.minusDays(1));
		System.out.println("Menos 7 dia: " + dataManipulacao.minusDays(7));
		System.out.println("Menos 30 dia: " + dataManipulacao.minusDays(30));
		System.out.println("Data Original:" + dataManipulacao);
		System.out.println("Menos 8 dia: " + dataManipulacao.minusDays(6));
		System.out.println("Menos 31 dia: " + dataManipulacao.minusDays(29));

		System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
		
		LocalDate localDateNovo = LocalDate.of(2018,12,06);  

		System.out.println("NOVO <-> Mais 1 dias:" + localDateNovo.plusDays(1));
		System.out.println("NOVO <-> Menos 1 dia: " + localDateNovo.minusDays(1));
		System.out.println("NOVO <-> Menos 7 dia: " + localDateNovo.minusDays(7));
		System.out.println("NOVO <-> Menos 30 dia: " + localDateNovo.minusDays(30));
		System.out.println("NOVO <-> Data Original:" + localDateNovo);
		System.out.println("NOVO <-> Menos 8 dia: " + localDateNovo.minusDays(6));
		System.out.println("NOVO <-> Menos 31 dia: " + localDateNovo.minusDays(29));
		
		System.out.println("◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆");
		
		
	     vEnd = System.currentTimeMillis();
	     
	     String vRet = vTemp(vStart,vEnd);
	     
	     System.out.println("TEMPO EXECUCAO <=> "+vRet);
		
		/*
		LocalTime time = LocalTime.now();
		System.out.println("Hora atual: " + time);

		String timeOutro2 = String.valueOf(time.plusHours(1).plusMinutes(1).plusSeconds(1));

		System.out.println("#1 " + timeOutro2);

		LocalTime time2 = LocalTime.now();

		String timeOutro = String.valueOf(time2.minusHours(-1).minusMinutes(-1));

		System.out.println("#2 " + timeOutro);

		LocalDate dataManipulacao2 = LocalDate.now();

		LocalTime l = LocalTime.now();
		LocalTime s = l.minusHours(1).minusMinutes(1);
		System.out.println(s);
		*/

	}

}
