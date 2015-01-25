package com.joseth.client;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class ConfigPainel extends Composite
{
	interface MyUiBinder extends UiBinder<Widget, ConfigPainel> {}
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	public ConfigPainel() 
	{
	    initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiField
	Panel configPrincipal;
	
	@UiHandler("classBt")
	void onClassBtClicked(ClickEvent event) 
	{
		Classificacoes classificacoes = new Classificacoes();
		configPrincipal.clear();
		configPrincipal.add(classificacoes);
	}
	
	@UiHandler("usuariosBt")
	void onUsuariosBtClicked(ClickEvent event) 
	{
		Usuarios usuarios = new Usuarios();
		configPrincipal.clear();
		configPrincipal.add(usuarios);
	}
}