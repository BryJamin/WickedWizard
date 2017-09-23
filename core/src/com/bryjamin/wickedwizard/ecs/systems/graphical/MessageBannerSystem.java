package com.bryjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.bryjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FollowCameraComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.BannerComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.UIComponent;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 18/04/2017.
 */

public class MessageBannerSystem extends EntitySystem {

    ComponentMapper<PositionComponent> pm;

    private TextureFontComponent titleFC;
    private TextureFontComponent messageFC;
    private TextureRegion bannerTexture;

    private static final float bannerHeight = Measure.units(10f);

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


    public void createItemBanner(String title, String message, float offsetY){

        for(Entity e : this.getEntities()){
            world.getSystem(com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem.class).kill(e);
        }


        Entity banner = world.createEntity();
        banner.edit().add(new BannerComponent());
        banner.edit().add(new UIComponent());
        com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent parentComponent = new com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent();
        banner.edit().add(parentComponent);

        FadeComponent fc = new FadeComponent(true, 4f, false);
        fc.count = 1;

        ExpireComponent ec = new ExpireComponent(10f);

        titleFC.color.a = 0;
        titleFC.text = title;
        createBannerText(0,  offsetY + Measure.units(5f), titleFC, parentComponent, fc, ec);


        messageFC.color.a = 0;
        messageFC.text = message;
        createBannerText(0, offsetY, messageFC, parentComponent, fc, ec);

        createBannerBox(offsetY, parentComponent, fc, ec, new Color(Color.BLACK));

        fc = new FadeComponent(true, 0.25f, false);
        fc.count = 1;

        Entity whiteFlashingBox = createBannerBox(offsetY, parentComponent, fc, new ExpireComponent(0.9f), new Color(Color.WHITE));
        whiteFlashingBox.getComponent(TextureRegionComponent.class).layer = TextureRegionComponent.FOREGROUND_LAYER_NEAR;
        whiteFlashingBox.getComponent(TextureRegionComponent.class).color.a = 1;

    }



    private Entity createBannerText(float offsetX, float offsetY, TextureFontComponent tfc, com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent parentBanner, FadeComponent fc, ExpireComponent ec){
        Entity text = world.createEntity();
        text.edit().add(new PositionComponent(gamecam.position.x + offsetX, gamecam.position.y + offsetY));
        text.edit().add(new FollowCameraComponent(0, offsetY));
        text.edit().add(new CollisionBoundComponent(
                new Rectangle(gamecam.position.x + offsetX, gamecam.position.y + offsetY, gamecam.viewportWidth, bannerHeight / 2)));
        text.edit().add(new UIComponent());
        text.edit().add(tfc);
        text.edit().add(fc);
        text.edit().add(ec);
        text.edit().add(new ChildComponent(parentBanner));
        return text;
    }


    private Entity createBannerBox(float offsetY, ParentComponent parentBanner, FadeComponent fc, ExpireComponent ec, Color color){

        Entity blackBackingBox = world.createEntity();
        blackBackingBox.edit().add(new PositionComponent(gamecam.position.x - gamecam.viewportWidth / 2, gamecam.position.y + offsetY));
        blackBackingBox.edit().add(new FollowCameraComponent(0, offsetY));

        TextureRegionComponent trc = new TextureRegionComponent(bannerTexture, 0,0, gamecam.viewportWidth, bannerHeight, TextureRegionComponent.FOREGROUND_LAYER_MIDDLE);
        trc.color = color;
        trc.color.a = 0;
        blackBackingBox.edit().add(trc);
        blackBackingBox.edit().add(new UIComponent());
        blackBackingBox.edit().add(ec);
        blackBackingBox.edit().add(fc);
        blackBackingBox.edit().add(new com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent(parentBanner));

        return  blackBackingBox;

    }

}
