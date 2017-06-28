package map.dev.ipath.model;

/**
 * Created by adrian on 12.03.2017.
 */

public class ReviewModel {

    private String userName;
    private float ratingValue;
    private String reviewContent;

    public ReviewModel() {
        this.userName = "";
        this.ratingValue = 0f;
        this.reviewContent = "";
    }

    public ReviewModel(String userName, float ratingValue, String reviewContent) {
        this.userName = userName;
        this.ratingValue = ratingValue;
        this.reviewContent = reviewContent;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setRatingValue(float ratingValue) {
        this.ratingValue = ratingValue;
    }

    public float getRatingValue() {
        return ratingValue;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public String getReviewContent() {
        return reviewContent;
    }
}
