package com.byrjamin.wickedwizard.ecs.systems.input;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.byrjamin.wickedwizard.MainGame;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.GrappleComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.byrjamin.wickedwizard.ecs.components.StatComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.WingComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GlideComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.JumpComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.systems.FindChildSystem;
import com.byrjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.byrjamin.wickedwizard.ecs.systems.ai.OnDeathSystem;
import com.byrjamin.wickedwizard.ecs.systems.graphical.RenderingSystem;
import com.byrjamin.wickedwizard.factories.PlayerFactory;
import com.byrjamin.wickedwizard.utils.BulletMath;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.Collider;
import com.byrjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 04/03/2017.
 */
public class PlayerInputSystem extends EntityProcessingSystem {

    public float MAX_GRAPPLE_LAUNCH = Measure.units(60f);
    private float MAX_GRAPPLE_MOVEMENT = Measure.units(150f);
    private float GRAPPLE_MOVEMENT = Measure.units(15f);


    ComponentMapper<PositionComponent> pm;
    ComponentMapper<ParentComponent> parm;
    ComponentMapper<ChildComponent> cm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<AccelerantComponent> am;
    ComponentMapper<MoveToComponent> mtm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<WeaponComponent> wm;
    ComponentMapper<AnimationStateComponent> asm;
    ComponentMapper<StatComponent> sm;
    ComponentMapper<GravityComponent> gm;
    ComponentMapper<GlideComponent> glm;
    ComponentMapper<TextureRegionComponent> trm;
    ComponentMapper<JumpComponent> jm;

    Viewport gameport;

    private PlayerInput playerInput;

    public Rectangle movementArea;


    @SuppressWarnings("unchecked")
    public PlayerInputSystem(Viewport gameport) {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class, PlayerComponent.class, AnimationStateComponent.class));
        this.gameport = gameport;
        movementArea = new Rectangle(gameport.getCamera().position.x - gameport.getWorldWidth() / 2,
                gameport.getCamera().position.y - gameport.getWorldHeight() / 2,
                MainGame.GAME_WIDTH, Measure.units(10f));

        playerInput = new PlayerInput(world, gameport, movementArea);
    }


    public PlayerInput getPlayerInput() {
        return playerInput;
    }

    @Override
    protected void process(Entity e) {

        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        AccelerantComponent ac = am.get(e);
        CollisionBoundComponent cbc = cbm.get(e);
        WeaponComponent wc = wm.get(e);
        AnimationStateComponent asc = asm.get(e);
        StatComponent sc = sm.get(e);
        TextureRegionComponent trc = trm.get(e);
        GravityComponent gc = gm.get(e);
        GlideComponent glc = glm.get(e);
        MoveToComponent mtc = mtm.get(e);


/*
        if(vc.velocity.x != 0) {
            System.out.println("Player X IS:" + vc.velocity.x);
            System.out.println("Player Y IS:" + vc.velocity.y);
        }
*/

        if (mtm.get(e).targetX == null && mtm.get(e).targetY == null) {
            playerInput.activeGrapple = false;
            gc.ignoreGravity = false;
        }

        if (!playerInput.activeGrapple) {

            if (playerInput.movementInputPoll != null) {
                if (Gdx.input.isTouched(playerInput.movementInputPoll)) {
                    Vector3 input = new Vector3(Gdx.input.getX(playerInput.movementInputPoll), Gdx.input.getY(playerInput.movementInputPoll), 0);
                    gameport.unproject(input);
                    if (input.y <= movementArea.y + movementArea.getHeight()) {
                        ac.accelX = Measure.units(15f) * sc.speed;
                        ac.maxX = Measure.units(80f) * sc.speed;
                        GrappleSystem.moveTo(input.x, cbc.getCenterX(), ac, vc);
                    }
                }
            }

            if (playerInput.firingInputPoll != null) {

                if (Gdx.input.isTouched(playerInput.firingInputPoll)) {

                    wc.timer.update(world.getDelta());

                    Vector3 input = new Vector3(Gdx.input.getX(playerInput.firingInputPoll), Gdx.input.getY(playerInput.firingInputPoll), 0);
                    gameport.unproject(input);
                    float x = pc.getX() + (cbc.bound.getWidth() / 2);
                    float y = pc.getY() + (cbc.bound.getHeight() / 2);
                    double angleOfTravel = (Math.atan2(input.y - y, input.x - x));


                    if (angleOfTravel >= 0) {
                        if (angleOfTravel <= (Math.PI / 2)) {
                            com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem.changeDirection(world, e, Direction.RIGHT, DirectionalComponent.PRIORITY.HIGH);
                        } else {
                            com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem.changeDirection(world, e, Direction.LEFT, DirectionalComponent.PRIORITY.HIGH);
                        }
                    } else {
                        if (angleOfTravel >= -(Math.PI / 2)) {
                            com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem.changeDirection(world, e, Direction.RIGHT, DirectionalComponent.PRIORITY.HIGH);
                        } else {
                            com.byrjamin.wickedwizard.ecs.systems.graphical.DirectionalSystem.changeDirection(world, e, Direction.LEFT, DirectionalComponent.PRIORITY.HIGH);
                        }
                    }

                    StatComponent statComponent = world.getSystem(FindPlayerSystem.class).getPC(StatComponent.class);

                    if (wc.timer.isFinishedAndReset(wc.weapon.getBaseFireRate() / statComponent.fireRate)) {
                        asc.queueAnimationState(AnimationStateComponent.FIRING);
                        wc.weapon.fire(world,e, x, y, angleOfTravel);
                    }
                }
            } else {
                if (asc.getDefaultState() != 0) {
                    asc.setDefaultState(0);
                }
            }
        }

        if (cbc.getRecentCollisions().contains(Collider.Collision.BOTTOM, false)) {
            turnOffGlide();
        }
    }

    public void grappleTo(float grappleX, float grappleY) {


        AssetManager am = world.getSystem(RenderingSystem.class).assetManager;
        PlayerFactory pf = new PlayerFactory(am);

        CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPC(CollisionBoundComponent.class);
        ParentComponent parc = world.getSystem(FindPlayerSystem.class).getPC(ParentComponent.class);
        GlideComponent glc = world.getSystem(FindPlayerSystem.class).getPC(GlideComponent.class);


        Rectangle r = world.getSystem(GrapplePointSystem.class).returnTouchedGrapple(grappleX, grappleY);



        if(r != null) {


            IntBag intBag = world.getAspectSubscriptionManager().get(Aspect.all(GrappleComponent.class, ChildComponent.class)).getEntities();

            for(int i = 0; i < intBag.size(); i++) {
                Entity grapple = world.getEntity(intBag.get(i));
                if (parc.children.contains(grapple.getComponent(ChildComponent.class), true)) world.getSystem(OnDeathSystem.class).kill(grapple);
            }

            Bag<Component> bag = pf.grappleShot(world.getSystem(FindPlayerSystem.class).getPC(ParentComponent.class),
                    cbc.getCenterX(),
                    cbc.getCenterY(),
                    r.x + r.getWidth() / 2,
                    r.y + r.getHeight() / 2,
                    BulletMath.angleOfTravel(cbc.getCenterX(), cbc.getCenterY(),
                            r.x + r.getWidth() / 2,
                            r.y + r.getHeight() / 2));

            Entity e = world.createEntity();

            for(Component c : bag) {
                e.edit().add(c);
            }


            turnOffGlide();

            world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class).velocity.y = 0;
            world.getSystem(FindPlayerSystem.class).getPC(VelocityComponent.class).velocity.x = 0;

        }

    }


    public void turnOnGlide(){

        AssetManager am = world.getSystem(RenderingSystem.class).assetManager;
        PlayerFactory pf = new PlayerFactory(am);

        PositionComponent pc = world.getSystem(FindPlayerSystem.class).getPC(PositionComponent.class);
        ParentComponent parc = world.getSystem(FindPlayerSystem.class).getPC(ParentComponent.class);
        GlideComponent glc = world.getSystem(FindPlayerSystem.class).getPC(GlideComponent.class);

        Entity e = world.createEntity();
        for(Component c : pf.wings(parc, pc, true)) {
            e.edit().add(c);
        }

        e = world.createEntity();
        for(Component c : pf.wings(parc, pc, false)) {
            e.edit().add(c);
        }

        glc.gliding = true;
        glc.active = true;

    }

    public void turnOffGlide(){

        ParentComponent parc = world.getSystem(FindPlayerSystem.class).getPC(ParentComponent.class);
        GlideComponent glc = world.getSystem(FindPlayerSystem.class).getPC(GlideComponent.class);


        //TODO find it only for the wings component

       // System.out.println("CHILDREN :" + parc.children.size);


        Array<ChildComponent> ok  = new Array<ChildComponent>();
        ok.addAll(parc.children);

        for(ChildComponent childComponent : ok) {

            Entity e = world.getSystem(FindChildSystem.class).findChildEntity(childComponent);

            if(e != null) {

                if (world.getMapper(WingComponent.class).has(e)) {
                    parc.children.removeValue(childComponent, false);
                    world.deleteEntity(e);
                }
            }

        }

        glc.gliding = false;
        glc.active = false;
    }


}