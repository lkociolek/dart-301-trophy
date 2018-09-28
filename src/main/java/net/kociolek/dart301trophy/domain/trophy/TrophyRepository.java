package net.kociolek.dart301trophy.domain.trophy;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "trophy", path = "trophy")
public interface TrophyRepository extends PagingAndSortingRepository<Trophy, String> {
    List<Trophy> findAll();
    List<Trophy> findByName(String name);
}
