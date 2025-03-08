package evaluation;

import classification.Instance;

import java.util.List;
import java.util.Objects;

public class Accuracy implements EvaluationMeasure<Number, Integer> {

    @Override
    public double evaluate(List<Instance<Number, Integer>> instances, List<Integer> predictions) {
        int positiveCount = 0, negativeCount = 0;
        int correctPositives = 0, correctNegatives = 0;

        for (int i = 0; i < instances.size(); i++) {
            int actual = instances.get(i).getOutput();
            int predicted = predictions.get(i);

            if (actual == 1) {
                positiveCount++;
                if (predicted == 1) correctPositives++;
            } else {
                negativeCount++;
                if (predicted == 0) correctNegatives++;
            }
        }

        double positiveAccuracy = (double) correctPositives / positiveCount;
        double negativeAccuracy = (double) correctNegatives / negativeCount;

        return (positiveAccuracy * positiveCount + negativeAccuracy * negativeCount) / instances.size();
    }
}
