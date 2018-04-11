public class enemy
{
	private int x;
	private int y;
	private boolean exists;
	public enemy(int x, int y, boolean exists) {
		this.x = x;
		this.y = y;
		this.exists = exists;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public boolean doesexist() {
		return exists;
	}
	public void kill() {
		exists = false;
	}
}