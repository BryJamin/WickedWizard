package com.byrjamin.wickedwizard.ecs.components.texture;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by Home on 01/04/2017.
 */

public class TextureFontComponent extends TextureRegionComponent {


    public BitmapFont font = new BitmapFont();

    public String text;

    public TextureFontComponent(){
        text = "Default";
        font.getData().setScale(5, 5);
    }

    public TextureFontComponent(String text){
        this.text = text;
        font.getData().setScale(5, 5);
    }

    public TextureFontComponent(String text, float scale){
        this.text = text;
        font.getData().setScale(scale, scale);
    }




}
