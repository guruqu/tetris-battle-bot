/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nac.tbot2;

/**
 *
 * @author nathaniel
 */
public interface BoardListener {

    public void onPieceEntered();

    public void onNextAreaChange(Integer[] aveColor);
}
