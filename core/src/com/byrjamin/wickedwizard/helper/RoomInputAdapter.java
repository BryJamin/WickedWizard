package com.byrjamin.wickedwizard.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.entity.player.Wizard;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomExit;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomTeleporter;
import com.byrjamin.wickedwizard.maps.rooms.layout.RoomWall;

/**
 * Created by Home on 18/01/2017.
 */
public class RoomInputAdapter extends InputAdapter{

    private Room room;
    private Viewport viewport;

    Vector3 input;

    public RoomInputAdapter(Room room, Viewport viewport){
        this.room = room;
        this.viewport = viewport;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        float x1 = Gdx.input.getX(pointer);
        float y1 = Gdx.input.getY(pointer);
        input = new Vector3(x1, y1, 0);
        viewport.unproject(input);

        Wizard w = room.getWizard();

        boolean fire = true;

        for(Rectangle r : room.getPlatforms()){
            if(r.contains(input.x, input.y)){
                if(w.getY() > r.getY() + r.getHeight()){
                    fire = false;
                    break;
                } else if(w.getY() == r.getY() + r.getHeight()){
                    room.getWizard().dash(input.x);
                    fire = false;
                    break;
                } else if(w.getY() < r.getY() + r.getHeight()){
                    room.getWizard().flyTo(input.x, input.y);
                    fire = false;
                    break;
                }
            }
        }

        for(RoomExit r : room.getRoomExits()){
            if(r.getBound().contains(input.x, input.y) && r.isOpen()){
                room.getWizard().flyTo(input.x, input.y);
                w.toggleFallthroughOn();
                fire = false;
                break;
            }
        }

        for(RoomWall r : room.getRoomWalls()){
            if(r.getBounds().contains(input.x, input.y) ){
                if(w.getY() >= r.getBounds().getY() + r.getBounds().getHeight() - 100){
                    room.getWizard().dash(input.x);
                    fire = false;
                    break;
                }
            }
        }

        for(RoomTeleporter r : room.getRoomTeleporters()){
            if(r.getBounds().contains(input.x, input.y) && r.isOpen()){
                r.setActive(true);
                room.getWizard().flyTo(input.x, input.y);
            } else {
                r.setActive(false);
            }
        }

        if(fire) {
           room.getWizard().startFiring(pointer);
        }

        return true;
    }


    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if(room.getWizard().getInputPoll() == pointer) {
            room.getWizard().stopFiring();
        }
        return true;
    }

}

