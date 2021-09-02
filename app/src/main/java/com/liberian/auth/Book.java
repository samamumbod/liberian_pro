package com.liberian.auth;

import java.util.Comparator;

public class Book implements Comparator<Book> {

    private long isbn;
    private String booktitle;
    private String author;
    private int publish_year;
    private String book_category;
    private int copies;

    public String getIsbn() {
        return String.valueOf(isbn);
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public String getBooktitle() {
        return booktitle;
    }

    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublish_year() {
        return String.valueOf(publish_year);
    }

    public void setPublish_year(int publish_year) {
        this.publish_year = publish_year;
    }

    public String getBook_category() {
        return book_category;
    }

    public void setBook_category(String book_category) {
        this.book_category = book_category;
    }

    public String getCopies() {
        return String.valueOf(copies);
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }



    @Override
    public int compare(Book o1, Book o2) {
        if (o1.getIsbn().equals(o2.getIsbn()) && o1.getBooktitle().equals(o2.getBooktitle())
            && o1.getAuthor().equals(o2.getAuthor()) && o1.getPublish_year().equals(o2.getPublish_year())
            && o1.getBook_category().equals(o2.getBook_category()) && o1.getCopies().equals(o2.getCopies())){
            return 1;
        }
        else{
            return 0;
        }
    }
}
