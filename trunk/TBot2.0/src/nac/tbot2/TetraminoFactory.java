/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nac.tbot2;

import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author camomot
 */
public class TetraminoFactory {

    private static Tetramino[][] MINOS;
    private static final TetraminoFactory INSTANCE = new TetraminoFactory();
    private static final String MINOS_FILE = "tetraminos.json";
    private static final Map<Integer, Tetramino[]> map = new HashMap<>(7);

    private TetraminoFactory() {
        Gson gson = new Gson();
        MINOS = gson.fromJson(new InputStreamReader(TetraminoFactory.class.getResourceAsStream(MINOS_FILE)), Tetramino[][].class);
        for (int i = 0; i < MINOS.length; i++) {
            Tetramino[] t = MINOS[i];
            for (int j = 0; j < t.length; j++) {
                t[j].translate();
            }
        }
    }

    public static Tetramino[] get(int colorSum) {
        return map.get(colorSum);
    }

    public static void set(int type, int colorSum) {
        map.put(colorSum, MINOS[type]);
    }
}
