package com.joseth.contas.daos;

import javax.ejb.Stateless;

import com.joseth.contas.beans.Classificacao;

@Stateless(name="cld")
public class ClassificacaoDAO extends DAOBase 
{
	public ClassificacaoDAO(){super(Classificacao.class);}
}