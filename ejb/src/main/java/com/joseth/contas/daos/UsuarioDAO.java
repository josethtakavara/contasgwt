package com.joseth.contas.daos;

import javax.ejb.Stateless;

import com.joseth.contas.beans.Usuario;

@Stateless(name="ud")
public class UsuarioDAO extends DAOBase 
{
	public UsuarioDAO(){super(Usuario.class);}
}