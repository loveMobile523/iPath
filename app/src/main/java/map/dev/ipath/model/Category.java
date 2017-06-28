package map.dev.ipath.model;

/**
 * Created by adrian on 12.03.2017.
 */

public class Category {

    private int categoryNumber;
    private String title;
    private boolean judgeSelected;

    public Category() {
        this.categoryNumber = 0;
        this.title = "Community Center";
        this.judgeSelected = false;
    }

    public Category(int categoryNumber, String title, boolean judgeSelected) {
        this.categoryNumber = categoryNumber;
        this.title = title;
        this.judgeSelected = judgeSelected;
    }

    public void setCategoryNumber(int categoryNumber) {
        this.categoryNumber = categoryNumber;
    }

    public int getCategoryNumber() {
        return categoryNumber;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setJudgeSelected(boolean judgeSelected) {
        this.judgeSelected = judgeSelected;
    }

    public boolean isJudgeSelected() {
        return judgeSelected;
    }
}
