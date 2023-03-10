package com.se07.controller.controllers.controllerscanbo;

import com.se07.controller.services.DipTraoThuongService;
import com.se07.controller.services.NhanKhauService;
import com.se07.controller.services.ThongTinDipDacBietService;
import com.se07.model.models.DipTraoThuongModel;
import com.se07.model.models.ThongTinDipDacBietDisplayModel;
import com.se07.model.models.ThongTinDipDacBietModel;
import com.se07.util.ComponentVisibility;
import com.se07.util.MyIntegerStringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class ControllerGiaiThuongDipDacBietCanBoView extends ControllerCanBoView {
    @FXML
    ComboBox comboBoxTimKiemDipDacBietCanBo, comboBoxTenNamDipDacBietCanBo,
            comboBoxMaNhanKhauDipDacBietCanBo, comboBoxTinhTrangDipDacBietCanBo;
    @FXML
    TableView<ThongTinDipDacBietDisplayModel> tableViewDipDacBietCanBo;
    @FXML
    TableColumn<ThongTinDipDacBietDisplayModel, String> tableColumnMaNhanKhauDipDacBietCanBo,
            tableColumnHoTenDipDacBietCanBo, tableColumnTenDipDacBietCanBo, tableColumnTinhTrangDipDacBietCanBo;
    @FXML
    TableColumn<ThongTinDipDacBietDisplayModel, Integer> tableColumnNamDipDacBietCanBo;
    @FXML
    TextField textFieldLocThongTinDipDacBietCanBo;

    private final DipTraoThuongService dipTraoThuongService = new DipTraoThuongService();
    private final ThongTinDipDacBietService thongTinDipDacBietService = new ThongTinDipDacBietService();
    final private MyIntegerStringConverter integerStringConverter = new MyIntegerStringConverter();
    final ObservableList<String> listTimKiem = FXCollections.observableArrayList(
            "H??? t??n", "M?? nh??n kh???u", "T??n d???p", "N??m", "T??n - N??m", "T??nh tr???ng");
    final ObservableList<String> listTenNamDipTraoThuong = dipTraoThuongService.getAllTenNamDipDacBiet();
    final ObservableList<String> listMaNhanKhau = new NhanKhauService().getAllMaNhanKhau();
    final ObservableList<String> listTinhTrang =
            FXCollections.observableArrayList("Ch??? x??c nh???n", "???? x??c nh???n", "???? t??? ch???i", "Ch??? x??a");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        super.initialize(url, resourceBundle);

        tableColumnMaNhanKhauDipDacBietCanBo.setCellValueFactory(new PropertyValueFactory<>("maNhanKhau"));
        tableColumnHoTenDipDacBietCanBo.setCellValueFactory(new PropertyValueFactory<>("hoTen"));
        tableColumnTenDipDacBietCanBo.setCellValueFactory(new PropertyValueFactory<>("tenDip"));
        tableColumnNamDipDacBietCanBo.setCellValueFactory(new PropertyValueFactory<>("nam"));
        tableColumnTinhTrangDipDacBietCanBo.setCellValueFactory(new PropertyValueFactory<>("tinhTrang"));

        comboBoxTimKiemDipDacBietCanBo.getItems().addAll(listTimKiem);
        comboBoxTimKiemDipDacBietCanBo.getSelectionModel().selectFirst();
        comboBoxTenNamDipDacBietCanBo.getItems().addAll(listTenNamDipTraoThuong);
        comboBoxTenNamDipDacBietCanBo.getSelectionModel().selectFirst();
        comboBoxMaNhanKhauDipDacBietCanBo.getItems().addAll(listMaNhanKhau);
        comboBoxMaNhanKhauDipDacBietCanBo.getSelectionModel().selectFirst();
        comboBoxTinhTrangDipDacBietCanBo.getItems().addAll(listTinhTrang);
        comboBoxTinhTrangDipDacBietCanBo.getSelectionModel().selectFirst();
        ComponentVisibility.change(comboBoxTenNamDipDacBietCanBo, false);
        ComponentVisibility.change(comboBoxMaNhanKhauDipDacBietCanBo, false);
        ComponentVisibility.change(comboBoxTinhTrangDipDacBietCanBo, false);

        tableViewDipDacBietCanBo.setEditable(true);
        tableColumnMaNhanKhauDipDacBietCanBo.setCellFactory(t -> new ComboBoxTableCell<>(listMaNhanKhau));
        tableColumnTinhTrangDipDacBietCanBo.setCellFactory(t -> new ComboBoxTableCell<>(listTinhTrang));

        tableViewDipDacBietCanBo.setItems(thongTinDipDacBietService.getAllThongTinDipDacBiet());
    }

    public void onSelectionComboBoxTimKiemDipDacBietCanBo(ActionEvent e) {
        String truongTimKiem = String.valueOf(comboBoxTimKiemDipDacBietCanBo.getValue());
        ComponentVisibility.change(textFieldLocThongTinDipDacBietCanBo, false);
        ComponentVisibility.change(comboBoxTenNamDipDacBietCanBo, false);
        ComponentVisibility.change(comboBoxMaNhanKhauDipDacBietCanBo, false);
        ComponentVisibility.change(comboBoxTinhTrangDipDacBietCanBo, false);
        switch (truongTimKiem) {
            case "M?? nh??n kh???u":
                ComponentVisibility.change(comboBoxMaNhanKhauDipDacBietCanBo, true);
                break;
            case "T??n - N??m":
                ComponentVisibility.change(comboBoxTenNamDipDacBietCanBo, true);
                break;
            case "T??nh tr???ng":
                ComponentVisibility.change(comboBoxTinhTrangDipDacBietCanBo, true);
                break;
            default:
                ComponentVisibility.change(textFieldLocThongTinDipDacBietCanBo, true);
                break;
        }
    }

    public void onPressedButtonThemMoiThongTinDipDacBietCanBo(MouseEvent e) throws IOException {
        if (e.isPrimaryButtonDown()) {
            sceneLoader.loadFxmlFileCanBo((Stage) ((Node) e.getSource()).getScene().getWindow(),
                    "ThemMoiThongTinDipDacBietCanBo.fxml");
        }
    }

    public void onPressedButtonLocThongTinDipDacBietCanBo(MouseEvent e) {
        if (e.isPrimaryButtonDown()) {
            locThongTinDipDacBietCanBo();
        }
    }

    public void onEnterPressedTrongOTimKiemDipDacBietCanBo(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            locThongTinDipDacBietCanBo();
        }
    }

    public void onPressedButtonPheDuyetThongTinDipDacBietCanBo(MouseEvent e) {
        if (e.isPrimaryButtonDown()) {
            xacNhanThongTinDipDacBietCanBo();
        }
    }

    private void xacNhanThongTinDipDacBietCanBo() {
        ThongTinDipDacBietDisplayModel thongTinDipDacBietDisplayModel =
                tableViewDipDacBietCanBo.getSelectionModel().getSelectedItem();
        if (thongTinDipDacBietDisplayModel == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("Vui l??ng ch???n tr?????ng h???p mu???n x??c nh???n");
            alert.showAndWait();
        } else if (thongTinDipDacBietDisplayModel.getTinhTrang() == "???? x??c nh???n") {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("Tr?????ng h???p ???? ???????c x??c nh???n");
            alert.showAndWait();
        } else if (thongTinDipDacBietDisplayModel.getTinhTrang().equals("Ch??? x??a")) {
            xoaThongTinDipDacBietCanBo();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("B???n ch???c ch???n mu???n x??c nh???n tr?????ng h???p n??y?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                thongTinDipDacBietDisplayModel.setTinhTrang("???? x??c nh???n");
                updateThongTinDipDacBietCanBo(thongTinDipDacBietDisplayModel);
            }
        }
    }

    public void onPressedButtonTuChoiThongTinDipDacBietCanBo(MouseEvent e) {
        if (e.isPrimaryButtonDown()) {
            tuChoiThongTinDipDacBietCanBo();
        }
    }

    private void tuChoiThongTinDipDacBietCanBo() {
        ThongTinDipDacBietDisplayModel thongTinDipDacBietDisplayModel =
                tableViewDipDacBietCanBo.getSelectionModel().getSelectedItem();
        if (thongTinDipDacBietDisplayModel == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("Vui l??ng ch???n tr?????ng h???p mu???n x??c nh???n");
            alert.showAndWait();
        } else if (thongTinDipDacBietDisplayModel.getTinhTrang().equals("???? t??? ch???i")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("Tr?????ng h???p ???? b??? t??? ch???i");
            alert.showAndWait();
        } else if (thongTinDipDacBietDisplayModel.getTinhTrang().equals("Ch??? x??a")) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("B???n ch???c ch???n mu???n kh??i ph???c tr?????ng h???p n??y?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                thongTinDipDacBietDisplayModel.setTinhTrang("???? x??c nh???n");
                updateThongTinDipDacBietCanBo(thongTinDipDacBietDisplayModel);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("B???n ch???c ch???n mu???n t??? ch???i tr?????ng h???p n??y?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                thongTinDipDacBietDisplayModel.setTinhTrang("???? t??? ch???i");
                updateThongTinDipDacBietCanBo(thongTinDipDacBietDisplayModel);
            }
        }
    }

    public void onDeletePressedTrongBangThongTinDipDacBietCanBo(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.DELETE)) {
            xoaThongTinDipDacBietCanBo();
        }
    }

    public void onPressedButtonXoaThongTinDipDacBietCanBo(MouseEvent e) {
        if (e.isPrimaryButtonDown()) {
            xoaThongTinDipDacBietCanBo();
        }
    }

    private void xoaThongTinDipDacBietCanBo() {
        ThongTinDipDacBietDisplayModel thongTinDipDacBietDisplayModel =
                tableViewDipDacBietCanBo.getSelectionModel().getSelectedItem();
        if (thongTinDipDacBietDisplayModel == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("Vui l??ng ch???n tr?????ng h???p mu???n x??a");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Th??ng b??o");
            alert.setHeaderText("B???n ch???c ch???n mu???n x??a tr?????ng h???p n??y!");
            if (alert.showAndWait().get() == ButtonType.OK) {
                Alert info = new Alert(Alert.AlertType.INFORMATION);
                info.setTitle("Th??ng b??o");
                if (thongTinDipDacBietService.deleteThongTinDipDacBiet(thongTinDipDacBietDisplayModel)) {
                    info.setHeaderText("X??a th??nh c??ng!");
                    tableViewDipDacBietCanBo.getItems().remove(thongTinDipDacBietDisplayModel);
                } else {
                    info.setHeaderText("X??a kh??ng th??nh c??ng!");
                }
                info.showAndWait();
                tableViewDipDacBietCanBo.refresh();
            }
        }
    }

    public void onPressedButtonThoatThongTinDipDacBietCanBo(MouseEvent e) throws IOException {
        if (e.isPrimaryButtonDown()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Th??ng b??o");
            alert.setContentText("B???n ch???c ch???n mu???n tho??t?");
            if (alert.showAndWait().get() == ButtonType.OK) {
                sceneLoader.loadFxmlFileCanBo((Stage) ((Node) e.getSource()).getScene().getWindow(),
                        "GiaiThuongCanBoView.fxml");
            }
        }
    }

    public void locThongTinDipDacBietCanBo() {
        String dieuKienKiemTra = String.valueOf(comboBoxTimKiemDipDacBietCanBo.getValue());
        String cauHoi = textFieldLocThongTinDipDacBietCanBo.getText();
        ObservableList<ThongTinDipDacBietDisplayModel> listThongTinDipDacBiet = FXCollections.observableArrayList();
        switch (dieuKienKiemTra) {
            case "M?? nh??n kh???u":
                listThongTinDipDacBiet = thongTinDipDacBietService.getAllThongTinDipDacBietByMaNhanKhau(
                        String.valueOf(comboBoxMaNhanKhauDipDacBietCanBo.getValue())
                );
                break;
            case "H??? t??n":
                listThongTinDipDacBiet = thongTinDipDacBietService.getAllThongTinDipDacBietByHoTen(cauHoi);
                break;
            case "T??n d???p":
                listThongTinDipDacBiet = thongTinDipDacBietService.getAllThongTinDipDacBietByTenDip(cauHoi);
                break;
            case "N??m":
                int nam = integerStringConverter.fromString(cauHoi);
                if (nam == -1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Th??ng b??o");
                    alert.setHeaderText("Vui l??ng nh???p n??m h???p l???");
                    alert.showAndWait();
                    textFieldLocThongTinDipDacBietCanBo.requestFocus();
                    return;
                }
                listThongTinDipDacBiet = thongTinDipDacBietService.getAllThongTinDipDacBietByNam(nam);
                break;
            case "T??n - N??m":
                String tenNam = String.valueOf(comboBoxTenNamDipDacBietCanBo.getValue());
                int index = tenNam.indexOf(" - ");
                String tenDip = tenNam.substring(0, index);
                int namDip = Integer.parseInt(tenNam.substring(index + 3));
                int id = dipTraoThuongService.getDipTraoThuongByTenAndNam(tenDip, namDip).get().getId();
                listThongTinDipDacBiet = thongTinDipDacBietService.getAllThongTinDipDacBietById(id);
                break;
            case "T??nh tr???ng":
                listThongTinDipDacBiet = thongTinDipDacBietService.getAllThongTinDipDacBietByTinhTrang(
                        String.valueOf(comboBoxTinhTrangDipDacBietCanBo.getValue())
                );
                break;
        }
        tableViewDipDacBietCanBo.setItems(listThongTinDipDacBiet);
    }

    private void updateThongTinDipDacBietCanBo(ThongTinDipDacBietDisplayModel thongTinDipDacBietDisplayModel) {
        ThongTinDipDacBietModel thongTinDipDacBietModel =
                thongTinDipDacBietService.convertDisplayModelToModel(thongTinDipDacBietDisplayModel);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Th??ng b??o!");
        if (thongTinDipDacBietService.updateThongTinDipDacBiet(thongTinDipDacBietModel)) {
            alert.setHeaderText("S???a th??ng tin th??nh c??ng");
        } else {
            alert.setHeaderText("S???a th??ng tin kh??ng th??nh c??ng");
        }
        if (alert.showAndWait().get() == ButtonType.OK) {
            tableViewDipDacBietCanBo.refresh();
        }
    }

    public void handleOnEditCommit(TableColumn.CellEditEvent<ThongTinDipDacBietDisplayModel, ?> event) {
        ThongTinDipDacBietDisplayModel thongTinDipDacBietDisplayModel = event.getRowValue();
        DipTraoThuongModel dipTraoThuongModel = dipTraoThuongService.getDipTraoThuongByTenAndNam(
                thongTinDipDacBietDisplayModel.getTenDip(),
                thongTinDipDacBietDisplayModel.getNam()).get();
        Date today = new Date();
        if (today.after(dipTraoThuongModel.getNgayTao()) && today.equals(dipTraoThuongModel.getNgayKetThuc())) {
            int column = event.getTablePosition().getColumn();
            switch (column) {
                case 0:
                    thongTinDipDacBietDisplayModel.setMaNhanKhau((String) event.getNewValue());
                    break;
                case 4:
                    thongTinDipDacBietDisplayModel.setTinhTrang((String) event.getNewValue());
                    break;
            }
            updateThongTinDipDacBietCanBo(thongTinDipDacBietDisplayModel);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Th??ng b??o!");
            alert.setHeaderText("D???p th??nh t??ch ch??a b???t ?????u ho???c ???? k???t th??c!");
            alert.showAndWait();
        }
    }

    public void handleOnEditCancel(TableColumn.CellEditEvent<ThongTinDipDacBietDisplayModel, ?> event) {
        ThongTinDipDacBietDisplayModel thongTinDipDacBietDisplayModel = event.getRowValue();
        int column = event.getTablePosition().getColumn();
        switch (column) {
            case 0:
                thongTinDipDacBietDisplayModel.setMaNhanKhau((String) event.getOldValue());
                break;
            case 4:
                thongTinDipDacBietDisplayModel.setTinhTrang((String) event.getOldValue());
                break;
        }
    }
}
