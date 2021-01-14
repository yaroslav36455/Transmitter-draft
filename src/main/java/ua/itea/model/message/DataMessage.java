package ua.itea.model.message;

import java.io.Serializable;

public class DataMessage implements LoaderMessage, Serializable {
	private static final long serialVersionUID = -2874259747188994501L;
	private DataRequest dataRequest;
	private DataAnswer dataAnswer;

	public DataRequest getDataRequest() {
		return dataRequest;
	}

	public void setDataRequest(DataRequest dataRequest) {
		this.dataRequest = dataRequest;
	}

	public DataAnswer getDataAnswer() {
		return dataAnswer;
	}

	public void setDataAnswer(DataAnswer dataAnswer) {
		this.dataAnswer = dataAnswer;
	}

	public boolean isEmpty() {
		return dataRequest == null && dataAnswer == null;
	}
}
