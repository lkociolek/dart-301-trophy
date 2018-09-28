package net.kociolek.dart301trophy.ui.trophy;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import net.kociolek.dart301trophy.domain.trophy.Trophy;
import net.kociolek.dart301trophy.domain.trophy.TrophyRepository;
import net.kociolek.dart301trophy.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

@SpringView(name = TrophiesView.VIEW_NAME)
public class TrophiesView extends VerticalLayout implements View {

    public static final String VIEW_NAME = "TrophiesView";
    private final Grid<Trophy> grid;
    private final TextField filter;
    private final TrophiesEditor trophiesEditor;
    private final Button addNewBtn;

    @Autowired
    TrophyRepository trophyRepository;
    @Autowired
    GameService gameService;

    @Autowired
    public TrophiesView(TrophyRepository trophyRepository, TrophiesEditor trophiesEditor, GameService gameService) {
        this.trophyRepository = trophyRepository;
        this.trophiesEditor = trophiesEditor;
        this.gameService = gameService;
        this.grid = new Grid<>(Trophy.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("Dodaj wydarzenie");
    }

    @PostConstruct
    protected void init() {
        Page.getCurrent().setTitle("Lista zawodów");

        Label title = new Label("<h1 style=\"font-weight: bold\">Lista zawodów</h1>", ContentMode.HTML);

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        actions.setComponentAlignment(addNewBtn, Alignment.MIDDLE_RIGHT);
        actions.setWidth(100, Unit.PERCENTAGE);
        VerticalLayout mainLayout = new VerticalLayout(title, actions, grid, trophiesEditor);
        mainLayout.setComponentAlignment(title, Alignment.MIDDLE_CENTER);
        addComponent(mainLayout);
        mainLayout.setMargin(false);

        addNewBtn.addClickListener(c -> trophiesEditor.editCelebration(new Trophy()));

        filter.setPlaceholder("Nazwa wydarzenia");
        filter.setValueChangeMode(ValueChangeMode.LAZY);
        filter.addValueChangeListener(c -> listTrophies(c.getValue()));

        grid.setHeight(300, Unit.PIXELS);
        grid.setWidth(100, Unit.PERCENTAGE);
        grid.setColumns("name");
        grid.getColumn("name").setCaption("Nazwa wydarzenia");
        grid.setColumnOrder("name");
        grid.addComponentColumn(trophy -> {
            Button button = new Button("Dodaj zawodników");
            button.addClickListener(click -> {
                VaadinService.getCurrentRequest().setAttribute("trophyId", trophy.getId());
                getUI().getNavigator().navigateTo("PlayerView");
            });
            return button;
        }).setCaption("Zawodnicy");
        grid.addComponentColumn(trophy -> {
            Button button = new Button("Rozpocznij zawody");
            button.addClickListener(click -> {
                VaadinService.getCurrentRequest().setAttribute("trophyId", trophy.getId());
                gameService.generateDivisionGames(trophy);
                getUI().getNavigator().navigateTo("DivisionView");
            });
            return button;
        }).setCaption("Zawodnicy");
            grid.setBodyRowHeight(40);
        grid.asSingleSelect().addValueChangeListener(event -> {
            try {
                trophiesEditor.editCelebration(trophyRepository.findById(event.getValue().getId()).orElse(null));
            } catch (NullPointerException ex) {

            }
        });

        trophiesEditor.setMargin(false);
        trophiesEditor.setChangeHandler(() -> {
            trophiesEditor.setVisible(false);
            listTrophies(filter.getValue());
        });

        listTrophies(null);

    }

    void listTrophies(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            grid.setItems(trophyRepository.findAll());
        } else {
            grid.setItems(trophyRepository.findByName(filterText));
        }
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}