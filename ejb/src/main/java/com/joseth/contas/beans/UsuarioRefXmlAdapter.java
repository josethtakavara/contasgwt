package com.joseth.contas.beans;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.joseth.contas.daos.UsuarioDAO;

public class UsuarioRefXmlAdapter extends XmlAdapter<String,Usuario>
{
	@EJB(beanName="ud")
	UsuarioDAO ud;
	
	public String marshal(Usuario c)
	{
		return ""+c.getId();
	}
	
    public Usuario unmarshal(String v)
	{
    	Integer id = Integer.parseInt(v);
    	try
		{
    		InitialContext ctx = new InitialContext();
			ud = (UsuarioDAO)ctx.lookup("java:module/ud");
		} catch (NamingException e) {
            throw new EJBException(e);
        }
    	return (Usuario)ud.pesquisarPorChavePrimaria(id);
	}
}