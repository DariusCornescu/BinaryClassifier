package gui;

import classification.models.PerceptronClassifier;
import data.DataSplitter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Objects;

public class PerceptronPageSetup extends ClassifierPage {
    @Override
    public void train(ActionEvent event) {
        int percentage = 80;
        if (trainSlider.getValue() != 0) {
            percentage = (int) trainSlider.getValue();
        }

        if (Objects.equals(fileStatusLabel.getText(), "✔")) {
            dataSplitter = new DataSplitter(percentage, selectedFile.getAbsolutePath());
        }

        // Retrieve learning rate and epochs from the UI
        double learningRate = 0.01; // Default learning rate
        if (!learningRateTextField.getText().isEmpty()) {
            learningRate = Double.parseDouble(learningRateTextField.getText());
        }

        int epochs = 50; // Default number of epochs
        if (!epochsTextField.getText().isEmpty()) {
            epochs = Integer.parseInt(epochsTextField.getText());
        }

        perceptronClassifier = new PerceptronClassifier(learningRate, epochs);
        perceptronClassifier.trainModel(dataSplitter.getListOfTrainingInstances());
        perceptronClassifier.validate(dataSplitter.getListOfValidationInstances());
        testButton.setDisable(false);
    }

    public void defaultLearning(ActionEvent event) {
        learningRateTextField.setText(String.valueOf(0.001));
        epochsTextField.setText(String.valueOf(250));
    }

    @Override
    public void test(ActionEvent event) {
        perceptronClassifier.test(dataSplitter.getListOfTestingInstances());
        predictions = perceptronClassifier.getTestPredictions();
        setMetrics(dataSplitter, predictions);

    }

        @FXML
        private TextField learningRateTextField, epochsTextField;

        @FXML
        private Button defaultLearningRateButton;
}
