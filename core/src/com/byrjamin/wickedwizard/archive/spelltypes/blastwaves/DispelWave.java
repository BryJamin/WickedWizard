package com.byrjamin.wickedwizard.archive.spelltypes.blastwaves;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Home on 01/01/2017.
 */
public class DispelWave extends BlastWave {

    private com.byrjamin.wickedwizard.archive.spelltypes.Dispellable.DISPELL dispelDirection;

    public DispelWave(float centerPointX, float centerPointY,  com.byrjamin.wickedwizard.archive.spelltypes.Dispellable.DISPELL dispelDirection){
        super(centerPointX, centerPointY);

        this.dispelDirection = dispelDirection;

        if(dispelDirection == com.byrjamin.wickedwizard.archive.spelltypes.Dispellable.DISPELL.HORIZONTAL){
            this.setDrawingColor(Color.BLUE);
        } else if(dispelDirection == com.byrjamin.wickedwizard.archive.spelltypes.Dispellable.DISPELL.VERTICAL){
            this.setDrawingColor(Color.RED);
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
    }

    public com.byrjamin.wickedwizard.archive.spelltypes.Dispellable.DISPELL getDispelDirection() {
        return dispelDirection;
    }

    public void setDispelDirection(com.byrjamin.wickedwizard.archive.spelltypes.Dispellable.DISPELL dispelDirection) {
        this.dispelDirection = dispelDirection;
    }

}
