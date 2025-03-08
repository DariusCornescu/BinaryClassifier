package evaluation;

import classification.Instance;

import java.util.List;

public class Recall implements EvaluationMeasure<Number, Integer> {
    @Override
    public double evaluate(List<Instance<Number, Integer>> instances, List<Integer> predictions) {
        int truePositives = 0, falseNegatives = 0;
        for( int i=0; i<instances.size(); i++){
            int actual = instances.get(i).getOutput();
            int predicted = predictions.get(i);

            if(predicted == 1) {
                if (actual == 1) truePositives++;
            }else{
                if (actual == 1) falseNegatives++;
            }
        }

        return (double) truePositives / ( truePositives + falseNegatives );
    }
}

