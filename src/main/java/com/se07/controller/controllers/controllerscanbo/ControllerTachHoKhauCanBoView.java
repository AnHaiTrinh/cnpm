package com.se07.controller.controllers.controllerscanbo;

import com.se07.controller.services.HoKhauService;
import com.se07.controller.services.NhanKhauService;
import com.se07.model.models.HoKhauModel;
import com.se07.model.models.NhanKhauModel;
import com.se07.util.SceneLoader;
import com.se07.util.UserInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

public class ControllerTachHoKhauCanBoView implements Initializable {
    @FXML
    TextField textFieldMaHoKhau, textFieldChuHo, textFieldDiaChi,
            textFieldMaHoKhauMoi, textFieldDiaChiMoi;
    @FXML
    ComboBox comboBoxChuHoMoi;
    @FXML
    TableView<NhanKhauModel> tableViewHoKhauCu, tableViewHoKhauMoi;
    @FXML
    TableColumn<NhanKhauModel, String> tableColumnMaNhanKhauCu, tableColumnHoTenCu,
            tableColumnMaNhanKhauMoi, tableColumnHoTenMoi;

    final int id = UserInfo.getUserId();
    private final HoKhauService hoKhauService = new HoKhauService();
    private final NhanKhauService nhanKhauService = new NhanKhauService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableColumnMaNhanKhauCu.setCellValueFactory(new PropertyValueFactory<>("maNhanKhau"));
        tableColumnMaNhanKhauMoi.setCellValueFactory(new PropertyValueFactory<>("maNhanKhau"));
        tableColumnHoTenCu.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        tableColumnHoTenMoi.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
    }

    public void onEnterPressedTrongOTimKiemMaHoKhau(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            timKiemMaHoKhau();
        }
    }

    public void onPressedButtonOKMaHoKhau(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            timKiemMaHoKhau();
        }
    }

    private void timKiemMaHoKhau() {
        String maHoKhau = textFieldMaHoKhau.getText();
        Optional<HoKhauModel> hoKhauModel = hoKhauService.getHoKhauByMaHoKhau(maHoKhau);
        if (hoKhauModel.isPresent()) {
            textFieldChuHo.setText(hoKhauModel.get().getChuHo());
            textFieldDiaChi.setText(hoKhauModel.get().getDiaChi());
            ObservableList<NhanKhauModel> nhanKhauModels = nhanKhauService.getAllNhanKhauTrongHoKhau(maHoKhau);
            tableViewHoKhauCu.setItems(nhanKhauModels);
            tableViewHoKhauMoi.setItems(FXCollections.observableArrayList());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("M?? h??? kh???u kh??ng t???n t???i");
            alert.showAndWait();
        }
    }

    public void onPressedButtonChapNhanTachHoKhauCanBo(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            tachHoKhau();
        }
    }

    private void tachHoKhau() {
        if (textFieldMaHoKhauMoi.getText().isBlank() || comboBoxChuHoMoi.getValue() == null ||
                textFieldDiaChiMoi.getText().isBlank()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("Vui l??ng nh???p ????? c??c tr?????ng th??ng tin cho h???");
            alert.showAndWait();
        } else if (tableViewHoKhauMoi.getItems().size() == 0 || tableViewHoKhauCu.getItems().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("H??? kh???u kh??ng ???????c ????? tr???ng");
            alert.showAndWait();
        } else {
            String maHoKhauMoi = textFieldMaHoKhauMoi.getText();
            String hoTen = String.valueOf(comboBoxChuHoMoi.getValue());
            if (hoKhauService.getHoKhauByMaHoKhau(maHoKhauMoi).isPresent()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Th??ng b??o");
                alert.setHeaderText("M?? h??? kh???u ???? t???n t???i!");
                alert.showAndWait();
                return;
            }
            HoKhauModel hoKhauModel = new HoKhauModel(maHoKhauMoi, hoTen, textFieldDiaChiMoi.getText(),
                    Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()), id);
            boolean error = false;
            if (hoKhauService.addHoKhau(hoKhauModel)) {
                for (NhanKhauModel nhanKhauModel : tableViewHoKhauMoi.getItems()) {
                    nhanKhauModel.setMaHoKhau(maHoKhauMoi);
                    if (!nhanKhauService.updateNhanKhau(nhanKhauModel)) {
                        error = true;
                        break;
                    }
                }
            } else {
                error = true;
            }
            if (error) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Th??ng b??o");
                alert.setHeaderText("T??ch h??? kh???u kh??ng th??nh c??ng");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Th??ng b??o");
                alert.setHeaderText("T??ch h??? kh???u th??nh c??ng");
                alert.showAndWait();
            }
        }
    }

    public void onPressedButtonKhongChapNhanTachHoKhauCanBo(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.isPrimaryButtonDown()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("B???n ch???c ch???n mu???n tho??t");
            if (alert.showAndWait().get() == ButtonType.OK) {
                new SceneLoader().loadFxmlFileCanBo((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow(),
                        "HoKhauCanBoView.fxml");
            }
        }
    }

    public void onPressedButtonChuyenNhanKhau(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            chuyenNhanKhauCuSangMoi();
        }
    }

    private void chuyenNhanKhauCuSangMoi() {
        NhanKhauModel nhanKhauModel = tableViewHoKhauCu.getSelectionModel().getSelectedItem();
        if (nhanKhauModel == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("Vui l??ng ch???n nh??n kh???u mu???n chuy???n sang h??? kh???u m???i");
            alert.showAndWait();
        } else if (nhanKhauModel.getHoTen().equals(textFieldChuHo.getText())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("Kh??ng th??? chuy???n ch??? h??? sang h??? m???i");
            alert.showAndWait();
        } else {
            tableViewHoKhauCu.getItems().remove(nhanKhauModel);
            tableViewHoKhauMoi.getItems().add(nhanKhauModel);
            comboBoxChuHoMoi.getItems().add(nhanKhauModel.getHoTen());
            tableViewHoKhauCu.refresh();
            tableViewHoKhauMoi.refresh();
        }
    }

    public void onPressedButtonHuyChuyenNhanKhau(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            chuyenNhanKhauMoiVeCu();
        }
    }

    private void chuyenNhanKhauMoiVeCu() {
        NhanKhauModel nhanKhauModel = tableViewHoKhauMoi.getSelectionModel().getSelectedItem();
        if (nhanKhauModel == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("Vui l??ng ch???n nh??n kh???u mu???n chuy???n v??? h??? kh???u c??");
            alert.showAndWait();
        } else {
            tableViewHoKhauMoi.getItems().remove(nhanKhauModel);
            tableViewHoKhauCu.getItems().add(nhanKhauModel);
            comboBoxChuHoMoi.getItems().remove(nhanKhauModel.getHoTen());
            tableViewHoKhauCu.refresh();
            tableViewHoKhauMoi.refresh();
        }
    }
}
