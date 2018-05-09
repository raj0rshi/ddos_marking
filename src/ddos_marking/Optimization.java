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
        CalculateLCA();

        L = new double[Math.max(this.N, MaxL + 1)];

    }

    void PrintN() {
        //System.out.println(N);
        //System.out.println(NodesTopToButtom.size());
    }

    void PrintLCA() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print("(" + i + "," + j + ")" + LCA[i][j] + " ");
            }
            System.out.println("");
        }
    }

    public int findLCA(int a, int b) {
        Node A = Nodes.get(a);
        Node B = Nodes.get(b);

        ArrayList<Integer> PA = new ArrayList<Integer>();

        ArrayList<Integer> PB = new ArrayList<Integer>();

        while (A.L != root.L) {
            PA.add(A.L);
            A = A.P;
        }
        while (B.L != root.L) {
            PB.add(B.L);
            B = B.P;
        }
        int lca = root.L;
        if (PA.isEmpty()) {
            return lca;
        }
        if (PB.isEmpty()) {
            return lca;
        }
        while (PA.get(PA.size() - 1) == PB.get(PB.size() - 1)) {
            lca = PA.get(PA.size() - 1);
            PA.remove(PA.size() - 1);
            PB.remove(PB.size() - 1);
            if (PA.isEmpty()) {
                return lca;
            }
            if (PB.isEmpty()) {
                return lca;
            }
        }
        return lca;
    }

    public void CalculateLCA() {

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                this.LCA[i][j] = findLCA(i, j);
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
        //  System.out.println("***************************");
        // CalculateLCA();
        // CalculateD(root, B);
        //PrintLCA();
        ArrayList<Integer> g = new ArrayList<Integer>();
        for (Node n : NodesTopToButtom) {
            if (n.AL > 0) {
                g.add(n.L);
                n.BAT = n.AL;
                //  System.out.println(n.L + ".AL=" + n.AL);
                //  System.out.println(n.L + ".BAT=" + n.BAT);
            } else {
                n.BAT = 0;
            }
        }
        //System.out.println("in find ass g:" + g.size());

        while (g.size() > B) {
            double min_p = Double.MAX_VALUE;
            int min_i = Integer.MAX_VALUE, min_j = Integer.MAX_VALUE;
            for (int i = 0; i < g.size(); i++) {
                for (int j = 0; j < g.size(); j++) {
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
            Node X1 = Nodes.get(g.get(min_i));
            Node X2 = Nodes.get(g.get(min_j));
            A.BAT = X1.BAT + X2.BAT;

            //System.out.println("merging: " + g.get(min_i) + " & " + g.get(min_j));
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

    public ArrayList<Integer> FindAssignment2(int B) {
        //  System.out.println("***************************");
        // CalculateLCA();
        // CalculateD(root, B);
        //PrintLCA();
        ArrayList<Integer> g = new ArrayList<Integer>();
        for (Node n : NodesTopToButtom) {
            n.AL = 0;
            for (User u : n.U) {
                if (!u.isLegit) {
                    n.AL += 1;
                }
            }
            if (n.AL > 0) {
                g.add(n.L);
                n.BAT = n.AL;

            } else {
                n.BAT = 0;
            }
            System.out.println(n.L + ".AL=" + n.AL);
            System.out.println(n.L + ".BAT=" + n.BAT);
        }
        //System.out.println("in find ass g:" + g.size());

        double total_cost = 0;
        while (g.size() > B) {
            System.out.println("************************************");

            for (int x : g) {
                Node n = Nodes.get(x);
                System.out.print(n.L + ".BAT=" + n.BAT + " ");
            }
            System.out.println("");
            double min_p = Double.MAX_VALUE;
            int min_i = Integer.MAX_VALUE, min_j = Integer.MAX_VALUE;
            for (int i = 0; i < g.size(); i++) {
                for (int j = 0; j < g.size(); j++) {
                    if (i == j) {
                        continue;
                    }

                    double p = Penalty(g.get(i), g.get(j));
                    System.out.println("penalty of " + "(" + g.get(i) + "," + g.get(j) + ")=" + p);
                    if (p < min_p) {
                        min_p = p;
                        min_i = i;
                        min_j = j;
                    }
                }

            }
            System.out.println("merging: " + g.get(min_i) + " & " + g.get(min_j));

            Node A = Nodes.get(LCA[g.get(min_i)][g.get(min_j)]);
            Node X1 = Nodes.get(g.get(min_i));
            Node X2 = Nodes.get(g.get(min_j));
            A.BAT = X1.BAT + X2.BAT;
            total_cost += min_p;

            //
            g.remove(min_i);
            if (min_i < min_j) {
                g.remove(min_j - 1);
            } else {
                g.remove(min_j);
            }
            g.add(A.L);

            System.out.println(g + "cost:" + total_cost);

        }
        System.out.println("total cost:" + total_cost);
        return g;
    }

    public void CalculateDP(int B) {

        //  //System.out.println("dp calculating");
        R2 = new Object[Math.max(this.N, MaxL + 1)][B + 1];
        A = new double[Math.max(this.N, MaxL + 1)][B + 1];
        R = new int[Math.max(this.N, MaxL + 1)][B + 1][SYSTEM_VARIABLE.RANDOM_TREE_MAXDEGREE + 1];

        for (int i = NodesTopToButtom.size() - 1; i >= 0; i--) {
            NodesButtomToTop.add(NodesTopToButtom.get(i));
        }

        for (Node n : NodesButtomToTop) {
            L[n.L] = 0;
            for (Node c : n.C) {
                L[n.L] += L[c.L];
            }
            L[n.L] += n.UL;
        }
        initA();
        for (int i = 0; i < NodesButtomToTop.size(); i++) {
            for (int j = 0; j <= B; j++) {
                Node n = NodesButtomToTop.get(i);
                int I = n.L;
                if (n.C.size() == 0) // leaf node
                {
                    A[I][j] = (1 - OMEGA) * n.UL;
                    for (int k = 0; k < (RANDOM_TREE_MAXDEGREE + 1); k++) {
                        R[I][j][k] = 0;

                    }
                    if ((j == 0) && (n.AL > 0)) {
                        A[I][j] = 100000000000.0;
                    }
                    if ((j == 0) && (n.AL <= 0)) {
                        A[I][j] = 0.0;
                    }
                    //  //System.out.println(I + "-" + j + ":" + A[I][j]);

                    R[I][j][RANDOM_TREE_MAXDEGREE] = j;
                } else// non leaf nodes
                {
                    L[n.L] += n.UL;
                    double min_cost = 1000000000.0;
                    //option 2
                    double p1 = findOptimalAssignmentWithOoutBlock(n, j);

                    double p2 = L[n.L];
                    ////System.out.println("(" + I + "," + j + ")" + "p1:" + p1 + "p2:" + p2);
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
                    //option 1
                    //  //System.out.println("option 1");

                    if (n.AL == 0) {
                        for (int k1 = j; k1 >= 0; k1--) {
                            for (int k2 = j; k2 >= 0; k2--) {
                                for (int k3 = j; k3 >= 0; k3--) {
                                    for (int k4 = j; k4 >= 0; k4--) {
                                        if ((k1 + k2 + k3 + k4) == j) {
                                            p = 0;
                                            if (n.C.size() > 0) {
                                                p += A[n.C.get(0).L][k1];
                                                //  //System.out.println("first " + I);
                                            }
                                            if (n.C.size() > 1) {
                                                p += A[n.C.get(1).L][k2];
                                                ////System.out.println("2nd " + I);
                                            }
                                            if (n.C.size() > 2) {
                                                p += A[n.C.get(2).L][k3];
                                                //  //System.out.println("3rd " + I);
                                            }
                                            if (n.C.size() > 3) {
                                                p += A[n.C.get(3).L][k4];
                                                //    //System.out.println("4th " + I);
                                            }
                                            if (p < min_cost) {
                                                min_cost = p;
                                                R[I][j][0] = k1;
                                                R[I][j][1] = k2;
                                                R[I][j][2] = k3;
                                                R[I][j][3] = k4;
                                                R[I][j][4] = 0;
                                                //   //System.out.println("option 1 cost:" + p);
                                                A[I][j] = min_cost;
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                    // //System.out.println("option 2");

                }
            }
        }
//        //System.out.println("dp calculating done");
//        //System.out.println("A");
//        printA();
//        //System.out.println("R");
//        printR();
    }

    private ArrayList<Node> getButtomUPNodes(Node root) {

        //  //System.out.println("starting buttom up");
        ArrayList<Node> N = new ArrayList<Node>();

        ArrayList<Node> NN = new ArrayList<Node>();
        ArrayList<Node> R = new ArrayList<Node>();
        N.add(root);

        while (!N.isEmpty()) {
            Node n = N.remove(0);
            NN.add(n);
            N.addAll(n.C);
            // //System.out.println(n.L);
        }

        for (int i = NN.size() - 1; i >= 0; i--) {
            R.add(NN.get(i));
        }
        // //System.out.println("finishing buttom up");
        return R;
    }

    int findMAX(HashMap<Integer, Double> A, ArrayList<Integer> g) {
        double max = Double.MIN_VALUE;
        int max_i = -1;
        for (int i : A.keySet()) {
            double b = A.get(i);
            if ((max < b) && (g.indexOf(i) < 0)) {
                max = b;
                max_i = i;
            }
        }
        return max_i;
    }

    void PrintBenefit(HashMap<Integer, Double> A) {
        for (int i : A.keySet()) {
            double b = A.get(i);
            //System.out.print(i + "-" + b + ", ");
        }
        //System.out.println("");
    }

    private double findOptimalAssignmentWithOoutBlock(Node root, int j) {
        //  //System.out.println("find op ass called at N:" + root.L + " B:" + j);

        ArrayList<Node> NODES = getButtomUPNodes(root);
//        //System.out.println("buttom up nodes: ");
//
//        for (Node n : NODES) {
//            //System.out.print(n.L + " ");
//        }
//        //System.out.println("******");
        ArrayList<Integer> g = new ArrayList<Integer>();
        HashMap<Integer, Double> BENEFIT = new HashMap<Integer, Double>();
        for (Node n : NODES) {
            BENEFIT.put(n.L, Double.MIN_VALUE);
        }
        for (Node n : NODES) {
            n.BAT = n.AL;
            for (Node c : n.C) {
                n.BAT += c.BAT;
            }
            //  //System.out.print(n.L + "(" + n.BAT + ")");
            BENEFIT.put(n.L, n.BAT * (D[n.L] - D[root.L]));
        }
        // //System.out.println("");
        double cost = 0;
        for (Node n : NODES) {
            if (n.AL > 0) {
                cost += n.AL * (D[n.L] - D[root.L]);
            }
        }

        double benefit = 0;
        g.add(root.L);
        int size = 1;
        // PrintBenefit(BENEFIT);
        while (size < (j - 1)) {
            //   //System.out.println("in while g:" + g.toString());
            int a = findMAX(BENEFIT, g);
            if (a != -1) {
                double b = BENEFIT.remove(a);

                reduceBAT(Nodes.get(a), root, g);

                g.add(a);
                benefit += b;

                for (Node n : NODES) {
                    Double new_b = calculateDistance(n, root, g) * n.BAT;
                    BENEFIT.put(n.L, new_b);
                }
                //      PrintBenefit(BENEFIT);
            }
            size++;

        }

        //  //System.out.println("g" + g.toString());
        R2[root.L][j] = g;
        // //System.out.println("benefit: " + benefit + " cost:" + cost + " return:" + (cost - benefit));
        if ((j == 0) && (root.BAT > 0)) {

            g.clear();
            return 10000000.0;
        }

        if ((j == 1) && (root.BAT > 0)) {

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
            //System.out.println("calling reduce bat with null node");
        }
        if (root == null) {
            //System.out.println("calling reduce bat with null root");
        }
        Node nn = n;
        int d = 0;
        //   //System.out.println("N: " + nn.L + " root:" + root.L);
        while (nn.L != root.L) {

//            //System.out.println(nn.L + " reduced by " + n.BAT);
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
        // System.out.println("finding dp assignment");
       // System.out.println(NodesTopToButtom.size());
        for (Node n : NodesTopToButtom) {
            for (User u : n.U) {
                if (u.isLegit) {
                    n.UL++;
                } else {
                    n.AL++;
                }
            }
         //   System.out.print(n.L + "[" + n.AL + "," + n.UL + "] ");
        }
      //  System.out.println("");
        CalculateDP(B);
        printA();

        ArrayList<AssTask> AsTsk = new ArrayList<AssTask>();
        ArrayList<Integer> g = new ArrayList<Integer>();

        AssTask at = new AssTask(root, B);
        AsTsk.add(at);
        while (!AsTsk.isEmpty()) {
            AssTask a = AsTsk.remove(0);

            //System.out.println("Loopup Node:" + a.N.L + " B:" + a.B);
            if (R[a.N.L][a.B][RANDOM_TREE_MAXDEGREE] > 0) {
                ArrayList<Integer> gx = (ArrayList<Integer>) R2[a.N.L][a.B];
                if (gx != null) {

                    //    //System.out.println("added to g: " + gx.toString());
                } else {
                    findOptimalAssignmentWithOoutBlock(a.N, a.B);
                    gx = (ArrayList<Integer>) R2[a.N.L][a.B];
                    //   //System.out.println("null non bloock assignment: " + a.N.L + " B:" + a.B);
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
        //    //System.out.println("finding dp assignment done");
        return g;
    }

    ArrayList<Integer> FindDPAssignment2(int B) {
        System.out.println("**********************finding dp assignment*********************************");

        System.out.println(NodesTopToButtom.size());
        for (Node n : NodesTopToButtom) {
            for (User u : n.U) {
                if (u.isLegit) {
                    n.UL++;
                } else {
                    n.AL++;
                }
            }
            System.out.print(n.L + "[" + n.AL + "," + n.UL + "]");
        }

        ArrayList<AssTask> AsTsk = new ArrayList<AssTask>();
        ArrayList<Integer> g = new ArrayList<Integer>();

        AssTask at = new AssTask(root, B);
        AsTsk.add(at);
        while (!AsTsk.isEmpty()) {
            AssTask a = AsTsk.remove(0);

            //System.out.println("Loopup Node:" + a.N.L + " B:" + a.B);
            if (R[a.N.L][a.B][RANDOM_TREE_MAXDEGREE] > 0) {
                ArrayList<Integer> gx = (ArrayList<Integer>) R2[a.N.L][a.B];
                if (gx != null) {

                    //    //System.out.println("added to g: " + gx.toString());
                } else {
                    findOptimalAssignmentWithOoutBlock(a.N, a.B);
                    gx = (ArrayList<Integer>) R2[a.N.L][a.B];
                    //   //System.out.println("null non bloock assignment: " + a.N.L + " B:" + a.B);
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
        //    //System.out.println("finding dp assignment done");
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
            //   //System.out.println("");
        }
    }

    private void printR() {
        for (int i = 0; i < R.length; i++) {
            for (int j = 0; j < R[i].length; j++) {

                //System.out.print("(" + i + "," + j + ")[");
                for (int k = 0; k < R[i][j].length; k++) {

                    //System.out.print(R[i][j][k] + " ");
                }
                //System.out.print("] ");

            }
            //System.out.println("");
        }
    }

    public ArrayList<Integer> NaiveAssignment(int B) {

        ArrayList<Node> NODES = getButtomUPNodes(root);
        for (Node n : NODES) {
            n.BAT = n.AL;
            for (Node c : n.C) {
                n.BAT += c.BAT;
            }
        }
        ArrayList<Node> CA = new ArrayList<Node>();
        CA.add(root);
        int size = CA.size();
        while (size < B) {
            double max_BAT = Double.MIN_VALUE;
            int max_i = -1;
            for (int i = 0; i < CA.size(); i++) {
                Node n = CA.get(i);
                if (max_BAT < n.BAT) {
                    max_BAT = n.BAT;
                    max_i = i;
                }

            }
            // System.out.println("max_bat_i:"+max_i +" max bat:"+ max_BAT);
            if (max_i >= 0) {
                Node removed = CA.remove(max_i);
                //    System.out.println(removed.L+" removed");
                size--;
                removed.BAT = -1;
                if (removed.AL > 0) {
                    //           System.out.println(removed.L+" added back");
                    CA.add(removed);
                    size++;
                }
                if ((size + removed.C.size()) > B) {
                    //        System.out.println("breaking");
                    ArrayList<Integer> g = new ArrayList<Integer>();
                    for (Node n : CA) {
                        g.add(n.L);
                    }
                    return g;

                } else {
                    CA.addAll(removed.C);
                }

            } else {
                ArrayList<Integer> g = new ArrayList<Integer>();
                for (Node n : CA) {
                    g.add(n.L);
                }
                return g;
            }
            size = CA.size();
        }
        ArrayList<Integer> g = new ArrayList<Integer>();
        for (Node n : CA) {
            g.add(n.L);
        }
        return g;
    }

}
