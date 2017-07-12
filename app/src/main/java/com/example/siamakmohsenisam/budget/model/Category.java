package com.example.siamakmohsenisam.budget.model;

/**
 * Created by siamakmohsenisam on 2017-07-05.
 */

public class Category {

    private String categoryName;

    public Category() {
        this("" , 0.0);
    }

    public Category(String categoryName, double limited) {
        this.categoryName = categoryName;
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

    @Override
    public String toString() {
        return categoryName ;
    }
}
