//package classification.models;
//
//import classification.Instance;
//import classification.Model;
//import evaluation.Accuracy;
//
//import java.util.*;
//
//public class LogisticRegression implements Model<Number, Integer> {
//    protected double[] weights;
//    protected double bias;
//    protected int epochs = 0;
//    protected double learningRate = 0;
//    protected List<Integer> testPredictions;
//    protected List<Instance<Number, Integer>> trainInstances;
//
//    public LogisticRegression(double learningRate, int epochs) {
//        this.learningRate = learningRate;
//        this.epochs = epochs;
//    }
//
//    public LogisticRegression() {
//        this.learningRate = 0;
//        this.epochs = 0;
//    }
//
//    @Override
//    public void trainModel(List<Instance<Number, Integer>> instances) {
//        this.trainInstances = instances;
//        initializeWeights(8);
//
//        for (int epoch = 0; epoch < epochs; epoch++) {
//            Collections.shuffle(instances, new Random());
//            double[] derivativeWeights = new double[weights.length];
//            double derivativeBias = 0.0;
//            int N = instances.size();
//
//            for (Instance<Number, Integer> instance : instances) {
//                double output = linearCombination(instance.getInput());
//                double sig = sigmoid(output);
//                double error = (sig - instance.getOutput());
//
//                for (int i = 0; i < 8; i++) {
//                    derivativeWeights[i] += error * instance.getInput().get(i).doubleValue();
//                }
//                derivativeBias += error;
//            }
//
//            for (int i = 0; i < 8; i++) {
//                weights[i] -= learningRate * (derivativeWeights[i] / N);
//            }
//            bias -= learningRate * (derivativeBias / N);
//        }
//    }
//
//    private void initializeWeights(int size) {
//        weights = new double[size];
//        Arrays.fill(weights, 0);
//        bias = 0.0;
//    }
//
//    private double linearCombination(List<Number> input) {
//        double sum = bias;
//        for (int i = 0; i < weights.length; i++) {
//            sum += weights[i] * input.get(i).doubleValue();
//        }
//        return sum;
//    }
//
//    private double sigmoid(double f) {
//        return 1.0 / (1.0 + Math.exp(-f));
//    }
//
//    @Override
//    public void validate(List<Instance<Number, Integer>> instances) {
//        if ( learningRate == 0 && epochs == 0 ){
//            tuneHyperparameters(instances,true,true);
//        }else if (learningRate == 0){
//            tuneHyperparameters(instances,true,false);
//        }else if (epochs == 0){
//            tuneHyperparameters(instances,false,true);
//        }
//    }
//
//    private void tuneHyperparameters(List<Instance<Number, Integer>> validationSet, boolean tuneLearningRate, boolean tuneEpochs){
//        List<Double> learningRates = new ArrayList<>(Arrays.asList(0.001, 0.01, 0.1));
//        List<Integer> epochValues = new ArrayList<>(Arrays.asList(100, 200, 300, 1000));
//
//        double bestAccuracy = -1.0;
//        double bestLearningRate = (learningRate != 0) ? learningRate : 0.001;
//        int bestEpochs = (epochs != 0) ? epochs : 150;
//
//        List<Double> learningRatesToUse = tuneLearningRate ? learningRates : List.of(learningRate);
//        List<Integer> epochsToUse = tuneEpochs ? epochValues : List.of(epochs);
//
//        for( int epoch : epochsToUse ){
//            for ( double learningRate : learningRatesToUse ){
//                this.learningRate = learningRate;
//                this.epochs = epoch;
//                initializeWeights(trainInstances.get(0).getInput().size());
//                trainModel(this.trainInstances);
//                List<Integer> predictions = predict(validationSet);
//                double accuracy = new Accuracy().evaluate(validationSet, predictions);
//
//                if (accuracy > bestAccuracy){
//                    bestAccuracy = accuracy;
//                    bestLearningRate = learningRate;
//                    bestEpochs = epoch;
//                }
//            }
//        }
//
//        this.learningRate = bestLearningRate;
//        this.epochs = bestEpochs;
//        initializeWeights(trainInstances.get(0).getInput().size());
//        trainModel(this.trainInstances);
//    }
//
//    private List<Integer> predict(List<Instance<Number, Integer>> data) {
//        List<Integer> predictions = new ArrayList<>();
//        for (Instance<Number, Integer> instance : data) {
//            double output = linearCombination(instance.getInput());
//            double sigmoidFunction = sigmoid(output);
//            int label = sigmoidFunction >= 0.5 ? 1 : 0;
//            predictions.add(label);
//        }
//        return predictions;
//    }
//
//    @Override
//    public void test(List<Instance<Number, Integer>> instances) { testPredictions = predict(instances); }
//
//    public List<Integer> getTestPredictions() { return testPredictions;}
//}

package classification.models;

import classification.Instance;
import classification.Model;
import evaluation.Accuracy;

import java.util.*;

public class LogisticRegression implements Model<Number, Integer> {
    protected double[] weights;
    protected double bias;
    protected int epochs = 0;
    protected double learningRate = 0;
    protected List<Integer> testPredictions;
    protected List<Instance<Number, Integer>> trainInstances;

    // Debug flags
    private boolean debugMode = false;  // toggle for general debug prints
    private boolean printGradients = false; // toggle for gradient prints

    public LogisticRegression(double learningRate, int epochs) {
        this.learningRate = learningRate;
        this.epochs = epochs;
    }

    public LogisticRegression() {
        this.learningRate = 0;
        this.epochs = 0;
    }


    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
    public void setPrintGradients(boolean printGradients) {
        this.printGradients = printGradients;
    }

    @Override
    public void trainModel(List<Instance<Number, Integer>> instances) {
        this.trainInstances = instances;

        if (debugMode) {
            printLabelDistribution("TRAIN", instances);
        }

        initializeWeights(8);

        if (debugMode) {
            System.out.println("Initial weights: " + Arrays.toString(weights));
            System.out.println("Initial bias: " + bias);
        }

        for (int epoch = 0; epoch < epochs; epoch++) {
            Collections.shuffle(instances, new Random());
            double[] derivativeWeights = new double[weights.length];
            double derivativeBias = 0.0;
            int N = instances.size();

            for (Instance<Number, Integer> instance : instances) {
                double output = linearCombination(instance.getInput());
                double sig = sigmoid(output);
                double error = (sig - instance.getOutput());

                for (int i = 0; i < 8; i++) {
                    derivativeWeights[i] = error * instance.getInput().get(i).doubleValue();
                }
                derivativeBias += error;
            }

            if (printGradients) {
                System.out.println("Epoch " + (epoch + 1) + " gradient: " + Arrays.toString(derivativeWeights));
                System.out.println("Bias gradient: " + derivativeBias);
            }

            for (int i = 0; i < 8; i++) {
                weights[i] -= learningRate * (derivativeWeights[i] / N);
            }

            bias -= learningRate * (derivativeBias / N);

            if (debugMode && epoch % 10 == 0) {
                System.out.println("Epoch " + (epoch + 1) + " updated weights: " + Arrays.toString(weights));
                System.out.println("Epoch " + (epoch + 1) + " updated bias: " + bias);
            }
        }

        if (debugMode) {
            System.out.println("Final weights: " + Arrays.toString(weights));
            System.out.println("Final bias: " + bias);
        }

    }

    private void initializeWeights(int size) {
        weights = new double[size];
        Arrays.fill(weights, 0);
        bias = 0.0;
    }

    private double linearCombination(List<Number> input) {
        double sum = bias;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * input.get(i).doubleValue();
        }
        return sum;
    }

    private double sigmoid(double f) {
        return 1.0 / (1.0 + Math.exp(-f));
    }

    @Override
    public void validate(List<Instance<Number, Integer>> instances) {
        if (debugMode) {
            printLabelDistribution("VALIDATION", instances);
        }

        // If learningRate and epochs are both zero, we tune them
        if (learningRate == 0 && epochs == 0) {
            tuneHyperparameters(instances, true, true);
        } else if (learningRate == 0) {
            tuneHyperparameters(instances, true, false);
        } else if (epochs == 0) {
            tuneHyperparameters(instances, false, true);
        }
    }

    private void tuneHyperparameters(List<Instance<Number, Integer>> validationSet,
                                     boolean tuneLearningRate,
                                     boolean tuneEpochs) {
        List<Double> learningRates = new ArrayList<>(Arrays.asList(0.001, 0.01, 0.1));
        List<Integer> epochValues = new ArrayList<>(Arrays.asList(20, 50, 100, 200));

        double bestAccuracy = -1.0;
        double bestLearningRate = (learningRate != 0) ? learningRate : 0.001;
        int bestEpochs = (epochs != 0) ? epochs : 150;

        List<Double> learningRatesToUse = tuneLearningRate ? learningRates : List.of(learningRate);
        List<Integer> epochsToUse = tuneEpochs ? epochValues : List.of(epochs);

        for (int epoch : epochsToUse) {
            for (double learningRate : learningRatesToUse) {
                this.learningRate = learningRate;
                this.epochs = epoch;
                initializeWeights(trainInstances.get(0).getInput().size());
                trainModel(this.trainInstances);
                List<Integer> predictions = predict(validationSet);
                double accuracy = new Accuracy().evaluate(validationSet, predictions);

                if (accuracy > bestAccuracy) {
                    bestAccuracy = accuracy;
                    bestLearningRate = learningRate;
                    bestEpochs = epoch;
                }

                if (debugMode) {
                    System.out.println("Tuning: LR=" + learningRate + ", EPOCHS=" + epoch + ", ACC=" + accuracy);
                }
            }
        }

        this.learningRate = bestLearningRate;
        this.epochs = bestEpochs;
        initializeWeights(trainInstances.get(0).getInput().size());
        trainModel(this.trainInstances);

        if (debugMode) {
            System.out.println("Best hyperparams found => LR=" + this.learningRate + ", EPOCHS=" + this.epochs);
        }
    }

    private List<Integer> predict(List<Instance<Number, Integer>> data) {
        List<Integer> predictions = new ArrayList<>();
        for (Instance<Number, Integer> instance : data) {
            double output = linearCombination(instance.getInput());
            double prob = sigmoid(output);

            if (debugMode) {
                System.out.printf("Sigmoid(prob) = %.5f for input = %s\n", prob, instance.getInput());
            }

            int label = (prob >= 0.5) ? 1 : 0;
            predictions.add(label);
        }
        return predictions;
    }

    @Override
    public void test(List<Instance<Number, Integer>> instances) {
        if (debugMode) {
            printLabelDistribution("TEST", instances);
        }
        testPredictions = predict(instances);

        System.out.println("Test predictions: " + testPredictions);
    }

    public List<Integer> getTestPredictions() {
        return testPredictions;
    }


    private void printLabelDistribution(String dataSetName, List<Instance<Number, Integer>> data) {
        long countOnes = data.stream().filter(i -> i.getOutput() == 1).count();
        long total = data.size();
        System.out.println(dataSetName + " label distribution => # of 1s: " + countOnes
                + " | # of 0s: " + (total - countOnes) + " | total: " + total);
    }
}