package ua.itea.gui.modellink;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import ua.itea.gui.GUIConnectionInfo;
import ua.itea.gui.GUIConnectionInfoController;
import ua.itea.gui.GUIIncomingConnectionDialog;
import ua.itea.gui.factory.GUIIncomingConnectionDialogFactory;
import ua.itea.model.ConnectionProvider;
import ua.itea.model.Mark;

public class GUIConnectionProvider implements ConnectionProvider {
	private GUIConnectionInfo gci;
	
	public GUIConnectionProvider(GUIConnectionInfo gci) {
		this.gci = gci;
	}

	@Override
	public void startIncoming(Socket socket) {
		Platform.runLater(() -> {
			try {
				GUIIncomingConnectionDialogFactory gicdf = new GUIIncomingConnectionDialogFactory(gci);
				GUIIncomingConnectionDialog dialog = gicdf.create();
				
				ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				
				GUIConnectionInfoController controller = gci.getController();
				controller.getName().setText(ois.readUTF());
				controller.getAddress().setText(socket.getInetAddress().getHostAddress());
				controller.getPort().setText(String.valueOf(socket.getPort()));
				
				oos.writeObject(dialog.showAndWait().isPresent() ? Mark.ACCEPT : Mark.REJECT);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void startOutgoing(Socket socket) {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.NONE, null, ButtonType.CANCEL);

			alert.getDialogPane().setContent(gci.getNode());
			alert.setHeaderText("Connection...");
			alert.setTitle("Outgoing connection");
			alert.setGraphic(new ProgressIndicator());
			
			alert.show();

			new Thread(()->{
				try {				
					ObjectOutputStream oosX = new ObjectOutputStream(socket.getOutputStream());
					oosX.writeObject(Mark.START);
					
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
					oos.writeUTF(gci.getController().getName().getText());
					oos.flush();
					
					Mark mark = (Mark) ois.readObject();
					Platform.runLater(()->{
						alert.setHeaderText("Answer: " + mark);
						alert.getButtonTypes().setAll(ButtonType.OK);
					});
					
				} catch (IOException | ClassNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}).start();
		});
	}
}
