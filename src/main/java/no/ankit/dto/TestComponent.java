package no.ankit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class TestComponent extends GenericComponent {

	private static final long serialVersionUID = 1L;

	String val;
	
	public TestComponent(String val){
		this.val=val;
	}
	
}
