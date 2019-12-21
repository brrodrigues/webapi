package br.com.meta.services;

import br.com.meta.models.Contato;

import java.util.List;

public interface ContatoService {

    Contato recuperar(Long id);
    public List<Contato> listar( Integer page, Integer size) ;
    Contato salvar (Contato contato);
    Contato atualizar(Contato contato);

}
