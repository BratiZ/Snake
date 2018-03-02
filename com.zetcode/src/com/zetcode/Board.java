package com.zetcode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    private final int B_WIDTH = 600,
                      B_HEIGHT = 600,
                      DOT_SIZE = 10,
                      ALL_DOTS = 900,
                      RAND_POS = 59,
                      DELAY = 100;

    private final int[] x = new int[ALL_DOTS],
                        y = new int[ALL_DOTS];

    private int dots,
                apple_x,
                apple_y,
                tunnelInX,
                tunnelInY,
                tunnelOutX,
                tunnelOutY,
                pressedKey;

    private boolean leftDirection = false,
                    rightDirection = true,
                    upDirection = false,
                    downDirection = false,
                    inGame = true,
                    muve = true;

    private Timer timer;
    
    private Image ball,
                  apple,
                  head,
                  tunnelIn,
                  tunnelOut;

    public Board() {

        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);

        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    private void loadImages() {

        ImageIcon iid = new ImageIcon("dot.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("head.png");
        head = iih.getImage();
        
        ImageIcon iiti = new ImageIcon("tunnel.png");
        tunnelIn = iiti.getImage();
        
        ImageIcon iito = new ImageIcon("tunnel.png");
        tunnelOut = iito.getImage();
    }

    private void initGame() {

        dots = 4;
        this.pressedKey = 39;

        for (int z = 0; z < dots; z++) {
            x[z] = 50 - z * 10;
            y[z] = 50;
        }

        locateApple();
        locateTunnel();
        
        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }
    
    private void doDrawing(Graphics g) {
        
        if (inGame) {

            g.drawImage(apple, apple_x, apple_y, this);
            g.drawImage(tunnelIn, tunnelInX, tunnelInY, this);
            g.drawImage(tunnelOut, tunnelOutX, tunnelOutY, this);
            
            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }

            Toolkit.getDefaultToolkit().sync();

        } else {

            gameOver(g);
        }        
    }

    private void gameOver(Graphics g) {
        
        String msg = "Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);
        
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple() {

        if ((x[0] == apple_x) && (y[0] == apple_y)) {

            dots++;
            locateApple();
        }
    }

    private void move() {

        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }

        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }

        if (rightDirection) {
            x[0] += DOT_SIZE;
        }

        if (upDirection) {
            y[0] -= DOT_SIZE;
        }

        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    private void checkCollision() {

        for (int z = dots; z > 0; z--) {

            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
            }
        }

        if (y[0] >= B_HEIGHT + DOT_SIZE) {
            inGame = false;
        }

        if (y[0] < 0) {
            inGame = false;
        }

        if (x[0] >= B_WIDTH + DOT_SIZE) {
            inGame = false;
        }
            
        if (x[0] < 0) {
            inGame = false;
        }
        
        if( x[0] == tunnelInX && y[0] == tunnelInY){
            if(leftDirection){
                x[0] = tunnelOutX - DOT_SIZE;
                y[0] = tunnelOutY;
            }
            if(rightDirection){
                x[0] = tunnelOutX + DOT_SIZE;
                y[0] = tunnelOutY;
            }
            if(upDirection){
                y[0] = tunnelOutY - DOT_SIZE;
                x[0] = tunnelOutX;
            }
            
            if(downDirection){
                y[0] = tunnelOutY + DOT_SIZE;
                x[0] = tunnelOutX;
            }
        }
        
        if( x[0] == tunnelOutX && y[0] == tunnelOutY){
            if(leftDirection){
                x[0] = tunnelInX - DOT_SIZE;
                y[0] = tunnelInY;
            }
            if(rightDirection){
                x[0] = tunnelInX + DOT_SIZE;
                y[0] = tunnelInY;
            }
            if(upDirection){
                y[0] = tunnelInY - DOT_SIZE;
                x[0] = tunnelInX;
            }
            
            if(downDirection){
                y[0] = tunnelInY + DOT_SIZE;
                x[0] = tunnelInX;
            }
        }

        if(!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {

        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
        
    }
    
    private void locateTunnel() {
        int r = (int) (Math.random() * RAND_POS/2);
        tunnelInX = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS/2);
        tunnelInY = ((r * DOT_SIZE));
        
        r = (int) (Math.random() * RAND_POS/2 + RAND_POS/2);
        tunnelOutX = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS/2) + RAND_POS/2;
        tunnelOutY = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (inGame) {
            muve = true;
            checkApple();
            checkCollision();
            move();
        }

        repaint();
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {            

            pressedKey = e.getKeyCode();
            
            if ((pressedKey == KeyEvent.VK_LEFT) && (!rightDirection) && muve) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
                muve = false;
            }

            else if ((pressedKey == KeyEvent.VK_RIGHT) && (!leftDirection) && muve) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
                muve = false;
            }

            else if ((pressedKey == KeyEvent.VK_UP) && (!downDirection) && muve) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
                muve = false;
            }

            else if ((pressedKey == KeyEvent.VK_DOWN) && (!upDirection) && muve) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
                muve = false;
            }
            
            if( pressedKey == KeyEvent.VK_R){
                timer.stop();
                inGame = true;
                upDirection = leftDirection = downDirection = false;
                rightDirection = true;
                initGame();
            }
        }
    }
}