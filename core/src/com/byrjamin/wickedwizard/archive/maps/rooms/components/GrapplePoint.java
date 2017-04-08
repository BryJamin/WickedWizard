package com.byrjamin.wickedwizard.archive.maps.rooms.components;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.utils.BoundsDrawer;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 31/01/2017.
 */
public class GrapplePoint {

    private float x;
    private float y;

    private float width = Measure.units(5);
    private float height = Measure.units(5);

    private float bound_width = Measure.units(10);
    private float bound_height = Measure.units(10);

    private Rectangle bounds;

    private TextureRegion GRAPPLE_TEXTURE;

    public GrapplePoint(float x, float y){
        this.x = x;
        this.y = y;
        bounds = new Rectangle(x - width / 2, y - height / 2, bound_width, bound_height);
        this.GRAPPLE_TEXTURE = PlayScreen.atlas.findRegion("grapple");
    }

    public GrapplePoint(float x, float y, TextureRegion GRAPPLE_TEXTURE){
        this(x, y);
        this.GRAPPLE_TEXTURE = GRAPPLE_TEXTURE;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void draw(SpriteBatch batch){

        batch.draw(GRAPPLE_TEXTURE, x,y, width, height);
        BoundsDrawer.drawBounds(batch, bounds);
    }

    public void setCenter(float posX, float posY) {
        x = posX - width / 2;
        y = posY - height / 2;
        bounds = new Rectangle(x - width / 2, y - height / 2, bound_width, bound_height);
    }

    public float getCenterX(){
        return x + width /2;
    }

    public float getCenterY(){
        return y + height /2;
    }




}
