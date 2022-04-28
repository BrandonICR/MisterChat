package mx.com.brandonicr.chat.control;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.web.WebEngine;
import mx.com.brandonicr.chat.common.dto.Directory;
import mx.com.brandonicr.chat.common.dto.PrivateSessionInfo;
import mx.com.brandonicr.chat.common.dto.User;
import mx.com.brandonicr.chat.control.events.PrivateMessageEvent;

public class DirectoryHandler {

    private WebEngine webEngine;
    private ListView<Button> listViewContactos;
    private ObservableList<Button> listObservable;
    private Directory directory;
    private User localUser;

    public DirectoryHandler(WebEngine webEngine, ListView<Button> listViewContactos,
            ObservableList<Button> listObservable, Directory directory, User localUser) {
        this.webEngine = webEngine;
        this.listViewContactos = listViewContactos;
        this.listObservable = listObservable;
        this.directory = directory;
        this.localUser = localUser;
    }

    public void addContact(User user, String alias) {
        directory.addContact(user);
        Button usuarioButton = new Button(alias);
        System.out.println(String.format("Nuevo usuario %s", user.getUserName()));
        PrivateSessionInfo pSessionInfo = new PrivateSessionInfo(localUser, user, webEngine);
        usuarioButton.setOnAction(new PrivateMessageEvent(pSessionInfo));
        Platform.runLater(() -> {
            listObservable.addAll(usuarioButton);
            listViewContactos.getItems().clear();
            listViewContactos.getItems().addAll(listObservable);
        });
    }

    public boolean existsContact(User user) {
        return directory.getContact(user.getUserName(), user.getCreationDate()) != null;
    }

    public Directory getDirectory() {
        return this.directory;
    }

}
