package com.byrjamin.wickedwizard.ecs.components.texture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.assets.Assets;

/**
 * Created by Home on 01/04/2017.
 */


public class TextureFontComponent extends TextureRegionComponent {

    public String font;

    public String text;

    public TextureFontComponent(){
        this(Assets.small, "Default");
        this.layer = FOREGROUND_LAYER_NEAR;
    }

    public TextureFontComponent(String font, String text, float offsetX, float offsetY, float width, float height, int layer, Color color) {
        this.font = font;
        this.text = text;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.layer = layer;
        this.color = color;
        this.DEFAULT = color;
    }

    public TextureFontComponent(String font, String text, float offsetX, float offsetY, float width, float height, int layer) {
        this.font = font;
        this.text = text;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.height = height;
        this.layer = layer;
    }


    public TextureFontComponent(String font, String text, float width, float height, int layer){
        this(font, text, 0,0, width, height, layer);
    }


    public TextureFontComponent(String text){
        this(Assets.small, text);
    }

    public TextureFontComponent(String font, String text){
        this.font = font;
        this.text = text;
    }


}
