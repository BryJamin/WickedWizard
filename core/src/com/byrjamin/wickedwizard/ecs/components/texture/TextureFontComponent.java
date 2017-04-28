package com.byrjamin.wickedwizard.ecs.components.texture;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

    public TextureFontComponent(String text){
        this(Assets.small, text);
    }

    public TextureFontComponent(String font, String text){
        this.font = font;
        this.text = text;
    }


}
