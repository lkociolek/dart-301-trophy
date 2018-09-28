package net.kociolek.dart301trophy.domain.game;

import net.kociolek.dart301trophy.domain.player.Player;
import net.kociolek.dart301trophy.domain.trophy.Trophy;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "game", path = "game")
public interface GameRepository extends PagingAndSortingRepository<Game, String> {

    Game findByPlayerOne(@Param("player") Player player);
    List<Game> findAllByTrophy(@Param("trophy") Trophy trophy);

}
