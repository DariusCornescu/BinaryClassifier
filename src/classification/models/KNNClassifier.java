package classification.models;

import classification.Instance;
import classification.Model;
import evaluation.Accuracy;
import utils.KnnUtils.Distance;
import utils.KnnUtils.EuclideanDistance;
import utils.KnnUtils.ManhattanDistance;

import java.util.*;

public class KNNClassifier implements Model<Number, Integer> {

    protected Integer k = 0; // Number of neighbors to consider
    protected Distance distanceMetric;
    protected List<Instance<Number, Integer>> trainingInstances;
    protected List<Integer> testPredictions;
    
    public KNNClassifier() {
        this.k = 0;
        this.distanceMetric = null;
    }

    public KNNClassifier(int k, Distance.DistanceType distance) {
        this.k = k;
        if (distance == Distance.DistanceType.EUCLIDEAN) {
            this.distanceMetric = new EuclideanDistance();
        } else if (distance == Distance.DistanceType.MANHATTAN) {
            this.distanceMetric = new ManhattanDistance();
        }
    }
    
    @Override
    public void trainModel(List<Instance<Number, Integer>> instances) { this.trainingInstances = instances;}

    @Override
    public void validate(List<Instance<Number, Integer>> instances) {
        if (this.distanceMetric == null) {
            this.distanceMetric = new EuclideanDistance();
        }

        if (this.k == 0) {
            int suggestedK = (int) Math.sqrt(instances.size());
            double highestAccuracy = -1.0;
            int bestK = suggestedK;
            Accuracy accuracyEvaluator = new Accuracy();

            for (int candidateK = Math.max(1, suggestedK - 5); candidateK <= suggestedK + 5; candidateK++) {
                this.k = candidateK;
                List<Integer> validPredictions = predict(instances);
                double currentAccuracy = accuracyEvaluator.evaluate(instances, validPredictions);

                if (currentAccuracy > highestAccuracy) {
                    highestAccuracy = currentAccuracy;
                    bestK = candidateK;
                }
            }
            this.k = bestK;
        }
    }

    @Override
    public void test(List<Instance<Number, Integer>> instances) {
        this.testPredictions = predict(instances);
    }

    private List<Integer> predict(List<Instance<Number, Integer>> instances) {
        List<Integer> predictions = new ArrayList<>();
        for (Instance<Number, Integer> instance : instances) {
            List<Instance<Number, Integer>> neighbors = findNearestNeighbors(instance);
            int predictedLabel = voteOnLabel(neighbors);
            predictions.add(predictedLabel);
        }
        return predictions;
    }

    private List<Instance<Number, Integer>> findNearestNeighbors(Instance<Number, Integer> target) {
        PriorityQueue<Instance<Number, Integer>> neighbors = new PriorityQueue<>(
                Comparator.comparingDouble(a -> -distanceMetric.computeDistance(target, a))
        );

        for (Instance<Number, Integer> candidate : trainingInstances) {
            double dist = distanceMetric.computeDistance(target, candidate);
            if (neighbors.size() < k) {
                neighbors.offer(candidate);
            } else {
                // If current candidate is closer than the farthest in the queue, replace it
                double farthestDist = distanceMetric.computeDistance(target, neighbors.peek());
                if (dist < farthestDist) {
                    neighbors.poll();
                    neighbors.offer(candidate);
                }
            }
        }

        return new ArrayList<>(neighbors);
    }

    private int voteOnLabel(List<Instance<Number, Integer>> neighbors) {
        Map<Integer, Integer> labelCounts = new HashMap<>();
        for (Instance<Number, Integer> neighbor : neighbors) {
            labelCounts.put(neighbor.getOutput(), labelCounts.getOrDefault(neighbor.getOutput(), 0) + 1);
        }
        return labelCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    public List<Integer> getTestPredictions() { return testPredictions; }
}
