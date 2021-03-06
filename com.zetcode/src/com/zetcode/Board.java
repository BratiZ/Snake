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
                      B_HEIGHT = 620,
                      DOT_SIZE = 10,
                      ALL_DOTS = 900,
                      RAND_POS = 59,
                      DELAY = 120;

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

            g.drawImage(tunnelIn, tunnelInX, tunnelInY, this);
            g.drawImage(tunnelOut, tunnelOutX, tunnelOutY, this);
            
            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }
            g.setColor( Color.LIGHT_GRAY);
            g.fillRect(0, 0, B_WIDTH+10, 20);
            
            g.setColor( Color.WHITE);
            g.drawString("Points: " + (this.dots - 4), 2, 15);
            Toolkit.getDefaultToolkit().sync();
            g.drawImage(apple, apple_x, apple_y, this);
            
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

            if ((z > 3) && (x[0] == x[z]) && (y[0] == y[z])) {
                dots = z;
            }
        }

        if (y[0] >= B_HEIGHT + DOT_SIZE) {
            inGame = false;
        }

        if (y[0] < 2*DOT_SIZE) {
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

        int r = (int) (Math.random() * (RAND_POS - 2)) + 2;
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * (RAND_POS - 3)) + 3;
        apple_y = ((r * DOT_SIZE));
        
    }
    
    private void locateTunnel() {
        int r = (int) (Math.random() * (RAND_POS-1)/2)+1;
        tunnelInX = ((r * DOT_SIZE));

        r = (int) (Math.random() * (RAND_POS-1)/2)+1;
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
            if( timer.isRunning() && muve){
                if ((pressedKey == KeyEvent.VK_LEFT) && (!rightDirection)) {
                    leftDirection = true;
                    upDirection = false;
                    downDirection = false;
                    muve = false;
                }

                else if ((pressedKey == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                    rightDirection = true;
                    upDirection = false;
                    downDirection = false;
                    muve = false;
                }

                else if ((pressedKey == KeyEvent.VK_UP) && (!downDirection)) {
                    upDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                    muve = false;
                }

                else if ((pressedKey == KeyEvent.VK_DOWN) && (!upDirection)) {
                    downDirection = true;
                    rightDirection = false;
                    leftDirection = false;
                    muve = false;
                }
            }
            if( pressedKey == KeyEvent.VK_R){
                timer.stop();
                inGame = true;
                upDirection = leftDirection = downDirection = false;
                rightDirection = true;
                initGame();
            }
            
            if( pressedKey == KeyEvent.VK_P){
                if( timer.isRunning())
                    timer.stop();
                else 
                    timer.start();
            }
            
            if( pressedKey == KeyEvent.VK_T){
                dots += 2; 
            }
            
        }
    }
}