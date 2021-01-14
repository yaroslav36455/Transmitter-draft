package ua.itea.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import ua.itea.model.factory.LocalFileReadableFactory;
import ua.itea.model.factory.RemoteFileRegisteredFactory;
import ua.itea.model.message.AutoBlockingQueue;
import ua.itea.model.message.DataAnswer;
import ua.itea.model.message.DataRequest;
import ua.itea.model.message.Message;
import ua.itea.model.message.NewFilesMessage;
import ua.itea.model.message.RemoveFilesMessage;
import ua.itea.model.message.factory.NewFilesMessageFactory;
import ua.itea.model.message.factory.RemoveFilesMessageFactory;

public class Uploader {
	private Registered registered;
	private FileBase<LocalFileReadable> files;
	private LoadLimit loadLimit;
	private LocalFileReadableFactory localFileReadableFactory;

	public Uploader() {
		this.loadLimit = new LoadLimit(50);
	}
	
	public LocalFileReadableFactory getLocalFileReadableFactory() {
		return localFileReadableFactory;
	}

	public void setLocalFileReadableFactory(LocalFileReadableFactory localFileReadableFactory) {
		this.localFileReadableFactory = localFileReadableFactory;
	}

	public Registered getRegistered() {
		return registered;
	}

	public void setRegistered(Registered registered) {
		this.registered = registered;
	}

	public FileBase<LocalFileReadable> getFiles() {
		return files;
	}

	public void setFiles(FileBase<LocalFileReadable> files) {
		this.files = files;
	}

	public LoadLimit getLoadLimit() {
		return loadLimit;
	}

	public void setLoadLimit(LoadLimit loadLimit) {
		this.loadLimit = loadLimit;
	}

	public DataAnswer load(DataRequest dataRequest) {
		DataAnswer dataAnswer = new DataAnswer();

		try {
			for (DataFileRequest dataFileRequest : dataRequest) {
				LocalFileReadable file = files.getFile(dataFileRequest.getFileId());
				
				if(file != null) {
					if(!file.isOpened()) {
						file.open();
					}
					
					DataBlock dataBlock = file.read(dataFileRequest.getDataBlockInfo());
					dataAnswer.add(new DataFileAnswer(dataFileRequest.getFileId(), dataBlock));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataAnswer;
	}
	
	public List<LocalFileReadable> addAll(List<File> list) {
		List<LocalFileReadable> newFiles = new ArrayList<>();
		
		for (File file : list) {
			newFiles.add(localFileReadableFactory.create(file));
		}

		return files.addAll(newFiles);
	}
	
	public List<LocalFileReadable> removeAll(List<FileId> list) {
		return files.removeAll(list);
	}

	public class Registered {
		private Set<FileId> registeredRemote;

		private AutoBlockingQueue<Message> outgoing;
		private RemoteFileRegisteredFactory remoteFileRegisteredFactory;
		private NewFilesMessageFactory newFilesMessageFactory;
		private RemoveFilesMessageFactory removeFilesMessageFactory;

		public Registered() {
			this.registeredRemote = new TreeSet<>((fid1, fid2)->fid1.get() - fid2.get());
		}

		public AutoBlockingQueue<Message> getOutgoing() {
			return outgoing;
		}

		public void setOutgoing(AutoBlockingQueue<Message> outgoing) {
			this.outgoing = outgoing;
		}

		public RemoteFileRegisteredFactory getRemoteFileRegisteredFactory() {
			return remoteFileRegisteredFactory;
		}

		public void setRemoteFileRegisteredFactory(RemoteFileRegisteredFactory remoteFileRegisteredFactory) {
			this.remoteFileRegisteredFactory = remoteFileRegisteredFactory;
		}

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

//		public boolean addRightNow(LocalFileRegistered newFile) {
//			if (addCached(newFile) && registeredRemote.add(newFile.getFileId())) {
//				NewFilesMessage newFilesMessage = newFilesMessageFactory.create();
//				newFilesMessage.add(remoteFileRegisteredFactory.create(newFile));
//				outgoing.add(newFilesMessage);
//				return true;
//			}
//
//			return false;
//		}
//
//		public boolean removeRightNow(FileId fileId) throws IOException {
//			if (removeCached(fileId) && registeredRemote.remove(fileId)) {
//				RemoveFilesMessage removeFilesMessage = removeFilesMessageFactory.create();
//				removeFilesMessage.add(fileId);
//				outgoing.add(removeFilesMessage);
//				return true;
//			}
//
//			return false;
//		}
//		
//		private boolean removeAndCloseReadable(FileId fileId) throws IOException {
//			LocalFileReadable file = files.removeAndGet(fileId);
//			
//			if (file != null) {
//				file.close();
//			}
//			
//			return file == null;
//		}

		public void updateRemote() {
			updateRemoteRemoveFiles();
			updateRemoteNewFiles();
		}
		
		private void updateRemoteRemoveFiles() {
			RemoveFilesMessage removeFilesMessage = removeFilesMessageFactory.create();
			
			Iterator<FileId> iterRemote = registeredRemote.iterator();
			while(iterRemote.hasNext()) {
				FileId fileId = iterRemote.next();
				
				if(!files.contains(fileId)) {
					iterRemote.remove();
					removeFilesMessage.add(fileId);
				}
			}
			if(!removeFilesMessage.isEmpty()) {
				outgoing.add(removeFilesMessage);
			}
		}
		
		private void updateRemoteNewFiles() {
			NewFilesMessage newFilesMessage = newFilesMessageFactory.create();

			for (LocalFileReadable localFileReadable : files) {
				if (registeredRemote.add(localFileReadable.getFileId())) {
					newFilesMessage.add(remoteFileRegisteredFactory.create(localFileReadable));
				}
			}
			if(!newFilesMessage.isEmpty()) {
				outgoing.add(newFilesMessage);
			}
		}
		
		public void resetRemote() {
			registeredRemote.clear();
		}

		public boolean isEmpty() {
			return registeredRemote.isEmpty();
		}
	}
}
