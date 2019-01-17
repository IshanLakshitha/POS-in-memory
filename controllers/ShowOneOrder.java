package lk.ijse.fx.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lk.ijse.fx.model.Order;
import lk.ijse.fx.model.OrderDetail;
import lk.ijse.fx.util.ManageCustomers;
import lk.ijse.fx.util.ManageOrders;
import lk.ijse.fx.view.util.OrderDetailTM;

import java.io.IOException;
import java.util.ArrayList;

public class ShowOneOrder {
    @FXML
    private DatePicker DatePick;
    @FXML
    private TextField txtOrderID;
    @FXML
    private TextField txtCustomerID;
    @FXML
    private TextField txtCustomerName;
    @FXML
    private TableView<OrderDetailTM> tblItemDetails;
    @FXML
    private Button btnBack;
    @FXML
    private Label lblTotal;

    private String orderId;

    public void clickBackButton(ActionEvent actionEvent) throws IOException {
        Parent parent = FXMLLoader.load(this.getClass().getResource("/lk/ijse/fx/view/ShowOrdersPage.fxml"));
        Scene scene = new Scene(parent);
        Stage primaryStage = (Stage) lblTotal.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public void initialize() {
        tblItemDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("code"));
        tblItemDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItemDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblItemDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblItemDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));

        reset();
    }

    public void setInitData(String orderId, double orderTotal){
        this.orderId = orderId;
        lblTotal.setText("Total : "+ orderTotal);
        fillData();

    }

    public void fillData() {
        Order order = ManageOrders.findOrder(this.orderId);
        txtCustomerID.setText(order.getCustomerId());
        txtOrderID.setText(order.getId());
        DatePick.setValue(order.getDate());
        txtCustomerName.setText(ManageCustomers.findCustomer(order.getCustomerId()).getName());

        ArrayList<OrderDetail> orderDetails = order.getOrderDetails();
        ObservableList<OrderDetailTM> details = FXCollections.observableArrayList();

        for (OrderDetail orderDetail : orderDetails) {
            details.add(new OrderDetailTM(orderDetail.getCode(),
                    orderDetail.getDescription(),
                    orderDetail.getQty(),
                    orderDetail.getUnitPrice(),
                    orderDetail.getQty() * orderDetail.getUnitPrice()));
        }
        tblItemDetails.setItems(details);
    }

    public void reset(){
        txtCustomerName.setEditable(false);
        txtCustomerID.setEditable(false);
        txtOrderID.setEditable(false);
    }

}
