package utils.KnnUtils;

import classification.Instance;

public class ManhattanDistance extends Distance{

    @Override
    public double computeDistance(Instance<Number, Integer> instance1, Instance<Number, Integer> instance2) {
        double distance = 0;
        for (int i = 0; i < instance1.getFeatures().size(); i++) {
            distance += Math.abs(instance1.getFeatures().get(i).doubleValue() - instance2.getFeatures().get(i).doubleValue());
        }
        return distance;
    }
}

// The mathematical formula for the Manhattan distance is:
//
