Programming Assignment 7: Fraud Detection

/* *****************************************************************************
 *  Describe how you implemented the Clustering constructor
 **************************************************************************** */
I first made sure that all the inputs were valid. Then, I created an edgeweightedgraph,
traversing through evey point in locations and connecting each point with every
other point on the graph. I then used that edgeweightedgraph to create a minimum
spanning tree. Next, I created a new edgeweighted graph that included every one
of the first edgeweightedgraph edges except for the longest edges in the MST. Using
this disconnected edgeweightedgraph, I was able to create a CC which portioned groups
of nodes from the graph into clusters.

/* *****************************************************************************
 *  Describe how you implemented the WeakLearner constructor
 **************************************************************************** */
Again, I first validated all inputs. I then traversed through each dimension, creating
arrays of comparators in order to later efficiently sort the points by their values
at said dimension. Before sorting, I initialized all points labeled 0 into fraudulent,
and all points labeled 1 into clean. However, after sorting the array, we adjust
the weights depending on where the v threshold may be, so we add and subtract from
the clean and fraudulent weights accordingly. We also know that if the next point in
the cH array has the same value as the current one, we don't have to compare it
to the max result. However, when one of the weights surpasses the previous max
weight, we update the dimension, value, and sign predictor accordingly.

/* *****************************************************************************
 *  Consider the large_training.txt and large_test.txt datasets.
 *  Run the boosting algorithm with different values of k and T (iterations),
 *  and calculate the test data set accuracy and plot them below.
 *
 *  (Note: if you implemented the constructor of WeakLearner in O(kn^2) time
 *  you should use the small_training.txt and small_test.txt datasets instead,
 *  otherwise this will take too long)
 **************************************************************************** */

      k          T         test accuracy       time (seconds)
   --------------------------------------------------------------------------
    5            2          .74                 .125
    5           10          .834                .203
    2           50          .60125              .467
    10          50          .95125              .683
    10          80          .96                 .827

/* *****************************************************************************
 *  Find the values of k and T that maximize the test data set accuracy,
 *  while running under 10 second. Write them down (as well as the accuracy)
 *  and explain:
 *   1. Your strategy to find the optimal k, T.
 *   2. Why a small value of T leads to low test accuracy.
 *   3. Why a k that is too small or too big leads to low test accuracy.
 **************************************************************************** */

My optimal k, T was 70, 500.
My strategy was starting small on both sides with 10 and 80. After this, I made T
very big at 500 and also tried making k very big at the value of 100, however, I
found that I had made my k too big, so I reduced it to 70 to get the run time under
10 seconds.

The reason that a small value of T leads to low test accuracy is because it means
that the model doesn't correct for wrong values enough. There aren't as many different
thresholds that are tested since there are only so many tests, so the accuracy is low.

If the k value is too small, it means that there isn't enough data about each sample
to make a good decision as to whether or not the transaction is fraudulent. If the k
value is too large, there may be too much information given that isn't relevant, which
may swing the model's decision wrongly.


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */

N/A
