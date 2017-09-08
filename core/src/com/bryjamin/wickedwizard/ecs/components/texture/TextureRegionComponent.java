package com.bryjamin.wickedwizard.ecs.components.texture;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Home on 03/03/2017.
 */
public class TextureRegionComponent extends Component{


    public final static int BACKGROUND_LAYER_FAR = -64;
    public final static int BACKGROUND_LAYER_MIDDLE = -32;
    public final static int BACKGROUND_LAYER_NEAR = -16;

    public final static int ENEMY_LAYER_FAR = -8;
    public final static int ENEMY_LAYER_MIDDLE = -4;
    public final static int ENEMY_LAYER_NEAR = -2;

    public final static int PLAYER_LAYER_FAR = 0;
    public final static int PLAYER_LAYER_MIDDLE = 2;
    public final static int PLAYER_LAYER_NEAR = 4;

    public final static int FOREGROUND_LAYER_FAR = 8;
    public final static int FOREGROUND_LAYER_MIDDLE = 16;
    public final static int FOREGROUND_LAYER_NEAR = 32;


    public int layer = PLAYER_LAYER_MIDDLE;

    public TextureRegion region;

    public float offsetX;
    public float offsetY;

    public float height;
    public float width;

    public float scaleX = 1;
    public float scaleY = 1;

    public float rotation = 0;

    public Color color = new Color(1,1,1,1);
    public Color DEFAULT = new Color(1,1,1,1);

    public TextureRegionComponent(TextureRegion region, float offsetX, float offsetY, float width, float height, int layer) {
        this.region = region;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.layer = layer;
    }


    public TextureRegionComponent(TextureRegion region, float offsetX, float offsetY, float width, float height, int layer, Color color) {
        this(region, offsetX,offsetY, width, height, layer);
        this.color = color;
        this.DEFAULT = new Color(color);
    }


    public TextureRegionComponent(){
    }

    public TextureRegionComponent(TextureRegion region, float width, float height){
        this(region, 0,0, width, height, ENEMY_LAYER_MIDDLE);
    }

    public TextureRegionComponent(TextureRegion region, float width, float height, int layer){
        this(region, 0,0, width, height, layer);
    }

    public TextureRegionComponent(TextureRegion region, float width, float height, int layer, Color color){
        this(region, 0,0, width, height, layer);
        this.color = color;
        this.DEFAULT = new Color(color);
    }


    public void setColor(float r, float g, float b, float a){
        color.set(r,g,b,a);
    }

}
