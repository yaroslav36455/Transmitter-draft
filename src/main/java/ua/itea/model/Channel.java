package ua.itea.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Channel implements Runnable, AutoCloseable {
	private Socket socket;
	private Reader reader;
	private Writer writer;
	private boolean server;

	public Channel(Socket socket, LocalFileBase fileBase) {
		this.socket = socket;
		this.reader = new Reader(fileBase.getReadableBase());
		this.writer = new Writer(fileBase.getWritableBase(), new LoadLimit(50000));
		
		this.server = true;
		
		new Thread(this).start();
	}

	@Override
	public void close() throws IOException {
		socket.close();
	}

	@Override
	public synchronized void run() {
		
		System.out.println("begin");

		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		
		try {
			System.out.println(server);
			if (server) {
				ois = new ObjectInputStream(socket.getInputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());
			} else {
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
			}
			System.out.println(server);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			if (!server) {
				Message message = new Message();
				
				message.setDataRequest(writer.createRequest());
				oos.writeObject(message);
				
				DataFileRequest dfr = message.getDataRequest().iterator().next();
				System.out.println(dfr.getFileId().get());
				System.out.println(dfr.getDataBlockInfo().getOffset());
				System.out.println(dfr.getDataBlockInfo().getSize());
			}
			
//			try {
//				System.out.println("wait");
//				wait();
//			} catch (InterruptedException e1) {
//				e1.printStackTrace();
//			}
			
			while (true) {
				System.out.println("*");
				Message message = (Message) ois.readObject();
				
				DataRequest dataRequest = message.getDataRequest(); // Дать Reader на обработку
				DataAnswer dataAnswer = message.getDataAnswer(); // Дать Writer на обработку
				
				DataAnswer answer = reader.process(dataRequest);
				DataRequest request = null;

				if (dataAnswer != null) {
					writer.process(dataAnswer);	
				}
				
				request = writer.createRequest();
				
				// send message
//				message = new Message();
				message.setDataAnswer(answer);
				message.setDataRequest(request);
				oos.writeObject(message);
				
				/* Поток пробуждается после:
				 * 	  Получении сообщеия (автоматически)
				 * 	  Добавлении файлов в локальную базу файлов 
				 * 	  При завершении записи блока данных на диск */
				
				/* После записи блока данных на диск записывающий поток 
				 * сразу сообщает об этом потоку, что поставляет эти данные,
				 * а сам засыпает. 
				 * Поток, что */
				
//				switch (message.getMessageType()) {
//				case REGISTER_REQUEST:
//					/* TODO add file to remotes (for downloading) */
//					break;
//					
//				case REGISTER_ACCEPT:
//					/* TODO add file to remotes (for uploading) */
//					break;
//
//				case DATA_REQUEST:
//					/* TODO add to sender queue this request */
//					DataRequest request = (DataRequest) message.getData();
//					
//					/* sleep for the sender to comlete the job */
//					sender.setDataRequest(request);
//					
//					break;
//					
//				case STOP:
//					/* TODO just mark as stopped file only remote */
//					break;
//
//				case PAUSE:
//					/* TODO just mark as paused file only remote */
//					break;
//
//				case SUCCESS:
//					/* TODO just mark as success file only remote */
//					break;
//					
//				case ERROR:
//					/* TODO try to determine not bad blocks checking the hash code ??? */
//					break;
//				}
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
