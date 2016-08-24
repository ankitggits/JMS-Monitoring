package no.ankit.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class MessageEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String replyTo;
	private String JMSCorrelationID;
	private String sourceAppId;
	private String opCode;
	private String timeStamp;
	private GenericComponent component;
	
	public MessageEntity(String opCode, GenericComponent component) {
		this.opCode = opCode;
		this.component = component;
	}

	public MessageEntity(GenericComponent component) {
		this.component = component;
	}


}
