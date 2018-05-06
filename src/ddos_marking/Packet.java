/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import java.util.ArrayList;

/**
 *
 * @author rajor
 */
public class Packet {

    boolean isLegit = true;
    int Src;
    int Dest;
    static int ID = 0;
    ArrayList<Integer> marking;

    public Packet(boolean isLegit, int Source, int Destination) {
        this.isLegit = isLegit;
        Src = Source;
        Dest = Destination;
        ID++;
        marking = new ArrayList<Integer>();
    }
}
