package ua.itea.model;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = -6481794848588146124L;
	
	//	private int hashCode;
	private StateChangedRequest stateChangedRequest;
	private StateChangedAnswer stateChangedAnswer;
	private DataRequest dataRequest;
	private DataAnswer dataAnswer;

	public StateChangedRequest getStateChangedRequest() {
		return stateChangedRequest;
	}

	public void setStateChangedRequest(StateChangedRequest stateChangedRequest) {
		this.stateChangedRequest = stateChangedRequest;
	}

	public StateChangedAnswer getStateChangedAnswer() {
		return stateChangedAnswer;
	}

	public void setStateChangedAnswer(StateChangedAnswer stateChangedAnswer) {
		this.stateChangedAnswer = stateChangedAnswer;
	}

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

//	public boolean isDefinitelyCorrupted() {
//		return this.hashCode != hashCode();
//	}
	
//	public void recomputeHashCode() {
//		
//	}
}
