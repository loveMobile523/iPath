package map.dev.ipath.model;

/**
 * Created by adrian on 03.04.2017.
 */

public class DBRate {

    private long id;
    private String username;
    private String place_id;
    private String place_name;
    private String content;
    private String value;
    private String updated;   // 1: from Remote DB,    0: to Remote DB

    public DBRate() {

    }

    public DBRate(String username, String place_id, String place_name, String content, String value, String updated) {
        this.username = username;
        this.place_id = place_id;
        this.place_name = place_name;
        this.content = content;
        this.value = value;
        this.updated = updated;
    }

    public DBRate(long id, String username, String place_id, String place_name, String content, String value, String updated) {
        this.id = id;
        this.username = username;
        this.place_id = place_id;
        this.place_name = place_name;
        this.content = content;
        this.value = value;
        this.updated = updated;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdated() {
        return updated;
    }
}
