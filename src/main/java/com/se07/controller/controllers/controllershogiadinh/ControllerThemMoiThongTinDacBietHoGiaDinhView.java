package com.se07.controller.controllers.controllershogiadinh;

import com.se07.controller.services.DipTraoThuongService;
import com.se07.controller.services.NhanKhauService;
import com.se07.controller.services.ThongTinDipDacBietService;
import com.se07.model.models.NhanKhauModel;
import com.se07.model.models.ThongTinDipDacBietModel;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerThemMoiThongTinDacBietHoGiaDinhView extends ControllerHoGiaDinhView {

    @FXML
    ComboBox comboBoxTenDipDacBietHoGiaDinh, comboBoxNamDipDacBietHoGiaDinh, comboBoxMaNhanKhauDipDacBietHoGiaDinh;
    @FXML
    TextField textFieldHoTenDipDacBietHoGiaDinh;
    @FXML
    GridPane gridPaneThemMoiThongTinDipDacBietHoGiaDinh;

    final private NhanKhauService nhanKhauService = new NhanKhauService();
    final private DipTraoThuongService dipTraoThuongService = new DipTraoThuongService();

    final private ThongTinDipDacBietService thongTinDipDacBietService = new ThongTinDipDacBietService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);
        gridPaneThemMoiThongTinDipDacBietHoGiaDinh.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                themMoiThongTinDipDacBietHoGiaDinh();
            } else if (keyEvent.getCode().equals(KeyCode.Q)) {
                try {
                    huyThemMoiThongTinDipDacBietHoGiaDinh(keyEvent);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        comboBoxMaNhanKhauDipDacBietHoGiaDinh.getItems().addAll(nhanKhauService.getAllMaNhanKhauTrongHoKhau(maHoKhauDangNhap));
        comboBoxTenDipDacBietHoGiaDinh.getItems().addAll(dipTraoThuongService.getAllTenTraoThuongDipDacBiet());
        comboBoxNamDipDacBietHoGiaDinh.getItems().addAll(dipTraoThuongService.getAllNamTraoThuongDipDacBiet());
        comboBoxTenDipDacBietHoGiaDinh.getSelectionModel().selectFirst();
        comboBoxNamDipDacBietHoGiaDinh.getSelectionModel().selectFirst();
    }

    public void onSelectionComboBoxTenDipDacBietHoGiaDinh(ActionEvent e) {
        String tenDip = String.valueOf(comboBoxTenDipDacBietHoGiaDinh.getValue());
        ObservableList<Integer> listNam = dipTraoThuongService.getNamByTenDipDacBiet(tenDip);
        comboBoxNamDipDacBietHoGiaDinh.getItems().clear();
        comboBoxNamDipDacBietHoGiaDinh.getItems().addAll(listNam);
        comboBoxNamDipDacBietHoGiaDinh.getSelectionModel().selectFirst();
    }

    public void onSelectionComboBoxMaNhanKhauDipDacBietHoGiaDinh(ActionEvent e) {
        String maNhanKhau = String.valueOf(comboBoxMaNhanKhauDipDacBietHoGiaDinh.getValue());
        NhanKhauModel nhanKhauModel = nhanKhauService.getNhanKhauByMaNhanKhau(maNhanKhau).get();
        textFieldHoTenDipDacBietHoGiaDinh.setText(nhanKhauModel.getHoTen());
    }


    public void onPressedButtonHoanThanhThongTinDipDacBietHoGiaDinh(MouseEvent e) {
        if (e.isPrimaryButtonDown()) {
            themMoiThongTinDipDacBietHoGiaDinh();
        }
    }

    public void onPressedButtonHuyThongTinDipDacBietHoGiaDinh(MouseEvent e) throws IOException {
        if (e.isPrimaryButtonDown()) {
            huyThemMoiThongTinDipDacBietHoGiaDinh(e);
        }
    }

    private void themMoiThongTinDipDacBietHoGiaDinh() {
        if (comboBoxTenDipDacBietHoGiaDinh.getValue() == null || comboBoxNamDipDacBietHoGiaDinh.getValue() == null ||
                comboBoxMaNhanKhauDipDacBietHoGiaDinh.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("Vui l??ng nh???p ?????y ????? c??c tr?????ng");
            alert.showAndWait();
            return;
        }
        String tenDip = String.valueOf(comboBoxTenDipDacBietHoGiaDinh.getValue());
        int nam = Integer.parseInt(String.valueOf(comboBoxNamDipDacBietHoGiaDinh.getValue()));
        String maNhanKhau = String.valueOf(comboBoxMaNhanKhauDipDacBietHoGiaDinh.getValue());
        int idDip = dipTraoThuongService.getDipTraoThuongByTenAndNam(tenDip, nam).get().getId();
        ThongTinDipDacBietModel thongTinDipDacBietModel = new ThongTinDipDacBietModel(idDip, maNhanKhau, tinhTrang, id);
        if (thongTinDipDacBietService.addThongTinDipDacBiet(thongTinDipDacBietModel)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("");
            alert.setContentText("B???n ???? th??m th??ng tin th??nh c??ng");
            alert.showAndWait();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Th??ng b??o");
        alert.setHeaderText("");
        alert.setContentText("Th??m th??ng tin th???t b???i!");
        alert.showAndWait();
    }

    private void huyThemMoiThongTinDipDacBietHoGiaDinh(Event e) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Th??ng b??o");
        alert.setContentText("B???n ch???c ch???n mu???n tho??t?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            sceneLoader.loadFxmlFileHoGiaDinh((Stage) ((Node) e.getSource()).getScene().getWindow(),
                    "GiaiThuongDipDacBietHoGiaDinhView.fxml");
        }
    }

}
