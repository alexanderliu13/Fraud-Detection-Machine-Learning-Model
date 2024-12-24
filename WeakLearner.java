import java.util.Arrays;

public class WeakLearner {
    // Stores dimension number
    private int resD;
    // Stores value where the threshold is
    private int resV;
    // Stores which side of the threshold represents what
    private int resS;
    // number of dimensions
    private int k;


    // train the weak learner
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        int n;
        validateInputs(input, weights, labels);

        n = input.length;
        k = input[0].length;
        resD = -1;
        resV = -1;
        resS = -1;
        double maxWeight = 0.0;

        for (int d = 0; d < k; d++) {
            ComparatorHelper[] cH = new ComparatorHelper[n];
            double cleanPointsWeights = 0;
            double fraudPointsWeights = 0;

            for (int i = 0; i < n; i++) {
                cH[i] = new ComparatorHelper(i, input[i][d]);
                double weight = weights[i];
                if (labels[i] == 0) {
                    fraudPointsWeights += weight;
                }
                else {
                    cleanPointsWeights += weight;
                }
            }

            Arrays.sort(cH);

            for (int j = 0; j < n; j++) {
                int ogIndex = cH[j].getIndex();
                double weight = weights[ogIndex];

                if (labels[ogIndex] == 0) {
                    cleanPointsWeights += weight;
                    fraudPointsWeights -= weight;
                }
                else {
                    cleanPointsWeights -= weight;
                    fraudPointsWeights += weight;
                }

                if (j + 1 < n) {
                    if (cH[j + 1].compareTo(cH[j]) == 0) {
                        continue;
                    }
                }
                if (cleanPointsWeights > maxWeight) {
                    maxWeight = cleanPointsWeights;
                    resD = d;
                    resS = 0;
                    resV = cH[j].getValue();
                }

                if (fraudPointsWeights > maxWeight) {
                    maxWeight = fraudPointsWeights;
                    resD = d;
                    resS = 1;
                    resV = cH[j].getValue();
                }
            }
        }
    }

    // validates inputs for the constructor
    private void validateInputs(int[][] input, double[] weights, int[] labels) {
        if (input == null || weights == null || labels == null) {
            throw new IllegalArgumentException();
        }

        if (!(input.length == weights.length && weights.length == labels.length)) {
            throw new IllegalArgumentException();
        }

        for (double weight : weights) {
            if (weight < 0) {
                throw new IllegalArgumentException();
            }
        }

        for (double label : labels) {
            if (label != 0 && label != 1) {
                throw new IllegalArgumentException();
            }
        }
    }

    private class ComparatorHelper implements Comparable<ComparatorHelper> {
        private final int ogIndex; // Position of the input value
        private final int value; // Value at a cluster in the transaction summary

        // initializes variables of position and value
        public ComparatorHelper(int i, int v) {
            this.ogIndex = i;
            this.value = v;
        }

        // returns position
        public int getIndex() {
            return ogIndex;
        }

        // returns value
        public int getValue() {
            return value;
        }

        // compares two comparators based on value
        public int compareTo(ComparatorHelper c2) {
            return Integer.compare(this.value, c2.value);
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null) {
            throw new IllegalArgumentException();
        }
        if (sample.length != k) {
            throw new IllegalArgumentException();
        }
        int tester = sample[dimensionPredictor()];
        if (signPredictor() == 0) {
            if (tester <= valuePredictor()) {
                return 0;
            }
            else {
                return 1;
            }
        }
        else {
            if (tester > valuePredictor()) {
                return 0;
            }
            else {
                return 1;
            }
        }
    }

    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return resD;
    }

    // return the value the learner uses to separate the data
    public int valuePredictor() {
        return resV;
    }

    // return the sign the learner uses to separate the data
    public int signPredictor() {
        return resS;
    }

    // unit testing
    public static void main(String[] args) {

    }
}
