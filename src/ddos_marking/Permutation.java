package ddos_marking;

// Java program to print all combination of size r in an array of size n
import java.io.*;
import java.util.ArrayList;

public class Permutation {

    static double min_cost = Integer.MAX_VALUE;
    static ArrayList<Integer> optimal_f;
    static Optimization1 opt1;

    static void combinationUtil(int arr[], int data[], int start,
            int end, int index, int r) {
        // Current combination is ready to be printed, print it
        if (index == r) {
            ArrayList<Integer> Comb = new ArrayList<Integer>();
            for (int j = 0; j < r; j++) {
                Comb.add(data[j]);
            }

            double cost = opt1.CalculateCost(Comb);
            //  System.out.println(Comb);
            //  System.out.println(cost);
            if (cost < min_cost) {
                min_cost = cost;
                optimal_f = new ArrayList<Integer>(Comb);
            }
            return;
        }

        // replace index with all possible elements. The condition
        // "end-i+1 >= r-index" makes sure that including one element
        // at index will make a combination with remaining elements
        // at remaining positions
        for (int i = start; i <= end && end - i + 1 >= r - index; i++) {
            data[index] = arr[i];
            combinationUtil(arr, data, i + 1, end, index + 1, r);
        }
    }

    // The main function that prints all combinations of size r
    // in arr[] of size n. This function mainly uses combinationUtil()
    static void printCombination(int arr[], int n, int r) {
        // A temporary array to store all combination one by one
        int data[] = new int[r];

        // Print all combination using temprary array 'data[]'
        combinationUtil(arr, data, 0, n - 1, 0, r);
    }

    /*Driver function to check for above function*/
    public static void main(Node ROOT, int B) throws IOException {

        // int B = 4;
        //Node ROOT = ddos_marking.CreateRandomTree.readSubtree("test.txt");
        //ddos_marking.graphics.DisplaySimpleTree.DrawTree(ROOT);
        opt1 = new Optimization1(ROOT);
        int arr[] = new int[opt1.Nodes.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = opt1.NodesTopToButtom.get(i).L;
        }

        File f = new File("output.txt");
        PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f, true)));

        ArrayList<Integer> F_JieWu = opt1.FindAssgnmentJieWu(B);
        double jiewu_cost = opt1.CalculateCost(F_JieWu);

        ArrayList<Integer> F_Greedy = opt1.FindAssignment(B);
        printCombination(arr, arr.length, B);
        double greedy_cost = opt1.CalculateCost(F_Greedy);

        ArrayList<Integer> F_Naive = opt1.NaiveAssignment(B);
        double naive_cost = opt1.CalculateCost(F_Naive);

        pw.print("G cost:\t" + greedy_cost + "\t");
        // System.out.println("F:" + F_Greedy);
        pw.print("Opt cost:\t" + min_cost + "\t");
        //  System.out.println("F:" + optimal_f);

        pw.print("jie cost:\t" + jiewu_cost + "\t");
        // System.out.println("F:" + F_JieWu);
        pw.print("Naive cost:\t" + naive_cost + "\t");

        pw.println("");

        System.out.print("G cost:\t" + greedy_cost + "\t");
        // System.out.println("F:" + F_Greedy);
        System.out.print("Opt cost:\t" + min_cost + "\t");
        //  System.out.println("F:" + optimal_f);

        System.out.print("jie cost:\t" + jiewu_cost + "\t");
        // System.out.println("F:" + F_JieWu);
        System.out.print("Naive cost:\t" + naive_cost + "\t");

        System.out.println("");
        pw.close();

    }

    public static void main(String[] args) throws IOException {

        int B = 5;
        Node ROOT = ddos_marking.CreateRandomTree.readSubtree("topologies\\test_tree.txt");
        ddos_marking.graphics.DisplaySimpleTree.DrawTree(ROOT);

        opt1 = new Optimization1(ROOT);
        int arr[] = new int[opt1.Nodes.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = opt1.NodesTopToButtom.get(i).L;
        }

        ArrayList<Integer> F_JieWu = opt1.FindAssgnmentJieWu(B);
        double jiewu_cost = opt1.CalculateCost(F_JieWu);
        //System.out.println("jie wu finished");
        ArrayList<Integer> F_Greedy = opt1.FindAssignment(B);
        //  System.out.println("greedy finished");

        ArrayList<Integer> F_Naive = opt1.NaiveAssignment(B);
        double naive_cost = opt1.CalculateCost(F_Naive);
        printCombination(arr, arr.length, B);
        System.out.println(optimal_f);
        
        //  System.out.println("opt finished");
        double greedy_cost = opt1.CalculateCost(F_Greedy);
        System.out.print("G cost:\t" + greedy_cost + "\t");
        // System.out.println("F:" + F_Greedy);
        System.out.print("Opt cost:\t" + min_cost + "\t");
        //  System.out.println("F:" + optimal_f);

        System.out.print("jie cost:\t" + jiewu_cost + "\t");
        System.out.print("Naive cost:\t" + naive_cost + "\t");
        // System.out.println("F:" + F_JieWu);

        System.out.println("");
    }

}


/* This code is contributed by Devesh Agrawal */
