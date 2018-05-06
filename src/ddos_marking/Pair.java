/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

/**
 *
 * @author rajor
 */
public class Pair implements Comparable<Pair> {

    int A;
    int D;

    public Pair() {
        A = 0;
        D = 0;
    }

    public Pair(int A, int D) {
        this.A = A;
        this.D = D;
    }

    @Override
    public int compareTo(Pair o) {
        if (o.A == A && o.D == D) {
            return 0;
        }
        if (o.A >= A && o.D >= D) {
            return 1;
        }
        return -1;

    }

    @Override
    public boolean equals(Object o) {
        Pair o1 = (Pair) o;
        if (o1.A == A && o1.D == D) {
            return true;
        }
        return false;
    }
}
