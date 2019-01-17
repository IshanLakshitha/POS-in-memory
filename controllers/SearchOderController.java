package lk.ijse.fx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lk.ijse.fx.model.Order;
import lk.ijse.fx.model.OrderDetail;
import lk.ijse.fx.util.ManageCustomers;
import lk.ijse.fx.util.ManageOrders;
import lk.ijse.fx.view.util.OrderTM;
import sun.security.krb5.SCDynamicStoreConfig;
import sun.security.pkcs.SigningCertificateInfo;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

public class SearchOderController {

    @FXML
    private TableView<OrderTM> tblOrderDetails;
    @FXML
    private TextField txtSearch;
    @FXML
    private ObservableList<OrderTM> olOrders;

    public void initialize() {
        tblOrderDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tblOrderDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        tblOrderDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("customerId"));

        ArrayList<Order> ordersDB = ManageOrders.getOrdersDB();
        olOrders = FXCollections.observableArrayList();

        for (Order order : ordersDB) {
            olOrders.add(new OrderTM(order.getId(), order.getDate(), order.getCustomerId(),
            ManageCustomers.findCustomer(order.getCustomerId()).getName(),
            getOrderTotal(order.getOrderDetails())));
        }

        tblOrderDetails.setItems(olOrders);
    }

    private double getOrderTotal(ArrayList<OrderDetail> orderDetails){
        double total = 0.0;
        for (OrderDetail orderDetail : orderDetails) {
            total += orderDetail.getQty() * orderDetail.getUnitPrice();
        }
        return total;
    }



    public void search(KeyEvent keyEvent) {
        ObservableList<OrderTM> tempList = FXCollections.observableArrayList();
        for (OrderTM olOrder : olOrders) {
            if(olOrder.getOrderId().startsWith(txtSearch.getText())){
                tempList.add(olOrder);
            }
        }
        tblOrderDetails.setItems(tempList);
    }

    public void clickBack(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("/lk/ijse/fx/view/MainPage.fxml"));
        Scene scene = new Scene(parent);

        Stage primaryStage = (Stage)txtSearch.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    private void ClickRow(MouseEvent mouseEvent) throws IOException {
            if(mouseEvent.getClickCount() == 2){
                OrderTM selectedItem = tblOrderDetails.getSelectionModel().getSelectedItem();

                FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/lk/ijse/fx/view/ShowOneOrder.fxml"));
                Parent parent =(Parent) fxmlLoader.load();
                ShowOneOrder controller = fxmlLoader.getController();
                controller.setInitData(selectedItem.getOrderId(), selectedItem.getTotal());
                Scene scene = new Scene(parent);
                ((Stage)tblOrderDetails.getScene().getWindow()).setScene(scene);
            }
        }
    }

