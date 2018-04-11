public class playerscore implements Comparable
{
	private String name;
	private int score;
	public playerscore (String name, int score) {
		this.name = name;
		this.score = score;
	}
	public String getname() {
		return name;
	}
	public String getscore() {
		return ("" + score);
	}
	public int compareTo (Object o) {//allows for sorting by score, high to low
		if (o instanceof playerscore == false)
			throw new ClassCastException("Not an item.");
		playerscore t = (playerscore)o;
		return t.score - this.score;
	}
}