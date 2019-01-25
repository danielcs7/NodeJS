package sendmail;

import java.sql.SQLException;
import java.text.ParseException;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class EnviaMail {
	
	public void sendMail() {
		
		System.out.println("Preparando para envio de e-mail....");
		HtmlEmail email = new HtmlEmail();
		email.setSSLOnConnect(true);
		email.setHostName( "smtp.gmail.com" );
		email.setSslSmtpPort( "465" );
		email.setAuthenticator( new DefaultAuthenticator( "logrelatoriosiab@gmail.com" ,  "I@b2018#" ) );
		
		try {
		    email.setFrom( "logrelatoriosiab@gmail.com" , "LOG Relatorio Ilhas do AlfaeBeto");
		    email.setDebug(true); 
		    email.setSubject( "Log da execução de relatório ilhas do alfaebeto" );
		 
		    EmailAttachment anexo = new EmailAttachment();
		    anexo.setPath("/tmp/LogRelatorioIlhasdoAlfaeBeto.txt");
		    anexo.setDisposition(EmailAttachment.ATTACHMENT);
		    anexo.setName("LogRelatorioIlhasdoAlfaeBeto.txt");
		 
		        email.attach(anexo);
		      //       
		    email.setHtmlMsg( "Segue anexo o log" );
		    email.addTo("diego@idados.org.br");
		    email.addCc("daniel@idados.org.br");
		    email.addBcc("joas@alfaebeto.org.br");
		    email.addBcc("ludimila@alfaebeto.org.br");
		    email.addBcc("renato@alfaebeto.org.br");
		    email.addBcc("maicon@idados.org.br");
		    email.send();
		} catch (EmailException e) {
		    e.printStackTrace();
		} 
	}
}