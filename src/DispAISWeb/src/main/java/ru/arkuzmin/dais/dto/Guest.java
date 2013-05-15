package ru.arkuzmin.dais.dto;

import java.io.Serializable;

public class Guest implements Serializable {

	private static final long serialVersionUID = 8062555817223795825L;

	private String guid;
	private String code;
	
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}
