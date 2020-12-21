package ua.itea.model;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = -6481794848588146124L;
	
	private Request request;
	private Answer answer;
	
	public boolean isEmpty() {
		return request == null && answer == null;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Answer getAnswer() {
		return answer;
	}

	public void setAnswer(Answer answer) {
		this.answer = answer;
	}
}
