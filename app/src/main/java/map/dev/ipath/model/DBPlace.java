package map.dev.ipath.model;

/**
 * Created by adrian on 29.03.2017.
 */

public class DBPlace {

    private long id;
    private String place_id;
    private String name;
    private String category_name;
    private String latitude;
    private String longitude;
    private String phone;
    private String email;
    private String address;
    private String rating;
    private String updated;     // 1: from Remote DB,    0: to Remote DB

    public DBPlace() {

    }

    public DBPlace(String name, String latitude, String longitude, String address) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;

        this.category_name = "";
        this.phone = "";
        this.email = "";
        this.rating = "";
        this.updated = "1";
    }

    public DBPlace(String name, String category_name, String latitude, String longitude,
                   String phone, String email, String address, String rating, String updated) {
        this.place_id = "";
        this.name = name;
        this.category_name = category_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.rating = rating;

        this.updated = updated;
    }

    public DBPlace(String place_id, String name, String category_name, String latitude, String longitude,
                   String phone, String email, String address, String rating, String updated) {
        this.place_id = place_id;
        this.name = name;
        this.category_name = category_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.rating = rating;

        this.updated = updated;
    }

    public DBPlace(long id, String place_id, String name, String category_name, String latitude, String longitude,
                   String phone, String email, String address, String rating, String updated) {
        this.id = id;
        this.place_id = place_id;
        this.name = name;
        this.category_name = category_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.rating = rating;

        this.updated = updated;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUpdated() {
        return updated;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return name;
    }
}
