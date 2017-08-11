package com.byrjamin.wickedwizard.factories.items;

import com.byrjamin.wickedwizard.utils.Pair;

/**
 * Created by Home on 06/08/2017.
 */

public class ItemResource {

    public static String WORD_ACCURACY = "Accuracy";

    public static class ItemValues {

        public final Pair<String, Integer> region;
        public final String name;
        public final String description;


        public static class ItemValueBuilder {

            private Pair<String, Integer> region = new Pair<String,Integer>("item/SarcasticLion", 0);
            private String name = "Default Item";
            private String description = "You forgot to set this value";


            public ItemValueBuilder region(String s)
            { region = new Pair<String, Integer>(s,0); return this; }

            public ItemValueBuilder region(String s, int index)
            { region = new Pair<String, Integer>(s, index); return this; }

            public ItemValueBuilder name(String val)
            { name = val; return this; }

            public ItemValueBuilder description(String val)
            { description = val; return this; }


            public ItemValues build() {
                return new ItemValues(this);
            }


        }

        public ItemValues(ItemValueBuilder ivb){
            region = ivb.region;
            name = ivb.name;
            description = ivb.description;
        }


    }



    //Accuracy
    public static class Accuracy {

        public static ItemValues Ace = new ItemValues.ItemValueBuilder().region("item/Ace")
                .name("Ace")
                .description(WORD_ACCURACY + "+ Luck+")
                .build();

        public static ItemValues aimAssist = new ItemValues.ItemValueBuilder().region("item/AimAssist")
                .name("Aim Assist")
                .description(WORD_ACCURACY + "++ Range+")
                .build();

        public static ItemValues CriticalEye = new ItemValues.ItemValueBuilder().region("item/CriticalEye")
                .name("Critical Eye")
                .description(WORD_ACCURACY + "+++")
                .build();

        public static ItemValues KeenEye = new ItemValues.ItemValueBuilder().region("item/KeenEye")
                .name("Keen Eye")
                .description(WORD_ACCURACY + "+")
                .build();

        public static ItemValues blockOfEnergy = new ItemValues.ItemValueBuilder().region("item/BlockOfEnergy")
                .name("Block of Energy")
                .description("Accuracy+ FireRate+")
                .build();

    }


    public static class Armor {

        public static ItemValues ironBody = new ItemValues.ItemValueBuilder().region("item/IronBody")
                .name("Iron Body")
                .description("You feel heavier")
                .build();

        public static ItemValues slimeCoat = new ItemValues.ItemValueBuilder().region("enemy/blob", 2)
                .name("Slime Coat")
                .description("Eww..")
                .build();


        public static ItemValues squareBuckler = new ItemValues.ItemValueBuilder().region("item/SquareBuckler")
                .name("Square Buckler")
                .description("Good for two hits")
                .build();

        public static ItemValues vitaminC = new ItemValues.ItemValueBuilder().region("item/VitaminC")
                .name("Vitamin C")
                .description("You no longer have Scurvy")
                .build();


    }


    public static class Damage {

        public static ItemValues anger = new ItemValues.ItemValueBuilder().region("item/Anger")
                .name("Anger")
                .description("Damage++")
                .build();

        public static ItemValues luckyShot = new ItemValues.ItemValueBuilder().region("item/LuckyShot")
                .name("Lucky Shot")
                .description("Damage+ Luck+")
                .build();

        public static ItemValues miniCatapult = new ItemValues.ItemValueBuilder().region("item/MiniCatapult")
                .name("Mini Catapult")
                .description("Damage+ Range+")
                .build();

        public static ItemValues miniTrebuchet = new ItemValues.ItemValueBuilder().region("item/MiniTrebuchet")
                .name("Mini Trebuchet")
                .description("Damage++ Range++")
                .build();

        public static ItemValues smoulderingEmber = new ItemValues.ItemValueBuilder().region("item/SmoulderingEmber")
                .name("Smouldering Ember")
                .description("Damage+ FireRate+")
                .build();

        public static ItemValues stability = new ItemValues.ItemValueBuilder().region("item/Stability")
                .name("Stability")
                .description("Damage+ Accuracy+")
                .build();


    }


    public static class FireRate {

        public static ItemValues elasticity = new ItemValues.ItemValueBuilder().region("item/Elasticity")
                .name("Elasticity")
                .description("FireRate+ Range+")
                .build();

        public static ItemValues minorAccerlerant = new ItemValues.ItemValueBuilder().region("item/MinorAccelerant")
                .name("Minor Accelerant")
                .description("FireRate+ Speed+")
                .build();

        public static ItemValues runedFragment = new ItemValues.ItemValueBuilder().region("item/RunedFragment")
                .name("Runed Fragment")
                .description("FireRate+ Luck+")
                .build();


        public static ItemValues swiftShot = new ItemValues.ItemValueBuilder().region("item/SwiftShot")
                .name("Swift Shot")
                .description("FireRate++")
                .build();

        public static ItemValues tacticalKnitwear = new ItemValues.ItemValueBuilder().region("item/TacticalKnitwear")
                .name("Tactical Knitwear")
                .description("FireRate+ Accuracy+")
                .build();


    }


    public static class Health {

        public static ItemValues hypertrophy = new ItemValues.ItemValueBuilder().region("item/Hypertrophy")
                .name("Hypertrophy")
                .description("Health+ Damage+ Speed-")
                .build();

        public static ItemValues ironFragment = new ItemValues.ItemValueBuilder().region("item/IronFragment")
                .name("Iron Fragment")
                .description("Health+ Armor+")
                .build();

        public static ItemValues sarcasticLion = new ItemValues.ItemValueBuilder().region("item/SarcasticLion")
                .name("Sarcastic Lion")
                .description("Rawr")
                .build();


        public static ItemValues sootheNote = new ItemValues.ItemValueBuilder().region("item/SootheNote")
                .name("Soothe Note")
                .description("Soothing")
                .build();

        public static ItemValues medicine = new ItemValues.ItemValueBuilder().region("item/Medicine")
                .name("Medicine")
                .description("Health+")
                .build();


    }


    public static class Luck {

        public static ItemValues forgottenFigment = new ItemValues.ItemValueBuilder().region("item/ForgottenScarab")
                .name("Forgotten Figment")
                .description("Something feels off...")
                .build();

        public static ItemValues goldenFigment = new ItemValues.ItemValueBuilder().region("item/GoldenScarab")
                .name("Golden Figment")
                .description("Luck+++")
                .build();


        public static ItemValues jadeFigment = new ItemValues.ItemValueBuilder().region("item/JadeScarab")
                .name("Jade Figment")
                .description("Luck++")
                .build();

        public static ItemValues threeLeafClover = new ItemValues.ItemValueBuilder().region("item/ThreeLeafClover")
                .name("Three Leaf Clover")
                .description("Close Enough... Luck+")
                .build();

    }


    public static class Range {

        public static ItemValues clearSight = new ItemValues.ItemValueBuilder().region("item/ClearSight")
                .name("Clear Sight")
                .description("Range++ Accuracy+")
                .build();

        public static ItemValues fireSight = new ItemValues.ItemValueBuilder().region("item/FireSight")
                .name("Fire Sight")
                .description("Range+ Damage+")
                .build();

        public static ItemValues laserScope = new ItemValues.ItemValueBuilder().region("item/LaserScope")
                .name("Laser Scope")
                .description("Range+++")
                .build();

        public static ItemValues lostLettersRangeFireRate = new ItemValues.ItemValueBuilder().region("item/LostLettersRangeFireRate")
                .name("Lost Letters")
                .description("R and F?")
                .build();

        public static ItemValues neatCube = new ItemValues.ItemValueBuilder().region("item/NeatCube")
                .name("Neat Cube")
                .description("Pretty Neat")
                .build();

        public static ItemValues quadonometry = new ItemValues.ItemValueBuilder().region("item/Quadonometry")
                .name("Quadonometry")
                .description("Range+ Accuracy+")
                .build();

        public static ItemValues scope = new ItemValues.ItemValueBuilder().region("item/Scope")
                .name("Scope")
                .description("Range++")
                .build();

    }

    public static class ShotSpeed {

        public static ItemValues boringRock = new ItemValues.ItemValueBuilder().region("item/BoringRock")
                .name("Boring Rock")
                .description("Pretty Boring...")
                .build();

        public static ItemValues bubble = new ItemValues.ItemValueBuilder().region("item/Bubble")
                .name("Bubble")
                .description("ShotSpeed++")
                .build();

        public static ItemValues dullFeather = new ItemValues.ItemValueBuilder().region("item/DullFeather")
                .name("Dull Feather")
                .description("ShotSpeed+ Speed+")
                .build();

        public static ItemValues lostLettersShotSpeedAccuracy = new ItemValues.ItemValueBuilder().region("item/LostLettersShotSpeedAccuracy")
                .name("Lost Letters")
                .description("S and A?")
                .build();

        public static ItemValues momentum = new ItemValues.ItemValueBuilder().region("item/Momentum")
                .name("Momentum")
                .description("ShotSpeed++ Speed+ Damage+")
                .build();

        public static ItemValues shinyFeather = new ItemValues.ItemValueBuilder().region("item/ShinyFeather")
                .name("Shiny Feather")
                .description("ShotSpeed++ Speed+")
                .build();


    }


    public static class Speed {

        public static ItemValues quickness = new ItemValues.ItemValueBuilder().region("item/Quickness")
                .name("Quickness")
                .description("Speed+++")
                .build();


    }



    public static class PickUp {

        public static ItemValues armorUp = new ItemValues.ItemValueBuilder().region("item/armor")
                .name("Armor Up")
                .description("Armor+")
                .build();

        public static ItemValues healthUp = new ItemValues.ItemValueBuilder().region("item/heart", 1)
                .name("Health Up")
                .description("Health+")
                .build();

        public static ItemValues fullHealthUp = new ItemValues.ItemValueBuilder().region("item/heart", 0)
                .name("Health Up")
                .description("Health++")
                .build();



        public static ItemValues moneyUp = new ItemValues.ItemValueBuilder().region("gold")
                .name("Money Up")
                .description("Money+")
                .build();

    }





}
