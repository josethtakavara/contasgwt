package com.joseth.client;

//import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathExpressionException;
//import javax.xml.xpath.XPathFactory;
//import org.w3c.dom.Element;
//import org.w3c.dom.NodeList;
//import org.xml.sax.InputSource;
import com.google.gwt.cell.client.NumberCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteHandler;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.SubmitButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.joseth.contas.beans.Conta;
import com.joseth.contas.beans.Movimento;

public class Carregar extends Composite
{
	interface MyUiBinder extends UiBinder<Widget, Carregar> {}
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	
	@UiField(provided=true) RadioButton tpArquivo = new RadioButton("tipo","Arquivo");
	@UiField(provided=true) RadioButton tpDados = new RadioButton("tipo","Dados");;
	@UiField(provided=true)	ListBox conta = new ListBox();
	
	@UiField(provided=true)	HorizontalPanel entrada  = new HorizontalPanel();
	@UiField(provided=true)	HorizontalPanel visualizar  = new HorizontalPanel();
	
    private List<Movimento> movimentos = new ArrayList<Movimento>();
	ListDataProvider<Movimento> movimentosDataProvider = new ListDataProvider<Movimento>();
	
	public Carregar() 
	{
		Home.serviceBus.getContas(
			new AsyncCallback<List<Conta>>()
			{
				public void onFailure(Throwable caught){}
			    public void onSuccess(List<Conta> result)
			    {
					for(Conta c:result)
						conta.addItem(c.getNumero(),""+c.getId());
			    }
			}
		);
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	@UiHandler("tpArquivo")
	public void onClickArquivo(ClickEvent event)
	{
		
		final int rndid = Random.nextInt();
		
		FormPanel fp = new FormPanel();
		VerticalPanel vp = new VerticalPanel();
		Hidden hd = new Hidden();
		FileUpload fu = new FileUpload();
		final CheckBox inverter = new CheckBox();
		Label inverterLabel = new Label("Inverter");
		SubmitButton sb = new SubmitButton("Visualizar");
		
		fp.setAction("/Contas-web/restrito/carregarArquivo");
		fp.setMethod("POST");
		fp.setEncoding("multipart/form-data");
		
		fu.setName("fu");

		hd.setName("hd");

		hd.setValue("rndid"+rndid);
		
		vp.add(fu);
		vp.add(hd);
		vp.add(inverter);
		vp.add(inverterLabel);
		vp.add(sb);
		fp.add(vp);
		
		fp.addSubmitCompleteHandler( 
			new SubmitCompleteHandler()
			{
				@Override
				public void onSubmitComplete(SubmitCompleteEvent event) 
				{
					Home.serviceBus.getArquivoMovimentos("rndid"+rndid, inverter.getValue(), 
						new AsyncCallback<List<Movimento>>()
						{
							public void onFailure(Throwable caught){}
						    public void onSuccess(List<Movimento> result)
						    {
						    	try
						    	{							  
						    		visualizar(result);
						    	}
						    	catch( Throwable t )
						    	{
						    		throw new RuntimeException(t);
						    	}
						    }
						}
					);
				}
			}
		);
		
		entrada.clear();
		entrada.add(fp);
	}
	
	@UiHandler("tpDados")
	public void onClickDados(ClickEvent event)
	{
		final TextArea ta = new TextArea();
		final CheckBox inverter = new CheckBox();
		Label inverterLabel = new Label("Inverter");
		Button visBotao = new Button("Visualizar");
		
		entrada.clear();
		
		entrada.add(ta);
		entrada.add(inverter);
		entrada.add(inverterLabel);
		entrada.add(visBotao);
		
		visBotao.addClickHandler(
			new ClickHandler()
			{
				@Override
				public void onClick(ClickEvent event) 
				{
					Home.serviceBus.getTextoMovimentos(ta.getText(), inverter.getValue(), 
						new AsyncCallback<List<Movimento>>()
						{
							public void onFailure(Throwable caught){}
						    public void onSuccess(List<Movimento> result)
						    {
						    	try
						    	{							  
						    		visualizar(result);
						    	}
						    	catch( Throwable t )
						    	{
						    		throw new RuntimeException(t);
						    	}
						    }
						}
					);
				}
			}
		);
	}
	
	ProvidesKey<Movimento> KEY_PROVIDER = new ProvidesKey<Movimento>() { @Override public Object getKey(Movimento item) { return item.getId(); } };

	public void visualizar( List<Movimento> newmovs )
	{
		CellTable<Movimento> mvs = new CellTable<Movimento>(KEY_PROVIDER);
		Button gravar = new Button("Gravar");
		
		movimentos.clear();
		movimentos.addAll(newmovs);

		mvs.setRowStyles(new RowStyles<Movimento>() 
		{
			@Override
			public String getStyleNames(Movimento row, int rowIndex) 
			{
				return (rowIndex%2)==0?"par":"impar";
			}
		});
		
	    // Data
  	    colunaData.setSortable(true);
//  	    mvs.addColumn(colunaData, "Data");
  	    mvs.setColumnWidth(colunaData, 75, Unit.PX);
	    
	    // Descrição
  	    colunaDescricao.setSortable(true);
  	    mvs.addColumn(colunaDescricao, "Descrição");
  	    mvs.setColumnWidth(colunaDescricao, 200, Unit.PX);
	    
	    // Documento
	    colunaDocumento.setSortable(true);
  	    mvs.addColumn(colunaDocumento, "Documento");
  	    mvs.setColumnWidth(colunaDocumento, 75, Unit.PX);
	    
	    // Valor
  	    colunaValor.setSortable(true);
  	    mvs.addColumn(colunaValor, "Valor");
  	    mvs.setColumnWidth(colunaValor, 50, Unit.PX);

  	    movimentosDataProvider.getList().clear();
  	    movimentosDataProvider.getList().addAll(movimentos);
  	    movimentosDataProvider.addDataDisplay(mvs);
  	    visualizar.clear();
  	    visualizar.add(mvs);
  	    visualizar.add(gravar);
  	    
//  	    gravar.addClickHandler(
//			new ClickHandler()
//			{
//				@Override
//				public void onClick(ClickEvent event) 
//				{
//					Home.serviceBus.getTextoMovimentos(ta.getText(), inverter.getValue(), 
//						new AsyncCallback<List<Movimento>>()
//						{
//							public void onFailure(Throwable caught){}
//						    public void onSuccess(List<Movimento> result)
//						    {
//						    	try
//						    	{							  
//						    		visualizar(result);
//						    	}
//						    	catch( Throwable t )
//						    	{
//						    		throw new RuntimeException(t);
//						    	}
//						    }
//						}
//					);
//				}
//			}
//		);
	}
	
	// Data
	final Column<Movimento, String> colunaData = new Column<Movimento, String>(new TextCell()) 
    {		
		public String getValue(Movimento m) 
		{
			DateTimeFormat dtf = DateTimeFormat.getFormat("dd/MM/yyyy");
			return dtf.format(m.getData());
		}
    };
	
	// Descrição
	Column<Movimento, String> colunaDescricao = new Column<Movimento, String>(new TextCell()) 
    {
		public String getValue(Movimento m) {return m.getDescricao();}
    };
	
	// Documento
	Column<Movimento, String> colunaDocumento = new Column<Movimento, String>(new TextCell()) 
    {
		public String getValue(Movimento m) {return m.getDocumento();}
    };
	
	// Valor
	Column<Movimento, Number> colunaValor = new Column<Movimento, Number>(new NumberCell(NumberFormat.getFormat("###0.00"))) 
    {
		public Number getValue(Movimento m) {return m.getValor();}
    };
}
