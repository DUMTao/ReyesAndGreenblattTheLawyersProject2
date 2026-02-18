/**
 * Returns the authors' names.
 * @return  Kamil Reyes and Greenblatt
 */

import java.util.*;

public class SaddlebagBalancer {

    private static volatile boolean foundSolution = false;

    public static List<Integer> getPartition(List<Integer> packageWeights){
        int n = packageWeights.size();
        double sum = 0;
        foundSolution = false; //we HAD TO ADD THIS

        //Base case if the list is empty
        if (n == 0){
            return packageWeights;
        }

        for (int w : packageWeights) { //Iterate sum of weights from list
            sum += w;
        }

        //Base case if the total is odd then stop the process early
        if (sum % 2 != 0){
            return Collections.nCopies(n, 0); //Return copy of array with 0's
        }

        //Sum =/ odd so save the half for side comparison
        int target = (int) sum / 2;

//--------
        //Create array of integers from the list of package arrays as weights
        Item[] items = new Item[n];
        for(int i = 0; i < n; i++){
            items[i] = new Item(packageWeights.get(i), i);
        }

        //Sort in weight descending order
        Arrays.sort(items, (a, b) -> b.weight - a.weight);

        //Extract sorted weights for the solution
        Integer[] weights = new Integer[n];
        for(int i = 0; i < n; i++){
            weights[i] = items[i].weight;
        }
//--------
        //Populate extra array
        int[] extra = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            extra[i] = extra[i + 1] + weights[i];
        }

        //Arrays for later ;)
        int[] solution = new int[n];
        //int[] current = new int[n];


        int splitDepth = Math.min(5, n); //Calculate how many possibilities the array could be and split based on that
        List<int[]> prefixes = new ArrayList<>(); //Store the possibilities
        generatePrefixes(prefixes, new int[splitDepth], 0); //later

        //Create a list for our lovely threads
        List<Thread> threads = new ArrayList<>();

        /*
        * Iterating though the threads,
        * it checks whether the weights starting from 0 to the right, if this is a valid target weight
        * if it is, then continue recursing until it isn't while creating a new partition thread
        * if it isn't, then return to and see if it's possible to add on the left while keeping it in balance
        */
        for (int[] prefix : prefixes) {
            PartitionThread t = new PartitionThread(prefix, weights, extra, target, solution);
            threads.add(t);
            t.start();
        }

        //"This is just saying uhhhh make sure there isnt an error but just assigning the solution, basically I'm pretty sure" - Matt
        //Wait until all threads are done, if errors continue :)
        for (Thread t : threads) {
            try { t.join(); }
            catch (InterruptedException ignored) {}
        }
//--------
        //Map final solution to the original list
        int[] finalSolution = new int[n];
        for(int i = 0; i < n; i++){
            finalSolution[items[i].index] = solution[i];
        }

        //Convert to List<Integer> for answer
        List<Integer> result = new ArrayList<>();
        for (int x : finalSolution) result.add(x);
        return result;
//--------
    }

    private static void generatePrefixes(List<int[]> list, int[] curr, int i) {
        //Base case, length of decision path
        if (i == curr.length) {
            list.add(curr.clone());
            return;
        }

        curr[i] = 0;
        generatePrefixes(list, curr, i + 1);
        curr[i] = 1;
        generatePrefixes(list, curr, i + 1);
    }

    private static class PartitionThread extends Thread {
        private final int[] prefix;
        private final Integer[] weights;
        private final int[] extra;
        private final int target;
        private final int[] current;
        private final int[] sharedSolution;

        PartitionThread(int[] prefix, Integer[] weights, int[] extra,
                        int target, int[] sharedSolution) {
            this.prefix = prefix;
            this.weights = weights;
            this.extra = extra;
            this.target = target;
            this.current =  new int[weights.length];
            this.sharedSolution = sharedSolution;
        }

        public void run() {
            if (foundSolution) {
                return;
            }

            int sum = 0;
            for (int i = 0; i < prefix.length; i++) {
                current[i] = prefix[i];

                if (prefix[i] == 1) {
                    sum += weights[i];
                }
            }

            if (sum > target) {
                return;
            }

            //int[] local = new int[weights.length];
            System.arraycopy(prefix, 0, current, 0, prefix.length);

            search(prefix.length, sum);
        }

        private void search(int idx, int sum) {
            if (foundSolution) {
                return;
            }

            //Takes in one thread with first "potential" solution, if it hasn't been found before then return the copy of that array
            if (sum == target) {
                synchronized (sharedSolution) {
                    if (!foundSolution) {
                        System.arraycopy(current, 0, sharedSolution, 0, sharedSolution.length);
                        foundSolution = true;
                    }
                }
                return;
            }

            if (idx >= weights.length) {
                return;
            }
            if (sum > target) {
                return;
            }
            if (sum + extra[idx] < target) {
                return;
            }

            //--------
            if (idx < prefix.length) {
                if (prefix[idx] == 1){
                    current[idx] = 1;
                    search(idx + 1, sum + weights[idx]);
                }
                else {
                    current[idx] = 0;
                    search(idx + 1, sum);
                }
                return;
            }
            //--------

            current[idx] = 1;
            search(idx + 1, sum + weights[idx]);

            current[idx] = 0;
            search(idx + 1, sum);

        }


    }

    //--------
    private static class Item {
        int weight;
        int index;

        Item(int w, int i){
            weight = w;
            index = i;
        }
    }
    //--------


    public static String getAuthors(){
        return "Kamil Reyes and Matt Greenblatt";
    }
}
