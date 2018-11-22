/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import static ddos_marking.SYSTEM_VARIABLE.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rajor
 */
public class Node {

    private static boolean Runing = true;

    private static int NumNodes = 0;

    
    public int L;
    public Node P;
    double mp = 0;

    double J_TL = 0;
    double J_TC = 0;
    public ArrayList<Node> C;
    public ArrayList<User> U;
    ArrayList<ArrayList<Integer>> Q;
    ArrayList<Packet> MQ;
    double attackrate = 0;
    double legitrate = 0;
    //for drawing
    public int xpos;  //stores x and y position of the node in the tree
    public int ypos;
    public Thread T;

    public double AL = 0;
    public double UL = 0;

    boolean isFilterActivated = false;
    boolean isDestFilterActivated = false;
    HashSet<Integer> BlockList;
    static volatile double Total_PACKET = 0;
    static volatile int Total_APF_Count = 0;
    static volatile int Total_LP_Count = 0;
    static volatile int Total_BAP_Count = 0;
    static volatile int Total_BLU_Count = 0;
    static volatile int Total_AP_Count = 0;
    //for optimization
    double BAT = 0;
    HashMap<Integer, Integer> CD;
    static ArrayList<Integer> g = new ArrayList<Integer>();

    public static boolean getRunningStatus() {
        return Runing;
    }

    public static void setRunningStatus(boolean run) {
        Runing = run;
    }

    public Node(int L) {
        this.L = L;
        Q = new ArrayList<ArrayList<Integer>>();
        MQ = new ArrayList<Packet>();
        C = new ArrayList<Node>();
        U = new ArrayList<User>();
        mp = MARKING_PROBABILITY;

        CD = new HashMap<Integer, Integer>();
        BlockList = new HashSet<Integer>();

        if (f.exists()) {
           // f.delete();
        }
        try {
            if (fw == null) {
                fw = new FileWriter(f, true);
            }
        } catch (IOException ex) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addChild(Node n) {
        if (!this.C.contains(n)) {
            n.P = this;
            this.C.add(n);
            CD.put(n.L, 1);
        }
    }

    public void addBlockList(int n) {
        // System.out.println("blocklist added:" + n + " to " + L);
        BlockList.add(n);
    }

    public void clearBlockList(int n) {
        BlockList.clear();
    }

    public static void incAPC() {
        Total_APF_Count++;
    }

    public static void incTPC() {
        Total_PACKET++;
    }

    public static void incBAP() {
        Total_BAP_Count++;
    }

    public static void incLPC() {
        Total_LP_Count++;
    }

    public static void incBLPC() {
        Total_BLU_Count++;
    }

    public static synchronized void incAPRC() {
        Total_AP_Count++;
    }

    public ArrayList<User> getU() {
        return U;
    }

    public synchronized void insert(Packet P) {
        if (this.MQ.size() > 10000) {
            System.out.println("packed dropped");
            return;
        }

        if (!P.isLegit) {
            Node.incAPC();
        }

        this.MQ.add(P);
        synchronized (this.T) {
            this.T.notify();
        }
    }

    public synchronized void insert2(Packet P) {
        if (this.MQ.size() > 10000) {
            System.out.println("packed dropped");
            return;
        }
        this.MQ.add(P);
        synchronized (this.T) {
            this.T.notify();
        }
    }

    public synchronized boolean forward() throws IOException {

        //System.out.println("forward");
        if (MQ.isEmpty()) {
            return false;
        }

        Packet P = MQ.get(0);
        MQ.remove(0);

        // System.out.println("Blocklist @ " + L + ":" + BlockList.toString());
//        if (isFilterActivated && !P.isLegit) {
//            Node.incBAP();
//            return true;
//        }
        if (isDestFilterActivated || (isFilterActivated && BlockList.contains(P.Src))) {
            //   System.out.println("blocked");
            if (!P.isLegit) {
                Node.incBAP();
            } else {
                Node.incBLPC();
            }
            return true;
        }

        if (Math.random() < mp) {
            P.marking.add(L);
        }
        if (this.P != null) {
            this.P.insert(P);
        } else {
//            //System.out.print("packet received:");
//            for(int ip: P.marking)
//            {
//                System.out.print(ip+ " ");
//            }
//            System.out.println("");
            ProcessByVictim(P);
        }
        return true;
    }

    public HashMap<Integer, ArrayList<Pair>> Marks = new HashMap<Integer, ArrayList<Pair>>();
    public HashMap<Integer, ArrayList<Integer>> Paths = new HashMap<Integer, ArrayList<Integer>>();
    public HashMap<Integer, Integer> AttackCount = new HashMap<Integer, Integer>();
    public HashMap<Integer, Integer> LegitCount = new HashMap<Integer, Integer>();

    public int packet_count = 0;

    private void markadd(Pair p, ArrayList<Pair> mark) {

        for (Pair pp : mark) {
            if (pp.equals(p)) {
                return;
            }
        }
        mark.add(p);
    }
    long systime = System.currentTimeMillis();
    long systime2 = System.currentTimeMillis();
    long systime3 = System.currentTimeMillis();
    long starttime = System.currentTimeMillis();
    boolean flag = true;
    boolean assigned = false;
    static File f = new File("P" + SYSTEM_VARIABLE.ASSIGNMENT_POLICY + "-B" + SYSTEM_VARIABLE.B + "-W" + SYSTEM_VARIABLE.OMEGA + "-MP" + SYSTEM_VARIABLE.MARKING_PROBABILITY + "-" + SYSTEM_VARIABLE.file + "-output.csv");
    static FileWriter fw;

    private void ProcessByVictim(Packet P) throws FileNotFoundException, IOException {
        packet_count++;

        if (P.isLegit) {
            incLPC();
            if (LegitCount.containsKey(P.Src)) {
                int C = LegitCount.get(P.Src);
                LegitCount.put(P.Src, C + 1);
            } else {
                LegitCount.put(P.Src, 1);
            }
        }
        if (!P.isLegit) {
            incAPRC();
            if (AttackCount.containsKey(P.Src)) {
                int C = AttackCount.get(P.Src);
                AttackCount.put(P.Src, C + 1);
            } else {
                AttackCount.put(P.Src, 1);
            }
        }

        if (P.marking.size() > 0) {
            int A = P.Src;
            int B = 0;

            ArrayList<Pair> mark = Marks.get(P.Src);
            if (mark == null) {
                mark = new ArrayList<Pair>();
                Marks.put(P.Src, mark);
            }
            for (int m : P.marking) {
                B = m;
                Pair pair = new Pair(A, B);
                if (A != B) {
                    markadd(pair, mark);
                }
                A = B;
            }
            Pair pair = new Pair(B, SYSTEM_VARIABLE.V);
            markadd(pair, mark);

        }
        if ((System.currentTimeMillis() - systime) > SYSTEM_VARIABLE.PATHUPDATE_INTERVAL) {

            systime = System.currentTimeMillis();
            ArrayList<Integer> path = Helper.PairToPath(Marks.get(P.Src));
            Paths.put(P.Src, path);
            //FileWriter fw = new FileWriter(f, true);
            //   AttackCount.clear();
            //  LegitCount.clear();
        }

        if (((System.currentTimeMillis() - systime2) > SYSTEM_VARIABLE.ASSIGNMENT_INTERVAL)) {
            systime2 = System.currentTimeMillis();

            Node root2 = Helper.PathsToTree(Paths);
            if (SYSTEM_VARIABLE.KNOWS_TOPOLOGY) {
                root2 = CreateRandomTree.ROOT;
            }
            if (!CreateRandomTree.hasLoop(root2) && root2 != null) {
                // System.out.println("Printing tree after " + AttackCount + " " + LegitCount);

                Optimization1 OP = new Optimization1(root2);
                NumNodes=OP.N;
                //  OP.PrintN();
                // OP.CalculateLCA();
                //  OP.PrintLCA();
                //  OP.PrintD();
                OP.ComputeAL(AttackCount);
                OP.ComputeUL(LegitCount);
                // CreateRandomTree.PrintTree(root2);

                if (SYSTEM_VARIABLE.ASSIGNMENT_POLICY == 1) {
                    System.out.println("*******P1********");
                    g = OP.FindAssignment(SYSTEM_VARIABLE.B);
                }
                if (SYSTEM_VARIABLE.ASSIGNMENT_POLICY == 2) {
                    // OP.CalculateDP(SYSTEM_VARIABLE.B);
                    System.out.println("*******P2********");
                    g = OP.FindDPAssignment(SYSTEM_VARIABLE.B);
                }
                if (SYSTEM_VARIABLE.ASSIGNMENT_POLICY == 3) {
                    System.out.println("*******P3********");
                    g = OP.NaiveAssignment(SYSTEM_VARIABLE.B);
                }
                //  System.out.println("optimal assignment: " + g.toString());

                ArrayList<Node> N = new ArrayList<Node>();
                N.add(CreateRandomTree.ROOT);
                while (!N.isEmpty()) {
                    Node n = N.remove(0);
                    N.addAll(n.C);
                    //  System.out.println(n.L + "-U:" + n.U.size());
                    if (g != null && g.contains(n.L)) {
                        for (int BIP : AttackCount.keySet()) {
                            n.addBlockList(BIP);
                            //System.out.println("added:" + BIP);
                        }
                        n.isFilterActivated = true;
                        if (SYSTEM_VARIABLE.ASSIGNMENT_POLICY == 2) {
                            n.isDestFilterActivated = true;
                        }
                    } else {

                        n.BlockList.clear();
                        n.isFilterActivated = false;
                        if (SYSTEM_VARIABLE.ASSIGNMENT_POLICY == 2) {
                            n.isDestFilterActivated = false;
                        }
                    }
                }
                // System.out.println("done1");
                // assigned = true;
            } else {
                System.out.println("tree has loop");
            }

            // System.out.println("done");
        }
    }
    int lessLUCount = 0;

    public void log() throws IOException {


        if ((System.currentTimeMillis() - starttime) > SYSTEM_VARIABLE.SIMULATION_TIME) {
            fw.flush();
            fw.close();
         //   FormatOutput.main(f);

            setRunningStatus(false);
            //  System.out.println("setting running status");
            System.exit(0);
        }
        if (flag) {
            flag = false;
            fw.append("Time,\t Total Cost,\tTotal Received Attack Packet,\t Total Blocked  Attack Packet,\tTotal Legit Packt,\t Total Blocked Legit Packet,\tfilter used,\tNum Nodes\n");
        } //            System.out.print("Path to " + P.Src + ": ");
        System.out.println("P" + SYSTEM_VARIABLE.ASSIGNMENT_POLICY + " ->Time:" + (System.currentTimeMillis() - starttime));
        System.out.println("Total Cost:" + Total_APF_Count / (Total_BAP_Count + 0.01));
        System.out.println("Total Received Attack Packet :" + Total_AP_Count);
        System.out.println("Total Blocked  Attack Packet:" + Total_BAP_Count);
        System.out.println("Total Legit Packt:" + Total_LP_Count);
        System.out.println("Total Blocked Legit Packet:" + Total_BLU_Count);
        int filter_used = 0;
        if (g != null) {
            System.out.println("Filter Used:" + g.size() + g);
            filter_used = g.size();
        } else {
            System.out.println("Filter Used: -");
        }
        
         System.out.println("Number of Nodes:" + NumNodes);
        fw.append((System.currentTimeMillis() - starttime) + ",\t");
        fw.append(Total_APF_Count + ",\t");
        fw.append(Total_AP_Count + ",\t");
        fw.append(Total_BAP_Count + ",\t");
        fw.append(Total_LP_Count + ",\t");
        fw.append(Total_BLU_Count + ",\t");
        fw.append(filter_used + ",\t");
        fw.append(NumNodes + ",\t");
        fw.append("\n");

        //clear assignment
        if (false && (Total_LP_Count < 700) && (SYSTEM_VARIABLE.ASSIGNMENT_POLICY == 2)) {

            lessLUCount++;
            if (lessLUCount > 5) {

                lessLUCount = 0;
                System.out.println("filter cleared");
                ArrayList<Node> N = new ArrayList<Node>();
                N.add(CreateRandomTree.ROOT);
                while (!N.isEmpty()) {
                    Node n = N.remove(0);
                    N.addAll(n.C);
                    n.isDestFilterActivated = false;
                    n.isFilterActivated = false;
                    n.BlockList.clear();
                }
            }
        } else {
            lessLUCount = 0;
        }

        //      refresh counts
        Total_APF_Count = 0;
        Total_AP_Count = 0;
        Total_BAP_Count = 0;
        Total_LP_Count = 0;
        Total_BLU_Count = 0;
        Total_PACKET = 0;
    }

    @Override
    public String toString() {
        return L + "";
    }
}
