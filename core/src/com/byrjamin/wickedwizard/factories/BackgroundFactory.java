package com.byrjamin.wickedwizard.factories;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;

import java.util.Random;

/**
 * Created by Home on 15/03/2017.
 */

public class BackgroundFactory {




    public static Bag<Component>backgroundBags(float x, float y, float BACKGROUND_WIDTH, float BACKGROUND_HEIGHT, float TILE_SIZE, Array<? extends TextureRegion> selection){
        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x, y));
        TextureRegionBatchComponent trbc = generateTRBC(BACKGROUND_WIDTH, BACKGROUND_HEIGHT, TILE_SIZE, selection, TextureRegionComponent.BACKGROUND_LAYER_FAR,1,1);
        bag.add(trbc);
        return bag;
    }


    public static TextureRegionBatchComponent generateTRBC (float width,
                                                            float height,
                                                            float tile_size,
                                                            Array<? extends TextureRegion> selection,
                                                            int layer,
                                                            int additionalrows,
                                                            int additionalColumns){


        Random random = new Random();

        Array<TextureRegion> backgrounds = new Array<TextureRegion>();
        int columns = ((int) width / (int) tile_size) + additionalrows;
        int rows = ((int) height / (int) tile_size) + additionalColumns;

        int count = rows * columns;

        for(int i = 0; i < count; i++){
            backgrounds.add(selection.get(random.nextInt(selection.size)));
        }

        return new TextureRegionBatchComponent(rows, columns, tile_size, tile_size, backgrounds, layer);

    }

    public static TextureRegionBatchComponent generateTRBC (float width, float height, float tile_size, Array<? extends TextureRegion> selection, int layer){
        return generateTRBC(width,height,tile_size,selection,layer,0,0);
    }




}
