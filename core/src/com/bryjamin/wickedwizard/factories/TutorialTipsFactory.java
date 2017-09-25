package com.bryjamin.wickedwizard.factories;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.assets.FontAssets;
import com.bryjamin.wickedwizard.assets.MenuStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.DisablePlayerInputComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.FollowCameraComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.UIComponent;
import com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.bryjamin.wickedwizard.utils.CenterMath;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by BB on 24/09/2017.
 */

public class TutorialTipsFactory extends AbstractFactory {


    private static final float titleY = Measure.units(60f);

    public TutorialTipsFactory(AssetManager assetManager) {
        super(assetManager);
    }






    public Bag<ComponentBag> cancelHoverTips(Bag<ComponentBag> fillBag, Camera gamecam){

        ParentComponent parentComponent = new ParentComponent();

        fillBag.add(blackBackground(new ComponentBag(), parentComponent, gamecam));
        fillBag.add(defaultText(new ComponentBag(), FontAssets.medium, parentComponent, gamecam, MenuStrings.Tutorial.liaTips(1), 0, titleY));
        fillBag.add(defaultText(new ComponentBag(), FontAssets.small, parentComponent, gamecam, MenuStrings.Tutorial.DOUBLE_TAP_STOP_HOVERING_1, 0, Measure.units(45f)));
        fillBag.add(defaultText(new ComponentBag(), FontAssets.small, parentComponent, gamecam, MenuStrings.Tutorial.DOUBLE_TAP_STOP_HOVERING_2, 0, Measure.units(30f)));
        fillBag.add(defaultText(new ComponentBag(), FontAssets.small, parentComponent, gamecam, MenuStrings.Tutorial.DOUBLE_TAP_STOP_HOVERING_3, 0, Measure.units(25f)));

        fillBag.add(defaultCenteredImage(new ComponentBag(), TextureStrings.ICON_HOVER, parentComponent, gamecam, Measure.units(20f), Measure.units(30f)));
        fillBag.add(tapToContinue(new ComponentBag(), parentComponent, Measure.units(0), Measure.units(5f), Measure.units(50f), Measure.units(10f)));

        return fillBag;

    }



    public Bag<ComponentBag> aimingTips(Bag<ComponentBag> fillBag, Camera gamecam){

        ParentComponent parentComponent = new ParentComponent();

        fillBag.add(blackBackground(new ComponentBag(), parentComponent, gamecam));
        fillBag.add(defaultText(new ComponentBag(), FontAssets.medium, parentComponent, gamecam, MenuStrings.Tutorial.liaTips(2), 0, titleY));
        fillBag.add(defaultText(new ComponentBag(), FontAssets.small, parentComponent, gamecam, MenuStrings.Tutorial.AIMING_1, 0, Measure.units(50f)));
        fillBag.add(defaultText(new ComponentBag(), FontAssets.small, parentComponent, gamecam, MenuStrings.Tutorial.AIMING_2, 0, Measure.units(45f)));
        fillBag.add(defaultText(new ComponentBag(), FontAssets.small, parentComponent, gamecam, MenuStrings.Tutorial.AIMING_3, 0, Measure.units(22.5f)));
        fillBag.add(defaultText(new ComponentBag(), FontAssets.small, parentComponent, gamecam, MenuStrings.Tutorial.AIMING_4, 0, Measure.units(17.5f)));


        ComponentBag bag = defaultCenteredImage(new ComponentBag(), TextureStrings.ICON_AIMING, parentComponent, gamecam, Measure.units(30f), Measure.units(20f));
        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.ICON_AIMING), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));

        fillBag.add(bag);

        fillBag.add(tapToContinue(new ComponentBag(), parentComponent, Measure.units(0), Measure.units(5f), Measure.units(50f), Measure.units(10f)));

        return fillBag;

    }



    public Bag<ComponentBag> twoThumbsTips(Bag<ComponentBag> fillBag, Camera gamecam){

        ParentComponent parentComponent = new ParentComponent();

        fillBag.add(blackBackground(new ComponentBag(), parentComponent, gamecam));
        fillBag.add(defaultText(new ComponentBag(), FontAssets.medium, parentComponent, gamecam, MenuStrings.Tutorial.liaTips(3), 0, titleY));
        fillBag.add(defaultText(new ComponentBag(), FontAssets.small, parentComponent, gamecam, MenuStrings.Tutorial.TWO_THUMBS_1, 0, Measure.units(50f)));
        fillBag.add(defaultText(new ComponentBag(), FontAssets.small, parentComponent, gamecam, MenuStrings.Tutorial.TWO_THUMBS_2, 0, Measure.units(45f)));
        fillBag.add(defaultText(new ComponentBag(), FontAssets.small, parentComponent, gamecam, MenuStrings.Tutorial.TWO_THUMBS_3, 0, Measure.units(22.5f)));
        fillBag.add(defaultText(new ComponentBag(), FontAssets.small, parentComponent, gamecam, MenuStrings.Tutorial.TWO_THUMBS_4, 0, Measure.units(17.5f)));


        ComponentBag bag = defaultCenteredImage(new ComponentBag(), TextureStrings.ICON_AIMING, parentComponent, gamecam, Measure.units(30f), Measure.units(20f));
        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.ICON_AIMING), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));

        fillBag.add(bag);

        fillBag.add(tapToContinue(new ComponentBag(), parentComponent, Measure.units(0), Measure.units(5f), Measure.units(50f), Measure.units(10f)));

        return fillBag;

    }






    private ComponentBag blackBackground(ComponentBag fillBag, ParentComponent parentComponent, Camera gamecam){


        fillBag.add(new PositionComponent());
        fillBag.add(new FollowCameraComponent());
        fillBag.add(new ChildComponent(parentComponent));
        fillBag.add(new UIComponent());
        fillBag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), gamecam.viewportWidth, gamecam.viewportHeight,
                TextureRegionComponent.BACKGROUND_LAYER_FAR,
                new Color(0,0,0,0.9f)));


        return fillBag;

    }


    private ComponentBag defaultText(ComponentBag fillBag, String font, ParentComponent parentComponent, Camera gamecam, String text, float x, float y){

        fillBag.add(new PositionComponent());
        fillBag.add(new FollowCameraComponent(x, y));
        fillBag.add(new CollisionBoundComponent(new Rectangle(x,y, gamecam.viewportWidth, Measure.units(5f))));
        fillBag.add(new ChildComponent(parentComponent));
        fillBag.add(new UIComponent());
        fillBag.add(new TextureFontComponent(font, text, new Color(1,1,1,1)));

        return fillBag;

    }


    private ComponentBag defaultCenteredImage(ComponentBag fillBag, String region, ParentComponent parentComponent, Camera gamecam, float size, float y){

        fillBag.add(new PositionComponent());
        fillBag.add(new FollowCameraComponent(CenterMath.offsetX(gamecam.viewportWidth, size), y));
        fillBag.add(new TextureRegionComponent(atlas.findRegion(region), size, size, TextureRegionComponent.BACKGROUND_LAYER_MIDDLE));
        fillBag.add(new ChildComponent(parentComponent));
        fillBag.add(new UIComponent());

        return fillBag;

    }




    private ComponentBag tapToContinue(ComponentBag fillBag, ParentComponent parentComponent, float x, float y, float width, float height){


        fillBag.add(new PositionComponent());
        fillBag.add(new FollowCameraComponent(x, y));
        fillBag.add(new DisablePlayerInputComponent());
        fillBag.add(new UIComponent());
        fillBag.add(new CollisionBoundComponent(new Rectangle(x, y, width, height)));
        fillBag.add(parentComponent);
        fillBag.add(new TextureFontComponent(FontAssets.medium, MenuStrings.TAP_TO_CONTINUE, new Color(1,1,1,0)));

        fillBag.add(new ActionAfterTimeComponent(new Action() {
            @Override
            public void performAction(World world, Entity e) {


                e.edit().add(new FadeComponent(true, 0.2f, false));

                e.edit().add(new ActionOnTouchComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        world.getSystem(OnDeathSystem.class).killChildComponentsIgnoreOnDeath(e.getComponent(ParentComponent.class));

                        e.deleteFromWorld();

                    }
                }));


            }
        }, 0.5f));


        return fillBag;

    }



























}
