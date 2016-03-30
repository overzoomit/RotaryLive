package it.stasbranger.rotarylive.domain;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Document
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@NotEmpty
	private String name;

	@Override
	public String getAuthority() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
