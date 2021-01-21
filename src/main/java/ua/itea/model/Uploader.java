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
import ua.itea.model.message.NewFilesMessage;
import ua.itea.model.message.RemoveFilesMessage;
import ua.itea.model.message.factory.NewFilesMessageFactory;
import ua.itea.model.message.factory.RemoveFilesMessageFactory;

public class Uploader {
	private Registered registered;
	private FileBase<LocalFileReadable> files;
	private LoadLimit uploadLimit;
	private LoadLimit remoteDownloadLimit;
	private LocalFileReadableFactory localFileReadableFactory;

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
		return uploadLimit;
	}

	public void setLoadLimit(LoadLimit loadLimit) {
		this.uploadLimit = loadLimit;
	}

	public List<DataFileAnswer> load(List<DataFileRequest> dataRequests) {
		List<DataFileAnswer> dataAnswers = new ArrayList<>();

		try {
			for (DataFileRequest dataFileRequest : dataRequests) {
				LocalFileReadable file = files.getFile(dataFileRequest.getFileId());

				if (file != null) {
					if (!file.isOpened()) {
						file.open();
					}

					DataBlock dataBlock = file.read(dataFileRequest.getDataBlockInfo());
					dataAnswers.add(new DataFileAnswer(dataFileRequest.getFileId(), dataBlock));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return dataAnswers.isEmpty() ? null : dataAnswers;
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

	public void start(Messenger messenger) {
		updateRemote(messenger);
	}

	public void stop() {
		registered.resetRemote();
		closeAll();
	}

	public void updateRemote(Messenger messenger) {
		registered.updateRemote(messenger, this);
	}

	private void closeAll() {
		for (LocalFileReadable localFileReadable : files) {
			try {
				if (localFileReadable.isOpened()) {
					localFileReadable.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static class Registered {
		private Set<FileId> registeredRemote;

		private RemoteFileRegisteredFactory remoteFileRegisteredFactory;
		private NewFilesMessageFactory newFilesMessageFactory;
		private RemoveFilesMessageFactory removeFilesMessageFactory;

		public Registered() {
			this.registeredRemote = new TreeSet<>((fid1, fid2) -> fid1.get() - fid2.get());
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

		public void updateRemote(Messenger messenger, Uploader uploader) {
			if (messenger.isConnectionEstablished()) {
				updateRemoteRemoveFiles(messenger, uploader);
				updateRemoteNewFiles(messenger, uploader);
			}
		}

		private void updateRemoteRemoveFiles(Messenger messenger, Uploader uploader) {
			List<FileId> idList = new ArrayList<>();
			FileBase<LocalFileReadable> files = uploader.getFiles();
			Iterator<FileId> iterRemote = registeredRemote.iterator();

			while (iterRemote.hasNext()) {
				FileId fileId = iterRemote.next();

				if (!files.contains(fileId)) {
					iterRemote.remove();
					idList.add(fileId);
				}
			}

			if (!idList.isEmpty()) {
				RemoveFilesMessage removeFilesMessage = removeFilesMessageFactory.create();

				removeFilesMessage.setList(idList);
				messenger.send(removeFilesMessage);
			}
		}

		private void updateRemoteNewFiles(Messenger messenger, Uploader uploader) {
			FileBase<LocalFileReadable> files = uploader.getFiles();
			List<RemoteFileRegistered> registeredFiles = new ArrayList<>();

			for (LocalFileReadable localFileReadable : files) {
				if (registeredRemote.add(localFileReadable.getFileId())) {
					registeredFiles.add(remoteFileRegisteredFactory.create(localFileReadable));
				}
			}

			if (!registeredFiles.isEmpty()) {
				NewFilesMessage newFilesMessage = newFilesMessageFactory.create();

				newFilesMessage.setList(registeredFiles);
				messenger.send(newFilesMessage);
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
