package com.bryjamin.wickedwizard.screens;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.utils.Bag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.bryjamin.wickedwizard.ecs.components.ai.Action;
import com.bryjamin.wickedwizard.ecs.components.ai.ActionOnTouchComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.CollisionBoundComponent;
import com.bryjamin.wickedwizard.ecs.components.movement.PositionComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureFontComponent;
import com.bryjamin.wickedwizard.ecs.components.texture.TextureRegionComponent;
import com.bryjamin.wickedwizard.utils.BagToEntity;
import com.bryjamin.wickedwizard.utils.ComponentBag;
import com.bryjamin.wickedwizard.utils.Measure;

/**
 * Created by ae164 on 19/05/17.
 */

public class MenuButton {


    private String font;
    private TextureRegion buttonTexture;

    private final float width;
    private final float height;
    private final Color foregroundColor;
    private final Color backgroundColor;


    private final float textOffSetX;
    private final float textOffSetY;


    private final Action action;


    public static class MenuButtonBuilder {

        //Required Parameters
        private final String font;
        private final TextureRegion buttonTexture;

        //Optional Parameters
        private float width = Measure.units(10f);
        private float height = Measure.units(10f);

        private float textOffSetX = 0;
        private float textOffSetY = 0;

        private Color foregroundColor = new Color(Color.WHITE);
        private Color backgroundColor = new Color(Color.BLACK);

        private Action action = new Action() {
            @Override
            public void performAction(World world, Entity e) {

            }
        };


        public MenuButtonBuilder(String font, TextureRegion buttonTexture){
            this.font = font;
            this.buttonTexture = buttonTexture;
        }


        public MenuButtonBuilder width(float val)
        { width = val; return this; }

        public MenuButtonBuilder height(float val)
        { height = val; return this; }

        public MenuButtonBuilder foregroundColor(Color val)
        { foregroundColor = val; return this; }

        public MenuButtonBuilder backgroundColor(Color val)
        { backgroundColor = val; return this; }

        public MenuButtonBuilder action(Action val)
        { action = val; return this; }

        public MenuButton build() {
            return new MenuButton(this);
        }

    }

    public MenuButton(MenuButtonBuilder mbb) {
        this.font = mbb.font;
        this.buttonTexture = mbb.buttonTexture;
        this.width = mbb.width;
        this.height = mbb.height;

        this.textOffSetX = mbb.textOffSetX;
        this.textOffSetY = mbb.textOffSetY;

        this.foregroundColor = mbb.foregroundColor;
        this.backgroundColor = mbb.backgroundColor;

        this.action = mbb.action;
    }
















    public Entity createButton(World world, String text, float x, float y) {

        Entity e =  BagToEntity.bagToEntity(world.createEntity(), textBag(text, x, y));
        BagToEntity.bagToEntity(world.createEntity(), backingBag(x, y));

        return e;

    }


    private ComponentBag textBag(String text, float x, float y){

        ComponentBag textBag = new ComponentBag();
        textBag.add(new PositionComponent(x, y));
        TextureFontComponent tfc = new TextureFontComponent(font, text, textOffSetX, textOffSetY, width,
                TextureRegionComponent.FOREGROUND_LAYER_MIDDLE,
                foregroundColor);
        textBag.add(tfc);
        Rectangle r = new Rectangle(x, y, width, height);
        textBag.add(new CollisionBoundComponent(r));
        textBag.add(new ActionOnTouchComponent(action));

        return textBag;

    }

    private ComponentBag backingBag(float x, float y){
        ComponentBag backingBag = new ComponentBag();
        backingBag.add(new PositionComponent(x, y));
        TextureRegionComponent trc = new TextureRegionComponent(buttonTexture, width, height, TextureRegionComponent.FOREGROUND_LAYER_FAR,
                backgroundColor);
        backingBag.add(trc);

        return backingBag;
    }


    public Bag<ComponentBag> createButton(String text, float x, float y){

        Bag<ComponentBag> bags = new Bag<ComponentBag>();
        bags.add(textBag(text, x, y));
        bags.add(backingBag(x, y));

        return bags;


    }


}
