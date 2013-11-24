package net.einspunktnull.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ToggleButton;

public class NonAutoToggleButton extends ToggleButton
{

	public NonAutoToggleButton(Context context)
	{
		super(context);
	}

	public NonAutoToggleButton(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public NonAutoToggleButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	@Override
	public void toggle()
	{
		// prevent the Button from autotoggling
		// super.toggle();
	}

}
