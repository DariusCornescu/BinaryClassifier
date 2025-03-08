package evaluation;

import classification.Instance;

import java.util.List;

public class Precision implements EvaluationMeasure<Number, Integer> {
    @Override
    public double evaluate(List<Instance<Number, Integer>> instances, List<Integer> predictions) {
        int truePositives = 0, falsePositives = 0;
        for( int i=0; i<instances.size(); i++){
            int actual = instances.get(i).getOutput();
            int predicted = predictions.get(i);

            if(predicted == 1)
                if( actual == 1) truePositives++;
                else falsePositives++;
        }

        return (double) truePositives / ( truePositives + falsePositives );
    }
}
