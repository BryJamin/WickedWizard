package com.byrjamin.wickedwizard.ecs.components.texture;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.screens.PlayScreen;

/**
 * Created by Home on 01/04/2017.
 */

public class ShapeComponent extends TextureRegionComponent{

    public ShapeRenderer.ShapeType shapeType = ShapeRenderer.ShapeType.Filled;

    public ShapeComponent(float offsetX, float offsetY, float width, float height) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
    }

    public ShapeComponent(){
        this(0,0, Measure.units(5), Measure.units(5));
    }

    public ShapeComponent(float width, float height){
        this(0,0, width, height);
    }

}
