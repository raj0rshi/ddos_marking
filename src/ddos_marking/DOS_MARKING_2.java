/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.System.exit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author rajor
 */
public class DOS_MARKING_2 {

    public static HashMap<Integer, Node> Nodes;
    public static ArrayList<Edge> recEdges;
    public static HashMap<Integer, ArrayList<Edge>> EdgesAtLevel;
    public static int PackRecCount = 0;
    public static boolean[] MarkedLevel;
    public static int[] MarkedLevelPC;
    public static int[] TotalMarkedLevelPC;
    public static double[] AvgMarkedLevelPC;
    public static File output;
    public static PrintWriter out;

    public static double MP_S = .1;
    public static double MP_E = 1;
    public static double MP_INC = 0.1;

    public static void main(String[] args) throws FileNotFoundException, IOException {

        Node CreateTree = CreateTree("input2.txt");
        output = new File("output.csv");
        out = new PrintWriter(new FileWriter(output, true), true);

        //  printTree();
        // getEdgesUptoLevel(0);
        EdgesAtLevel = new HashMap<Integer, ArrayList<Edge>>();
        int L = 3;
        MarkedLevel = new boolean[L + 1];
        MarkedLevelPC = new int[L + 1];
        TotalMarkedLevelPC = new int[L + 1];
        AvgMarkedLevelPC = new double[L + 1];
        for (int i = 1; i <= L; i++) {
            ArrayList<Edge> edgeL = getEdgesUptoLevel(i);
            EdgesAtLevel.put(i, edgeL);
            System.out.println("edgeL:" + i + " Size: " + edgeL.size());
            MarkedLevel[i] = false;
            MarkedLevelPC[i] = 0;
        }
        setAllMarkingPr(MP_S);
        setAllMarkingPrAtLevel( 1.0/256.0, 0);
        setAllMarkingPrAtLevel( 4.0/256.0,1);
        setAllMarkingPrAtLevel( 32.0/256.0,2);
        setAllMarkingPrAtLevel( 64.0/256.0,3);
        boolean flag = true;
        boolean roundcomplete = false;
        int rounds = 0;
        while (flag) {
            ArrayList<Node> Children = getChildNodes();
            for (Node child : Children) {
                ArrayList<Integer> packet = new ArrayList<>();
                double rand = Math.random();
                child.Q.add(packet);
            }

            for (int i : Nodes.keySet()) {
                Node n = Nodes.get(i);

                while (n.Q.size() > 0) {
                    ArrayList<Integer> packet = n.Q.remove(0);
                    double rand = Math.random();
                    if (rand <= n.mp) {
                        packet.add(n.L);
                    }
                    if (n.P != null) {
                        n.P.Q.add(packet);
                    } else if (sendToDest(packet))//true to stop
                    {
                        System.out.println("true return from sendtodest");
                        roundcomplete = true;
                        break;
                    }

                }
                if (roundcomplete) {
                    break;
                }
            }
            if (roundcomplete) {
                rounds++;
                for (int i = 1; i < TotalMarkedLevelPC.length; i++) {
                    TotalMarkedLevelPC[i] += MarkedLevelPC[i];
                }
                // exit(0);

                int ROUND = 1000;
                clearEverything();
                roundcomplete = false;
                if (rounds % ROUND == 0) {
                    for (int i = 1; i < TotalMarkedLevelPC.length; i++) {
                        AvgMarkedLevelPC[i] = (double) TotalMarkedLevelPC[i] / (double) ROUND;
                        TotalMarkedLevelPC[i] = 0;
                    }

                    for (int i = 1; i < MarkedLevelPC.length; i++) {
                        out.print("MP:" + MP_S + "," + "L:" + i + "," + AvgMarkedLevelPC[i] + ",");
                    }
                    out.println();
                    out.flush();

                    setAllMarkingPr(MP_S);
                    printTree();

                    break;
//                    if (MP_S > MP_E) {
//                        break;
//                    }
//                    MP_S = MP_S + MP_INC;

                }
            }
        }

    }

    public static void setAllMarkingPr(double mp) {
        for (int i : Nodes.keySet()) {
            Node n = Nodes.get(i);
            n.mp = mp;
            System.out.println("Node: " + n.L + " mp changed to " + mp);
        }
    }

    public static void setmarkingrec(Node n, double mp, int L) {
        if (L == 0) {
            n.mp = mp;
            System.out.println("Node: " + n.L + " mp changed to " + mp);
        } else {
            for (Node nn : n.C) {
                setmarkingrec(nn, mp, L - 1);
            }
        }

    }

    public static void setAllMarkingPrAtLevel(double mp, int Level) {
        Node r = getRootNode();
        if (r == null) {
            System.out.println("no root found");
            return;
        }
        setmarkingrec(r, mp, Level);
    }

    public static void clearEverything() {
        for (int i = 1; i <= EdgesAtLevel.size(); i++) {
            ArrayList<Edge> edgeL = EdgesAtLevel.get(i);
            for (Edge e : edgeL) {
                e.Marked = false;
            }
            EdgesAtLevel.put(i, edgeL);
            MarkedLevel[i] = false;
            MarkedLevelPC[i] = 0;
        }
        PackRecCount = 0;
        
        for(int i: Nodes.keySet())
        {
            Node n=Nodes.get(i);
            n.Q.clear();
        }
    }

    public static Node CreateTree(String file) throws FileNotFoundException {
        Nodes = new HashMap<Integer, Node>();

        Scanner scn = new Scanner(new File(file));

        while (scn.hasNext()) {
            String line = scn.nextLine();
            StringTokenizer strtok = new StringTokenizer(line);

            int L = Integer.parseInt(strtok.nextToken());
            double mp = Double.parseDouble(strtok.nextToken());
            Node N = Nodes.get(L);

            if (N == null) {
                N = new Node(L);
                Nodes.put(L, N);
            }
            N.mp = mp;
            while (strtok.hasMoreTokens()) {
                int C = Integer.parseInt(strtok.nextToken());
                Node CN = Nodes.get(C);
                if (CN == null) {
                    CN = new Node(C);
                    Nodes.put(C, CN);
                }
                N.addChild(CN);
            }

        }

        return null;

    }

    static void printTree() {

        for (int i : Nodes.keySet()) {
            Node n = Nodes.get(i);
            System.out.print(n.L + " " + n.mp + " ->");
            for (Node c : n.C) {
                if (c.P != null) {
                    System.out.print(c.L + "<-" + c.P.L + " ");
                }
            }
            System.out.println("");
        }

    }

    public static ArrayList<Node> getChildNodes() {
        ArrayList<Node> ret = new ArrayList<Node>();
        for (int i : Nodes.keySet()) {
            Node n = Nodes.get(i);

            if (n.C.size() == 0) {
                ret.add(n);
            }
        }
        return ret;
    }

    public static Node getRootNode() {

        for (int i : Nodes.keySet()) {
            Node n = Nodes.get(i);

            if (n.P == null) {
                return n;
            }
        }
        return null;
    }

    public static ArrayList<Edge> getEdgesUptoLevel(int Level) {

        Node r = getRootNode();
        if (r == null) {
            System.out.println("no root found");
            return null;
        }

        ArrayList<Node> nodes = new ArrayList<Node>();
        ArrayList<Edge> edges = new ArrayList<Edge>();
        nodes.add(r);
        ArrayList<Node> next_nodes = new ArrayList<Node>();
        while (!nodes.isEmpty() && Level >= 0) {
            Node n = nodes.remove(0);
            // System.out.println(n.L + "->");
            if (n.P != null) {
                Edge e = new Edge(n.P.L, n.L);
                edges.add(e);
            }

            for (Node children : n.C) {
                // System.out.println(children.L);
                next_nodes.add(children);
            }
            //System.out.println(n.C.size());
            if (nodes.size() == 0) {
                Level--;
                nodes = next_nodes;
                next_nodes = next_nodes = new ArrayList<Node>();
            }

        }

        return edges;
    }

    private static boolean sendToDest(ArrayList<Integer> packet) {
        PackRecCount++;
        // if(PackRecCount>5) return true;
        boolean ret = true;
        System.out.println(packet);
        if (packet.size() > 1) {
            for (int i = 1; i < packet.size(); i++) {
                Edge e = new Edge(packet.get(i - 1), packet.get(i));
                for (int Level : EdgesAtLevel.keySet()) {

                    if (MarkedLevel[Level]) {
                        continue;
                    }
                    ArrayList<Edge> edgesL = EdgesAtLevel.get(Level);
                    markeEdge(e, edgesL);
                    if (isAllMarked(edgesL)) {
                        System.out.println(PackRecCount + " Level:" + Level + " Marked");
                        MarkedLevel[Level] = true;
                        MarkedLevelPC[Level] = PackRecCount;
                    } else {
                        ret = false;
                    }
                }

            }
        } else {
            ret = false;
        }

        return ret;
    }

    private static void markeEdge(Edge e, ArrayList<Edge> edgesL) {

        for (Edge ee : edgesL) {
            //  System.out.println(ee.A+"-"+ee.B +" and "+e.A+"-"+e.B );
            if (ee.isEqual(e)) {
                ee.Marked = true;
                //  System.out.println("marked edge:" +ee.A+"-"+ee.B);
            }

        }
    }

    private static boolean isAllMarked(ArrayList<Edge> edgesL) {

        for (Edge ee : edgesL) {
            if (!ee.Marked) {
                return false;
            }
        }
        return true;
    }

    private static class Edge {

        int A;
        int B;
        boolean Marked;

        public Edge() {
            A = -1;
            B = -1;
            Marked = false;
        }

        public Edge(int A, int B) {
            this.A = A;
            this.B = B;
            Marked = false;
            //    System.out.println(A + "-" + B);
        }

        public boolean isEqual(Edge e) {
            if ((A == e.A) && (B == e.B)) {
                return true;
            }
            if ((B == e.A) && (A == e.B)) {
                return true;
            }

            return false;

        }
    }

}
