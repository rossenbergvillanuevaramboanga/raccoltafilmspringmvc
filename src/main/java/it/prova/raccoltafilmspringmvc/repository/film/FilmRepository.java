package it.prova.raccoltafilmspringmvc.repository.film;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import it.prova.raccoltafilmspringmvc.model.Film;
import it.prova.raccoltafilmspringmvc.model.Regista;

public interface FilmRepository 
extends PagingAndSortingRepository<Film, Long>, 
JpaSpecificationExecutor<Film>, 
CrudRepository<Film, Long>,
CustomFilmRepository{
	
	@Query("from Film f join fetch f.regista where f.id = ?1")
	Film findSingleFilmEager(Long id);

}
