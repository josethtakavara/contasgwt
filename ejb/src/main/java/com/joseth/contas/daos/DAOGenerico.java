package com.joseth.contas.daos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import com.joseth.contas.beans.Classificacao;
import com.joseth.contas.beans.Conta;
import com.joseth.contas.beans.Usuario;

@Stateless(name="dg")
public class DAOGenerico
{
	@PersistenceContext  (unitName = "primary")
	private EntityManager em;
	
	@Transactional
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Classificacao> getClassificacoes()
	{
		Query q = em.createQuery("from com.joseth.contas.beans.Classificacao");
		return q.getResultList();
	}
	@Transactional
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Usuario> getUsuarios()
	{
		Query q = em.createQuery("from com.joseth.contas.beans.Usuario");
		List<Usuario> in = q.getResultList();
		List<Usuario> out = new ArrayList<Usuario>();
		Mapper mapper = new DozerBeanMapper();

		for( Usuario u: in)
		{
			u = mapper.map(u, Usuario.class);
			out.add(u);
		}
		return out;
	}
	@Transactional
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public List<Conta> getContas()
	{
		Query q = em.createQuery("from com.joseth.contas.beans.Conta");
		List<Conta> in = q.getResultList();
		List<Conta> out = new ArrayList<Conta>();
		Mapper mapper = new DozerBeanMapper();

		for( Conta c: in)
		{
			c = mapper.map(c, Conta.class);
			out.add(c);
		}
		return out;
	}
	
    @Transactional
    public void update(Classificacao c)
	{
		Query q = em.createQuery("from com.joseth.contas.beans.Classificacao c where c.id = ?");
		q.setParameter(1,c.getId());
		Classificacao c1 = (Classificacao)q.getSingleResult();
		c1.setNome(c.getNome());
		em.persist(c1);
	}
    
    
	private List<String> meses = new ArrayList(Arrays.asList("01","02","03","04","05","06","07","08","09","10","11","12"));
	public List<String> getMeses() 
	{   
	   return meses;
    }
	
	public List<Integer> getAnos() 
	{   
	        Query query = em.createQuery(
                "select distinct year(data) " +
                "from Movimento " +
                "where conta is not null " +
                "order by year(data)"                
            );
	        return (List<Integer>) query.getResultList();
    }
	
	public List<String> getMesesAnos() 
	{   
	        Query query = em.createQuery(
                "select distinct concat(lpad(month(data),2,'0'),'/',year(data)) " +
                "from Movimento " +
                "where conta is not null " +
                "order by concat(year(data),lpad(month(data),2,'0'))"                
            );
	        return (List<String>) query.getResultList();
    }
	
	public List<String> getMesesAnos(Conta c) 
	{   
	        Query query = em.createQuery(
                "select distinct concat(lpad(month(data),2,'0'),'/',year(data)) " +
                "from Movimento " +
                "where conta = :conta " +
                "order by year(data),month(data)"                
            );
	        query.setParameter("conta",c);
	        return (List<String>) query.getResultList();
    }

}
