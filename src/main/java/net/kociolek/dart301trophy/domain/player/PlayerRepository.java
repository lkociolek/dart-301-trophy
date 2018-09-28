package net.kociolek.dart301trophy.domain.player;

import net.kociolek.dart301trophy.domain.game.TrophyFinal;
import net.kociolek.dart301trophy.domain.game.TrophyStageOne;
import net.kociolek.dart301trophy.domain.game.TrophySemifinal;
import net.kociolek.dart301trophy.domain.game.TrophyStageTwo;
import net.kociolek.dart301trophy.domain.trophy.Trophy;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "player", path = "player")
public interface PlayerRepository extends PagingAndSortingRepository<Player, String> {

    List<Player> findAllByTrophyAndDivision(@Param("trophy") Trophy trophy, @Param("division") Division division);
    List<Player> findTop2ByPointsOrderByPointsDesc(Division division);
    Player findPlayerByTrophyAndTrophyStageOne(@Param("trophy") Trophy trophy, @Param("trophyStageOne") TrophyStageOne trophyStageOne);
    Player findPlayerByTrophyAndTrophyStageTwo(@Param("trophy") Trophy trophy, @Param("trophyStageTwo") TrophyStageTwo trophyStageTwo);
    Player findPlayerByTrophyAndTrophySemifinal(@Param("trophy") Trophy trophy, @Param("trophySemifinal") TrophySemifinal trophySemifinal);
    Player findPlayerByTrophyAndTrophyFinal(@Param("trophy") Trophy trophy, @Param("trophyFinal") TrophyFinal trophyFinal);
    List<Player> findAllByTrophy(@Param("trophy") Trophy trophy);
}
