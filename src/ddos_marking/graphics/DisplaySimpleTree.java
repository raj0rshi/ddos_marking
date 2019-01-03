package ddos_marking.graphics;

// Code for popping up a window that displays a custom component
// in this case we are displaying a Binary Search tree  
// reference problem 4.38 of Weiss to compute tree node x,y positions
// input is a text file name that will form the Binary Search Tree
//     java DisplaySimpleTree textfile
import ddos_marking.Node;
import ddos_marking.User;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class DisplaySimpleTree extends JFrame {

    JScrollPane scrollpane;
    DisplayPanel panel;
    public static int WIN_X = 1600;
    public static int WIN_Y = 800;

    public DisplaySimpleTree(MyTree t) {
        panel = new DisplayPanel(t);
        panel.setPreferredSize(new Dimension(WIN_X, WIN_Y));
        scrollpane = new JScrollPane(panel);
        getContentPane().add(scrollpane, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();  // cleans up the window panel
    }

    public static void DrawTree(Node r) {

        MyTree t = new MyTree(); // t is Binary tree we are displaying

        t.root = r;

        t.computeNodePositions(); //finds x,y positions of the tree nodes
        t.maxheight = t.treeHeight(t.root); //finds tree height for scaling y axis
        DisplaySimpleTree dt = new DisplaySimpleTree(t);//get a display panel
        dt.setVisible(true); //show the display
    }
}

class DisplayPanel extends JPanel {

    MyTree t;

    public DisplayPanel(MyTree t) {
        this.t = t; // allows dispay routines to access the tree
        setBackground(Color.white);
        setForeground(Color.black);
    }

    protected void paintComponent(Graphics g) {
        g.setColor(getBackground()); //colors the window
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.BLACK); //set color and fonts
        Font MyFont = new Font("SansSerif", Font.PLAIN, 10);
        g.setFont(MyFont);
        MyFont = new Font("SansSerif", Font.BOLD, 20); //bigger font for tree
        g.setFont(MyFont);
        this.drawTree(g, t.root); // draw the tree
        revalidate(); //update the component panel
    }

    public void drawTree(Graphics g, Node root) {//actually draws the tree
        int dx, dy, dx2, dy2;
        int XSCALE, YSCALE;
        XSCALE = 1;//scale x by total nodes in tree
        YSCALE = 80; //scale y by tree height

        // System.out.println("[" + root.xpos + "," + root.ypos + "]");
        int ucount = 0;
        for (ddos_marking.User u : root.U) {
            if (!u.isLegit) {
                ucount++;
            }
        }
        String s = "" + (root.L) + "-" + ucount;
        dx = root.xpos * XSCALE;
        dy = root.ypos * YSCALE;
        g.drawString(s, dx, dy);
        g.drawOval(dx - 3, dy - 3, 9, 9);
        int i = 0;
        for (User u : root.U) {
            if (u.isLegit) {
                g.setColor(Color.green);
            } else {
                g.setColor(Color.red);
            }
            g.drawRect((int) (dx - 20 + i * (40.0 / root.U.size())), dy + 20, 15, 15);

            g.setColor(Color.black);
            g.drawLine((int) (dx - 15 + i * (40.0 / root.U.size())), dy + 20, dx, dy);

            g.drawString((u.dataRate+"").substring(0,3), (int) (dx - 30 + i * (40.0 / root.U.size())), dy + 30);

            i++;
            break;
        }
        if (root != null) {
            for (Node c : root.C) {
                dx2 = c.xpos * XSCALE;
                dy2 = c.ypos * YSCALE;
                g.drawLine(dx, dy, dx2, dy2);
                drawTree(g, c);
            }
        }
    }
}

class MyTree {

    String inputString = new String();
    Node root;
    int totalnodes = 0; //keeps track of the inorder number for horiz. scaling 
    int maxheight = 0;//keeps track of the depth of the tree for vert. scaling
    private final int WIN_X = DisplaySimpleTree.WIN_X;
    private final int WIN_Y = DisplaySimpleTree.WIN_Y;

    MyTree() {
        root = null;
    }

    public int treeHeight(Node t) {
        if (t == null) {
            return -1;
        } else {

            int max = Integer.MIN_VALUE;
            for (Node c : t.C) {
                int h = treeHeight(c);
                if (max <= h) {
                    max = h;
                }
            }
            return 1 + max;
        }

    }

    public void computeNodePositions() {
        int depth = 1;
        DFS_traversal(root, 0, WIN_X, 1);
    }

//traverses tree and computes x,y position of each node, stores it in the node
    public void inorder_traversal(Node t, int depth) {
        if (t != null) {
            for (Node c : t.C) {
                t.xpos = totalnodes++; //x coord is node number in inorder traversal
                t.ypos = depth; // mark y coord as depth
                inorder_traversal(c, depth + 1);
            }
        }
    }

    public void DFS_traversal(Node root, int l, int r, int d) {
        if (root != null) { // inorder traversal to draw each node
            //   System.out.println(root.L);
            int children = root.C.size();
            root.xpos = (l + r) / 2;
            root.ypos = d;
            int c_l = l;
            for (Node c : root.C) {
                int x_space = (r - l) / children;
                DFS_traversal(c, c_l, c_l + x_space, d + 1);
                c_l += x_space;
            }
        }

    }

}
