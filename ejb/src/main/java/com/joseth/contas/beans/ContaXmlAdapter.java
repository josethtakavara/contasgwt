package com.joseth.contas.beans;

import java.io.StringWriter;

import javax.ejb.EJB;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.joseth.contas.daos.ContaDAO;

public class ContaXmlAdapter extends XmlAdapter<String,Conta>
{
	@EJB(beanName="cd")
	ContaDAO cd;
	
	public String marshal(Conta c)
	{
		StringWriter sw = new StringWriter();
    	JAXB.marshal(c,sw);
    	return sw.toString();
	}
	
    public Conta unmarshal(String v)
	{
    	Conta c = JAXB.unmarshal(v,Conta.class);
    	Conta c2 = (Conta)cd.pesquisarPorChavePrimaria(c.getId());
    	if( c2 == null )
    	{
    		cd.salvar(c);
    		c = c2;
		}
    	return c2;
	}
}
