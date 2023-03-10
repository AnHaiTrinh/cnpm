package com.se07.controller.controllers.controllershogiadinh;

import com.se07.controller.services.HoKhauService;
import com.se07.controller.services.TamTruService;
import com.se07.controller.services.TamTruService;
import com.se07.model.models.HoKhauModel;
import com.se07.model.models.TamTruModel;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.controlsfx.control.spreadsheet.Grid;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

public class ControllerDangKyTamTruNhanKhauHoGiaDinhView extends ControllerHoGiaDinhView implements Initializable {
    @FXML
    TextField textFieldCCCDDangKyTamTruHoGiaDinh, textFieldLyDoDangKyTamTruHoGiaDinh, textFieldHoTenDangKyTamTruHoGiaDinh;
    @FXML
    ComboBox comboBoxNoiTamTruDangKyTamTruHoGiaDinh;
    @FXML
    DatePicker datePickerTuNgayDangKyTamTruHoGiaDinh, datePickerDenNgayDangKyTamTruHoGiaDinh;
    @FXML
    GridPane gridPaneDangKyTamTruHoGiaDinh;
    HoKhauService hoKhauService = new HoKhauService();
    LocalDate today = LocalDate.now();
    private String tinhTrang = "Chờ xác nhận";
    final ObservableList<String> listCCCD = new TamTruService().getAllCCCD();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gridPaneDangKyTamTruHoGiaDinh.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                dangKyThemMoiTamTruHoGiaDinh();
            } else if (keyEvent.getCode().equals(KeyCode.Q)) {
                try {
                    huyDangKyTamTruNhanKhauHoGiaDinh(keyEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        comboBoxNoiTamTruDangKyTamTruHoGiaDinh.getItems().addAll(hoKhauService.getAllDiaChiHoKhau(maHoKhauDangNhap));
        comboBoxNoiTamTruDangKyTamTruHoGiaDinh.getSelectionModel().selectFirst();
        datePickerTuNgayDangKyTamTruHoGiaDinh.setValue(today);
        datePickerDenNgayDangKyTamTruHoGiaDinh.setValue(today.plusDays(7));
    }

    public void onPressedButtonDangKyTamTruNhanKhauHoGiaDinh(MouseEvent e) throws IOException {
        if (e.isPrimaryButtonDown()) {
            dangKyThemMoiTamTruHoGiaDinh();
        }
    }

    public void onPressedButtonHuyDangKyTamTruNhanKhauHoGiaDinh(MouseEvent e) throws IOException {
        if (e.isPrimaryButtonDown()) {
            huyDangKyTamTruNhanKhauHoGiaDinh(e);
        }
    }

    public void dangKyThemMoiTamTruHoGiaDinh() {
        TamTruService tamTruService = new TamTruService();
        if (textFieldLyDoDangKyTamTruHoGiaDinh.getText().isBlank() || textFieldCCCDDangKyTamTruHoGiaDinh.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("Vui lòng nhập đầy đủ các trường");
            alert.showAndWait();
            return;
        }
        String CCCD = textFieldCCCDDangKyTamTruHoGiaDinh.getText();
        for (String tmp :
                listCCCD) {
            if (tmp.equals(CCCD)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Thông báo");
                alert.setHeaderText("Mã CCCD đã tồn tại");
                alert.showAndWait();
                return;
            }
        }
        String maHoKhau = maHoKhauDangNhap;
        String hoTen = textFieldHoTenDangKyTamTruHoGiaDinh.getText();
        Date tuNgay = new Date(datePickerTuNgayDangKyTamTruHoGiaDinh.getValue().toEpochDay());
        Date denNgay = new Date(datePickerDenNgayDangKyTamTruHoGiaDinh.getValue().toEpochDay());
        String lyDo = textFieldLyDoDangKyTamTruHoGiaDinh.getText();
        TamTruModel tamTruModel = new TamTruModel(maHoKhau, CCCD, hoTen, tuNgay, denNgay, lyDo, tinhTrang, id);
        if (tamTruService.addTamTru(tamTruModel)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Thông báo");
            alert.setHeaderText("");
            alert.setContentText("Bạn đã đăng ký thành công");
            if (alert.showAndWait().get() == ButtonType.OK) {
                textFieldLyDoDangKyTamTruHoGiaDinh.setText("");
                textFieldCCCDDangKyTamTruHoGiaDinh.setText("");
                textFieldHoTenDangKyTamTruHoGiaDinh.setText("");
            }
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText("");
        alert.setContentText("Đăng ký tạm trú thất bại!");
        alert.showAndWait();
    }

    public void huyDangKyTamTruNhanKhauHoGiaDinh(Event e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Thông báo");
        alert.setHeaderText("");
        alert.setContentText("Bạn muốn thoát khỏi màn hình đăng ký tạm trú!");
        if (alert.showAndWait().get() == ButtonType.OK) {
            sceneLoader.loadFxmlFileHoGiaDinh((Stage) ((Node) e.getSource()).getScene().getWindow(), "TamTruHoGiaDinhView.fxml");
        }
    }
}
