import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class test {

    private static long vStart;
	private static long vEnd;
    
    
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
    }
}