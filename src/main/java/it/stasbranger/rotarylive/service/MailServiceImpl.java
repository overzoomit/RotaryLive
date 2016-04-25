package it.stasbranger.rotarylive.service;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.CharEncoding;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import it.stasbranger.rotarylive.domain.User;

@Service("MailService")
public class MailServiceImpl implements MailService {

	@Autowired private transient JavaMailSender mailSender;
	@Autowired private transient VelocityEngine velocityEngine;

	@Value("${spring.mail.username}")
	private String from;
	 
	private String internetaddress = "RotaryLive Team";
	
	private void sendEmail(String to, String subject, String content, String from, boolean isMultipart, boolean isHtml) {
	    MimeMessage mimeMessage = mailSender.createMimeMessage();
	    try {
	        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
	        message.setTo(to);
	        message.setFrom(new InternetAddress(from, internetaddress));
	        message.setSubject(subject);
	        message.setText(content, isHtml);
	        mailSender.send(mimeMessage);
	    } catch (Exception e) {
	       e.printStackTrace();
	    }
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void sendForgotPassword(User user, String code) {
		    Map model = new HashMap();
		    model.put("user", user);
		    model.put("code", code);
		    String content = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/forgot_password.vm", "UTF-8", model);
		    String subject = "Change password for RotaryLive";
		    sendEmail(user.getMember().getEmail(), subject, content, from ,false, true);
	}
}
