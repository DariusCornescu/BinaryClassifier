package evaluation;

import classification.Instance;

import java.util.List;

public class ConfusionMatrix {

    public int [][] computeMatrix(List<Instance<Number,Integer>> instances, List<Integer> predictions){
        int truePositives = 0, trueNegatives = 0;
        int falsePositives = 0, falseNegatives = 0;

        for(int i =0; i<instances.size(); i++)
        {
            int actual = instances.get(i).getOutput();
            int predicted = predictions.get(i);
            if ( predicted == 1){
                if ( actual == 1 ) truePositives++;
                else falsePositives++;
            }else{
                if ( actual == 1) falseNegatives++;
                else trueNegatives++;}
        }

        return new int[][]{
                {truePositives, falseNegatives},
                {falsePositives, trueNegatives} };

    }
}
