package com.joseth.client;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.joseth.contas.beans.Classificacao;
import com.joseth.contas.beans.Movimento;

public class Movimentos extends Composite
{
	interface MyUiBinder extends UiBinder<Widget, Movimentos> {}
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	ProvidesKey<Movimento> KEY_PROVIDER = new ProvidesKey<Movimento>() { @Override public Object getKey(Movimento item) { return item.getId(); } };
	 
	@UiField(provided=true) Grid classGrid=new Grid(1,1);
	
	@UiField(provided = true) FiltroMovimentos filtroMovimentos = new FiltroMovimentos(this);;
	@UiField(provided = true) CellTable<Movimento> movimentos = new CellTable<Movimento>(KEY_PROVIDER);;
	@UiField(provided = true) SimplePager movimentosPager;
	ListDataProvider<Movimento> movimentosDataProvider = new ListDataProvider<Movimento>();

	PopupPanel movimentoContextMenu = new PopupPanel(true);
	VerticalPanel movimentoContextMenuInnerPanel = new VerticalPanel();
	Label labelSubmovimentos = new Label("Submovimentos");
	Label labelAdicionar = new Label("Adicionar Sub");
	Label labelDividir = new Label("Dividir");
	Label labelInverter = new Label("Inverter");
	
	ClickHandlerSubMovimentos chsm = new ClickHandlerSubMovimentos(movimentoContextMenu);
	
	public Movimentos() 
	{        	
		movimentoContextMenu.setWidth("100px");   	
    	movimentoContextMenu.add(movimentoContextMenuInnerPanel);

    	labelSubmovimentos.addStyleName("movPopItem");
    	labelAdicionar.addStyleName("movPopItem");
    	labelDividir.addStyleName("movPopItem");
    	labelInverter.addStyleName("movPopItem");
    	
    	labelSubmovimentos.addClickHandler(chsm);
    	
    	movimentoContextMenuInnerPanel.add(labelSubmovimentos);
    	movimentoContextMenuInnerPanel.add( new HTML("<hr/>") );
    	movimentoContextMenuInnerPanel.add(labelAdicionar);
    	movimentoContextMenuInnerPanel.add( new HTML("<hr/>") );
    	movimentoContextMenuInnerPanel.add(labelDividir);
    	movimentoContextMenuInnerPanel.add( new HTML("<hr/>") );
    	movimentoContextMenuInnerPanel.add(labelInverter);
    	movimentoContextMenu.addStyleName("movPopMenu");
    		  	
		movimentos.setWidth("100%", true);

		movimentos.setAutoHeaderRefreshDisabled(true);
		movimentos.setAutoFooterRefreshDisabled(true);
		
		movimentos.setRowStyles(new RowStyles<Movimento>() 
		{
			@Override
			public String getStyleNames(Movimento row, int rowIndex) 
			{
				return (rowIndex%2)==0?"par":"impar";
			}
		});

		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		movimentosPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		movimentosPager.setDisplay(movimentos);
		
	    initTableColumns();
	    
	    movimentosDataProvider.addDataDisplay(movimentos);

		Home.serviceBus.getClassificacoes(
			new AsyncCallback<List<Classificacao>>()
			{
				public void onFailure(Throwable caught){}
				public void onSuccess(List<Classificacao> result)
			    {
					classGrid.resize((int)Math.ceil(result.size()/4), 4);
					int x=0,y=0;
					for( Classificacao c: result)
					{
					    Label l = new Label(c.getNome());
					    l.getElement().setDraggable(Element.DRAGGABLE_TRUE);
					    l.addDragStartHandler( new ClassificacaoDragStartHandler(c.getId() ) );
					    l.setStyleName("classPainelCelula");
						classGrid.setWidget(y, x, l);
						
						x++;
						if( x>3)
						{
							x=0;
							y++;
						}
					}
			    }
			} 
		);
		 
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	private void initTableColumns() 
	{
		//Id
		colunaId.setSortable(true);
		colunaId.setCellStyleNames("mvId");
	    movimentos.addColumn(colunaId,"Id");
        movimentos.getHeader(0).setHeaderStyleNames("mvId");
	    
	    // Data
  	    colunaData.setSortable(true);
  	    colunaData.setCellStyleNames("mvData");
  	    movimentos.addColumn(colunaData, "Data");
  	    movimentos.getHeader(1).setHeaderStyleNames("mvData");
	    
	    // Descrição
  	    colunaDescricao.setSortable(true);
  	    colunaData.setCellStyleNames("mvDescricao");
  	    movimentos.addColumn(colunaDescricao, "Descrição");
  	    movimentos.getHeader(2).setHeaderStyleNames("mvDescricao");
	    
	    // Documento
	    colunaDocumento.setSortable(true);
	    colunaDocumento.setCellStyleNames("mvDocumento");
  	    movimentos.addColumn(colunaDocumento, "Documento");
  	    movimentos.getHeader(3).setHeaderStyleNames("mvDocumento");
	    
	    // Valor
  	    colunaValor.setSortable(true);
  	    colunaValor.setCellStyleNames("mvValor");
  	    movimentos.addColumn(colunaValor, "Valor");
  	    movimentos.getHeader(4).setHeaderStyleNames("mvValor");
 	    
	    // Comentario
  	    colunaComentario.setSortable(true);
  	    colunaComentario.setCellStyleNames("mvComentario");
  	    movimentos.addColumn(colunaComentario, "Comentario");
  	    movimentos.getHeader(5).setHeaderStyleNames("mComentario");
  	    
  	    // Usuario
  	    movimentos.addColumn(colunaUsuario, "Usuario");
  	    colunaUsuario.setCellStyleNames("mvUsuario");
  	    movimentos.getHeader(6).setHeaderStyleNames("mvUsuario");

  	    // Classificações
  	    movimentos.addColumn(colunaClassificacoes, "Classificações");
  	    colunaClassificacoes.setCellStyleNames("mvClassificacoes");
  	    movimentos.getHeader(7).setHeaderStyleNames("mvClassificacoes");
	}

	// Id
	Column<Movimento, String> colunaId = new Column<Movimento, String>(new TextCell()
	{ 
		@Override
		public Set<String> getConsumedEvents() {
            HashSet<String> events = new HashSet<String>();
            events.add(BrowserEvents.CONTEXTMENU);
            return events;
        }
	}) 
    {
		public String getValue(Movimento m) {return m.getId().toString();}

		// Handler para abrir o contextmenu de ações da linha atual
		@Override
        public void onBrowserEvent(Cell.Context context, Element elem, Movimento object, NativeEvent event)
        {
        	event.preventDefault();
        	chsm.setPai(object);
        	movimentoContextMenu.setPopupPosition(event.getClientX()+5, event.getClientY());
        	movimentoContextMenu.show();
        	
        }
    };

	// Data
	final Column<Movimento, String> colunaData = new Column<Movimento, String>(new EditTextCell()) 
	  	    {		
	  			{
	  		  	    setFieldUpdater(
	  		  	    	new FieldUpdater<Movimento, String>() 
	  		  	    	{
	  		  	    		@Override
	  		  	    		public void update(int index, Movimento m, String v) 
	  		  	    		{
	  		  	    			DateTimeFormat dtf = DateTimeFormat.getFormat("dd/MM/yyyy");
	  		  	    			try
	  		  	    			{
	  		  	    				Date d = dtf.parseStrict(v);
	  		  	    				m.setData(d);
	  		  	    			}
	  		  	    			catch( IllegalArgumentException pe)
	  		  	    			{
	  		  	    				((EditTextCell) colunaData.getCell()).clearViewData(m.getId()); 
	  		  	    				movimentos.redraw();
	  		  	    			}
//		  		  	    			Home.serviceBus.update(object,null);
	  		  	    		}
	  		  	    	}
	  		      	);

	  			}
	  			//public Date getValue(Movimento m) {return m.getData();}
	  			public String getValue(Movimento m) 
	  			{
	  				DateTimeFormat dtf = DateTimeFormat.getFormat("dd/MM/yyyy");
	  				return dtf.format(m.getData());
				}
	  	    };

	
	// Descrição
	Column<Movimento, String> colunaDescricao = new Column<Movimento, String>(new EditTextCell()) 
	  	    {
	  			{
	  				setFieldUpdater(
	  				    	new FieldUpdater<Movimento, String>() 
	  				    	{
	  				    		@Override
	  				    		public void update(int index, Movimento m, String v) 
	  				    		{
	  				    			m.setDescricao(v);
//		  				    			Home.serviceBus.update(object,null);
	  				    		}
	  				    	}
	  			    	);
	  			}
	  			public String getValue(Movimento m) {return m.getDescricao();}
	  	    };
	
	// Documento
	Column<Movimento, String> colunaDocumento = new Column<Movimento, String>(new EditTextCell()) 
	  	    {
	  			{
	  				setFieldUpdater(
	  				    	new FieldUpdater<Movimento, String>() 
	  				    	{
	  				    		@Override
	  				    		public void update(int index, Movimento m, String v) 
	  				    		{
	  				    			m.setDocumento(v);
//		  				    			Home.serviceBus.update(object,null);
	  				    		}
	  				    	}
	  			    	);
	  			}
	  			public String getValue(Movimento m) {return m.getDocumento();}
	  	    };

	
	// Valor
	Column<Movimento, Number> colunaValor = new Column<Movimento, Number>(new NumberCell(NumberFormat.getFormat("###0.00"))) 
    {
		{
			setFieldUpdater(
			    	new FieldUpdater<Movimento, Number>() 
			    	{
			    		@Override
			    		public void update(int index, Movimento m, Number v) 
			    		{
			    			m.setValor(v.doubleValue());
//		  				    	Home.serviceBus.update(object,null);
			    		}
			    	}
		    	);
		}
		public Number getValue(Movimento m) {return m.getValor();}
    };

	
    // Comentario
	Column<Movimento, String> colunaComentario = new Column<Movimento, String>(new EditTextCell()) 
    {
		{
			setFieldUpdater(
			    	new FieldUpdater<Movimento, String>() 
			    	{
			    		@Override
			    		public void update(int index, Movimento m, String v) 
			    		{
			    			m.setComentario(v);
//				    			Home.serviceBus.update(object,null);
			    		}
			    	}
		    	);
		}
		public String getValue(Movimento m) {return m.getComentario();}
    };
	
    // Usuario
	Column<Movimento, String> colunaUsuario = new Column<Movimento, String>(new TextCell()) 
    {
		public String getValue(Movimento m) {return m.getUsuarioRaiz().getNome();}
    };
	
    // Classificações
	Column<Movimento, Movimento> colunaClassificacoes = new Column<Movimento, Movimento>(new ClassListCell(movimentosDataProvider)) 
    {
		public Movimento getValue(Movimento m) 
		{
			return m;
		}
    };
}

// ClickHandler para abrir painel de submovimentos
class ClickHandlerSubMovimentos implements ClickHandler
{
	Movimento pai;
	PopupPanel movimentoContextMenu;
	public ClickHandlerSubMovimentos( PopupPanel movimentoContextMenu )
	{
		this.movimentoContextMenu = movimentoContextMenu;
	}
	@Override
	public void onClick(ClickEvent event) 
	{
			PopupPanel pm = new PopupPanel();
			pm.setPopupPosition(200, 100);
			pm.setSize("1100px", "700px");
			pm.setModal(true);

			pm.add(new SubMovimentos( pm, pai ));
			
			pm.addStyleName("subcontas");
			pm.show();
			movimentoContextMenu.hide();
	}
	public void setPai(Movimento pai){this.pai=pai;}
};

class ClassificacaoDragStartHandler implements DragStartHandler
{
    int cid;
    public ClassificacaoDragStartHandler(int cid)
    {
        this.cid=cid;
    }

    @Override
    public void onDragStart(DragStartEvent event)
    {
        event.getDataTransfer().setData("text/plain", ""+cid);
        
    }
    
}