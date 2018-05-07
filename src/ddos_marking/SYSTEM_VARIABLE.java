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
public interface SYSTEM_VARIABLE {

    public static final double ATTACKER_RATIO = .4;
    public static final int MAX_DATARATE = 3;
    public static final double INTERNAL_NODE_USER_PROBABILITY = 0.05;

    public static final double MARKING_PROBABILITY = .4;

    public static final int RANDOM_TREE_MAXDEGREE = 4;
    public static final int RANDOM_TREE_MAXHEIGHT = 6;

    public static final int V = -3;
    public static final int U = -4;
   // public static final String file = "tree2.txt";
    public static final String file = "tree_exp_3.txt";
    public static final double OMEGA = 0.5;
    public static final int B = 30;
    public static final long SIMULATION_TIME = 500000;
    public static final long ASSIGNMENT_INTERVAL = 10000;

    public static final long PATHUPDATE_INTERVAL = 1000;
}
