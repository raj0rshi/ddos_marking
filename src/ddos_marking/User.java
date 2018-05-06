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
public class User {

    boolean isLegit = false;
    double dataRate = 0;

    public User(boolean x, double dr) {
        isLegit = x;
        dataRate = dr;
    }
}
