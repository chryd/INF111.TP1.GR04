package echecs.Test;

import static org.junit.Assert.*;
import com.echecs.Position;//echecs.Position;
import echecs.pieces.*;
import org.junit.Test;

public class PositionTest {
    @Test
    public void testEstVoisine(){
        Position p1 = new Position('a', (byte) 1);
        Position voisineVertical = new Position('a', (byte) 2);
        Position voisineHorizontal = new Position('b', (byte) 1);
        Position voisineDiagonale = new Position('b', (byte) 2);
        Position nonVoisine = new Position('h', (byte) 2);


        assertTrue(p1.estVoisineDe(voisineVertical));
        assertTrue(p1.estVoisineDe(voisineHorizontal));
        assertTrue(p1.estVoisineDe(voisineDiagonale));
        assertFalse(p1.estVoisineDe(nonVoisine));
    }

    @Test
    public void testEstSurLaMemeLigneQue(){
        Position p1 = new Position('a', (byte) 1);
        Position surLaMemeLigne = new Position('h', (byte) 1);
        Position pasSurLaMemeLigne = new Position('a', (byte) 8);

        assertTrue(p1.estSurLaMemeLigneQue(surLaMemeLigne));
        assertFalse(p1.estSurLaMemeLigneQue(pasSurLaMemeLigne));
    }

    @Test
    public void testEstSurLaMemeColonneQue(){
        Position p1 = new Position('a', (byte) 1);
        Position pasSurLaMemeColonne = new Position('h', (byte) 1);
        Position surLaMemeColonne = new Position('a', (byte) 8);

        assertTrue(p1.estSurLaMemeColonneQue(surLaMemeColonne));
        assertFalse(p1.estSurLaMemeColonneQue(pasSurLaMemeColonne));
    }

    @Test
    public void testEstSurLaMemeDiagonaleQue(){
        Position p1 = new Position('a', (byte) 1);
        Position surLaMemeDiagonale = new Position('h', (byte) 8);
        Position pasSurLaMemeDiagonale = new Position('a', (byte) 7);

        assertTrue(p1.estSurLaMemeDiagonaleQue(surLaMemeDiagonale));
        assertFalse(p1.estSurLaMemeDiagonaleQue(pasSurLaMemeDiagonale));
    }
}
