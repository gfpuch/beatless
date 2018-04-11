import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import java.applet.*;
import java.net.*;
import java.util.*;
import sun.audio.*;
import java.io.*;
import javax.swing.JOptionPane;
public class screen extends Canvas implements KeyListener
{
	// lots of variables
	private BufferStrategy buffer;
	private long startfps;
	private long endfps;
	private int alpha = 255;
	private int creditalpha = 0;
	private int nx = 0;
	private int ny = 0;
	private boolean wayback = false;
	private boolean credit = true;
	private boolean credit2 = false;
	private boolean menu = false;
	private boolean start = false;
	private boolean pause = false;
	private boolean debug = false;
	private boolean islooping = false;
	private int dx = 120;
	private int dy = 0;
	private int shootleftx;
	private int shootlefty;
	private int shootrightx;
	private int shootrighty;
	private int shootleftalpha = 255;
	private int shootrightalpha = 255;
	private boolean shootleft = false;
	private boolean shootright = false;
	private boolean shotleft = false;
	private boolean shotright = false;
	private boolean movingright = false;
	private boolean movingleft = false;
	private boolean movethisframe = true;
	private AudioClip box, beam;
	private AudioPlayer audioplayer = AudioPlayer.player;
	private ContinuousAudioDataStream continuousaudiodatastream;
	private LinkedList<enemy> boxes = new LinkedList<enemy>();
	private LinkedList<enemy> dots = new LinkedList<enemy>();
	private int mode = 33;
	private int scorebefore = 0;
	private int score = 0;
	private int scoredifference = 0;
	private boolean killed = false;
	private boolean gameover = false;
	private int hardcounter = 0;
	private int softcounter = 0;
	private boolean skip = false;
	private boolean nameentered = false;
	private LinkedList<playerscore> players = new LinkedList<playerscore>();
	public screen() {
		setLocation(0, 20);
		setSize(250, 480);
		setBackground(Color.lightGray);
		setFocusable(false);
		try {
			box = Applet.newAudioClip(new URL("file:" + System.getProperty("user.dir") + "/" + "box.wav"));
			beam = Applet.newAudioClip(new URL("file:" + System.getProperty("user.dir") + "/" + "beam.wav"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		try {
			continuousaudiodatastream = new ContinuousAudioDataStream((new AudioStream(new FileInputStream(new File("loop.wav")))).getData());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
		}
	}
	public void boxcatch(int x, int y) {
		for (int i = 0; i < boxes.size(); i++) {
			// is the box enemy being caught?
			if ((new Rectangle(x, y + 10, 10, 1)).intersects(new Rectangle(((enemy) (boxes.get(i))).getX(), ((enemy) (boxes.get(i))).getY(), 10, 10)) == true && boxes.get(i).doesexist() == true) {
				boxes.get(i).kill(); // kills enemy if player has caught it
				score += 30;// adds to score
				box.play();
			}
		}
	}
	public void keyTyped(KeyEvent k) {
	}
	public void keyPressed(KeyEvent k) {
		if (k.getKeyCode() == KeyEvent.VK_LEFT) {
			if (pause == false) {
				movingleft = true;
			}
		} else if (k.getKeyCode() == KeyEvent.VK_D) {
			if (gameover == false) {
				if (pause == false) {
					if (debug == false) {
						debug = true;
					} else
						debug = false;
				}
			}
		} else if (k.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (pause == false) {
				movingright = true;
			}
		} else if (k.getKeyCode() == KeyEvent.VK_P) {
			if (menu == false && credit == false && credit2 == false) {
				if (pause == false) {
					pause = true;
				} else {
					pause = false;
				}
			}
		} else if (k.getKeyCode() == KeyEvent.VK_H) {
			if (mode == 33) {
				mode = 16;
			} else
				mode = 33;
		} else if (k.getKeyCode() == KeyEvent.VK_ENTER) {
			if (gameover == true)
				skip = true;
			else {
				start = true;
				credit = false;
				credit2 = false;
			}
		}
	}
	public void keyReleased(KeyEvent k) {
		if (k.getKeyCode() == KeyEvent.VK_RIGHT) {
			if (pause == false) {
				movingright = false;
			}
		} else if (k.getKeyCode() == KeyEvent.VK_LEFT) {
			if (pause == false) {
				movingleft = false;
			}
		} else if (k.getKeyCode() == KeyEvent.VK_ALT) {
			if (gameover == false) {
				beam.play();
				shootright = true;
			}
		} else if (k.getKeyCode() == KeyEvent.VK_CONTROL) {
			if (gameover == false) {
				beam.play();
				shootleft = true;
			}
		}
	}
	public void enableBuffer() {// code for double buffering
		addNotify();
		createBufferStrategy(2);
		buffer = getBufferStrategy();
	}
	public void refresh() throws InterruptedException {
		// end fps counter
		endfps = System.currentTimeMillis();
		// lock to ~30fps in easy mode
		// lock to ~60fps in hard mode
		if (endfps - startfps < mode) {
			Thread.sleep(mode - (endfps - startfps));
			endfps = System.currentTimeMillis();
		}
		// get screen from buffer
		Graphics g = buffer.getDrawGraphics();
		g.setColor(new Color(230, 230, 230, 255));
		g.setFont(new Font("Verdana", Font.PLAIN, 10));
		if (credit == false) {// is first credit over?
			if (menu == false) {// is menu exited?
				if (pause == false) {// is game paused?
					if (mode == 16)
						hardcounter++;
					else
						softcounter++;
					// is audio looping yet, otherwise don't start loop again
					if (islooping == false) {
						audioplayer.start(continuousaudiodatastream);
						islooping = true;
					}
					// move every other frame
					if (movethisframe == true) {
						if (movingleft == true) {
							dx -= 10;
							if (dx < -10)
								dx = 250;
						}
						if (movingright == true) {
							dx += 10;
							if (dx > 250)
								dx = -10;
						}
						movethisframe = false;
					} else if (movethisframe == false) {
						movethisframe = true;
					}
					// clear screen
					g.clearRect(0, 0, 250, 480);
					g.drawString("s c o r e  :  " + score, 10, 10);// draw score
					if (debug == true) {
						// draw fps and position
						g.drawString("fps  " + (1000 / (endfps - startfps)), 191, 15);
						g.drawString("pos " + dx, 190, 25);
						g.drawString("      " + dy, 190, 35);
					}
					startfps = System.currentTimeMillis();
					// enemy draw code
					g.setColor(new Color(122, 138, 153, 150));
					for (int i = 0; i < boxes.size(); i++) {
						if (boxes.get(i).doesexist() == true)
							g.fillRect(((enemy) (boxes.get(i))).getX(), ((enemy) (boxes.get(i))).getY(), 10, 10);
					}
					g.setColor(new Color(230, 230, 230, 100));
					for (int i = 0; i < dots.size(); i++) {
						if (dots.get(i).doesexist() == true)
							g.fillRect(((enemy) (dots.get(i))).getX(), ((enemy) (dots.get(i))).getY(), 10, 4);
					}
					// left beam weapon
					if (shootleft == true) {// is weapon being shot right now?
						shootleftx = dx;
						shootlefty = dy - 30;
						shootleft = false;
						shootleftalpha = 255;
						shotleft = true;
						scorebefore = score;
						for (int i = 0; i < dots.size(); i++) {
							if ((new Rectangle(0, shootlefty + 2, 10, 10)).contains(new Point(((enemy) (dots.get(i))).getX(), ((enemy) (dots.get(i))).getY()))) {
								dots.get(i).kill();
								score += 15;
							}
						}
						if (scorebefore == score)
							score -= 25;
					} else if (shotleft == true) {// was weapon just shot?
						// draw left beam weapon
						// i really don't want to explain all of this code.
						// okay fine but i'm not re-explaining for right weapon
						// redraws a fading-out 2px line from origin to x = 0
						// each time it redraws, it shifts a little more to left
						// redraws a line above and below doing same thing
						// except those have 100 less alpha value
						g.setColor(new Color(122, 138, 153, shootleftalpha));
						for (int i = 0; i < (250 - shootleftx) / 10; i++) {
							g.fillRect(-(i * 10), shootlefty + 6, shootleftx, 2);
							if (shootleftalpha > 99) {
								g.setColor(new Color(122, 138, 153, shootleftalpha - 100));
								g.fillRect(-(i * 10), shootlefty + 8, shootleftx, 2);
								g.fillRect(-(i * 10), shootlefty + 4, shootleftx, 2);
								g.setColor(new Color(122, 138, 153, shootleftalpha));
							}
						}
						if (shootleftalpha < 6) {// has weapon disappeared?
							shotleft = false;
						}
						shootleftalpha -= 25;
					}
					// right beam weapon
					if (shootright == true) {// is weapon being shot right now?
						shootrightx = dx;
						shootrighty = dy - 30;
						shootright = false;
						shootrightalpha = 255;
						shotright = true;
						scorebefore = score;
						for (int i = 0; i < dots.size(); i++) {
							if ((new Rectangle(240, shootrighty + 2, 10, 10)).contains(new Point(((enemy) (dots.get(i))).getX(), ((enemy) (dots.get(i))).getY()))) {
								dots.get(i).kill();
								score += 15;
							}
						}
						if (scorebefore == score) {
							score -= 25;
						}
					} else if (shotright == true) {// was weapon just shot?
						// draw right beam weapon
						g.setColor(new Color(122, 138, 153, shootrightalpha));
						for (int i = 0; i < (250 - shootrightx) / 10; i++) {
							g.fillRect(shootrightx + 10 + i * 10, shootrighty + 6, shootrightx, 2);
							if (shootrightalpha > 99) {
								g.setColor(new Color(122, 138, 153, shootrightalpha - 100));
								g.fillRect(shootrightx + 10 + i * 10, shootrighty + 8, shootrightx, 2);
								g.fillRect(shootrightx + 10 + i * 10, shootrighty + 4, shootrightx, 2);
								g.setColor(new Color(122, 138, 153, shootrightalpha));
							}
						}
						if (shootrightalpha < 6) {// has weapon disappeared?
							shotright = false;
						}
						shootrightalpha -= 25;
					}
					// PLAYER CODE
					g.setColor(new Color(230, 230, 230, alpha - 100));// body
					g.fillRect(-10 + dx, -20 + dy, 10, 10);
					g.fillRect(-10 + dx, -10 + dy, 10, 10);
					g.fillRect(10 + dx, -20 + dy, 10, 10);
					g.fillRect(10 + dx, -10 + dy, 10, 10);
					g.setColor(new Color(230, 230, 230, alpha));
					g.fillRect(dx, -30 + dy, 10, 10);
					boxcatch(dx, -30 + dy);
					// enemy draw code
					if (dy == 0) {
						boxes.clear();
						for (int i = 0; i < 7; i++) {
							nx = (int) ((Math.random()) * 210) + 20;
							nx -= (nx % 10);
							boxes.add(new enemy(nx, i * 60 + 80, true));
						}
						dots.clear();
						for (int i = 0; i < 40 + (int) (Math.random()) * 10; i++) {
							ny = (int) (Math.random() * 420) + 60;
							ny -= (ny % 24);
							if (Math.random() > 0.5)
								dots.add(new enemy(0, ny, true));
							else
								dots.add(new enemy(240, ny, true));
						}
						scoredifference = score;
					}
					dy += 1;
					if (dy > 520) {// if player below offscreen
						dy = 0;// bring player to above offscreen
						if (softcounter > hardcounter) {
							if (score - scoredifference < 210) {
								gameover = true;
								menu = true;
							}
						} else {
							if (score - scoredifference < 400) {
								gameover = true;
								menu = true;
							}
						}
					}
					// ohgod gradient
					for (int i = 0; i < 100; i += 2) {
						g.setColor(new Color(122, 138, 153, (100 - i)));
						g.fillRect(0, 0 + (i / 2), 250, 1);
					}
					// OHGODHARDMODE
					if (mode == 16) {
						g.setColor(new Color(230, 230, 230, 255));
						g.drawString("H A R D", 200, 470);
					}
				} else {// this is what it does when you pause
					audioplayer.stop(continuousaudiodatastream);
					islooping = false;
					g.drawString("P   A   U   S   E", 10, 470);
				}
			} else if (gameover == false) {// main menu!!
				g.clearRect(0, 0, 250, 480);
				g.setColor(new Color(230, 230, 230, creditalpha));
				g.drawString("p r e s s   e n t e r   t o   s t a r t", 32, 230);
				if (start == false) {
					if (creditalpha < 255 && wayback == false)
						creditalpha++;
					else if (creditalpha >= 255 && wayback == false) {
						wayback = true;
						Thread.sleep(450);
					} else if (creditalpha > 50 && wayback == true) {
						creditalpha--;
					} else if (creditalpha <= 50 && wayback == true) {
						wayback = false;
						Thread.sleep(450);
					}
				} else {
					if (creditalpha > 0)
						creditalpha--;
					else {
						menu = false;
						Thread.sleep(450);
					}
				}
				// ohgod gradient
				for (int i = 0; i < 100; i += 2) {
					g.setColor(new Color(122, 138, 153, (100 - i)));
					g.fillRect(0, 0 + (i / 2), 250, 1);
				}
			} else {// game over!
				if (skip == false) {
					g.clearRect(0, 0, 250, 480);
					// ohgod gradient
					for (int i = 0; i < 100; i += 2) {
						g.setColor(new Color(122, 138, 153, (100 - i)));
						g.fillRect(0, 0 + (i / 2), 250, 1);
					}
					g.setColor(new Color(230, 230, 230, creditalpha));
					g.drawString("G  A  M  E  //  O  V  E  R", 58, 100);
					g.setColor(new Color(230, 230, 230, 155));
					g.setFont(new Font("Verdana", Font.PLAIN, 9));
					g.drawString("s c o r e : " + score, 58, 120);
					g.drawString("please press enter.", 58, 140);
					g.drawString("you spent:", 0, 430);
					if ((Math.round((double) hardcounter / (double) (softcounter + hardcounter) * 100.0)) == 50) {
						g.drawString("50%", 100, 445);
						g.drawString("of the time in both modes.", 0, 460);
						g.setColor(new Color(122, 138, 153, 255 - creditalpha));
						g.drawString("how did you do that.", 0, 475);
					} else if (softcounter > hardcounter) {
						g.drawString((Math.round((double) softcounter / (double) (softcounter + hardcounter) * 100.0)) + "%", 0, 445);
						g.drawString("of the time in soft mode.", 0, 460);
						g.setColor(new Color(122, 138, 153, 255 - creditalpha));
						g.drawString("your spirit is weak.", 0, 475);
					} else if (softcounter < hardcounter) {
						g.drawString((Math.round((double) hardcounter / (double) (softcounter + hardcounter) * 100.0)) + "%", 0, 445);
						g.drawString("of the time in hard mode.", 0, 460);
						g.setColor(new Color(122, 138, 153, 255 - creditalpha));
						g.drawString("you have admirable qualities.", 0, 475);
					}
					if (creditalpha < 255 && wayback == false)
						creditalpha++;
					else if (wayback == false) {
						Thread.sleep(600);
						wayback = true;
					} else if (creditalpha > 0 && wayback == true) {
						creditalpha--;
					} else {
						creditalpha = 0;
						wayback = false;
						Thread.sleep(300);
					}
				} else {// score entry
					// name entry
					String name = (JOptionPane.showInputDialog(this, "enter your name.", "", JOptionPane.PLAIN_MESSAGE));
					if (softcounter > hardcounter)
						name = name + " - soft";
					else
						name = name + " - hard";
					nameentered = true;
					try {
						players.add(new playerscore(name, score));
						String s = "";
						BufferedReader in = new BufferedReader(new FileReader("scoreboard"));
						// read in a file and sort it into the list of players
						while ((s = in.readLine()) != null) {
							String[] list = s.split("%");
							String tempname = "";
							for (int z = 1; z < list.length; z++) {
								tempname = tempname + list[z];
							}
							players.add(new playerscore(tempname, Integer.parseInt(list[0])));
						}
						in.close();
						Collections.sort(players);// sort in terms of score
						BufferedWriter out = new BufferedWriter(new FileWriter("scoreboard"));
						// write the sorted scores
						for (int i = 0; i < players.size(); i++) {
							out.write(players.get(i).getscore() + "%" + players.get(i).getname());
							if (i != players.size() - 1)
								out.newLine();
						}
						out.close();
						in = new BufferedReader(new FileReader("scoreboard"));
						System.out.println("\nscores: \n");
						// print all the scores
						while ((s = in.readLine()) != null) {
							String[] list = s.split("%");
							for (int i = 0; i < list.length; i++)
								System.out.print(list[i] + " ");
							System.out.println();
						}
						in.close();
						System.out.println("\nend.");
						System.exit(0);
					} catch (FileNotFoundException e) {
					} catch (IOException e) {
					}
				}
			}
		} else {
			if (credit2 == false) {// is second credit over?
				g.clearRect(0, 0, 250, 480);
				g.setColor(new Color(230, 230, 230, creditalpha));
				// N
				g.fillRect(90, 230, 5, 5);
				g.fillRect(90, 235, 5, 5);
				g.fillRect(90, 240, 5, 5);
				g.fillRect(90, 245, 5, 5);
				g.fillRect(95, 235, 5, 5);
				g.fillRect(100, 240, 5, 5);
				g.fillRect(105, 230, 5, 5);
				g.fillRect(105, 235, 5, 5);
				g.fillRect(105, 240, 5, 5);
				g.fillRect(105, 245, 5, 5);
				// U
				g.fillRect(115, 230, 5, 5);
				g.fillRect(115, 235, 5, 5);
				g.fillRect(115, 240, 5, 5);
				g.fillRect(115, 245, 5, 5);
				g.fillRect(120, 245, 5, 5);
				g.fillRect(125, 230, 5, 5);
				g.fillRect(125, 235, 5, 5);
				g.fillRect(125, 240, 5, 5);
				g.fillRect(125, 245, 5, 5);
				// L
				g.fillRect(135, 230, 5, 5);
				g.fillRect(135, 235, 5, 5);
				g.fillRect(135, 240, 5, 5);
				g.fillRect(135, 245, 5, 5);
				g.fillRect(140, 245, 5, 5);
				// L
				g.fillRect(150, 230, 5, 5);
				g.fillRect(150, 235, 5, 5);
				g.fillRect(150, 240, 5, 5);
				g.fillRect(150, 245, 5, 5);
				g.fillRect(155, 245, 5, 5);
				// date
				g.drawString("15.06.2009", 93, 260);
				if (creditalpha < 255 && wayback == false)
					creditalpha++;
				else if (wayback == false) {
					Thread.sleep(600);
					wayback = true;
				} else if (creditalpha > 0 && wayback == true) {
					creditalpha--;
				} else {
					creditalpha = 0;
					wayback = false;
					credit2 = true;
					Thread.sleep(300);
				}
			} else {
				g.clearRect(0, 0, 250, 480);
				g.setColor(new Color(230, 230, 230, creditalpha));
				// I
				g.fillRect(60, 230, 5, 5);
				g.fillRect(60, 250, 5, 5);
				g.fillRect(65, 230, 5, 5);
				g.fillRect(65, 235, 5, 5);
				g.fillRect(65, 240, 5, 5);
				g.fillRect(65, 245, 5, 5);
				g.fillRect(65, 250, 5, 5);
				g.fillRect(70, 230, 5, 5);
				g.fillRect(70, 250, 5, 5);
				// C
				g.fillRect(80, 230, 5, 5);
				g.fillRect(85, 230, 5, 5);
				g.fillRect(90, 230, 5, 5);
				g.fillRect(80, 235, 5, 5);
				g.fillRect(80, 240, 5, 5);
				g.fillRect(80, 245, 5, 5);
				g.fillRect(80, 250, 5, 5);
				g.fillRect(85, 250, 5, 5);
				g.fillRect(90, 250, 5, 5);
				// S
				g.fillRect(100, 230, 5, 5);
				g.fillRect(105, 230, 5, 5);
				g.fillRect(110, 230, 5, 5);
				g.fillRect(100, 235, 5, 5);
				g.fillRect(100, 240, 5, 5);
				g.fillRect(105, 240, 5, 5);
				g.fillRect(110, 240, 5, 5);
				g.fillRect(110, 245, 5, 5);
				g.fillRect(100, 250, 5, 5);
				g.fillRect(105, 250, 5, 5);
				g.fillRect(110, 250, 5, 5);
				// 4
				g.fillRect(120, 230, 5, 5);
				g.fillRect(120, 235, 5, 5);
				g.fillRect(120, 240, 5, 5);
				g.fillRect(125, 240, 5, 5);
				g.fillRect(130, 230, 5, 5);
				g.fillRect(130, 235, 5, 5);
				g.fillRect(130, 240, 5, 5);
				g.fillRect(135, 240, 5, 5);
				g.fillRect(130, 245, 5, 5);
				g.fillRect(130, 250, 5, 5);
				// M
				g.fillRect(145, 230, 5, 5);
				g.fillRect(145, 235, 5, 5);
				g.fillRect(145, 240, 5, 5);
				g.fillRect(145, 245, 5, 5);
				g.fillRect(145, 250, 5, 5);
				g.fillRect(150, 230, 5, 5);
				g.fillRect(155, 230, 5, 5);
				g.fillRect(155, 235, 5, 5);
				g.fillRect(155, 240, 5, 5);
				g.fillRect(155, 245, 5, 5);
				g.fillRect(155, 250, 5, 5);
				g.fillRect(160, 230, 5, 5);
				g.fillRect(165, 230, 5, 5);
				g.fillRect(165, 235, 5, 5);
				g.fillRect(165, 240, 5, 5);
				g.fillRect(165, 245, 5, 5);
				g.fillRect(165, 250, 5, 5);
				// 1
				g.fillRect(175, 230, 5, 5);
				g.fillRect(175, 250, 5, 5);
				g.fillRect(180, 230, 5, 5);
				g.fillRect(180, 235, 5, 5);
				g.fillRect(180, 240, 5, 5);
				g.fillRect(180, 245, 5, 5);
				g.fillRect(180, 250, 5, 5);
				g.fillRect(185, 250, 5, 5);
				// other
				g.drawString("designed for ms. wong", 60, 265);
				if (creditalpha < 255 && wayback == false)
					creditalpha++;
				else if (wayback == false) {
					Thread.sleep(600);
					wayback = true;
				} else if (creditalpha > 0 && wayback == true) {
					creditalpha--;
				} else {
					credit = false;
					credit2 = false;
					creditalpha = 0;
					wayback = false;
					menu = true;
				}
			}
			// ohgod gradient
			for (int i = 0; i < 100; i += 2) {
				g.setColor(new Color(122, 138, 153, (100 - i)));
				g.fillRect(0, 0 + (i / 2), 250, 1);
			}
		}
		buffer.show();// sho taimu da
	}
}