package com.zetcode;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import javax.swing.JFrame;

public class frame extends JFrame{

    public frame() throws HeadlessException {
        super();
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.yellow);
        add( new Board());
        pack();
        setResizable(false);
        setVisible(true);
    }
}
