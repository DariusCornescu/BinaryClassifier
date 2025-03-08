package gui;

import classification.models.DecisionTreeClassifier;
import data.DataSplitter;
import javafx.event.ActionEvent;

import java.util.Objects;

public class DecisionTreePageSetup extends ClassifierPage{
    @Override
    public void train(ActionEvent event) {
        int percentage = 80;
        if (trainSlider.getValue() != 0) {
            percentage = (int) trainSlider.getValue();
        }

        if (Objects.equals(fileStatusLabel.getText(), "✔")) {
            dataSplitter = new DataSplitter(percentage, selectedFile.getPath());

            decisionTreeClassifier = new DecisionTreeClassifier();
            decisionTreeClassifier.trainModel(dataSplitter.getListOfTrainingInstances());
            decisionTreeClassifier.validate(dataSplitter.getListOfValidationInstances());
            testButton.setDisable(false);
        }

    }

    @Override
    public void test(ActionEvent event) {
        decisionTreeClassifier.test(dataSplitter.getListOfTestingInstances());
        predictions = decisionTreeClassifier.getTestPredictions();

        setMetrics(dataSplitter, predictions);
    }
}
