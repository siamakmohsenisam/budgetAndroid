package com.example.siamakmohsenisam.budget.model;

/**
 * Created by siamakmohsenisam on 2017-07-05.
 */

public class Category {

    private String categoryName;
    private double limited;

    public Category() {
        this("" , 0.0);
    }

    public Category(String categoryName, double limited) {
        this.categoryName = categoryName;
        this.limited = limited;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        if (categoryName.matches(MyPattern.Word.getMyPattern())) {
            categoryName = categoryName.substring(0,1).toUpperCase()+ categoryName.substring(1).toLowerCase();
            this.categoryName = categoryName;
        }
        else throw new IllegalArgumentException("category name is not correct");
    }

    public double getLimited() {
        return limited;
    }

    public void setLimited(double limited) {
        if (String.valueOf(limited).matches(MyPattern.DecimalNumber.getMyPattern()))
            this.limited = limited;
        else throw new IllegalArgumentException("limited is not correct");
    }

    @Override
    public String toString() {
        return categoryName +" : " + limited ;
    }
}
