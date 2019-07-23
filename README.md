# FallingRocksAndroidGame
Collision detection with use of quadtrees example

This application loads sprites onto the screen with different behaviours.

The player sprite is controlled with a joystick which sends callbacks to the playerclass to inform the direction of movement.
Tapping the screen fires projectiles towards the location of the screen tap.

The ghost sprite reads the player sprite location and moves towards the coordinates of the player sprite.

The rock sprites move downward in a random order and have random velocities.

The sprites are have unique collision behaviours, the rock sprite inform the player sprite of the downward velocity when falling
and the ghosts are removed on collision with the player or the player projectile.

Due to the high volume of sprites, a quad tree was implemented to split the screen into 4 rectangles every time a certain number of sprites
were clustered within the same rectangle bounds, the same rectangle is then split into a further 4 rectangles another time if the threhold 
of sprites within a single bound is reached.

Collision detection is only applied to sprites within the same rectangle bound within the screen for improved optimisation.
