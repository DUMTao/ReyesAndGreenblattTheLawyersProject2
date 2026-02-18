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


        //Base case if the list is empty
        if (n == 0){
            return packageWeights;
        }

        //Create array of integers from the list of package arrays as weights
        Integer[] weights = packageWeights.toArray(new Integer[0]);

        for (int w : packageWeights) { //Iterate sum of weights from list
            sum += w;
        }

        //Base case if the total is odd then stop the process early
        if (sum % 2 != 0){
            return Collections.nCopies(n, 0); //Return copy of array with 0's
        }

        //Sum =/ odd so save the half for side comparison
        int target = (int) sum / 2;
        Arrays.sort(weights, Collections.reverseOrder()); //Sort in descending order

        //Populate extra array
        int[] extra = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            extra[i] = extra[i + 1] + extra[i];
        }

        //Arrays for later ;)
        int[] solution = new int[n];
        int[] current = new int[n];


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
            PartitionThread t = new PartitionThread(prefix, weights, extra, target, current, solution);
            threads.add(t);
            t.start();
        }

        //"This is just saying uhhhh make sure there isnt an error but just assigning the solution, basically I'm pretty sure" - Matt
        //Wait until all threads are done, if errors continue :)
        for (Thread t : threads) {
            try { t.join(); }
            catch (InterruptedException ignored) {}
        }

        List<Integer> result = new ArrayList<>();
        for (int x : solution) result.add(x);
        return result;
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
        private final int[] sharedCurrent;
        private final int[] sharedSolution;

        PartitionThread(int[] prefix, Integer[] weights, int[] extra,
                        int target, int[] sharedCurrent, int[] sharedSolution) {
            this.prefix = prefix;
            this.weights = weights;
            this.extra = extra;
            this.target = target;
            this.sharedCurrent = sharedCurrent;
            this.sharedSolution = sharedSolution;
        }

        public void run() {
            if (foundSolution) {
                return;
            }

            int sum = 0;
            for (int i = 0; i < prefix.length; i++) {
                if (prefix[i] == 1) {
                    sum += weights[i];
                }
            }

            if (sum > target) {
                return;
            }

            int[] local = new int[weights.length];
            System.arraycopy(prefix, 0, local, 0, prefix.length);

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
                        System.arraycopy(sharedCurrent, 0, sharedSolution, 0, sharedSolution.length);
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

            sharedCurrent[idx] = 1;
            search(idx + 1, sum + weights[idx]);

            sharedCurrent[idx] = 0;
            search(idx + 1, sum);
        }


    }








    //Size of weights list
    //n = size of original weight list
    //int index
    //int sum = sum of all the weights
    //double target =  sum / 2

    //Arrays for Weights, Current State and Solution (?)
    //int weight = new List[n] --og weights
    //int current = new Array[n] --current state of weights
    //int solution = new Array[n] --solution array before converted to list
    //int extra = new Array[weight.size + 1] --sorted array by greater -> lesser

    //Populate the extra array with the same values as weighted after sorting
    //for (int i = weight.size - 1; i >= 0; i--){
    //  extra[i] = extra[i+1] + weights[i];
    //}

    //Base Cases when it's greater, sum is greater or when the sum if less than target
    /*
    * If (sum == target){
    *   return true;
    * }
    *
    * If (sum > target){
    *   return false;
    * }
    *
    * If (sum + extra[index] < target){
    * return false;
    * }
    *
    * If (index > weight.length){
    *   return false;
    * }
    *
    */

    //Base cases are completed so then recursion time
    /*
    * current[index] = 1;
    * If (<method name>(index + 1, sum + weights[index], target, extra, current, solution){
    * return true;
    * }
    *
    * current[index] = 0;
    * if (<method name>(index + 1, sum, target, extra, current, solution)){
    * return true;
    * }
    *
    * */


    //Takes a list of integers that if the weight of the packages that returns the solution list
    /*
    * public static List<Integer> getPartition(List<Integer> packageWeights){
    *   int n = weights.size or .length
    *       Base case if the list is empty
    *   if (n == 0){
    *       return empty list;
    *   }
    *   int extra = weightsList.toArray(new int[0])
    *   double sum = sum of weights
    *       Base case if the total is odd then stop the process early
    *   if (sum % 2 != 0){
    *       return "hey you can't balance this twin you better walk ✌️😭"
    *   }
    *
    *   target = sum / 2;
    *
    *   //<Insert thread knowledge here>
    *
    * }
    *
    */
    public static String getAuthors(){
        return "Kamil Reyes and Matt Greenblatt";
    }
}
