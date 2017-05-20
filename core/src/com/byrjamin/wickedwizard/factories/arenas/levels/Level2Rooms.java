package com.byrjamin.wickedwizard.factories.arenas.levels;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.factories.AbstractFactory;
import com.byrjamin.wickedwizard.factories.arenas.Arena;
import com.byrjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.byrjamin.wickedwizard.factories.arenas.ArenaGen;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.factories.chests.ChestFactory;
import com.byrjamin.wickedwizard.factories.enemy.TurretFactory;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.utils.Measure;

import static com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_HEIGHT;
import static com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory.SECTION_WIDTH;

/**
 * Created by Home on 17/05/2017.
 */

public class Level2Rooms extends AbstractFactory{

    ArenaShellFactory arenaShellFactory;
    ChestFactory chestFactory;
    DecorFactory decorFactory;
    ArenaEnemyPlacementFactory arenaEnemyPlacementFactory;
    TurretFactory turretFactory;

    ArenaSkin arenaSkin;

    public Level2Rooms(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.arenaShellFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaShellFactory(assetManager, arenaSkin);
        this.chestFactory = new ChestFactory(assetManager);
        this.arenaEnemyPlacementFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.ArenaEnemyPlacementFactory(assetManager, arenaSkin);
        this.decorFactory = new com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.turretFactory = new TurretFactory(assetManager);
        this.arenaSkin = arenaSkin;
    }


    public Array<ArenaGen> getLevel2RoomArray(){
        Array<ArenaGen> ag = new Array<ArenaGen>();
        ag.add(room4Kugel());
        ag.add(goatWizardCenter());
        ag.add(oneTurretTwoBouncers());
        return ag;
    }


    public ArenaGen room4Kugel(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                MapCoords m = new MapCoords();
                Arena arena = new Arena(arenaSkin, m);

                arena.setWidth(SECTION_WIDTH);
                arena.setHeight(SECTION_HEIGHT);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(m,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                arena.addEntity(arenaEnemyPlacementFactory.kugelDuscheFactory.kugelDusche(arena.getWidth() / 2,(arena.getHeight() / 2) + Measure.units(2.5f)));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }

    public ArenaGen goatWizardCenter(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                MapCoords m = new MapCoords();
                Arena arena = new Arena(arenaSkin, m);

                arena.setWidth(SECTION_WIDTH);
                arena.setHeight(SECTION_HEIGHT);

                arena = new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(m,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR))
                        .buildArena(arena);

                arenaEnemyPlacementFactory.spawnGoatWizard(arena, arena.getWidth() / 2,(arena.getHeight() / 2));
                arena.roomType = Arena.RoomType.TRAP;
                return arena;
            }
        };
    }


    public ArenaGen oneTurretTwoBouncers(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {
                Arena a = arenaShellFactory.createOmniArenaHiddenGrapple(new MapCoords());
                a.addEntity(arenaEnemyPlacementFactory.spawnFixedSentry(a.getWidth() / 2, a.getHeight() / 2));
                a.addEntity(arenaEnemyPlacementFactory.spawnLargeBouncer(a.getWidth() - Measure.units(20), a.getHeight() - Measure.units(15f)));
                a.addEntity(arenaEnemyPlacementFactory.spawnLargeBouncer(Measure.units(20), a.getHeight() - Measure.units(15f)));
                a.roomType = Arena.RoomType.TRAP;
                return a;
            }
        };
    }




    public ArenaGen width2RoomOnlyVerticalExits(){
        return new ArenaGen() {
            @Override
            public Arena createArena() {


                MapCoords defaultCoords = new MapCoords(0,0);

                Arena arena = new Arena(arenaSkin, defaultCoords, new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()));

                arena.setWidth(SECTION_WIDTH * 2);
                arena.setHeight(SECTION_HEIGHT);

                arena =  new ArenaBuilder(assetManager, arenaSkin)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.FULL,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR)).buildArena(arena);


                arena.addEntity(decorFactory.wallBag(Measure.units(20), Measure.units(20), arena.getWidth() - Measure.units(40), Measure.units(25f), arenaSkin));

                arena.addEntity(decorFactory.grapplePointBag(Measure.units(12.5f), (arena.getHeight() / 2) + Measure.units(2.5f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(12.5f), (arena.getHeight() / 2) + Measure.units(2.5f)));



                return arena;
            }
        };
    }



}
