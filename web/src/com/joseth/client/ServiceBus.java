package com.joseth.client;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.joseth.contas.beans.Classificacao;
import com.joseth.contas.beans.Conta;
import com.joseth.contas.beans.Movimento;
import com.joseth.contas.beans.Usuario;

@RemoteServiceRelativePath("serviceBus")
public interface ServiceBus extends RemoteService  
{
	public List<Classificacao> getClassificacoes();
	public List<Usuario> getUsuarios();
	public List<Conta> getContas();
	public void update(Classificacao c);
	public List<String> getMeses();
	public List<Integer> getAnos();
	public List<String> getMesesAnos();
	public List<String> getMesesAnos(Conta c);
	
	public Collection<Movimento> getMovimentos(
			Boolean subMovimentos,
			String movimentoPaiId,
			String filtroDe, String filtroAte,
			String usuario, String conta,
			Collection<String> classificacoes, 
			String descricao, String comentario,  Double valor,
			Boolean pessoal,
			Boolean semComentario, Boolean semClassificacoes, Boolean sinalMais, Boolean sinalMenos
			);
	
	public List<Movimento> getArquivoMovimentos(String pass, Boolean inverter);
	public List<Movimento> getTextoMovimentos(String texto, Boolean inverter);
	public void atualizarMovimento(Movimento m);
	
}
