package net.kociolek.dart301trophy.domain.trophy;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "trophy", path = "trophy")
public interface TrophyRepository extends PagingAndSortingRepository<Trophy, String> {
}
