package com.example.rssreader.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.rssreader.data.dto.database.FeedDB;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {FeedDB.class}, version = 2, exportSchema = false)
public abstract class FeedsDatabase extends RoomDatabase {

    public abstract FeedDao feedDao();

    private static volatile FeedsDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static FeedsDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FeedsDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FeedsDatabase.class, "feeds_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
