package com.sz.library.pojo;

import org.litepal.crud.DataSupport;

import java.util.Date;

public class Borrow extends DataSupport {
    private int id;
    private int bookId;
    private int userId;
    private boolean isReturnBack;
    private int promiseDays;
    private Date borrowDay;
    private Date returnDay;

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

    public int getPromiseDays() {
        return promiseDays;
    }

    public void setPromiseDays(int promiseDays) {
        this.promiseDays = promiseDays;
    }

    public Date getBorrowDay() {
        return borrowDay;
    }

    public void setBorrowDay(Date borrowDay) {
        this.borrowDay = borrowDay;
    }

    public Date getReturnDay() {
        return returnDay;
    }

    public void setReturnDay(Date returnDay) {
        this.returnDay = returnDay;
    }

    @Override
    public String toString() {
        return "Borrow{" +
                "id=" + id +
                ", bookId=" + bookId +
                ", userId=" + userId +
                ", isReturnBack=" + isReturnBack +
                ", promiseDays=" + promiseDays +
                ", borrowDay=" + borrowDay +
                ", returnDay=" + returnDay +
                '}';
    }
}
