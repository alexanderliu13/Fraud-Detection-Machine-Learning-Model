import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.List;

public class BoostingAlgorithm {
    // stores weights of each point
    private double[] weights;
    // stores the input after it is reduced
    private int[][] reducedInput;
    // stores the label of each location
    private int[] labels;
    // Clustering object
    private Clustering clusterCreate;
    // Stores all the weakLearners created by the iterate method
    private List<WeakLearner> wlList;


    // create the clusters and initialize your data structures
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations, int k) {
        if (labels == null) {
            throw new IllegalArgumentException();
        }
        int m = labels.length;
        if (input == null || locations == null) {
            throw new IllegalArgumentException();
        }

        if (input.length != labels.length) {
            throw new IllegalArgumentException();
        }

        if (k < 1 || k > m) {
            throw new IllegalArgumentException();
        }

        for (int label : labels) {
            if (!(label == 0 || label == 1)) {
                throw new IllegalArgumentException();
            }
        }
        this.labels = labels.clone();
        clusterCreate = new Clustering(locations, k);
        reducedInput = new int[labels.length][k];
        for (int i = 0; i < input.length; i++) {
            reducedInput[i] = clusterCreate.reduceDimensions(input[i]);
        }

        weights = new double[labels.length];
        for (int j = 0; j < weights.length; j++) {
            weights[j] = 1.0 / weights.length;
        }

        wlList = new ArrayList<>();

    }

    // return the current weight of the ith point
    public double weightOf(int i) {
        return weights[i];
    }

    // apply one step of the boosting algorithm
    public void iterate() {
        WeakLearner wl = new WeakLearner(reducedInput, weights, labels);
        wlList.add(wl);
        double sum = 0;
        for (int i = 0; i < reducedInput.length; i++) {
            if (wl.predict(reducedInput[i]) != labels[i]) {
                weights[i] = weights[i] * 2;
            }
            sum += weights[i];
        }
        for (int k = 0; k < weights.length; k++) {
            weights[k] = weights[k] / sum;
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null) {
            throw new IllegalArgumentException();
        }
        int[] reducedSample = clusterCreate.reduceDimensions(sample);
        int numZeros = 0;
        int numOnes = 0;
        for (WeakLearner i : wlList) {
            if (i.predict(reducedSample) == 0) {
                numZeros++;
            }
            else {
                numOnes++;
            }
        }

        if (numZeros >= numOnes) {
            return 0;
        }
        return 1;

    }

    // unit testing
    public static void main(String[] args) {
        DataSet training = new DataSet(args[0]);
        DataSet testing = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int tt = Integer.parseInt(args[3]);

        int[][] trainingInput = training.getInput();
        int[][] testingInput = testing.getInput();
        int[] trainingLabels = training.getLabels();
        int[] testingLabels = testing.getLabels();
        Point2D[] trainingLocations = training.getLocations();

        Stopwatch s = new Stopwatch();
        double startTime = s.elapsedTime();

        // train the model
        BoostingAlgorithm model = new BoostingAlgorithm(trainingInput, trainingLabels,
                                                        trainingLocations, k);
        for (int t = 0; t < tt; t++)
            model.iterate();

        // calculate the training data set accuracy
        double trainingAccuracy = 0;
        for (int i = 0; i < training.getN(); i++)
            if (model.predict(trainingInput[i]) == trainingLabels[i])
                trainingAccuracy += 1;
        trainingAccuracy /= training.getN();

        // calculate the test data set accuracy
        double testAccuracy = 0;
        
        for (int i = 0; i < testing.getN(); i++)
            if (model.predict(testingInput[i]) == testingLabels[i])
                testAccuracy += 1;
        testAccuracy /= testing.getN();

        double endTime = s.elapsedTime();
        double elapsedTime = endTime - startTime;
        StdOut.println("Elapsed time is: " + elapsedTime);
        StdOut.println("Training accuracy of model: " + trainingAccuracy);
        StdOut.println("Test accuracy of model: " + testAccuracy);
    }
}
