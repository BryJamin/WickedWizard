package com.byrjamin.wickedwizard.factories.arenas;

import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.archive.maps.MapCoords;
import com.byrjamin.wickedwizard.ecs.components.object.DoorComponent;
import com.byrjamin.wickedwizard.factories.EntityFactory;
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

            System.out.println(sections.size);

            int multX = s.coords.getX() - defaultCoords.getX();
            System.out.println("MultX is " + multX);
            int multY = s.coords.getY() - defaultCoords.getY();

            float posX = multX * SECTION_WIDTH;
            float posY = multY * SECTION_HEIGHT;

            //Left
            if(s.left == wall.FULL) {
                arena.addEntity(EntityFactory.wallBag(0 + posX, 0 + posY, WALLWIDTH, SECTION_HEIGHT));
            } else if(s.right == wall.DOOR){
                arena.addEntity(EntityFactory.wallBag(0, WALLWIDTH * 6 + posY, WALLWIDTH, SECTION_HEIGHT));
                arena.addDoor(EntityFactory.doorBag(0 + posX, Measure.units(10) + posY,
                        new MapCoords(defaultCoords.getX() + multX, defaultCoords.getY() + multY),
                        new MapCoords(defaultCoords.getX() - 1 + multX, defaultCoords.getY() + multY),
                        DoorComponent.DIRECTION.left));
            }
            //Right
            if(s.right == wall.FULL) {
                arena.addEntity(EntityFactory.wallBag(SECTION_WIDTH - WALLWIDTH + posX, 0 + posY, WALLWIDTH, SECTION_HEIGHT));
            } else if(s.right == wall.DOOR){
                arena.addEntity(EntityFactory.wallBag(SECTION_WIDTH - WALLWIDTH, WALLWIDTH * 6 + posY, WALLWIDTH, SECTION_HEIGHT));
                arena.addDoor(EntityFactory.doorBag(SECTION_WIDTH - WALLWIDTH + posX, Measure.units(10) + posY,
                        new MapCoords(defaultCoords.getX() + multX, defaultCoords.getY() + multY),
                        new MapCoords(defaultCoords.getX() + 1 + multX, defaultCoords.getY() + multY),
                        DoorComponent.DIRECTION.right));
            }

            //Ceiling
            if(s.ceiling){
                arena.addEntity(EntityFactory.wallBag(0 + posX,  SECTION_HEIGHT - WALLWIDTH + posY, SECTION_WIDTH, WALLWIDTH));
            }

            //Floor
            if(s.floor){
                arena.addEntity(EntityFactory.wallBag(0 + posX,  -WALLWIDTH + posY, SECTION_WIDTH, WALLWIDTH * 3));
            }


        }

    }







    public static class Section {

        public MapCoords coords;
        public wall left;
        public wall right;
        public boolean ceiling;
        public boolean floor;


        public Section(MapCoords coords, wall left, wall right, boolean ceiling, boolean floor){
            this.coords = coords;
            this.left = left;
            this.right = right;
            this.ceiling = ceiling;
            this.floor = floor;
        }

    }

}
