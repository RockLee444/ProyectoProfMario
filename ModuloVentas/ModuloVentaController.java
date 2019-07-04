/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moduloventa;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.StageStyle;

/**
 *
 * @author Luis Matuz
 */
public class ModuloVentaController implements Initializable {
    
    @FXML private TableView table;
    @FXML private TableColumn codigo;
    @FXML private TableColumn nombre;
    @FXML private TableColumn cantidad;
    @FXML private TableColumn precio;
    @FXML private TableColumn imagen;
    
    @FXML private Button botonAgregar;
    @FXML private Button botonEditar;
    @FXML private Button botonEliminar;
    @FXML private Button botonCancelarSeleccion;
    @FXML private Button botonComprar;
    
    @FXML private Label subTotalLabel;
    @FXML private Label ivaLabel;
    @FXML private Label totalLabel;
    
    @FXML private TextField fieldCodigo;
    @FXML private TextField fieldCantidad;
    
    private Producto producto=null;
    private double subTotal;
    private double iva;
    private double total;
    
    private ObservableList<Producto> data = FXCollections.observableArrayList();;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        updateLabels();
        subTotal = 0;
        iva = 0;
        total = 0;
        
        botonComprar.setDisable(true);
        botonEditar.setDisable(true);
        botonEliminar.setDisable(true);
        botonCancelarSeleccion.setDisable(true);

        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
    @Override
    public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
        if(table.getSelectionModel().getSelectedItem() != null) 
        {    
           TableView.TableViewSelectionModel selectionModel = table.getSelectionModel();
           Producto producto =(Producto) selectionModel.getSelectedItem();
           if(producto!=null){
                    fieldCodigo.setText(""+producto.getCodigo());
                    fieldCodigo.setDisable(true);
                    fieldCantidad.setText(""+producto.getTotalAComprar());
                    botonAgregar.setDisable(true);
                    botonEditar.setDisable(false);
                    botonEliminar.setDisable(false);  
                    botonCancelarSeleccion.setDisable(false);
           }
         }
         }
     });
    }    
    
    @FXML
    public void agregarProducto(ActionEvent event){
        boolean found = false;
        if(!(fieldCantidad.getText().isEmpty() || fieldCodigo.getText().isEmpty())){
            int cantidadIngresada = Integer.parseInt(fieldCantidad.getText());
            int codigoIngresado = Integer.parseInt(fieldCodigo.getText());
          
            if(cantidadIngresada>0){
                Producto agregar = searchItemInTable();
              if(agregar!=null){
                 if((cantidadIngresada+agregar.getTotalAComprar())<=agregar.getCantidad()){
                     agregar.setTotalAComprar(agregar.getTotalAComprar()+ cantidadIngresada);
                    found = true;
                 } else {
                     found = true;
                     notEnoughStock(agregar);
                 }
                
              } else {
                  //Buscar en base de datos
                  try{
                        Connection conn;
                        String sSQL = "SELECT * FROM  productos WHERE codigo = '" + (""+codigoIngresado) + "'";
                        String url = "jdbc:mysql://localhost:3306/proyectovisual?autoReconnect=false&useSSL=false"; 
                        conn = DriverManager.getConnection(url,"root",""); 
                        Statement st = conn.createStatement(); 

                        ResultSet rs = st.executeQuery(sSQL);
                        while(rs.next()){
                            if(rs.getString("codigo").equals(""+codigoIngresado)){
                                int codigo = Integer.parseInt(rs.getString("codigo"));
                                int cantidad = Integer.parseInt(rs.getString("cantidad"));
                                double precio = Double.parseDouble(rs.getString("precio"));
                                String nombre = rs.getString("nombre");
                                
                               Producto encontrado = new Producto(codigo,nombre,cantidad,precio);
                               if((cantidadIngresada+encontrado.getTotalAComprar())<=encontrado.getCantidad()){
                                   encontrado.setTotalAComprar(encontrado.getTotalAComprar() + cantidadIngresada);
                                   data.add(encontrado);
                                   updateTable();
                                   found = true;
                               } else {
                                   found = true;
                                   notEnoughStock(encontrado);
                               }
                                
                            }
                        }
                        
                        if(!found){
                            Alert alerta = new Alert(Alert.AlertType.ERROR);
                            alerta.setTitle("ERROR");
                            alerta.setContentText("Producto no encontrado.");
                            alerta.initStyle(StageStyle.UTILITY);
                            alerta.setHeaderText(null);
                            alerta.showAndWait();
                        }
                  } catch(Exception e){
                        Alert alerta = new Alert(Alert.AlertType.ERROR);
                            alerta.setTitle("ERROR");
                            alerta.setContentText("Error al conectar con la base de datos. Contacte a un asesor.");
                            alerta.initStyle(StageStyle.UTILITY);
                            alerta.setHeaderText(null);
                            alerta.showAndWait();
                  }
                  
              }
              if(found){
                  updateTable();
                  updatePrices();
              }
            } else {
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("ERROR");
                    alerta.setContentText("Por favor, ingrese una cantidad válida.");
                    alerta.initStyle(StageStyle.UTILITY);
                    alerta.setHeaderText(null);
                    alerta.showAndWait();
            }
          
        } else {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("ERROR");
                    alerta.setContentText("Por favor, ingrese todos los datos.");
                    alerta.initStyle(StageStyle.UTILITY);
                    alerta.setHeaderText(null);
                    alerta.showAndWait();
        }
        
        if(table.getItems().size()>0){
            botonComprar.setDisable(false);
        }
        
    }
    
    @FXML
    public void editarProducto(ActionEvent event){
        if(Integer.parseInt(fieldCantidad.getText())>0){
            Iterator it = data.iterator();
            Producto editar = searchItemInTable();
            if(editar!=null){
                editar.setTotalAComprar(Integer.parseInt(fieldCantidad.getText()));
            }           
            buttonReset();
            updatePrices();
            updateTable();
        }
    }
    
    @FXML
    public void eliminarProducto(ActionEvent event){  
        Producto eliminar = searchItemInTable();
            if(eliminar!=null){
                data.remove(eliminar);
            }     
        updatePrices();
        updateTable();
        buttonReset();
        if(table.getItems().size()==0){
            botonComprar.setDisable(true);
        }
    }
    //Botón en el que se genera el ticket :v
    @FXML
    public void comprarProductos(ActionEvent event){
        botonComprar.setDisable(true);
        Iterator it = data.iterator();
        while(it.hasNext()){
            Producto comprar = (Producto) it.next();
            int oldQuantity = comprar.getCantidad();
            int soldQuantity = comprar.getTotalAComprar();
            comprar.setCantidad(oldQuantity-soldQuantity);
            String query = "UPDATE productos SET cantidad='"+comprar.getCantidad()+"' WHERE codigo='"+comprar.getCodigo()+"'";
            Connection conn;
            
            try{
                String url = "jdbc:mysql://localhost:3306/proyectovisual?autoReconnect=false&useSSL=false"; 
                conn = DriverManager.getConnection(url,"root",""); 
                Statement st = conn.createStatement();
                st.executeUpdate(query);
            }catch(Exception ex){
                Alert alerta = new Alert(Alert.AlertType.ERROR);
                            alerta.setTitle("ERROR");
                            alerta.setContentText("Error al conectar con la base de datos. Contacte a un asesor.");
                            alerta.initStyle(StageStyle.UTILITY);
                            alerta.setHeaderText(null);
                            alerta.showAndWait();
            }
            
        }
        
        data.clear();
        table.getItems().clear();
        fieldCantidad.clear();
        fieldCodigo.clear();
        updatePrices();
        updateLabels();
    }
    
    @FXML
    public void cancelarSeleccion(ActionEvent event){
        buttonReset();
        updateTable();
    }
    
    //Para buscar producto :v
    @FXML
    public void buscarProducto(ActionEvent event){
        
    }
    
    
    private void buttonReset(){
        botonEditar.setDisable(true);
        botonEliminar.setDisable(true);
        botonCancelarSeleccion.setDisable(true);
        botonAgregar.setDisable(false);
        fieldCodigo.clear();
        fieldCodigo.setDisable(false);
        fieldCantidad.clear();
    }
    
    private void updateTable(){
        table.getItems().clear();
        table.getItems().addAll(data);
    }
    
    private Producto searchItemInTable(){
        Producto encontrado = null;
        Iterator it = data.iterator();
        while(it.hasNext()){
            Producto buscar =(Producto) it.next();
            if((""+buscar.getCodigo()).equals(fieldCodigo.getText())){
                encontrado = buscar;
            }
        }
        return encontrado;
    }
    
    private void notEnoughStock(Producto x){
        Alert alerta = new Alert(Alert.AlertType.ERROR);
                            alerta.setTitle("ERROR");
                            alerta.setContentText("No hay suficientes productos.\nStock: " + x.getCantidad());
                            alerta.initStyle(StageStyle.UTILITY);
                            alerta.setHeaderText(null);
                            alerta.showAndWait();
    }
    
    private void updatePrices(){
        subTotal = 0;
        total = 0;
        iva=0;
        
        Iterator it = data.iterator();
        while(it.hasNext()){
            Producto aux =(Producto) it.next();
            subTotal+= aux.getTotalAComprar()*aux.getPrecio();
        }
        iva = subTotal*0.16;
        total = subTotal+iva;
        
        updateLabels();
    }
    
    private void updateLabels(){
        subTotalLabel.setText("SUBTOTAL: $" + (""+subTotal));
        ivaLabel.setText("IVA: $" + (""+iva));
        totalLabel.setText("TOTAL: $" + (""+total));
        
    }
}