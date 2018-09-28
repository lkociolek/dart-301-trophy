package net.kociolek.dart301trophy.service;

import net.kociolek.dart301trophy.domain.player.Division;
import net.kociolek.dart301trophy.domain.player.Player;
import net.kociolek.dart301trophy.domain.player.PlayerRepository;
import net.kociolek.dart301trophy.domain.trophy.Trophy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerService {

    @Autowired
    PlayerRepository playerRepository;

    String name;
    Player player;

    // stwórz gracza
    public void createPlayer(Trophy trophy, String name) {
        player = new Player();
        player.setPoints(0);
        player.setTrophy(trophy);
        player.setName(name);
        playerRepository.save(player);
    }

    // generuj graczy losowych, aby było 32 zawodników
    public void generateRandomPlayers(Trophy trophy) {
        int numberOfPlayers = playerRepository.findAllByTrophy(trophy).size();
        for (int i = numberOfPlayers; i <= 31; i++) {
            String name = "Player " + numberOfPlayers++;
            createPlayer(trophy, name);
        }
    }

    // przypisz zawodników do grupy
    public void addPlayersToDivisions(Trophy trophy) {
        Player player;
        List<Player> players = playerRepository.findAllByTrophy(trophy);
        List<Player> playersInDivision;
        List<Division> divisions = Arrays.asList(Division.values());
        Iterator<Division> divisionIterator = divisions.iterator();
        while (divisionIterator.hasNext()) {
            Division division = divisionIterator.next();
            playersInDivision = playerRepository.findAllByTrophyAndDivision(trophy, division);
            int size = playersInDivision.size();
            while (size < 4) {
                Random random = new Random();
                player = players.get(random.nextInt(players.size() - 1 + 1) );
                player.setDivision(division);
                playerRepository.save(player);
                players.remove(player);
                players = players.parallelStream()
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                size++;
            }
        }
    }

}
