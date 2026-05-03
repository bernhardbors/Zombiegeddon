# INF112 - Zoombiegeddon
* Team: A-laget (Gruppe 4)
* Members: 
  * Isak Graarud - Team Lead
  * Bernhard Bors - Report Manager
  * Synne Berg Hermansen - Structural Manager
  * Tuva Kvamme - Test Manager
  * Theodor Flornes - Graphic Designer
  * Johannes Moe - Sound Manager
  
# Project resources
* Git repository:  [Project](https://git.app.uib.no/inf112/26v/proj/a-laget)
* Trello: [Link](https://trello.com/invite/b/697a6b2f6f393809f801d2e8/ATTIe6db3e071af79154e7d9739a6eda1ab8EE0DA4E4/inf112-a-laget)
* Meeting notes: [Docs](https://docs.google.com/document/d/1DTBr6Ew_FQDBkRHb4hHOdzjmSTYw7m4FUMoYYwyxgII/edit?usp=sharing)
* Reports: 
  * [Report 1](doc/report-1.md)
  * [Report 2](doc/report-2.md)
  * [Report 3](doc/report-3.md)
  * [Report 4](doc/report-4.md)


# About Zoombiegeddon: 
Zoombiegeddon is a 2D-game where the player 
fights zombie animals across different levels to
stop an evil professor’s experiment.

# Controls 
| Key                     | Action     |
|-------------------------|------------|
| A/Left                  | Move left  |
| D/Right                 | Move right |
| W/Up/Space              | Jump       |
| P                       | Pause      | 
| Esc                     | Quit       |
| Hold Shift while moving | Sprint     |



## Code Structure
* `inf112.zoombiegeddon.app` – game initialization
* `inf112.zoombiegeddon.model` – game logic 
* `inf112.zoombiegeddon.view` – rendering and screens
* `inf112.zoombiegeddon.controller` – handles user input
* `inf112.zoombiegeddon.audio` – sound and music handling
* `inf112.zoombiegeddon.events` – event handling system

	
    
## Credits
* https://gafferongames.com/post/fix_your_timestep/ used in the class WorldPhysics
* https://emhuo.itch.io/peaberry-pixel-font Font in the game
* https://kenney-assets.itch.io/pixel-platformer Tiles for the levels
* https://fartfish.itch.io/zombie-game-asset-pack Zombies and background 
* https://gandalfhardcore.itch.io/free-pixel-art-sidescroller-asset-pack-32x32-city Background
* https://reedster123.itch.io/pixel-heart-healthbar-sprite-sheet Health bar 
* https://xdeviruchi.itch.io/16-bit-fantasy-adventure-music-pack In-Game Music
* https://phoenix1291.itch.io/sound-effects-pack-2 Health, Speed Pick Up, and Game Over/Death Sound Effects
* https://the-sound-guild.itch.io/footsteps-sounds-02 footsteps and running sound
* https://noahkuehne.itch.io/free-menu-sound-pack-ui-sounds-v2 Button sounds for menu.
* https://jacob-creates.itch.io/game-sound-effects zombie death
* The icons for powerups has been drawn by Bernhard Bors
* The games cover photo and story mode video is AI generated

# How to run

### Requirements
* Requires Java 25 

### Build and run the game
* `mvn package`
* `java -jar target/zoombiegeddon.jar`

## Testing
* Run unit tests with `mvn test`


