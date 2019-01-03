package ddos_marking;

// Java program to generate all unique 
// partitions of an integer 
import java.util.ArrayList;

public class GFG {
    // Function to print an array p[] of size n 

     ArrayList<ArrayList<Integer>> Comb = new ArrayList<>();
     ArrayList<ArrayList<Integer>> Perm = new ArrayList<>();
     int k = 4;
     int b = 10;

     void printArray(int p[], int n) {
        ArrayList<Integer> a = new ArrayList<>();
        for (int i = 0; i < n; i++) {

            a.add(p[i]);
        }

        while (a.size() < k) {
            a.add(0);
        }
        Comb.add(a);
        // System.out.println(a);
    }

    // Function to generate all unique partitions of an integer 
     void printAllUniqueParts(int n) {
        int[] p = new int[n]; // An array to store a partition 
        int k = 0; // Index of last element in a partition 
        p[k] = n; // Initialize first partition as number itself 

        // This loop first prints current partition, then generates next 
        // partition. The loop stops when the current partition has all 1s 
        while (true) {
            // print current partition 
            if ((k + 1) <= this.k) {
                printArray(p, k + 1);
            }

            // Generate next partition 
            // Find the rightmost non-one value in p[]. Also, update the 
            // rem_val so that we know how much value can be accommodated 
            int rem_val = 0;
            while (k >= 0 && p[k] == 1) {
                rem_val += p[k];
                k--;
            }

            // if k < 0, all the values are 1 so there are no more partitions 
            if (k < 0) {
                return;
            }

            // Decrease the p[k] found above and adjust the rem_val 
            p[k]--;
            rem_val++;

            // If rem_val is more, then the sorted order is violeted. Divide 
            // rem_val in differnt values of size p[k] and copy these values at 
            // different positions after p[k] 
            while (rem_val > p[k]) {
                p[k + 1] = p[k];
                rem_val = rem_val - p[k];
                k++;
            }

            // Copy rem_val to next position and increment position 
            p[k + 1] = rem_val;
            k++;
        }
    }

    // Returns true if str[curr] does not matches with any of the  
// characters after str[start]  
     boolean shouldSwap(int str[], int start, int curr) {
        for (int i = start; i < curr; i++) {
            if (str[i] == str[curr]) {
                return false;
            }
        }
        return true;
    }

// Prints all distinct permutations in str[0..n-1]  
     void findPermutations(int str[], int index, int n) {
        if (index >= n) {
            ArrayList<Integer> a = new ArrayList<>();

            for (int i : str) {
                a.add(i);
            }
            Perm.add(a);
            return;
        }

        for (int i = index; i < n; i++) {

            // Proceed further for str[i] only if it  
            // doesn't match with any of the characters  
            // after str[index]  
            boolean check = shouldSwap(str, index, i);
            if (check) {
                swap(str, index, i);
                findPermutations(str, index + 1, n);
                swap(str, index, i);
            }
        }
    }

     void swap(int[] str, int i, int j) {
        int c = str[i];
        str[i] = str[j];
        str[j] = c;
    }

    
     void checkPerm()
    {
        for(ArrayList<Integer> a: Perm)
        {
            if(a.size()!=this.k)
            {
                System.out.println("something went wrong for "+ this.k+ " "+ this.b+ a);
                System.exit(0);
            }
        }
    }
    // Driver program 
    public  ArrayList<ArrayList<Integer>> PermsKB(int k, int b) {
        //System.out.println("All Unique Partitions of k=" + k + " b=" + b);

        this.k = k;
        this.b = b;

        if (b == 0) {
            ArrayList<Integer> a = new ArrayList<>();
            while (a.size() < k) {
                a.add(0);
            }
            Perm.add(a);
            return Perm;
        }
        if (b < 0) {
            return Perm;
        }
        printAllUniqueParts(b);
        for (ArrayList<Integer> comb : Comb) {
            int[] str = new int[comb.size()];
            for (int i = 0; i < str.length; i++) {
                str[i] = comb.get(i);
            }
            findPermutations(str, 0, str.length);
        }
       // checkPerm();
        return Perm;

    }
    
    public static void main(String [] args)
    {
        new GFG().PermsKB(1, 1);
    }
}
