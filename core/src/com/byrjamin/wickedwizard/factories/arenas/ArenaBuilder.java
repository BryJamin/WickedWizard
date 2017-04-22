package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.utils.MapCoords;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.factories.BackgroundFactory;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.utils.Measure;

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
        FULL, DOOR, NONE
    }

    private MapCoords defaultCoords;
    private Array<Section> sections = new Array<Section>();

    public static class Builder {

        //Required Parameters
        private final MapCoords startingCoords;
        private Arena arena;
        //private final float posX;
        //private final float posY;

        //optional Paramters
        private Array<Section> sections = new Array<Section>();

        public Builder(MapCoords startingCoords, Arena arena) {
            this.startingCoords = startingCoords;
            this.arena = arena;
        }

        public Builder section (Section section){
            sections.add(section);
         //   arena.cotainingCoords.add(section.coords);
            return this;
        }

        public ArenaBuilder build() {
            return new ArenaBuilder(this, arena);
        }

    }


    public ArenaBuilder(Builder builder, Arena arena){
        defaultCoords = builder.startingCoords;
        sections = builder.sections;


        for(Section s : sections) {


            //arena.cotainingCoords.add(s.coords);

            System.out.println(sections.size);

            int multX = s.coords.getX() - defaultCoords.getX();
            System.out.println("MultX is " + multX);
            int multY = s.coords.getY() - defaultCoords.getY();

            int coordX = defaultCoords.getX() + multX;
            int coordY = defaultCoords.getY() + multY;

            float posX = multX * SECTION_WIDTH;
            float posY = multY * SECTION_HEIGHT;



            //float


            //Left
            if(s.left == wall.FULL) {
                arena.addEntity(EntityFactory.wallBag(0 + posX, 0 + posY, WALLWIDTH, SECTION_HEIGHT));
            } else if(s.left == wall.DOOR){
                arena.addEntity(EntityFactory.wallBag(0 + posX, WALLWIDTH * 6 + posY, WALLWIDTH, SECTION_HEIGHT));
                arena.addDoor(EntityFactory.doorBag(0 + posX, Measure.units(10) + posY,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX - 1, coordY),
                        DoorComponent.DIRECTION.left));
            }
            //Right
            if(s.right == wall.FULL) {
                arena.addEntity(EntityFactory.wallBag(SECTION_WIDTH - WALLWIDTH + posX, 0 + posY, WALLWIDTH, SECTION_HEIGHT));
            } else if(s.right == wall.DOOR){
                arena.addEntity(EntityFactory.wallBag(SECTION_WIDTH - WALLWIDTH + posX, WALLWIDTH * 6 + posY, WALLWIDTH, SECTION_HEIGHT));
                arena.addDoor(EntityFactory.doorBag(SECTION_WIDTH - WALLWIDTH + posX, Measure.units(10) + posY,
                        new MapCoords(coordX, coordY),
                        new MapCoords(coordX + 1, coordY),
                        DoorComponent.DIRECTION.right));
            }

            //Ceiling
            if(s.ceiling != wall.NONE){
                arena.addEntity(EntityFactory.wallBag(0 + posX,  SECTION_HEIGHT - WALLWIDTH + posY, SECTION_WIDTH, WALLWIDTH));
                if(s.ceiling == wall.DOOR) {
                    arena.addDoor(EntityFactory.grateBag(SECTION_WIDTH / 2 + posX, 900 + posY,
                            new MapCoords(coordX, coordY),
                            new MapCoords(coordX, coordY + 1),
                            DoorComponent.DIRECTION.up));
                }
            }

            //Floor
            if(s.floor != wall.NONE){
                arena.addEntity(EntityFactory.wallBag(0 + posX,  -WALLWIDTH + posY, SECTION_WIDTH, WALLWIDTH * 3));

                if(s.floor == wall.DOOR) {
                    arena.addDoor(EntityFactory.grateBag(SECTION_WIDTH / 2 + posX, 400 + posY,
                            new MapCoords(coordX, coordY),
                            new MapCoords(coordX, coordY -1),
                            DoorComponent.DIRECTION.down));
                }
            }

            //Background
            arena.addEntity(BackgroundFactory.backgroundBags(0 + posX,0 + posY,
                    SECTION_WIDTH,
                    SECTION_HEIGHT,
                    Measure.units(15),
                    PlayScreen.atlas.findRegions("backgrounds/wall")));



        }

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
