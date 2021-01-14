package ua.itea.model;

import java.io.Serializable;

import ua.itea.model.message.DataAnswer;

public class UploaderAnswer implements Serializable {
	private static final long serialVersionUID = -7820799044935650832L;
	private DataAnswer dataAnswer;
	
	public DataAnswer getDataAnswer() {
		return dataAnswer;
	}
	
	public void setDataAnswer(DataAnswer dataAnswer) {
		this.dataAnswer = dataAnswer;
	}
}
