# beatless

A small game made with Java/Swing I made in 2009. To run, open beatless.jar.

beatless is a top down "gatherer". the point of the game is to accumulate enough points per "screen" (ie. 520 pixels of vertical movement) so that you do not lose.

if you don't accumulate enough, the game ends and your score is transcribed to the high score list.

# issues

Beatless was designed in 2009, and so was built for JDK 6, which included incompatible libraries with JDK 10 (specifically, the sun.audio library). The project has been migrated to JDK 10 with no audio at the moment. Since this is a legacy project, there isn't much incentive to change it.

# how to play

<- arrow: left  
-> arrow: right  
ctrl: left beam  
alt: right beam  
p: pause  
h: HARD MODE  

What to do:  
'capture' the grey boxes and shoot the white ones.  
each missed shot is -10 points  
each shot hit is 30 points  
each box captured is 10 points  
game over when you miss a box and complete the screen.  

![screenshot 1](https://github.com/gfpuch/beatless/blob/master/readme_photos/beatless_1.png?raw=true)
![screenshot 2](https://github.com/gfpuch/beatless/blob/master/readme_photos/beatless_2.png?raw=true)
![screenshot 3](https://github.com/gfpuch/beatless/blob/master/readme_photos/beatless_3.png?raw=true)

# classes

-beatless
-screen
-enemy
-playerscore

beatless  
	this is the main class. It extends JFrame and implements ActionListener (for the custom close button) and MouseListener + MouseMotionListener (for the window dragging)
	
	imports:
	import java.awt.*;
	import java.awt.event.*;
	import javax.swing.*;

	variables:
	private static boolean USE_CROSS_PLATFORM_UI = true; - uses cross platform ui for close button's simple design
	private screen sc; - the screen that contains the actual game code
	private Point location; - the position the mouse pointer is currently at
	private MouseEvent pressed; - a mouse event for when the mouse is clicked
	methods:
		beatless() (constructor), paint() used for drawing to screen, the actionPerformed() method for the button, all the MouseListener/MouseMotionListener related methods, the main method

screen  
	this class contains the majority of the game's code. It extends Canvas and implements KeyListener (for key input)

	imports:
	import java.awt.*;
	import java.awt.image.*;
	import java.awt.event.*;
	import java.applet.*;
	import java.net.*;
	import java.util.*;
	import sun.audio.*;
	import java.io.*;
	import javax.swing.JOptionPane;

	variables:
	private BufferStrategy buffer; - the double buffering strategy used for graphics rendering
	private long startfps; - gets the seconds at the beginning of the frame
	private long endfps; - gets the seconds at the end, used for FPS calculation
	private int alpha = 255; - an alpha variable, slightly redundant
	private int creditalpha = 0; - an alpha variable used for dynamic frame-dependant alpha values changes
	private int nx = 0; - x value used for random enemy location generation
	private int ny = 0; - y value used for random enemy location generation
	private boolean wayback = false; - a boolean used for dynamic frame-dependant alpha value changes
	private boolean credit = true; - a boolean used to check if the first credit screen is underway
	private boolean credit2 = false; - a boolean used to check if the second credit screen is underway
	private boolean menu = false; - a boolean used to check if the main menu screen is underway
	private boolean start = false; - a boolean used to check if user skips credits and menu. hidden.
	private boolean pause = false; - a boolean used to check if user has paused the game
	private boolean debug = false; - a boolean used to check if user has enabled debug mode. hidden.
	private boolean islooping = false; - a boolean used to check if the background music has started looping.
	private int dx = 120; - x origin of player
	private int dy = 0; - y origin of player
	private int shootleftx; - calculates the x value when the player shoots left
	private int shootlefty; - calulates the y value when the player shoots left
	private int shootrightx; - calculates the x value when the player shoots right 
	private int shootrighty; - calculates the y value when the player shoots right
	private int shootleftalpha = 255; - alpha value used for fading effect of left beam weapon
	private int shootrightalpha = 255; - alpha value used for fading effect of right beam weapon
	private boolean shootleft = false; - boolean used to check if user is currently inputting left-beam
	private boolean shootright = false; - boolean used to check if user is currently inputting right-beam
	private boolean shotleft = false; - boolean used to check if user has just inputted left-beam
	private boolean shotright = false; - boolean used to check if user has just inputted right-beam
	private boolean movingright = false; - is the user moving right
	private boolean movingleft = false; - is the user moving left
	private boolean movethisframe = true; - boolean used for "every other frame" movement, for better handling
	private AudioClip box, beam; - holds the sounds made when interacting with enemies
	private AudioPlayer audioplayer = AudioPlayer.player; - audio player
	private ContinuousAudioDataStream continuousaudiodatastream; - holds the looping background sound
	private LinkedList<enemy> boxes = new LinkedList<enemy>(); - holds the instance of box enemies on this "screen"
	private LinkedList<enemy> dots = new LinkedList<enemy>(); - holds the instance of dot enemies on this "screen"
	private int mode = 33; - holds framerate. altered for hard mode (to 16)
	private int scorebefore = 0; - used in calculation of score
	private int score = 0; - holds score
	private int scoredifference = 0; - used in calculation of game over
	private boolean killed = false; - holds existance status
	private boolean gameover = false; - holds state of game over
	private int hardcounter = 0; - holds amount of frames spent in hard mode
	private int softcounter = 0; - holds amount of frames spent in easy mode
	private boolean skip = false; - used to check if score entry is underway
	private boolean nameentered = false; - used to check if user's name has already been entered
	private LinkedList<playerscore> players = new LinkedList<playerscore>(); - holds the player names and their scores
	
    methods:
	screen() (constructor), boxcatch() (used to see if player has intersects box at catch "zone"), all of the KeyListener methods, the enableBuffer() method 	for double buffering, and refresh() which redraws this screen each frame

enemy  
	a class for storing enemies. It can hold two types; boxes or dots. For each enemy, it stores their x-coordinate (int), their y-coordinate (int), and 		their state of existance (boolean used to identify if they are killed).

     methods: enemy() (constructor) getX(), getY(), doesexist(), kill()

playerscore  
	a class for storing player names (string) and their scores (int). Implements comparable and defines its compareTo method via descending score.

     methods: playerscore() (constructor), getname(), getscore(), compareTo()
