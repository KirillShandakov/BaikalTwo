package penza.shandakov.baikal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import penza.shandakov.baikal.POJO.ForClient;
import penza.shandakov.baikal.server.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class PersonalAccountController {


    @FXML
    private ComboBox<String> boxCity;

    @FXML
    private Button buttonBack;

    @FXML
    private Button buttonNext;

    @FXML
    private ImageView errorImage;

    @FXML
    private Label errorInput;

    @FXML
    private ImageView imageApplication;

    @FXML
    private ImageView imageTracking;

    @FXML
    private DatePicker inputDateBirth;

    @FXML
    private TextField inputInfo;

    @FXML
    private PasswordField inputPasswordOne;

    @FXML
    private PasswordField inputPasswordTwo;

    @FXML
    private TextField nameInput;

    @FXML
    private Pane paneFirst;

    @FXML
    private TextField patronymicInput;

    @FXML
    private Label errorInputTwo;


    @FXML
    private Button saveButton;

    @FXML
    private TextField surnameInput;

    @FXML
    private ImageView errorImageTwo;

    @FXML
    private Label text;

    @FXML
    private RadioButton changePassword;

    @FXML
    private Label textTwo;


    private String selectCity = "";

    private boolean checkInfo = false;

    private int checkBack = 0;
    private String nameClient = "";

    private String name = "";
    private String surname = "";
    private String birthday = "";


    DatabaseHandler dbHandler = new DatabaseHandler();
    ForClient forClient = new ForClient();

    @FXML
    void initialize() {
        try {
            initCity();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        errorImage.setVisible(false);
        errorImageTwo.setVisible(false);
        errorInput.setVisible(false);
        errorInputTwo.setVisible(false);
        inputDateBirth.setVisible(false);
        ResultSet resultAuto;
        LocalDateTime dateTime = LocalDateTime.now();
        forClient = new ForClient();
        forClient.setId(String.valueOf(AuthorizationController.id));
        resultAuto = dbHandler.checkInfoClient(forClient);


        try {
            if (resultAuto.next()) {
                if(!resultAuto.getString(1).equals("")){
                    nameClient = resultAuto.getString(2).substring(resultAuto.getString(2).indexOf(' ') + 1);
                    checkInfo = true;
                }
                else {
                    checkInfo = false;
                }
            } else {
                checkInfo = false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (checkInfo) {
            forClient = new ForClient();
            forClient.setId(String.valueOf(AuthorizationController.id));
            resultAuto = dbHandler.getInfoClient(forClient);
            try {
                if (resultAuto.next()) {
                    surnameInput.setText(resultAuto.getString(1).substring(0, resultAuto.getString(1).indexOf(' ')));
                    nameInput.setText(resultAuto.getString(1).substring(resultAuto.getString(1).indexOf(' ') + 1, resultAuto.getString(1).lastIndexOf(' ')));
                    patronymicInput.setText(resultAuto.getString(1).substring(resultAuto.getString(1).lastIndexOf(' ') + 1));
                    boxCity.setValue(resultAuto.getString(2));
                    selectCity = resultAuto.getString(2);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            if (dateTime.getHour() <= 11 && dateTime.getHour() >= 6)
                textTwo.setText("???????????? ????????, " + nameClient);
            else if (dateTime.getHour() > 11 && dateTime.getHour() <= 17) {
                textTwo.setText("???????????? ????????, " + nameClient);
            } else if (dateTime.getHour() > 17) {
                textTwo.setText("???????????? ??????????, " + nameClient);
            }
            textTwo.setVisible(true);
            paneFirst.setVisible(false);

            saveButton.setOnAction(actionEvent -> {
                if (!changePassword.isSelected()) {

                    if (surnameInput.getText().equals("")) {
                        errorImageTwo.setVisible(true);
                        errorImageTwo.setLayoutX(50);
                        errorImageTwo.setLayoutY(255);
                    } else if (nameInput.getText().equals("")) {
                        errorImageTwo.setVisible(true);
                        errorImageTwo.setLayoutX(50);
                        errorImageTwo.setLayoutY(313);
                    } else if (patronymicInput.getText().equals("")) {
                        errorImageTwo.setVisible(true);
                        errorImageTwo.setLayoutX(50);
                        errorImageTwo.setLayoutY(375);
                    } else if (selectCity.equals("")) {
                        errorImageTwo.setVisible(true);
                        errorImageTwo.setLayoutX(400);
                        errorImageTwo.setLayoutY(255);
                    } else {
                        forClient = new ForClient();
                        forClient.setSurname(surnameInput.getText());
                        forClient.setName(nameInput.getText());
                        forClient.setPatronymic(patronymicInput.getText());
                        forClient.setCity(selectCity);
                        forClient.setId(String.valueOf(AuthorizationController.id));
                        try {
                            dbHandler.updateClientFull(forClient);
                        } catch (SQLException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        errorInputTwo.setVisible(true);
                        errorInputTwo.setText("??????????????");
                    }
                } else {
                    errorInputTwo.setVisible(false);
                    if (inputPasswordOne.getText().equals("")) {
                        errorImageTwo.setVisible(true);
                        errorImageTwo.setLayoutX(400);
                        errorImageTwo.setLayoutY(313);
                    } else if (inputPasswordTwo.getText().equals("")) {
                        errorImageTwo.setVisible(true);
                        errorImageTwo.setLayoutX(400);
                        errorImageTwo.setLayoutY(375);
                    } else {
                        if (inputPasswordOne.getText().equals(inputPasswordTwo.getText())) {
                            forClient = new ForClient();
                            forClient.setPassword(AuthorizationController.getMd5(inputPasswordOne.getText()));
                            forClient.setId(String.valueOf(AuthorizationController.id));
                            try {
                                dbHandler.changePasswordTwo(forClient);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            errorInputTwo.setText("??????????????");
                        } else {
                            errorInputTwo.setMinWidth(180);
                            errorInputTwo.setText("???????????? ???? ??????????????????");
                            errorInputTwo.setVisible(true);
                        }

                    }
                }
            });
        } else {
            changePassword.setVisible(false);
            saveButton.setVisible(false);
            surnameInput.setVisible(false);
            nameInput.setVisible(false);
            patronymicInput.setVisible(false);
            boxCity.setVisible(false);
            inputPasswordOne.setVisible(false);
            inputPasswordTwo.setVisible(false);
            textTwo.setVisible(false);
            paneFirst.setVisible(true);
            buttonBack.setOnAction(actionEvent -> {
                switch (checkBack) {
                    case (0):
                        AuthorizationController.openWindow("/penza/shandakov/baikal/main.fxml", buttonBack, "?????????????? ????????????????");
                        break;
                    case (1):
                        checkBack = 0;
                        text.setText("???????? ???????");
                        inputInfo.setPromptText("??????");
                        inputInfo.setText(name);
                        errorInput.setVisible(false);
                        break;
                    case (2):
                        checkBack = 1;
                        text.setText("???????? ???????????????");
                        inputInfo.setPromptText("??????????????");
                        inputInfo.setText(surname);
                        inputInfo.setVisible(true);
                        inputDateBirth.setVisible(false);
                        errorInput.setVisible(false);
                        break;
                }
            });

            buttonNext.setOnAction(actionEvent -> {
                switch (checkBack) {
                    case (0):
                        if (!inputInfo.getText().equals("")) {
                            checkBack = 1;
                            name = inputInfo.getText();
                            text.setText("???????? ???????????????");
                            inputInfo.setPromptText("??????????????");
                            inputInfo.setText("");
                        } else if (checkBack == 0) {
                            errorImage.setVisible(true);
                        }
                        break;
                    case (1):
                        if (!inputInfo.getText().equals("")) {
                            checkBack = 2;
                            text.setText("?????????????? ???????? ????????????????");
                            surname = inputInfo.getText();
                            inputInfo.setVisible(false);
                            inputDateBirth.setVisible(true);
                        } else if (checkBack == 1) {
                            errorImage.setVisible(true);
                        }
                        break;
                    case (2):
                        if (inputDateBirth.getValue() != null) {
                            birthday = String.valueOf(inputDateBirth.getValue());
                            if (Math.toIntExact((ChronoUnit.YEARS.between(LocalDate.parse(birthday), dateTime))) >= 18) {
                                forClient = new ForClient();
                                forClient.setName(name);
                                forClient.setSurname(surname);
                                forClient.setBirthday(birthday);
                                forClient.setId(String.valueOf(AuthorizationController.id));
                                try {
                                    dbHandler.updateClient(forClient);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                } catch (ClassNotFoundException e) {
                                    throw new RuntimeException(e);
                                }
                                paneFirst.setVisible(false);
                                checkInfo = true;
                                textTwo.setVisible(true);
                                if (dateTime.getHour() <= 11 && dateTime.getHour() >= 6)
                                    textTwo.setText("???????????? ????????, " + nameClient);
                                else if (dateTime.getHour() > 11 && dateTime.getHour() <= 18) {
                                    textTwo.setText("???????????? ????????, " + nameClient);
                                } else if (dateTime.getHour() > 18) {
                                    textTwo.setText("???????????? ??????????, " + nameClient);
                                }

                                surnameInput.setVisible(true);
                                nameInput.setVisible(true);
                                patronymicInput.setVisible(true);
                                boxCity.setVisible(true);
                                inputPasswordOne.setVisible(true);
                                inputPasswordTwo.setVisible(true);
                                changePassword.setVisible(true);
                                saveButton.setVisible(true);

                            } else {
                                errorInput.setText("?????? ???????????? 18");
                                errorInput.setVisible(true);
                            }
                        } else if (checkBack == 2) {
                            errorImage.setVisible(true);
                        }
                        break;
                }
            });
        }

        boxCity.setOnAction(this::getCity);

        // ???????????? ?????? ???????????????? ???????????????????? ??????????????
        imageApplication.setOnMouseClicked(mouseEvent -> {
            AuthorizationController.openWindow("/penza/shandakov/baikal/cargo.fxml", buttonBack, "???????????????????? ????????????????");
        });
        // ???????????? ?????? ???????????????? ????????????????????????
        imageTracking.setOnMouseClicked(mouseEvent -> {
            AuthorizationController.openWindow("/penza/shandakov/baikal/tracking.fxml", buttonBack, "????????????????????????");
        });
    }

    // ?????????? ?????????????? ?? ???????????????????? ???????????????????? ?????????????????? ???????????????? ???? ????????????
    private void getCity(ActionEvent actionEvent) {
        selectCity = boxCity.getValue();
    }

    public void mouseClick(MouseEvent mouseEvent) {
        errorImage.setVisible(false);
        errorImageTwo.setVisible(false);
    }

    private void initCity() throws SQLException {
        ResultSet rs;
        rs = dbHandler.getCity();
        while (true) {
            assert rs != null;
            if (!rs.next()) break;
            boxCity.getItems().addAll(rs.getString(1));
        }
    }


}
