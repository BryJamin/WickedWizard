package com.byrjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.assets.Assets;
import com.byrjamin.wickedwizard.ecs.components.identifiers.BannerComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
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



    private static final float itemBannerTitleTextOffsetY = Measure.units(21.5f);
    private static final float itemBannerDescriptionOffsetY = Measure.units(16.5f);


    private static final float levelBannerTextOffsetY = Measure.units(19.5f);

    private static final float bannerHeight = Measure.units(10f);
    private static final float bannerFollowOffsetY = Measure.units(12.5f);

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
        banner.edit().add(parentComponent);

        FadeComponent fc = new FadeComponent(true, 4f, false);
        fc.count = 1;

        ExpireComponent ec = new ExpireComponent(10f);

        titleFC.color.a = 0;
        titleFC.text = title;
        createBannerText(0,  itemBannerTitleTextOffsetY, titleFC, parentComponent, fc, ec);


        messageFC.color.a = 0;
        messageFC.text = message;
        createBannerText(0, itemBannerDescriptionOffsetY, messageFC, parentComponent, fc, ec);

        createBannerBox(parentComponent, fc, ec, new Color(Color.BLACK));

        fc = new FadeComponent(true, 0.25f, false);
        fc.count = 1;

        Entity whiteFlashingBox = createBannerBox(parentComponent, fc, new ExpireComponent(0.9f), new Color(Color.WHITE));
        whiteFlashingBox.getComponent(TextureRegionComponent.class).layer = TextureRegionComponent.FOREGROUND_LAYER_NEAR;
        whiteFlashingBox.getComponent(TextureRegionComponent.class).color.a = 1;

    }


    public void createLevelBanner(String message){

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


        TextureFontComponent trc = new TextureFontComponent(Assets.medium, message);
        trc.color.a = 0;
        trc.layer = TextureRegionComponent.FOREGROUND_LAYER_NEAR;
        createBannerText(0,  levelBannerTextOffsetY, trc, parentComponent, fc, ec);

        createBannerBox(parentComponent, fc, ec, new Color(Color.BLACK));

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


    private Entity createBannerBox(ParentComponent parentBanner, FadeComponent fc, ExpireComponent ec, Color color){

        Entity blackBackingBox = world.createEntity();
        blackBackingBox.edit().add(new PositionComponent(gamecam.position.x - gamecam.viewportHeight / 2, gamecam.position.y + bannerFollowOffsetY));
        blackBackingBox.edit().add(new FollowPositionComponent(gamecam.position, - MainGame.GAME_WIDTH / 2, bannerFollowOffsetY));

        TextureRegionComponent trc = new TextureRegionComponent(bannerTexture, 0,0, MainGame.GAME_WIDTH, bannerHeight, TextureRegionComponent.FOREGROUND_LAYER_MIDDLE);
        trc.color = color;
        trc.color.a = 0;
        blackBackingBox.edit().add(trc);
        blackBackingBox.edit().add(ec);
        blackBackingBox.edit().add(fc);
        blackBackingBox.edit().add(new ChildComponent(parentBanner));

        return  blackBackingBox;

    }

}
