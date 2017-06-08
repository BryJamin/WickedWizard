package com.byrjamin.wickedwizard.factories.arenas;

import com.artemis.Component;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.factories.arenas.decor.DecorFactory;
import com.byrjamin.wickedwizard.factories.arenas.skins.ArenaSkin;
import com.byrjamin.wickedwizard.utils.BagSearch;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.factories.BackgroundFactory;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 16/04/2017.
 */

public class ArenaBuilder {

    public static final float SECTION_WIDTH = MainGame.GAME_WIDTH;
    public static final float SECTION_HEIGHT = MainGame.GAME_HEIGHT;

/*    public static final float WIDTH = MainGame.GAME_WIDTH;
    public static final float HEIGHT = MainGame.GAME_HEIGHT;*/

    public static final float WALLWIDTH = Measure.units(5);

    public enum wall {
        FULL, DOOR, MANDATORYDOOR, NONE, GRAPPLE, MANDATORYGRAPPLE; //TODO waaaaay too hackkkkkyy
    }

    private MapCoords defaultCoords;
    private Array<Section> sections = new Array<Section>();

    private AssetManager assetManager;
    private ArenaSkin arenaSkin;
    private BackgroundFactory bf = new BackgroundFactory();

    public ArenaBuilder(AssetManager assetManager, ArenaSkin arenaSkin){
        this.assetManager = assetManager;
        this.arenaSkin = arenaSkin;
    }

    public ArenaBuilder addSection(Section s){
        sections.add(s);
        return this;
    }


    public Arena buildArena(Arena arena){

        defaultCoords = arena.getStartingCoords();



        DecorFactory decorFactory = new DecorFactory(assetManager, arenaSkin);

        for(Section s : sections) {


            //arena.cotainingCoords.add(s.coords);

            //System.out.println(sections.size);

            int multX = s.coords.getX() - defaultCoords.getX();
            int multY = s.coords.getY() - defaultCoords.getY();

            int coordX = defaultCoords.getX() + multX;
            int coordY = defaultCoords.getY() + multY;

            float posX = multX * SECTION_WIDTH;
            float posY = multY * SECTION_HEIGHT;



            //float


            //Left
            if(s.left == wall.FULL) {
                arena.addEntity(decorFactory.wallBag(0 + posX, 0 + posY, WALLWIDTH, SECTION_HEIGHT, arenaSkin));
            } else if(s.left == wall.DOOR || s.left == wall.MANDATORYDOOR){
                arena.addEntity(decorFactory.wallBag(0 + posX, WALLWIDTH * 6 + posY, WALLWIDTH, SECTION_HEIGHT - WALLWIDTH * 4, arenaSkin));
                arena.addDoor(decorFactory.doorBag(0 + posX, Measure.units(10) + posY,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX - 1, coordY),
                        Direction.LEFT), s.left == wall.MANDATORYDOOR);
            }
            //Right
            if(s.right == wall.FULL) {
                arena.addEntity(decorFactory.wallBag(SECTION_WIDTH - WALLWIDTH + posX, 0 + posY, WALLWIDTH, SECTION_HEIGHT, arenaSkin));
            } else if(s.right == wall.DOOR || s.right == wall.MANDATORYDOOR){
                arena.addEntity(decorFactory.wallBag(SECTION_WIDTH - WALLWIDTH + posX, WALLWIDTH * 6 + posY, WALLWIDTH, SECTION_HEIGHT -  WALLWIDTH * 4, arenaSkin));
                arena.addDoor(decorFactory.doorBag(SECTION_WIDTH - WALLWIDTH + posX, Measure.units(10) + posY,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX + 1, coordY),
                        Direction.RIGHT), s.right == wall.MANDATORYDOOR);
            }

            //Ceiling
            if(s.ceiling == wall.FULL){
                arena.addEntity(decorFactory.wallBag(0 + posX,  SECTION_HEIGHT - WALLWIDTH + posY, SECTION_WIDTH, WALLWIDTH, arenaSkin));

            } else if(s.ceiling == wall.DOOR || s.ceiling == wall.MANDATORYDOOR ||s.ceiling == wall.GRAPPLE || s.ceiling == wall.MANDATORYGRAPPLE) {

                arena.addEntity(decorFactory.wallBag(0 + posX,  SECTION_HEIGHT - WALLWIDTH + posY, Measure.units(40f), WALLWIDTH, arenaSkin));

                arena.addDoor(decorFactory.horizontalDoorBag(Measure.units(40f) + posX, SECTION_HEIGHT - WALLWIDTH + posY,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX, coordY + 1),
                        Direction.UP), (s.ceiling == wall.MANDATORYDOOR || s.ceiling == wall.MANDATORYGRAPPLE));

                arena.addEntity(decorFactory.wallBag(Measure.units(60) + posX,  SECTION_HEIGHT - WALLWIDTH + posY, Measure.units(40f), WALLWIDTH, arenaSkin));

                if(s.ceiling == wall.GRAPPLE || s.ceiling == wall.MANDATORYGRAPPLE){

                    ComponentBag bag = decorFactory.hiddenGrapplePointBag(posX + SECTION_WIDTH / 2, posY + ((SECTION_HEIGHT / 4) * 3));
                    DoorComponent dc = new DoorComponent(
                            new MapCoords(coordX, coordY),
                            new MapCoords(coordX, coordY + 1),
                            Direction.UP);
                    dc.ignore = true;
                    bag.add(dc);
                    arena.addDoor(bag);


                }

            }

            //Floor
            if(s.floor == wall.FULL){
                arena.addEntity(decorFactory.wallBag(0 + posX,  0 + posY, SECTION_WIDTH, WALLWIDTH * 2, arenaSkin));
            } else if(s.floor == wall.DOOR || s.floor == wall.MANDATORYDOOR) {

                arena.addEntity(decorFactory.wallBag(0 + posX,  0 + posY, Measure.units(40f), WALLWIDTH * 2, arenaSkin));

                arena.addDoor(decorFactory.horizontalDoorBag(Measure.units(40f) + posX, 0 + posY,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX, coordY - 1),
                        Direction.DOWN), s.floor == wall.MANDATORYDOOR);

                Bag<Component> bag = decorFactory.horizontalDoorBag(Measure.units(40f) + posX, Measure.units(5f) + posY,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX, coordY - 1),
                        Direction.DOWN);

                BagSearch.getObjectOfTypeClass(DoorComponent.class, bag).ignore = true;


                arena.addEntity(decorFactory.platform(Measure.units(40f) + posX, Measure.units(5f), Measure.units(20f)));

                arena.addDoor(bag);

                arena.addEntity(decorFactory.wallBag(Measure.units(60) + posX,  0 + posY, Measure.units(40f), WALLWIDTH * 2, arenaSkin));

            }

            //Background
            arena.addEntity(bf.backgroundBags(0 + posX,0 + posY,
                    SECTION_WIDTH,
                    SECTION_HEIGHT,
                    Measure.units(15),
                    arenaSkin.getBackgroundTextures(),
                    arenaSkin));



        }



        return arena;




    }




    public Section findSection(MapCoords mapCoords) {
        for(Section s : sections) {
            if(s.coords.equals(mapCoords)) {
                return s;
            }
        }
        return null;
    }







    public static class Section {

        public MapCoords coords;
        public wall left;
        public wall right;
        public wall ceiling;
        public wall floor;


        public Section(MapCoords coords, wall left, wall right, wall ceiling, wall floor){
            this.coords = coords;
            this.left = left;
            this.right = right;
            this.ceiling = ceiling;
            this.floor = floor;
        }

    }

}
