package com.se07.controller.controllers.controllersketoan;

import com.se07.controller.services.*;
import com.se07.model.models.DipTraoThuongModel;
import com.se07.model.models.TraoThuongModel;
import com.se07.view.TreasurerView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ControllerTraoThuongThuQuyView extends ControllerThuQuyView {

    public String tenDip;
    public int nam, idNhap;
    @FXML
    Label labelTenNamTraoThuongThuQuy, labelIDNhapTraoThuongThuQuy;
    @FXML
    ComboBox<String> comboBoxChonQua1, comboBoxChonQua2, comboBoxChonQua3, comboBoxChonQua4, comboBoxChonQua5;

    ComboBox<String>[] comboBoxChonQua;
    @FXML
    TextField textFieldChonSoLuong1, textFieldChonSoLuong2, textFieldChonSoLuong3, textFieldChonSoLuong4,
            textFieldChonSoLuong5;
    TextField[] textFieldChonSoLuong;

    private final ObservableList<String> listTenPhanThuong = new PhanThuongService().getAllTenPhanThuong();
    private final PhanThuongService phanThuongService = new PhanThuongService();
    private final TraoThuongService traoThuongService = new TraoThuongService();
    private final DipTraoThuongService dipTraoThuongService = new DipTraoThuongService();

    private final ThongTinDipDacBietService thongTinDipDacBietService = new ThongTinDipDacBietService();
    private final ThongTinThanhTichService thongTinThanhTichService = new ThongTinThanhTichService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        listTenPhanThuong.add("");
        comboBoxChonQua = new ComboBox[]{comboBoxChonQua1, comboBoxChonQua2, comboBoxChonQua3, comboBoxChonQua4, comboBoxChonQua5};
        for (ComboBox comboBox : comboBoxChonQua) {
            comboBox.setItems(listTenPhanThuong);
        }
        textFieldChonSoLuong = new TextField[]{
                textFieldChonSoLuong1, textFieldChonSoLuong2, textFieldChonSoLuong3, textFieldChonSoLuong4, textFieldChonSoLuong5};
    }

    public void onPressedButtonXacNhanChonQuaThuQuy(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            xacNhanChonQuaThuQuy();
        }
    }

    private void xacNhanChonQuaThuQuy() {
        ArrayList<TraoThuongModel> traoThuongModels = new ArrayList<>();
        HashMap<String, Integer> tenPhanThuong = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            String tenQua = comboBoxChonQua[i].getValue();
            String stringSoLuong = textFieldChonSoLuong[i].getText();
            int soLuong;
            if (tenQua == null || tenQua.isBlank()) {
                if (!stringSoLuong.isBlank()) {
                    hienThiLoiNhap();
                    return;
                }
            } else {
                if (tenPhanThuong.containsKey(tenQua) || stringSoLuong.isBlank()) {
                    hienThiLoiNhap();
                    return;
                } else {
                    try {
                        soLuong = Integer.valueOf(stringSoLuong);
                    } catch (NumberFormatException numberFormatException) {
                        hienThiLoiNhap();
                        return;
                    }
                    tenPhanThuong.put(tenQua, soLuong);
                }
            }
        }

        if (tenPhanThuong.isEmpty()) {
            hienThiLoiNhap();
            return;
        }

        for (Map.Entry<String, Integer> entry : tenPhanThuong.entrySet()) {
            traoThuongModels.add(new TraoThuongModel(
                    phanThuongService.getPhanThuongByTen(entry.getKey()).get().getMaPhanThuong(),
                    entry.getValue()));
        }

        DipTraoThuongModel dipTraoThuongModel = dipTraoThuongService.getDipTraoThuongByTenAndNam(tenDip, nam).get();
        if (dipTraoThuongModel.getKieu().equals("D???p ?????c bi???t")) {
            if (idNhap == -1) {
                for (int id : thongTinDipDacBietService.getAllIdNhapTheoIdDip(dipTraoThuongModel.getId()))
                    for (TraoThuongModel traoThuongModel : traoThuongModels) {
                        traoThuongModel.setIdNhap(id);
                        if (!traoThuongService.deleteTraoThuongDipDacBiet(traoThuongModel)) {
                            hienThiLoiCoSoDuLieu();
                            return;
                        }
                        if (!traoThuongService.addTraoThuongDipDacBiet(traoThuongModel)) {
                            hienThiLoiCoSoDuLieu();
                            return;
                        }
                    }
            } else {
                for (TraoThuongModel traoThuongModel : traoThuongModels) {
                    traoThuongModel.setIdNhap(idNhap);
                    if (!traoThuongService.deleteTraoThuongDipDacBiet(traoThuongModel)) {
                        hienThiLoiCoSoDuLieu();
                        return;
                    }
                    if (!traoThuongService.addTraoThuongDipDacBiet(traoThuongModel)) {
                        hienThiLoiCoSoDuLieu();
                        return;
                    }
                }
            }
        } else {
            if (idNhap == -1) {
                for (int id : thongTinThanhTichService.getAllIdNhapTheoIdDip(dipTraoThuongModel.getId()))
                    for (TraoThuongModel traoThuongModel : traoThuongModels) {
                        traoThuongModel.setIdNhap(id);
                        if (!traoThuongService.deleteTraoThuongThanhTich(traoThuongModel)) {
                            hienThiLoiCoSoDuLieu();
                            return;
                        }
                        if (!traoThuongService.addTraoThuongThanhTich(traoThuongModel)) {
                            hienThiLoiCoSoDuLieu();
                            return;
                        }
                    }
            } else {
                for (TraoThuongModel traoThuongModel : traoThuongModels) {
                    traoThuongModel.setIdNhap(idNhap);
                    if (!traoThuongService.deleteTraoThuongThanhTich(traoThuongModel)) {
                        hienThiLoiCoSoDuLieu();
                        return;
                    }
                    if (!traoThuongService.addTraoThuongThanhTich(traoThuongModel)) {
                        hienThiLoiCoSoDuLieu();
                        return;
                    }
                }
            }
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Th??ng b??o");
        alert.setHeaderText("Trao th?????ng th??nh c??ng");
        alert.showAndWait();
    }

    private void hienThiLoiNhap() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Th??ng b??o");
        alert.setHeaderText("Nh???p th??ng tin kh??ng h???p l???. Vui l??ng nh???p l???i!");
        alert.showAndWait();
    }

    private void hienThiLoiCoSoDuLieu() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Th??ng b??o");
        alert.setHeaderText("Trao th?????ng kh??ng th??nh c??ng");
        alert.showAndWait();
    }


    public void onPressedButtonThemLoaiPhanThuongThuQuy(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.isPrimaryButtonDown()) {
            Stage stage = new Stage();
            stage.initOwner(((Node) mouseEvent.getSource()).getScene().getWindow());
            stage.setTitle("Th??m ph???n th?????ng");
            stage.setScene(new Scene(
                    new FXMLLoader(TreasurerView.class.getResource("ThemLoaiPhanThuongThuQuyView.fxml")).load()));
            stage.show();
        }
    }

    public void onPressedButtonHuyChonQuaThuQuy(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.isPrimaryButtonDown()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("B???n ch???c ch???n mu???n tho??t?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                sceneLoader.loadFxmlFileThuQuy((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow(),
                        "GiaiThuongThuQuyView.fxml");
            }
        }
    }

    public void setLabel() {
        labelTenNamTraoThuongThuQuy.setText("T??n d???p - n??m: " + tenDip + " - " + nam);
        labelIDNhapTraoThuongThuQuy.setText("ID Nh???p: " + ((idNhap == -1) ? "T???t c???" : idNhap));
    }
}
