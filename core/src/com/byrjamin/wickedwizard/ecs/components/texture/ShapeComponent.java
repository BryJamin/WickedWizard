package com.byrjamin.wickedwizard.ecs.components.texture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 01/04/2017.
 */

public class ShapeComponent extends TextureRegionComponent{

    public ShapeRenderer.ShapeType shapeType = ShapeRenderer.ShapeType.Filled;

    public ShapeComponent(float offsetX, float offsetY, float width, float height, int layer) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.layer = layer;
    }

    public ShapeComponent(float offsetX, float offsetY, float width, float height, int layer, Color color) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.layer = layer;
        this.color = color;
        this.DEFAULT = color;
    }

    public ShapeComponent(){
        this(0,0, Measure.units(5), Measure.units(5), FOREGROUND_LAYER_FAR);
    }

    public ShapeComponent(float width, float height, int layer){
        this(0,0, width, height, layer);
    }

}
