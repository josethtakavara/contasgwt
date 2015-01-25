package com.joseth.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class ResumoPainel extends Composite
{
	interface MyUiBinder extends UiBinder<Widget, ResumoPainel> {}
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	public ResumoPainel() 
	{
	    initWidget(uiBinder.createAndBindUi(this));
	    setUserName("2");
	}
	

	@UiField Element  usuariosCnt;
	public void setUserName(String userName) 
	{
		usuariosCnt.setInnerText(userName);
	}
}