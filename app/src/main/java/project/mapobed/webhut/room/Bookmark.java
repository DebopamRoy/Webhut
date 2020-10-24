package project.mapobed.webhut.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookmark_table")
public class Bookmark {
    @PrimaryKey(autoGenerate = true)
    private int book_id;

    private final String book_name;

    private final String book_link;

    public Bookmark(String book_name, String book_link) {
        this.book_name = book_name;
        this.book_link = book_link;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public String getBook_name() {
        return book_name;
    }

    public String getBook_link() {
        return book_link;
    }


}
