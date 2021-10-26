package kz.madina.chemistrydictionary;

public class Term {

    private Definition frst;
    private Definition scnd;
    private int id;
    private int bookmarked;

    public Term(int id, int bookmarked, Definition frst, Definition scnd) {
        this.frst = frst;
        this.scnd = scnd;
        this.id = id;
        this.bookmarked = bookmarked;
    }

    public Definition getFrst() {
        return frst;
    }

    public void setFrst(Definition frst) {
        this.frst = frst;
    }

    public Definition getScnd() {
        return scnd;
    }

    public void setScnd(Definition scnd) {
        this.scnd = scnd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(int bookmarked) {
        this.bookmarked = bookmarked;
    }

}
