/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.BilancioAlbum;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdiacenze"
    private Button btnAdiacenze; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA2"
    private ComboBox<Album> cmbA2; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doCalcolaAdiacenze(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	Album a = this.cmbA1.getValue();
    	
    	if(a == null) {
    		this.txtResult.setText("Selezionare un album per continuare.");
    		return;
    	}
    	
    	List<BilancioAlbum> adiacenze = this.model.getAdiacenti(a);
    	
    	this.txtResult.appendText("Successori di "+a+" in ordine desc di bilancio:\n");
    	
    	for(BilancioAlbum ba : adiacenze) {
    		
    		this.txtResult.appendText("\n"+ba.getA()+", bilancio = "+ba.getBilancio());
    	}
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	Integer X = 0;
    	
    	try {
    		X = Integer.parseInt(this.txtX.getText());
    		
    		if(X<=0){
    			this.txtResult.setText("Inserire un numero positivo.");
    			return;
    		}
    	}catch(NumberFormatException e) {
    		txtResult.setText("Formato non corretto per il numero di recensioni.");
    		return;
    	}
    	
    	Album partenza = this.cmbA1.getValue();
    	Album arrivo = this.cmbA2.getValue();
    	
    	if(partenza.equals(arrivo)) {
    		this.txtResult.setText("Selezionare due album diversi per continuare.");
    		return;
    	}
    	
    	List<Album> percorso = this.model.getPercorso(partenza, arrivo, X);
    	
    	this.txtResult.appendText("Percorso da "+partenza+" a " + arrivo);
    	
    	for(Album a : percorso) {
    		this.txtResult.appendText("\n"+a);
    	}
    	
    	
    	
    	
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	int N = 0;
    	
    	try {
    		N = Integer.parseInt(this.txtN.getText());
    		
    		if(N<=0){
    			this.txtResult.setText("Inserire un numero positivo.");
    			return;
    		}
    	}catch(NumberFormatException e) {
    		txtResult.setText("Formato non corretto per il numero di recensioni.");
    		return;
    	}
    	
    	this.model.creaGrafo(N);
    	this.txtResult.setText(this.model.infoGrafo());
    	this.btnAdiacenze.setDisable(false);
    	this.btnPercorso.setDisable(false);
    	
    	List<Album> albums = this.model.getVertici(N);
    	Collections.sort(albums);
    	this.cmbA1.getItems().addAll(albums);
    	
    	
    	List<Album> albums2 = this.model.getVertici(N);
    	Collections.sort(albums2);
    	this.cmbA2.getItems().addAll(albums2);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdiacenze != null : "fx:id=\"btnAdiacenze\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA2 != null : "fx:id=\"cmbA2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }

    
    public void setModel(Model model) {
    	this.model = model;
    	this.btnAdiacenze.setDisable(true);
    	this.btnPercorso.setDisable(true);
    }
}
