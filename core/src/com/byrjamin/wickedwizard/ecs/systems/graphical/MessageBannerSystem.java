package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.byrjamin.wickedwizard.ecs.components.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.ShapeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.CameraSystem;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 18/04/2017.
 */

public class MessageBannerSystem extends BaseSystem {

    static ComponentMapper<DirectionalComponent> dm;
    ComponentMapper<PositionComponent> pm;

    TextureFontComponent titleFC;
    TextureFontComponent messageFC;

    public MessageBannerSystem() {
        titleFC = new TextureFontComponent("Default", 5);
        titleFC.layer = TextureRegionComponent.FOREGROUND_LAYER_NEAR;;


        messageFC = new TextureFontComponent("Default", 3);
        messageFC.layer = TextureRegionComponent.FOREGROUND_LAYER_NEAR;
    }

    @Override
    protected void processSystem() {


    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }


    public void createBanner(String title, String message){


        OrthographicCamera gamecam = world.getSystem(CameraSystem.class).getGamecam();

        FadeComponent fc = new FadeComponent();
        fc.alpha = 0f;
        fc.fadeIn = true;
        fc.isEndless = true;
        fc.alphaTimer = 0f;
        fc.alphaTimeLimit = 4f;

        ExpireComponent ec = new ExpireComponent(8f);

        Entity itemText = world.createEntity();
        itemText.edit().add(new PositionComponent(gamecam.position.x - gamecam.viewportWidth / 2, gamecam.position.y + Measure.units(13)));
        itemText.edit().add(new FollowPositionComponent(gamecam.position, -gamecam.viewportWidth / 2, Measure.units(13)));
        titleFC.color.a = 0;
        titleFC.text = title;
        itemText.edit().add(titleFC);
        itemText.edit().add(fc);
        itemText.edit().add(ec);

        //TODO something about scaling the font causes slow down possible due to scaling the bitmap font twice?
        //TODO not sure but for now I'm using a slash n

        Entity itemDescription = world.createEntity();
        itemDescription.edit().add(new PositionComponent(gamecam.position.x - gamecam.viewportWidth / 2, gamecam.position.y + Measure.units(8)));
        itemDescription.edit().add(new FollowPositionComponent(gamecam.position, -gamecam.viewportWidth / 2, Measure.units(8)));
        messageFC.color.a = 0;
        itemDescription.edit().add(messageFC);
        itemDescription.edit().add(fc);
        itemDescription.edit().add(ec);

        Entity blackBackingBox = world.createEntity();
        blackBackingBox.edit().add(new PositionComponent(gamecam.position.x - gamecam.viewportWidth / 2, gamecam.position.y + Measure.units(5)));
        blackBackingBox.edit().add(new FollowPositionComponent(gamecam.position, -gamecam.viewportWidth / 2, Measure.units(5)));

        ShapeComponent sc = new ShapeComponent(0,0, gamecam.viewportWidth, Measure.units(10f), TextureRegionComponent.FOREGROUND_LAYER_MIDDLE);
        sc.color = Color.BLACK;
        sc.color.a = 0;
        sc.shapeType = ShapeRenderer.ShapeType.Filled;
        blackBackingBox.edit().add(sc);
        blackBackingBox.edit().add(ec);

        blackBackingBox.edit().add(fc);


    }

    public static void changeDirection(World world, Entity e, Direction direction, DirectionalComponent.PRIORITY priority) {
        if(dm.has(e)){
            DirectionalComponent dc = dm.get(e);
            dc.setDirection(direction, priority);
        }
    }

}
