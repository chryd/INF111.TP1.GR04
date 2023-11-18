package echecs.Test;

import static org.junit.Assert.*;
import com.echecs.PartieEchecs;//echecs.Position;
import echecs.pieces.*;
import org.junit.Test;

public class PartieEchecTest {
    PartieEchecs partie = new PartieEchecs();

    @Test
    public void testConstructeur(){
        Piece[][] expected = new Piece[8][8];
        com.echecs.pieces.Piece[][] echiquier = partie.getEchiquier();
        //Placement des pièces :

        //placement des pions
        for(int i = 0; i < 8; i++){
            expected[i][1] = new Pion('n');
            expected[i][6] = new Pion('b');
        }

        //placement des tours
        expected[0][0] = new Tour('n');
        expected[7][0] = new Tour('n');
        expected[0][7] = new Tour('b');
        expected[7][7] = new Tour('b');

        //placement des cavaliers
        expected[1][0] = new Cavalier('n');
        expected[6][0] = new Cavalier('n');
        expected[1][7] = new Cavalier('b');
        expected[6][7] = new Cavalier('b');

        //placement des fous
        expected[2][0] = new Fou('n');
        expected[5][2] = new Fou('n');
        expected[2][7] = new Fou('b');
        expected[5][7] = new Fou('b');

        //placement des dames
        expected[3][0] = new Dame('n');
        expected[3][7] = new Dame('b');

        //placement des rois
        expected[4][0] = new Roi('n');
        expected[4][7] = new Roi('b');

        assertSame(expected, echiquier);
    }

}
