/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import static ddos_marking.SYSTEM_VARIABLE.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rajor
 */
public class CreateRandomTree {

    static int label = 0;
    static Node ROOT;

    static ArrayList<Thread> Threads = new ArrayList<Thread>();

    public static void main(String[] args) throws IOException {
//        ROOT = new Node(label);
//        CreateSubtree2(RANDOM_TREE_MAXHEIGHT, RANDOM_TREE_MAXDEGREE, ROOT);
//        ddos_marking.graphics.DisplaySimpleTree.DrawTree(ROOT);
//        saveSubtree(ROOT, SYSTEM_VARIABLE.file);
//        Scanner scn = new Scanner(System.in);
//        scn.nextByte();
//        System.exit(0);

        if (args.length > 0) {
            SYSTEM_VARIABLE.ASSIGNMENT_POLICY = Long.parseLong(args[0]);
        }
        if (args.length > 1) {
            SYSTEM_VARIABLE.B = Integer.parseInt(args[1]);
        }
        if (args.length > 2) {
            SYSTEM_VARIABLE.OMEGA = Double.parseDouble(args[2]);
        }
        if (args.length > 3) {
            SYSTEM_VARIABLE.file = args[3];
        }

        if (args.length > 4) {
            SYSTEM_VARIABLE.SIMULATION_TIME = Long.parseLong(args[4]);
        }
        System.out.println("Starting Problem " + SYSTEM_VARIABLE.ASSIGNMENT_POLICY);

        //  SYSTEM_VARIABLE.file = "tree_exp_real.txt";
        ROOT = readSubtree(SYSTEM_VARIABLE.file);
//      
////        Optimization1 OP = new Optimization1(ROOT);
////        System.out.println(OP.FindDPAssignment(30));
        // ddos_marking.graphics.DisplaySimpleTree.DrawTree(ROOT);
        traverseTree(ROOT);
        StartRouting(ROOT);

//
    }

    public static Node readSubtree(String filename) throws IOException {
        Scanner scn = new Scanner(new File(filename));
        HashMap<Integer, Node> Nodes = new HashMap<Integer, Node>();
        while (scn.hasNext()) {
            String line = scn.nextLine();
            StringTokenizer line_tok = new StringTokenizer(line, ",");
            int L = Integer.parseInt(line_tok.nextToken().trim());
            Node n;
            if (!Nodes.containsKey(L)) {
                n = new Node(L);
                Nodes.put(L, n);
            }
            n = Nodes.get(L);

            //System.out.println(L);
            String Children = line_tok.nextToken().trim();

            // System.out.println(Children);
            if (Children.trim().length() != 0) {
                StringTokenizer child_tok = new StringTokenizer(Children);
                while (child_tok.hasMoreElements()) {
                    int CL = Integer.parseInt(child_tok.nextToken());
                    // System.out.print(CL);
                    Node c;
                    if (!Nodes.containsKey(CL)) {
                        c = new Node(CL);
                        Nodes.put(CL, c);
                    }
                    c = Nodes.get(CL);
                    n.C.add(c);
                    //   System.out.println("added "+c.L + " to "+ L);
                    c.P = n;
                }
            }

            String Users = line_tok.nextToken().trim();
            if (Users.trim().length() != 0) {
                StringTokenizer user_tok = new StringTokenizer(Users);
                while (user_tok.hasMoreElements()) {
                    boolean isLegit = Boolean.parseBoolean(user_tok.nextToken());
                    double datarate = Double.parseDouble(user_tok.nextToken());
                    User u = new User(isLegit, datarate);
                    n.U.add(u);
                }
            }

        }
        return Nodes.get(0);
    }

    public static void saveSubtree(Node N, String filename) throws IOException {

        File f = new File(filename);
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
        PrintWriter writer = new PrintWriter(f, "UTF-8");
        saveSubtreeRec(N, writer);
        writer.close();

    }

    public static void saveSubtreeRec(Node N, PrintWriter pw) {
        if (N != null) {
            pw.print(N.L + ", ");
            for (Node c : N.C) {
                pw.print(c.L + " ");
            }
            pw.print(", ");
            for (User u : N.U) {
                pw.print(u.isLegit + " ");
                pw.print(u.dataRate + " ");
            }
            pw.println();
            for (Node c : N.C) {
                saveSubtreeRec(c, pw);
            }

        }
    }

    public static void CreateSubtree(int depth, int degree, Node N) {

        if (depth <= 0) {
            CreateUser(N);
            return;
        }
//        if (Math.random() <= 0.1) {
//            CreateUser(N);
//            return;
//        }
        if (Math.random() <= INTERNAL_NODE_USER_PROBABILITY) {
            CreateUser(N);
        }

        int deg = (int) (Math.random() * 100 % degree);
        if (deg > 0) {
            deg++;
        }
        for (int c = 0; c < deg; c++) {
            Node n = new Node(++label);
            N.C.add(n);
            n.P = N;
            CreateSubtree(depth - 1, degree, n);
        }

        if (deg == 0) {
            CreateUser(N);
            return;
        }
    }

    public static void CreateSubtree2(int depth, int degree, Node N) {

        if (depth <= 0) {
            CreateUser(N);
            return;
        }
//        if (Math.random() <= 0.1) {
//            CreateUser(N);
//            return;
//        }
        if (Math.random() <= INTERNAL_NODE_USER_PROBABILITY) {
            CreateUser(N);
        }

        int deg = degree;
        if (deg > 0) {
            deg++;
        }
        for (int c = 0; c < deg; c++) {
            Node n = new Node(++label);
            N.C.add(n);
            n.P = N;
            CreateSubtree2(depth - 1, degree, n);
        }

        if (deg == 0) {
            CreateUser(N);
            return;
        }
    }

    public static void CreateUser(Node n) {
        int numbe_of_user = (int) (Math.random() * 6 + 1);

        int Case = (int) (Math.random() * 100) % 2;

        if (Case == 0) {
            //all white
            for (int i = 0; i < numbe_of_user; i++) {
                boolean islegit = true;
                double datarate = (Math.random() * 100) % MAX_DATARATE + 1;
                User u = new User(islegit, datarate);
                n.U.add(u);
            }
        }
        if (Case == 1) {
            //all black
            for (int i = 0; i < numbe_of_user; i++) {
                boolean islegit = false;
                double datarate = (Math.random() * 100) % MAX_DATARATE + 1;
                User u = new User(islegit, datarate);
                n.U.add(u);
            }
        }
        if (Case == 2) {
            //mixed
            for (int i = 0; i < numbe_of_user; i++) {
                boolean islegit = true;

                if (Math.random() < ATTACKER_RATIO) {
                    islegit = false;
                } else {
                    islegit = true;
                }
                double datarate = (Math.random() * 100) % MAX_DATARATE + 1;
                User u = new User(islegit, datarate);
                n.U.add(u);
            }
        }

    }

    public static void traverseTree(Node root) {
        System.out.print(root.L + "(" + root.AL + "," + root.UL + ")" + "->");
        for (Node c : root.C) {
            System.out.print(c.L + "(" + c.AL + "," + c.UL + ")" + " ");
        }
        for (User u : root.U) {
            if (!u.isLegit) {
                System.out.print("[A: " + u.dataRate + "]");
            } else {
                System.out.print("[L: " + u.dataRate + "]");
            }

        }
        System.out.println("");
        for (Node c : root.C) {
            traverseTree(c);
        }

    }

    public static void TreeGetAllNodes(Node root, HashMap<Integer, Node> Nodes) {

        //  System.out.print(root.L + "(" + root.AL + "," + root.UL + ")" + "->");
        Nodes.put(root.L, root);
        for (Node c : root.C) {
            //System.out.print(c.L + "(" + c.AL + "," + c.UL + ")" + " ");

        }
        for (User u : root.U) {
            if (!u.isLegit) {
                System.out.print("[A: " + u.dataRate + "]");
            } else {
                System.out.print("[L: " + u.dataRate + "]");
            }

        }
        System.out.println("");
        for (Node c : root.C) {
            TreeGetAllNodes(c, Nodes);
        }

    }
    static HashSet<Integer> visited = new HashSet<Integer>();

    public static void PrintTree(Node root) {
        if (root == null) {
            return;
        }
        // visited.add(root.L);
        System.out.print(root.L + "->");
        for (Node c : root.C) {
            System.out.print(c.L + " ");
        }
        System.out.println("");
        for (Node c : root.C) {
            traverseTree(c);
        }

    }

    public static boolean hasLoop(Node root) {
        boolean hasloop = false;
        if (root == null) {
            return hasloop;
        }
        ArrayList<Node> N = new ArrayList<Node>();
        HashSet<Integer> H = new HashSet<Integer>();
        N.add(root);
        while (!N.isEmpty()) {
            Node n = N.remove(0);
            if (H.contains(n.L)) {
                hasloop = false;
            } else {
                H.add(n.L);
            }
            N.addAll(n.C);
        }

        return hasloop;
    }

    public static void StartAll(Node root) {
        
        ROOT=root;
        StartRouting(root);
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        root.log();
                        Thread.sleep(SYSTEM_VARIABLE.LOG_INTERVAL);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(CreateRandomTree.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        T.start();

    }

    private static void StartRouting(Node root) {

        new RoutingThread(root);
        for (Node c : root.C) {
            StartRouting(c);
        }

    }

    public static Node CreateTreeFromRealData() {
        return null;
    }

}
