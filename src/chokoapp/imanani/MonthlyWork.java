package chokoapp.imanani;

public class MonthlyWork {

    private String code;
    private String description;
    private long duration;
    private int percent;

    public MonthlyWork(String code, String description, long duration){
        this.code = code;
        this.description = description;
        this.duration = duration;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
