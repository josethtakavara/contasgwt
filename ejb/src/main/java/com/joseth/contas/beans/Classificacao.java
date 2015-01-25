package com.joseth.contas.beans;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;


//@XmlRootElement
//@-Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Cacheable
public class Classificacao implements Serializable, Comparable<Classificacao> {
	
	private Integer id;
	private Integer version;
	private String nome;
		
	@Id
//	@GeneratedValue(generator="hibincClassificacao")
//	@GenericGenerator(name="hibincClassificacao", strategy = "increment")
	public Integer getId() {
	     return id;
	}

	public void setId(Integer id) {
	     this.id = id;
	}
	
	@Version
	public Integer getVersion() {
	     return version;
	}

	public void setVersion(Integer version) {
	     this.version = version;
	}   	
	
//	@Length(max=20)
	public String getNome() { 
	     return nome;
	}

	public void setNome(String nome) {
	     this.nome = nome;
	}
	
	public int hashCode(){return id==null?super.hashCode():id.intValue();}
	public boolean equals( Object o ){return o instanceof Classificacao && ((Classificacao)o).getId().equals(id);}

	public String toString(){return "Classificacao("+id+","+nome+")";}
	
	public int compareTo(Classificacao c) {
		return nome.compareTo(((Classificacao)c).getNome());
	}
}
