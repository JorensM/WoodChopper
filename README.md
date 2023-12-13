# WoodChopper
 A Spigot plugin that makes zombie wood choppers

 [Demo](https://www.youtube.com/watch?v=UTMIA0GE2Fo)

**Time taken to complete the test**

5 hours

**Challenges or issues**

I wasn't able to complete the task completely due to the limitations of the Spigot API. 
I'm sure these limitations could've been overcome though, if I had been given more time.
One thing in particular that was challenging was the fact that Spigot API doesn't have any pathfinding features,
so I had to do a hack where I spawned an invisible mob that the wood chopper would follow, in order to control
the wood choppers movement.

I wasn't able to implement the chopper chopping down a tree completely, due to time limitations.

**Overview**

I implemented a bit/chest structure, upon building which you spawn a "Wood Chopper" - a zombie that looks for wood and chops it.
Depending on what type of bit you used, the chopper will have different properties

 * Diamond - fastest chopping cooldown and no particles
 * Emerald - medium chopping cooldown and purple particles
 * Gold - slowest chopping speed but possibility to yield 1-4 blocks per chop, and green particles
 
Additionally, when touching a mushroom, the chopper will despawn.
