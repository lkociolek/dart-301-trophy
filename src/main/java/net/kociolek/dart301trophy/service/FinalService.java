package net.kociolek.dart301trophy.service;

import net.kociolek.dart301trophy.domain.game.Game;
import net.kociolek.dart301trophy.domain.game.GameRepository;
import net.kociolek.dart301trophy.domain.game.TrophyFinal;
import net.kociolek.dart301trophy.domain.game.TrophySemifinal;
import net.kociolek.dart301trophy.domain.player.Division;
import net.kociolek.dart301trophy.domain.player.Player;
import net.kociolek.dart301trophy.domain.player.PlayerRepository;
import net.kociolek.dart301trophy.domain.trophy.Trophy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class FinalService extends GameService {

    Player player;
    Player playerOne;
    Player playerTwo;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    // generuj mecz fianłowy
    @Override
    public void generateGames(Trophy trophy) {
        assignPlace(trophy);
        playerOne = playerRepository.findPlayerByTrophyAndTrophyFinal(trophy, Arrays.asList(TrophyFinal.values()).get(0));
        playerTwo = playerRepository.findPlayerByTrophyAndTrophyFinal(trophy, Arrays.asList(TrophyFinal.values()).get(1));
        createGame(trophy, playerOne, playerTwo);
    }

    // przypisz zawodnikom pozycje w finale
    @Override
    public void assignPlace(Trophy trophy) {
        List<Division> divisions = Arrays.asList(Division.values());
        Iterator<Division> divisionIterator = divisions.iterator();

        int i = 0;
        while (i < 2) {
            while (divisionIterator.hasNext()) {
                player = playerRepository.findPlayerByTrophyAndTrophySemifinal(trophy, Arrays.asList(TrophySemifinal.values()).get(i));
                Game game = gameRepository.findByPlayerOne(player);
                playerOne = player;
                playerTwo = playerRepository.findById(game.getPlayerTwo()).get();
                if (game.getPlayerOneScore() > game.getPlayerTwoScore()) {
                    playerOne.setTrophyFinal(Arrays.asList(TrophyFinal.values()).get(i));
                } else {
                    playerTwo.setTrophyFinal(Arrays.asList(TrophyFinal.values()).get(i));
                }
            }
            i++;
        }
    }
}
