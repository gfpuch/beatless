# beatless

A small game made with Java/Swing I made in 2009. To run, open beatless.jar.

beatless is a top down "gatherer". the point of the game is to accumulate enough points per "screen" (ie. 520 pixels of vertical movement) so that you do not lose. if you don't accumulate enough, the game ends and your score is transcribed to the high score list.  

the game was initially designed with sound at its core. a minimal electronic hum loops as different actions in-game trigger fitting other synthetic sounds for the purpose of creating a "beatless" song while you play. originally, this game was to use FFT on sound files to determine each level procedurally, in beat with the music.

# issues

beatless was designed in 2009, and so was built for JDK 6, which included incompatible libraries with JDK 10 (specifically, the sun.audio library). The project has been migrated to JDK 10 with no audio at the moment. Since this is a legacy project, there isn't much incentive to change it.

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

	methods:
		beatless() (constructor), paint() used for drawing to screen, the actionPerformed() method for the button,  
		all the MouseListener/MouseMotionListener related methods, the main method

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

    methods:
	screen() (constructor), boxcatch() (used to see if player has intersects box at catch "zone"), all of the  
	KeyListener methods, the enableBuffer() method for double buffering, and refresh() which redraws this screen each frame

enemy  
	a class for storing enemies. It can hold two types; boxes or dots. For each enemy, it stores their x-coordinate (int), their y-coordinate (int), and their state of existence (boolean used to identify if they are killed).

     methods:  
     	enemy() (constructor) getX(), getY(), doesexist(), kill()

playerscore  
	a class for storing player names (string) and their scores (int). Implements comparable and defines its compareTo method via descending score.

     methods:  
     	playerscore() (constructor), getname(), getscore(), compareTo()
