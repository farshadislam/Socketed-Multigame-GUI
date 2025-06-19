### seng300-Project, P- 17 

# The OMG platform

### *Instructions:*
- To start the games and play against the bots just open [this file](src/main/java/org/seng/gui/HelloApplication.java),
- run it and either create your own account or use some of the [existing ones](./output.txt),
- once there select whatever game you want and play it, be it against a bot or a player near you, 
- If you are creating an account make sure to have at least 5 letters for username, and if you run into any issues there is always a help button,
- For playing against a player online one first has to start the [server](src/main/java/org/seng/networking/SocketMatchServer), after that
- Known bugs:
  - when playing the checkers black pieces can be selected by clicking at ***the edges*** of the tiles and not the center, otherwise everything works fine there
  - 
### Game Objectives:
  - *Checkers*:
    - to win one must best the opponent by taking every single piece from them, you can move only on black tiles, to take a piece one must have an empty tile behind it
    - one could also receive a king piece by moving one of the pieces to the end of the board (opposite side), king can move both forward and back while the rest of the pieces just move forward (diagonally)
  - *Connect4*:
    - to play the game one just has to click on the column where they want to add their piece/ball
    - to win one has to connect 4 balls in the same line, be it diagonally, horizontally or vertically 
    - keep an eye out for your opponent as well, especially if it is a bot since it can be unpredictable
  - *TicTacToe*:
    - to play the game one only has to click on the tile where they want to add their symbol (X/O)
    - if you are playing as X connect 3 X's in any direction and vice versa if you are playing as O 
### Overview

- This is a multiplayer platform on which one can play 3 games:
  - TicTacToe
  - Checkers
  - Connect4

- This platform has many features such as queuing for games against people, people of similar skill, and bots.
- They can play against their friends or random opponents both online and on the same machine,
- They can look through the previous games one played, see the leaderboards, their ranks, places... 

- The platform and its games have many interactive features such as:
  - ready check screens (both players are required to press ready to start the game, not needed against bots),
  - player could open the help page at any time,
  -  player can open the leaderboard page and see their own stats or profile (wins/loses),
  -  user can create an account or login/logout with an existing one,
  -  there is also and option for resetting password in case that it was forgotten,
  -  then there is an option to manage the account (change username, password, email)
  -  player could search for their friend on this platform, and if they exist in the system under that said username one could add them as a friend,
  -  there is a back button, and prompt which asks the user if they want to quit in case it was a misclick,
