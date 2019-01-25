package sendmail;

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
		    email.setFrom( "logrelatoriosiab@gmail.com" , "LOG Exec Tabela Materialize");
		    email.setDebug(true); 
		    email.setSubject( "Log da execução da tabela Materialize" );
		 
		    EmailAttachment anexo = new EmailAttachment();
		    anexo.setPath("/tmp/LogExecMaterializeILhas.txt");
		    anexo.setDisposition(EmailAttachment.ATTACHMENT);
		    anexo.setName("LogExecMaterializeILhas.txt");
		 
		        email.attach(anexo);
		             
		    email.setHtmlMsg( "Segue anexo o log" );
		    email.addTo("daniel@idados.org.br");
		    email.send();
		} catch (EmailException e) {
		    e.printStackTrace();
		} 
	}
}