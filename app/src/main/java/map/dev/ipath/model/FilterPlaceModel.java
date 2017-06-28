package map.dev.ipath.model;

/**
 * Created by adrian on 11.03.2017.
 */

public class FilterPlaceModel {

    private int category;
    private String title;
    private float ratingValue;
    private String distanceDescription;
    private float distanceValue;

    public FilterPlaceModel(){
        this.category = 0;
        this.title = "";
        this.ratingValue = 0;
        this.distanceDescription = "";
        this.distanceValue = 0;
    }

    public FilterPlaceModel(int category,
                            String title,
                            float ratingValue,
                            String distanceDescription,
                            float distanceValue) {
        this.category = category;
        this.title = title;
        this.ratingValue = ratingValue;
        this.distanceDescription = distanceDescription;
        this.distanceValue = distanceValue;
    }

    public FilterPlaceModel(int category,
                            String title,
                            float ratingValue,
                            String distanceDescription) {
        this.category = category;
        this.title = title;
        this.ratingValue = ratingValue;
        this.distanceDescription = distanceDescription;
        this.distanceValue = (float) 0;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getCategory() {
        return category;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setRatingValue(float ratingValue) {
        this.ratingValue = ratingValue;
    }

    public float getRatingValue() {
        return ratingValue;
    }

    public void setDistanceDescription(String distanceDescription) {
        this.distanceDescription = distanceDescription;
    }

    public String getDistanceDescription() {
        return distanceDescription;
    }

    public void setDistanceValue(float distanceValue) {
        this.distanceValue = distanceValue;
    }

    public float getDistanceValue() {
        return distanceValue;
    }
}
