package net.kociolek.dart301trophy.domain.player;

public enum Division {

    DIVISION1("Grupa A"),
    DIVISION2("Grupa B"),
    DIVISION3("Grupa C"),
    DIVISION4("Grupa D"),
    DIVISION5("Grupa E"),
    DIVISION6("Grupa F"),
    DIVISION7("Grupa G"),
    DIVISION8("Grupa H");

    private String division;

    Division(String division) {
        this.division = division;
    }

    public String getDivision() {
        return division;
    }
}
