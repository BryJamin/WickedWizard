package com.bryjamin.wickedwizard.factories.items;

import com.badlogic.gdx.utils.Array;
import com.bryjamin.wickedwizard.assets.ColorResource;
import com.bryjamin.wickedwizard.assets.TextureStrings;
import com.bryjamin.wickedwizard.factories.arenas.challenges.adventure.AdventureUnlocks;
import com.bryjamin.wickedwizard.factories.items.companions.ItemAutoRockets;
import com.bryjamin.wickedwizard.factories.items.companions.ItemCrownOfBiggaBlobba;
import com.bryjamin.wickedwizard.factories.items.companions.ItemDangerDetector;
import com.bryjamin.wickedwizard.factories.items.companions.ItemGhastlyWail;
import com.bryjamin.wickedwizard.factories.items.companions.ItemLiasCrown;
import com.bryjamin.wickedwizard.factories.items.companions.ItemMegaSideCannons;
import com.bryjamin.wickedwizard.factories.items.companions.ItemMiniSpinnyThingie;
import com.bryjamin.wickedwizard.factories.items.companions.ItemMyVeryOwnStalker;
import com.bryjamin.wickedwizard.factories.items.companions.ItemSideCannons;
import com.bryjamin.wickedwizard.factories.items.companions.ItemTesserWreck;
import com.bryjamin.wickedwizard.factories.items.companions.ItemXisGlare;
import com.bryjamin.wickedwizard.factories.items.conditionals.ItemLabyrinthCodex;
import com.bryjamin.wickedwizard.factories.items.passives.accuracy.ItemAce;
import com.bryjamin.wickedwizard.factories.items.passives.accuracy.ItemAimAssist;
import com.bryjamin.wickedwizard.factories.items.passives.accuracy.ItemBlazingShades;
import com.bryjamin.wickedwizard.factories.items.passives.accuracy.ItemBlockOfEnergy;
import com.bryjamin.wickedwizard.factories.items.passives.accuracy.ItemCriticalEye;
import com.bryjamin.wickedwizard.factories.items.passives.accuracy.ItemKeenEye;
import com.bryjamin.wickedwizard.factories.items.passives.accuracy.ItemLensLessMonocle;
import com.bryjamin.wickedwizard.factories.items.passives.accuracy.ItemSharpShootersDiary;
import com.bryjamin.wickedwizard.factories.items.passives.armor.ItemAngrySlimeCoat;
import com.bryjamin.wickedwizard.factories.items.passives.armor.ItemArmorUp;
import com.bryjamin.wickedwizard.factories.items.passives.armor.ItemBookOfXi;
import com.bryjamin.wickedwizard.factories.items.passives.armor.ItemIronBody;
import com.bryjamin.wickedwizard.factories.items.passives.armor.ItemSlimeCoat;
import com.bryjamin.wickedwizard.factories.items.passives.armor.ItemSmoulderingArmor;
import com.bryjamin.wickedwizard.factories.items.passives.armor.ItemSquareBuckler;
import com.bryjamin.wickedwizard.factories.items.passives.armor.ItemSteppingStones;
import com.bryjamin.wickedwizard.factories.items.passives.armor.ItemTesssScarf;
import com.bryjamin.wickedwizard.factories.items.passives.armor.ItemVitaminC;
import com.bryjamin.wickedwizard.factories.items.passives.armor.ItemWandasScarf;
import com.bryjamin.wickedwizard.factories.items.passives.armor.ItemWhiteGoggles;
import com.bryjamin.wickedwizard.factories.items.passives.armor.ItemXisOldScarf;
import com.bryjamin.wickedwizard.factories.items.passives.damage.ItemAnger;
import com.bryjamin.wickedwizard.factories.items.passives.damage.ItemLuckyShot;
import com.bryjamin.wickedwizard.factories.items.passives.damage.ItemMiniCatapult;
import com.bryjamin.wickedwizard.factories.items.passives.damage.ItemSmoulderingEmber;
import com.bryjamin.wickedwizard.factories.items.passives.damage.ItemStability;
import com.bryjamin.wickedwizard.factories.items.passives.damage.ItemStatueOfAjir;
import com.bryjamin.wickedwizard.factories.items.passives.damage.rankTwo.ItemMiniTrebuchet;
import com.bryjamin.wickedwizard.factories.items.passives.firerate.ItemElasticity;
import com.bryjamin.wickedwizard.factories.items.passives.firerate.ItemSwiftShot;
import com.bryjamin.wickedwizard.factories.items.passives.firerate.ItemTacticalKnitwear;
import com.bryjamin.wickedwizard.factories.items.passives.health.ItemDilation;
import com.bryjamin.wickedwizard.factories.items.passives.health.ItemDisappointment;
import com.bryjamin.wickedwizard.factories.items.passives.health.ItemHealingTonic;
import com.bryjamin.wickedwizard.factories.items.passives.health.ItemHealingTonicPlus;
import com.bryjamin.wickedwizard.factories.items.passives.health.ItemHealthUp;
import com.bryjamin.wickedwizard.factories.items.passives.health.ItemHyperTrophy;
import com.bryjamin.wickedwizard.factories.items.passives.health.ItemIronFragment;
import com.bryjamin.wickedwizard.factories.items.passives.health.ItemPhisScarf;
import com.bryjamin.wickedwizard.factories.items.passives.health.ItemPoisonedDrop;
import com.bryjamin.wickedwizard.factories.items.passives.health.ItemSarcasticLion;
import com.bryjamin.wickedwizard.factories.items.passives.health.ItemSecondWind;
import com.bryjamin.wickedwizard.factories.items.passives.health.ItemSootheNote;
import com.bryjamin.wickedwizard.factories.items.passives.health.Medicine;
import com.bryjamin.wickedwizard.factories.items.passives.luck.ItemEnigmaticShot;
import com.bryjamin.wickedwizard.factories.items.passives.luck.ItemEquivalentExchange;
import com.bryjamin.wickedwizard.factories.items.passives.luck.ItemEyesOfAmalgama;
import com.bryjamin.wickedwizard.factories.items.passives.luck.ItemForgottenFigment;
import com.bryjamin.wickedwizard.factories.items.passives.luck.ItemGoldenFigment;
import com.bryjamin.wickedwizard.factories.items.passives.luck.ItemIWishYouWell;
import com.bryjamin.wickedwizard.factories.items.passives.luck.ItemJadeFigment;
import com.bryjamin.wickedwizard.factories.items.passives.luck.ItemLuckPlus10;
import com.bryjamin.wickedwizard.factories.items.passives.luck.ItemLuckyRoll;
import com.bryjamin.wickedwizard.factories.items.passives.luck.ItemPatternedOpal;
import com.bryjamin.wickedwizard.factories.items.passives.luck.ItemTesseract;
import com.bryjamin.wickedwizard.factories.items.passives.luck.ItemThreeDimensionalGold;
import com.bryjamin.wickedwizard.factories.items.passives.luck.ItemThreeLeafClover;
import com.bryjamin.wickedwizard.factories.items.passives.range.ItemBuckShot;
import com.bryjamin.wickedwizard.factories.items.passives.range.ItemClearSight;
import com.bryjamin.wickedwizard.factories.items.passives.range.ItemFireSight;
import com.bryjamin.wickedwizard.factories.items.passives.range.ItemFocusShot;
import com.bryjamin.wickedwizard.factories.items.passives.range.ItemGoldenScope;
import com.bryjamin.wickedwizard.factories.items.passives.range.ItemLaserScope;
import com.bryjamin.wickedwizard.factories.items.passives.range.ItemLeahsScarf;
import com.bryjamin.wickedwizard.factories.items.passives.range.ItemLostLettersRangeFireRate;
import com.bryjamin.wickedwizard.factories.items.passives.range.ItemNeatCube;
import com.bryjamin.wickedwizard.factories.items.passives.range.ItemQuadonometry;
import com.bryjamin.wickedwizard.factories.items.passives.range.ItemScope;
import com.bryjamin.wickedwizard.factories.items.passives.shotsize.ItemBigShot;
import com.bryjamin.wickedwizard.factories.items.passives.shotsize.ItemCannonCube;
import com.bryjamin.wickedwizard.factories.items.passives.shotsize.ItemLeafCutter;
import com.bryjamin.wickedwizard.factories.items.passives.shotsize.ItemMiniShot;
import com.bryjamin.wickedwizard.factories.items.passives.shotsize.ItemPodShot;
import com.bryjamin.wickedwizard.factories.items.passives.shotsize.ItemWideLens;
import com.bryjamin.wickedwizard.factories.items.passives.shotspeed.ItemBlueEyeGems;
import com.bryjamin.wickedwizard.factories.items.passives.shotspeed.ItemBoringRock;
import com.bryjamin.wickedwizard.factories.items.passives.shotspeed.ItemBubble;
import com.bryjamin.wickedwizard.factories.items.passives.shotspeed.ItemDullFeather;
import com.bryjamin.wickedwizard.factories.items.passives.shotspeed.ItemLostLettersShotSpeedAccuracy;
import com.bryjamin.wickedwizard.factories.items.passives.shotspeed.ItemMomentum;
import com.bryjamin.wickedwizard.factories.items.passives.shotspeed.ItemShinyFeather;
import com.bryjamin.wickedwizard.factories.items.passives.speed.ItemAbstractLeaf;
import com.bryjamin.wickedwizard.factories.items.passives.speed.ItemHotStep;
import com.bryjamin.wickedwizard.factories.items.passives.speed.ItemQuickStep;
import com.bryjamin.wickedwizard.factories.items.passives.speed.ItemQuickness;
import com.bryjamin.wickedwizard.utils.enums.ItemType;

/**
 * Created by Home on 06/08/2017.
 */

public class ItemResource {


    public static Array<Item> allItems = new Array<Item>();

    static {

        AdventureUnlocks.setUpUnlocks();

        allItems.addAll(Accuracy.accuracyItems);
        allItems.addAll(Armor.armorItems);
        allItems.addAll(Damage.damageItems);
        allItems.addAll(FireRate.fireRateItems);
        allItems.addAll(Health.healthItems);
        allItems.addAll(Luck.luckItems);
        allItems.addAll(Range.rangeItems);
        allItems.addAll(ShotSize.shotSizeItems);
        allItems.addAll(ShotSpeed.shotSpeedItems);
        allItems.addAll(Speed.speedItems);
        allItems.addAll(Companion.companionItems);
        allItems.addAll(Conditionals.conditionalItems);

    }

    public static Array<Item> getAllItems() {

        Array<Item> all = new Array<Item>();
        all.addAll(allItems);
        return all;
    }


    //Accuracy
    public static class Accuracy {


        public static Item[] accuracyItems = {
                new ItemAce(),
                new ItemAimAssist(),
                new ItemBlazingShades(),
                new ItemBlockOfEnergy(),
                new ItemCriticalEye(),
                new ItemKeenEye(),
                new ItemLensLessMonocle(),
                new ItemSharpShootersDiary()};


        public static ItemLayout Ace = new ItemLayout.ItemValueBuilder("005e433e-81d8-11e7-bb31-be2e44b06b34")
                .region("item/Ace")
                .name("Ace")
                .itemTypes(com.bryjamin.wickedwizard.utils.enums.ItemType.ITEM)
                .description("Accuracy+ Luck+")
                .build();

        public static ItemLayout aimAssist = new ItemLayout.ItemValueBuilder("005e4604-81d8-11e7-bb31-be2e44b06b34")
                .region("item/AimAssist")
                .name("Aim Assist")
                .description("Accuracy++ Range+")
                .build();

        public static ItemLayout blazingShades = new ItemLayout.ItemValueBuilder("ca02c60a-ca62-4bf8-8c3b-b5dc1a8bf73f")
                .region("item/BlazingShades")
                .name("Blazing Shades")
                .description("Less Accuracy, More Fiery")
                .build();


        public static ItemLayout CriticalEye = new ItemLayout.ItemValueBuilder("005e4712-81d8-11e7-bb31-be2e44b06b34")
                .region("item/CriticalEye")
                .name("Critical Eye")
                .description("Accuracy+++")
                .build();


        public static ItemLayout erraticFire = new ItemLayout.ItemValueBuilder("ef59ff5c-45e0-4260-89bf-1cfd4ef12065")
                .region("item/ErraticFire")
                .name("Erratic Fire")
                .description("Accuracy--- FireRate+++")
                .build();

        public static ItemLayout KeenEye = new ItemLayout.ItemValueBuilder("005e4ac8-81d8-11e7-bb31-be2e44b06b34")
                .region("item/KeenEye")
                .name("Keen Eye")
                .description("Accuracy++")
                .build();

        public static ItemLayout blockOfEnergy = new ItemLayout.ItemValueBuilder("005e4e60-81d8-11e7-bb31-be2e44b06b34")
                .region("item/BlockOfEnergy")
                .name("Block of Energy")
                .description("Accuracy+ FireRate+")
                .build();

        public static ItemLayout lensLessMagnifyingGlass = new ItemLayout.ItemValueBuilder("b0c88615-51fb-4bd3-ac6d-3b5cc362d8e8")
                .region("item/LensLessMagnifyingGlass")
                .name("Lensless Magnifying Glass")
                .description("This somehow improves Accuracy")
                .build();


        public static ItemLayout sharpShootersDiary = new ItemLayout.ItemValueBuilder("98ae3842-b9e4-4e51-98ff-4302b5b9e047")
                .itemTypes(ItemType.ITEM, ItemType.SHOP)
                .region("item/SharpShootersDiary")
                .name("SharpShooter's Diary")
                .description("Aim For The Weak Spots")
                .build();

    }


    public static class Armor {

        public static Item[] armorItems = {
                new ItemAngrySlimeCoat(),
                new ItemArmorUp(),
                new ItemBookOfXi(),
                new ItemIronBody(),
                new ItemSlimeCoat(),
                new ItemSmoulderingArmor(),
                new ItemSquareBuckler(),
                new ItemSteppingStones(),
                new ItemTesssScarf(),
                new ItemVitaminC(),
                new ItemWandasScarf(),
                new ItemWhiteGoggles(),
                new ItemXisOldScarf()};

        public static ItemLayout angrySlimeCoat = new ItemLayout.ItemValueBuilder("54d58ef6-3534-49bb-acac-3c484febff2a")
                .region("enemy/blob", 2)
                .textureColor(ColorResource.BLOB_RED)
                .name("Angry Slime Coat")
                .description("Eww.. but also Grr..")
                .build();

        public static ItemLayout armorUp = new ItemLayout.ItemValueBuilder("121b0f84-359f-4d4a-832f-c5f6f77b82d1")
                .region("item/ArmorUp")
                .name("Armor Up!")
                .description("Armored Up!")
                .build();

        public static ItemLayout bookOfXi = new ItemLayout.ItemValueBuilder("3d443ef3-d5b2-47c3-93c6-f3d1cea3d5cf")
                .region("item/BookOfXi")
                .name("Book Of Xi")
                .description("Trades Health for Armor")
                .build();



        public static ItemLayout ironBody = new ItemLayout.ItemValueBuilder("795d5bee-81d8-11e7-bb31-be2e44b06b34")
                .region("item/IronBody")
                .name("Iron Body")
                .description("You feel heavier")
                .build();

        public static ItemLayout slimeCoat = new ItemLayout.ItemValueBuilder("795d5ea0-81d8-11e7-bb31-be2e44b06b34")
                .region("enemy/blob", 2)
                .textureColor(ColorResource.BLOB_GREEN)
                .name("Slime Coat")
                .description("Eww..")
                .build();

        public static ItemLayout smoulderingHelm = new ItemLayout.ItemValueBuilder("f48b95ec-ec77-4407-8d5b-2f83ba1517fb")
                .region("item/SmoulderingHelm")
                .name("Smouldering Helm")
                .description("Damage+")
                .build();


        public static ItemLayout squareBuckler = new ItemLayout.ItemValueBuilder("795d6508-81d8-11e7-bb31-be2e44b06b34")
                .region("item/SquareBuckler")
                .name("Square Buckler")
                .description("Good for two hits")
                .build();


        public static ItemLayout steppingStones = new ItemLayout.ItemValueBuilder("b0b5ef20-c00c-4cba-880f-5ad6a78c16d5")
                .region("item/SteppingStones")
                .name("Stepping Stones")
                .description("Speed+ Armor+")
                .build();


        public static ItemLayout tesssScarf = new ItemLayout.ItemValueBuilder("d04b2868-2fe2-4014-a6f3-d99e30e25d66")
                .region("item/TesssScarf")
                .name("Tess's Scarf")
                .description("Everything Up!")
                .build();


        public static ItemLayout vitaminC = new ItemLayout.ItemValueBuilder("795d68be-81d8-11e7-bb31-be2e44b06b34")
                .region("item/VitaminC")
                .name("Vitamin C")
                .description("Cures Scurvy")
                .build();


        public static ItemLayout wandasScarf = new ItemLayout.ItemValueBuilder("4f4436bc-088d-49c0-b39d-2c5ab9a0e3fb")
                .region("item/WandasScarf")
                .name("Wanda's Scarf")
                .description("You prefer your own")
                .build();


        public static ItemLayout whiteGoggles = new ItemLayout.ItemValueBuilder("e3cf6cf0-5b22-4a57-be4e-f85a2e4b7fa7")
                .itemTypes(ItemType.ITEM)
                .region("item/WhiteGoggles")
                .name("White Goggles")
                .description("Accuracy+")
                .build();


        public static ItemLayout xisOldScarf = new ItemLayout.ItemValueBuilder("a8ed2705-4495-445c-b26e-d23831c9b0a5")
                .region("item/XisOldScarf")
                .name("Xi's Old Scarf")
                .description("Grants Crit Based on Your Current Armor")
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

        public static ItemLayout anger = new ItemLayout.ItemValueBuilder("9863a17e-81d8-11e7-bb31-be2e44b06b34")
                .itemTypes(com.bryjamin.wickedwizard.utils.enums.ItemType.BOSS, com.bryjamin.wickedwizard.utils.enums.ItemType.ITEM)
                .region("item/Anger")
                .name("Anger")
                .description("Damage++")
                .build();

        public static ItemLayout luckyShot = new ItemLayout.ItemValueBuilder("9863a7a0-81d8-11e7-bb31-be2e44b06b34")
                .region("item/LuckyShot")
                .name("Lucky Shot")
                .description("Damage+ Luck+")
                .build();

        public static ItemLayout miniCatapult = new ItemLayout.ItemValueBuilder("9863a944-81d8-11e7-bb31-be2e44b06b34")
                .region("item/MiniCatapult")
                .name("Mini Catapult")
                .description("Damage+ Range+")
                .build();

        public static ItemLayout miniTrebuchet = new ItemLayout.ItemValueBuilder("9863aa8e-81d8-11e7-bb31-be2e44b06b34")
                .region("item/MiniTrebuchet")
                .name("Mini Trebuchet")
                .description("The drawing that could")
                .build();

        public static ItemLayout smoulderingEmber = new ItemLayout.ItemValueBuilder("9863abce-81d8-11e7-bb31-be2e44b06b34")
                .region("item/SmoulderingEmber")
                .name("Smouldering Ember")
                .description("Damage+ FireRate+")
                .build();

        public static ItemLayout stability = new ItemLayout.ItemValueBuilder("9863ad04-81d8-11e7-bb31-be2e44b06b34")
                .region("item/Stability")
                .name("Stability")
                .description("Damage+ Accuracy+")
                .build();


        public static ItemLayout statueOfAjir = new ItemLayout.ItemValueBuilder("e45f39f3-4ea3-493d-b985-c02527b4167d")
                .itemTypes(com.bryjamin.wickedwizard.utils.enums.ItemType.BOSS)
                .region("item/StatueOfAjir")
                .name("Statue Of Ajir")
                .description("Too Lifelike")
                .build();

    }


    public static class FireRate {

        public static Item[] fireRateItems = {
                new ItemElasticity(),
                new com.bryjamin.wickedwizard.factories.items.passives.firerate.ItemMinorAccelerant(),
                new com.bryjamin.wickedwizard.factories.items.passives.firerate.ItemRunedFragment(),
                new ItemSwiftShot(),
                new com.bryjamin.wickedwizard.factories.items.passives.firerate.ItemStatueOfAdoj(),
                new ItemTacticalKnitwear()};

        public static ItemLayout statueOfAdoj = new ItemLayout.ItemValueBuilder("32ab3abc-81d9-11e7-bb31-be2e44b06b34")
                .region("item/StatueOfAdoj")
                .name("Statue Of Adoj")
                .description("So Lifelike")
                .build();


        public static ItemLayout elasticity = new ItemLayout.ItemValueBuilder("32ab3abc-81d9-11e7-bb31-be2e44b06b34")
                .region("item/Elasticity")
                .name("Elasticity")
                .description("FireRate+ Range+")
                .build();

        public static ItemLayout minorAccerlerant = new ItemLayout.ItemValueBuilder("32ab3d64-81d9-11e7-bb31-be2e44b06b34")
                .region("item/MinorAccelerant")
                .name("Minor Accelerant")
                .description("FireRate+ Speed+")
                .build();

        public static ItemLayout runedFragment = new ItemLayout.ItemValueBuilder("32ab3e5e-81d9-11e7-bb31-be2e44b06b34")
                .region("item/RunedFragment")
                .name("Runed Fragment")
                .description("FireRate+ Luck+")
                .build();


        public static ItemLayout swiftShot = new ItemLayout.ItemValueBuilder("32ab3f3a-81d9-11e7-bb31-be2e44b06b34")
                .region("item/SwiftShot")
                .name("Swift Shot")
                .description("FireRate++")
                .build();

        public static ItemLayout tacticalKnitwear = new ItemLayout.ItemValueBuilder("32ab4386-81d9-11e7-bb31-be2e44b06b34")
                .region("item/TacticalKnitwear")
                .name("Tactical Knitwear")
                .description("FireRate+ Accuracy+")
                .build();


    }


    public static class Health {

        public static Item[] healthItems = {
                new ItemDilation(),
                new ItemDisappointment(),
                new ItemHealingTonic(),
                new ItemHealingTonicPlus(),
                new ItemHealthUp(),
                new ItemHyperTrophy(),
                new ItemIronFragment(),
                new ItemPhisScarf(),
                new ItemPoisonedDrop(),
                new ItemSarcasticLion(),
                new ItemSecondWind(),
                new ItemSootheNote(),
                new Medicine()};


        public static ItemLayout dilation = new ItemLayout.ItemValueBuilder("13d4b00e-25f3-4ac6-9048-e9b395ee9fde")
                .itemTypes(ItemType.ITEM)
                .region("item/Dilation")
                .name("Dilation")
                .description("Woah")
                .build();

        public static ItemLayout disappointment = new ItemLayout.ItemValueBuilder("edbd9f26-1b15-4f8d-a15d-f620066e0154")
                .itemTypes(ItemType.ITEM, ItemType.BOSS)
                .region("item/Disappointment")
                .name("Disappointment")
                .description("...")
                .build();


        public static ItemLayout healingTonic = new ItemLayout.ItemValueBuilder("a03fc429-5bbd-41ff-846b-9c2040f40164")
                .itemTypes(ItemType.ITEM, ItemType.SHOP)
                .region("item/HealingTonic")
                .name("Healing Tonic")
                .description("Heals Minor Wounds and Increases Luck")
                .build();

        public static ItemLayout healingTonicPlus = new ItemLayout.ItemValueBuilder("2cb10f91-6c0f-46b1-a854-9ddbbd0151b3")
                .itemTypes(ItemType.ITEM, ItemType.SHOP)
                .region("item/HealingTonicPlus")
                .name("Healing Tonic Plus")
                .description("Heals Major Wounds and Increases Luck")
                .build();



        public static ItemLayout healthUp = new ItemLayout.ItemValueBuilder("2239cd0d-fad6-449e-848b-f6eaec7e3349")
                .itemTypes(ItemType.ITEM, ItemType.BOSS)
                .region("item/HealthUp")
                .name("Health Up!")
                .description("Healthed Up!")
                .build();


        public static ItemLayout hypertrophy = new ItemLayout.ItemValueBuilder("5085bada-81d9-11e7-bb31-be2e44b06b34")
                .itemTypes(ItemType.ITEM, ItemType.BOSS)
                .region("item/Hypertrophy")
                .name("Hypertrophy")
                .description("Health+ Damage+ Speed-")
                .build();

        public static ItemLayout ironFragment = new ItemLayout.ItemValueBuilder("5085bdd2-81d9-11e7-bb31-be2e44b06b34")
                .itemTypes(ItemType.ITEM, ItemType.BOSS)
                .region("item/IronFragment")
                .name("Iron Fragment")
                .description("Health+ Armor+")
                .build();


        public static ItemLayout phisScarf = new ItemLayout.ItemValueBuilder("68e06af7-fc92-412b-943d-fd767488ad94")
                .itemTypes(ItemType.ITEM, ItemType.BOSS)
                .region("item/PhisScarf")
                .name("Phi's Scarf")
                .description("It's Comforting")
                .build();

        public static ItemLayout poisonedDrop = new ItemLayout.ItemValueBuilder("de3b5e62-4c7b-4841-b506-93a51bf9a087")
                .itemTypes(ItemType.ITEM, ItemType.BOSS)
                .region("item/PurpleHeart")
                .name("Poisoned Drop")
                .description("Why Did You Drink That?!")
                .build();

        public static ItemLayout sarcasticLion = new ItemLayout.ItemValueBuilder("5085becc-81d9-11e7-bb31-be2e44b06b34")
                .itemTypes(ItemType.ITEM, ItemType.BOSS)
                .region("item/SarcasticLion")
                .name("Sarcastic Lion")
                .description("Rawr")
                .build();

        public static ItemLayout secondWind = new ItemLayout.ItemValueBuilder("5abcf470-9e35-4ddd-9b8f-ca63232876fb")
                .itemTypes(ItemType.ITEM, ItemType.BOSS)
                .region("item/SecondWind")
                .name("Second Wind")
                .description("You Feel Invigorated")
                .build();


        public static ItemLayout sootheNote = new ItemLayout.ItemValueBuilder("5085bf9e-81d9-11e7-bb31-be2e44b06b34")
                .itemTypes(ItemType.ITEM, ItemType.BOSS)
                .region("item/SootheNote")
                .name("Soothe Note")
                .description("Soothing")
                .build();

        public static ItemLayout medicine = new ItemLayout.ItemValueBuilder("5085c066-81d9-11e7-bb31-be2e44b06b34")
                .itemTypes(ItemType.ITEM, ItemType.BOSS)
                .region("item/Medicine")
                .name("Medicine")
                .description("Health+")
                .build();




    }


    public static class Luck {

        public static Item[] luckItems = {
                new ItemEnigmaticShot(),
                new ItemEquivalentExchange(),
                new ItemEyesOfAmalgama(),
                new ItemForgottenFigment(),
                new ItemGoldenFigment(),
                new ItemIWishYouWell(),
                new ItemJadeFigment(),
                new ItemLuckPlus10(),
                new ItemLuckyRoll(),
                new ItemPatternedOpal(),
                new ItemTesseract(),
                new ItemThreeDimensionalGold(),
                new ItemThreeLeafClover()};

        public static ItemLayout enigmaticShot = new ItemLayout.ItemValueBuilder("07d26ef6-418a-4c0f-89c0-ec0c45e1647a")
                .region("item/EnigmaticShot")
                .name("Enigmatic Shot")
                .description("Random Stat Manipulation")
                .build();


        public static ItemLayout equivalentExchange = new ItemLayout.ItemValueBuilder("82b66590-e0d2-45ed-9cfb-9d8d9a75c5df")
                .region("item/EquivalentExchange")
                .name("Equivalent Exchange")
                .description("Swaps Highest and Lowest Stat")
                .build();

        public static ItemLayout eyesOfAmalgama = new ItemLayout.ItemValueBuilder("de5c3cf1-d510-4cb1-b4ce-63c99ab0832d")
                .region("item/EyesOfAmalgama")
                .name("Eyes Of Amalgama")
                .description("Random All Stat increase")
                .build();


        public static ItemLayout forgottenFigment = new ItemLayout.ItemValueBuilder("6b37cd6e-81d9-11e7-bb31-be2e44b06b34")
                .region("item/ForgottenScarab")
                .name("Forgotten Figment")
                .description("Something feels off...")
                .build();

        public static ItemLayout goldenFigment = new ItemLayout.ItemValueBuilder("6b37d07a-81d9-11e7-bb31-be2e44b06b34")
                .region("item/GoldenScarab")
                .name("Golden Figment")
                .description("Luck+++")
                .build();


        public static ItemLayout iWishYouWell = new ItemLayout.ItemValueBuilder("818c03ae-3c90-46f7-9d9a-5bd110b93648")
                .region("item/IWishYouWell")
                .name("I Wish You Well")
                .description("I Sincerely do")
                .build();


        public static ItemLayout jadeFigment = new ItemLayout.ItemValueBuilder("6b37d17e-81d9-11e7-bb31-be2e44b06b34")
                .region("item/JadeScarab")
                .name("Jade Figment")
                .description("Luck++")
                .build();

        public static ItemLayout luckyRoll = new ItemLayout.ItemValueBuilder("e74d2389-7a50-48e3-9e12-ac74cac1ac44")
                .itemTypes(ItemType.ITEM, ItemType.SHOP)
                .region("item/LuckyRoll")
                .name("Lucky Roll")
                .description("Uses Luck to Increase a Random Stat")
                .build();

        public static ItemLayout luckPlus10 = new ItemLayout.ItemValueBuilder("badfeb55-453e-4d97-a1d2-f23288ffdc34")
                .itemTypes(ItemType.ITEM)
                .region("item/LuckPlusTen")
                .name("Luck +10")
                .description("Luck +(5 x 2)")
                .build();


        public static ItemLayout patternedOpal = new ItemLayout.ItemValueBuilder("642722ee-18f8-4b34-bf95-a6c21243af63")
                .itemTypes(ItemType.ITEM)
                .region("item/PatternedOpal")
                .name("Patterened Opal")
                .description("Luck+")
                .build();

        public static ItemLayout tesseract = new ItemLayout.ItemValueBuilder("202e9080-484c-4693-b408-8661ed08b54d")
                .itemTypes(ItemType.ITEM, ItemType.SHOP)
                .region("item/Tesseract")
                .name("TESSeract")
                .description("Get it? :D")
                .build();

        public static ItemLayout threeLeafClover = new ItemLayout.ItemValueBuilder("6b37d250-81d9-11e7-bb31-be2e44b06b34")
                .region("item/ThreeLeafClover")
                .name("Three Leaf Clover")
                .description("Close Enough... Luck+")
                .build();


        public static ItemLayout threeDimensionalGold = new ItemLayout.ItemValueBuilder("36712291-d64c-4ea2-bea7-de3e8395d463")
                .region("item/ThreeDimensionalGold")
                .itemTypes(com.bryjamin.wickedwizard.utils.enums.ItemType.ITEM)
                .name("3-Dimensional Gold")
                .description("Woah")
                .build();


    }


    public static class Range {

        public static Item[] rangeItems = {
                new ItemBuckShot(),
                new ItemClearSight(),
                new ItemFireSight(),
                new ItemFocusShot(),
                new ItemGoldenScope(),
                new ItemLaserScope(),
                new ItemLeahsScarf(),
                new ItemLostLettersRangeFireRate(),
                new ItemNeatCube(),
                new ItemQuadonometry(),
                new ItemScope()};

        public static ItemLayout buckShot = new ItemLayout.ItemValueBuilder("3491ee46-bf2e-40ef-a8a0-836a5583bdb7")
                .region("item/BuckShot")
                .name("Buckshot")
                .description("Less Range, More Power")
                .build();


        public static ItemLayout clearSight = new ItemLayout.ItemValueBuilder("8074c97a-81d9-11e7-bb31-be2e44b06b34")
                .region("item/ClearSight")
                .name("Clear Sight")
                .description("Range+ Accuracy+")
                .build();

        public static ItemLayout focusShot = new ItemLayout.ItemValueBuilder("1731028e-86e4-4907-b656-847d793579f2")
                .region("item/FocusShot")
                .name("Focus Shot")
                .description("Range+++ FireRate--")
                .build();


        public static ItemLayout fireSight = new ItemLayout.ItemValueBuilder("8074cf92-81d9-11e7-bb31-be2e44b06b34")
                .region("item/FireSight")
                .name("Fire Sight")
                .description("Range+ Damage+")
                .build();


        public static ItemLayout goldenScope = new ItemLayout.ItemValueBuilder("7f440d23-ecb0-451a-88d1-1694a2473afc")
                .region("item/GoldenScope")
                .name("Golden Scope")
                .description("Range+ Luck+")
                .build();


        public static ItemLayout laserScope = new ItemLayout.ItemValueBuilder("8074d1ae-81d9-11e7-bb31-be2e44b06b34")
                .region("item/LaserScope")
                .name("Laser Scope")
                .description("Range+++")
                .build();

        public static ItemLayout leahsScarf = new ItemLayout.ItemValueBuilder("882d4670-ad95-4cf1-ac50-8f32345dc08b")
                .region("item/LeahsScarf")
                .name("Leah's Scarf")
                .description("The First")
                .build();

        public static ItemLayout lostLettersRangeFireRate = new ItemLayout.ItemValueBuilder("8074d488-81d9-11e7-bb31-be2e44b06b34")
                .region("item/LostLettersRangeFireRate")
                .name("Lost Letters")
                .description("R and F?")
                .build();

        public static ItemLayout neatCube = new ItemLayout.ItemValueBuilder("8074d65e-81d9-11e7-bb31-be2e44b06b34")
                .region("item/NeatCube")
                .name("Neat Cube")
                .description("Pretty Neat")
                .build();

        public static ItemLayout quadonometry = new ItemLayout.ItemValueBuilder("8074d816-81d9-11e7-bb31-be2e44b06b34")
                .region("item/Quadonometry")
                .name("Quadonometry")
                .description("Range+ Accuracy+")
                .build();

        public static ItemLayout scope = new ItemLayout.ItemValueBuilder("8074db54-81d9-11e7-bb31-be2e44b06b34")
                .region("item/Scope")
                .name("Scope")
                .description("Range++")
                .build();

    }




    public static class ShotSize {


        public static Item[] shotSizeItems = {
                new ItemBigShot(),
                new ItemCannonCube(),
                new ItemLeafCutter(),
                new ItemMiniShot(),
                new ItemPodShot(),
                new ItemWideLens()

        };


        public static ItemLayout bigShot = new ItemLayout.ItemValueBuilder("af47fff1-c63d-468d-844e-b6f2634753b8")
                .region("item/BigShot")
                .name("Big Shot")
                .description("Bigger Shots")
                .build();


        public static ItemLayout cannonCube = new ItemLayout.ItemValueBuilder("275fcafa-661a-4a5e-a50a-14217a5ef7be")
                .region("item/CannonCube")
                .name("CannonCube")
                .description("More destructive, Less Aerodynamic")
                .build();


        public static ItemLayout leafCutter = new ItemLayout.ItemValueBuilder("af0bc30b-e11e-49dc-a82f-fe1762c7d770")
                .region("item/LeafCutter")
                .name("Leaf Cutter")
                .description("Shots like the breeze")
                .build();



        public static ItemLayout miniShot = new ItemLayout.ItemValueBuilder("0b133834-e2db-47a9-8b2b-95fbc51fa683")
                .region("item/MiniShot")
                .name("Mini Shot")
                .description("Smaller Shots")
                .build();


        public static ItemLayout podShot = new ItemLayout.ItemValueBuilder("a2ad4edd-4121-406d-ab38-41f65fd1d02d")
                .region("item/PodShot")
                .name("Pod Shot")
                .itemTypes(ItemType.ITEM)
                .description("Like Peas In A Shot")
                .build();


        public static ItemLayout widelens = new ItemLayout.ItemValueBuilder("f76c7dba-f42d-4680-af2c-0dfb4657db31")
                .region("item/WideLens")
                .name("Wide Lens")
                .description("Accuracy+")
                .build();
    }



    public static class ShotSpeed {

        public static Item[] shotSpeedItems = {
                new ItemBlueEyeGems(),
                new ItemBoringRock(),
                new ItemBubble(),
                new ItemDullFeather(),
                new ItemLostLettersShotSpeedAccuracy(),
                new ItemMomentum(),
                new ItemShinyFeather()};


        public static ItemLayout blueEyeGems = new ItemLayout.ItemValueBuilder("1fba9964-bb31-4cfb-8201-56ccb206389b")
                .region("item/BlueEyeGems")
                .name("Blue-Eye Gems")
                .description("Note: Are Not Blue Eye-Gems")
                .build();

        public static ItemLayout boringRock = new ItemLayout.ItemValueBuilder("ad70fbce-81d9-11e7-bb31-be2e44b06b34")
                .region("item/BoringRock")
                .name("Boring Rock")
                .description("Pretty Boring...")
                .build();

        public static ItemLayout bubble = new ItemLayout.ItemValueBuilder("ad70fe44-81d9-11e7-bb31-be2e44b06b34")
                .region("item/Bubble")
                .name("Bubble")
                .description("ShotSpeed++")
                .build();

        public static ItemLayout dullFeather = new ItemLayout.ItemValueBuilder("ad70ff34-81d9-11e7-bb31-be2e44b06b34")
                .region("item/DullFeather")
                .name("Dull Feather")
                .description("ShotSpeed+ Speed+")
                .build();

        public static ItemLayout lostLettersShotSpeedAccuracy = new ItemLayout.ItemValueBuilder("ad710074-81d9-11e7-bb31-be2e44b06b34")
                .region("item/LostLettersShotSpeedAccuracy")
                .name("Lost Letters")
                .description("S and A?")
                .build();

        public static ItemLayout momentum = new ItemLayout.ItemValueBuilder("ad7101fa-81d9-11e7-bb31-be2e44b06b34")
                .region("item/Momentum")
                .name("Momentum")
                .description("ShotSpeed++ Speed+ Damage+")
                .build();

        public static ItemLayout personalBubble = new ItemLayout.ItemValueBuilder("5d26b473-36d5-455d-b928-2f28f3ccdfa6")
                .region("item/PersonalBubble")
                .name("Personal Bubble")
                .description("ShotSpeed++")
                .build();

        public static ItemLayout shinyFeather = new ItemLayout.ItemValueBuilder("ad710358-81d9-11e7-bb31-be2e44b06b34")
                .region("item/ShinyFeather")
                .name("Shiny Feather")
                .description("ShotSpeed++ Speed++")
                .build();


    }


    public static class Speed {

        public static Item[] speedItems = {
                new ItemAbstractLeaf(),
                new ItemHotStep(),
                new ItemQuickness(),
                new ItemQuickStep()
        };

        public static ItemLayout abstractLeaf = new ItemLayout.ItemValueBuilder("394d3ba7-7467-41f8-a68f-00179c95c2ba")
                .itemTypes(ItemType.SHOP, ItemType.ITEM)
                .region("item/AbstractLeaf")
                .name("Abstract Leaf")
                .description("Speed++")
                .build();


        public static ItemLayout hotStep = new ItemLayout.ItemValueBuilder("b7368b6c-96e4-4de1-8f41-bd7a6a36dc3d")
                .itemTypes(ItemType.SHOP, ItemType.ITEM)
                .region("item/HotStep")
                .name("Hot Step")
                .description("Speed+ Damage+")
                .build();


        public static ItemLayout quickness = new ItemLayout.ItemValueBuilder("ce6f7cce-81d9-11e7-bb31-be2e44b06b34").region("item/Quickness")
                .name("Quickness")
                .description("Speed+++")
                .build();


        public static ItemLayout quickStep = new ItemLayout.ItemValueBuilder("80d809ef-f961-4f1c-9751-a97877d911c7")
                .itemTypes(ItemType.SHOP, ItemType.ITEM)
                .region("item/QuickStep")
                .name("Quick Step")
                .description("Speed++")
                .build();

    }


    public static class Companion {

        public static Item[] companionItems = {
                new ItemAutoRockets(),
                new ItemCrownOfBiggaBlobba(),
                new ItemDangerDetector(),
                new ItemGhastlyWail(),
                new ItemLiasCrown(),
                new ItemMegaSideCannons(),
                new ItemMiniSpinnyThingie(),
                new ItemMyVeryOwnStalker(),
                new ItemSideCannons(),
                new ItemTesserWreck(),
                new ItemXisGlare()
        };


        public static ItemLayout autoRockets =  new ItemLayout.ItemValueBuilder("2c468520-0b04-4e5d-b269-e4ce72e6dfd4")
                .region("item/companion/AutoRockets")
                .name("Auto Rockets")
                .description("Occasionally Fire Rockets at Enemies")
                .build();

        public static ItemLayout crownOfBiggaBlobba =  new ItemLayout.ItemValueBuilder("8a6a0e43-10fc-4939-880e-5e69b48173f8")
                .region("item/companion/CrownOfBiggaBlobba")
                .name("Crown Of BiggaBlobba")
                .description("Fits Nicely")
                .build();

        public static ItemLayout dangerDetector =  new ItemLayout.ItemValueBuilder("f151f958-cedf-47ce-96c1-2271ac417859")
                .region("item/companion/DangerDetector")
                .name("Danger Detector")
                .description("Detects Danger apparently")
                .build();

        public static ItemLayout ghastlyWail =  new ItemLayout.ItemValueBuilder("74fdf448-3c4d-4a0c-a32b-3fed6f19821c")
                .region("item/companion/GhastlyWail")
                .name("Ghastly Wail")
                .description("Screams Externally")
                .build();


        public static ItemLayout liasCrown =  new ItemLayout.ItemValueBuilder("b032283b-c2b7-40d4-a6a4-243b0ba1b421")
                .itemTypes(ItemType.ITEM, ItemType.BOSS)
                .region("item/companion/LiasCrown")
                .name("Lia's Crown")
                .description("Congratulations")
                .build();

        public static ItemLayout miniSpinnyThingie =  new ItemLayout.ItemValueBuilder("2200b179-33ee-4cd9-9853-515708bceda8")
                .region(TextureStrings.KUGELDUSCHE_LASER)
                .name("Mini Spinny Thingie")
                .description("A thingie that is mini and spinnys")
                .build();


        public static ItemLayout myVeryOwnStalker =  new ItemLayout.ItemValueBuilder("05324bec-c14d-403e-b034-df7536d10d2c")
                .region("item/companion/MyVeryOwnStalker")
                .name("Your Very Own Stalker")
                .description("A chill runs down your square")
                .build();


        public static ItemLayout sideCannons =  new ItemLayout.ItemValueBuilder("8cb06824-df80-486b-9c3e-eb3b4d03f8f7")
                .region("item/companion/SideCannons")
                .name("Side Cannons")
                .description("\"Cannons\"")
                .build();


        public static ItemLayout megaSideCannons =  new ItemLayout.ItemValueBuilder("57ab14c9-3335-467b-86f8-c944a04494f3")
                .region("item/companion/MegaSideCannons")
                .name("Mega Side Cannons")
                .description("\"Mega Cannons\"")
                .build();

        public static ItemLayout tesserWreck =  new ItemLayout.ItemValueBuilder("d0ed0581-e21b-4a46-98c1-ff019c816240")
                .itemTypes(ItemType.ITEM)
                .region("item/companion/TesserWreck")
                .name("TesserWRECK")
                .description("This Was A Mistake")
                .build();

        public static ItemLayout xisGlare =  new ItemLayout.ItemValueBuilder("511ebb70-4a7a-4420-9b98-9f534fc2f1de")
                .itemTypes(ItemType.ITEM, ItemType.BOSS)
                .region("item/companion/XisGlare")
                .name("Xi's Glare")
                .description("Piercing")
                .build();

    }


    public static class Conditionals {

        public static Item[] conditionalItems = {
                new ItemLabyrinthCodex(),
        };


        public static ItemLayout labyrinthCodex =  new ItemLayout.ItemValueBuilder("cd088912-2721-4a53-bf02-8afdc7514a6a")
                .itemTypes(ItemType.ITEM, ItemType.SHOP)
                .region("item/LabyrinthCodex")
                .name("Labyrinth Codex")
                .description("Reveals Areas Of The Map")
                .build();

    }


    public static class PickUp {

        public static ItemLayout armorUp = new ItemLayout.ItemValueBuilder("d21f630c-81d9-11e7-bb31-be2e44b06b34").region("item/armor")
                .name("Armor Up")
                .description("Armor+")
                .build();

        public static ItemLayout healthUp = new ItemLayout.ItemValueBuilder("d21f66f4-81d9-11e7-bb31-be2e44b06b34")
                .region("item/heart", 1)
                .name("Health Up")
                .description("Health+")
                .build();

        public static ItemLayout fullHealthUp = new ItemLayout.ItemValueBuilder("d21f68c0-81d9-11e7-bb31-be2e44b06b34")
                .region("item/heart", 0)
                .name("Health Up")
                .description("Health++")
                .build();



        public static ItemLayout moneyUp = new ItemLayout.ItemValueBuilder("d21f6f82-81d9-11e7-bb31-be2e44b06b34")
                .region("gold")
                .name("Money Up")
                .description("Money+")
                .build();

    }









}
