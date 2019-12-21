package br.com.meta.services;

import br.com.meta.models.Contato;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Named
public class ContatoServiceImpl implements ContatoService {

    @Inject
    EntityManager manager;

    @Override
    public Contato recuperar(Long id) {
        final Contato contato = manager.find(Contato.class, id);
        return contato;
    }

    @Override
    public List<Contato> listar(Integer pagina, Integer total) {

        if (pagina == null) {
            pagina = 1;
        }
        if (total == null) {
            total = 10;
        }

        final CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        final CriteriaQuery<Contato> query = criteriaBuilder.createQuery(Contato.class);
        final Root<Contato> from = query.from(Contato.class);
        final CriteriaQuery<Contato> select = query.select(from);

        final List<Contato> resultList = manager.createQuery(select).setMaxResults(total).setFirstResult(pagina).getResultList();

        return  resultList;

    }

    @Override
    public Contato salvar(Contato contato) {
        manager.getTransaction().begin();
        manager.persist(contato);
        manager.getTransaction().commit();
        return contato;
    }

    @Override
    public Contato atualizar(Contato contato) {

        manager.getTransaction().begin();
        final Contato contatoAtualizado = manager.merge(contato);
        manager.getTransaction().commit();

        return contatoAtualizado;

    }
}
