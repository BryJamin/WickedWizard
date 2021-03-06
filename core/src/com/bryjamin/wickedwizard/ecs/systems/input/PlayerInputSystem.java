package com.bryjamin.wickedwizard.ecs.systems.input;

import com.artemis.Aspect;
import com.artemis.Component;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.Bag;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.SoundFileStrings;
import com.bryjamin.wickedwizard.ecs.components.AdditionalWeaponComponent;
import com.bryjamin.wickedwizard.ecs.components.StatComponent;
import com.bryjamin.wickedwizard.ecs.components.WeaponComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.GrappleComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.PlayerComponent;
import com.bryjamin.wickedwizard.ecs.components.identifiers.WingComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.DirectionalComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.GlideComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.JumpComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.MoveToComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.systems.FindPlayerSystem;
import com.bryjamin.wickedwizard.factories.PlayerFactory;
import com.bryjamin.wickedwizard.utils.BulletMath;
import com.bryjamin.wickedwizard.utils.Measure;
import com.bryjamin.wickedwizard.utils.collider.Collider;
import com.bryjamin.wickedwizard.utils.enums.Direction;

/**
 * Created by Home on 04/03/2017.
 */
public class PlayerInputSystem extends EntityProcessingSystem {

    public float MAX_GRAPPLE_LAUNCH = Measure.units(60f);
    private float MAX_GRAPPLE_MOVEMENT = Measure.units(150f);
    private float GRAPPLE_MOVEMENT = Measure.units(15f);


    ComponentMapper<PositionComponent> pm;
    ComponentMapper<DirectionalComponent> dm;
    ComponentMapper<ParentComponent> parm;
    ComponentMapper<ChildComponent> cm;
    ComponentMapper<VelocityComponent> vm;
    ComponentMapper<AccelerantComponent> am;
    ComponentMapper<MoveToComponent> mtm;
    ComponentMapper<CollisionBoundComponent> cbm;
    ComponentMapper<WeaponComponent> wm;
    ComponentMapper<AdditionalWeaponComponent> additionalWeaponComponentMapper;
    ComponentMapper<AnimationStateComponent> asm;
    ComponentMapper<StatComponent> sm;
    ComponentMapper<GravityComponent> gm;
    ComponentMapper<GlideComponent> glm;
    ComponentMapper<TextureRegionComponent> trm;
    ComponentMapper<JumpComponent> jm;

    Viewport gameport;

    private PlayerInput playerInput;

    boolean disableInput = false;

    public Rectangle movementArea;

    public boolean hasStartedFiring = false;

   // public static boolean isAutoFire = false;

/*    public static void updateAutoFireUsingPreferences(){
        isAutoFire = Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_AUTOFIRE, false);
    }*/


    @SuppressWarnings("unchecked")
    public PlayerInputSystem(Viewport gameport) {
        super(Aspect.all(PositionComponent.class, VelocityComponent.class, PlayerComponent.class, AnimationStateComponent.class));
        this.gameport = gameport;
        movementArea = new Rectangle(gameport.getCamera().position.x - gameport.getWorldWidth() / 2,
                gameport.getCamera().position.y - gameport.getWorldHeight() / 2,
                MainGame.GAME_WIDTH, Measure.units(10f));

        playerInput = new PlayerInput(world, gameport, movementArea, this);

        //isAutoFire = Gdx.app.getPreferences(PreferenceStrings.SETTINGS).getBoolean(PreferenceStrings.SETTINGS_AUTOFIRE, true);
    }


    public PlayerInput getPlayerInput() {
        return playerInput;
    }


    public void movePlayer(float targetX, float currentPosition, AccelerantComponent ac, VelocityComponent vc, float speedMultiplier){
        ac.accelX = Measure.units(15f) * (1 + speedMultiplier);
        ac.maxX = Measure.units(80f) * (1 + speedMultiplier);
        GrappleSystem.moveTo(targetX, currentPosition, ac, vc);
    }

    public void autoMove(float targetX, float speedMultiplier){
        for(Entity e : this.getEntities()){
            movePlayer(targetX, cbm.get(e).getCenterX(), am.get(e), vm.get(e), speedMultiplier);
        }
    }

    public void updateWeapons(WeaponComponent wc, AdditionalWeaponComponent adc){
        if (wc.timer.isFinished()) {
            wc.timer.reset(wc.weapon.getBaseFireRate());
        }

        for (WeaponComponent weaponComponent : adc.additionalWeapons) {
            if (weaponComponent.timer.isFinished()) {
                weaponComponent.timer.reset(weaponComponent.weapon.getBaseFireRate());
            }
        }

        wc.timer.update(world.getDelta());

        for (WeaponComponent weaponComponent : adc.additionalWeapons) {
            weaponComponent.timer.update(world.getDelta());
        }

        //The purpose for the delay reset is for enemies/companies that react to player fire to have a chance to see that the player has fired

    }

    public void fireWeapons(WeaponComponent wc, AdditionalWeaponComponent adc, AnimationStateComponent asc, Entity e, float x, float y, double angleOfTravelInRadians){

        if (wc.timer.isFinished()) {
            hasStartedFiring = true;
            asc.queueAnimationState(AnimationStateComponent.FIRING);
            wc.weapon.fire(world, e, x, y, angleOfTravelInRadians);
        }

        for (WeaponComponent weaponComponent : adc.additionalWeapons) {
            if (weaponComponent.timer.isFinished()) {
                hasStartedFiring = true;
                asc.queueAnimationState(AnimationStateComponent.FIRING);
                weaponComponent.weapon.fire(world, e, x, y, angleOfTravelInRadians);
            }
        }


    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    protected void process(Entity e) {


        PositionComponent pc = pm.get(e);
        VelocityComponent vc = vm.get(e);
        AccelerantComponent ac = am.get(e);
        CollisionBoundComponent cbc = cbm.get(e);
        com.bryjamin.wickedwizard.ecs.components.WeaponComponent wc = wm.get(e);
        AdditionalWeaponComponent adc = additionalWeaponComponentMapper.get(e);
        AnimationStateComponent asc = asm.get(e);
        com.bryjamin.wickedwizard.ecs.components.StatComponent sc = sm.get(e);
        TextureRegionComponent trc = trm.get(e);
        GravityComponent gc = gm.get(e);
        GlideComponent glc = glm.get(e);
        MoveToComponent mtc = mtm.get(e);

        if (cbc.getRecentCollisions().contains(Collider.Collision.BOTTOM, false) && vc.velocity.y <= 0) {
            turnOffGlide();
        }

        if(disableInput) return;
        //if(!isEnabled()) return;

        if (mtm.get(e).targetX == null && mtm.get(e).targetY == null) {
            playerInput.activeGrapple = false;
            gc.ignoreGravity = false;
        }

        if (!playerInput.activeGrapple) {

            if (playerInput.movementInputPoll != null) {

                if (Gdx.input.isTouched(playerInput.movementInputPoll)) {
                    Vector3 input = new Vector3(Gdx.input.getX(playerInput.movementInputPoll), Gdx.input.getY(playerInput.movementInputPoll), 0);
                    gameport.unproject(input);
                    if (input.y <= movementArea.y + movementArea.getHeight() && !mtc.hasTarget()) {
                        movePlayer(input.x, cbc.getCenterX(), ac, vc, sc.speed);
                    }
                } else {
                   playerInput.movementInputPoll = null;
                }
            }

            if (playerInput.firingInputPoll != null) {

                if (Gdx.input.isTouched(playerInput.firingInputPoll)) {

                    updateWeapons(wc, adc);

                    Vector3 input = new Vector3(Gdx.input.getX(playerInput.firingInputPoll), Gdx.input.getY(playerInput.firingInputPoll), 0);
                    gameport.unproject(input);
                    float x = cbc.getCenterX();
                    float y = cbc.getCenterY();
                    double angleOfTravel = (Math.atan2(input.y - y, input.x - x));

                    if (hasStartedFiring) {
                        if (dm.has(e)) {
                            setDirectionOfPlayerUsingFiringAngle(dm.get(e), angleOfTravel, DirectionalComponent.PRIORITY.HIGHEST);
                        }
                    }


                    fireWeapons(wc, adc, asc, e, x, y, angleOfTravel);

                } else {
                    playerInput.firingInputPoll = null;
                }

            /*} else if(isAutoFire && world.getSystem(FiringAISystem.class).enemiesExist()) {

                updateWeapons(wc, adc);

                float x = cbc.getCenterX();
                float y = cbc.getCenterY();

                double angleOfTravel = world.getSystem(FiringAISystem.class).firingAngleToNearestEnemyInRadians(x, y);

                if (hasStartedFiring) {
                    if (dm.has(e)) {
                        setDirectionOfPlayerUsingFiringAngle(dm.get(e), angleOfTravel, DirectionalComponent.PRIORITY.HIGHEST);
                    }
                }

                fireWeapons(wc, adc, asc, e, x, y, angleOfTravel);*/

            } else {

                hasStartedFiring = false;

                wc.timer.setResetTime(wc.defaultStartTime);
                wc.timer.reset();

                for(WeaponComponent weaponComponent : adc.additionalWeapons){
                    weaponComponent.timer.setResetTime(wc.defaultStartTime);
                    weaponComponent.timer.reset();
                }



                if (asc.getDefaultState() != 0) {
                    asc.setDefaultState(0);
                }
            }
        }

    }





    public void setDirectionOfPlayerUsingFiringAngle(DirectionalComponent dc, double firingAngle, DirectionalComponent.PRIORITY priority){

        if (firingAngle >= 0) {
            dc.setDirection(firingAngle <= (Math.PI / 2) ? Direction.RIGHT : Direction.LEFT, priority);
        } else {
            dc.setDirection(firingAngle >= -(Math.PI / 2) ? Direction.RIGHT : Direction.LEFT, priority);
        }
    }




    public void grappleTo(float grappleX, float grappleY) {


        AssetManager am = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem.class).assetManager;
        PlayerFactory pf = new PlayerFactory(am);

        CollisionBoundComponent cbc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(CollisionBoundComponent.class);
        com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent parc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class);
        GlideComponent glc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(GlideComponent.class);


        Rectangle r = world.getSystem(GrapplePointSystem.class).returnTouchedGrapple(grappleX, grappleY);



        if(r != null) {


            IntBag intBag = world.getAspectSubscriptionManager().get(Aspect.all(GrappleComponent.class, com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent.class)).getEntities();

            for(int i = 0; i < intBag.size(); i++) {
                Entity grapple = world.getEntity(intBag.get(i));
                if (parc.children.contains(grapple.getComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent.class), true)) world.getSystem(com.bryjamin.wickedwizard.ecs.systems.ai.OnDeathSystem.class).kill(grapple);
            }

            Bag<Component> bag = pf.grappleShot(world.getSystem(FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class),
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


            world.getSystem(com.bryjamin.wickedwizard.ecs.systems.audio.SoundSystem.class).playRandomSound(SoundFileStrings.grappleFireMix);

            turnOffGlide();

            world.getSystem(FindPlayerSystem.class).getPlayerComponent(VelocityComponent.class).velocity.y = 0;
            world.getSystem(FindPlayerSystem.class).getPlayerComponent(VelocityComponent.class).velocity.x = 0;

        }

    }


    public void turnOnGlide(){

        AssetManager am = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.graphical.RenderingSystem.class).assetManager;
        PlayerFactory pf = new PlayerFactory(am);

        PositionComponent pc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(PositionComponent.class);
        com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent parc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class);
        GlideComponent glc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(GlideComponent.class);
        MoveToComponent mtc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(MoveToComponent.class);
        VelocityComponent velocityComponent = world.getSystem(FindPlayerSystem.class).getPlayerComponent(VelocityComponent.class);
        JumpComponent jumpComponent = world.getSystem(FindPlayerSystem.class).getPlayerComponent(JumpComponent.class);

        if(mtc.hasTarget()){

            velocityComponent.velocity.y = 0;
            velocityComponent.velocity.x = 0;
            mtc.reset();
        }

        Entity e = world.createEntity();
        for(Component c : pf.wings(parc, pc, true)) {
            e.edit().add(c);
        }

        e = world.createEntity();
        for(Component c : pf.wings(parc, pc, false)) {
            e.edit().add(c);
        }


        velocityComponent.velocity.y = Measure.units(80f);
        jumpComponent.jumps--;

        glc.gliding = true;
        glc.active = true;


    }

    public void turnOffGlide(){

        ParentComponent parc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(com.bryjamin.wickedwizard.ecs.components.identifiers.ParentComponent.class);
        GlideComponent glc = world.getSystem(FindPlayerSystem.class).getPlayerComponent(GlideComponent.class);


        //TODO find it only for the wings component

       // System.out.println("CHILDREN :" + parc.children.size);


        Array<com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent> ok  = new Array<com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent>();
        ok.addAll(parc.children);

        for(com.bryjamin.wickedwizard.ecs.components.identifiers.ChildComponent childComponent : ok) {

            Entity e = world.getSystem(com.bryjamin.wickedwizard.ecs.systems.FindChildSystem.class).findChildEntity(childComponent);

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