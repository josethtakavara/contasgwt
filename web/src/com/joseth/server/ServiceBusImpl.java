package com.joseth.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.joseth.client.ServiceBus;
import com.joseth.contas.beans.Classificacao;
import com.joseth.contas.beans.Conta;
import com.joseth.contas.beans.Movimento;
import com.joseth.contas.beans.Usuario;
import com.joseth.contas.daos.ClassificacaoDAO;
import com.joseth.contas.daos.ContaDAO;
import com.joseth.contas.daos.DAOGenerico;
import com.joseth.contas.daos.MovimentoDAO;
import com.joseth.contas.daos.UsuarioDAO;

//@WebServlet(urlPatterns="/com.joseth.ContasGWT/serviceBus")
public class ServiceBusImpl extends RemoteServiceServlet implements ServiceBus
{
	@EJB(beanName="dg")
	DAOGenerico dg;
	
	@EJB(beanName="mvd")
	MovimentoDAO mvd;
	
	@EJB(beanName="ud")
	UsuarioDAO ud;
	
	@EJB(beanName="cd")
	ContaDAO cd;
	
	@EJB(beanName="cld")
	ClassificacaoDAO cld;
	
	@Override
	public List<Classificacao> getClassificacoes() 
	{
		return cld.pesquisarTodos("nome");
	}
	
	@Override
	public List<Usuario> getUsuarios() 
	{
		return dg.getUsuarios();
	}
	
	@Override
	public List<Conta> getContas() 
	{
		return dg.getContas();
	}
	
	public void update(Classificacao c)
	{
		dg.update(c);
	}
	public List<String> getMeses()
	{
		return dg.getMeses();
	}
	public List<Integer> getAnos()
	{ 
		return dg.getAnos();
	}
	public List<String> getMesesAnos()
	{
		return dg.getMesesAnos();
	}
	public List<String> getMesesAnos(Conta c)
	{
		return dg.getMesesAnos(c);
	}
	
	public Collection<Movimento> getMovimentos(
			Boolean subMovimentos,
			String movimentoPaiId,
			String filtroDe, String filtroAte,
			String usuarioId, String contaId,
			Collection<String> classificacoesId, 
			String descricao, String comentario,  Double valor,
			Boolean pessoal,
			Boolean semComentario, Boolean semClassificacoes, Boolean sinalMais, Boolean sinalMenos
			)
	{
		Usuario u = null;
		Conta c = null;
		Collection<Classificacao> classList = new ArrayList<Classificacao>();
		Movimento pai = null;
		
		if( movimentoPaiId != null && !"".equals(movimentoPaiId))
		{
			try
			{
				pai = (Movimento)mvd.pesquisarPorChavePrimaria(Integer.parseInt(movimentoPaiId));
			}
			catch(NumberFormatException nfe){}
		}
		if( usuarioId != null && !"".equals(usuarioId))
		{
			try
			{
				u = (Usuario)ud.pesquisarPorChavePrimaria(Integer.parseInt(usuarioId));
			}
			catch(NumberFormatException nfe){}
		}
		if( contaId != null && !"".equals(contaId))
		{
			try
			{
				c = (Conta)cd.pesquisarPorChavePrimaria(Integer.parseInt(contaId));
			}
			catch(NumberFormatException nfe){}
		}
		for( String cl:classificacoesId)
		{
			try
			{
				classList.add((Classificacao)cld.pesquisarPorChavePrimaria(Integer.parseInt(cl)));
			}
			catch(NumberFormatException nfe){}
		}
		
		Collection<Movimento> ret = mvd.pesquisarTudo(
				subMovimentos,
				pai,
				filtroDe, filtroAte,
				u, c, classList, descricao, comentario, valor, pessoal, 
				semComentario, semClassificacoes, sinalMais, sinalMenos);
		
		for(Movimento m: ret)
			m.setMovimentos(null);
		
		return ret;
	}
	
	public List<Movimento> getArquivoMovimentos(String pass, Boolean inverter)
	{
		HttpServletRequest tlr = this.getThreadLocalRequest();
		HttpSession s = tlr.getSession(true);
		Text2Movs t2m = new Text2Movs(mvd,null,null);
		List<Movimento> ret = t2m.preProcessar( (String)s.getAttribute(pass), inverter ); 
		return ret;
	}
	
	public List<Movimento> getTextoMovimentos(String texto, Boolean inverter)
	{
		Text2Movs t2m = new Text2Movs(mvd,null,null);
		List<Movimento> ret = t2m.preProcessar( texto, inverter ); 
		return ret;
	}
	
	public void atualizarMovimento(Movimento m)
	{
	    mvd.salvar(m);
	}
 
} 