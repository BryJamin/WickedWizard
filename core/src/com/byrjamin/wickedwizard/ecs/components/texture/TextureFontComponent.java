package com.byrjamin.wickedwizard.ecs.components.texture;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.byrjamin.wickedwizard.assets.Assests;

/**
 * Created by Home on 01/04/2017.
 */


public class TextureFontComponent extends TextureRegionComponent {

    public BitmapFont font;

    public String text;

    public TextureFontComponent(){
        this(Assests.smallFont, "Default");
        this.layer = FOREGROUND_LAYER_NEAR;
    }

    public TextureFontComponent(String text){
        this(Assests.smallFont, text);
    }

    public TextureFontComponent(BitmapFont font, String text){
        this.font = font;
        this.text = text;
    }


}
