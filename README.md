# WickedWizard
Let's see how far I get in 8 months using an hour per day

# Personal Issue Tracker

## Priority 

### Current Issues

- Create a 'god mode' and 'bounds mode' menu option. 
- Change animation so it is easier to queue animations such as the firing animation. 

- Change turret to have animation

- Change tutorial to have more 'forced sections' (teach double jump)
-Create a menu Screen and when you start game if you have no preferences of tutorial make one. 
- Vertical wall bug. You can land on the side of top of wall vertically stacking with one another,
due to the way the future bound is used. 
-Create TextureLoader Parameter. For usages in tests 
-Add in a Time Limit of sorts where we die if you run out of time. *probably won't*
-Improve message banner to have an offset so you can change where it shows up. 
- Camera shake on explosion

###Bugs
- You can trigger shooting and moving the have the same pointer due to platforms. You double tap
to go below and then hold to move it counts as the same pointer and you shoot and move
-Collisions should stop a grapple or you just travel for ever. 
-Message for Solitary level is positioned incorrectly
-General clean up and maintenance, seperating of the RoomTransitionSystem into different classes
- Potential bug with room transition animation on wide rooms. Sliver of old room may be visible
- May be possible to fall through the world due to player input being unpaused when the enxt transition screen is going (hard to replicate)



### Backburner (Broad goals that need to be broken down once we reach them)

-Giant Factory? Similar to AssetManager, is generic and stores all Factories? 
- Biggablobba needs a phase where he goes underground and chases the player 
- Larger map on tap 
- Pause 
- Sound both effects and music 
- Repaint doors if they lead to a special room 
- Enemies that summon other enemies should be minions and should not drop look 
- New Enemies 
    - Has a block shield that needs to be destroyed 
    - A stamp from above (possibly in the foundary?) Red hot crusher plate 
    - Spike balls that bounce off walls 
    - Enemy that shoots out a cone of bullets 0 degrees 45 degrees and -45 degrees. 
    
- New Items 
    - Items generally need to toned down to give more minor increases to stats 
    - If you have an item it should be removed from the pool of items that can be generated 
    - Items that reveal the map and change your weapon 
    
- Time Limit to complete the game, if you run out of time you die. (Casual game mode if you do not want to do that)

- New Bosses 
    - Prince and Princess box for a face. Two enemies that when one dies the other tries to ressurect
    - Giant KugelDusche enemy that the player has to traverse of moving planks to hit. 
    
- New Rooms 
    - Room where 
    
- Explosives and Lasers
  

### Completed

-Platforms I have created the functionality and design of platform as a first place. You need to double tap to go below them.
-Message when you enter a new level**
- Due to the change in item a class may need to be created for Item. This way you can get the index,
of the texture region it uses when calling to the axis. ** Used a pair class to solve this **
-Black Room Transition to avoid jarring room transition **
-Add accelerant to camera so the movement isn't so jarring ** it is still jarring
-- Create a tutorial section for platforms 
-Add a fade to the item pickup entity. 
- Giblets. create an Ondeath that spawns out giblets when an enemies dies
- Minion component, (can't drop loot))
- Change chest to open only when near it
 --Chest need a loot table and so do stores and Item Rooms (but for that I may need more items** Need to make differnet item pools

