package com.byrjamin.wickedwizard.factories.items;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;
import com.byrjamin.wickedwizard.assets.ColorResource;
import com.byrjamin.wickedwizard.assets.TextureStrings;
import com.byrjamin.wickedwizard.factories.arenas.challenges.ChallengesResource;
import com.byrjamin.wickedwizard.factories.items.companions.ItemCrownOfBiggaBlobba;
import com.byrjamin.wickedwizard.factories.items.companions.ItemDangerDetector;
import com.byrjamin.wickedwizard.factories.items.companions.ItemMiniSpinnyThingie;
import com.byrjamin.wickedwizard.factories.items.passives.accuracy.ItemAce;
import com.byrjamin.wickedwizard.factories.items.passives.accuracy.ItemAimAssist;
import com.byrjamin.wickedwizard.factories.items.passives.accuracy.ItemBlockOfEnergy;
import com.byrjamin.wickedwizard.factories.items.passives.accuracy.ItemCriticalEye;
import com.byrjamin.wickedwizard.factories.items.passives.accuracy.ItemKeenEye;
import com.byrjamin.wickedwizard.factories.items.passives.accuracy.ItemLensLessMonocle;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemAngrySlimeCoat;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemIronBody;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemSlimeCoat;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemSquareBuckler;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemVitaminC;
import com.byrjamin.wickedwizard.factories.items.passives.armor.ItemWandasScarf;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemAnger;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemLuckyShot;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemMiniCatapult;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemMiniTrebuchet;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemSmoulderingEmber;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemStability;
import com.byrjamin.wickedwizard.factories.items.passives.damage.ItemStatueOfAjir;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemStatueOfAdoj;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemElasticity;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemMinorAccelerant;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemRunedFragment;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemSwiftShot;
import com.byrjamin.wickedwizard.factories.items.passives.firerate.ItemTacticalKnitwear;
import com.byrjamin.wickedwizard.factories.items.passives.health.ItemHyperTrophy;
import com.byrjamin.wickedwizard.factories.items.passives.health.ItemIronFragment;
import com.byrjamin.wickedwizard.factories.items.passives.health.ItemSarcasticLion;
import com.byrjamin.wickedwizard.factories.items.passives.health.ItemSootheNote;
import com.byrjamin.wickedwizard.factories.items.passives.health.Medicine;
import com.byrjamin.wickedwizard.factories.items.passives.luck.ItemForgottenFigment;
import com.byrjamin.wickedwizard.factories.items.passives.luck.ItemGoldenFigment;
import com.byrjamin.wickedwizard.factories.items.passives.luck.ItemJadeFigment;
import com.byrjamin.wickedwizard.factories.items.passives.luck.ItemThreeDimensionalGold;
import com.byrjamin.wickedwizard.factories.items.passives.luck.ItemThreeLeafClover;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemClearSight;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemFireSight;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemLaserScope;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemLostLettersRangeFireRate;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemNeatCube;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemQuadonometry;
import com.byrjamin.wickedwizard.factories.items.passives.range.ItemScope;
import com.byrjamin.wickedwizard.factories.items.passives.shotspeed.ItemBoringRock;
import com.byrjamin.wickedwizard.factories.items.passives.shotspeed.ItemBubble;
import com.byrjamin.wickedwizard.factories.items.passives.shotspeed.ItemDisappointment;
import com.byrjamin.wickedwizard.factories.items.passives.shotspeed.ItemDullFeather;
import com.byrjamin.wickedwizard.factories.items.passives.shotspeed.ItemLostLettersShotSpeedAccuracy;
import com.byrjamin.wickedwizard.factories.items.passives.shotspeed.ItemMomentum;
import com.byrjamin.wickedwizard.factories.items.passives.shotspeed.ItemShinyFeather;
import com.byrjamin.wickedwizard.factories.items.passives.speed.ItemQuickness;
import com.byrjamin.wickedwizard.utils.Pair;
import com.byrjamin.wickedwizard.utils.enums.ItemType;

/**
 * Created by Home on 06/08/2017.
 */

public class ItemResource {


    public static Array<Item> allItems = new Array<Item>();

    static {
        allItems.addAll(Accuracy.accuracyItems);
        allItems.addAll(Armor.armorItems);
        allItems.addAll(Damage.damageItems);
        allItems.addAll(FireRate.fireRateItems);
        allItems.addAll(Health.healthItems);
        allItems.addAll(Luck.luckItems);
        allItems.addAll(Range.rangeItems);
        allItems.addAll(ShotSpeed.shotSpeedItems);
        allItems.addAll(Speed.speedItems);
        allItems.addAll(Companion.companionItems);
    }

    public static Array<Item> getAllItems() {

        Array<Item> all = new Array<Item>();
        all.addAll(allItems);
        return all;
    }




    public static String WORD_ACCURACY = "Accuracy";

    public static class ItemValues {

        public final String id;
        public final Pair<String, Integer> region;
        public final Color textureColor;
        public final String name;
        public final String description;
        public final Array<ItemType> itemTypes;
        public final String challengeId;


        public static class ItemValueBuilder {

            //Required
            private String id;

            //Optional
            private Pair<String, Integer> region = new Pair<String,Integer>("item/SarcasticLion", 0);
            private Color textureColor = new Color(Color.WHITE);
            private String name = "Default Item";
            private String description = "You forgot to set this value";
            private Array<ItemType> itemTypes = new Array<ItemType>();
            private String challengeId = ChallengesResource.TUTORIAL_COMPLETE;


            public ItemValueBuilder(String id){
                this.id = id;
                this.itemTypes.addAll(ItemType.BOSS, ItemType.ITEM, ItemType.SHOP);
            }

            public ItemValueBuilder region(String s)
            { region = new Pair<String, Integer>(s,0); return this; }

            public ItemValueBuilder region(String s, int index)
            { region = new Pair<String, Integer>(s, index); return this; }


            public ItemValueBuilder textureColor(Color val)
            { textureColor = val; return this; }

            public ItemValueBuilder name(String val)
            { name = val; return this; }

            public ItemValueBuilder description(String val)
            { description = val; return this; }

            public ItemValueBuilder itemTypes(ItemType... val) {
                itemTypes.clear();
                itemTypes.addAll(val);
                return this;
            }

            public ItemValueBuilder challengeId(String val)
            { challengeId = val; return this; }


            public ItemValues build() {
                return new ItemValues(this);
            }


        }

        public ItemValues(ItemValueBuilder ivb){
            this.id = ivb.id;
            this.region = ivb.region;
            this.textureColor = ivb.textureColor;
            this.name = ivb.name;
            this.description = ivb.description;
            this.itemTypes = ivb.itemTypes;
            this.challengeId = ivb.challengeId;
        }


    }



    //Accuracy
    public static class Accuracy {


        public static Item[] accuracyItems = {
                new ItemAce(),
                new ItemAimAssist(),
                new ItemBlockOfEnergy(),
                new ItemCriticalEye(),
                new ItemKeenEye(),
                new ItemLensLessMonocle()};


        public static ItemValues Ace = new ItemValues.ItemValueBuilder("005e433e-81d8-11e7-bb31-be2e44b06b34")
                .region("item/Ace")
                .name("Ace")
                .description(WORD_ACCURACY + "+ Luck+")
                .build();

        public static ItemValues aimAssist = new ItemValues.ItemValueBuilder("005e4604-81d8-11e7-bb31-be2e44b06b34")
                .region("item/AimAssist")
                .name("Aim Assist")
                .description(WORD_ACCURACY + "++ Range+")
                .build();

        public static ItemValues CriticalEye = new ItemValues.ItemValueBuilder("005e4712-81d8-11e7-bb31-be2e44b06b34")
                .region("item/CriticalEye")
                .name("Critical Eye")
                .description(WORD_ACCURACY + "+++")
                .challengeId(ChallengesResource.Rank3Challenges.rank3ArenaTrial)
                .build();

        public static ItemValues KeenEye = new ItemValues.ItemValueBuilder("005e4ac8-81d8-11e7-bb31-be2e44b06b34")
                .region("item/KeenEye")
                .name("Keen Eye")
                .description(WORD_ACCURACY + "+")
                .build();

        public static ItemValues blockOfEnergy = new ItemValues.ItemValueBuilder("005e4e60-81d8-11e7-bb31-be2e44b06b34")
                .region("item/BlockOfEnergy")
                .name("Block of Energy")
                .description("Accuracy+ FireRate+")
                .build();

        public static ItemValues lensLessMagnifyingGlass = new ItemValues.ItemValueBuilder("b0c88615-51fb-4bd3-ac6d-3b5cc362d8e8")
                .region("item/LensLessMagnifyingGlass")
                .name("Lensless Magnifying Glass")
                .description("This somehow improves Accuracy")
                .build();

    }


    public static class Armor {

        public static Item[] armorItems = {
                new ItemAngrySlimeCoat(),
                new ItemIronBody(),
                new ItemSlimeCoat(),
                new ItemSquareBuckler(),
                new ItemVitaminC(),
                new ItemWandasScarf()};

        public static ItemValues angrySlimeCoat = new ItemValues.ItemValueBuilder("54d58ef6-3534-49bb-acac-3c484febff2a")
                .challengeId(ChallengesResource.Rank3Challenges.rank3TimeTrail)
                .region("enemy/blob", 2)
                .textureColor(ColorResource.BLOB_RED)
                .name("Slime Coat")
                .description("Eww.. but also Grr..")
                .challengeId(ChallengesResource.Rank1Challenges.tutorialSpeedRun)
                .build();


        public static ItemValues ironBody = new ItemValues.ItemValueBuilder("795d5bee-81d8-11e7-bb31-be2e44b06b34")
                .region("item/IronBody")
                .name("Iron Body")
                .description("You feel heavier")
                .build();

        public static ItemValues slimeCoat = new ItemValues.ItemValueBuilder("795d5ea0-81d8-11e7-bb31-be2e44b06b34")
                .region("enemy/blob", 2)
                .textureColor(ColorResource.BLOB_GREEN)
                .name("Slime Coat")
                .description("Eww..")
                .challengeId(ChallengesResource.Rank1Challenges.tutorialSpeedRun)
                .build();


        public static ItemValues squareBuckler = new ItemValues.ItemValueBuilder("795d6508-81d8-11e7-bb31-be2e44b06b34")
                .region("item/SquareBuckler")
                .name("Square Buckler")
                .description("Good for two hits")
                .build();

        public static ItemValues vitaminC = new ItemValues.ItemValueBuilder("795d68be-81d8-11e7-bb31-be2e44b06b34")
                .region("item/VitaminC")
                .name("Vitamin C")
                .description("You no longer have Scurvy")
                .build();


        public static ItemValues wandasScarf = new ItemValues.ItemValueBuilder("4f4436bc-088d-49c0-b39d-2c5ab9a0e3fb")
                .region("item/WandasScarf", 2)
                .name("Wanda's Scarf")
                .description("She's like you, you know.")
                .challengeId(ChallengesResource.Rank2Challenges.perfectWanda)
                .build();


    }


    public static class Damage {

        public static Item[] damageItems = {
                new ItemAnger(),
                new ItemLuckyShot(),
                new ItemMiniCatapult(),
                new ItemMiniTrebuchet(),
                new ItemSmoulderingEmber(),
                new ItemStability(),
                new ItemStatueOfAjir()};

        public static ItemValues anger = new ItemValues.ItemValueBuilder("9863a17e-81d8-11e7-bb31-be2e44b06b34")
                .itemTypes(ItemType.BOSS)
                .challengeId(ChallengesResource.Rank1Challenges.arenaSpeedRun)
                .region("item/Anger")
                .name("Anger")
                .description("Damage++")
                .build();

        public static ItemValues luckyShot = new ItemValues.ItemValueBuilder("9863a7a0-81d8-11e7-bb31-be2e44b06b34")
                .region("item/LuckyShot")
                .name("Lucky Shot")
                .description("Damage+ Luck+")
                .build();

        public static ItemValues miniCatapult = new ItemValues.ItemValueBuilder("9863a944-81d8-11e7-bb31-be2e44b06b34")
                .region("item/MiniCatapult")
                .name("Mini Catapult")
                .description("Damage+ Range+")
                .build();

        public static ItemValues miniTrebuchet = new ItemValues.ItemValueBuilder("9863aa8e-81d8-11e7-bb31-be2e44b06b34")
                .region("item/MiniTrebuchet")
                .name("Mini Trebuchet")
                .description("Damage++ Range++")
                .challengeId(ChallengesResource.Rank4Challenges.rank4Arena)
                .build();

        public static ItemValues smoulderingEmber = new ItemValues.ItemValueBuilder("9863abce-81d8-11e7-bb31-be2e44b06b34")
                .region("item/SmoulderingEmber")
                .name("Smouldering Ember")
                .description("Damage+ FireRate+")
                .build();

        public static ItemValues stability = new ItemValues.ItemValueBuilder("9863ad04-81d8-11e7-bb31-be2e44b06b34")
                .region("item/Stability")
                .name("Stability")
                .description("Damage+ Accuracy+")
                .build();


        public static ItemValues statueOfAjir = new ItemValues.ItemValueBuilder("e45f39f3-4ea3-493d-b985-c02527b4167d")
                .challengeId(ChallengesResource.Rank3Challenges.perfectAjir)
                .itemTypes(ItemType.BOSS)
                .region("item/StatueOfAjir")
                .name("Statue Of Ajir")
                .description("Too Lifelike")
                .build();

    }


    public static class FireRate {

        public static Item[] fireRateItems = {
                new ItemElasticity(),
                new ItemMinorAccelerant(),
                new ItemRunedFragment(),
                new ItemSwiftShot(),
                new ItemStatueOfAdoj(),
                new ItemTacticalKnitwear()};

        public static ItemValues statueOfAdoj = new ItemValues.ItemValueBuilder("32ab3abc-81d9-11e7-bb31-be2e44b06b34")
                .challengeId(ChallengesResource.Rank1Challenges.perfectAdoj)
                .region("item/StatueOfAdoj")
                .name("Statue Of Adoj")
                .description("So Lifelike")
                .build();


        public static ItemValues elasticity = new ItemValues.ItemValueBuilder("32ab3abc-81d9-11e7-bb31-be2e44b06b34")
                .region("item/Elasticity")
                .name("Elasticity")
                .description("FireRate+ Range+")
                .build();

        public static ItemValues minorAccerlerant = new ItemValues.ItemValueBuilder("32ab3d64-81d9-11e7-bb31-be2e44b06b34")
                .region("item/MinorAccelerant")
                .name("Minor Accelerant")
                .description("FireRate+ Speed+")
                .build();

        public static ItemValues runedFragment = new ItemValues.ItemValueBuilder("32ab3e5e-81d9-11e7-bb31-be2e44b06b34")
                .region("item/RunedFragment")
                .name("Runed Fragment")
                .description("FireRate+ Luck+")
                .build();


        public static ItemValues swiftShot = new ItemValues.ItemValueBuilder("32ab3f3a-81d9-11e7-bb31-be2e44b06b34")
                .region("item/SwiftShot")
                .name("Swift Shot")
                .description("FireRate++")
                .build();

        public static ItemValues tacticalKnitwear = new ItemValues.ItemValueBuilder("32ab4386-81d9-11e7-bb31-be2e44b06b34")
                .region("item/TacticalKnitwear")
                .name("Tactical Knitwear")
                .description("FireRate+ Accuracy+")
                .build();


    }


    public static class Health {

        public static Item[] healthItems = {
                new ItemHyperTrophy(),
                new ItemIronFragment(),
                new ItemSarcasticLion(),
                new ItemSootheNote(),
                new Medicine()};


        public static ItemValues hypertrophy = new ItemValues.ItemValueBuilder("5085bada-81d9-11e7-bb31-be2e44b06b34")
                .region("item/Hypertrophy")
                .name("Hypertrophy")
                .description("Health+ Damage+ Speed-")
                .build();

        public static ItemValues ironFragment = new ItemValues.ItemValueBuilder("5085bdd2-81d9-11e7-bb31-be2e44b06b34")
                .region("item/IronFragment")
                .name("Iron Fragment")
                .description("Health+ Armor+")
                .build();

        public static ItemValues sarcasticLion = new ItemValues.ItemValueBuilder("5085becc-81d9-11e7-bb31-be2e44b06b34")
                .region("item/SarcasticLion")
                .name("Sarcastic Lion")
                .description("Rawr")
                .build();


        public static ItemValues sootheNote = new ItemValues.ItemValueBuilder("5085bf9e-81d9-11e7-bb31-be2e44b06b34")
                .region("item/SootheNote")
                .name("Soothe Note")
                .description("Soothing")
                .build();

        public static ItemValues medicine = new ItemValues.ItemValueBuilder("5085c066-81d9-11e7-bb31-be2e44b06b34")
                .region("item/Medicine")
                .name("Medicine")
                .description("Health+")
                .build();


    }


    public static class Luck {

        public static Item[] luckItems = {
                new ItemForgottenFigment(),
                new ItemGoldenFigment(),
                new ItemJadeFigment(),
                new ItemThreeDimensionalGold(),
                new ItemThreeLeafClover()};

        public static ItemValues forgottenFigment = new ItemValues.ItemValueBuilder("6b37cd6e-81d9-11e7-bb31-be2e44b06b34")
                .challengeId(ChallengesResource.Rank2Challenges.arenaTrail)
                .region("item/ForgottenScarab")
                .name("Forgotten Figment")
                .description("Something feels off...")
                .build();

        public static ItemValues goldenFigment = new ItemValues.ItemValueBuilder("6b37d07a-81d9-11e7-bb31-be2e44b06b34")
                .region("item/GoldenScarab")
                .name("Golden Figment")
                .description("Luck+++")
                .build();


        public static ItemValues jadeFigment = new ItemValues.ItemValueBuilder("6b37d17e-81d9-11e7-bb31-be2e44b06b34")
                .region("item/JadeScarab")
                .name("Jade Figment")
                .description("Luck++")
                .build();

        public static ItemValues threeLeafClover = new ItemValues.ItemValueBuilder("6b37d250-81d9-11e7-bb31-be2e44b06b34")
                .region("item/ThreeLeafClover")
                .name("Three Leaf Clover")
                .description("Close Enough... Luck+")
                .build();


        public static ItemValues threeDimensionalGold = new ItemValues.ItemValueBuilder("36712291-d64c-4ea2-bea7-de3e8395d463")
                .challengeId(ChallengesResource.Rank3Challenges.perfectBoomy)
                .region("item/ThreeDimensionalGold")
                .name("3-Dimensional Gold")
                .description("Woah")
                .build();


    }


    public static class Range {

        public static Item[] rangeItems = {
                new ItemClearSight(),
                new ItemFireSight(),
                new ItemLaserScope(),
                new ItemLostLettersRangeFireRate(),
                new ItemNeatCube(),
                new ItemQuadonometry(),
                new ItemScope()};

        public static ItemValues clearSight = new ItemValues.ItemValueBuilder("8074c97a-81d9-11e7-bb31-be2e44b06b34")
                .region("item/ClearSight")
                .name("Clear Sight")
                .description("Range++ Accuracy+")
                .build();

        public static ItemValues fireSight = new ItemValues.ItemValueBuilder("8074cf92-81d9-11e7-bb31-be2e44b06b34")
                .region("item/FireSight")
                .name("Fire Sight")
                .description("Range+ Damage+")
                .build();

        public static ItemValues laserScope = new ItemValues.ItemValueBuilder("8074d1ae-81d9-11e7-bb31-be2e44b06b34")
                .region("item/LaserScope")
                .name("Laser Scope")
                .description("Range+++")
                .build();

        public static ItemValues lostLettersRangeFireRate = new ItemValues.ItemValueBuilder("8074d488-81d9-11e7-bb31-be2e44b06b34")
                .region("item/LostLettersRangeFireRate")
                .name("Lost Letters")
                .description("R and F?")
                .build();

        public static ItemValues neatCube = new ItemValues.ItemValueBuilder("8074d65e-81d9-11e7-bb31-be2e44b06b34")
                .region("item/NeatCube")
                .name("Neat Cube")
                .description("Pretty Neat")
                .build();

        public static ItemValues quadonometry = new ItemValues.ItemValueBuilder("8074d816-81d9-11e7-bb31-be2e44b06b34")
                .region("item/Quadonometry")
                .name("Quadonometry")
                .description("Range+ Accuracy+")
                .build();

        public static ItemValues scope = new ItemValues.ItemValueBuilder("8074db54-81d9-11e7-bb31-be2e44b06b34")
                .region("item/Scope")
                .name("Scope")
                .description("Range++")
                .build();

    }

    public static class ShotSpeed {

        public static Item[] shotSpeedItems = {
                new ItemBoringRock(),
                new ItemBubble(),
                new ItemDisappointment(),
                new ItemDullFeather(),
                new ItemLostLettersShotSpeedAccuracy(),
                new ItemMomentum(),
                new ItemShinyFeather()};


        public static ItemValues boringRock = new ItemValues.ItemValueBuilder("ad70fbce-81d9-11e7-bb31-be2e44b06b34")
                .region("item/BoringRock")
                .name("Boring Rock")
                .description("Pretty Boring...")
                .build();

        public static ItemValues bubble = new ItemValues.ItemValueBuilder("ad70fe44-81d9-11e7-bb31-be2e44b06b34")
                .region("item/Bubble")
                .name("Bubble")
                .description("ShotSpeed++")
                .build();

        public static ItemValues disappointment = new ItemValues.ItemValueBuilder("edbd9f26-1b15-4f8d-a15d-f620066e0154")
                .region("item/Disappointment")
                .name("Disappointment")
                .description("...")
                .build();

        public static ItemValues dullFeather = new ItemValues.ItemValueBuilder("ad70ff34-81d9-11e7-bb31-be2e44b06b34")
                .region("item/DullFeather")
                .name("Dull Feather")
                .description("ShotSpeed+ Speed+")
                .build();

        public static ItemValues lostLettersShotSpeedAccuracy = new ItemValues.ItemValueBuilder("ad710074-81d9-11e7-bb31-be2e44b06b34")
                .region("item/LostLettersShotSpeedAccuracy")
                .name("Lost Letters")
                .description("S and A?")
                .build();

        public static ItemValues momentum = new ItemValues.ItemValueBuilder("ad7101fa-81d9-11e7-bb31-be2e44b06b34")
                .region("item/Momentum")
                .name("Momentum")
                .description("ShotSpeed++ Speed+ Damage+")
                .build();

        public static ItemValues shinyFeather = new ItemValues.ItemValueBuilder("ad710358-81d9-11e7-bb31-be2e44b06b34")
                .challengeId(ChallengesResource.Rank2Challenges.rank2TimeTrail)
                .region("item/ShinyFeather")
                .name("Shiny Feather")
                .description("ShotSpeed++ Speed++")
                .build();


    }


    public static class Speed {

        public static Item[] speedItems = {
                new ItemQuickness()};


        public static ItemValues quickness = new ItemValues.ItemValueBuilder("ce6f7cce-81d9-11e7-bb31-be2e44b06b34").region("item/Quickness")
                .challengeId(ChallengesResource.Rank4Challenges.rank4TimeTrail)
                .name("Quickness")
                .description("Speed+++")
                .build();


    }


    public static class Companion {

        public static Item[] companionItems = {
                new ItemDangerDetector(),
                new ItemCrownOfBiggaBlobba(),
                new ItemMiniSpinnyThingie()
        };

        public static ItemValues dangerDetector =  new ItemValues.ItemValueBuilder("f151f958-cedf-47ce-96c1-2271ac417859")
                .region("item/companion/DangerDetector")
                .name("Danger Detector")
                .description("Detects Danger apparently")
                .build();


        public static ItemValues crownOfBiggaBlobba =  new ItemValues.ItemValueBuilder("8a6a0e43-10fc-4939-880e-5e69b48173f8")
                .challengeId(ChallengesResource.Rank1Challenges.perfectBlobba)
                .region("item/companion/CrownOfBiggaBlobba")
                .name("Crown Of BiggaBlobba")
                .description("Fits Nicely")
                .build();


        public static ItemValues miniSpinnyThingie =  new ItemValues.ItemValueBuilder("2200b179-33ee-4cd9-9853-515708bceda8")
                .challengeId(ChallengesResource.Rank2Challenges.perfectKugel)
                .region(TextureStrings.KUGELDUSCHE_LASER)
                .name("Mini Spinny Thingie")
                .description("A thingie that is mini and spinnys")
                .build();







    }



    public static class PickUp {

        public static ItemValues armorUp = new ItemValues.ItemValueBuilder("d21f630c-81d9-11e7-bb31-be2e44b06b34").region("item/armor")
                .name("Armor Up")
                .description("Armor+")
                .build();

        public static ItemValues healthUp = new ItemValues.ItemValueBuilder("d21f66f4-81d9-11e7-bb31-be2e44b06b34")
                .region("item/heart", 1)
                .name("Health Up")
                .description("Health+")
                .build();

        public static ItemValues fullHealthUp = new ItemValues.ItemValueBuilder("d21f68c0-81d9-11e7-bb31-be2e44b06b34")
                .region("item/heart", 0)
                .name("Health Up")
                .description("Health++")
                .build();



        public static ItemValues moneyUp = new ItemValues.ItemValueBuilder("d21f6f82-81d9-11e7-bb31-be2e44b06b34")
                .region("gold")
                .name("Money Up")
                .description("Money+")
                .build();

    }





}
