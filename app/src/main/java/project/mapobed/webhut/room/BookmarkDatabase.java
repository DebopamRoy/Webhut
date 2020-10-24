package project.mapobed.webhut.room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Bookmark.class}, version = 1)
public abstract class BookmarkDatabase extends RoomDatabase {

    private static BookmarkDatabase instance;

    public abstract BookmarkDao bookmarkDao();

    public static synchronized BookmarkDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), BookmarkDatabase.class, "bookmark_table")
                    .fallbackToDestructiveMigration()
                    .addCallback(callback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDB(instance).execute();
        }
    };

    private static class PopulateDB extends AsyncTask<Void, Void, Void> {
        private BookmarkDao bookmarkDao;

        public PopulateDB(BookmarkDatabase bookmark_Database) {
            bookmarkDao = bookmark_Database.bookmarkDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;

        }
    }
}
