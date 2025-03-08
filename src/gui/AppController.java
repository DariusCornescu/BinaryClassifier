package gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class AppController {
     public void initialize() {
         Tab knnTab = createTab("KNN", "KNNPageSetup.fxml");
         Tab decisionTreeTab = createTab("Decision Tree", "DecisionTreePageSetup.fxml");
         Tab perceptronTab = createTab("Perceptron", "PerceptronPageSetup.fxml");
         Tab naiveBayesTab = createTab("Naive Bayes", "NaiveBayesPageSetup.fxml");
         Tab logisticRegressionTab = createTab("Logistic Regression", "LogisticRegressionPageSetup.fxml");
         logisticRegressionTab.setStyle("-fx-opacity: 0.5");

         tabPane.getTabs().addAll(knnTab, decisionTreeTab, perceptronTab, naiveBayesTab, logisticRegressionTab);

         tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
             if (newTab != null && newTab.getText().equals("Logistic Regression")){
                 tabPane.getSelectionModel().select(oldTab);
                 showFunctionalityInProgressAlert();
             }
         });
     }

    private void showFunctionalityInProgressAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Feature Not Available");
        alert.setHeaderText("Functionality In Progress");
        alert.setContentText("The Logistic Regression classifier is currently under development.\n\n" +
                "Expected features:\n" +
                "• Binary classification\n" +
                "• Model training and evaluation\n" +
                "• Performance metrics\n\n" +
                "Please check back later for updates!");

        // Get the DialogPane
        DialogPane dialogPane = alert.getDialogPane();

        // Load the CSS file
        String css = getClass().getResource("style.css").toExternalForm();
        dialogPane.getStylesheets().add(css);
        alert.showAndWait();
    }

    private Tab createTab(String title, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Node content = loader.load();

            Object controller = loader.getController();
            if (controller instanceof ClassifierPage) {
                ((ClassifierPage) controller).setAppController(this);
            }
            return new Tab(title, content);
        } catch (Exception e) {
            e.printStackTrace();
            return new Tab(title + " (Error)");
        }
    }


    @FXML
    private AnchorPane rootPane;

    @FXML
    protected TabPane tabPane;

    @FXML
   protected Label accuracyLabel, f1Score, precisionLabel, recallLabel, falseNegativeLabel, falsePositivesLabel, trueNegativeLabel, truePositivesLabel;


}