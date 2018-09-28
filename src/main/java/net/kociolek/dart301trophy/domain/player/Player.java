package net.kociolek.dart301trophy.domain.player;

import lombok.Data;
import net.kociolek.dart301trophy.domain.game.TrophyFinal;
import net.kociolek.dart301trophy.domain.game.TrophyStageOne;
import net.kociolek.dart301trophy.domain.game.TrophySemifinal;
import net.kociolek.dart301trophy.domain.game.TrophyStageTwo;
import net.kociolek.dart301trophy.domain.trophy.Trophy;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Grupa
 */

@Entity
@Data
public class Player {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "uuid2")
    private String id;
    private String name;
    private Division division;
    private int points;
    private TrophyStageOne trophyStageOne;
    private TrophyStageTwo trophyStageTwo;
    private TrophySemifinal trophySemifinal;
    private TrophyFinal trophyFinal;

    @OneToOne
    private Trophy trophy;
}
