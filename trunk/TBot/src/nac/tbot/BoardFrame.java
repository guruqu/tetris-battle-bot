/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nac.tbot;

/**
 *
 * @author nathaniel
 */
public class BoardFrame extends javax.swing.JFrame {

  private BoardWatcher boardWatcher;

  public void setBoardWatcher(BoardWatcher boardWatcher) {
    this.boardWatcher = boardWatcher;
  }

  /**
   * Creates new form BoardFrame
   */
  public BoardFrame() {
    initComponents();
    setResizable(false);
  }

  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    measurePanel1 = new nac.tbot.MeasurePanel();

    setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
    addWindowListener(new java.awt.event.WindowAdapter() {
      public void windowClosing(java.awt.event.WindowEvent evt) {
        formWindowClosing(evt);
      }
    });

    javax.swing.GroupLayout measurePanel1Layout = new javax.swing.GroupLayout(measurePanel1);
    measurePanel1.setLayout(measurePanel1Layout);
    measurePanel1Layout.setHorizontalGroup(
      measurePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 179, Short.MAX_VALUE)
    );
    measurePanel1Layout.setVerticalGroup(
      measurePanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 360, Short.MAX_VALUE)
    );

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(measurePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(measurePanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addContainerGap())
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
      boardWatcher.setLoc(measurePanel1.getLocationOnScreen());
      boardWatcher.setRun(true);
      setVisible(false);
    }//GEN-LAST:event_formWindowClosing
  /**
   * @param args the command line arguments
   */
  // Variables declaration - do not modify//GEN-BEGIN:variables
  private nac.tbot.MeasurePanel measurePanel1;
  // End of variables declaration//GEN-END:variables
}