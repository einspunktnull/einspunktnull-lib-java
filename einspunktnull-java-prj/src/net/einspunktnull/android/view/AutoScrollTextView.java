package net.einspunktnull.android.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ScrollView;
import android.widget.TextView;

public class AutoScrollTextView extends ScrollView
{

	private TextView textView;

	public AutoScrollTextView(Context context)
	{
		super(context);
		init(context);
	}

	public AutoScrollTextView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public AutoScrollTextView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context)
	{
		textView = new TextView(context);
		android.view.ViewGroup.LayoutParams layoutParams = new android.view.ViewGroup.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		textView.setLayoutParams(layoutParams);
		addView(textView);
		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener()
		{

			@Override
			public void onGlobalLayout()
			{
				scrollTo(0, textView.getMeasuredHeight());
			}
		});
	}

	public void addText(String txt)
	{
		addText(txt, "", "");
	}

	public void addText(String txt, String prefix)
	{
		addText(txt, prefix, "");
	}

	public void addText(String txt, String prefix, String suffix)
	{
		String text = getText().toString();
		text += prefix + txt + suffix;
		setText(text);
	}

	public void setText(String txt)
	{
		textView.setText(txt);
	}

	public CharSequence getText()
	{
		return textView.getText();
	}

}
