package h1.util;

import java.util.Properties;

import h1.config.Constant;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

// Gui mail OTP qua SMTP Gmail (cau hinh o h1.config.Constant)
public class MailUtil {

	private MailUtil() {
	}

	private static Session buildSession() {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", Constant.MAIL_HOST);
		props.put("mail.smtp.port", String.valueOf(Constant.MAIL_PORT));

		return Session.getInstance(props, new jakarta.mail.Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Constant.MAIL_USERNAME, Constant.MAIL_PASSWORD);
			}
		});
	}

	// purposeText: vd "kich hoat tai khoan" hoac "dat lai mat khau"
	public static void sendOtpMail(String toEmail, String otpCode, String purposeText) throws MessagingException {
		Session session = buildSession();

		MimeMessage message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(Constant.MAIL_USERNAME, Constant.MAIL_FROM_NAME));
		} catch (Exception e) {
			// InternetAddress(String,String) khai bao UnsupportedEncodingException,
			// thuc te khong xay ra voi charset mac dinh nen chi log lai
			e.printStackTrace();
		}
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
		message.setSubject("Ma OTP " + purposeText);

		String html = "<div style='font-family:Arial,sans-serif'>"
				+ "<p>Xin chao,</p>"
				+ "<p>Ma OTP de <b>" + purposeText + "</b> cua ban la:</p>"
				+ "<p style='font-size:28px;font-weight:bold;letter-spacing:4px'>" + otpCode + "</p>"
				+ "<p>Ma co hieu luc trong " + Constant.OTP_EXPIRE_MINUTES + " phut. "
				+ "Neu ban khong yeu cau, vui long bo qua email nay.</p>"
				+ "</div>";
		message.setContent(html, "text/html; charset=UTF-8");

		Transport.send(message);
	}
}
