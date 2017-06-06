package com.byrjamin.wickedwizard.factories.enemy;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.IntMap;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.ecs.components.ai.Action;
import com.byrjamin.wickedwizard.ecs.components.ai.ExploderComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.LootComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.MinionComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.AccelerantComponent;
import com.byrjamin.wickedwizard.ecs.components.BlinkComponent;
import com.byrjamin.wickedwizard.ecs.components.CollisionBoundComponent;
import com.byrjamin.wickedwizard.ecs.components.identifiers.EnemyComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.FiringAIComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.BounceComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.GravityComponent;
import com.byrjamin.wickedwizard.ecs.components.HealthComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.MoveToPlayerComponent;
import com.byrjamin.wickedwizard.ecs.components.ai.PhaseComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.byrjamin.wickedwizard.ecs.components.object.SpawnerComponent;
import com.byrjamin.wickedwizard.ecs.components.movement.VelocityComponent;
import com.byrjamin.wickedwizard.ecs.components.WeaponComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.AnimationStateComponent;
import com.byrjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.byrjamin.wickedwizard.factories.DeathFactory;
import com.byrjamin.wickedwizard.factories.items.pickups.MoneyPlus1;
import com.byrjamin.wickedwizard.factories.weapons.WeaponFactory;
import com.byrjamin.wickedwizard.factories.items.ItemFactory;
import com.byrjamin.wickedwizard.utils.ComponentBag;
import com.byrjamin.wickedwizard.utils.Measure;
import com.byrjamin.wickedwizard.utils.collider.HitBox;

/**
 * Created by Home on 26/03/2017.
 */

public class BlobFactory extends EnemyFactory {

    private final float width = Measure.units(9);
    private final float height = Measure.units(9);

    private final float textureWidth = Measure.units(11);
    private final float textureHeight = Measure.units(11);

    private final float textureOffsetX = -Measure.units(1f);
    private final float textureOffsetY = 0;

    private ItemFactory itemf;
    private WeaponFactory wf;
    private DeathFactory df;

    public BlobFactory(AssetManager assetManager) {
        super(assetManager);
        this.df = new DeathFactory(assetManager);
        this.itemf = new ItemFactory(assetManager);
        this.wf = new WeaponFactory(assetManager);
    }

    public Bag<Component> blobBag(float x, float y){


        x = x - width / 2;
        y = y - height / 2;

        Bag<Component> bag = this.defaultEnemyBag(new ComponentBag(), x , y, width, height, 10);
        bag.add(new VelocityComponent(0, 0));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
        bag.add(new GravityComponent());
        bag.add(new MoveToPlayerComponent());
        bag.add(new AccelerantComponent(Measure.units(15), 0));

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.BLOB_STANDING), Animation.PlayMode.LOOP));


        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOB_STANDING),
                textureOffsetX, textureOffsetY, textureWidth, textureHeight,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));
        return bag;
    }

    public Bag<Component> dummyBlob(float x, float y){


        x = x - width / 2;
        y = y - height / 2;

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new VelocityComponent(0, 0));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
        bag.add(new GravityComponent());
        bag.add(new EnemyComponent());
        bag.add(new HealthComponent(1000));
        bag.add(new BlinkComponent());
        AnimationStateComponent sc = new AnimationStateComponent();
        sc.setDefaultState(0);
        bag.add(sc);
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0,  new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.BLOB_STANDING), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOB_STANDING),
                textureOffsetX, textureOffsetY, textureWidth, textureHeight,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));
        return bag;
    }

    public Bag<Component> shopKeeperBlob(float x, float y){


        x = x - width / 2;
        y = y - height / 2;

        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new VelocityComponent(0, 0));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width, height), true));
        bag.add(new GravityComponent());
        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0,  new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.BLOB_STANDING), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BLOB_STANDING),
                textureOffsetX, textureOffsetY, textureWidth, textureHeight,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));
        return bag;
    }

    public Bag<Component> smallblobBag(float x, float y){

        float scale = 0.5f;

        x = x - width* scale / 2;
        y = y - height * scale / 2;

        Bag<Component> bag = this.defaultEnemyBag(new ComponentBag(), x , y, width * scale, height * scale, 2);
        bag.add(new VelocityComponent(0, 0));
        bag.add(new CollisionBoundComponent(new Rectangle(x,y, width * scale, height * scale), true));
        bag.add(new GravityComponent());
        bag.add(new AccelerantComponent(Measure.units(2.5f), 0, Measure.units(30), 0));
        bag.add(new MoveToPlayerComponent());
        bag.add(new ExploderComponent());

        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0,  new Animation<TextureRegion>(0.15f / 1f, atlas.findRegions(TextureStrings.BLOB_STANDING), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(this.atlas.findRegion(TextureStrings.BLOB_STANDING),
                textureOffsetX * scale,
                textureOffsetY * scale,
                textureWidth * scale,
                textureHeight * scale,
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));
        return bag;
    }


    public Bag<Component> BiggaBlobbaBag(float x, float y){


        x = x - width / 2;
        y = y - height / 2;



        Bag<Component> bag = new Bag<Component>();
        bag.add(new PositionComponent(x,y));
        bag.add(new VelocityComponent(0, 0));

        Rectangle collision = new Rectangle(x, y, Measure.units(33), Measure.units(38));

        CollisionBoundComponent cbc = new CollisionBoundComponent(new Rectangle(x, y, Measure.units(33), Measure.units(38)));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, Measure.units(33), Measure.units(17))));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, Measure.units(29), Measure.units(5)),
                Measure.units(2), Measure.units(17)));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, Measure.units(23), Measure.units(5)),
                Measure.units(5), Measure.units(22)));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, Measure.units(15), Measure.units(3)),
                Measure.units(9), Measure.units(27)));
        cbc.hitBoxes.add(new HitBox(new Rectangle(x, y, Measure.units(9), Measure.units(7)),
                Measure.units(12), Measure.units(30)));

        bag.add(cbc);
        bag.add(new GravityComponent());
        bag.add(new LootComponent(5));
       // bag.add(new AccelerantComponent(Measure.units(10), Measure.units(20f)));
       // bag.add(new MoveToPlayerComponent());
        bag.add(new EnemyComponent());
        bag.add(new HealthComponent(65));
        bag.add(new BlinkComponent());
        bag.add(new AnimationStateComponent(0));
        IntMap<Animation<TextureRegion>> animMap = new IntMap<Animation<TextureRegion>>();
        animMap.put(0, new Animation<TextureRegion>(1f / 20f, atlas.findRegions(TextureStrings.BIGGABLOBBA_STANDING), Animation.PlayMode.LOOP));
        bag.add(new AnimationComponent(animMap));
        bag.add(new TextureRegionComponent(atlas.findRegion(TextureStrings.BIGGABLOBBA_STANDING),
                -Measure.units(6),
                0,
                Measure.units(45),
                Measure.units(45),
                TextureRegionComponent.ENEMY_LAYER_MIDDLE));

        //TODO fix biggablobba
/*        OnDeathComponent odc = new OnDeathComponent();
        odc.getComponentBags().addAll(itemf.createIntangibleFollowingPickUpBag(0,0, new MoneyPlus1()));
        odc.getComponentBags().addAll(itemf.createIntangibleFollowingPickUpBag(0,0, new MoneyPlus1()));
        odc.getComponentBags().addAll(itemf.createIntangibleFollowingPickUpBag(0,0, new MoneyPlus1()));
        odc.getComponentBags().addAll(itemf.createIntangibleFollowingPickUpBag(0,0, new MoneyPlus1()));
        df.giblets(odc, 10, Color.GREEN);
        bag.add(odc);*/


        WeaponComponent wc = new WeaponComponent(wf.enemyWeapon(),  1.5f);
        bag.add(wc);

        SpawnerFactory.Spawner s = new SpawnerFactory.Spawner() {
            @Override
            public Bag<Component> spawnBag(float x, float y) {
                Bag<Component> b = smallblobBag(x,y);
                b.add(new MinionComponent());
                return b;
            }
        };

        final SpawnerComponent sc2 = new SpawnerComponent(2.0f, s);
        sc2.isEndless = true;

        PhaseComponent pc = new PhaseComponent();


        Action action1 = new Action(){
            FiringAIComponent f = new FiringAIComponent();
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(f);
            }
            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(FiringAIComponent.class);
            }
        };

        Action action2 = new Action(){
            @Override
            public void performAction(World world, Entity e) {
                e.edit().add(sc2);
                e.edit().add(new BounceComponent());
                e.getComponent(VelocityComponent.class).velocity.y = Measure.units(40f);
            }
            @Override
            public void cleanUpAction(World world, Entity e) {
                e.edit().remove(sc2);
                e.edit().remove(BounceComponent.class);
            }
        };



        pc.addPhase(7.0f, action1);
        pc.addPhase(5.0f, action2);
        pc.addPhaseSequence(1,0);

        bag.add(pc);


        return bag;




    }


}
