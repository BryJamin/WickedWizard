package com.byrjamin.wickedwizard.ecs.systems.input;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.ecs.components.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GlideComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.JumpComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.GrapplePointSystem;
import com.byrjamin.wickedwizard.ecs.systems.MoveToSystem;
import com.byrjamin.wickedwizard.factories.PlayerFactory;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.timer.StateTimer;

/**
 * Created by Home on 14/04/2017.
 */

public class PlayerInput extends InputAdapter{

    private World world;
    private Rectangle movementArea;

    public float MAX_GRAPPLE_LAUNCH = Measure.units(60f);
    private float MAX_GRAPPLE_MOVEMENT = Measure.units(150f);
    private float GRAPPLE_MOVEMENT = Measure.units(15f);

    public Integer movementInputPoll;
    public Integer firingInputPoll;
    public Integer tapInputPoll;
    public boolean activeGrapple;

    public StateTimer tapInputTimer = new StateTimer(0.25f);


    private boolean inTapSquare;

    private Viewport gameport;
    private Array<ChildComponent> wingChildren = new Array<ChildComponent>();


    public PlayerInput(World world, Viewport gameport, Rectangle movementArea){
        this.world = world;
        this.movementArea = movementArea;
        this.gameport = gameport;
    }


    public void update(World world){
        this.world = world;
    }



    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        if(pointer > 1) return false;

        Vector3 touchInput = new Vector3(screenX, screenY, 0);
        gameport.unproject(touchInput);

        if(!activeGrapple) {
            activeGrapple = world.getSystem(GrapplePointSystem.class).touchedGrapple(touchInput.x, touchInput.y);
        }
        if(!activeGrapple) {
            if (touchInput.y <= movementArea.y + movementArea.getHeight()) {
                movementInputPoll = pointer;
                world.getSystem(FindPlayerSystem.class).getPC(GravityComponent.class).ignoreGravity = false;
            } else if(firingInputPoll == null){
                firingInputPoll = pointer;
            }
            tapInputTimer.reset();
        } else {
            world.getSystem(PlayerInputSystem.class).grappleTo(touchInput.x, touchInput.y);
        }

        world.getSystem(ActiveOnTouchSystem.class).activeOnTouchTrigger(touchInput.x, touchInput.y);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        if(pointer > 1) return false;

        if(!activeGrapple) {

            //TODO Find player get player should be player position or something if player can't be found
            if(!tapInputTimer.isFinished()/*&& jm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).jumps > 0*/) {


                Vector3 input = new Vector3(screenX, screenY, 0);
                gameport.unproject(input);

                CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
                VelocityComponent vc = world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class);
                JumpComponent jc = world.getSystem(FindPlayerSystem.class).getPC(JumpComponent.class);

                if(input.y > cbc.getCenterY()) {
                    if (jc.jumps > 0) {
                        vc.velocity.y = Measure.units(80f);
                        jc.jumps--;
                        world.getSystem(PlayerInputSystem.class).turnOffGlide();
                        world.getSystem(PlayerInputSystem.class).turnOnGlide();
                    }

                } else {
                    world.getSystem(PlayerInputSystem.class).turnOffGlide();
                }
                tapInputTimer.reset();
            }


        }

        if(movementInputPoll != null) {
            movementInputPoll = (movementInputPoll == pointer) ? null : movementInputPoll;
        }

        if(firingInputPoll != null) {
            firingInputPoll = (firingInputPoll == pointer) ? null : firingInputPoll;

            if(firingInputPoll == null){
                world.getSystem(FindPlayerSystem.class).getPC(WeaponComponent.class).timer.reset();
            }

        }
        return false;
    }


    public void setWorld(World world) {
        this.world = world;
    }
}
