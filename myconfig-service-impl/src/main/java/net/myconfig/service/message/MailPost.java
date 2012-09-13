package net.myconfig.service.message;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import net.myconfig.core.MyConfigProfiles;
import net.myconfig.service.api.ConfigurationKey;
import net.myconfig.service.api.ConfigurationService;
import net.myconfig.service.api.message.Message;
import net.myconfig.service.api.message.MessageChannel;
import net.myconfig.service.model.Ack;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@Profile(MyConfigProfiles.PROD)
public class MailPost extends AbstractMessagePost {

	private final Logger logger = LoggerFactory.getLogger(MailPost.class);

	private final JavaMailSender mailSender;
	private final ConfigurationService configurationService;

	@Autowired
	public MailPost(JavaMailSender mailSender, ConfigurationService configurationService) {
		this.mailSender = mailSender;
		this.configurationService = configurationService;
	}

	@Override
	public boolean supports(MessageChannel channel) {
		return (channel == MessageChannel.EMAIL);
	}

	@Override
	public Ack post(final Message message, final String destination) {
		
		final String replyToAddress = configurationService.getParameter(ConfigurationKey.APP_REPLYTO_ADDRESS);

		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				mimeMessage.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(destination));
				mimeMessage.setFrom(new InternetAddress(replyToAddress));
				mimeMessage.setSubject(message.getTitle());
				mimeMessage.setText(message.getContent());
			}
		};
		try {
			this.mailSender.send(preparator);
			return Ack.OK;
		} catch (MailException ex) {
			logger.error("[mail] Cannot send mail: {}", ExceptionUtils.getRootCauseMessage(ex));
			return Ack.NOK;
		}
	}

}
