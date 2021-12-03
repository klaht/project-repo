package sample;

public class DataPassing {
    private Integer startTimeSeconds = 0;
    private Integer startTimeMinutes = 0;
    private Integer startTimeHours = 1;

    private Integer originalSecond = startTimeSeconds;
    private Integer originalMinutes = startTimeMinutes;
    private Integer originalHours = startTimeHours;


    private final static DataPassing INSTANCE = new DataPassing();

    public static DataPassing getInstance() {
        return INSTANCE;
    }


    public Integer getOriginalSecond() {
        return originalSecond;
    }

    public void setOriginalSecond(Integer originalSecond) {
        this.originalSecond = originalSecond;
    }

    public Integer getStartTimeSeconds() {
        return startTimeSeconds;
    }

    public void setStartTimeSeconds(Integer startTimeSeconds) {
        this.startTimeSeconds = startTimeSeconds;
    }

    public Integer getStartTimeMinutes() {
        return startTimeMinutes;
    }

    public void setStartTimeMinutes(Integer startTimeMinutes) {
        this.startTimeMinutes = startTimeMinutes;
    }


    public Integer getOriginalMinutes() {
        return originalMinutes;
    }

    public void setOriginalMinutes(Integer originalMinutes) {
        this.originalMinutes = originalMinutes;
    }

    public void setStartTimeHours(Integer startTimeHours) {
        this.startTimeHours = startTimeHours;
    }

    public Integer getStartTimeHours() {
        return startTimeHours;
    }

    public Integer getOriginalHours() {
        return originalHours;
    }

    public void setOriginalHours(Integer originalHours) {
        this.originalHours = originalHours;
    }

}
