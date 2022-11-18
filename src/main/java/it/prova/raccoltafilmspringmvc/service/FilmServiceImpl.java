package it.prova.raccoltafilmspringmvc.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.prova.raccoltafilmspringmvc.model.Film;
import it.prova.raccoltafilmspringmvc.repository.film.FilmRepository;

@Service
public class FilmServiceImpl implements FilmService {

	@Autowired
	private FilmRepository repository;

	@Transactional(readOnly = true)
	public List<Film> listAllElements() {
		return (List<Film>) repository.findAll();
	}

	@Transactional(readOnly = true)
	public Film caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Transactional(readOnly = true)
	public Film caricaSingoloElementoEager(Long id) {
		return repository.findSingleFilmEager(id);
	}

	@Transactional
	public void aggiorna(Film filmInstance) {
		repository.save(filmInstance);
	}

	@Transactional
	public void inserisciNuovo(Film filmInstance) {
		repository.save(filmInstance);
	}

	@Transactional
	public void rimuovi(Film filmInstance) {
		repository.delete(filmInstance);
	}

	@Transactional(readOnly = true)
	public List<Film> findByExample(Film example) {
		return repository.findByExample(example);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Film> findByExampleWithPagination(Film example, Integer pageNo, Integer pageSize,
			String sortBy) {
		// TODO Auto-generated method stub
		Specification<Film> specificationCriteria = (root, query, cb) -> {
			
			List<Predicate> predicates = new ArrayList<Predicate>();
			
			if(StringUtils.isNotEmpty(example.getTitolo()))
				predicates.add(cb.like(cb.upper(root.get("titolo")), "%" + example.getTitolo().toUpperCase() + "%"));
			
			if(StringUtils.isNotEmpty(example.getGenere()))
				predicates.add(cb.like(cb.upper(root.get("genere")), "%" + example.getGenere().toUpperCase() + "%"));
			
			if (example.getDataPubblicazione() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("dataPubblicazione"), example.getDataPubblicazione()));
			
			if (example.getMinutiDurata() != null)
				predicates.add(cb.equal(root.get("dataPubblicazione"), example.getMinutiDurata()));
			
			if (example.getRegista() != null)
				predicates.add(cb.equal(root.get("regista.id"), example.getRegista().getId()));

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));	
		};
		
		Pageable paging = null;
		// se non passo parametri di paginazione non ne tengo conto
		if (pageSize == null || pageSize < 10)
			paging = Pageable.unpaged();
		else
			paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));

		return repository.findAll(specificationCriteria, paging);
		
	}

}
