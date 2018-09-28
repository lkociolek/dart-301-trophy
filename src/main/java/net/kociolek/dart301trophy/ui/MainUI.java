package net.kociolek.dart301trophy.ui;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import net.kociolek.dart301trophy.ui.trophy.TrophiesView;

@Theme("valo")
@SpringUI
@SpringViewDisplay
public class MainUI extends UI implements ViewDisplay {

    private Panel springViewDisplay;

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        final VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        setContent(root);

        final CssLayout navigationBar = new CssLayout();
        navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);

        springViewDisplay = new Panel();
        springViewDisplay.setSizeFull();
        root.addComponent(springViewDisplay);
        root.setExpandRatio(springViewDisplay, 1.0f);
        root.getUI().getNavigator().navigateTo(TrophiesView.VIEW_NAME);

    }

    @Override
    public void showView(View view) {
        springViewDisplay.setContent((Component) view);
    }

}
