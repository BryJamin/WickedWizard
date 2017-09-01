package com.bryjamin.wickedwizard.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/**
 * Created by Home on 28/04/2017.
 */

public abstract class AbstractFactory {

    protected AssetManager assetManager;
    protected TextureAtlas atlas;

    public AbstractFactory(AssetManager assetManager){
        this.assetManager = assetManager;
        this.atlas = assetManager.get(com.bryjamin.wickedwizard.assets.FileLocationStrings.spriteAtlas, TextureAtlas.class);
    }



}
