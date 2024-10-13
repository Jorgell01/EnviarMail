package dad.mail;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class RootController {

    // Propiedades para los campos de entrada
    private final StringProperty fromEmailProperty = new SimpleStringProperty();
    private final StringProperty toEmailProperty = new SimpleStringProperty();
    private final StringProperty subjectProperty = new SimpleStringProperty();
    private final StringProperty messageProperty = new SimpleStringProperty();
    private final StringProperty smtpServerProperty = new SimpleStringProperty();
    private final StringProperty portProperty = new SimpleStringProperty();
    private final StringProperty passwordProperty = new SimpleStringProperty();
    private final BooleanProperty sslProperty = new SimpleBooleanProperty();

    @FXML
    private TextArea areaText;

    @FXML
    private Button clearButton;

    @FXML
    private Button closeButton;

    @FXML
    private TextField fromEmail;

    @FXML
    private TextField portText;

    @FXML
    private TextField psswText;

    @FXML
    private Button sendButton;

    @FXML
    private TextField smtpText;

    @FXML
    private CheckBox sslCheckbox;

    @FXML
    private TextField subjectText;

    @FXML
    private TextField toEmail;

    @FXML
    public void initialize() {
        fromEmail.textProperty().bindBidirectional(fromEmailProperty);
        toEmail.textProperty().bindBidirectional(toEmailProperty);
        subjectText.textProperty().bindBidirectional(subjectProperty);
        areaText.textProperty().bindBidirectional(messageProperty);
        smtpText.textProperty().bindBidirectional(smtpServerProperty);
        portText.textProperty().bindBidirectional(portProperty);
        psswText.textProperty().bindBidirectional(passwordProperty);
        sslCheckbox.selectedProperty().bindBidirectional(sslProperty);

        sendButton.setOnAction(this::onActionSend);
        clearButton.setOnAction(this::onActionDelete);
        closeButton.setOnAction(this::onActionClose);
    }

    @FXML
    void onActionSend(ActionEvent event) {
        try {
            Email email = new SimpleEmail();
            email.setHostName(smtpServerProperty.get());
            email.setSmtpPort(Integer.parseInt(portProperty.get()));
            email.setAuthentication(fromEmailProperty.get(), passwordProperty.get());
            email.setSSLOnConnect(sslProperty.get());
            email.setFrom(fromEmailProperty.get());
            email.setSubject(subjectProperty.get());
            email.setMsg(messageProperty.get());
            email.addTo(toEmailProperty.get());
            email.send();

            showAlertSuccess(toEmailProperty.get());

        } catch (EmailException | NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se pudo enviar el email.\n" + e.getMessage());
        }
    }

    @FXML
    void onActionDelete(ActionEvent event) {
        fromEmailProperty.set("");
        toEmailProperty.set("");
        subjectProperty.set("");
        messageProperty.set("");
        smtpServerProperty.set("");
        portProperty.set("");
        passwordProperty.set("");
        sslProperty.set(false);
    }

    @FXML
    void onActionClose(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showAlertSuccess(String recipientEmail) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mensaje enviado");
        alert.setHeaderText("Mensaje enviado con éxito a '" + recipientEmail + "'.");
        alert.setContentText(null);
        alert.showAndWait();
    }

}
