package com.zetcode;

import java.awt.HeadlessException;
import javax.swing.JFrame;

public class frame extends JFrame{

    public frame() throws HeadlessException {
        super();
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
        add( new Board());
        setSize( 600, 600);
        setResizable(false);
        setVisible(true);
    }
}
