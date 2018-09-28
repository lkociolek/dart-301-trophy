package net.kociolek.dart301trophy.service;

import net.kociolek.dart301trophy.domain.game.Game;
import net.kociolek.dart301trophy.domain.game.GameRepository;
import net.kociolek.dart301trophy.domain.game.TrophySemifinal;
import net.kociolek.dart301trophy.domain.game.TrophyStageTwo;
import net.kociolek.dart301trophy.domain.player.Division;
import net.kociolek.dart301trophy.domain.player.Player;
import net.kociolek.dart301trophy.domain.player.PlayerRepository;
import net.kociolek.dart301trophy.domain.trophy.Trophy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class SemiService extends GameService {

    Player player;
    Player playerOne;
    Player playerTwo;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    // przypisz zawodnikom pozycje w półfinale
    @Override
    public void assignPlace(Trophy trophy) {
        List<Division> divisions = Arrays.asList(Division.values());
        Iterator<Division> divisionIterator = divisions.iterator();

        int i = 0;
        while (i < 4) {
            while (divisionIterator.hasNext()) {
                player = playerRepository.findPlayerByTrophyAndTrophyStageTwo(trophy, Arrays.asList(TrophyStageTwo.values()).get(i));
                Game game = gameRepository.findByPlayerOne(player);
                playerOne = player;
                playerTwo = playerRepository.findById(game.getPlayerTwo()).get();
                if (game.getPlayerOneScore() > game.getPlayerTwoScore()) {
                    playerOne.setTrophySemifinal(Arrays.asList(TrophySemifinal.values()).get(i));
                } else {
                    playerTwo.setTrophySemifinal(Arrays.asList(TrophySemifinal.values()).get(i));
                }
            }
            i++;
        }
    }

    // generuj mecze półfianłowe
    @Override
    public void generateGames(Trophy trophy) {
        List<Division> divisions = Arrays.asList(Division.values());
        Iterator<Division> divisionIterator = divisions.iterator();

        assignPlace(trophy);

        int i = 0;
        int j = TrophySemifinal.values().length;

        while (divisionIterator.hasNext()) {
            playerOne = playerRepository.findPlayerByTrophyAndTrophySemifinal(trophy, Arrays.asList(TrophySemifinal.values()).get(i));
            playerTwo = playerRepository.findPlayerByTrophyAndTrophySemifinal(trophy, Arrays.asList(TrophySemifinal.values()).get(j));
            createGame(trophy, playerOne, playerTwo);
            i++;
            j--;
        }
    }


}
