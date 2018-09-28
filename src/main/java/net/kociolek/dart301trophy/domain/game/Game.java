package net.kociolek.dart301trophy.domain.game;

import lombok.Data;
import net.kociolek.dart301trophy.domain.trophy.Trophy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
public class Game {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "uuid2")
    private String id;
    private String playerOne;
    private String playerTwo;
    private int playerOneScore;
    private int playerTwoScore;
    @ManyToOne
    @JoinColumn(name = "trophy_id")
    private Trophy trophy;
}
