package com.bryjamin.wickedwizard.factories.arenas.bossrooms;

import com.badlogic.gdx.assets.AssetManager;
import com.bryjamin.wickedwizard.factories.AbstractFactory;
import com.bryjamin.wickedwizard.factories.arenas.Arena;
import com.bryjamin.wickedwizard.factories.arenas.ArenaBuilder;
import com.bryjamin.wickedwizard.factories.arenas.ArenaCreate;
import com.bryjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.bryjamin.wickedwizard.factories.enemy.bosses.BossKugelDusc;
import com.bryjamin.wickedwizard.utils.MapCoords;
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


    public ArenaCreate giantKugelArena(){
        return new ArenaCreate() {
            @Override
            public Arena createArena(MapCoords defaultCoords) {

                Arena arena =  new ArenaBuilder(assetManager, arenaSkin, Arena.ArenaType.TRAP)
                        .addSection(new ArenaBuilder.Section(defaultCoords,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY()),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX(), defaultCoords.getY() + 1),
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE))
                        .addSection(new ArenaBuilder.Section(new MapCoords(defaultCoords.getX() + 1, defaultCoords.getY() + 1),
                                ArenaBuilder.wall.NONE,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.DOOR,
                                ArenaBuilder.wall.NONE)).buildArena();


                arena.addEntity(new BossKugelDusc(assetManager).giantKugelDusche(arena.getWidth() / 2, arena.getHeight() / 2 - Measure.units(10f)));

                arena.addEntity(decorFactory.grapplePointBag(Measure.units(30f), Measure.units(35f)));
                arena.addEntity(decorFactory.grapplePointBag(Measure.units(30f), Measure.units(70f)));

                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(30f), Measure.units(35f)));
                arena.addEntity(decorFactory.grapplePointBag(arena.getWidth() - Measure.units(30f), Measure.units(70f)));

                float width = Measure.units(125f);
                //arena.addEntity(decorFactory.wallBag(CenterMath.offsetX(arena.getWidth(), width), arena.getHeight() - Measure.units(25f), width, Measure.units(5f)));
                arena.addEntity(decorFactory.platform(Measure.units(0), arena.getHeight() - Measure.units(35f), arena.getWidth()));

                return arena;
            }
        };
    }






}
