package net.kociolek.dart301trophy.domain.trophy;

import lombok.Data;
import net.kociolek.dart301trophy.domain.game.Game;
import net.kociolek.dart301trophy.domain.player.Player;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Trophy {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "uuid2")
    private String id;

    private String name;

    @OneToMany(mappedBy = "trophy", cascade = CascadeType.ALL)
    private List<Game> games;

    @OneToMany(mappedBy = "trophy", cascade = CascadeType.ALL)
    private List<Player> players;

}
