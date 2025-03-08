package gui;

import classification.models.LogisticRegression;
import data.DataSplitter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Objects;

public class LogisticRegressionPageSetup extends ClassifierPage{
    @Override
    public void train(ActionEvent event) {
        int percentage = 80;
        if (trainSlider.getValue() != 0) {
            percentage = (int) trainSlider.getValue();
        }

        if (Objects.equals(fileStatusLabel.getText(),"✔")){
            dataSplitter = new DataSplitter(percentage, selectedFile.getAbsolutePath());
            double learningRate = 0;
            if (!learningRateTextField.getText().isEmpty()) {
                learningRate = Double.parseDouble(learningRateTextField.getText());
            }

            int epochs = 0;
            if (!epochsTextField.getText().isEmpty()) {
                epochs = Integer.parseInt(epochsTextField.getText());
            }

            logisticRegressionClassifier = new LogisticRegression(learningRate, epochs);
            logisticRegressionClassifier.trainModel(dataSplitter.getListOfTrainingInstances());
            logisticRegressionClassifier.validate(dataSplitter.getListOfValidationInstances());
            testButton.setDisable(false);
        }
    }

    public void defaultLearning(ActionEvent event) {
        learningRateTextField.setText(String.valueOf(0.01));
        epochsTextField.setText(String.valueOf(50));
    }
    @Override
    public void test(ActionEvent event) {
        logisticRegressionClassifier.test(dataSplitter.getListOfTestingInstances());
        predictions = logisticRegressionClassifier.getTestPredictions();

        setMetrics(dataSplitter, predictions);
    }

    @FXML
    private TextField learningRateTextField, epochsTextField;

    @FXML
    private Button defaultLearningRateButton;
}

//package gui;
//
//import classification.models.LogisticRegression;
//import data.DataSplitter;
//import javafx.event.ActionEvent;
//import javafx.fxml.FXML;
//import javafx.scene.control.Button;
//import javafx.scene.control.TextField;
//
//import java.util.Objects;
//
//public class LogisticRegressionPageSetup extends ClassifierPage {
//
//    @Override
//    public void train(ActionEvent event) {
//        int percentage = 80;
//        if (trainSlider.getValue() != 0) {
//            percentage = (int) trainSlider.getValue();
//        }
//
//        if (Objects.equals(fileStatusLabel.getText(), "✔")) {
//            dataSplitter = new DataSplitter(percentage, selectedFile.getAbsolutePath());
//
//            double learningRate = 0.01; // Default learning rate
//            if (!learningRateTextField.getText().isEmpty()) {
//                learningRate = Double.parseDouble(learningRateTextField.getText());
//            }
//
//            int epochs = 50; // Default number of epochs
//            if (!epochsTextField.getText().isEmpty()) {
//                epochs = Integer.parseInt(epochsTextField.getText());
//            }
//
//            logisticRegressionClassifier = new LogisticRegression(learningRate, epochs);
//
//            // Enable debugging options
//            logisticRegressionClassifier.setDebugMode(true); // Enable general debug mode
//            logisticRegressionClassifier.setPrintGradients(true); // Enable gradient debugging (optional)
//
//            logisticRegressionClassifier.trainModel(dataSplitter.getListOfTrainingInstances());
//
//            logisticRegressionClassifier.validate(dataSplitter.getListOfValidationInstances());
//
//            testButton.setDisable(false);
//        }
//    }
//
//    public void defaultLearning(ActionEvent event) {
//        learningRateTextField.setText(String.valueOf(0.01)); // Default learning rate
//        epochsTextField.setText(String.valueOf(50)); // Default epochs
//    }
//
//    @Override
//    public void test(ActionEvent event) {
//        // Test the model using the testing data
//        logisticRegressionClassifier.test(dataSplitter.getListOfTestingInstances());
//        predictions = logisticRegressionClassifier.getTestPredictions();
//
//        // Update metrics in the UI
//        setMetrics(dataSplitter, predictions);
//    }
//
//    @FXML
//    private TextField learningRateTextField, epochsTextField;
//
//    @FXML
//    private Button defaultLearningRateButton;
//}
