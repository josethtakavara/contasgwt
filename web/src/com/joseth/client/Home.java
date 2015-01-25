package com.joseth.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class Home implements EntryPoint   
{
	//CellList<String> classificacaoList = new CellList<String>(new TextCell());
	//ListDataProvider<String> classificacaoDataProvider = new ListDataProvider<String>();
	
	public static ServiceBusAsync serviceBus;
	
	@Override
	public void onModuleLoad() 
	{
		serviceBus = GWT.create(ServiceBus.class);
		BarraBotoes barraBotoes = new BarraBotoes();
		RootPanel.get("barraBotoes").add(barraBotoes);
	}
}