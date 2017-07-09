package com.example.siamakmohsenisam.budget.model;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by siamakmohsenisam on 2017-07-06.
 */

public class ListManager {
    
    private static final ListManager ourInstance = new ListManager();

    public static ListManager getInstance() {
        return ourInstance;
    }

    private ListManager() {
    }

    private ArrayList<Account> listOfAccount;
    private ArrayList<Category> listOfCategory;
    private ArrayList<Budget> listOfBudget;

    public ArrayList<Account> getListOfAccount() {
        return listOfAccount;
    }

    public void setListOfAccount(ArrayList<Account> listOfAccount) {
        this.listOfAccount = listOfAccount;
    }

    public ArrayList<Category> getListOfCategory() {
        return listOfCategory;
    }

    public void setListOfCategory(ArrayList<Category> listOfCategory) {
        this.listOfCategory = listOfCategory;
    }

    public ArrayList<Budget> getListOfBudget() {
        return listOfBudget;
    }

    public void setListOfBudget(ArrayList<Budget> listOfBudget) {
        this.listOfBudget = listOfBudget;
    }

    /**
     *
     * @param account , category , budget
     * @return Boolean
     *
     * Add to our lists
     */

    public Boolean add(Account account){
        if (getAccount(account.getAccountName())!= null)
            listOfAccount.remove(account);
        return listOfAccount.add(account);
    }

    public Boolean add(Category category){
        if (getCategory(category.getCategoryName())!= null)
            listOfCategory.remove(category);
        return listOfCategory.add(category);
    }

    public Boolean add(Budget budget){
        return listOfBudget.add(budget);
    }

    /**
     *
     * @param index
     * @return
     *
     * remove from lists
     */
    public Account removeAccount(int index){
        return listOfAccount.remove(index);
    }

    public Category removeCategory(int index){
        return listOfCategory.remove(index);
    }

    public Budget removeBudget(int index){
        return listOfBudget.remove(index);
    }

    /**
     *
     * @param index
     * @param account , category , budget
     * @return Boolean
     *
     * edit lists
     *
     */
    public Boolean edit(int index, Account account){
        listOfAccount.remove(index);
        return listOfAccount.add(account);
    }

    public Boolean edit(int index, Category category){
        listOfCategory.remove(index);
        return listOfCategory.add(category);
    }

    public Boolean edit(int index, Budget budget){
        listOfBudget.remove(index);
        return listOfBudget.add(budget);
    }

    /**
     *
     * @param index
     * @return
     * get Account , Category or budget
     *
     */
    public Account getAccount(int index){
        return listOfAccount.get(index);
    }

    public Category getCategory(int index){
        return listOfCategory.get(index);
    }

    public Budget getBudget(int index){
        return listOfBudget.get(index);
    }

    public Account getAccount(String name){
        for (Account account: listOfAccount)
            if (account.getAccountName().equals(name))
                return account;
        return null;
    }

    public Category getCategory(String name){
        for (Category category: listOfCategory)
            if (category.getCategoryName().equals(name))
                return category;
        return null;
    }

    public ArrayList<Budget> getBudgets(String nameAccount, String nameCategory, Calendar from , Calendar to){

        ArrayList<Budget> budgets = new ArrayList<Budget>();

        for (Budget budget: listOfBudget){
            if ((budget.getAccount().getAccountName().equals(nameAccount)|| nameAccount.equals(""))
                    &&(budget.getCategory().getCategoryName().equals(nameCategory)|| nameCategory.equals(""))
                    && (budget.getDate().after(from) && budget.getDate().before(to))){

                budgets.add(budget);
            }
        }
        return budgets;
    }


}


