package com.joseth.contas.daos;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.xml.bind.JAXB;

import org.apache.commons.logging.Log;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;

public class DAOBase 
{
//	@Logger
	Log log;

	@PersistenceContext  (unitName = "primary")
	protected EntityManager em;

	protected Class clazz;

	public DAOBase(Class clazz) {
		if (clazz == null)
			throw new NullPointerException();
		this.clazz = clazz;
	}

	public Object pesquisarPorChavePrimaria(Serializable o) {
		return (Object) em.find(clazz, o);
	}

	public List pesquisar(Map<String, Object> m) {
		return pesquisar(m, null);
	}

	public List pesquisar(Map<String, Object> m, String ordem) {
		String hql = "from " + clazz.getCanonicalName() + " as o ";
		int i = 0;
		for (Iterator<String> it = m.keySet().iterator(); it.hasNext(); i++) {
			String campo = (String) it.next();
			if (i == 0)
				hql += "where ";
			else
				hql += "and ";
			if (m.get(campo) == null)
				hql += "o." + campo + " is null ";
			else
				hql += "o." + campo + "=? ";
		}
		if (ordem != null) {
			hql += " order by " + ordem;
		}

		Query q = em.createQuery(hql);

		i = 0;
		for (Iterator it = m.values().iterator(); it.hasNext();) {
			Object v = it.next();
			if (v != null) {
				q.setParameter(i, v);
				i++;
			}
		}

		return q.getResultList();
	}

	public List pesquisar(String campo, Object valor) {
		Query q = null;
		if (valor != null) {
			q = em.createQuery(
					"from " + clazz.getCanonicalName() + " as o where o."
							+ campo + "=?");
			q.setParameter(1, valor);
		} else
			q = em.createQuery(
					"from " + clazz.getCanonicalName() + " as o where o."
							+ campo + " is null");
		return q.getResultList();
	}

	public List pesquisar(String campo, Object valor, String ordem) {
		String hql = "from " + clazz.getCanonicalName() + " as o ";
		Query q = null;

		if (valor != null) {
			hql += "where o." + campo + "=? ";
		} else
			hql += "where o." + campo + " is null ";

		if (ordem != null) {
			hql += "order by o." + ordem;
		}

		q = em.createQuery(hql);
		q.setParameter(1, valor);

		return q.getResultList();
	}

	public List pesquisarTodos() {
		return pesquisarTodos(null);
	}

	public List pesquisarTodos(String campo) {
		return pesquisarTodos(campo, true);
	}

	public List pesquisarTodos(String campo, boolean up) {
		String jpaQL = "from " + clazz.getCanonicalName();

		if (campo != null)
			jpaQL += " order by " + campo + (up ? " asc" : " desc");

		return em.createQuery(jpaQL).getResultList();
	}

	@Transactional
	public void salvar(Collection c) {
		for (Iterator it = c.iterator(); it.hasNext();) {
			Object o = it.next();
			verificaClasse(o);
			salvarInterno(o);
		}
	}

	@Transactional
	public void salvar(Object o) {
		verificaClasse(o);
		salvarInterno(o);
	}

	protected void salvarInterno(Object o) {
		verificaClasse(o);
		em.persist(o);
	}

	@Transactional
	public void replicar(Object o) {
		verificaClasse(o);
		//if( hibSession == null )
			//hibSession = (Session) Component.getInstance("hibSession",true);
		Session s = em.unwrap(Session.class);
		s.replicate(o, ReplicationMode.OVERWRITE);
		s.flush();
		// System.out.println( em.getClass() );
	}

	@Transactional
	public void apagar(Collection c) {
		for (Iterator it = c.iterator(); it.hasNext();) {
			Object o = it.next();
			verificaClasse(o);
			apagarInterno(o);
		}
	}

	@Transactional
	public void apagar(Object o) {
		verificaClasse(o);
		apagarInterno(o);
	}

	public void apagarInterno(Object o) {
		verificaClasse(o);
		
		o = em.merge(o);
		em.remove(o);
	}

	public void verificaClasse(Object o) {
		if (!clazz.isInstance(o))
			throw new Error("Uso incorreto do DAO. Esperado objecto da classe "
					+ clazz.getName() + " mas encontrada "
					+ (o == null ? null : o.getClass().getName()));
	}

	public String toXml(Object o) {
		verificaClasse(o);
		StringWriter sw = new StringWriter();
		JAXB.marshal(o, sw);
		return sw.toString();
	}

	public Object fromXml(String s) {
		return JAXB.unmarshal(new StringReader(s), clazz);
	}
	
	@Transactional
	public void merge( Object o )
	{
		em.merge(o);
	}

}
