/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nac.tbot2;

import com.google.gson.Gson;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author nathaniel
 */
public class TBot2 extends javax.swing.JFrame {

    private BoardFrame boardFrame = new BoardFrame();
    private NextAreaFrame nextAreaFrame = new NextAreaFrame();
    private BoardWatcher boardWatcher = new BoardWatcher();
    private Preferences preferences = Preferences.userNodeForPackage(TBot2.class);
    private static final String PREF_BOARD_BOUNDS = "PREF_BOARD_BOUNDS";
    private static final String PREF_NEXT_AREA_BOUNDS = "PREF_NEXT_AREA_BOUNDS";
    public static final String PREF_I = "PREF_I";
    public static final String PREF_T = "PREF_T";
    public static final String PREF_O = "PREF_O";
    public static final String PREF_J = "PREF_J";
    public static final String PREF_L = "PREF_L";
    public static final String PREF_S = "PREF_S";
    public static final String PREF_Z = "PREF_Z";

    /**
     * Creates new form TBot2
     */
    public TBot2() {
        initComponents();
        boardFrame.setBoardWatcher(boardWatcher);
        nextAreaFrame.setBoardWatcher(boardWatcher);
        boardWatcher.setBoardListener(new BoardListener() {
            @Override
            public void onPieceEntered() {
                System.out.println("onPieceEntered");
            }

            @Override
            public void onNextAreaChange(int[] aveColor) {
                System.out.println("change");
                Object[] options = {"I", "T", "O", "J", "L", "S", "Z"};
                int n = JOptionPane.showOptionDialog(rootPane, "Test", "Test", JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, null);
                System.out.println("n:" + n);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jButton1.setText("Set Board");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Set Next Piece Area");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Start");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Stop");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)))
                .addContainerGap(261, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 203, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                boardFrame.setOpacity(0.5f);
                boardFrame.setVisible(true);
            }
        });
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                nextAreaFrame.setOpacity(0.5f);
                nextAreaFrame.setVisible(true);
            }
        });
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            preferences.putByteArray(PREF_NEXT_AREA_BOUNDS, TBotUtils.serialize(boardWatcher.getNextAreaBounds()));
            preferences.putByteArray(PREF_BOARD_BOUNDS, TBotUtils.serialize(boardWatcher.getBounds()));
        } catch (Exception ex) {
            Logger.getLogger(TBot2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            byte[] d = preferences.getByteArray(PREF_NEXT_AREA_BOUNDS, null);
            Rectangle rect;
            if (d != null) {
                rect = (Rectangle) TBotUtils.deserialize(d);
                if (rect != null) {
                    System.out.println("rect:" + rect);
                    boardWatcher.setNextAreaBounds(rect);
                }
            }

            d = preferences.getByteArray(PREF_BOARD_BOUNDS, null);
            if (d != null) {
                rect = (Rectangle) TBotUtils.deserialize(d);
                if (rect != null) {
                    System.out.println("rect:" + rect);
                    boardWatcher.setBounds(rect);
                }

            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(TBot2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowOpened

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        boardWatcher.start();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        boardWatcher.stop();
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

        JFrame.setDefaultLookAndFeelDecorated(true);
        /* Create and display the form */
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TBot2().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    // End of variables declaration//GEN-END:variables
}
