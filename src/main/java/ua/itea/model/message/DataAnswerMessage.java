package ua.itea.model.message;

import java.io.Serializable;
import java.util.List;

import ua.itea.model.DataFileAnswer;
import ua.itea.model.Downloader;
import ua.itea.model.Channel;

public class DataAnswerMessage extends DataMessage implements Serializable {
	private static final long serialVersionUID = -6359875826557787382L;
	private List<DataFileAnswer> answers;
	
	public DataAnswerMessage() {
		/* empty */
	}
	
	public DataAnswerMessage(List<DataFileAnswer> answers) {
		this.answers = answers;
	}
	
	public List<DataFileAnswer> getList() {
		return answers;
	}

	public void setList(List<DataFileAnswer> answers) {
		this.answers = answers;
	}

	public boolean isEmpty() {
		return answers == null || answers.isEmpty();
	}
	
	public int size() {
		return isEmpty() ? 0 : answers.size();
	}

	@Override
	public void execute(Channel loader) {
		Downloader downloader = loader.getDownloader();
		DataRequestMessage dataRequest = new DataRequestMessage();
		
		/* TODO: parallelize A */
		dataRequest.setList(downloader.createRequest(answers));
		getMessenger().send(dataRequest);
		
		/* TODO: parallelize B */
		downloader.save(answers);
	}
}
