/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ddos_marking;

import static ddos_marking.SYSTEM_VARIABLE.V;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rajor
 */
public class RoutingThread implements Runnable {

    Node N;
    Thread T;

    public RoutingThread(Node n) {
        N = n;
        T = new Thread(this);
        N.T = T;
        T.start();

       // System.out.println("routing started at node: " + N.L + " with mp=" + N.mp);
        Thread UT = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                 //   System.out.println("Users at node "+N.L +" : "+ N.U.size());
                    for (User u : N.getU()) {
                        for (int i = 0; i < 1; i++) {
                            Packet p = new Packet(u.isLegit, N.L, V);
                       //     System.out.println("packet generated at node:" + N.L);
                            N.insert(p);
                        }
                    }
                    try {
                       // System.out.println("packet generator sleeping at node :" + N.L);
                        Thread.sleep((int)(Math.random()*10));
                        // System.out.println("packet generator waking up at node :" + N.L);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RoutingThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });
        UT.start();

    }

    @Override
    public void run() {
        while (true) {
            try {
                if (N.forward()) {
                    if (N.P != null) {
                        //System.out.println("packet forwarded at node:" + N.L + " to " + N.P.L);
                    }

                } else {
                    try {
                        synchronized (T) {
                            //System.out.println("router " + N.L + " sleeping");
                           T.wait();
                           
                           
                           // System.out.println("router " + N.L + " is waking up");

                        }
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RoutingThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(RoutingThread.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
