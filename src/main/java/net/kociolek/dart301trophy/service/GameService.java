package net.kociolek.dart301trophy.service;

import net.kociolek.dart301trophy.domain.player.Division;
import net.kociolek.dart301trophy.domain.game.*;
import net.kociolek.dart301trophy.domain.player.Player;
import net.kociolek.dart301trophy.domain.player.PlayerRepository;
import net.kociolek.dart301trophy.domain.trophy.Trophy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public abstract class GameService {

    Game game;
    int playerOneScore;
    int playerTwoScore;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    public abstract void generateGames(Trophy trophy);
    public abstract void assignPlace(Trophy trophy);

    // stwórz mecz
    public void createGame(Trophy trophy, Player playerOne, Player playerTwo) {
        game = new Game();
        game.setTrophy(trophy);
        game.setPlayerOne(playerOne.getId());
        game.setPlayerTwo(playerTwo.getId());
        gameRepository.save(game);
    }

    // ustaw wynik meczu
    public void setScoresForGame(Game game) {
        game.setPlayerOneScore(playerOneScore);
        game.setPlayerTwoScore(playerTwoScore);
        gameRepository.save(game);
    }

    // sprawdzanie czy, zawodnikowi udało się uzyskać 301 pkt wymagane do wygrania spotkania
    public void checkIfWinner(Game game) {
        setScoresForGame(game);
        if(game.getPlayerOneScore() == 301) {
            //TODO: show monit about the winner
        } else if(game.getPlayerTwoScore() == 301) {
            //TODO: show monit about the winner
        }
    }

    // dodaj punkty za mecz
    public void addPointsForPlayers(Game game) {
        Player playerOne = playerRepository.findById(game.getPlayerOne()).get();
        Player playerTwo = playerRepository.findById(game.getPlayerTwo()).get();
        int playerOnePoints = playerOne.getPoints();
        int playerTwoPoints = playerTwo.getPoints();
        playerOne.setPoints(playerOnePoints + game.getPlayerOneScore());
        playerTwo.setPoints(playerTwoPoints + game.getPlayerTwoScore());
        gameRepository.save(game);
    }

}