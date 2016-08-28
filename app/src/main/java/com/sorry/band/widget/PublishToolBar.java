package com.sorry.band.widget;

import com.sorry.band.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

public class PublishToolBar extends RelativeLayout{
	
	private ImageButton backButton;
	private ImageButton publishButton;
	
	public PublishToolBar(Context context) {
		this(context,null);
	}
	public PublishToolBar(final Context context, AttributeSet attrs){
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.publish_tool_bar, this, true);
		backButton = (ImageButton) findViewById(R.id.backToMainButton);
		publishButton = (ImageButton) findViewById(R.id.publishButton);
	}
	
	public ImageButton getBackButton(){
		return backButton;
	}
	
	public ImageButton getPublishButton(){
		return publishButton;
	}

}
