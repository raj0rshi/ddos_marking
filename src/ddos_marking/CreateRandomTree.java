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

/**
 *
 * @author rajor
 */
public class CreateRandomTree {

    static int label = 0;
    static Node ROOT;

    public static void main(String[] args) throws IOException {
//        ROOT = new Node(label);
//        CreateSubtree(RANDOM_TREE_MAXHEIGHT, RANDOM_TREE_MAXDEGREE+1, ROOT);
//        ddos_marking.graphics.DisplaySimpleTree.DrawTree(ROOT);
//
//        saveSubtree(ROOT, SYSTEM_VARIABLE.file);
        ROOT = readSubtree(SYSTEM_VARIABLE.file);
        traverseTree(ROOT);
        ddos_marking.graphics.DisplaySimpleTree.DrawTree(ROOT);

        StartRouting(ROOT);

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

    public static void CreateUser(Node n) {
        int numbe_of_user = (int) (Math.random() * 4 + 1);
        for (int i = 0; i < numbe_of_user; i++) {
            boolean islegit = true;

            if (Math.random() < ATTACKER_RATIO) {
                islegit = false;
            }
            double datarate = (Math.random() * 100) % MAX_DATARATE + 1;
            User u = new User(islegit, datarate);
            n.U.add(u);
        }
    }

    public static void traverseTree(Node root) {
        System.out.print(root.L + "(" + root.xpos + "," + root.ypos + ")" + "->");
        for (Node c : root.C) {
            System.out.print(c.L + "(" + c.xpos + "," + c.ypos + ")" + " ");
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

    private static void StartRouting(Node root) {

        new RoutingThread(root);
        for (Node c : root.C) {
            StartRouting(c);
        }

    }

}
