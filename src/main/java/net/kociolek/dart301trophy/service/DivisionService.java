package net.kociolek.dart301trophy.service;

import net.kociolek.dart301trophy.domain.game.GameRepository;
import net.kociolek.dart301trophy.domain.player.Division;
import net.kociolek.dart301trophy.domain.player.Player;
import net.kociolek.dart301trophy.domain.player.PlayerRepository;
import net.kociolek.dart301trophy.domain.trophy.Trophy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DivisionService extends GameService {

    Player playerOne;
    Player playerTwo;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    // generuj mecze fazy grupowej
    @Override
    public void generateGames(Trophy trophy) {
        List<Division> divisions = Arrays.asList(Division.values());
        for(Division d: divisions) {
            List<Player> playersInDivision = playerRepository.findAllByTrophyAndDivision(trophy, d);
            int i = 0;
            int j = 1;
            int k;
            do {
                playerOne = playersInDivision.get(i);
                k = j;
                do {
                    if(k < playersInDivision.size()) {
                        playerTwo = playersInDivision.get(k);
                        createGame(trophy, playerOne, playerTwo);
                        k++;
                    }
                } while (k < playersInDivision.size());
                i++;
                j++;

            } while (i < playersInDivision.size());
        }
    }

    @Override
    public void assignPlace(Trophy trophy) {

    }
}
