package gui;

import classification.models.DecisionTreeClassifier;
import classification.models.NaiveBayesClassifier;
import data.DataSplitter;
import javafx.event.ActionEvent;

import java.util.Objects;

public class NaiveBayesPageSetup extends ClassifierPage {
    @Override
    public void train(ActionEvent event) {
        int percentage = 80;
        if (trainSlider.getValue() != 0) {
            percentage = (int) trainSlider.getValue();
        }

        if (Objects.equals(fileStatusLabel.getText(), "✔")) {
            dataSplitter = new DataSplitter(percentage, selectedFile.getPath());

            naiveBayesClassifier = new NaiveBayesClassifier();
            naiveBayesClassifier.trainModel(dataSplitter.getListOfTrainingInstances());
            naiveBayesClassifier.validate(dataSplitter.getListOfValidationInstances());
            testButton.setDisable(false);
        }
    }

    @Override
    public void test(ActionEvent event) {
        naiveBayesClassifier.test(dataSplitter.getListOfTestingInstances());
        predictions = naiveBayesClassifier.getTestPredictions();

        setMetrics(dataSplitter, predictions);
    }
}
