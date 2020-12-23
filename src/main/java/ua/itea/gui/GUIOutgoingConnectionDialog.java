package ua.itea.gui;

import java.io.IOException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressIndicator;
import ua.itea.model.Mark;

public class GUIOutgoingConnectionDialog extends Dialog<ButtonType> {
	
	public GUIOutgoingConnectionDialog(GUIConnectionInfo gci) {
		getDialogPane().setContent(gci.getNode());
		getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
		setTitle("Outgoing connection");
		setHeaderText("Connection...");
		setGraphic(new ProgressIndicator());

		setListeners();
	}

	private void setListeners() {
//		setOnShowing(e->{
//			new Thread(()->{
//				try {
//					c.start();
//					c.writeName(gci.getController().getName().getText());
//
//					Mark mark = c.readMark();
//					Platform.runLater(()->{
//						setHeaderText("Answer: " + mark);
//						getDialogPane().getButtonTypes().setAll(ButtonType.OK);
//					});
//					
//				} catch (IOException | ClassNotFoundException e) {
//					e.printStackTrace();
//				} finally {
//					try {
//						c.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				
//			}).start();
//		});
	}
}
