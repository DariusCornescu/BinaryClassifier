package classification.models;

import classification.Instance;
import classification.Model;
import evaluation.Accuracy;

import java.util.*;
import java.util.stream.Stream;

public class PerceptronClassifier implements Model<Number, Integer> {
    protected double[] weights;
    protected double bias;
    protected int epochs = 0;
    protected double learningRate = 0;
    protected List<Integer> testPredictions;
    protected List<Instance<Number, Integer>> trainInstances;

    public PerceptronClassifier(double learningRate, int epochs) {
        this.learningRate = learningRate;
        this.epochs = epochs;
    }

    public PerceptronClassifier() {
        this.learningRate = 0;
        this.epochs = 0;
    }

    @Override
    public void trainModel(List<Instance<Number, Integer>> instances) {
        this.trainInstances = instances;
        initializeWeights(8);

        for ( int epoch = 0; epoch < epochs; epoch++ ) {
            Collections.shuffle(instances, new Random());
            for ( Instance<Number, Integer> instance : instances ) {
                double linearOutput = linearCombination(instance.getInput());
                int predicted = linearOutput >= 0 ? 1 : 0;
                int actual = instance.getOutput();

                if ( predicted != actual ) {
                    double error = actual - predicted;
                    updateWeightsAndBias(instance.getInput(), error);
                    }
                }
            }
        }

    private void initializeWeights(int size) {
        weights = new double[size];
        Arrays.fill(weights, 0);
        bias = 0.0;
    }

    private void updateWeightsAndBias(List<Number> input, double error) {
        for ( int i = 0; i < weights.length; i++ ) {
            weights[i] += learningRate * error * input.get(i).doubleValue();
        }
        bias += learningRate * error;
    }


    // This method calculates the linear combination of the input features and the weights
    private double linearCombination(List<Number> input) {
        double sum = this.bias;
        for ( int i = 0; i < this.weights.length; i++ ) {
            sum += this.weights[i] * input.get(i).doubleValue();
        }
        return sum;
    }

    @Override
    public void validate(List<Instance<Number, Integer>> instances) {
        if ( learningRate == 0 && epochs == 0 ){
            tuneHyperparameters(instances,true,true);
        }else if (learningRate == 0){
            tuneHyperparameters(instances,true,false);
        }else if (epochs == 0) {
            tuneHyperparameters(instances, false, true);
        }else{
            tuneHyperparameters(instances, false, false);
        }
    }

    private void tuneHyperparameters(List<Instance<Number, Integer>> validationSet, boolean tuneLearningRate, boolean tuneEpochs){
        List<Double> learningRates = new ArrayList<>(Arrays.asList(0.0001,0.001, 0.01, 0.1));
        List<Integer> epochValues = new ArrayList<>(Arrays.asList(50, 100, 200, 300));

        double bestAccuracy = -1.0;
        double bestLearningRate = (learningRate != 0) ? learningRate : 0.001;
        int bestEpochs = (epochs != 0) ? epochs : 150;

        List<Double> learningRatesToUse = tuneLearningRate ? learningRates : List.of(learningRate);
        List<Integer> epochsToUse = tuneEpochs ? epochValues : List.of(epochs);

        for( int epoch : epochsToUse ){
            for ( double learningRate : learningRatesToUse ){
                this.learningRate = learningRate;
                this.epochs = epoch;

                initializeWeights(trainInstances.get(0).getInput().size());
                trainModel(this.trainInstances);
                List<Integer> predictions = predict(validationSet);
                double accuracy = new Accuracy().evaluate(validationSet, predictions);

                if (accuracy > bestAccuracy){
                    bestAccuracy = accuracy;
                    bestLearningRate = learningRate;
                    bestEpochs = epoch;
                }
            }
        }

        this.learningRate = bestLearningRate;
        this.epochs = bestEpochs;
        initializeWeights(trainInstances.get(0).getInput().size());
        trainModel(this.trainInstances);
    }

    private List<Integer> predict(List<Instance<Number, Integer>> data) {
        List<Integer> predictions = new ArrayList<>();
        for (Instance<Number, Integer> instance : data) {
            double output = linearCombination(instance.getInput());
            int label = output >= 0 ? 1 : 0;
            predictions.add(label);
        }
        return predictions;
    }

    @Override
    public void test(List<Instance<Number, Integer>> instances) { testPredictions = predict(instances);}

    public List<Integer> getTestPredictions() { return this.testPredictions; }
}
