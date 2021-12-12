# Brick_Destroy

This is a simple arcade video game.
You will have a rectangular bar as a player.
It will bounce a ball to hit and break the bricks.
Your aim is to destroy all the bricks.
The game has very simple commands
<br><table border='1'><tr><th>SPACE</th><td>start/pause the game</td></tr>
<tr><th>A</th><td>move the player right</td></tr>
<tr><th>D</th><td>move the player right</td></tr>
<tr><th>ESC</th><td>enter/exit pause menu</td></tr></table>
Note that different type of bricks have different characteristics.
The game automatically pause if the frame loses focus.


Changes done:
1. group classes into 3 packages:
   1. game ui : user interface and graphics stuff 
   2. game element: element and objects of the game content 
   3. cheat: console that is used to debug or cheat
2. Main class is the entry point of the game, while GameLogic class works with all packages, so they are not grouped into any package.
3. Features added:
   1. new theme and appearance
   2. properly center in-game status message
   3. show available key bindings in pause menu
   4. add highscore view, pop out whenever game over or victory, or clicked button in home menu
   5. add info page in home menu
   6. add picture about brick breaker in home menu
4. Fixes done:
   1. prevent overbound error when using skip level in cheat tool
   2. make sure to reset game at game over, also update ball in in-game status message the moment lose 1 ball, not after pressing space

