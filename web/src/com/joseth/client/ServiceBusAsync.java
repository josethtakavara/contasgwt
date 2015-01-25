package com.joseth.client;

import java.util.Collection;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.joseth.contas.beans.Classificacao;
import com.joseth.contas.beans.Conta;
import com.joseth.contas.beans.Movimento;
import com.joseth.contas.beans.Usuario;

public interface ServiceBusAsync 
{
	public void getClassificacoes(AsyncCallback<List<Classificacao>> callback);
	public void getUsuarios(AsyncCallback<List<Usuario>> callback);
	public void getContas(AsyncCallback<List<Conta>> callback);
	public void update(Classificacao c,AsyncCallback<Void> callback);
	public void getMeses(AsyncCallback<List<String>> callback);
	public void getAnos(AsyncCallback<List<Integer>> callback);
	public void getMesesAnos(AsyncCallback<List<String>> callback);
	public void getMesesAnos(Conta c,AsyncCallback<List<String>> callback);
	
	public void getMovimentos(
			Boolean subMovimentos,
			String movimentoPaiId,
			String filtroDe, String filtroAte,
			String usuario, String conta,
			Collection<String> classificacoes, 
			String descricao, String comentario,  Double valor,
			Boolean pessoal,
			Boolean semComentario, Boolean semClassificacoes, Boolean sinalMais, Boolean sinalMenos,
			AsyncCallback<Collection<Movimento>> callback);
	
	public void getArquivoMovimentos(String pass, Boolean inverter, AsyncCallback<List<Movimento>> callback);
	public void getTextoMovimentos(String texto, Boolean inverter, AsyncCallback<List<Movimento>> callback);
	public void atualizarMovimento(Movimento m, AsyncCallback<Void> callback);
	
}
