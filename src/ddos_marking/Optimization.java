/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import static ddos_marking.SYSTEM_VARIABLE.OMEGA;
import static ddos_marking.SYSTEM_VARIABLE.RANDOM_TREE_MAXDEGREE;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author rajor
 */
public class Optimization {

    Node root;
    int N;
    int MaxL;
    int[][] LCA;
    int[] D;
    HashMap<Integer, Node> Nodes = new HashMap<Integer, Node>();
    ArrayList<Node> NodesTopToButtom = new ArrayList<Node>();
    ArrayList<Node> NodesButtomToTop = new ArrayList<Node>();

    double[][] A;
    int[][][] R;
    double[] L;
    Object[][] R2;
    int Budget = 0;

    public Optimization(Node root) {
        this.root = root;
        ArrayList<Node> N = new ArrayList<Node>();
        N.add(root);
        MaxL = root.L;
        while (!N.isEmpty()) {
            Node n = N.remove(0);
            NodesTopToButtom.add(n);
            Nodes.put(n.L, n);
            N.addAll(n.C);
            if (MaxL < n.L) {
                MaxL = n.L;
            }
        }
        this.N = Nodes.size();
        LCA = new int[Math.max(this.N, MaxL + 1)][Math.max(this.N, MaxL + 1)];
        D = new int[Math.max(this.N, MaxL + 1)];
        CalculateD(root, 0);

        L = new double[Math.max(this.N, MaxL + 1)];
        A = new double[Math.max(this.N, MaxL + 1)][Math.max(this.N, MaxL + 1)];
        R = new int[Math.max(this.N, MaxL + 1)][Math.max(this.N, MaxL + 1)][SYSTEM_VARIABLE.RANDOM_TREE_MAXDEGREE + 1];

    }

    void PrintN() {
        System.out.println(N);
        System.out.println(NodesTopToButtom.size());
    }

    void PrintLCA() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(LCA[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public void CalculateLCA() {

        int[][] LCA = new int[this.N][this.N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                LCA[i][j] = Integer.MAX_VALUE;
            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Node A = NodesTopToButtom.get(i);
                Node B = NodesTopToButtom.get(j);
                //     System.out.println(A.L + "-" + B.L);

                if (A.L == B.L) {
                    LCA[i][j] = A.L;
                } else if ((A.P != null) && (A.P.L == B.L)) {
                    LCA[i][j] = B.L;
                } else if ((B.P != null) && (B.P.L == A.L)) {
                    LCA[i][j] = A.L;
                } else {
                    if (i != 0) {
                        LCA[i][j] = Math.min(LCA[i - 1][j], LCA[i][j]);
                    }
                    if (j != 0) {
                        LCA[i][j] = Math.min(LCA[i][j - 1], LCA[i][j]);
                    }
                    if ((i != 0) && (j != 0)) {
                        LCA[i][j] = Math.min(LCA[i - 1][j - 1], LCA[i][j]);
                    }
                }

            }
        }

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.LCA[NodesTopToButtom.get(i).L][NodesTopToButtom.get(j).L] = LCA[i][j];
            }
        }
    }

    public void initBAT() {
        for (Node n : NodesTopToButtom) {
            n.BAT = 0;
        }
    }

    public void CalculateD(Node n, int d) {
        D[n.L] = d;
        for (Node c : n.C) {
            CalculateD(c, d + 1);
        }

    }

    void PrintD() {
        System.out.println("distance from root to any node:");
        for (int i : D) {
            System.out.print(i + " ");
        }
        System.out.println("");
    }

    public void ComputeAL(HashMap<Integer, Integer> ALs) {
        for (int i : ALs.keySet()) {
            if (Nodes.containsKey(i)) {
                Nodes.get(i).AL = ALs.get(i);
            }
        }
    }

    public void ComputeUL(HashMap<Integer, Integer> ULs) {
        for (int i : ULs.keySet()) {
            if (Nodes.containsKey(i)) {
                Nodes.get(i).UL = ULs.get(i);
            }
        }
    }

    double Penalty(int a, int b) {
        double p = 0;
        Node A = Nodes.get(a);
        Node B = Nodes.get(b);
        int lca = LCA[a][b];
        p = A.BAT * (D[a] - D[lca]) + B.BAT * (D[b] - D[lca]);
        return p;
    }

    public ArrayList<Integer> FindAssignment(int B) {
        ArrayList<Integer> g = new ArrayList<Integer>();
        for (Node n : NodesTopToButtom) {
            if (n.AL > 0) {
                g.add(n.L);
                n.BAT = n.AL;
            }
        }
        System.out.println("in find ass g:" + g.size());

        while (g.size() > B) {
            double min_p = Double.MAX_VALUE;
            int min_i = Integer.MAX_VALUE, min_j = Integer.MAX_VALUE;
            for (int i = 0; i < g.size(); i++) {
                for (int j = i; j < g.size(); j++) {
                    if (i == j) {
                        continue;
                    }
                    if (Penalty(g.get(i), g.get(j)) < min_p) {
                        min_p = Penalty(g.get(i), g.get(j));
                        min_i = i;
                        min_j = j;
                    }
                }

            }

            Node A = Nodes.get(LCA[g.get(min_i)][g.get(min_j)]);
            A.BAT += min_p;

            System.out.println("merging: " + g.get(min_i) + " & " + g.get(min_j));
            g.remove(min_i);
            if (min_i < min_j) {
                g.remove(min_j - 1);
            } else {
                g.remove(min_j);
            }
            g.add(A.L);

        }
        return g;
    }

    public void CalculateDP(int B) {

        System.out.println("dp calculating");
        R2 = new Object[Math.max(this.N, MaxL + 1)][B + 1];

        for (int i = NodesTopToButtom.size() - 1; i >= 0; i--) {
            NodesButtomToTop.add(NodesTopToButtom.get(i));
        }
        initA();
        for (int i = 0; i < NodesButtomToTop.size(); i++) {
            for (int j = 0; j <= B; j++) {
                Node n = NodesButtomToTop.get(i);
                int I = n.L;
                if (n.C.size() == 0) // leaf node
                {
                    L[I] = n.UL;
                    A[I][j] = OMEGA * n.AL + (1 - OMEGA) * n.UL;
                    for (int k = 0; k < (RANDOM_TREE_MAXDEGREE + 1); k++) {
                        R[I][j][k] = 0;

                    }
                    if ((j == 0) && (n.AL > 0)) {
                        A[I][j] = 100000000000.0;
                    }
                    if ((j == 0) && (n.AL <= 0)) {
                        A[I][j] = 0.0;
                    }
                    System.out.println(I + "-" + j + ":" + A[I][j]);

                    R[I][j][RANDOM_TREE_MAXDEGREE] = j;
                } else// non leaf nodes
                {

                    double min_cost = 1000000000.0;

                    //option 1
                    //  System.out.println("option 1");
                    for (int k1 = j; k1 >= 0; k1--) {
                        for (int k2 = j; k2 >= 0; k2--) {
                            for (int k3 = j; k3 >= 0; k3--) {
                                for (int k4 = j; k4 >= 0; k4--) {
                                    if ((k1 + k2 + k3 + k4) == j) {
                                        double p = 0;
                                        if (n.C.size() > 0) {
                                            p += A[n.C.get(0).L][k1];
                                            System.out.println("first " + I);
                                        }
                                        if (n.C.size() > 1) {
                                            p += A[n.C.get(1).L][k2];
                                            System.out.println("2nd " + I);
                                        }
                                        if (n.C.size() > 2) {
                                            p += A[n.C.get(2).L][k3];
                                            System.out.println("3rd " + I);
                                        }
                                        if (n.C.size() > 3) {
                                            p += A[n.C.get(3).L][k4];
                                            System.out.println("4th " + I);
                                        }
                                        if (p < min_cost) {
                                            min_cost = p;
                                            R[I][j][0] = k1;
                                            R[I][j][1] = k2;
                                            R[I][j][2] = k3;
                                            R[I][j][3] = k4;
                                            R[I][j][4] = 0;
                                            System.out.println("option 1 cost:" + p);
                                            A[I][j] = min_cost;
                                        }

                                    }
                                }
                            }
                        }
                    }
                    // System.out.println("option 2");
                    //option 2
                    double p1 = findOptimalAssignmentWithOoutBlock(n, j);
                    //System.out.println("p1 found");
                    double p2 = L[n.L];
                    double p = OMEGA * p1 + (1 - OMEGA) * p2;
                    if (p < min_cost) {
                        min_cost = p;
                        R[I][j][0] = 0;
                        R[I][j][1] = 0;
                        R[I][j][2] = 0;
                        R[I][j][3] = 0;
                        R[I][j][4] = j;
                        A[I][j] = min_cost;
                    }
                }
            }
        }
        System.out.println("dp calculating done");
        System.out.println("A");
        printA();
        System.out.println("R");
        printR();
    }

    private ArrayList<Node> getButtomUPNodes(Node root) {

        //  System.out.println("starting buttom up");
        ArrayList<Node> N = new ArrayList<Node>();

        ArrayList<Node> NN = new ArrayList<Node>();
        ArrayList<Node> R = new ArrayList<Node>();
        N.add(root);

        while (!N.isEmpty()) {
            Node n = N.remove(0);

            NN.add(n);
            N.addAll(n.C);
            // System.out.println(n.L);
        }

        for (int i = NN.size() - 1; i >= 0; i--) {
            R.add(NN.get(i));
        }
        // System.out.println("finishing buttom up");
        return R;
    }

    int findMAX(double[] A) {
        double max = Double.MIN_VALUE;
        int max_i = 0;
        for (int i = 0; i < A.length; i++) {
            if (max < A[i]) {
                max = A[i];
                max_i = i;
            }
        }
        return max_i;
    }

    private double findOptimalAssignmentWithOoutBlock(Node root, int j) {
        // System.out.println("find op ass called at N:" + root.L + " B:" + j);

        ArrayList<Node> NODES = getButtomUPNodes(root);
        // System.out.println("buttom up nodes: " + NODES);
        ArrayList<Integer> g = new ArrayList<Integer>();
        double[] BENEFIT = new double[MaxL + 1];
        for (int i = 0; i < BENEFIT.length; i++) {
            BENEFIT[i] = Double.MIN_VALUE;
        }
        for (Node n : NODES) {
            n.BAT = n.AL;
            for (Node c : n.C) {
                n.BAT += c.BAT;
            }
            BENEFIT[n.L] = n.BAT * (D[n.L] - D[root.L]);

        }
        double cost = 0;
        for (Node n : NODES) {
            if (n.AL > 0) {
                cost += n.AL * (D[n.L] - D[root.L]);
            }
        }

        double benefit = 0;
        while (g.size() < (j - 1)) {
            //   System.out.println("in while g:" + g.toString());
            int a = findMAX(BENEFIT);
            BENEFIT[a] = Double.MIN_VALUE;
            benefit += a;
            reduceBAT(Nodes.get(a), root, g);
            g.add(a);
            for (Node n : NODES) {
                BENEFIT[n.L] = calculateDistance(n, root, g) * n.BAT;
            }
        }

        //  System.out.println("g" + g.toString());
        R2[root.L][j] = g;
        // System.out.println("benefit: " + benefit + " cost:" + cost + " return:" + (cost - benefit));
        if ((j == 0) && (root.BAT > 0)) {

            return 10000000.0;
        }

        if ((j == 1) && (root.BAT > 0)) {
            g.add(root.L);
            return cost;
        }
        return cost - benefit;
    }

    private int calculateDistance(Node n, Node root, ArrayList<Integer> g) {
        Node nn = n;
        int d = 0;
        while (nn.L != root.L) {
            if (g.indexOf(nn.L) >= 0) {
                return d;
            } else {
                d++;
                nn = nn.P;
            }
        }
        return d;
    }

    private int reduceBAT(Node n, Node root, ArrayList<Integer> g) {
        if (n == null) {
            System.out.println("calling reduce bat with null node");
        }
        if (root == null) {
            System.out.println("calling reduce bat with null root");
        }
        Node nn = n;
        int d = 0;
        //   System.out.println("N: " + nn.L + " root:" + root.L);
        while (nn.L != root.L) {

//            System.out.println(nn.L + " reduced by " + n.BAT);
            nn.BAT -= n.BAT;
            if (g.indexOf(nn.L) >= 0) {

                break;
            }
            nn = nn.P;
            if (nn == null) {
                break;
            }
        }
        return d;
    }

    ArrayList<Integer> FindDPAssignment(int B) {
        //   System.out.println("finding dp assignment");
        ArrayList<AssTask> AsTsk = new ArrayList<AssTask>();
        ArrayList<Integer> g = new ArrayList<Integer>();

        AssTask at = new AssTask(root, B);
        AsTsk.add(at);
        while (!AsTsk.isEmpty()) {
            AssTask a = AsTsk.remove(0);

            System.out.println("Loopup Node:" + a.N.L + " B:" + a.B);
            if (R[a.N.L][a.B][RANDOM_TREE_MAXDEGREE] > 0) {
                ArrayList<Integer> gx = (ArrayList<Integer>) R2[a.N.L][a.B];
                if (gx != null) {

                    System.out.println("added to g: " + gx.toString());
                } else {
                    findOptimalAssignmentWithOoutBlock(a.N, a.B);
                    gx = (ArrayList<Integer>) R2[a.N.L][a.B];
                    System.out.println("null non bloock assignment: " + a.N.L + " B:" + a.B);
                }
                g.addAll(gx);
            } else {
                for (int i = 0; i < a.N.C.size(); i++) {
                    Node c = a.N.C.get(i);
                    at = new AssTask(c, R[a.N.L][a.B][i]);
                    AsTsk.add(at);
                }
            }

        }
        //    System.out.println("finding dp assignment done");
        return g;
    }

    class AssTask {

        int B;
        Node N;

        public AssTask(Node N, int B) {
            this.N = N;
            this.B = B;
        }
    }

    private void printA() {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {

                if (A[i][j] < 100000) {
                    System.out.print("(" + i + "," + j + ")" + A[i][j] + " ");
                } else {
                    System.out.print("(" + i + "," + j + ")" + "*" + " ");
                }
            }
            System.out.println("");
        }
    }

    private void initA() {
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A[0].length; j++) {
                A[i][j] = 10000000.0;
            }
            //   System.out.println("");
        }
    }

    private void printR() {
        for (int i = 0; i < R.length; i++) {
            for (int j = 0; j < R[i].length; j++) {

                System.out.print("[");
                for (int k = 0; k < R[i][j].length; k++) {

                    System.out.print(R[i][j][k] + " ");
                }
                System.out.print("] ");

            }
            System.out.println("");
        }
    }

}
