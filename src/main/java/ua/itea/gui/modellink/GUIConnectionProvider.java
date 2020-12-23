package ua.itea.gui.modellink;

import java.io.IOException;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import ua.itea.gui.GUIConnectionInfo;
import ua.itea.gui.GUIConnectionInfoController;
import ua.itea.gui.GUIIncomingConnectionDialog;
import ua.itea.gui.GUIOutgoingConnectionDialog;
import ua.itea.gui.factory.GUIIncomingConnectionDialogFactory;
import ua.itea.model.ConnectionClient;
import ua.itea.model.ConnectionProvider;
import ua.itea.model.ConnectionServer;
import ua.itea.model.Mark;

public class GUIConnectionProvider implements ConnectionProvider {
	private GUIConnectionInfo gci;

	public GUIConnectionProvider(GUIConnectionInfo gci) {
		this.gci = gci;
	}

	@Override
	public void startIncoming(ConnectionServer c) {
		Platform.runLater(() -> {
			try {
				GUIConnectionInfoController controller = gci.getController();
				controller.getName().setText(c.readName());
				controller.getAddress().setText(c.getSocket().getInetAddress().getHostAddress());
				controller.getPort().setText(String.valueOf(c.getSocket().getPort()));

				GUIIncomingConnectionDialogFactory gicdf = new GUIIncomingConnectionDialogFactory(gci);
				GUIIncomingConnectionDialog dialog = gicdf.create();

				if (dialog.showAndWait().isPresent()) {
					c.accept();
				} else {
					c.reject();
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					c.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void startOutgoing(ConnectionClient c) {
		Platform.runLater(() -> {
			GUIOutgoingConnectionDialog gocd = new GUIOutgoingConnectionDialog(gci);
			gocd.show();

			new Thread(() -> {
				try {
					c.start();
					c.writeName(gci.getController().getName().getText());

					Mark mark = c.readMark();

					Platform.runLater(() -> {
						gocd.setHeaderText("Answer: " + mark);
						gocd.getDialogPane().getButtonTypes().setAll(ButtonType.OK);
						
						if (mark == Mark.ACCEPT) {
							((ProgressIndicator) gocd.getGraphic()).setProgress(1);	
						} else {
							gocd.setGraphic(null);
						}
					});

				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				} finally {
					try {
						c.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		});
	}
}
