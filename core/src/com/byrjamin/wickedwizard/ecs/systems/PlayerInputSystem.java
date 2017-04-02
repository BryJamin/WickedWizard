package com.byrjamin.wickedwizard.ecs.systems;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.ecs.components.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.JumpComponent;
import com.byrjamin.wickedwizard.ecs.components.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.components.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.factories.BulletFactory;
import com.byrjamin.wickedwizard.factories.EntityFactory;
import com.byrjamin.wickedwizard.helper.Measure;
import com.byrjamin.wickedwizard.helper.collider.Collider;
import com.byrjamin.wickedwizard.helper.timer.StateTimer;

/**
 * Created by Home on 04/03/2017.
 */
public class PlayerInputSystem extends EntityProcessingSystem implements InputProcessor {

    public float MAX_GRAPPLE_LAUNCH = Measure.units(60f);
    private float MAX_GRAPPLE_MOVEMENT = Measure.units(150f);
    private float GRAPPLE_MOVEMENT = Measure.units(15f);


    ComponentMapper<PositionComponent> pm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<AccelerantComponent> am;
    ComponentMapper<MoveToComponent> mtm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<WeaponComponent> wm;
    ComponentMapper<AnimationStateComponent> sm;
    ComponentMapper<GravityComponent> gm;
    ComponentMapper<TextureRegionComponent> trm;
    ComponentMapper<JumpComponent> jm;



    OrthographicCamera gamecam;
    Viewport gameport;

    Vector3 touchInput;
    Vector3 unprojectedInput;

    public Vector2 grappleDestination;
    Vector2 flyVelocity;

    public boolean hasTarget;
    private boolean canFire;

    private StateTimer jumpTimer = new StateTimer(0.25f);

    private Integer movementInputPoll = null;
    private Integer firingInputPoll = null;

    @SuppressWarnings("unchecked")
    public PlayerInputSystem(OrthographicCamera gamecam, Viewport gameport) {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class, PlayerComponent.class, AnimationStateComponent.class));
        this.gamecam = gamecam;
        this.gameport = gameport;
    }

    @Override
    protected void initialize() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        AccelerantComponent ac = am.get(e);
        CollisionBoundComponent cbc = cbm.get(e);
        WeaponComponent wc = wm.get(e);
        AnimationStateComponent sc = sm.get(e);
        TextureRegionComponent trc = trm.get(e);
        GravityComponent gc = gm.get(e);
        MoveToComponent mtc = mtm.get(e);

        jumpTimer.update(world.getDelta());


        if(grappleDestination != null){
            if(Math.abs(vc.velocity.x) < MAX_GRAPPLE_MOVEMENT && Math.abs(vc.velocity.y) < MAX_GRAPPLE_MOVEMENT) {

                float x = pc.getX() + (cbc.bound.getWidth() / 2);
                float y = pc.getY() + (cbc.bound.getHeight() / 2);

                vc.velocity.add(
                        (float) Math.cos(Math.atan2(grappleDestination.y - y, grappleDestination.x - x)) * GRAPPLE_MOVEMENT,
                        (float) Math.sin(Math.atan2(grappleDestination.y - y, grappleDestination.x - x)) * GRAPPLE_MOVEMENT);


            }

            if(cbc.bound.contains(grappleDestination.x, grappleDestination.y)){
                vc.velocity.x = 0;
                pc.position.x = grappleDestination.x - cbc.bound.width / 2;
                pc.position.y = grappleDestination.y - cbc.bound.height / 2;
                if(vc.velocity.y > MAX_GRAPPLE_LAUNCH) {
                    vc.velocity.y = MAX_GRAPPLE_LAUNCH;
                } else if(vc.velocity.y < 0){
                    vc.velocity.y = 0;
                }

                grappleDestination = null;
                gc.ignoreGravity = false;
            }

        } else {
            if(mtm.get(e).targetX == null && mtm.get(e).targetY == null){
                gc.ignoreGravity = false;
            }
        }

        if(movementInputPoll != null) {
            if(Gdx.input.isTouched(movementInputPoll)) {
                Vector3 input = new Vector3(Gdx.input.getX(movementInputPoll), Gdx.input.getY(movementInputPoll), 0);
                gameport.unproject(input);

                if(input.y < 195){
                    MoveToSystem.moveTo(input.x, cbc.getCenterX(), ac, vc);
                }
            }
        }  else if(grappleDestination == null) {
            if(cbc.getRecentCollisions().contains(Collider.Collision.TOP, false)){
                MoveToSystem.decelerate(ac, vc);
            }
        }

        if(firingInputPoll != null){

            wc.timer.update(world.getDelta());

            if(Gdx.input.isTouched(firingInputPoll)) {

                if(wc.timer.isFinishedAndReset()) {
                    sc.setState(1);
                    Vector3 input = new Vector3(Gdx.input.getX(firingInputPoll), Gdx.input.getY(firingInputPoll), 0);
                    gameport.unproject(input);

                    float x = pc.getX() + (cbc.bound.getWidth() / 2);
                    float y = pc.getY() + (cbc.bound.getHeight() / 2);
                    double angleOfTravel = (Math.atan2(input.y - y, input.x - x));

                    if(angleOfTravel >= 0) trc.scaleX = (angleOfTravel <= (Math.PI / 2)) ? 1 : -1;
                    else trc.scaleX = (angleOfTravel >= -(Math.PI / 2)) ? 1 : -1;

                    Entity bullet =  BulletFactory.createBullet(world, x, y, angleOfTravel);

                    for(Component c : wc.additionalComponenets) {
                        bullet.edit().add(c);
                    }
                }


                if(!cbc.getRecentCollisions().contains(Collider.Collision.TOP, false)
                        && movementInputPoll == null && grappleDestination == null
                        && mtc.targetX == null
                        && jm.get(e).jumps > 0) {
                    if(vc.velocity.y < gc.gravity) {
                        vc.velocity.y = gc.gravity;
                    }
                    vc.velocity.x = 0;
                }




            }
        } else {
            if(sc.getState() != 0) {
                sc.setState(0);
            }
        }


    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        touchInput = new Vector3(screenX, screenY, 0);
        unprojectedInput = gameport.unproject(touchInput);

        grappleDestination = world.getSystem(GrappleSystem.class).canGrappleTo(unprojectedInput.x, unprojectedInput.y);
        if(grappleDestination == null) {

            if (touchInput.y <= 195) {
                hasTarget = true;
                movementInputPoll = pointer;
                gm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).ignoreGravity = false;
            } else if(firingInputPoll == null){
                canFire = true;
                firingInputPoll = pointer;
                jumpTimer.reset();
            } else {
                jumpTimer.reset();
            }
        } else {
            mtm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).targetX = null;
            gm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).ignoreGravity = true;
            vm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).velocity.y = 0;
            vm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).velocity.x = 0;
        }

        world.getSystem(ActiveOnTouchSystem.class).activeOnTouchTrigger(touchInput.x, touchInput.y);

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {


        if(grappleDestination == null) {

            //TODO Find player get player should be player position or something if player can't be found
            if(!jumpTimer.isFinished() && jm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).jumps > 0) {
                CollisionBoundComponent cbc = cbm.get(world.getSystem(FindPlayerSystem.class).getPlayer());
                MoveToComponent mtc = mtm.get(world.getSystem(FindPlayerSystem.class).getPlayer());

                gm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).ignoreGravity = true;
                jm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).jumps--;

                Vector3 input = new Vector3(screenX, screenY, 0);
                gameport.unproject(input);

                world.getSystem(MoveToSystem.class).flyTo(Math.atan2(input.y - cbc.getCenterY(), input.x - cbc.getCenterX()) ,
                        Measure.units(20f),
                        GRAPPLE_MOVEMENT * 10,
                        mtc,
                        cbc);
            }


        }

        if(movementInputPoll != null) {
            movementInputPoll = (movementInputPoll == pointer) ? null : movementInputPoll;
        }

        if(firingInputPoll != null) {
            firingInputPoll = (firingInputPoll == pointer) ? null : firingInputPoll;

            if(firingInputPoll == null){
                wm.get(world.getSystem(FindPlayerSystem.class).getPlayer()).timer.reset();
            }

        }
        canFire = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
