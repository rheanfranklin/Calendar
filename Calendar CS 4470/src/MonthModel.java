package main;

public class MonthModel {

    private final CalModel calModel;

    private final GlassPaneMonth glassPane;

    public MonthModel(CalModel calModel) {
        this.calModel = calModel;
        glassPane = new GlassPaneMonth(calModel);
    }

    public CalModel getCalModel() { return calModel; }

    public GlassPaneMonth getGlassPane() { return glassPane; }
}
