package com.joseth.contas.beans;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.joseth.contas.daos.ClassificacaoDAO;

public class ClassificacaoRefXmlAdapter extends XmlAdapter<String,Classificacao>
{
	@EJB(beanName="cld")
	ClassificacaoDAO cld;
	 
	public String marshal(Classificacao c)
	{
		return ""+c.getId();
	}
	
    public Classificacao unmarshal(String v)
	{
    	try
		{
    		InitialContext ctx = new InitialContext();
			cld = (ClassificacaoDAO)ctx.lookup("java:module/cld");
		} catch (NamingException e) {
            throw new EJBException(e);
        }
    	Integer id = Integer.parseInt(v);
    	return (Classificacao)cld.pesquisarPorChavePrimaria(id);
	}
}
