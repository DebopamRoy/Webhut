package project.mapobed.webhut.room;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class BookmarkRepository {
    private BookmarkDao bookmarkDao;
    private LiveData<List<Bookmark>> allBookmark;

    public BookmarkRepository(Application application) {
        BookmarkDatabase database = BookmarkDatabase.getInstance(application);
        bookmarkDao = database.bookmarkDao();
        allBookmark = bookmarkDao.getAllBookmarks();
    }

    public void insert(Bookmark bookmark) {
        new InsertBookmarkAsync(bookmarkDao).execute(bookmark);

    }

    public void update(Bookmark bookmark) {
        new UpdateBookmarkAsync(bookmarkDao).execute(bookmark);
    }

    public void delete(Bookmark bookmark) {
        new DeleteBookmarkAsync(bookmarkDao).execute(bookmark);

    }

    public void deleteAllBookmarks() {
        new DeleteAllBookmarkAsync(bookmarkDao).execute();

    }

    public LiveData<List<Bookmark>> getAllBookmark() {
        return allBookmark;
    }

    public static class InsertBookmarkAsync extends AsyncTask<Bookmark, Void, Void> {
        private BookmarkDao bookmarkDao;

        public InsertBookmarkAsync(BookmarkDao bookmarkDao) {
            this.bookmarkDao = bookmarkDao;
        }

        @Override
        protected Void doInBackground(Bookmark... bookmarks) {
            bookmarkDao.insert(bookmarks[0]);
            return null;
        }
    }

    public static class UpdateBookmarkAsync extends AsyncTask<Bookmark, Void, Void> {
        private BookmarkDao bookmarkDao;

        public UpdateBookmarkAsync(BookmarkDao bookmarkDao) {
            this.bookmarkDao = bookmarkDao;
        }

        @Override
        protected Void doInBackground(Bookmark... bookmark_entities) {
            bookmarkDao.update(bookmark_entities[0]);
            return null;
        }
    }


    public static class DeleteBookmarkAsync extends AsyncTask<Bookmark, Void, Void> {
        private BookmarkDao bookmarkDao;

        public DeleteBookmarkAsync(BookmarkDao bookmarkDao) {
            this.bookmarkDao = bookmarkDao;
        }

        @Override
        protected Void doInBackground(Bookmark... bookmark_entities) {
            bookmarkDao.delete(bookmark_entities[0]);
            return null;
        }
    }
    public static class DeleteAllBookmarkAsync extends AsyncTask<Void, Void, Void> {
        private BookmarkDao bookmarkDao;

        public DeleteAllBookmarkAsync(BookmarkDao bookmarkDao) {
            this.bookmarkDao = bookmarkDao;
        }

        @Override
        protected Void doInBackground(Void...Void) {
            bookmarkDao.deleteAllBookmark();
            return null;
        }
    }
}
