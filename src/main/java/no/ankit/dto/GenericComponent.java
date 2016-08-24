package no.ankit.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GenericComponent implements Serializable {


	private static final long serialVersionUID = 1L;
	private long sentTime;
	private long recTime;
	private long ackSentTime;
	private long ackRecTime;

}
