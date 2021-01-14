package ua.itea.model.message;

import java.util.List;

import ua.itea.model.FileBase;
import ua.itea.model.FileId;
import ua.itea.model.LocalFileRegistered;
import ua.itea.model.message.factory.DataMessageFactory;
import ua.itea.model.message.factory.NewFilesMessageFactory;
import ua.itea.model.message.factory.RemoveFilesMessageFactory;

public class MessageFactory {
	private NewFilesMessageFactory newFilesMessageFactory;
	private RemoveFilesMessageFactory removeFilesMessageFactory;
	private DataMessageFactory dataMessageFactory;
	
	public NewFilesMessageFactory getNewFilesMessageFactory() {
		return newFilesMessageFactory;
	}

	public void setNewFilesMessageFactory(NewFilesMessageFactory newFilesMessageFactory) {
		this.newFilesMessageFactory = newFilesMessageFactory;
	}

	public RemoveFilesMessageFactory getRemoveFilesMessageFactory() {
		return removeFilesMessageFactory;
	}

	public void setRemoveFilesMessageFactory(RemoveFilesMessageFactory removeFilesMessageFactory) {
		this.removeFilesMessageFactory = removeFilesMessageFactory;
	}

	public DataMessageFactory getDataMessageFactory() {
		return dataMessageFactory;
	}

	public void setDataMessageFactory(DataMessageFactory dataMessageFactory) {
		this.dataMessageFactory = dataMessageFactory;
	}
}
