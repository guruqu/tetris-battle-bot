**TBot** is a bot for tetris games on [tetrisfriends.com](http://www.tetrisfriends.com) and Tetris Battle on Facebook.

This bot uses java.awt.Robot class to "visualize" the board by identifying piece colors and detect the current piece. It selects the between the current piece and the piece on hold and which position and orientation will generate the best outcome of the tetris board. Additionally, the bot will go through buildup and breakdown phases which greatly increases the chances to score.

The algorithm used is mainly based on [El-Tetris](http://ielashi.com/el-tetris-an-improvement-on-pierre-dellacheries-algorithm/) but implemented in java.

### TBot v1.3 [DOWNLOAD](https://drive.google.com/folderview?id=0BwaWDMD7MRkxRXJOc3MyYzF0U2M&usp=sharing) ###
Modified the piece detection to use color nearness algorithm instead of exact RGB values to cater to other types of games in [tetrisfriends.com](http://www.tetrisfriends.com)

Marathon Mode: Reduce the number of buildup limit and breakdown limit ex: 5/0

### Bot in Action ###

[http://vimeo.com/61396163](http://vimeo.com/61396163) - this is a previous version.


### Instructions ###

  1. Download and install [java](http://www.java.com/en/download/index.jsp) if you haven't it yet.
  1. Download the zip file from [here](https://drive.google.com/folderview?id=0BwaWDMD7MRkxRXJOc3MyYzF0U2M&usp=sharing). Then, unzip it anywhere.
  1. Double click on TBot.jar to start the application.
  1. Restore the browser window of Tetris Battle. **It is important that the browser and the application don't share the same screen space**. <br /><img width='500' height='200' src='http://tetris-battle-bot.googlecode.com/svn/trunk/TBot2.0/screen%20space.PNG'></img>
  1. Just press the "Find Board" button and adjust the transparent window that will popout to the edges of the tetris board. **Closing the transparent window** will store the board location so do not move the tetris board after. <br /><img src='http://tetris-battle-bot.googlecode.com/svn/trunk/TBot2.0/find%20board.PNG' height='200'></img>
  1. Press the "Start" button on the game and just maintain window focus on the browser and that's it. It will automatically detect the start of the game.


### Options ###

Key Delay - This is the time delay before the key/move is sent to the focused window.
This number should be balanced. If the number is very low, the bot will go crazy. If the number is very high, the bot is slow.

Tower Gap - The width of the gap on the right that the bot reserves on the Buildup phase.

Buildup Limit - The height of the board that will trigger the bot to go into the Breakdown phase and decrease its height.

Breakdown Limit - The height of the board that will trigger the bot to go into the Buildup phase and increase its height while maintaining the tower gap.


### TODO ###

  1. 1-piece lookahead
  1. better board awareness


### Disclaimer ###

This will not make you rank 100 in Tetris Battle.
Skilled humans can beat this bot.
At least, in v1.0. We don't know in v2.0. =)