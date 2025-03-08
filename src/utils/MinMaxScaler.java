package utils;

import classification.Instance;

import java.util.ArrayList;
import java.util.List;

public class MinMaxScaler {
    private double[] mins;
    private double[] maxs;

    public void fit(List<Instance<Number, Integer>> instances){
        int featureCount = instances.get(0).getInput().size();
        mins = new double[featureCount];
        maxs = new double[featureCount];

        for (int i = 0; i < featureCount; i++) {
            mins[i] = Double.POSITIVE_INFINITY;
            maxs[i] = Double.NEGATIVE_INFINITY;
        }

        for (Instance<Number, Integer> instance : instances) {
            List<Number> inputs = instance.getInput();
            for (int i = 0; i < featureCount; i++) {
                double value = inputs.get(i).doubleValue();
                if (value < mins[i]) mins[i] = value;
                if (value > maxs[i]) maxs[i] = value;
            }
        }

        for (int i = 0; i < featureCount; i++) {
            if (mins[i] == maxs[i]) maxs[i] = mins[i] + 1;
        }
    }

    public void transform(List<Instance<Number, Integer>> instances) {
        for (Instance<Number, Integer> instance : instances) {
            List<Number> inputs = instance.getInput();
            List<Number> scaledInputs = new ArrayList<>();
            for (int i = 0; i < inputs.size(); i++) {
                double scaledValue = (inputs.get(i).doubleValue() - mins[i]) / (maxs[i] - mins[i]);
                scaledInputs.add(scaledValue);
            }
            instance.setInput(scaledInputs); // Ensure you have a setter in Instance class
        }
    }

    public double[] getMinValues() { return mins; }
    public double[] getMaxValues() { return maxs; }

}
