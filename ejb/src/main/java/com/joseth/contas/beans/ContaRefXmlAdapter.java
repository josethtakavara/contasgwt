package com.joseth.contas.beans;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.joseth.contas.daos.ContaDAO;

public class ContaRefXmlAdapter extends XmlAdapter<String,Conta>
{
	@EJB(beanName="cd")
	ContaDAO cd;
	
	public String marshal(Conta c)
	{
		return ""+c.getId();
	}
	
    public Conta unmarshal(String v)
	{
    	Integer id = Integer.parseInt(v);
    	if( cd == null )
    	{
    		try
    		{
    			InitialContext ctx = new InitialContext();
    			cd = (ContaDAO)ctx.lookup("java:module/cd");
    		} catch (NamingException e) {
                throw new EJBException(e);
            }
    	}
    	return (Conta)cd.pesquisarPorChavePrimaria(id);
	}
}
