package net.kociolek.dart301trophy.service;

import net.kociolek.dart301trophy.domain.game.Game;
import net.kociolek.dart301trophy.domain.game.GameRepository;
import net.kociolek.dart301trophy.domain.game.TrophyStageOne;
import net.kociolek.dart301trophy.domain.player.Division;
import net.kociolek.dart301trophy.domain.player.Player;
import net.kociolek.dart301trophy.domain.player.PlayerRepository;
import net.kociolek.dart301trophy.domain.trophy.Trophy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class StageOneService extends GameService {

    Player playerOne;
    Player playerTwo;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    // przypisz zawodnikom pozycję w pierwszej fazie pucharowej
    @Override
    public void assignPlace(Trophy trophy) {
        List<Division> divisions = Arrays.asList(Division.values());
        Iterator<Division> divisionIterator = divisions.iterator();

        int i = 0;
        while (i < 16) {
            while (divisionIterator.hasNext()) {
                List<Player> players = playerRepository.findTop2ByPointsOrderByPointsDesc(divisionIterator.next());
                players.get(0).setTrophyStageOne(Arrays.asList(TrophyStageOne.values()).get(i));
                players.get(1).setTrophyStageOne(Arrays.asList(TrophyStageOne.values()).get(i + 1));
            }
            i++;
        }
    }

    // generuj mecze pierwszej fazy pucharowej
    @Override
    public void generateGames(Trophy trophy) {
        List<Division> divisions = Arrays.asList(Division.values());
        Iterator<Division> divisionIterator = divisions.iterator();

        assignPlace(trophy);

        int i = 0;
        int j = TrophyStageOne.values().length;
        while (divisionIterator.hasNext()) {
            playerOne = playerRepository.findPlayerByTrophyAndTrophyStageOne(trophy, Arrays.asList(TrophyStageOne.values()).get(i));
            playerTwo = playerRepository.findPlayerByTrophyAndTrophyStageOne(trophy, Arrays.asList(TrophyStageOne.values()).get(j));
            createGame(trophy, playerOne, playerTwo);
            i++;
            j--;
        }
    }

}
