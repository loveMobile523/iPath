package map.dev.ipath.model;

/**
 * Created by adrian on 03.04.2017.
 */

public class DBFavorite {
    private long id;
    private String place_id;
    private String placename;
    private String updated;     // 1: from Remote DB,    0: to Remote DB

    public DBFavorite() {

    }

    public DBFavorite(String place_id, String placename, String updated) {
        this.place_id = place_id;
        this.placename = placename;
        this.updated = updated;
    }

    public DBFavorite(long id, String place_id, String placename, String updated) {
        this.id = id;
        this.place_id = place_id;
        this.placename = placename;
        this.updated = updated;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public String getPlacename() {
        return placename;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdated() {
        return updated;
    }
}
