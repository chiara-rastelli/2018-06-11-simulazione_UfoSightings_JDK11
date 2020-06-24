package it.polito.tdp.ufo;


import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.ufo.model.AnnoAvvistamenti;
import it.polito.tdp.ufo.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private Button btnAnalizza;

    @FXML
    private Button btnSequenzaAvvistamenti;

    @FXML
    private ComboBox<AnnoAvvistamenti> boxAnno;

    @FXML
    private ComboBox<String> boxStato;

    @FXML
    private TextArea txtResult;

    @FXML
    void handleAnalizza(ActionEvent event) {
    	this.txtResult.clear();
    	if (this.boxStato.getValue() == null) {
    		this.txtResult.setText("Devi scegliere uno stato!\n");
    		return;
    	}
    	String stato = this.boxStato.getValue();
    	this.txtResult.setText("Analisi per lo stato "+stato+":\n");
    	this.txtResult.appendText("Stati precedenti nel grafo: \n");
    	this.txtResult.appendText(this.model.getPrecedenti(stato).toString()+"\n");
    	this.txtResult.appendText("Stati successivi nel grafo: \n");
    	this.txtResult.appendText(this.model.getSuccessivi(stato).toString()+"\n");
    	this.txtResult.appendText("Stati raggiungibili: \n");
    	this.txtResult.appendText(this.model.getStatiRaggiungibili(stato).toString());
    }

    @FXML
    void handleAvvistamenti(ActionEvent event) {
    	this.txtResult.clear();
    	if (this.boxAnno.getValue() == null) {
    		this.txtResult.setText("Devi prima selezionare un anno!\n");
    		return;
    	}
    	this.model.creaGrafo(this.boxAnno.getValue().getAnno());
    	this.boxStato.getItems().addAll(this.model.getVertici());
    	this.btnAnalizza.setDisable(false);
    	this.btnSequenzaAvvistamenti.setDisable(false);
    }

    @FXML
    void handleSequenza(ActionEvent event) {
    	this.txtResult.setText("Cammino piu' lungo:\n");;
    	for(String s : this.model.getCamminoPiuLungo(this.boxStato.getValue()))
    		this.txtResult.appendText(s+"\n");
    }

    @FXML
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert boxStato != null : "fx:id=\"boxStato\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Ufo.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSequenzaAvvistamenti != null : "fx:id=\"btnSequenzaAvvistamenti\" was not injected: check your FXML file 'Scene.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxAnno.getItems().addAll(this.model.getAllAvvistamentiAnno());
		this.btnAnalizza.setDisable(true);
		this.btnSequenzaAvvistamenti.setDisable(true);
	}
}
