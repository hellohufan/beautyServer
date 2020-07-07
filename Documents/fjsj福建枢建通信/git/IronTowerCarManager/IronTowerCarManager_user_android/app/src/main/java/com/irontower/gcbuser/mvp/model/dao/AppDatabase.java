package com.irontower.gcbuser.mvp.model.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import com.irontower.gcbuser.mvp.model.entity.CarSearch;

@Database(entities = {CarSearch.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {


  public abstract CarSearchDao carSearchDao();

  private static volatile AppDatabase INSTANCE;

  public AppDatabase() {
  }


  public static AppDatabase getInstance(Context context) {
    if (INSTANCE == null) {
      synchronized (AppDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE =
              Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "gcb.db")
                  .allowMainThreadQueries()

                  .build();
        }

      }

    }
    return INSTANCE;
  }


}
