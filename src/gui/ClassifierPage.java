package gui;

import classification.models.*;
import data.DataSplitter;
import evaluation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public abstract class ClassifierPage {
    AppController appController;
    DataSplitter dataSplitter;
    KNNClassifier knnClassifier;
    NaiveBayesClassifier naiveBayesClassifier;
    PerceptronClassifier perceptronClassifier;
    DecisionTreeClassifier decisionTreeClassifier;
    LogisticRegression logisticRegressionClassifier;
    List<Integer> predictions;

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void initialize() {
        testButton.setDisable(true);

        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 80);
        trainSpinner.setValueFactory(valueFactory);

        trainSlider.setValue(80);
        trainSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            trainSpinner.getValueFactory().setValue(newVal.intValue());
        });

        trainSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            trainSlider.setValue(newVal);
        });
    }

    public void chooseFile(ActionEvent event) {
        System.out.println("Choose file");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open File");
        selectedFile = fileChooser.showOpenDialog(null);
        System.out.println("Selected file: " + selectedFile);
        if (selectedFile != null ) {
            fileStatusLabel.setText("✔");
            fileStatusLabel.setStyle("-fx-font-size: 18; -fx-text-fill: green;");
            String filename = selectedFile.getAbsolutePath();
            System.out.println("File: " + filename);
        } else {
            fileStatusLabel.setText("❌");
            fileStatusLabel.setStyle("-fx-font-size: 18; -fx-text-fill: red;");
        }
    }


    public abstract void train(ActionEvent event);
    public abstract void test(ActionEvent event);

    public void setMetrics(DataSplitter dataSplitter, List<Integer> predictions) {
        Accuracy accuracy = new Accuracy();
        double accuracyValue = accuracy.evaluate(dataSplitter.getListOfTestingInstances(), predictions);
        appController.accuracyLabel.setText(String.format("%.4f", accuracyValue));


        Precision precision = new Precision();
        double precisionValue = precision.evaluate(dataSplitter.getListOfTestingInstances(), predictions);
        appController.precisionLabel.setText(String.format("%.4f", precisionValue));

        Recall recall = new Recall();
        double recallValue = recall.evaluate(dataSplitter.getListOfTestingInstances(), predictions);
        appController.recallLabel.setText(String.format("%.4f", recallValue));

        F1Score f1Score = new F1Score();
        double f1Value = f1Score.evaluate(dataSplitter.getListOfTestingInstances(), predictions);
        appController.f1Score.setText(String.format("%.4f", f1Value));

        ConfusionMatrix confusionMatrix = new ConfusionMatrix();
        int[][] matrix = confusionMatrix.computeMatrix(dataSplitter.getListOfTestingInstances(), predictions);
        appController.truePositivesLabel.setText(String.valueOf(matrix[0][0]));
        appController.falseNegativeLabel.setText(String.valueOf(matrix[0][1]));
        appController.falsePositivesLabel.setText(String.valueOf(matrix[1][0]));
        appController.trueNegativeLabel.setText(String.valueOf(matrix[1][1]));
    }

    @FXML
    protected Button chooseFile, testButton, trainButton;

    @FXML
    protected Label fileStatusLabel;

    @FXML
    protected Slider trainSlider;

    @FXML
    protected Spinner<Integer> trainSpinner;

    File selectedFile;

}
