package com.byrjamin.wickedwizard.item;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.byrjamin.wickedwizard.arenas.Arena;
import com.byrjamin.wickedwizard.screens.PlayScreen;
import com.byrjamin.wickedwizard.sprites.Wizard;

/**
 * Created by Home on 14/12/2016.
 */
public class Item {


    private Sprite sprite;

    public enum itemName {

        ATTACK_UP {
            @Override
            public void itemEffect(Wizard w) {
                w.increaseDamage(0.2f);
            }
            @Override
            public Sprite getSprite() {
                return PlayScreen.atlas.createSprite("fire");
            }
        },
        ATTACK_DOWN {
            @Override
            public void itemEffect(Wizard w) {

            }
            @Override
            public Sprite getSprite() {
                return PlayScreen.atlas.createSprite("fire");
            }
        },
        HEALTH_UP {
            @Override
            public void itemEffect(Wizard w) {
            }
            @Override
            public Sprite getSprite() {
                return PlayScreen.atlas.createSprite("fire");
            }

        },
        RELOAD_SPEED_UP {
            @Override
            public void itemEffect(Wizard w) {
            }
            @Override
            public Sprite getSprite() {
                return PlayScreen.atlas.createSprite("fire");
            }
        };


        public abstract void itemEffect(Wizard w);
        public abstract Sprite getSprite();

    }

    //TODO enums as methods??


    private itemName currentItem;

    private void setSprite(Sprite s){
        sprite = s;
    }

    public Item(itemName i){
        currentItem = i;
    }

    public void applyItem(Wizard w){
        w.increaseDamage(0.2f);
    }











    public void update(float dt){

    }


}
