package net.kociolek.dart301trophy.ui.trophy;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import net.kociolek.dart301trophy.domain.player.PlayerRepository;
import net.kociolek.dart301trophy.domain.trophy.Trophy;
import net.kociolek.dart301trophy.domain.trophy.TrophyRepository;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class TrophiesEditor extends VerticalLayout {

    TextField name = new TextField("Nazwa wydarzenia");
    Button save = new Button("Zapisz", FontAwesome.SAVE);
    Button delete = new Button("Usuń", FontAwesome.REMOVE);
    Button cancel = new Button("Anuluj");
    CssLayout actions = new CssLayout(save, delete, cancel);
    Binder<Trophy> binder = new Binder<>(Trophy.class);
    @Autowired
    private TrophyRepository trophyRepository;
    @Autowired
    private PlayerRepository playerRepository;
    private Trophy trophy;

    @Autowired
    public TrophiesEditor(TrophyRepository trophyRepository, PlayerRepository playerRepository) {
        this.trophyRepository = trophyRepository;
        this.playerRepository = playerRepository;

        HorizontalLayout row = new HorizontalLayout(name);
        Label validationStatus = new Label();
        addComponents(row, actions, validationStatus);

        binder.forField(name)
                .asRequired("Pola oznaczone gwiazdką są wymagane")
                .withValidationStatusHandler(status -> {
                    validationStatus.setValue(status.getMessage().orElse(""));
                    validationStatus.setVisible(status.isError());
                }).bind(Trophy::getName, Trophy::setName);

        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        save.addClickListener(e -> {
            trophyRepository.save(trophy);
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("celebrationId", trophy.getId());
            getUI().getNavigator().navigateTo("CelebrationView");
        });

        delete.addClickListener(e -> {
            playerRepository.deleteAll(playerRepository.findAllByTrophy(trophy));
            trophyRepository.delete(trophy);
        });

        cancel.addClickListener(e -> editCelebration(trophy));
        setVisible(false);
    }

    public final void editCelebration(Trophy t) {
        if (t == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = t.getId() != null;
        if (persisted) {
            trophy = trophyRepository.findById(t.getId()).orElse(null);
        } else {
            trophy = t;
        }

        cancel.setVisible(persisted);
        binder.setBean(trophy);

        setVisible(true);

        save.focus();
        name.selectAll();
    }

    public void setChangeHandler(ChangeHandler h) {
        save.addClickListener(event -> h.onChange());
        delete.addClickListener(event -> h.onChange());
        cancel.addClickListener(event -> h.onChange());
    }

    public interface ChangeHandler {

        void onChange();
    }
}
