package com.sz.library.pojo;

import org.litepal.crud.DataSupport;


public class Borrow extends DataSupport {
    private int id;
    private int bookId;
    private int userId;
    private boolean isReturnBack;
    private String promiseDay;
    private String borrowDay;
    private String returnDay;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isReturnBack() {
        return isReturnBack;
    }

    public void setReturnBack(boolean returnBack) {
        isReturnBack = returnBack;
    }

    public String getPromiseDay() {
        return promiseDay;
    }

    public void setPromiseDay(String promiseDay) {
        this.promiseDay = promiseDay;
    }

    public String getBorrowDay() {
        return borrowDay;
    }

    public void setBorrowDay(String borrowDay) {
        this.borrowDay = borrowDay;
    }

    public String getReturnDay() {
        return returnDay;
    }

    public void setReturnDay(String returnDay) {
        this.returnDay = returnDay;
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", userId=" + userId +
                ", isReturnBack=" + isReturnBack +
                ", promiseDay=" + promiseDay +
                ", borrowDay=" + borrowDay +
                ", returnDay=" + returnDay +
                '}';
    }
}
