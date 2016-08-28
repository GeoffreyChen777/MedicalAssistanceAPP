package com.sorry.band.widget;

import com.sorry.band.R;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainToolbar extends RelativeLayout{
	private TextView title;
	private ImageButton highFrequencyButton;
	private ImageButton menuButton;
	public MainToolbar(Context context) {
		this(context,null);
	}
	public MainToolbar(final Context context, AttributeSet attrs){
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.maintoolbar, this, true);
		title = (TextView) findViewById(R.id.titleTextView);
		highFrequencyButton = (ImageButton) findViewById(R.id.highFrequencyButton);
		menuButton = (ImageButton) findViewById(R.id.menuButton);
	}

	public ImageButton getHighFrequencyButton(){
		return highFrequencyButton;
	}
	public ImageButton getMenuButton(){
		return menuButton;
	}

	public TextView getTitleView(){
		return title;
	}

}
