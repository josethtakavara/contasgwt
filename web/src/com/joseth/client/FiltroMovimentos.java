package com.joseth.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.joseth.contas.beans.Classificacao;
import com.joseth.contas.beans.Conta;
import com.joseth.contas.beans.Movimento;
import com.joseth.contas.beans.Usuario;

public class FiltroMovimentos extends Composite
{
	interface MyUiBinder extends UiBinder<Widget, FiltroMovimentos> {}
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	 
	@UiField(provided=true)	ListBox filtroMesDe = new ListBox();
	@UiField(provided=true)	ListBox filtroMesAte = new ListBox();
	@UiField(provided=true)	ListBox filtroAnoDe = new ListBox();
	@UiField(provided=true)	ListBox filtroAnoAte = new ListBox();
	@UiField(provided=true)	ListBox classificacoes = new ListBox(true);
	@UiField(provided=true)	ListBox usuario = new ListBox();
	@UiField(provided=true)	ListBox conta = new ListBox();
	@UiField(provided=true)	TextBox descricao = new TextBox();
	@UiField(provided=true)	TextBox comentario = new TextBox();
	@UiField(provided=true)	TextBox valor = new TextBox();
	@UiField(provided=true)	CheckBox subMovimentos = new CheckBox();
	@UiField(provided=true)	CheckBox sinalMais = new CheckBox();
	@UiField(provided=true)	CheckBox sinalMenos = new CheckBox();
	@UiField(provided=true)	CheckBox semClassificacoes = new CheckBox();
	@UiField(provided=true)	CheckBox semComentarios = new CheckBox();
	@UiField(provided=true)	CheckBox pessoal = new CheckBox();
	
	Movimento movimentoPai;
	
	Movimentos movimentos;
	
	@UiHandler("filtrar")
	void onFiltrarClicked(ClickEvent event) 
	{
		filtrar();
	}
	void filtrar()
	{
        if( filtroAnoDe.getSelectedIndex() <= 0  && 
    		filtroAnoAte.getSelectedIndex() <= 0 && 
    		usuario.getSelectedIndex() <= 0 && 
    		conta.getSelectedIndex() <= 0 &&
            !semClassificacoes.getValue() && 
            classificacoes.getSelectedIndex() < 0  &&
            "".equals(descricao.getText()) && 
            "".equals(comentario.getText()) &&
            movimentoPai == null 
        )
        	return;
        
        List<String> classFiltro = new ArrayList<String>();
        for( int i=0; i<classificacoes.getItemCount(); i++)
        	if( classificacoes.isItemSelected(i) )
        		classFiltro.add(classificacoes.getValue(i));
        
//        if( classificacoesFiltro != null && classificacoesFiltro.size()==0 )
//        {
//            classificacoesFiltro = null;
//        }
//        
        String filtroMesAnoDe = null;
        String filtroMesAnoAte = null;
//        
        if( filtroAnoDe.getSelectedIndex() > 0 )
        	filtroMesAnoDe  = (filtroMesDe.getSelectedIndex()  <= 0?"01":  filtroMesDe.getItemText(filtroMesDe.getSelectedIndex()))+"/"+filtroAnoDe.getItemText(filtroAnoDe.getSelectedIndex());
        if( filtroAnoAte.getSelectedIndex() > 0 )
        	filtroMesAnoAte = (filtroMesAte.getSelectedIndex() <= 0?"12":filtroMesAte.getItemText(filtroMesAte.getSelectedIndex()))+"/"+filtroAnoAte.getItemText(filtroAnoAte.getSelectedIndex());
        
        if( filtroMesAnoDe == null  )
        	if( filtroMesAte.getSelectedIndex()  > 0 )
        		filtroMesAnoDe = filtroMesAnoAte;
        	else if( filtroAnoAte.getSelectedIndex()  > 0 ) 
        		filtroMesAnoDe = "01/"+filtroAnoAte.getItemText(filtroAnoAte.getSelectedIndex());
        
        if( filtroMesAnoAte == null  )
	        if( filtroMesDe.getSelectedIndex()  > 0 )
	        	filtroMesAnoAte = filtroMesAnoDe;
	    	else if( filtroAnoDe.getSelectedIndex()  > 0 )
	    		filtroMesAnoAte = "12/"+filtroAnoDe.getItemText(filtroAnoDe.getSelectedIndex());
//        
//        if( subFiltroMovimentos )
//        	FiltroMovimentos = movimentoDAO.pesquisarTudo(
//        			filtroMesAnoDe,
//        			filtroMesAnoAte,
//        			usuario,conta,
//        			classificacoesFiltro,
//        			descricao,comentario,valor,
//        			pessoal,
//        			semComentario,semClassificacoes,sinalMais,sinalMenos);
//        else
        
        	
        Double val = null;
        try
        {
        	Double.parseDouble(valor.getText());
        }
        catch(NumberFormatException nfe)
        {
        	
        }
        
		Home.serviceBus.getMovimentos( 
				subMovimentos.getValue(),
				(movimentoPai==null?null:""+movimentoPai.getId()),
				filtroMesAnoDe,filtroMesAnoAte,
				usuario.getSelectedIndex() >= 0 ? usuario.getValue(usuario.getSelectedIndex()):"-1",
				conta.getSelectedIndex() >= 0 ? conta.getValue(conta.getSelectedIndex()):"-1",
    			classFiltro,
    			descricao.getText(),comentario.getText(),
    			val,
    			pessoal.getValue(),
    			semComentarios.getValue(),semClassificacoes.getValue(),sinalMais.getValue(),sinalMenos.getValue(),
    			
				new AsyncCallback<Collection<Movimento>>()
				{
					public void onFailure(Throwable caught){}
					public void onSuccess(Collection<Movimento> result)
				    {       
						movimentos.movimentosDataProvider.getList().clear();
						movimentos.movimentosDataProvider.getList().addAll(result);
				    }
				}
			);

	}
	
	public FiltroMovimentos(Movimentos movimentos) 
	{
		this.movimentos = movimentos;
		
		classificacoes.setVisibleItemCount(10);
		
		Home.serviceBus.getMeses(
				new AsyncCallback<List<String>>()
				{
					public void onFailure(Throwable caught){}
					public void onSuccess(List<String> result)
				    {
						filtroMesDe.addItem("Mês");
						filtroMesAte.addItem("Mês");

						for(String m:result)
						{
							filtroMesDe.addItem(m);
							filtroMesAte.addItem(m);
						}
				    }
				}
			);
			
			Home.serviceBus.getAnos(
				new AsyncCallback<List<Integer>>()
				{
					public void onFailure(Throwable caught){}
					public void onSuccess(List<Integer> result)
				    {
						filtroAnoDe.addItem("Ano");
						filtroAnoAte.addItem("Ano");
						for(Integer m:result)
						{
							filtroAnoDe.addItem(m.toString());
							filtroAnoAte.addItem(m.toString());
						}
				    }
				}
			);
			
			Home.serviceBus.getUsuarios(
				new AsyncCallback<List<Usuario>>()
				{
					public void onFailure(Throwable caught){}
					public void onSuccess(List<Usuario> result)
				    {
						usuario.addItem("Usuario");
						for(Usuario u:result)
							usuario.addItem(u.getNome(),""+u.getId());
				    }
				}
			);
			
			Home.serviceBus.getContas(
					new AsyncCallback<List<Conta>>()
					{
						public void onFailure(Throwable caught){}
						public void onSuccess(List<Conta> result)
					    {
							conta.addItem("Conta");
							for(Conta c:result)
								conta.addItem(c.getNumero(),""+c.getId());
					    }
					}
				);
			Home.serviceBus.getClassificacoes(
					new AsyncCallback<List<Classificacao>>()
					{
						public void onFailure(Throwable caught){}
						public void onSuccess(List<Classificacao> result)
					    {
							for( Classificacao c: result)
							{
								classificacoes.addItem(c.getNome(),""+c.getId());
							}
					    }
					} 
				);

		
		initWidget(uiBinder.createAndBindUi(this));
	}
}