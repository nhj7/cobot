package nhj.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {
	private static MimeMessage MIME_MESSAGE;
	private static String MAIL_SMTP_HOST = "smtp.daum.net";
	private static String MAIL_SMTP_PORT = "465";
	private static String MAIL_SMTP_USER = "admin@cobot.co.kr";
	private static String MAIL_SMTP_USER_NM = "스팀몰관리자";
	private static String MAIL_SMTP_ID = "nhj12311";
	private static String MAIL_SMTP_PSWD = "skgud77&&";
	
	
	static{
		Properties props = System.getProperties();
		props.setProperty("mail.smtp.host", MAIL_SMTP_HOST);
		props.setProperty("mail.smtp.port", MAIL_SMTP_PORT);
		props.setProperty("mail.smtp.user", MAIL_SMTP_USER);
		
		props.setProperty("mail.smtp.starttls.enable", "true");				
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.socketFactory.port", MAIL_SMTP_PORT);
		props.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
		    protected PasswordAuthentication getPasswordAuthentication() {
		      return new PasswordAuthentication(MAIL_SMTP_ID, MAIL_SMTP_PSWD);
		    }
		});
		MIME_MESSAGE = new MimeMessage(session);
	}
	
	
	
	
	
	
	public static void sendMail(String toMail, String subject, String content) throws Throwable{		
		MIME_MESSAGE.setFrom(new InternetAddress(MAIL_SMTP_USER, MAIL_SMTP_USER_NM));
		MIME_MESSAGE.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail, toMail));		    
		MIME_MESSAGE.setSubject(subject);
		MIME_MESSAGE.setContent(content, "text/html; charset=utf-8");
		Transport.send(MIME_MESSAGE);
	}
	
	public static void main(String[] args) throws Throwable {
		
		sendMail("nhj7@naver.com", "하하하하", "테스트 메일 발송합니다!!!");
		
		
		if(true)return;
		
		try {
		    // 발신자, 수신자, 참조자, 제목, 본문 내용 등을 설정한다
		    MIME_MESSAGE.setFrom(new InternetAddress(MAIL_SMTP_USER, MAIL_SMTP_USER_NM));
		    MIME_MESSAGE.addRecipient(Message.RecipientType.TO, new InternetAddress("nhj12311@gmail.com", "nhj12311@gmail.com"));		    
		    MIME_MESSAGE.setSubject("제목이 이러저러합니다");
		    MIME_MESSAGE.setContent("본문이 어쩌구저쩌구합니다", "text/html; charset=utf-8");
		    // 메일을 발신한다
		    System.out.println("111");
		    
		    long cur = System.currentTimeMillis();
		    
		    Transport.send(MIME_MESSAGE);
		    
		    System.out.println("send 1 : " + (System.currentTimeMillis() - cur ) + "ms" );
		    
		    MIME_MESSAGE.setSubject("제목이 이러저러합니다 22222222");
		    cur = System.currentTimeMillis();
		    
		    Transport.send(MIME_MESSAGE);
		    
		    System.out.println("222");
		    
		    System.out.println("send 2 : " + (System.currentTimeMillis() - cur ) + "ms" );
		    
		} catch (Exception e) {
		    // 적절히 처리
			e.printStackTrace();
		}
		
		
	}
}
