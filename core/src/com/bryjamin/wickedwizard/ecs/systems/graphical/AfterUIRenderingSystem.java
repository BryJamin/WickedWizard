package com.bryjamin.wickedwizard.ecs.systems.graphical;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bryjamin.wickedwizard.MainGame;
import com.bryjamin.wickedwizard.assets.FileLocationStrings;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.UIComponent;
import com.bryjamin.wickedwizard.utils.CenterMath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by BB on 22/09/2017.
 */

public class AfterUIRenderingSystem extends EntitySystem {

    private ComponentMapper<PositionComponent> pm;
    private ComponentMapper<com.bryjamin.wickedwizard.ecs.components.identifiers.BulletComponent> bulletm;
    private ComponentMapper<TextureRegionComponent> trm;
    private ComponentMapper<com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent> trbm;
    private ComponentMapper<TextureFontComponent> trfm;
    private ComponentMapper<CollisionBoundComponent> cbm;

    private ArrayList<Entity> orderedEntities = new ArrayList<Entity>();

    public SpriteBatch batch;
    public Viewport gameport;
    public AssetManager assetManager;
    public TextureAtlas atlas;

    public Color batchColor = new Color(Color.WHITE);

    private GlyphLayout glyphLayout = new GlyphLayout();
    public boolean disable = false;

    public AfterUIRenderingSystem(MainGame game, Viewport gameport) {
        super(Aspect.all(PositionComponent.class, UIComponent.class).one(
                TextureRegionComponent.class,
                TextureRegionBatchComponent.class,
                TextureFontComponent.class));

        this.batch = game.batch;
        this.gameport = gameport;
        this.assetManager = game.assetManager;
        this.atlas = assetManager.get(FileLocationStrings.spriteAtlas, TextureAtlas.class);

    }

    @Override
    protected void begin() {
        batch.setProjectionMatrix(gameport.getCamera().combined);
        batch.enableBlending();
        if(!batch.isDrawing()) {
            batch.begin();
        }
    }



    @Override
    public void inserted(Entity e) {
        orderedEntities.add(e);
        Collections.sort(orderedEntities, new Comparator<Entity>() {
            @Override
            public int compare(Entity e1, Entity e2) {

                Integer layer1 = 0;
                Integer layer2 = 0;

                if(trm.has(e1)) {
                    layer1 = trm.get(e1).layer;
                } else if(trbm.has(e1)){
                    layer1 = trbm.get(e1).layer;
                } else if(trfm.has(e1)){
                    layer1 = trfm.get(e1).layer;
                }

                if(trm.has(e2)) {
                    layer2 = trm.get(e2).layer;
                } else if(trbm.has(e2)){
                    layer2 = trbm.get(e2).layer;
                } else if(trfm.has(e2)){
                    layer2 = trfm.get(e2).layer;
                }

                return layer1.compareTo(layer2);
            }
        });
    }

    @Override
    public void removed(Entity e) {
        orderedEntities.remove(e);
    }



    @Override
    protected void processSystem() {

        if(!disable) {
            for (int i = 0; orderedEntities.size() > i; i++) {
                process(orderedEntities.get(i));
            }
        }


    }

    protected boolean process(Entity e) {

        PositionComponent pc = pm.get(e);

        if(cbm.has(e)) {
            if (!CameraSystem.isOnCamera(cbm.get(e).bound, gameport.getCamera())) return false;
        }


        if(trm.has(e)) {
            TextureRegionComponent trc = trm.get(e);


            float originX = trc.width * 0.5f;
            float originY = trc.height * 0.5f;

            batch.setColor(trc.color);


            TextureRegion tr = trc.region != null ? trc.region : atlas.findRegion(TextureStrings.BLOCK);

            batch.draw(tr,
                    pc.getX() + trc.offsetX, pc.getY() + trc.offsetY,
                    originX, originY,
                    trc.width, trc.height,
                    trc.scaleX, trc.scaleY,
                    trc.rotation);
            batch.setColor(batchColor);

        }

        if(trbm.has(e)){
            com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionBatchComponent trbc = trbm.get(e);
            int count = 0;
            batch.setColor(trbc.color);

            float originX = trbc.width * 0.5f;
            float originY = trbc.height * 0.5f;

            for(int i = 0; i < trbc.columns; i++){
                for(int j = 0; j < trbc.rows; j ++){
                    batch.draw(trbc.regions.get(count),
                            pc.getX() + (trbc.width * i) + trbc.offsetX,
                            pc.getY() + (trbc.height * j) + trbc.offsetY,
                            originX,
                            originY,
                            trbc.width + 1,
                            //The top does not have the extra pixel
                            (j == trbc.rows - 1) ? trbc.height : trbc.height + 1,
                            trbc.scaleX,
                            trbc.scaleY,
                            trbc.rotation); //This is to avoid pixel errors between repeated textures
                    count++;
                }
            }
            batch.setColor(batchColor);

        }

        if(trfm.has(e)) {

            //TODO should really use the font width not this random ass camera shizz

            TextureFontComponent trfc = trfm.get(e);
            BitmapFont bmf = assetManager.get(trfc.font, BitmapFont.class);
            bmf.setColor(trfc.color);

            if(cbm.has(e)){
                CollisionBoundComponent cbc = cbm.get(e);
                glyphLayout.setText(bmf, trfc.text);

/*
                System.out.println("layout height" + glyphLayout.height);
                System.out.println("Bound height" + cbc.bound.height);*/

                bmf.draw(batch, glyphLayout,
                        pc.getX() + CenterMath.offsetX(cbc.bound.getWidth(), glyphLayout.width) + trfc.offsetX,
                        pc.getY() + glyphLayout.height + CenterMath.offsetY(cbc.bound.getHeight(), glyphLayout.height) + trfc.offsetY);
            } else {
                bmf.draw(batch, trfc.text,
                        pc.getX() + trfc.offsetX, pc.getY() + trfc.offsetY
                        ,trfc.width, trfc.align, true);
            }

            bmf.setColor(Color.WHITE);
        }

        batch.setColor(batchColor);

        return true;

    }


    @Override
    protected void end() {
        batch.end();
    }


}
