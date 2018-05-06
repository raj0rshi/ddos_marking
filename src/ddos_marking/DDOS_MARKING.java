/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import static ddos_marking.Analytical.get_number_of_packets;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author rajor
 */
public class DDOS_MARKING {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        int n = 0;
        int R = 25;

        File f = new File("marked.txt");
        if (!f.exists()) {
            f.createNewFile();
        }
        FileOutputStream pw = new FileOutputStream(f);
        double alph = 0.00;
        int tot_n = 0;
        double mp = .2 - alph;
        int ROUND = 1000;

        for (mp = .2; mp <1.04; mp += .05) {
            tot_n = 0;
            for (int round = 0; round < ROUND; round++) {
                n = 0;
                ArrayList< ArrayList<Integer>> packets = new ArrayList< ArrayList<Integer>>();

                while (true) {
                    n++;

                    ArrayList<Integer> packet = new ArrayList<Integer>();
                    boolean marked = false;
                    for (int i = 0; i < R; i++) {
                        double rand = Math.random();
                        if (!marked) {
                            if (rand < (mp-alph)) {
                                packet.add(i);//packet marked
                                marked=true;

                            }
                        } else if (rand <( mp + alph)) {
                            packet.add(i);//packet marked

                        }
                    }
                    if (packet.size() > 0) {
                        packets.add(packet);
                    }

                    int x = 0;

                    if (check_path_seq(packets, R)) {
                        tot_n += n;
                        break;
                    }

                }

            }
            
            double sim=tot_n / ROUND;
            double ana=LinkProbability.expN(R, mp);
            System.out.println(R + "\t" + mp + "\t" + sim+ "\t" +ana+"\t"+((sim-ana)/sim));
         //   System.out.println(R + "\t" + mp + "\t" + LinkProbability.expN(R, mp));
        }
    }

    static boolean check_path_seq(ArrayList< ArrayList<Integer>> packets, int R) {
        //    System.out.println("************");
        HashSet<Pair> pairs = new HashSet<Pair>();

        for (ArrayList<Integer> packet : packets) {
//
//            for(int i:packet)
//            {
//                System.out.print(i+"->");
//            }
//            System.out.println("");
            if (packet.size() == 1) {
                Pair p = new Pair(packet.get(0), R);
                if (!pairs.contains(p)) {
                    //  pairs.add(p);
                }
            }
            for (int i = 1; i < packet.size(); i++) {
                Pair p = new Pair(packet.get(i - 1), packet.get(i));
                if (!pairs.contains(p)) {
                    pairs.add(p);
                }

                if (i == packet.size() - 1) {
                    p = new Pair(packet.get(i), R);
                    if (!pairs.contains(p)) {
                        //  pairs.add(p);
                    }
                }
            }
        }

//        for (Pair p : pairs) {
//            System.out.println(p.A + "->" + p.D);
//        }
        int exp_top = R - 1;

        HashSet<Integer> tails = new HashSet<Integer>();
        while (pairs.size() != 0) {
            HashSet<Integer> tops = getTop(pairs);
            if (tops.size() != 1) {

//                System.out.print("top:");
//                for (int x : tops) {
//                    System.out.print(x + " ");
//
//                }
//                System.out.println("tops !=1");
                return false;
            }
            int top = 0;
            for (int x : tops) {
                //      System.out.print(x + " ");
                top = x;
            }
            if (tails.size() > 0 && !tails.contains(top)) {
                System.out.println("tails does not contain top");
//                for (int i : tails) {
//                    System.out.print(i);
//                }
//                System.out.println("");
                // return false;
            }

            HashSet<Pair> pairs2 = new HashSet<Pair>(pairs);
//            System.out.println("before reomove");
//            for (Pair p : pairs) {
//                System.out.println(p.A + "->" + p.D);
//            }

            tails = new HashSet<Integer>();
            for (Pair p : pairs2) {
                if (p.D == top) {
                    pairs.remove(p);
                    tails.add(p.A);
                }
            }
//            System.out.println("after remove");
//            for (Pair p : pairs) {
//                System.out.println(p.A + "->" + p.D);
//            }
            if (exp_top != top) {
                //   System.out.println("exp top " + (exp_top) + "!= top(" + top + ")");
                return false;
            } else {
                //   System.out.println(top + "<-");
            }
            exp_top--;
            if (exp_top == 0) {
                return true;
            }
        }

        return false;
    }

    static HashSet<Integer> getTop(HashSet<Pair> pairs) {
        HashSet<Integer> top = new HashSet<Integer>();
        HashSet<Pair> pairs2 = new HashSet<Pair>(pairs);
        for (Pair p : pairs) {
            boolean flag = true;
            for (Pair p2 : pairs2) {
                if (p.D == p2.A) {
                    flag = false;
                }
            }
            if (flag == true && !top.contains(p.D)) {
                top.add(p.D);
            }
        }
        return top;
    }

}
