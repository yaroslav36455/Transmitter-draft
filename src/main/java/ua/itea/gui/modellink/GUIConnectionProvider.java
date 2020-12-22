package ua.itea.gui.modellink;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.application.Platform;
import ua.itea.gui.GUIIncomingConnectionDialog;
import ua.itea.gui.factory.GUIConnectionInfoFactory;
import ua.itea.gui.factory.GUIIncomingConnectionDialogFactory;
import ua.itea.model.ConnectionProvider;
import ua.itea.model.Mark;

public class GUIConnectionProvider implements ConnectionProvider {

	@Override
	public void startIncoming(Socket socket) {
		Platform.runLater(() -> {
			try {
				GUIConnectionInfoFactory gcif = new GUIConnectionInfoFactory();
				GUIIncomingConnectionDialogFactory gicdf = new GUIIncomingConnectionDialogFactory(gcif);
				GUIIncomingConnectionDialog dialog = gicdf.create();
				
				ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
				
				oos.writeObject(dialog.showAndWait().isPresent() ? Mark.ACCEPT
																 : Mark.REJECT);
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
		// TODO Auto-generated method stub
		
	}

}
