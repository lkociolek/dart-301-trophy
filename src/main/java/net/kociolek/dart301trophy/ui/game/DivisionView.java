package net.kociolek.dart301trophy.ui.game;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import net.kociolek.dart301trophy.domain.game.Game;
import net.kociolek.dart301trophy.domain.game.GameRepository;
import net.kociolek.dart301trophy.domain.player.Player;
import net.kociolek.dart301trophy.domain.player.PlayerRepository;
import net.kociolek.dart301trophy.domain.trophy.Trophy;
import net.kociolek.dart301trophy.domain.trophy.TrophyRepository;
import net.kociolek.dart301trophy.service.GameService;
import net.kociolek.dart301trophy.ui.trophy.TrophiesEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringView(name = DivisionView.VIEW_NAME)
public class DivisionView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "DivisionView";
    static String trophyId = VaadinService.getCurrentRequest().getAttribute("trophyId").toString();
    private final Grid<Game> grid;
    Trophy trophy;

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    GameService gameService;
    @Autowired
    TrophyRepository trophyRepository;

    @Autowired
    public DivisionView(PlayerRepository playerRepository, GameRepository gameRepository, GameService gameService, TrophyRepository trophyRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.gameService = gameService;
        this.trophyRepository = trophyRepository;
        this.grid = new Grid<>(Game.class);

    }

    @PostConstruct
    protected void init() {
        Page.getCurrent().setTitle("Dart 301 Trophy");
        trophy = trophyRepository.findById(trophyId).orElse(null);

        Label title = new Label("<h1 style=\"font-weight: bold\">" +trophy.getName()+ "</h1>", ContentMode.HTML);
        List<Game> games = gameRepository.findAllByTrophy(trophy);

        VerticalLayout mainLayout = new VerticalLayout(title, grid);
        mainLayout.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        addComponent(mainLayout);
        mainLayout.setMargin(false);

        grid.setHeight(300, Unit.PIXELS);
        grid.setWidth(100, Unit.PERCENTAGE);
        grid.setColumns("playerOne", "playerTwo");
        // GRUPA
//        grid.addComponentColumn(trophy -> gameRepository.findByPlayerOne(playerRepository.find(trophy.getPlayerOne()));

//        }).setCaption("Punkty").setId("playerOneScore");
        grid.getColumn("playerOne").setCaption("Gracz 1");
        grid.addComponentColumn(trophy -> {
            TextField playerOneScore= new TextField();
            return playerOneScore;
        }).setCaption("Punkty").setId("playerOneScore");
        grid.addComponentColumn(trophy -> {
            TextField playerTwoScore= new TextField();
            return playerTwoScore;
        }).setCaption("Punkty").setId("playerTwoScore");
        grid.getColumn("playerTwo").setCaption("Gracz 2");
        grid.setColumnOrder("playerOne", "playerOneScore", "playerTwoScore", "playerTwo");
        grid.setBodyRowHeight(40);
        grid.setData(games);
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}