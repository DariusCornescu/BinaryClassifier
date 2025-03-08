package classification.models;

import classification.Instance;
import classification.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NaiveBayesClassifier implements Model<Number, Integer> {

    private Map<Integer, Double> classPriors;  // P(C) probability true/false
    private Map<Integer, double[][]> meanVariancePerClass; // Mean and Variance for each feature per class
    private List<Integer> testPredictions;

    @Override
    public void trainModel(List<Instance<Number, Integer>> instances) {
        this.classPriors = new HashMap<>();
        this.meanVariancePerClass = new HashMap<>();
        // Calculate class counts
        Map<Integer, Integer> classCounts = new HashMap<>();
        for (Instance<Number, Integer> instance : instances) {
            int label = instance.getOutput();
            classCounts.put(label, classCounts.getOrDefault(label, 0) + 1);
        }
        // Calculate class priors
        int totalInstances = instances.size();
        for (Map.Entry<Integer, Integer> entry : classCounts.entrySet()) {
            double prior = entry.getValue() / (double) totalInstances;
            classPriors.put(entry.getKey(), prior);
        }

        // Calculate mean and variance for each feature per class
        for ( Integer classLabel : classCounts.keySet()) {
            List<Instance<Number, Integer>> classInstances = new ArrayList<>();
            for (Instance<Number, Integer> instance : instances) {
                if (instance.getOutput().equals(classLabel)) {
                    classInstances.add(instance);
                }
            }

            int featureCount = classInstances.get(0).getInput().size();
            double[][] stats = computeMeanVarianceForClass(classInstances, featureCount);
            meanVariancePerClass.put(classLabel, stats);
        }
    }

    private double[][] computeMeanVarianceForClass(List<Instance<Number, Integer>> instances, int featureCount) {
        double[][] stats = new double[featureCount][2]; // [mean = 0, variance=1]
        // Compute mean
        for (int f = 0; f < featureCount; f++) {
            double sum = 0;
            for (Instance<Number, Integer> instance : instances) {
                sum += instance.getInput().get(f).doubleValue();
            }
            double mean = sum / instances.size();
            stats[f][0] = mean;
        }

        // Compute variance
        for (int f = 0; f < featureCount; f++) {
            double mean = stats[f][0];
            double varSum = 0;
            for (Instance<Number, Integer> instance : instances) {
                double diff = instance.getInput().get(f).doubleValue() - mean;
                varSum += diff * diff;
            }
            double variance = varSum / instances.size();
            // Add a small epsilon to avoid division by zero
            if (variance == 0) {
                variance = 1e-9;
            }
            stats[f][1] = variance;
        }

        return stats;
    }

    @Override
    public void validate(List<Instance<Number, Integer>> instances) {}

    @Override
    public void test(List<Instance<Number, Integer>> instances) {testPredictions = new ArrayList<>();
        for (Instance<Number, Integer> instance : instances) {
            int predictedLabel = predictInstance(instance);
            testPredictions.add(predictedLabel);
        }
    }

    private int predictInstance(Instance<Number, Integer> instance) {
        // Compute posterior probabilities for each class
        Map<Integer, Double> logPosteriors = new HashMap<>();
        int featureCount = instance.getInput().size();

        for (Integer classLabel : classPriors.keySet()) {
            double logPosterior = Math.log(classPriors.get(classLabel));
            double[][] stats = meanVariancePerClass.get(classLabel);

            for (int f = 0; f < featureCount; f++) {
                double x = instance.getInput().get(f).doubleValue();
                double mean = stats[f][0];
                double variance = stats[f][1];
                double p = gaussianProbability(x, mean, variance);
                logPosterior += Math.log(p);
            }

            logPosteriors.put(classLabel, logPosterior);
        }

        // Select class with the highest posterior probability
        return logPosteriors.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get().getKey();
    }

    private double gaussianProbability(double x, double mean, double variance) {
        double exponent = Math.exp(-((x - mean) * (x - mean)) / (2 * variance));
        return (1 / Math.sqrt(2 * Math.PI * variance)) * exponent;
    }

    public List<Integer> getTestPredictions() { return testPredictions; }
}
