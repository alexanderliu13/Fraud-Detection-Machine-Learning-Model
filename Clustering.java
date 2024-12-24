import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdRandom;

public class Clustering {
    // Connected components object to find clusters
    private CC clusters;
    // The number of locations
    private int m;

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {
        if (locations == null) {
            throw new IllegalArgumentException();
        }
        m = locations.length;

        for (Point2D point : locations) {
            if (point == null) {
                throw new IllegalArgumentException();
            }
        }

        if (k > m || k < 1) {
            throw new IllegalArgumentException();
        }

        EdgeWeightedGraph g = new EdgeWeightedGraph(locations.length);
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                g.addEdge(new Edge(i, j, locations[i].distanceTo(locations[j])));
            }
        }
        KruskalMST mst = new KruskalMST(g);
        EdgeWeightedGraph newG = new EdgeWeightedGraph(m);
        int edgesAdded = 0;
        for (Edge e : mst.edges()) {
            if (edgesAdded < m - k) {
                newG.addEdge(e);
                edgesAdded++;
            }
        }

        clusters = new CC(newG);
    }

    // return the cluster of the ith location
    public int clusterOf(int i) {
        if (i < 0 || i > m - 1) {
            throw new IllegalArgumentException();
        }
        return clusters.id(i);
    }

    // use the clusters to reduce the dimensions of an input
    public int[] reduceDimensions(int[] input) {
        if (input == null) {
            throw new IllegalArgumentException();
        }
        if (input.length != m) {
            throw new IllegalArgumentException();
        }

        int[] res = new int[clusters.count()];
        for (int i = 0; i < input.length; i++) {
            res[clusterOf(i)] += input[i];
        }

        return res;

    }

    // unit testing (required)
    public static void main(String[] args) {
        int c = Integer.parseInt(args[0]);
        int p = Integer.parseInt(args[1]);

        Point2D[] centers = new Point2D[c];
        centers[0] = new Point2D(StdRandom.uniformDouble(0, 1000),
                                 StdRandom.uniformDouble(0, 1000));

        for (int i = 1; i < c; i++) {
            boolean isValid = false;
            while (!isValid) {
                centers[i] = new Point2D(StdRandom.uniformDouble(0, 1000),
                                         StdRandom.uniformDouble(0, 1000));
                int count = 0;
                for (int j = 0; j < i; j++) {
                    if (centers[i].distanceTo(centers[j]) < 4) {
                        break;
                    }
                    count++;
                }
                if (count == i) {
                    isValid = true;
                }
            }
        }

        Point2D[] locations = new Point2D[c * p];
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < p; j++) {
                Point2D center = centers[i];
                locations[i * p + j] = new Point2D(
                        StdRandom.uniformDouble(center.x() - 1, center.x() + 1),
                        StdRandom.uniformDouble(center.y() - 1, center.y() + 1));
                while (locations[i * p + j].distanceTo(center) > 1) {
                    locations[i * p + j] = new Point2D(
                            StdRandom.uniformDouble(center.x() - 1, center.x() + 1),
                            StdRandom.uniformDouble(center.y() - 1, center.y() + 1));
                }
            }
        }

        Clustering test = new Clustering(locations, c);
        System.out.println(test.clusterOf(0));
    }
}
