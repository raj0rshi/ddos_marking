/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import java.util.Random;

/**
 *
 * @author rajor
 */
public class SYSTEM_VARIABLE {

    public static final double ATTACKER_RATIO = .84;
    public static final int MAX_DATARATE = 2;
    public static final double INTERNAL_NODE_USER_PROBABILITY = 0.25;

    public static final double MARKING_PROBABILITY = .5;

    public static final int RANDOM_TREE_MAXDEGREE = 4;
    public static final int RANDOM_TREE_MAXHEIGHT = 6;

    public static final int V = -3;
    public static final int U = -4;
   // public static String file = "tree_exp_2.txt";
    public static String file = "tree_exp_6.txt";
    public static double OMEGA = 0.5;
    public static int B = 100;
    public static long SIMULATION_TIME = 700000;
    public static final long ASSIGNMENT_INTERVAL = 20000;

    public static final long PATHUPDATE_INTERVAL = 10000;
    public static long ASSIGNMENT_POLICY = 2;
    public static final long LOG_INTERVAL = 2000;
    static boolean KNOWS_TOPOLOGY = true;

}
