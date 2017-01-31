package com.byrjamin.wickedwizard.maps.rooms.layout;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.helper.BoundsDrawer;
import com.byrjamin.wickedwizard.helper.Measure;

/**
 * Created by Home on 31/01/2017.
 */
public class GrapplePoint {

    private float x;
    private float y;

    private float width = Measure.units(5);
    private float height = Measure.units(5);

    private Rectangle bounds;

    private TextureRegion GRAPPLE_TEXTURE;

    public GrapplePoint(float x, float y, TextureRegion GRAPPLE_TEXTURE){
        this.x = x;
        this.y = y;
        bounds = new Rectangle(x, y, width, height);
        this.GRAPPLE_TEXTURE = GRAPPLE_TEXTURE;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void draw(SpriteBatch batch){

        batch.draw(GRAPPLE_TEXTURE, x,y, width, height);

        BoundsDrawer.drawBounds(batch, bounds);
    }




}
