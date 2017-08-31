package com.byrjamin.wickedwizard.ecs.components.texture;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Align;
import com.byrjamin.wickedwizard.assets.FontAssets;

/**
 * Created by Home on 01/04/2017.
 */


public class TextureFontComponent extends TextureRegionComponent {

    public String font;

    public String text;

    public int align = Align.center;

    public TextureFontComponent(){
        this(FontAssets.small, "Default");
        this.layer = FOREGROUND_LAYER_NEAR;
    }

    public TextureFontComponent(String font, String text, float offsetX, float offsetY, float width, int layer, Color color) {
        this.font = font;
        this.text = text;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.width = width;
        this.layer = layer;
        this.color = color;
        this.DEFAULT = color;
    }

    public TextureFontComponent(String font, String text, float offsetX, float offsetY, float width, int layer) {
        this(font, text, offsetX, offsetY, width, layer, new Color(Color.WHITE));
    }

    public TextureFontComponent(String font, String text, float width, int layer, Color color){
        this(font, text, 0,0, width, layer, color);
    }


    public TextureFontComponent(String font, String text, float width, int layer){
        this(font, text, 0,0, width, layer);
    }


    public TextureFontComponent(String text){
        this(FontAssets.small, text);
    }

    public TextureFontComponent(String font, String text){
        this.font = font;
        this.text = text;
    }

    public TextureFontComponent(String font, String text, Color color){
        this.font = font;
        this.text = text;
        this.color = color;
        this.DEFAULT = color;
    }


    public TextureFontComponent(String font, String text, int layer, Color color){
        this.font = font;
        this.text = text;
        this.color = color;
        this.DEFAULT = color;
    }



}
