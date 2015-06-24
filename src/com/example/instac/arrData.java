package com.example.instac;

import android.graphics.drawable.Drawable;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public class arrData 
{
	private Drawable drawable;
	private Drawable seldraw;
	private String title;
	private String subTitle;
	private boolean selected;
	private boolean chk;
	private RelativeLayout ll;
	
	
	public Drawable getDrawable()
	{
		return drawable;
	}
	
	public void setDrawable(Drawable drawable)
	{
		this.drawable = drawable;
	}
	
	public Drawable getSelDrawable()
	{
		return seldraw;
	}
	
	public void setSelDrawable(Drawable seldraw)
	{
		this.seldraw = seldraw;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getSubTitle()
	{
		return subTitle;
	}
	
	public void setSubTitle(String subTitle)
	{
		this.subTitle = subTitle; 
	}
	
	
	public boolean isSelected()
	{
		return selected;
	}
	
	public void setSelected(boolean selected)
	{
		this.selected = selected;
	}
	
	public boolean isCheck()
	{
		return chk;
	}
	
	public void setCheck(boolean check)
	{
		chk = check;
	}
	
	public RelativeLayout getProdLay()
	{
		return ll;
	}
	
	public void setProdLay(RelativeLayout ll)
	{
		this.ll = ll;
	}


}
