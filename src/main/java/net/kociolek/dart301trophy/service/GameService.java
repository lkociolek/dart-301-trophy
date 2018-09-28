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
public class GameService {

    Game game;
    Player player;
    Player playerOne;
    Player playerTwo;
    int playerOneScore;
    int playerTwoScore;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PlayerRepository playerRepository;

    // generuj mecze fazy grupowej
    public void generateDivisionGames(Trophy trophy) {
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

    // generuj mecze pierwszej fazy pucharowej
    public void generateStageOneGames(Trophy trophy) {
        List<Division> divisions = Arrays.asList(Division.values());
        Iterator<Division> divisionIterator = divisions.iterator();

        assignStageOnePlace();

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

    // generuj mecze ćwierćfianłowe
    public void generateStageTwoGames(Trophy trophy) {
        List<Division> divisions = Arrays.asList(Division.values());
        Iterator<Division> divisionIterator = divisions.iterator();

        assignStageTwoPlace(trophy);

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

    // generuj mecze półfianłowe
    public void generateStageSemiGames(Trophy trophy) {
        List<Division> divisions = Arrays.asList(Division.values());
        Iterator<Division> divisionIterator = divisions.iterator();

        assignStageSemiPlace(trophy);

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

    // generuj mecz fianłowy
    public void generateStageFinalGames(Trophy trophy) {
        assignStageSemiPlace(trophy);
        playerOne = playerRepository.findPlayerByTrophyAndTrophyFinal(trophy, Arrays.asList(TrophyFinal.values()).get(0));
        playerTwo = playerRepository.findPlayerByTrophyAndTrophyFinal(trophy, Arrays.asList(TrophyFinal.values()).get(1));
        createGame(trophy, playerOne, playerTwo);
    }

    // przypisz zawodnikom pozycję w pierwszej fazie pucharowej
    private void assignStageOnePlace() {
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

    // przypisz zawodnikom pozycje w ćwierćfinale
    private void assignStageTwoPlace(Trophy trophy) {
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

    // przypisz zawodnikom pozycje w półfinale
    private void assignStageSemiPlace(Trophy trophy) {
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

    // przypisz zawodnikom pozycje w finale
    private void assignStageFinalPlace(Trophy trophy) {
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