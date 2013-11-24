package net.einspunktnull.math;

public class Rectangle
{

	public int x = 0;
	public int y = 0;
	public int w = 0;
	public int h = 0;

	public Rectangle(int x, int y, int w, int h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	public Rectangle(float x, float y, float w, float h)
	{
		this((int) x, (int) y, (int) w, (int) h);
	}

	public Rectangle scale(float scale)
	{
		return new Rectangle(x * scale, y * scale, w * scale, h * scale);
	}
}
