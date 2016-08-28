package com.sorry.band.widget;


import com.sorry.band.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class BottomBar extends RelativeLayout {
	private ImageButton panel1Button;
    private ImageButton panel2Button;
	 public BottomBar(Context context) {
		this(context,null);
	}

	
	  
	   public BottomBar(final Context context, AttributeSet attrs){
			super(context, attrs);
			LayoutInflater.from(context).inflate(R.layout.bottombar, this, true);
			panel1Button = (ImageButton) findViewById(R.id.panel1Button);
			panel2Button = (ImageButton) findViewById(R.id.panel2Button);
	    }
	   public ImageButton getbt1(){
		   return panel1Button;
	   }
	   public ImageButton getbt2(){
		   return panel2Button;
	   }

}
