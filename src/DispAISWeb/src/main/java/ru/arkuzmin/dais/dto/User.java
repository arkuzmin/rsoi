package ru.arkuzmin.dais.dto;

import java.io.Serializable;

public class User implements Serializable {
	
	private static final long serialVersionUID = 5920203360826675285L;
	
	private String name;
	private String lname;
	private String mname;
	private String login;
	private String pwd;
	private String guid;
	
	public static class Builder {
		private String login;
		private String pwd;
		private String name;
		
		private String lname = null;
		private String mname = null;
		private String guid = null;
		
		public Builder(String login, String pwd, String name) {
			this.login = login;
			this.name = name;
			this.pwd = pwd;
		}
		
		public Builder lname(String lname) {
			this.lname = lname;
			return this;
		}
		
		public Builder mname(String mname) {
			this.mname = mname;
			return this;
		}
		
		public User build() {
			return new User(this);
		}
		
	}
	
	public User() {}
	
	private User(Builder builder) {
		this.name = builder.name;
		this.login = builder.login;
		this.pwd = builder.pwd;
		this.guid = builder.guid;
		this.mname = builder.mname;
		this.lname = builder.lname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getMname() {
		return mname;
	}

	public void setMname(String mname) {
		this.mname = mname;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}
}
