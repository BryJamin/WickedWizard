package com.byrjamin.wickedwizard.factories.items;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.ecs.components.AltarComponent;
import com.byrjamin.wickedwizard.ecs.components.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.PickUpComponent;
import com.byrjamin.wickedwizard.ecs.components.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.object.WallComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.FadeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.HighlightComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.ShapeComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.CameraSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.MessageBannerSystem;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;

import java.util.Random;

/**
 * Created by Home on 09/04/2017.
 */

public class ItemFactory {


    public static ComponentBag createPickUpBag(float x, float y, PickUp pickUp){

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));

        Random random = new Random();

        bag.add(new VelocityComponent(random.nextInt((int) Measure.units(60f)) -Measure.units(30f), Measure.units(30f)));
        bag.add(new GravityComponent());
        bag.add(new PickUpComponent(pickUp));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, Measure.units(5), Measure.units(5))));
        bag.add(new TextureRegionComponent(pickUp.getRegion(), Measure.units(5), Measure.units(5),
                TextureRegionComponent.PLAYER_LAYER_FAR));

        return bag;
    }


    public static ComponentBag createFloatingItemBag(float x, float y, Item item){

        float width = Measure.units(8);
        float height = Measure.units(8);

        x = x - width / 2;
        y = y - height / 2;

        ComponentBag bag = new ComponentBag();
        bag.add(new PositionComponent(x,y));
        bag.add(new PickUpComponent(item));
        bag.add(new HighlightComponent());
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));
        bag.add(new TextureRegionComponent(item.getRegion(), width, height,
                TextureRegionComponent.PLAYER_LAYER_FAR));
        return bag;
    }


    public static Array<ComponentBag> createItemAltarBag(float x, float y, Item item){

        float width = Measure.units(15);
        float height = Measure.units(15);

        x = x - width / 2;
        y = y - height / 2;

        Array<ComponentBag> bags =  new Array<ComponentBag>();

        PositionComponent positionComponent = new PositionComponent(x,y);

        ComponentBag altarItemTexture = new ComponentBag();
        altarItemTexture.add(new PositionComponent());
        altarItemTexture.add(new TextureRegionComponent(item.getRegion(), Measure.units(5), Measure.units(5), TextureRegionComponent.FOREGROUND_LAYER_FAR));
        altarItemTexture.add(new FollowPositionComponent(positionComponent.position, width / 2 - Measure.units(2.5f), Measure.units(5)));

        ChildComponent c = new ChildComponent();
        altarItemTexture.add(c);

        ComponentBag bag = new ComponentBag();
        bag.add(new ParentComponent(c));
        bag.add(positionComponent);
        //bag.add(new PickUpComponent(item));
        bag.add(new AltarComponent(item));
        // bag.add(new HighlightComponent());
        bag.add(new VelocityComponent());
        bag.add(new GravityComponent());

        Rectangle bound = new Rectangle(new Rectangle(x,y, width, height / 3));

        bag.add(new CollisionBoundComponent(bound));
        bag.add(new TextureRegionComponent(PlayScreen.atlas.findRegion("altar"), width, height,
                TextureRegionComponent.PLAYER_LAYER_FAR));
        bag.add(new ProximityTriggerAIComponent(bound, activeAltar()));

        bags.add(altarItemTexture);
        bags.add(bag);





        return bags;
    }



    private static Action activeAltar (){

        return new Action() {
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(new HighlightComponent());


                AltarComponent ac = e.getComponent(AltarComponent.class);

                if(ac.hasItem) {
                    EntitySubscription subscription = world.getAspectSubscriptionManager().get(Aspect.all(PlayerComponent.class));
                    IntBag entityIds = subscription.getEntities();

                    for (int i = 0; i < entityIds.size(); i++) {
                        Entity player = world.getEntity(entityIds.get(i));
                        ac.item.applyEffect(world, player);

                        CollisionBoundComponent pBound = player.getComponent(CollisionBoundComponent.class);

                        Entity itemHoverAffect = world.createEntity();
                        itemHoverAffect.edit().add(new PositionComponent());
                        itemHoverAffect.edit().add(new FollowPositionComponent(player.getComponent(PositionComponent.class).position,
                                0, pBound.bound.getHeight() + pBound.bound.getHeight() / 4));
                        itemHoverAffect.edit().add(new TextureRegionComponent(ac.item.getRegion(),
                                Measure.units(5), Measure.units(5), TextureRegionComponent.PLAYER_LAYER_FAR));
                        itemHoverAffect.edit().add(new ExpireComponent(0.9f));

                        world.getSystem(MessageBannerSystem.class).createBanner(ac.item.getName(), ac.item.getDescription());

/*                        OrthographicCamera gamecam = world.getSystem(CameraSystem.class).getGamecam();

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
                        TextureFontComponent tfc = new TextureFontComponent(ac.item.getName() + "\n" + ac.item.getDescription(), 4);
                        tfc.layer = TextureRegionComponent.FOREGROUND_LAYER_NEAR;
                        tfc.color.a = 0;
                        itemText.edit().add(tfc);
                        itemText.edit().add(fc);
                        itemText.edit().add(ec);

                        //TODO something about scaling the font causes slow down possible due to scaling the bitmap font twice?
                        //TODO not sure but for now I'm using a slash n

                        Entity itemDescription = world.createEntity();
                        itemDescription.edit().add(new PositionComponent(gamecam.position.x - gamecam.viewportWidth / 2, gamecam.position.y + Measure.units(8)));
                        itemDescription.edit().add(new FollowPositionComponent(gamecam.position, -gamecam.viewportWidth / 2, Measure.units(8)));
                       // TextureFontComponent tfc2 = new TextureFontComponent(ac.item.getDescription());
                       // tfc2.font.getData().setScale(3,3);
                       // tfc2.color.a = 0;
                       // tfc2.layer = TextureRegionComponent.FOREGROUND_LAYER_NEAR;
                       // itemDescription.edit().add(tfc2);
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

                        blackBackingBox.edit().add(fc);*/


                    }

                    ac.hasItem = false;
                    world.getSystem(FindChildSystem.class).findChildEntity(e.getComponent(ParentComponent.class).children.first()).deleteFromWorld();

                }


                Entity child = world.getSystem(FindChildSystem.class).findChildEntity(e.getComponent(ParentComponent.class).children.first());
                if(child != null){
                    child.edit().add(new HighlightComponent());
                }
               // e.getComponent(ParentComponent.class).children.get(0)
            }

            @Override
            public void cleanUpAction(World w, Entity e) {
                e.edit().remove(HighlightComponent.class);

                Entity child = w.getSystem(FindChildSystem.class).findChildEntity(e.getComponent(ParentComponent.class).children.first());
                if(child != null){
                    child.edit().remove(HighlightComponent.class);
                }
            }
        };


    }



}
