package registros;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import Connector.Connector;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.io.FileOutputStream;
import java.awt.Color;
import java.io.FileNotFoundException;

import com.itextpdf.text.*;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class SampleController implements Initializable {
	public SampleController() {
		//ventana.setIconImage(new ImageIcon(getClass().getResource("../Imagenes/logoz.png")).getImage());
		
	}
	
	static  Connection conn = Connector.getConnection();
	String valq = "0";
	@FXML
	private AnchorPane ventana;
	 @FXML
	    private TableView<Person> table;

	    @FXML
	    private TableColumn<?, ?> columna0;

	    @FXML
	    private TableColumn<?, ?> columna1;

	    @FXML
	    private TableColumn<?, ?> columna2;

	    @FXML
	    private TableColumn<?, ?> columna3;

	    @FXML
	    private TableColumn<?, ?> columna4;

	    @FXML
	    private TableColumn<?, ?> columna5;
	    @FXML
	    private TableColumn<?, ?> columna10;

	    @FXML
	    private TableColumn<?, ?> columna11;
	@FXML
	private Button filtar,GenerarPdf;
	

	
	@FXML 
	private TextField cajaDia,cajaMes,cajaAño,cajaCompra,cajaCodigo;


	
	
	
	public void showBooks() {
		ObservableList<Person> personList = FXCollections.observableArrayList();
		
		String query = "SELECT * FROM registros";
		
		try {
			

			Statement st;
			ResultSet rs;
			
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			Person person=null;
			int a=0;
			while(rs.next()) {
				person = new Person(rs.getString("compra_id"),rs.getString("codigo"),rs.getString("productos"),rs.getString("cantidad"), rs.getString("precio"), rs.getString("Dia"),rs.getString("Mes"),rs.getString("Años"));
				personList.add(person);
				
			}
			table.getItems().setAll(this.getPersonList());
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//return personList;
	}
	@FXML
	public void Filtrar() {
		
		try {
			ObservableList<Person> personList = FXCollections.observableArrayList();
			String query = "SELECT * FROM registros";
			boolean dias=false,meses=false,años=false,codigos=false;
			String dia="",mes="",año="";
			if(cajaDia.getText().equals("")) {
				
			}else {
				query = query+" where Dia='"+cajaDia.getText()+"'";
				dias=true;
			}
			
			if(cajaMes.getText().equals("")) {
				
				
			}else {
				if(dias) {
					query = query+" and Mes="+cajaMes.getText();
				}else {
					query = query+" where Mes="+cajaMes.getText();
				}
				meses=true;
			}
		
			if(cajaAño.getText().equals("")) {
				
			}else {
				if(dias || meses) {
					query = query+" and Años="+cajaAño.getText();
				}else {
					query =query+" where Años="+cajaAño.getText();
				}
			}
			
			
			if(cajaCodigo.getText().equals("")) {
				
			}else {
				if(dias || meses || años  ) {
					query = query+" and codigo="+cajaCodigo.getText();
				}else {
					query = query+" where codigo="+cajaCodigo.getText(); 
				}
				codigos=true;
			}
		
			
			if(cajaCompra.getText().equals("")) {
				
			}else {
				if(dias || meses || años || codigos) {
					query = query+" and compra_id="+cajaCompra.getText();
				}else {
					query = query+" where compra_id="+cajaCompra.getText(); 
				}
				
			}
			
			Statement st;
			ResultSet rs;
			
			st = conn.createStatement();
			rs = st.executeQuery(query);
			
			Person person=null;
		
			while(rs.next()) {
				person = new Person(rs.getString("compra_id"),rs.getString("codigo"),rs.getString("productos"),rs.getString("cantidad"), rs.getString("precio"), rs.getString("Dia"),rs.getString("Mes"),rs.getString("Años"));
				personList.add(person);
				
			}
			table.getItems().setAll(personList);
		
		}catch(Exception e) {
		
			e.printStackTrace();
	
		}
	}
	
	public ObservableList<Person> getPersonList() {
		ObservableList<Person> personList = FXCollections.observableArrayList();

		
		String query = "SELECT * FROM registros";
		
		Statement st;
		ResultSet rs;
		
		try {
			st = Connector.getConnection().createStatement();
			rs = st.executeQuery(query);
			
			Person personas;
			while(rs.next()) {
				personas = new Person(rs.getString("compra_id"),rs.getString("codigo"),rs.getString("productos"),rs.getString("cantidad"), rs.getString("precio"), rs.getString("Dia"),rs.getString("Mes"),rs.getString("Años"));
				personList.add(personas);
			}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return personList;
		
	}
	/*
	@FXML 
	private void updateButton() {
	ObservableList<Person> data, pers;
		data = table.getItems();
		pers = table.getSelectionModel().getSelectedItems();
		
	String query = "UPDATE usuarios SET nombre='"+primerNombre.getText()+"',last_name='"+segundoNombre.getText()+"',email='"+email.getText()+" WHERE ID="+String.valueOf(pers.get(0).getId());
	executeQuery(query);
		primerNombre.setText("");
		segundoNombre.setText("");
		email.setText("");
	showBooks();
	} */
	
	public void executeQuery(String query) {
		
		Statement st;
		try {
		st = conn.createStatement();
		st.executeUpdate(query);
		} catch (Exception e) {
		e.printStackTrace();
		}
		}

	
	

	
	 @FXML
		public void generatePDF(ActionEvent event) {
			  try
			    {
				  int tamaño=table.getItems().size();
				  
				    Document document = new Document();

		    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("./AddTableExample.pdf"));
	        document.open();
	 
	        PdfPTable tabla = new PdfPTable(6); 
	        tabla.setWidthPercentage(100); //Width 100%
	        tabla.setSpacingBefore(10f); //Space before table
	        tabla.setSpacingAfter(10f); //Space after table
	        
	        
	        //Set Column widths
	       
	        float[] columnWidths = {1f, 1f, 1f, 1f,1f,1f}; 
	        tabla.setWidths(columnWidths);
	        
	        PdfPCell cell1 = new PdfPCell(new Paragraph("producto"));
	        cell1.setBorderColor(BaseColor.RED);
	        cell1.setPaddingLeft(10);
	        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
	 
	        PdfPCell cell2 = new PdfPCell(new Paragraph("cantidad"));
	        cell2.setBorderColor(BaseColor.RED);
	        cell2.setPaddingLeft(10);
	        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
	 
	        PdfPCell cell3 = new PdfPCell(new Paragraph("precio"));
	        cell3.setBorderColor(BaseColor.RED);
	        cell3.setPaddingLeft(10);
	        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
	 
	        PdfPCell cell4 = new PdfPCell(new Paragraph("dia"));
	        cell4.setBorderColor(BaseColor.RED);
	        cell4.setPaddingLeft(10);
	        cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        

	        PdfPCell cell5 = new PdfPCell(new Paragraph("mes"));
	        cell5.setBorderColor(BaseColor.RED);
	        cell5.setPaddingLeft(10);
	        cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
	 
	        PdfPCell cell6 = new PdfPCell(new Paragraph("año"));
	        cell6.setBorderColor(BaseColor.RED);
	        cell6.setPaddingLeft(10);
	        cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        
	        tabla.addCell(cell1);
	        tabla.addCell(cell2);
	        tabla.addCell(cell3);
	        tabla.addCell(cell4);
	        tabla.addCell(cell5);
	        tabla.addCell(cell6);
	        
	 for(int i=0;i<tamaño;i++) {
	        PdfPCell cellx = new PdfPCell(new Paragraph(table.getItems().get(i).getProducto()));
	        cellx.setBorderColor(BaseColor.PINK);
	        cellx.setPaddingLeft(10);
	        cellx.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cellx.setVerticalAlignment(Element.ALIGN_MIDDLE);
	 
	        PdfPCell celly = new PdfPCell(new Paragraph(table.getItems().get(i).getCantidad()));
	        celly.setBorderColor(BaseColor.PINK);
	        celly.setPaddingLeft(10);
	        celly.setHorizontalAlignment(Element.ALIGN_CENTER);
	        celly.setVerticalAlignment(Element.ALIGN_MIDDLE);
	 
	        PdfPCell cellz = new PdfPCell(new Paragraph(table.getItems().get(i).getPrecio()));
	        cellz.setBorderColor(BaseColor.PINK);
	        cellz.setPaddingLeft(10);
	        cellz.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cellz.setVerticalAlignment(Element.ALIGN_MIDDLE);
	 
	        PdfPCell cellxyz = new PdfPCell(new Paragraph(table.getItems().get(i).getDia()));
	        cellxyz.setBorderColor(BaseColor.PINK);
	        cellxyz.setPaddingLeft(10);
	        cellxyz.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cellxyz.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        
	        PdfPCell cellzz = new PdfPCell(new Paragraph(table.getItems().get(i).getMes()));
	        cellzz.setBorderColor(BaseColor.PINK);
	        cellzz.setPaddingLeft(10);
	        cellzz.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cellzz.setVerticalAlignment(Element.ALIGN_MIDDLE);
	 
	        PdfPCell cellxx = new PdfPCell(new Paragraph(table.getItems().get(i).getAño()));
	        cellxx.setBorderColor(BaseColor.PINK);
	        cellxx.setPaddingLeft(10);
	        cellxx.setHorizontalAlignment(Element.ALIGN_CENTER);
	        cellxx.setVerticalAlignment(Element.ALIGN_MIDDLE);
	        
	        tabla.addCell(cellx);
	        tabla.addCell(celly);
	        tabla.addCell(cellz);
	        tabla.addCell(cellxyz);
	        tabla.addCell(cellzz);
	        tabla.addCell(cellxx);
			    }
	        document.add(tabla);
	        
	        document.close();
	        writer.close();
	        JOptionPane.showMessageDialog(null, this,"guardando los datos", 0);
			} catch (DocumentException e) {
		    	e.printStackTrace();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    }
		}
	

		
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		columna10.setMinWidth(100);
		columna11.setMinWidth(100);
		columna0.setMinWidth(100);
		columna1.setMinWidth(100);
		columna2.setMinWidth(100);
		columna3.setMinWidth(50);
		columna4.setMinWidth(50);
		columna5.setMinWidth(50);
		
		showBooks();
	}
	
	
	
	
	
	public static class Person {
		private final SimpleStringProperty codigo;
		private final SimpleStringProperty compra;
		private final SimpleStringProperty producto;
		private final SimpleStringProperty cantidad;
		private final SimpleStringProperty precio;
		private final SimpleStringProperty dia;
		private final SimpleStringProperty mes;
		private final SimpleStringProperty año;
		
		
		private Person(String cod,String comp,String produc, String canti, String preci, String di, String me, String añ) {
			this.producto = new SimpleStringProperty(produc); //Aquí se modifica, requiere variable
			this.cantidad = new SimpleStringProperty(canti);
			this.precio = new SimpleStringProperty(preci);
			this.dia = new SimpleStringProperty(di);
			this.mes = new SimpleStringProperty(me);
			this.año = new SimpleStringProperty(añ);
			this.codigo=new SimpleStringProperty(cod);
			this.compra=new SimpleStringProperty(comp);
		}


		public String getCodigo() {
			return codigo.get();
		}


		public String getCompra() {
			return compra.get();
		}


		public String getProducto() {
			return producto.get();
		}


		public String getCantidad() {
			return cantidad.get();
		}


		public void setPrecio(String dato) {
			precio.set(dato);
		}


		public void setDia(String dato) {
			dia.set(dato);
		}


		public void getMes(String dato) {
			mes.set(dato);
		}


		public void setAño(String dato) {
			año.set(dato);
		}
		
		
		public void setCodigo(String dato) {
			codigo.set(dato);
		}


		public void setCompra(String dato) {
			compra.set(dato);
		}


		public void setProducto(String dato) {
			producto.set(dato);
		}


		public void setCantidad(String dato) {
			cantidad.set(dato);
		}


		public String getPrecio() {
			return precio.get();
		}


		public String getDia() {
			return dia.get();
		}


		public String getMes() {
			return mes.get();
		}


		public String getAño() {
			return año.get();
		}
	}
	
	
	 
}
