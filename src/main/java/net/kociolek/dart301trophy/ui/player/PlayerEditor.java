package net.kociolek.dart301trophy.ui.player;

import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import net.kociolek.dart301trophy.domain.player.Player;
import net.kociolek.dart301trophy.domain.player.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;

@SpringComponent
@UIScope
public class PlayerEditor extends VerticalLayout {

    TextField name = new TextField("Nazwa zawodnika");

    Button save = new Button("Zapisz", FontAwesome.SAVE);
    Button delete = new Button("Usuń", FontAwesome.REMOVE);
    Button cancel = new Button("Anuluj");
    CssLayout actions = new CssLayout(save, delete, cancel);
    Binder<Player> binder = new Binder<>(Player.class);

    @Autowired
    private PlayerRepository playerRepository;
    private Player player;


    @Autowired
    public PlayerEditor(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;

        HorizontalLayout row = new HorizontalLayout(name);
        Label validationStatus = new Label();
        addComponents(row, actions, validationStatus);

        binder.forField(name)
                .asRequired("Pola oznaczone gwiazdką są wymagane")
                .withValidationStatusHandler(status -> {
                    validationStatus.setValue(status.getMessage().orElse(""));
                    validationStatus.setVisible(status.isError());
                }).bind(Player::getName, Player::setName);

        setSpacing(true);
        actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        save.setStyleName(ValoTheme.BUTTON_PRIMARY);
        save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

        save.addClickListener(e -> {
            playerRepository.save(player);
            getUI().getNavigator().navigateTo("PlayerView");
        });

        delete.addClickListener(e -> {
            playerRepository.delete(player);
            getUI().getNavigator().navigateTo("PlayerView");
        });

        cancel.addClickListener(e -> editMember(player));
        setVisible(false);
    }

    public final void editMember(Player p) {
        if (p == null) {
            setVisible(false);
            return;
        }
        final boolean persisted = p.getId() != null;
        if (persisted) {
            player = playerRepository.findById(p.getId()).orElse(null);
        } else {
            player = p;
        }
        cancel.setVisible(persisted);
        binder.setBean(player);

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
