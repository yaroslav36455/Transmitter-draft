package ua.itea.model.message;

import java.io.Serializable;
import java.util.List;

import ua.itea.model.FileId;
import ua.itea.model.Channel;

public class RemoveFilesMessage extends LoaderMessage implements Serializable {
	private static final long serialVersionUID = -1205787736557141799L;
	private List<FileId> idList;
	
	public RemoveFilesMessage() {
		/* empty */
	}
	
	public RemoveFilesMessage(List<FileId> idList) {
		this.idList = idList;
	}

	public List<FileId> getList() {
		return idList;
	}

	public void setList(List<FileId> idList) {
		this.idList = idList;
	}

	public boolean isEmpty() {
		return idList == null || idList.isEmpty();
	}
	
	public int size() {
		return isEmpty() ? 0 : idList.size();
	}
	
	@Override
	public void execute(Channel loader) {
		loader.getDownloader().removeAll(idList);
	}

}
