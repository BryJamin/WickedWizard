package com.byrjamin.wickedwizard.screens;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.ShapeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.arenas.skins.SolitarySkin;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by ae164 on 19/05/17.
 */

public class MenuButton {


    private String font;

    public MenuButton(String font) {
        this.font = font;
    }


    //float width = Measure.units(30f);
    //float height = Measure.units(10f);
    //

    public Entity createButton(World world, String text, float x, float y, float width, float height, Color foreground, Color background) {


        x = x - width / 2;
        y = y - width / 2;

        Entity e = world.createEntity();
        e.edit().add(new PositionComponent(x, y));
        TextureFontComponent tfc = new TextureFontComponent(font, text, 0, height / 2 + Measure.units(1f), width, height, TextureRegionComponent.FOREGROUND_LAYER_NEAR);
        tfc.color = foreground;
        tfc.DEFAULT = foreground;
        e.edit().add(tfc);

        Rectangle r = new Rectangle(x, y, width, height);

        e.edit().add(new CollisionBoundComponent(r));


        Entity shape = world.createEntity();
        shape.edit().add(new PositionComponent(x, y));

        ShapeComponent sc = new ShapeComponent(width, height, TextureRegionComponent.FOREGROUND_LAYER_MIDDLE);
        sc.color = background;
        sc.DEFAULT = background;

        shape.edit().add(sc);

        return e;

    }

}
