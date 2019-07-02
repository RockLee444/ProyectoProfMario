package application;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeException;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

public class SampleController {
	@FXML
	private TextField txtGen;
	
	private Barcode codigo = null;
	private String codigoSTR="";
	
	public void generarCodigo(){
		codigoSTR="";
		Random r = new Random();
		for (int i = 0; i < 9; i++) {
			codigoSTR=codigoSTR+Math.abs((r.nextInt()%9));
		}
		   try {
		        codigo = BarcodeFactory.createCode39(codigoSTR, true);//Reemplazar esto por el valor que deseen
		    } catch (BarcodeException e) {
		    }
		    codigo.setDrawingText(true);//determina si se agrega o no el número codificado debajo del código de barras
		    //tamaño de la barra
		       codigo.setBarWidth(2);
		       codigo.setBarHeight(60);
		  
		    try {
		    //Ruta y nombre del archivo PNG a crear
		    String strFileName= "src/"+codigoSTR+".PNG";
		          File file = new File(strFileName);
		          FileOutputStream fos = new FileOutputStream(file);
		             BarcodeImageHandler.writePNG(codigo, fos);//formato de ejemplo PNG
		             System.out.println("Archivo creado: "+strFileName);
		    } catch (Exception ex) {
		     System.out.println("Error: "+ ex.getMessage());
		    }
	}
}
