package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.ecs.components.BannerComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 18/04/2017.
 */

public class MessageBannerSystem extends EntitySystem {

    ComponentMapper<PositionComponent> pm;

    TextureFontComponent titleFC;
    TextureFontComponent messageFC;
    TextureRegion bannerTexture;

    private Camera gamecam;

    /**
     * Creates an entity system that uses the specified aspect as a matcher
     * against entities.
     *
     */
    public MessageBannerSystem(TextureRegion bannerTexture, Camera gamecam) {
        super(Aspect.all(BannerComponent.class));

        titleFC = new TextureFontComponent("Default");
        titleFC.layer = TextureRegionComponent.FOREGROUND_LAYER_NEAR;;

        this.bannerTexture = bannerTexture;
        messageFC = new TextureFontComponent("Default");
        messageFC.layer = TextureRegionComponent.FOREGROUND_LAYER_NEAR;

        this.gamecam = gamecam;


    }

    @Override
    protected void processSystem() {


    }

    @Override
    protected boolean checkProcessing() {
        return false;
    }


    public void createItemBanner(String title, String message){

        for(Entity e : this.getEntities()){
            world.getSystem(OnDeathSystem.class).kill(e);
        }


        Entity banner = world.createEntity();
        banner.edit().add(new BannerComponent());
        ParentComponent parentComponent = new ParentComponent();
        banner.edit().add(new ParentComponent());

        FadeComponent fc = new FadeComponent(true, 4f, false);
        fc.count = 1;

        ExpireComponent ec = new ExpireComponent(10f);
        
        titleFC.color.a = 0;
        titleFC.text = title;
        createBannerText(0,  Measure.units(14), titleFC, parentComponent, fc, ec);


        messageFC.color.a = 0;
        messageFC.text = message;
        createBannerText(0, Measure.units(9f), messageFC, parentComponent, fc, ec);

        createBlackBackingBox(parentComponent, fc, ec, new Color(Color.BLACK));

        fc = new FadeComponent(true, 0.5f, false);
        fc.count = 1;

        Entity whiteFlashingBox = createBlackBackingBox(parentComponent, fc, new ExpireComponent(0.9f), new Color(Color.WHITE));
        whiteFlashingBox.getComponent(TextureRegionComponent.class).layer = TextureRegionComponent.FOREGROUND_LAYER_NEAR;
        whiteFlashingBox.getComponent(TextureRegionComponent.class).color.a = 1;

    }


    private Entity createBannerText(float offsetX, float offsetY, TextureFontComponent tfc, ParentComponent parentBanner, FadeComponent fc, ExpireComponent ec){
        Entity text = world.createEntity();
        text.edit().add(new PositionComponent(gamecam.position.x + offsetX, gamecam.position.y + offsetY));
        text.edit().add(new FollowPositionComponent(gamecam.position, offsetX, offsetY));
        text.edit().add(tfc);
        text.edit().add(fc);
        text.edit().add(ec);
        text.edit().add(new ChildComponent(parentBanner));
        return text;
    }


    private Entity createBlackBackingBox(ParentComponent parentBanner, FadeComponent fc, ExpireComponent ec, Color color){

        Entity blackBackingBox = world.createEntity();
        blackBackingBox.edit().add(new PositionComponent(gamecam.position.x - gamecam.viewportHeight / 2, gamecam.position.y + Measure.units(5)));
        blackBackingBox.edit().add(new FollowPositionComponent(gamecam.position, - MainGame.GAME_WIDTH / 2, Measure.units(5)));

        TextureRegionComponent trc = new TextureRegionComponent(bannerTexture, 0,0, MainGame.GAME_WIDTH, Measure.units(10f), TextureRegionComponent.FOREGROUND_LAYER_MIDDLE);
        trc.color = color;
        trc.color.a = 0;
        blackBackingBox.edit().add(trc);
        blackBackingBox.edit().add(ec);
        blackBackingBox.edit().add(fc);
        blackBackingBox.edit().add(new ChildComponent(parentBanner));

        return  blackBackingBox;

    }

}
