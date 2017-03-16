package com.byrjamin.wickedwizard.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Home on 15/03/2017.
 */

public class TextureRegionBatchComponent extends TextureRegionComponent {

    public int rows;
    public int columns;

    public Array<TextureRegion> regions;

    public TextureRegionBatchComponent(){
        
    }

    public TextureRegionBatchComponent(int rows, int columns, float width, float height, Array<TextureRegion> regions){
        this.rows = rows;
        this.columns = columns;
        this.width = width;
        this.height = height;
        this.regions = regions;
    }


}
