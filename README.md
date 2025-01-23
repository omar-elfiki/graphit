Graph IT! An Interactive Graph Coloring Game

Graph coloring refers to the problem of assigning colors to vertices in a graph in such a way that one uses the least amount of colors possible so that no two adjacent vertices share the same color. The minimum amount of colors that can be used to color a graph in such a way is the chromatic number for the corresponding graph.

In this phase, a player gets provided with a game in which they will get the option to play 3 different editions of games based on graph coloring problems, making it an easy and interactive way to learn about the graph coloring problem.

## These are the 3 possible game modes that a player can choose:

### 1. To the Bitter End: 
The player simply has to colour the graph as quickly as possible.
### 2. Random Order:
Here the computer generates a random ordering of the vertices and the player has to pick the colours of the vertices in exactly that order. Once the colour of a vertex has been chosen, it cannot be changed again. (Note that the player can never get stuck, because they are always allowed to introduce a new colour.) The goal for the player is to use as few colours as possible.
### 3. I Changed My Mind: 
The same setup as “Random order”, but the user can undo colorings. Colorings may only be undone in the order in which they were made, but they can be undone any number of times. For example, if the user has colored vertices 1, 2, 3, and 4, they can undo the coloring for just vertex 4, vertices 4 and 3, vertices, 4, 3, and 2, or all four. The goal for the player is to use as few colours as possible.

## How to run the program
The version of our program has been built using the JavaFX GUI architecture, which required a bit of tinkering to make working.

To run the program, your device will require the following files in order to run the program:
1. JDK version 22+
2. JavaFX, tested with version 23 (https://gluonhq.com/products/javafx/)

In order to actually run the program, the following steps are recommended in order to get it to run on your device:
### For IntelliJ (best option)
1. Find the path to the folder with the JavaFX libary (We placed the folder in C:/, for easiness during the development)
2. Add to the configuration of the startMenu file the following VM Options command: 
```json
--module-path <fx-file-path>/lib --add-modules javafx.controls,javafx.fxml
```
3. Run the startMenu file in order to get to the start screen
4. Choose the desired gamemode, then press Start.
5. Enjoy!

### For Virtual Studio Code:
1. Find the path to the folder with the JavaFX files (We placed the folder in C:/, for easiness during the development)
2. Open the ```<launch.json>``` file and add the following configuration:
```json
{
    "type": "java",
    "name": "Launch Game",
    "request": "launch",
    "mainClass": "startMenu",
    "vmArgs": "--module-path <fx-file-path>/lib --add-modules javafx.controls,javafx.fxml"
}
```
3. Replace the existing filepath with the path to the JavaFX files
4. Run the configuration and enjoy the game!



## How to play the game:
### Graph uploading
In any version of the game, the player has 2 different methods of adding graphs into the play area:
1. Give the number of vertices and edges, then generate a random configuration to play with, by pressing Generate.
2. Upload a file with a graph configuration saved on the device, by pressing Upload and choosing a text file. A menu will open and the user can choose files like ```<input_file_name>``` 

### Functionalities of the game screen:
After uploading a graph, the player needs to color the graph correctly as fast as possible.
These are the following functionalities:
1. Color List (All game modes. Player chooses a color to color a vertex. If needed, player can add more colors with the Add Color button)
2. Clear button (All game modes. Clears the graph, to play with a different graph)
3. Undo button (Only To the bitter end and I changed my mind. Undoes the coloring of a vertex)
4. Hint button (All game modes. Gives a hint to what the player should do next)
5. Timer (All game modes. Used to track how fast the player completes the graph)
6. Pause button (All game modes. Pauses the game and time)
7. Give up button (All game modes. Makes the player lose and solves the game)

## Advanced Viewing Mode:
### For IntelliJ (best option)
1. Find the path to the folder with the JavaFX libary (We placed the folder in C:/, for easiness during the development)
2. Add to the configuration of the ExtendedUIP3 file the following VM Options command:
```json
--module-path <fx-file-path>/lib --add-modules javafx.controls,javafx.fxml
```
3. Run the configuration
4. Enjoy!

### For Virtual Studio Code:
1. Find the path to the folder with the JavaFX files (We placed the folder in C:/, for easiness during the development)
2. Open the ```<launch.json>``` file and add the following configuration:
```json
{
    "type": "java",
    "name": "Launch Game",
    "request": "launch",
    "mainClass": "ExtendedUIP3",
    "vmArgs": "--module-path <fx-file-path>/lib --add-modules javafx.controls,javafx.fxml"
}
```
3. Replace the existing filepath with the path to the JavaFX files
4. Run the configuration and enjoy!

### Functionalities of the Advanced Viewing Mode screen:
1. Upload Graph Button (Upload a file with data for a graph to be viewed and worked on)
2. Color Graph Button (After uploading the graph, the user can press this button to color this graph correctly)
3. Clear Graph Button (The graph can be deleted from the UI, allowing the user to input a new graph and work on it)
4. Unlock Button (When a graph is uploaded in the UI, the user can press this button to unlock the ability to zoom and pan inside of the UI. When it's locked, the user can move around the vertices, but not pan the UI)
5. Number of edges display 
6. Number of vertices display
7. Algorithm used display
8. Chromatic number display
9. Lower Bound display
10. Type display


# Team Members - BCS Group 34
1. Omar Elfiki
2. Mihnea Jianu
3. Vlad Manolescu
4. Haotian Liu
5. Ivars Svarpstons
6. Nezar Souilem
