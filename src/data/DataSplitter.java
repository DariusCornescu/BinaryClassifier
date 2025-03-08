package data;

import classification.Instance;
import utils.MinMaxScaler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DataSplitter {
    protected Integer percentage;
    protected String filename;
    protected List<Instance<Number, Integer>> listOfAllInstances;
    protected List<Instance<Number, Integer>> listOfTrainingInstances = new ArrayList<>();
    protected List<Instance<Number, Integer>> listOfValidationInstances = new ArrayList<>();
    protected List<Instance<Number, Integer>> listOfTestingInstances = new ArrayList<>();
    private final MinMaxScaler scaler = new MinMaxScaler();

    public DataSplitter(Integer percentage, String filename) {
        this.percentage = percentage;
        this.filename = filename;
        setListOfAllInstances();
        splitInstancesInGroups();
    }


    public void setPercentage(Integer percentage) { this.percentage = percentage; }

    public List<Instance<Number, Integer>> getListOfTrainingInstances() { return listOfTrainingInstances; }
    public List<Instance<Number, Integer>> getListOfValidationInstances() { return listOfValidationInstances; }
    public List<Instance<Number, Integer>> getListOfTestingInstances() { return listOfTestingInstances; }

    public void setListOfAllInstances() {
        DataLoader loader = new DataLoader(this.filename);
        this.listOfAllInstances = loader.getListOfInstances();
        System.out.println("Loaded " + this.listOfAllInstances.size() + " instances");

        // Clone the list for pre-scaling samples
        List<Instance<Number, Integer>> preScalingSamples = new ArrayList<>();
        for (int i = 0; i < Math.min(10, listOfAllInstances.size()); i++) {
            preScalingSamples.add(listOfAllInstances.get(i));
        }

        // Print sample instances before scaling
        printSampleInstances("Before Scaling", preScalingSamples);

        this.scaler.fit(this.listOfAllInstances);
        System.out.println("Fitted the scaler");
        this.scaler.transform(this.listOfAllInstances);
        System.out.println("Transformed the instances");

        // Clone the list again for post-scaling samples
        List<Instance<Number, Integer>> postScalingSamples = new ArrayList<>();
        for (int i = 0; i < Math.min(5, listOfAllInstances.size()); i++) {
            postScalingSamples.add(listOfAllInstances.get(i));
        }

        // Print sample instances after scaling
        printSampleInstances("After Scaling", postScalingSamples);
    }

    private void printSampleInstances(String label, List<Instance<Number, Integer>> samples) {
        System.out.println("\n" + label + " Sample Instances:");
        for (int i = 0; i < samples.size(); i++) {
            Instance<Number, Integer> instance = samples.get(i);
            System.out.printf("Instance %d: %s%n", i + 1, instance.getInput());
        }
        System.out.println();


        System.out.println("Feature-wise Min Values: " + Arrays.toString(scaler.getMinValues()));
        System.out.println("Feature-wise Max Values: " + Arrays.toString(scaler.getMaxValues()));

    }

    public void splitInstancesInGroups(){
        int numberOfTrainingInstances = (int)((double) percentage / 100 * listOfAllInstances.size());
        moveInstances(numberOfTrainingInstances, listOfAllInstances, listOfTrainingInstances);
        moveInstances(listOfAllInstances.size(), listOfAllInstances, listOfTestingInstances);
        int numberOfValidationInstances = (int)(0.1 * listOfTrainingInstances.size());
        moveInstances(numberOfValidationInstances, listOfTrainingInstances, listOfValidationInstances);
    }

    private void moveInstances(int numberOfInstances, List<Instance<Number, Integer>> source, List<Instance<Number, Integer>> target) {
        Random rand = new Random();
        while( numberOfInstances-- > 0){
            int randomIndex = rand.nextInt(source.size());
            target.add(source.get(randomIndex));
            source.remove(randomIndex);
        }
    }

}
