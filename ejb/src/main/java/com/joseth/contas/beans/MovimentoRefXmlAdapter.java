package com.joseth.contas.beans;

import javax.ejb.EJB;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.joseth.contas.daos.MovimentoDAO;

public class MovimentoRefXmlAdapter extends XmlAdapter<String,Movimento>
{
	@EJB(beanName="mvd")
	MovimentoDAO mvd;

	public String marshal(Movimento c)
	{
		return ""+c.getId();
	}
	
    public Movimento unmarshal(String v) throws Exception
	{
    	Integer id = Integer.parseInt(v);
    	Movimento m =(Movimento)mvd.pesquisarPorChavePrimaria(id);

    	if( m == null )
    		throw new Exception("Movimento não carregado");
    	return m;
    	//return (Movimento)hibSession.load(Movimento.class, id);
	}
}
