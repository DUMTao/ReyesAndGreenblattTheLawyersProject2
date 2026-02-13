/**
 * Returns the authors' names.
 * @return  Kamil Reyes and Greenblatt
 */

import java.util.*;

public class SaddlebagBalancer {
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
    public static List<Integer> getPartition(List<Integer> packageWeights){

    }

    public static String getAuthors(){
        return "Kamil Reyes and Matt Greenblatt";
    }
}
