package model;

import java.sql.Timestamp;

public class DrawHistory {
	
	private int id;
	private Integer userId;
	private int cardPk;
	private String question;
	private boolean reversed;
	private Timestamp createdAt;
	
	public DrawHistory() {
		
	}

	public DrawHistory(Integer userId, int cardPk, String question, boolean reversed) {
		super();
		this.userId = userId;
		this.cardPk = cardPk;
		this.question = question;
		this.reversed = reversed;
	}

	public DrawHistory(int id, Integer userId, int cardPk, String question, boolean reversed, Timestamp createdAt) {
		super();
		this.id = id;
		this.userId = userId;
		this.cardPk = cardPk;
		this.question = question;
		this.reversed = reversed;
		this.createdAt = createdAt;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public int getCardPk() {
		return cardPk;
	}

	public void setCardPk(int cardPk) {
		this.cardPk = cardPk;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public boolean isReversed() {
		return reversed;
	}

	public void setReversed(boolean reversed) {
		this.reversed = reversed;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
	

}
