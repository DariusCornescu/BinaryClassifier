package classification.models;

import classification.Instance;
import classification.Model;

import java.util.*;
import java.util.stream.Collectors;

public class DecisionTreeClassifier implements Model<Number, Integer> {
    private static class Node {
        private String attribute;
        private double threshold;
        private int label;
        private Node left;
        private Node right;

        private boolean isLeaf() { return attribute == null; }
    }

    private Node root;
    private List<Integer> testPredictions;
    private final int maxDepth = 20; // Example stopping criterion

    @Override
    public void trainModel(List<Instance<Number, Integer>> instances) { root = buildTree(instances, new HashSet<>(), 0); }

    private Node buildTree(List<Instance<Number, Integer>> instances, Set<Integer> usedFeatures, int depth) {
        // Check stopping conditions
        if (instances.isEmpty()) return null;
        if (allSameLabel(instances)) {
            return createLeaf(instances.get(0).getOutput());
        }
        if (depth >= maxDepth) {
            return createLeaf(majorityLabel(instances));
        }

        double baseEntropy = computeEntropy(instances);
        SplitResult bestSplit = findBestSplit(instances, usedFeatures, baseEntropy);

        if (bestSplit.attributeIndex == -1) {
            // No good split found -> create a leaf
            return createLeaf(majorityLabel(instances));
        }

        List<Instance<Number, Integer>> leftSubset = new ArrayList<>();
        List<Instance<Number, Integer>> rightSubset = new ArrayList<>();
        for (Instance<Number, Integer> instance : instances) {
            double val = instance.getInput().get(bestSplit.attributeIndex).doubleValue();
            if (val <= bestSplit.threshold) {
                leftSubset.add(instance);
            } else {
                rightSubset.add(instance);
            }
        }

        Node node = new Node();
        node.attribute = "Attribute_" + bestSplit.attributeIndex;
        node.threshold = bestSplit.threshold;

        Set<Integer> newUsedFeatures = new HashSet<>(usedFeatures);
        newUsedFeatures.add(bestSplit.attributeIndex);

        node.left = buildTree(leftSubset, newUsedFeatures, depth + 1);
        node.right = buildTree(rightSubset, newUsedFeatures, depth + 1);

        return node;
    }

    private Node createLeaf(int label) {
        Node leaf = new Node();
        leaf.label = label;
        return leaf;
    }

    private boolean allSameLabel(List<Instance<Number, Integer>> instances) {
        int firstLabel = instances.get(0).getOutput();
        for (Instance<Number, Integer> instance : instances) {
            if (!instance.getOutput().equals(firstLabel)) {
                return false;
            }
        }
        return true;
    }

    private int majorityLabel(List<Instance<Number, Integer>> instances) {
        Map<Integer, Integer> labelCounts = new HashMap<>();
        for (Instance<Number, Integer> instance : instances) {
            int label = instance.getOutput();
            labelCounts.put(label, labelCounts.getOrDefault(label, 0) + 1);
        }

        return labelCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get().getKey();
    }

    private class SplitResult {
        int attributeIndex = -1;
        double threshold = Double.NaN;
        double infoGain = -1.0;
    }

    private SplitResult findBestSplit(List<Instance<Number, Integer>> instances, Set<Integer> usedFeatures, double baseEntropy) {
        SplitResult best = new SplitResult();
        int featureCount = instances.get(0).getInput().size();

        for (int feature = 0; feature < featureCount; feature++) {
            if (usedFeatures.contains(feature)) continue;
            double[] thresholds = computeCandidateThresholds(instances, feature);
            for (double threshold : thresholds) {
                double ig = computeInformationGain(instances, feature, threshold, baseEntropy);
                if (ig > best.infoGain) {
                    best.infoGain = ig;
                    best.threshold = threshold;
                    best.attributeIndex = feature;
                }
            }
        }
        return best;
    }


    private double computeEntropy(List<Instance<Number, Integer>> instances) {
        Map<Integer, Long> labelCounts = instances.stream()
                .map(Instance::getOutput)
                .collect(Collectors.groupingBy(label -> label, Collectors.counting()));

        double total = instances.size();
        double entropy = 0.0;
        for (Long count : labelCounts.values()) {
            double p = count / total;
            entropy -= p * (Math.log(p) / Math.log(2));
        }
        return entropy;
    }

    private double[] computeCandidateThresholds(List<Instance<Number, Integer>> instances, int attributeColumn) {
        List<Double> values = instances.stream()
                .map(i -> i.getInput().get(attributeColumn).doubleValue())
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        // Use midpoints between distinct consecutive values
        List<Double> thresholds = new ArrayList<>();
        for (int i = 0; i < values.size() - 1; i++) {
            double mid = (values.get(i) + values.get(i + 1)) / 2.0;
            thresholds.add(mid);
        }

        return thresholds.stream().mapToDouble(d -> d).toArray();
    }

    private double computeInformationGain(List<Instance<Number, Integer>> instances, int attributeColumn, double threshold, double baseEntropy) {
        List<Instance<Number, Integer>> leftSubset = new ArrayList<>();
        List<Instance<Number, Integer>> rightSubset = new ArrayList<>();

        for (Instance<Number, Integer> instance : instances) {
            double val = instance.getInput().get(attributeColumn).doubleValue();
            if (val <= threshold) {
                leftSubset.add(instance);
            } else {
                rightSubset.add(instance);
            }
        }

        double total = instances.size();
        double weightedEntropy =
                ((double) leftSubset.size() / total) * computeEntropy(leftSubset) +
                        ((double) rightSubset.size() / total) * computeEntropy(rightSubset);

        return baseEntropy - weightedEntropy;
    }

    @Override
    public void validate(List<Instance<Number, Integer>> instances) {
    }

    @Override
    public void test(List<Instance<Number, Integer>> instances) {
        testPredictions = new ArrayList<>();
        for (Instance<Number, Integer> instance : instances) {
            testPredictions.add(predictInstance(instance));
        }
    }

    private int predictInstance(Instance<Number, Integer> instance) {
        Node current = root;
        while (current != null && !current.isLeaf()) {
            int featureIndex = Integer.parseInt(current.attribute.split("_")[1]);
            double value = instance.getInput().get(featureIndex).doubleValue();
            current = (value <= current.threshold) ? current.left : current.right;
        }
        return (current == null) ? 0 : current.label; // fallback in case of null
    }

    public List<Integer> getTestPredictions() { return testPredictions; }
}
