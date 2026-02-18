import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.lang.*;
import java.util.concurrent.Callable;

    /**
     * Code to test submitted solutions for balancing bike messenger saddlebags.
     *
     * @author Kyle Burke
     */
    public class SaddlebagBalancerTester extends SpeedGrader.CodeTester {

        private static final int MAX_SCORE = 100;

        private static final double MULTIPLIER_TO_TIME_OUT = 5.0;

        private static final int[] SIZES = new int[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40};

        public SaddlebagBalancerTester() {
            //no state
        }

        public String getCodeAuthors() {
            return SaddlebagBalancer.getAuthors();
        }

        /**
         * Runs the tests and prints feedback if asserts are enabled.
         */
        public Long call() {

            //System.out.println("Testing SaddlebagBalancer test on a list of size " + N + ".");
            Random random = new Random();

            int n = this.problemSize-1; //one less than the actual total length of the list.
            long sumA = 0;
            long sumB = 0;

            List<Integer> ints = new ArrayList<Integer>();
            while (ints.size() < n) {
                int nextInt = random.nextInt(65535);
                ints.add(nextInt);
            }

            for (int i = 0; i < ints.size()/2; i++) {
                sumA += ints.get(i);
            }

            for (int i = ints.size()/2; i < ints.size(); i++) {
                sumB += ints.get(i);
            }

            ints.add((int) Math.abs(sumA - sumB));
            long half =  Math.max(sumA, sumB);
            Collections.shuffle(ints);
            List<Integer> intsCopy = new ArrayList<>();
            intsCopy.addAll(ints);


            long startTime = getSystemMillis();
            List<Integer> partition = SaddlebagBalancer.getPartition(intsCopy);
            long stopTime = getSystemMillis();

            boolean test = ints.size() == partition.size();
            String message = "The parition list isn't the same length as the list of integers: 0 points.";
            assert test : message;


            long partitionSum = 0;
            for (int i = 0; i < partition.size(); i++) {
                if (partition.get(i) == 1) {
                    partitionSum += ints.get(i);
                }
            }

            test = partitionSum == half;
            message = "The partition doesn't separate the list into two equal parts: 0 points.";
            assert test : message;

            long time = stopTime-startTime;
            //System.out.println("Your code ran in " + time + " milliseconds.");
            return time;

        }

        public static void main(String[] args) {
            SpeedGrader grader = new SpeedGrader();
            SpeedGrader.CodeTester tester = new SaddlebagBalancerTester();
            grader.runTests(tester, MULTIPLIER_TO_TIME_OUT, MAX_SCORE, SIZES);
        }
    }
