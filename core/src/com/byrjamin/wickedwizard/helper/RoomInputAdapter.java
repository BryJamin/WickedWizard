package com.byrjamin.wickedwizard.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.gameobject.player.Wizard;
import com.byrjamin.wickedwizard.maps.rooms.Room;
import com.byrjamin.wickedwizard.maps.rooms.components.GrapplePoint;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomDoor;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomGrate;
import com.byrjamin.wickedwizard.maps.rooms.components.RoomWall;

/**
 * Created by Home on 18/01/2017.
 */
public class RoomInputAdapter extends InputAdapter{

    private Room room;
    private Viewport viewport;
    OrthographicCamera gamecam;

    private int movementPoll = -1;
    private int jumpPoll = -2;
    private int firingPoll = -3;
    private boolean jumpBool;
    private RoomWall movementWall;

    Vector3 input;

    public RoomInputAdapter(Room room, Viewport viewport){
        this.room = room;
        this.viewport = viewport;
    }


    public void update(Room room, Viewport viewport, OrthographicCamera gamecam){
        this.room = room;
        this.viewport = viewport;
        this.gamecam = gamecam;

        boolean test = room.getWizard().isFiring() ;

        if(movementPoll != -1) {
            if (Gdx.input.isTouched(movementPoll) && movementPoll != firingPoll) {
                float x1 = Gdx.input.getX(movementPoll);
                float y1 = Gdx.input.getY(movementPoll);
                input = new Vector3(x1, y1, 0);
                //This is so inputs match up to the game co-ordinates.
                gamecam.unproject(input);

                //System.out.println(input.y);

                //TODO not good;
                if (input.y <= 300 && !room.getWizard().isFlying()) {
                    room.getWizard().moveTo(input.x);
                    jumpPoll = movementPoll;
                }

            }
        }

    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        float x1 = Gdx.input.getX(pointer);
        float y1 = Gdx.input.getY(pointer);
        input = new Vector3(x1, y1, 0);
        //viewport.unproject(input);
        gamecam.unproject(input);

        Wizard w = room.getWizard();

        boolean fire = true;

        for(Rectangle r : room.getPlatforms()){
            if(r.contains(input.x, input.y)){
                if(w.getY() > r.getY() + r.getHeight()){
                    fire = false;
                    break;
                } else if(w.getY() == r.getY() + r.getHeight()){
                    room.getWizard().moveTo(input.x);
                    fire = false;
                    break;
                } else if(w.getY() < r.getY() + r.getHeight()){
                    room.getWizard().flyTo(input.x, input.y);
                    fire = false;
                    break;
                }
            }
        }

        for(RoomDoor r : room.getRoomDoors()){
            if(r.getBounds().contains(input.x, input.y) && r.isUnlocked()){
                room.getWizard().flyTo(input.x, input.y);
                w.toggleFallthroughOn();
                fire = false;
                break;
            }
        }

        for(RoomWall r : room.getRoomWalls()){
            if(r.getBounds().contains(input.x, input.y) ){
                if(w.getY() > r.getBounds().getY() + r.getBounds().getHeight()) {
                    room.getWizard().flyTo(input.x, r.getBounds().getY() + r.getBounds().getHeight());
                    System.out.println("INSIDE");
                    movementPoll = pointer;
                    fire = false;
                    break;
                } else if(w.getY() >= r.getBounds().getY() + r.getBounds().getHeight() - 100){
                    room.getWizard().moveTo(input.x);
                    movementWall = r;
                    movementPoll = pointer;
                    fire = false;
                    break;
                }
            }
        }

        for(RoomGrate r : room.getRoomGrates()){
            if(r.getBounds().contains(input.x, input.y) && r.isUnlocked()){
                r.setActive(true);
                room.getWizard().flyTo(input.x, input.y);
                fire = false;
            } else {
                r.setActive(false);
            }
        }

        for(GrapplePoint r : room.getGrapplePoints()){
            if(r.getBounds().contains(input.x, input.y)){
                room.getWizard().flyTo(r.getCenterX(), r.getCenterY());
                fire = false;
            }
        }

        if(fire) {
            room.getWizard().startFiring(pointer);
            firingPoll = pointer;
        }

        return true;
    }


    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        if(room.getWizard().getInputPoll() == pointer) {
            room.getWizard().stopFiring();
            firingPoll = -3;
        }

        Vector3 input = new Vector3(x,y,0);
        gamecam.unproject(input);

        System.out.println(jumpPoll +" Jumpoll");

        if(movementPoll == pointer && input.y > 300 && room.isWizardonGround()) {
            room.getWizard().jump();
            System.out.println("INSIDE");
        }

        if(movementPoll == pointer){
            movementPoll = -1;
        }

        return true;
    }

}

