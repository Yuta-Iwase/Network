package Tools;


import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class SendMail {

	private String myAddress;
	private String password;

	private boolean login = false;

	private static final String ENCODE = "ISO-2022-JP";

	public SendMail(String myAddress, String password){
		this.myAddress = myAddress;
		this.password = password;
		login = true;
	}

	//ここからメール送付に必要なSMTP,SSL認証などの設定
    public void send(String destinationAddress, String subject, String message) {
    	if(login) {
    		final Properties props = new Properties();

            // SMTPサーバーの設定。ここではgooglemailのsmtpサーバーを設定。
            props.setProperty("mail.smtp.host", "smtp.gmail.com");

            // SSL用にポート番号を変更。
            props.setProperty("mail.smtp.port", "465");

            // タイムアウト設定
            props.setProperty("mail.smtp.connectiontimeout", "60000");
            props.setProperty("mail.smtp.timeout", "60000");

            // 認証
            props.setProperty("mail.smtp.auth", "true");

            // SSLを使用するとこはこの設定が必要。
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.socketFactory.port", "465");

           //propsに設定した情報を使用して、sessionの作成
            final Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myAddress, password);
                }
            });


            // ここからメッセージ内容の設定。上記で作成したsessionを引数に渡す。
            final MimeMessage mimeMessage = new MimeMessage(session);

            try {
                final Address addrFrom = new InternetAddress(myAddress, myAddress, ENCODE);
                mimeMessage.setFrom(addrFrom);

                final Address addrTo = new InternetAddress(destinationAddress, destinationAddress, ENCODE);
                mimeMessage.addRecipient(Message.RecipientType.TO, addrTo);

                // メールのSubject
                mimeMessage.setSubject(subject, ENCODE);

                // メール本文。
                String replacedMessage = message.replaceAll("<br>", "\r\n");
                mimeMessage.setText(replacedMessage, ENCODE);


                // その他の付加情報。
                mimeMessage.addHeader("X-Mailer", "blancoMail 0.1");
                mimeMessage.setSentDate(new Date());
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            // メール送信。
            try {
            	Transport.send(mimeMessage);
            } catch (AuthenticationFailedException e) {
            	// 認証失敗
//            	e.printStackTrace();
            	System.out.println(e);
            } catch (MessagingException e) {
            	// smtpサーバへの接続失敗
            	e.printStackTrace();
            }
    	}

    }

    public void sendMyself(String subject, String message) {
    	send(myAddress, subject, message);
    }

}
