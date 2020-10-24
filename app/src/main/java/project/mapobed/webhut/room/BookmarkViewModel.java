package project.mapobed.webhut.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class BookmarkViewModel extends AndroidViewModel {
    private BookmarkRepository repository;
    private LiveData<List<Bookmark>> allBookmark;

    public BookmarkViewModel(@NonNull Application application) {
        super(application);
        repository = new BookmarkRepository(application);
        allBookmark = repository.getAllBookmark();
    }

    public void insert(Bookmark bookmark) {
        repository.insert(bookmark);
    }

    public void update(Bookmark bookmark) {
        repository.update(bookmark);
    }

    public void delete(Bookmark bookmark) {
        repository.delete(bookmark);
    }

    public void deleteAllBookmarks() {
        repository.deleteAllBookmarks();
    }

    public LiveData<List<Bookmark>> getAllBookmark() {
        return allBookmark;
    }
}
