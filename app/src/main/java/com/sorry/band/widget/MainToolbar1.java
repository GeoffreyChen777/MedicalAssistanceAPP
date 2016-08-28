package com.sorry.band.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.sorry.band.R;

public class MainToolbar1 extends RelativeLayout{
	 
	private ImageButton listButton;
	private ImageButton newButton;
	
	public MainToolbar1(Context context) {
		this(context,null);
	}
	public MainToolbar1(final Context context, AttributeSet attrs){
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.maintoolbar1, this, true);
		listButton = (ImageButton) findViewById(R.id.listButton);
		newButton = (ImageButton) findViewById(R.id.newButton);
	}
	
	public ImageButton getListButton(){
		return listButton;
	}
	
	public ImageButton getNewButton(){
		return newButton;
	}

}