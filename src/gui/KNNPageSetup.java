package gui;

import classification.models.KNNClassifier;
import data.DataSplitter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import utils.KnnUtils.Distance;

import java.util.Objects;

public class KNNPageSetup  extends ClassifierPage {

    @Override
    public void train(ActionEvent event) {
        System.out.println("Start training");
        int percentage = 80;
        if (trainSlider.getValue() != 0) {
            percentage = (int) trainSlider.getValue();
        }
        System.out.println("Percentage: " + percentage);

        if (Objects.equals(fileStatusLabel.getText(), "✔")) {
            dataSplitter = new DataSplitter(percentage, selectedFile.getPath());
            System.out.println("DataSplitter: " + dataSplitter);


            int k = 0;
            if (!kTextField.getText().isEmpty()) {
                k = Integer.parseInt(kTextField.getText());
            }

            Distance.DistanceType distance = null;

            if (euclideanRadio.isSelected()) {
                distance = Distance.DistanceType.EUCLIDEAN;
                System.out.println("Distance chosen by user: EUCLIDEAN");
            } else if (manhattanRadio.isSelected()) {
                distance = Distance.DistanceType.MANHATTAN;
                System.out.println("Distance chosen by user: MANHATTAN");
            } else {
                // If no radio button is selected, distance remains null (or handle a default).
                System.out.println("No distance radio selected. Using null or handle default here.");
            }
            System.out.println("Distance name" );

            knnClassifier = new KNNClassifier(k, distance);
            knnClassifier.trainModel(dataSplitter.getListOfTrainingInstances());
            knnClassifier.validate(dataSplitter.getListOfValidationInstances());
            testButton.setDisable(false);
        }
    }

    public void defaultNeighbors(ActionEvent event) {
        kTextField.setText(String.valueOf((int) Math.sqrt((double) trainSlider.getValue() / 100 * 768)));
        System.out.println("Default neighbors"+ kTextField);
    }

    @Override
    public void test(ActionEvent event) {
        knnClassifier.test(dataSplitter.getListOfTestingInstances());
        predictions = knnClassifier.getTestPredictions();

        setMetrics(dataSplitter, predictions);
    }

    @FXML
    private TextField kTextField;

    @FXML
    private Button defaultKButton;

    @FXML
   private ToggleGroup distanceToggle;

    @FXML
    private RadioButton euclideanRadio, manhattanRadio;




}
