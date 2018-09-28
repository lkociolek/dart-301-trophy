package net.kociolek.dart301trophy.service;

import net.kociolek.dart301trophy.domain.game.Game;
import net.kociolek.dart301trophy.domain.game.GameRepository;
import net.kociolek.dart301trophy.domain.game.TrophyStageOne;
import net.kociolek.dart301trophy.domain.game.TrophyStageTwo;
import net.kociolek.dart301trophy.domain.player.Division;
import net.kociolek.dart301trophy.domain.player.Player;
import net.kociolek.dart301trophy.domain.player.PlayerRepository;
import net.kociolek.dart301trophy.domain.trophy.Trophy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class StageTwoService extends GameService {

    Player player;
    Player playerOne;
    Player playerTwo;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    // przypisz zawodnikom pozycje w ćwierćfinale
    @Override
    public void assignPlace(Trophy trophy) {
        List<Division> divisions = Arrays.asList(Division.values());
        Iterator<Division> divisionIterator = divisions.iterator();

        int i = 0;
        while (i < 8) {
            while (divisionIterator.hasNext()) {
                player = playerRepository.findPlayerByTrophyAndTrophyStageOne(trophy, Arrays.asList(TrophyStageOne.values()).get(i));
                Game game = gameRepository.findByPlayerOne(player);
                playerOne = player;
                playerTwo = playerRepository.findById(game.getPlayerTwo()).get();
                if (game.getPlayerOneScore() > game.getPlayerTwoScore()) {
                    playerOne.setTrophyStageTwo(Arrays.asList(TrophyStageTwo.values()).get(i));
                } else {
                    playerTwo.setTrophyStageTwo(Arrays.asList(TrophyStageTwo.values()).get(i));
                }
            }
            i++;
        }
    }

    // generuj mecze ćwierćfianłowe
    @Override
    public void generateGames(Trophy trophy) {
        List<Division> divisions = Arrays.asList(Division.values());
        Iterator<Division> divisionIterator = divisions.iterator();

        assignPlace(trophy);

        int i = 0;
        int j = TrophyStageTwo.values().length;

        while (divisionIterator.hasNext()) {
            playerOne = playerRepository.findPlayerByTrophyAndTrophyStageTwo(trophy, Arrays.asList(TrophyStageTwo.values()).get(i));
            playerTwo = playerRepository.findPlayerByTrophyAndTrophyStageTwo(trophy, Arrays.asList(TrophyStageTwo.values()).get(j));
            createGame(trophy, playerOne, playerTwo);
            i++;
            j--;
        }
    }

}
