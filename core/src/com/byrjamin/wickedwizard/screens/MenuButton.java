package com.byrjamin.wickedwizard.screens;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by ae164 on 19/05/17.
 */

public class MenuButton {


    private String font;
    private TextureRegion buttonTexture;

    public MenuButton(String font, TextureRegion buttonTexture) {
        this.font = font;
        this.buttonTexture = buttonTexture;
    }


    //float width = Measure.units(30f);
    //float height = Measure.units(10f);
    //

    public Entity createButton(World world, String text, float x, float y, float width, float height, Color foreground, Color background) {


        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(x, y));
        TextureFontComponent tfc = new TextureFontComponent(font, text, 0, height / 2 + Measure.units(1f), width, TextureRegionComponent.FOREGROUND_LAYER_NEAR);
        tfc.color = foreground;
        tfc.DEFAULT = foreground;
        e.edit().add(tfc);

        Rectangle r = new Rectangle(x, y, width, height);

        e.edit().add(new CollisionBoundComponent(r));


        Entity shape = world.createEntity();
        shape.edit().add(new PositionComponent(x, y));

        TextureRegionComponent trc = new TextureRegionComponent(buttonTexture, width, height, TextureRegionComponent.FOREGROUND_LAYER_MIDDLE, background);

        shape.edit().add(trc);

        return e;

    }


    public Entity createButtonWithAction(World world, String text, float x, float y, float width, float height, Color foreground, Color background, Action action){

        Entity e = createButton(world, text, x, y, width, height, foreground, background);
        e.edit().add(new ActionOnTouchComponent(action));
        return e;

    }


}
