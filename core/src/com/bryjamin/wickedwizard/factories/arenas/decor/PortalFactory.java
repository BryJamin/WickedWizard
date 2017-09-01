package com.bryjamin.wickedwizard.factories.arenas.decor;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ProximityTriggerAIComponent;
import com.bryjamin.wickedwizard.ecs.components.ai.Task;
import com.bryjamin.wickedwizard.ecs.components.identifiers.BossTeleporterComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.bryjamin.wickedwizard.ecs.systems.input.PlayerInputSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.bryjamin.wickedwizard.ecs.systems.level.EndGameSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.MapTeleportationSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.bryjamin.wickedwizard.ecs.systems.level.ScreenWipeSystem;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.presets.BreakRoom;
import com.bryjamin.wickedwizard.screens.DataSave;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 26/07/2017.
 */

public class PortalFactory extends AbstractFactory {


    public static final float mapPortalSize = Measure.units(10f);
    public static final float levelPortalSize = Measure.units(20f);


    public PortalFactory(AssetManager assetManager) {
        super(assetManager);
    }



    public com.bryjamin.wickedwizard.utils.ComponentBag mapPortal (float x, float y, BossTeleporterComponent btc){

        com.bryjamin.wickedwizard.utils.ComponentBag bag = portal(x,y, mapPortalSize, mapPortalSize, new Task() {
            @Override
            public void performAction(World world, Entity e) {
                world.getSystem(MapTeleportationSystem.class).goFromSourceToDestination(e.getComponent(BossTeleporterComponent.class));
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        });

        bag.add(btc);

        return bag;

    }


    public com.bryjamin.wickedwizard.utils.ComponentBag endGamePortal(float x, float y){

        com.bryjamin.wickedwizard.utils.ComponentBag bag = portal(x,y, mapPortalSize, mapPortalSize, new Task() {
            @Override
            public void performAction(World world, Entity e) {

                world.getSystem(ScreenWipeSystem.class).startScreenWipe(ScreenWipeSystem.Transition.FADE, new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        world.getSystem(EndGameSystem.class).backToMenu();

                    }
                });
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        });

        return bag;
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag endChallengePortal(float x, float y, final String id){

        com.bryjamin.wickedwizard.utils.ComponentBag bag = portal(x,y, mapPortalSize, mapPortalSize, new Task() {
            @Override
            public void performAction(World world, Entity e) {

                DataSave.saveChallengeData(id);

                world.getSystem(ScreenWipeSystem.class).startScreenWipe(ScreenWipeSystem.Transition.FADE, new Action() {
                    @Override
                    public void performAction(World world, Entity e) {
                        world.getSystem(EndGameSystem.class).backToMenu();

                    }
                });
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        });

        return bag;
    }

    public com.bryjamin.wickedwizard.utils.ComponentBag customSmallPortal(float x, float y, final Action action){

        return portal(x, y, mapPortalSize, mapPortalSize, new Task() {
            @Override
            public void performAction(World world, Entity e) {
                action.performAction(world, e);
            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }
        });


    }


    public com.bryjamin.wickedwizard.utils.ComponentBag portal(float x, float y, float width, float height, Task task){

        x = x - width / 2;
        y = y - height / 2;

        com.bryjamin.wickedwizard.utils.ComponentBag bag = new com.bryjamin.wickedwizard.utils.ComponentBag();
        bag.add(new PositionComponent(x, y));


        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height)));

        AnimationStateComponent sc = new AnimationStateComponent(AnimationStateComponent.DEFAULT);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(AnimationStateComponent.DEFAULT, new Animation<TextureRegion>(0.1f, atlas.findRegions(com.bryjamin.wickedwizard.assets.TextureStrings.TELEPORTER), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));

        bag.add(new TextureRegionComponent(atlas.findRegion(com.bryjamin.wickedwizard.assets.TextureStrings.TELEPORTER),
                width,
                height,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        bag.add(new ProximityTriggerAIComponent(task, new HitBox(new Rectangle(x,y, width, height))));


        return bag;

    }


    public com.bryjamin.wickedwizard.utils.ComponentBag levelPortal(float x, float y){

        return portal(x,y, levelPortalSize, levelPortalSize, new Task() {
            @Override
            public void performAction(World world, Entity e) {

                world.getSystem(ScreenWipeSystem.class).startScreenWipe(ScreenWipeSystem.Transition.FADE, new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);
                        rts.packRoom(world, rts.getCurrentArena());
                        rts.setCurrentMap(new ArenaMap(new BreakRoom(assetManager).createBreakRoom()));
                        rts.unpackRoom(rts.getCurrentArena());

                        PositionComponent player =  world.getSystem(FindPlayerSystem.class).getPlayerComponent(PositionComponent.class);
                        player.position.x = rts.getCurrentArena().getWidth() / 2;
                        player.position.y = rts.getCurrentArena().getHeight() / 2;
                        VelocityComponent vc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(VelocityComponent.class);
                        MoveToComponent mtc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(MoveToComponent.class);
                        vc.velocity.x = 0;
                        vc.velocity.y = 0;
                        world.getSystem(PlayerInputSystem.class).turnOffGlide();

                       // world.getSystem(MapTeleportationSystem.class).createNewLevel();
                    }
                });

            }

            @Override
            public void cleanUpAction(World world, Entity e) {

            }

        });

    }



}
