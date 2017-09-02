package com.bryjamin.wickedwizard.factories.arenas.bossrooms;

import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by Home on 27/05/2017.
 */

public class BossRoomGiantKugelRoom extends AbstractFactory {

    private com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory decorFactory;
    private ArenaSkin arenaSkin;


    public BossRoomGiantKugelRoom(AssetManager assetManager, ArenaSkin arenaSkin) {
        super(assetManager);
        this.decorFactory = new com.bryjamin.wickedwizard.factories.arenas.decor.DecorFactory(assetManager, arenaSkin);
        this.arenaSkin = arenaSkin;
    }


    public com.bryjamin.wickedwizard.factories.arenas.ArenaCreate giantKugelArena(){
        return new com.bryjamin.wickedwizard.factories.arenas.ArenaCreate() {
            @Override
            public com.bryjamin.wickedwizard.factories.arenas.Arena createArena(com.bryjamin.wickedwizard.utils.MapCoords defaultCoords) {

                com.bryjamin.wickedwizard.factories.arenas.Arena arena =  new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder(assetManager, arenaSkin, com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP)
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(defaultCoords,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE))
                        .addSection(new com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.Section(new com.bryjamin.wickedwizard.utils.MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1),
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.DOOR,
                                com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder.wall.NONE)).buildArena();


                arena.addEntity(new com.bryjamin.wickedwizard.factories.enemy.bosses.BossKugelDusc(assetManager).giantKugelDusche(arena.getWidth() / 2, arena.getHeight() / 2 - Measure.units(10f)));

                arena.addEntity(decorFactory.grapplePointBag(Measure.units(30f), Measure.units(50f)));
                arena.addEntity(decorFactory.grapplePointBag(Measure.units(30f), Measure.units(80f)));

                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(35f), Measure.units(50f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(35f), Measure.units(80f)));


                arena.addEntity(decorFactory.grapplePointBag(Measure.units(50f), Measure.units(100)));

                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() / 2, Measure.units(100f)));

                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(55), Measure.units(100f)));

                arena.arenaType = com.bryjamin.wickedwizard.factories.arenas.Arena.ArenaType.TRAP;

                return arena;
            }
        };
    }






}
