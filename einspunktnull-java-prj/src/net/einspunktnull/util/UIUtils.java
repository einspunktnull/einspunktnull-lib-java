package net.einspunktnull.util;

import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;

public class UIUtils
{

	public static void setImageIcon(JFrame frame, String imgPath)
	{
		Image image = new ImageIcon(imgPath).getImage();
		frame.setIconImage(image);
	}

	@SuppressWarnings("rawtypes")
	public static boolean selectJComboboxItem(JComboBox cb, Object value)
	{

		int items = cb.getItemCount();
		for (int i = 0; i < items; i++)
		{
			Object item = cb.getItemAt(i);
			if (item.equals(value))
			{
				ActionListener[] listeners = cb.getActionListeners();
				for (ActionListener actionListener : listeners)
				{
					cb.removeActionListener(actionListener);
				}
				cb.setSelectedIndex(i);
				for (ActionListener actionListener : listeners)
				{
					cb.addActionListener(actionListener);
				}
				return true;
			}
		}
		return false;
	}

}
