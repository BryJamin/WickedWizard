package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.ShapeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 18/04/2017.
 */

public class MessageBannerSystem extends BaseSystem {

    static ComponentMapper<DirectionalComponent> dm;
    ComponentMapper<PositionComponent> pm;

    TextureFontComponent titleFC;
    TextureFontComponent messageFC;

    public MessageBannerSystem() {
        titleFC = new TextureFontComponent("Default");
        titleFC.layer = TextureRegionComponent.FOREGROUND_LAYER_NEAR;;


        messageFC = new TextureFontComponent("Default");
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
        itemText.edit().add(new PositionComponent(gamecam.position.x, gamecam.position.y + Measure.units(14)));
        itemText.edit().add(new FollowPositionComponent(gamecam.position, 0, Measure.units(14)));
        titleFC.color.a = 0;
        titleFC.text = title;
        itemText.edit().add(titleFC);
        itemText.edit().add(fc);
        itemText.edit().add(ec);

        Entity itemDescription = world.createEntity();
        itemDescription.edit().add(new PositionComponent(gamecam.position.x, gamecam.position.y + Measure.units(9)));
        itemDescription.edit().add(new FollowPositionComponent(gamecam.position, 0, Measure.units(9)));
        messageFC.color.a = 0;
        messageFC.text = message;
        itemDescription.edit().add(messageFC);
        itemDescription.edit().add(fc);
        itemDescription.edit().add(ec);

        Entity blackBackingBox = world.createEntity();
        blackBackingBox.edit().add(new PositionComponent(gamecam.position.x - gamecam.viewportHeight / 2, gamecam.position.y + Measure.units(5)));
        blackBackingBox.edit().add(new FollowPositionComponent(gamecam.position, - MainGame.GAME_WIDTH / 2, Measure.units(5)));

        ShapeComponent sc = new ShapeComponent(0,0, MainGame.GAME_WIDTH, Measure.units(10f), TextureRegionComponent.FOREGROUND_LAYER_MIDDLE);
        sc.color = new Color(Color.BLACK);
        sc.color.a = 0;
        sc.shapeType = ShapeRenderer.ShapeType.Filled;
        blackBackingBox.edit().add(sc);
        blackBackingBox.edit().add(ec);
        blackBackingBox.edit().add(fc);


    }

}
