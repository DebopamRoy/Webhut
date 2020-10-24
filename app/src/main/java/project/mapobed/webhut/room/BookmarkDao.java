package project.mapobed.webhut.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BookmarkDao {

    @Insert
    void insert(Bookmark bookmark);

    @Update
    void update(Bookmark bookmark);

    @Delete
    void delete(Bookmark bookmark);

    @Query("DELETE FROM bookmark_table")
    void deleteAllBookmark();

    @Query("SELECT * FROM bookmark_table ORDER BY book_name ASC")
    LiveData<List<Bookmark>> getAllBookmarks();
}

