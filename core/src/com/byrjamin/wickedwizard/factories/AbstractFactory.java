package com.byrjamin.wickedwizard.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.byrjamin.wickedwizard.assets.FileLocationStrings;

/**
 * Created by Home on 28/04/2017.
 */

public abstract class AbstractFactory {

    protected AssetManager assetManager;
    protected TextureAtlas atlas;

    public AbstractFactory(AssetManager assetManager){
        this.assetManager = assetManager;
        this.atlas = assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);
    }



}
