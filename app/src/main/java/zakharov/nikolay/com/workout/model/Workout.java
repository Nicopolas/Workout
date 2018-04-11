package zakharov.nikolay.com.workout.model;

import java.util.Date;

public class Workout {
    private String title;
    private String description;
    private int recordCount;
    private Date recordDate;

    public Workout() { }

    public Workout(String title, String description, int recordCount, Date record_date) {
        this.title = title;
        this.description = description;
        this.recordCount = recordCount;
        this.recordDate = record_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }
}
