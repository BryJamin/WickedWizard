package com.byrjamin.wickedwizard.factories.arenas.bossrooms;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ActionAfterTimeComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.ExpireComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FollowPositionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.InCombatActionComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.Task;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ArenaLockComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.level.ArenaMap;
import com.byrjamin.wickedwizard.ecs.systems.level.RoomTransitionSystem;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.presetmaps.GalleryAtTheEndMap;
import com.byrjamin.wickedwizard.factories.arenas.skins.AllBlackSkin;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.factories.enemy.bosses.BossTheEnd;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;

import static com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH;

/**
 * Created by Home on 09/07/2017.
 */

public class BossArenaEndBoss extends AbstractFactory {


    private ArenaShellFactory arenaShellFactory;
    private ChestFactory chestFactory;
    private DecorFactory decorFactory;
    private ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    private ArenaSkin arenaSkin;


    private static final float playerSummonTime = 2f;


    public BossArenaEndBoss(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);

        this.arenaShellFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        //this.arenaEnemyPlacementFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory(assetManager, arenaSkin);
        this.decorFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.arenaSkin = arenaSkin; //arenaSkin;

    }



    public ArenaMap theEndMapAdventureMode(final ArenaMap destinationMapAfterBossKill){

        Arena arena = endArena().createArena(new MapCoords(0,0));

        arena.createArenaBag().add(new InCombatActionComponent(new Task() {
            @Override
            public void performAction(World world, Entity e) {

            }

            @Override
            public void cleanUpAction(World world, Entity e) {
                //e.edit().remove(ConditionalActionComponent.class);

                RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);
                rts.packRoom(world, rts.getCurrentArena());
                rts.setCurrentMap(destinationMapAfterBossKill);
                rts.unpackRoom(rts.getCurrentArena());

                Arena a = world.getSystem(RoomTransitionSystem.class).getCurrentArena();

                PositionComponent pc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(PositionComponent.class);
                pc.position.x = a.getWidth() / 2;
                pc.position.y = a.getHeight() / 2;
            }
        }));


        return new ArenaMap(arena);
    }


    public ArenaCreate endStartingRoom(final ArenaMap destinationMapAfterBossKill) {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL))
                        .buildArena();


                arena.createArenaBag().add(new ActionAfterTimeComponent(new Action() {
                    @Override
                    public void performAction(World world, Entity e) {

                        createWhiteFlash(world);

                        RoomTransitionSystem rts = world.getSystem(RoomTransitionSystem.class);
                        rts.packRoom(world, rts.getCurrentArena());
                        rts.setCurrentMap(theEndMapAdventureMode(destinationMapAfterBossKill));
                        rts.unpackRoom(rts.getCurrentArena());


                    }
                }, playerSummonTime));


                arena.createArenaBag().add(new ArenaLockComponent());



                return arena;
            }
        };
    }


    public ArenaCreate endStartingRoom() {
        return endStartingRoom(new GalleryAtTheEndMap(assetManager).endGameMap());
    }



    public ArenaCreate endArena() {
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena = new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                new AllBlackSkin()))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 20),
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                new AllBlackSkin()))
                        .buildArena();

                arena.addEntity(new BossTheEnd(assetManager).end(SECTION_WIDTH / 2, Measure.units(35f)));

                return arena;
            }
        };
    }




    public Entity createWhiteFlash(World world){
        float width = ArenaShellFactory.SECTION_WIDTH * 3;
        float height = ArenaShellFactory.SECTION_HEIGHT * 3;

        Entity e = world.createEntity();
        e.edit().add(new FollowPositionComponent(world.getSystem(FindPlayerSystem.class).getPlayerComponent(PositionComponent.class).position, -width / 2, -height / 2));
        e.edit().add(new PositionComponent(0, 0));
        e.edit().add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOCK), width, height, TextureRegionComponent.FOREGROUND_LAYER_NEAR,
                new Color(Color.WHITE)));
        e.edit().add(new ExpireComponent(0.2f));
        return e;
    }


}
