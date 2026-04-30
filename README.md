# INF112 - Zoombiegeddon
* Team: A-laget
* Members: 
  * Isak Graarud
  * Bernhard Bors
  * Synne Berg Hermansen
  * Tuva Kvamme 
  * Theodor Flornes
  * Johannes Moe
  
# Project resources
* Git repository:  [Project](https://git.app.uib.no/inf112/26v/proj/a-laget)
* Trello: [Link](https://trello.com/invite/b/697a6b2f6f393809f801d2e8/ATTIe6db3e071af79154e7d9739a6eda1ab8EE0DA4E4/inf112-a-laget)
* Meeting notes: [Docs](https://docs.google.com/document/d/1DTBr6Ew_FQDBkRHb4hHOdzjmSTYw7m4FUMoYYwyxgII/edit?usp=sharing)

# About Zoombiegeddon: 
Zoombiegeddon is a 2D-game where the player 
fights zombie animals across different levels to
stop an evil professor’s experiment.


## Code Structure
* `inf112.zoombiegeddon.app` – game initialization
* `inf112.zoombiegeddon.model` – game logic 
* `inf112.zoombiegeddon.view` – rendering and screens
* `inf112.zoombiegeddon.controller` – handles user input
* `inf112.zoombiegeddon.audio` – sound and music handling
* `inf112.zoombiegeddon.events` – event handling system

	
    
## Credits
* https://gafferongames.com/post/fix_your_timestep/ used in the class WorldPhysics
* https://libgdxinfo.wordpress.com/basic-label/ used in FontGenerator 
* https://pixbob.itch.io/pixbob-mini-lite Font 
* https://kenney-assets.itch.io/pixel-platformer Tiles
* https://fartfish.itch.io/zombie-game-asset-pack Zombies 
* The games cover photo and story mode video is AI generated

# How to run

### Requirements
* Requires Java 25 

### Run the game
* Run the Jar file with `java -jar target/zoombiegeddon.jar`

### Build and run with Maven 
* `mvn package`
* `java -jar target/zoombiegeddon.jar`

## Testing
* Run unit tests with `mvn test`


