package ua.itea.model.message;

import java.io.Serializable;
import java.util.List;

import ua.itea.model.DataFileRequest;
import ua.itea.model.Channel;

public class DataRequestMessage extends DataMessage implements Serializable {
	private static final long serialVersionUID = 4700656965292957296L;
	private List<DataFileRequest> requests;
	
	public DataRequestMessage() {
		/* empty */
	}
	
	public DataRequestMessage(List<DataFileRequest> requests) {
		this.requests = requests;
	}

	public List<DataFileRequest> getList() {
		return requests;
	}

	public void setList(List<DataFileRequest> requests) {
		this.requests = requests;
	}

	public boolean isEmpty() {
		return requests == null || requests.isEmpty();
	}
	
	public int size() {
		return isEmpty() ? 0 : requests.size();
	}

	@Override
	public void execute(Channel loader) {
		DataAnswerMessage dataAnswer = new DataAnswerMessage();
		
		dataAnswer.setList(loader.getUploader().load(requests));
		getMessenger().send(dataAnswer);
	}
}
