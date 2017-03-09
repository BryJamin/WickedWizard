package com.byrjamin.wickedwizard.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 03/03/2017.
 */
public class TextureRegionComponent extends Component{

    public TextureRegion region;

    public float offsetX;
    public float offsetY;

    public float height;
    public float width;

    public float scaleX = 1;
    public float scaleY = 1;

    public float rotation = 0;

    public Color color = new Color(1,1,1,1);
    public Color DEFAULT = Color.WHITE;

    public TextureRegionComponent(TextureRegion region, float offsetX, float offsetY, float width, float height) {
        this.region = region;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
    }

    public TextureRegionComponent(){
        this(PlayScreen.atlas.findRegion(TextureStrings.BLOB_STANDING), 0,0, Measure.units(5), Measure.units(5));
    }

    public TextureRegionComponent(TextureRegion region){
        this(region, 0,0, Measure.units(5), Measure.units(5));
    }

    public TextureRegionComponent(TextureRegion region, float width, float height){
        this(region, 0,0, width, height);
    }


    public void setColor(float r, float g, float b, float a){
        color.set(r,g,b,a);
    }

}
