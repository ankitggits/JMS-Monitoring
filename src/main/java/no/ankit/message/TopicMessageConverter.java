package no.ankit.message;

import no.ankit.dto.MessageEntity;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.util.Date;

import static no.ankit.util.MessageUtility.FORMATTER;

@Component
public class TopicMessageConverter implements MessageConverter{

	@Override
	public MessageEntity fromMessage(Message message) throws JMSException, MessageConversionException {
		MessageEntity genericMessage = (MessageEntity) ((ObjectMessage) message).getObject();
		genericMessage.setTimeStamp(FORMATTER.format(new Date(message.getJMSTimestamp())));
		genericMessage.setJMSCorrelationID(message.getJMSCorrelationID());
		genericMessage.setSourceAppId(message.getStringProperty("sourceAppId"));
		genericMessage.setReplyTo(message.getStringProperty("replyTo"));
		return genericMessage;
	}
	
	@Override
	public Message toMessage(Object obj, Session session) throws JMSException, MessageConversionException {
		return null;
	}
}