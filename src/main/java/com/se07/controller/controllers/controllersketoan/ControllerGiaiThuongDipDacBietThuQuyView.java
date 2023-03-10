package com.se07.controller.controllers.controllersketoan;

import com.se07.controller.services.DipTraoThuongService;
import com.se07.controller.services.PhanThuongService;
import com.se07.controller.services.ThongTinDipDacBietService;
import com.se07.controller.services.TraoThuongService;
import com.se07.model.models.ThongTinTraoThuongDipDacBiet;
import com.se07.model.models.TraoThuongModel;
import com.se07.view.TreasurerView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerGiaiThuongDipDacBietThuQuyView extends ControllerThuQuyView {

    String tenDip;
    int nam, idDip;
    @FXML
    Label labelTenDipTraoThuongDipDacBiet, labelNamTraoThuongDipDacBiet;
    @FXML
    TableView<ThongTinTraoThuongDipDacBiet> tableViewGiaiThuongThuQuy;
    @FXML
    TableColumn<ThongTinTraoThuongDipDacBiet, String> tableColumnMaNhanKhauGiaiThuongDipDacBietThuQuy,
            tableColumnHoTenGiaiThuongDipDacBietThuQuy, tableColumnTenGiaiThuongDipDacBietThuQuy;
    @FXML
    TableColumn<ThongTinTraoThuongDipDacBiet, Integer> tableColumnIDNhapGiaiThuongDipDacBietThuQuy,
            tableColumnDonGiaGiaiThuongDipDacBietThuQuy, tableColumnSoLuongGiaiThuongDipDacBietThuQuy,
            tableColumnThanhTienGiaiThuongDipDacBietThuQuy;
    private final TraoThuongService traoThuongService = new TraoThuongService();
    private final PhanThuongService phanThuongService = new PhanThuongService();

    public void onPressedButtonChonPhanQuaThuQuy(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.isPrimaryButtonDown()) {
            ThongTinTraoThuongDipDacBiet thongTinTraoThuongDipDacBiet =
                    tableViewGiaiThuongThuQuy.getSelectionModel().getSelectedItem();
            if (thongTinTraoThuongDipDacBiet == null) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Th??ng b??o");
                alert.setHeaderText("Vui l??ng ch???n tr?????ng h???p mu???n trao qu??");
                alert.showAndWait();
            } else {
                if (thongTinTraoThuongDipDacBiet.getTenPhanThuong() != null) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Th??ng b??o");
                    alert.setHeaderText("Tr?????ng h???p ???? ???????c trao th?????ng. B???n c?? mu???n ti???p t???c?");
                    alert.setContentText("Th??ng tin v??? l???n trao th?????ng c?? s??? b??? x??a");
                    if (alert.showAndWait().get() == ButtonType.CANCEL) return;
                }
                traoThuongThuQuy(thongTinTraoThuongDipDacBiet.getIdNhap());
            }
        }
    }

    private void traoThuongThuQuy(int idNhap) throws IOException {
        FXMLLoader loader = new FXMLLoader(TreasurerView.class.getResource("TraoThuongThuQuyView.fxml"));
        Parent root = loader.load();
        ControllerTraoThuongThuQuyView controller = loader.getController();
        controller.tenDip = tenDip;
        controller.nam = nam;
        controller.idNhap = idNhap;
        controller.setLabel();
        Stage stage = (Stage) tableViewGiaiThuongThuQuy.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);

    }


    public void onPressedButtonTraoThuongChoTatCaDipDacBietThuQuy(MouseEvent mouseEvent) throws IOException {
        if (mouseEvent.isPrimaryButtonDown()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("B???n c?? mu???n ti???p t???c?");
            alert.setContentText("Th??ng tin v??? l???n trao th?????ng c?? s??? b??? x??a");
            if (alert.showAndWait().get() == ButtonType.CANCEL) return;
            traoThuongThuQuy(-1);
        }
    }

    public void loadData() {
        tableColumnIDNhapGiaiThuongDipDacBietThuQuy.setCellValueFactory(new PropertyValueFactory<>("idNhap"));
        tableColumnMaNhanKhauGiaiThuongDipDacBietThuQuy.setCellValueFactory(new PropertyValueFactory<>("maNhanKhau"));
        tableColumnHoTenGiaiThuongDipDacBietThuQuy.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        tableColumnTenGiaiThuongDipDacBietThuQuy.setCellValueFactory(new PropertyValueFactory<>("tenPhanThuong"));
        tableColumnDonGiaGiaiThuongDipDacBietThuQuy.setCellValueFactory(new PropertyValueFactory<>("donGia"));
        tableColumnSoLuongGiaiThuongDipDacBietThuQuy.setCellValueFactory(new PropertyValueFactory<>("soLuong"));
        tableColumnThanhTienGiaiThuongDipDacBietThuQuy.setCellValueFactory(new PropertyValueFactory<>("thanhTien"));

        labelTenDipTraoThuongDipDacBiet.setText("T??n d???p: " + tenDip);
        labelNamTraoThuongDipDacBiet.setText("N??m: " + nam);
        idDip = new DipTraoThuongService().getDipTraoThuongByTenAndNam(tenDip, nam).get().getId();
        tableViewGiaiThuongThuQuy.setItems(new ThongTinDipDacBietService().getAllThongTinDipDacBietAndTraoThuongByIdDip(idDip));
    }

    public void onPressedButtonXoaDipDacBietThuQuy(MouseEvent e) throws IOException {
        if (e.isPrimaryButtonDown()) {
            xoaTraoThuongDipDacBietThuQuy();
        }
    }

    private void xoaTraoThuongDipDacBietThuQuy() {
        ThongTinTraoThuongDipDacBiet thongTinTraoThuongDipDacBiet = tableViewGiaiThuongThuQuy.getSelectionModel().getSelectedItem();
        if (thongTinTraoThuongDipDacBiet == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("Vui l??ng ch???n tr?????ng h???p mu???n x??a");
            alert.showAndWait();
        } else if (thongTinTraoThuongDipDacBiet.getTenPhanThuong() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("Tr?????ng h???p ch??a ???????c trao th?????ng");
            alert.showAndWait();
        } else {
            TraoThuongModel traoThuongModel = new TraoThuongModel(
                    thongTinTraoThuongDipDacBiet.getIdNhap(),
                    phanThuongService.getPhanThuongByTen(thongTinTraoThuongDipDacBiet.getTenPhanThuong()).get().getMaPhanThuong(),
                    thongTinTraoThuongDipDacBiet.getSoLuong()
            );
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("B???n ch???c ch???n mu???n x??a tr?????ng h???p n??y");
            if (alert.showAndWait().get() == ButtonType.OK) {
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Th??ng b??o");
                if (traoThuongService.deleteTraoThuongDipDacBiet(traoThuongModel)) {
                    info.setHeaderText("X??a th??nh c??ng");
                    thongTinTraoThuongDipDacBiet.setTenPhanThuong(null);
                    thongTinTraoThuongDipDacBiet.setDonGia(null);
                    thongTinTraoThuongDipDacBiet.setSoLuong(null);
                    thongTinTraoThuongDipDacBiet.setThanhTien(null);
                } else {
                    info.setHeaderText("X??a kh??ng th??nh c??ng");
                }
                info.showAndWait();
                tableViewGiaiThuongThuQuy.refresh();
            }
        }
    }
}
