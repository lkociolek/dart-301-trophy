//package net.kociolek.dart301trophy.ui;
//
//import com.vaadin.navigator.View;
//import com.vaadin.navigator.ViewChangeListener;
//import com.vaadin.spring.annotation.SpringView;
//import com.vaadin.ui.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringApplication;
//import org.springframework.context.ApplicationContext;
//
//import javax.annotation.PostConstruct;
//
//@SpringView(name = Menu.VIEW_NAME)
//public class Menu extends VerticalLayout implements View {
//
//    public static final String VIEW_NAME = "Menu";
//
//    private MenuBar menuBar = new MenuBar();
//    private MenuBar.MenuItem trophies;
//    private MenuBar.MenuItem players;
//    private MenuBar.MenuItem games;
//    private Button closeButton;
//
//
//    @Autowired
//    private ApplicationContext ctx;
//
//    public Menu() {
//
//        trophies = menuBar.addItem("Zawody", e -> getUI().getNavigator().navigateTo("TrophiesView"));
//        players = menuBar.addItem("Administracja");
//        games = administration.addItem("Funkcjonariusze", e -> getUI().getNavigator().navigateTo("MemberView"));
//        weaponList = administration.addItem("Dodaj/usuń broń", e -> getUI().getNavigator().navigateTo("WeaponView"));
//        assignWeapon = administration.addItem("Przypisz broń", e -> getUI().getNavigator().navigateTo("AssignWeaponView"));
//        department = administration.addItem("Komórki organizacyjne", e -> getUI().getNavigator().navigateTo("DepartmentView"));
//        reports = menuBar.addItem("Raporty");
//        assignedWeapon = reports.addItem("Przypisana broń", e -> getUI().getNavigator().navigateTo("AssignedWeaponView"));
//        membersReport = reports.addItem("Zestawienia personale", e -> getUI().getNavigator().navigateTo("ReportView"));
//        closeButton = new Button("X");
//    }
//
//    @PostConstruct
//    protected void init() throws NoSuchFieldException {
//
////        membersReport.setEnabled(false);
//        closeButton.addClickListener(e -> SpringApplication.exit(ctx, () -> 0));
//        HorizontalLayout layout = new HorizontalLayout(menuBar);
//        layout.setWidth(100, Unit.PERCENTAGE);
//        layout.setComponentAlignment(menuBar, Alignment.MIDDLE_LEFT);
////        layout.setComponentAlignment(closeButton, Alignment.MIDDLE_RIGHT);
//        addComponent(layout);
//        setMargin(false);
//        setSpacing(true);
//
//    }
//
//    @Override
//    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
//    }
//
//}