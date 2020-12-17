package ua.itea;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import ua.itea.gui.ApplicationImpl;
import ua.itea.model.Channel;
import ua.itea.model.FileBase;
import ua.itea.model.FileId;
import ua.itea.model.LoadLimit;
import ua.itea.model.LocalFileReadable;
import ua.itea.model.LocalFileWritable;
import ua.itea.model.Priority;
import ua.itea.model.Reader;
import ua.itea.model.FileTotalSize;
import ua.itea.model.WritableFileInfo;
import ua.itea.model.Writer;

public class Main {

	public static void main(String[] args) throws IOException {
		ApplicationImpl.main(args);
		
//		String s = "server";
//		String c = "client";
//		boolean server = args[0].equals(s) ? true : false; 
//		Socket socket = null;
//		Reader reader = null;
//		Writer writer = null;
//		
//		// покуда FileId у LocalFileReadable сервера и LocalFileClient совпадают!
//		if (server) {
//			FileBase<LocalFileReadable> fileBase = new FileBase<>();
//			fileBase.add(new LocalFileReadable(new File("/home/vessel/Documents/eclipse-java-workspace/Transmitter/server/3746348.jpg")));
//			
//			ServerSocket serverSocket = new ServerSocket(32421, Integer.MAX_VALUE,
//									InetAddress.getByName("127.0.0.1"));
//			
//			socket = serverSocket.accept();
//			reader = new Reader(fileBase);
//			writer = new Writer(new FileBase<>(), new LoadLimit(50000));
//			
//			new Connection(server, socket, reader, writer);
////			serverSocket.close();
//		} else {
//			FileBase<LocalFileWritable> fileBase = new FileBase<>();
//			FileId fileId = new FileId();
//			FileTotalSize totalSize = new FileTotalSize(115827);
//			Priority priority = new Priority(1);
//			File file = new File("/home/vessel/Documents/eclipse-java-workspace/Transmitter/client/3746348.jpg");
//			WritableFileInfo fileInfo = new WritableFileInfo(fileId, file, totalSize, priority);
//			fileBase.add(new LocalFileWritable(fileInfo));
//			
//			socket = new Socket(InetAddress.getByName("127.0.0.1"), 32421);
//			reader = new Reader(new FileBase<>());
//			writer = new Writer(fileBase, new LoadLimit(50000));
//			
//			new Connection(server, socket, reader, writer);
//		}
	}
}
