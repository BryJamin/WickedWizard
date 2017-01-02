package com.byrjamin.wickedwizard.spelltypes.blastwaves;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.spelltypes.*;

/**
 * Created by Home on 01/01/2017.
 */
public class DispelWave extends BlastWave{

    private Dispellable.DISPELL dispelDirection;

    public DispelWave(float centerPointX, float centerPointY,  Dispellable.DISPELL dispelDirection){
        super(centerPointX, centerPointY);

        this.dispelDirection = dispelDirection;

        if(dispelDirection == Dispellable.DISPELL.HORIZONTAL){
            this.setDrawingColor(Color.BLUE);
        } else if(dispelDirection == Dispellable.DISPELL.VERTICAL){
            this.setDrawingColor(Color.RED);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    public Dispellable.DISPELL getDispelDirection() {
        return dispelDirection;
    }

    public void setDispelDirection(Dispellable.DISPELL dispelDirection) {
        this.dispelDirection = dispelDirection;
    }

}
