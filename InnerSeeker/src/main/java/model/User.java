package model;

import java.sql.Timestamp;

public class User {

	private int id;
	private String username;
	private String password;
	private String hint;
	private String hintAnswer;
	private Role role;
	private Timestamp createdAt;

	public User() {
	}

	public User(String username, String password, String hint, String hintAnswer) {

		this.username = username;
		this.password = password;
		this.hint = hint;
		this.hintAnswer = hintAnswer;
	}

	public User(int id, String username, String password, String hint, String hintAnswer, Role role,
			Timestamp createdAt) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.hint = hint;
		this.hintAnswer = hintAnswer;
		this.role = role;
		this.createdAt = createdAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHint() {
		return hint;
	}

	public void setHint(String hint) {
		this.hint = hint;
	}

	public String getHintAnswer() {
		return hintAnswer;
	}

	public void setHintAnswer(String hintAnswer) {
		this.hintAnswer = hintAnswer;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
}