package net.kociolek.dart301trophy.ui.player;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import net.kociolek.dart301trophy.domain.player.Player;
import net.kociolek.dart301trophy.domain.player.PlayerRepository;
import net.kociolek.dart301trophy.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@SpringView(name = PlayerView.VIEW_NAME)
public class PlayerView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "PlayerView";
    private final Grid<Player> grid;
    private final TextField filter;
    private final PlayerEditor playerEditor;
    private final Button addNewBtn;
    @Autowired
    private final PlayerRepository playerRepository;
    @Autowired
    private final PlayerService playerService;

    @Autowired
    public PlayerView(PlayerRepository playerRepository,
                      PlayerService playerService,
                      PlayerEditor playerEditor) {
        this.playerRepository = playerRepository;
        this.grid = new Grid<>(Player.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Dodaj zawodnika");
        this.playerEditor = playerEditor;
        this.playerService = playerService;
    }

    @PostConstruct
    protected void init() {
        Page.getCurrent().setTitle("Dart 301 Trophy");

        Label title = new Label("<h1 style=\"font-weight: bold\">Lista zawodników</h1>", ContentMode.HTML);

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        actions.setComponentAlignment(addNewBtn, Alignment.MIDDLE_RIGHT);
        actions.setWidth(100, Unit.PERCENTAGE);
        VerticalLayout mainLayout = new VerticalLayout(title, actions, grid, playerEditor);
        mainLayout.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        addComponent(mainLayout);
        mainLayout.setMargin(false);

        addNewBtn.addClickListener(e -> playerEditor.editMember(new Player()));

        filter.setPlaceholder("Nazwa zawodnika");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(e -> listPlayers(e.getValue()));

        grid.setHeight(300, Unit.PIXELS);
        grid.setWidth(100, Unit.PERCENTAGE);
        grid.setColumns("name");
        grid.getColumn("Nazwa zawodnika");
        grid.setColumnOrder("name");
        grid.asSingleSelect().addValueChangeListener(member -> {
            try {
                playerEditor.editMember(playerRepository.findById(member.getValue().getId()).orElse(null));
            } catch (NullPointerException ex) {

            }
        });

        playerEditor.setMargin(false);
        playerEditor.setChangeHandler(() -> {
            playerEditor.setVisible(false);

        });
        listPlayers(null);

    }

    void listPlayers(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(playerRepository.findAll());
        } else {
            grid.setItems(playerRepository.findAllByNameContainingIgnoreCase(filterText));
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}