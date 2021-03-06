package com.bryjamin.wickedwizard.factories;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;

import java.util.Random;

/**
 * Created by Home on 15/03/2017.
 */

public class BackgroundFactory {




    public Bag<Component>backgroundBags(float x, float y, float BACKGROUND_WIDTH, float BACKGROUND_HEIGHT, float TILE_SIZE, Array<? extends TextureRegion> selection, ArenaSkin arenaSkin){
        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x, y));
        TextureRegionBatchComponent trbc = generateTRBC(BACKGROUND_WIDTH, BACKGROUND_HEIGHT, TILE_SIZE, selection, TextureRegionComponent.BACKGROUND_LAYER_FAR,1,0);
        trbc.color = arenaSkin.getBackgroundTint();
        bag.add(trbc);
        return bag;
    }

    public Bag<Component>backgroundBags(float x, float y, float BACKGROUND_WIDTH, float BACKGROUND_HEIGHT, float TILE_SIZE, Array<? extends TextureRegion> selection, Color color){
        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x, y));
        TextureRegionBatchComponent trbc = generateTRBC(BACKGROUND_WIDTH, BACKGROUND_HEIGHT, TILE_SIZE, selection, TextureRegionComponent.BACKGROUND_LAYER_FAR,1,1);
        trbc.color = color;
        bag.add(trbc);
        return bag;
    }


    public TextureRegionBatchComponent generateTRBC (float width,
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

    public TextureRegionBatchComponent generateTRBC (float width, float height, float tile_size, Array<? extends TextureRegion> selection, int layer){
        return generateTRBC(width,height,tile_size,selection,layer,0,0);
    }




}
