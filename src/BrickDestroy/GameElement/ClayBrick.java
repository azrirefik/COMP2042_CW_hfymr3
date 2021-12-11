package BrickDestroy.GameElement;

import BrickDestroy.GameUI.Theme;

import java.awt.*;
import java.awt.Point;


/**
 * Created by filippo on 04/09/16.
 *
 */
public class ClayBrick extends Brick {

    private static final String NAME = "Clay Brick";
    private static final Color DEF_INNER = Theme.COL01;
    private static final Color DEF_BORDER = Theme.COL00;
    private static final int CLAY_STRENGTH = 1;
    private static final int COST = 1;






    public ClayBrick(Point point, Dimension size){
        super(NAME,point,size,DEF_BORDER,DEF_INNER,CLAY_STRENGTH);
    }

    @Override
    protected Shape makeBrickFace(Point pos, Dimension size) {
        return new Rectangle(pos,size);
    }

    @Override
    public Shape getBrick() {
        return super.brickFace;
    }

    public int getCost() {
        return COST;
    }

}
