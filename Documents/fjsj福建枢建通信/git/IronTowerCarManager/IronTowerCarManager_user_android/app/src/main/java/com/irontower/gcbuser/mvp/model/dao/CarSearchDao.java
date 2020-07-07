package com.irontower.gcbuser.mvp.model.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import com.irontower.gcbuser.mvp.model.entity.CarSearch;
import io.reactivex.Single;
import java.util.List;

@Dao
public interface CarSearchDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertHistory(CarSearch carSearch);


  @Query("SELECT * FROM car_search WHERE  userId == :userId  ORDER BY id DESC")
  Single<List<CarSearch>> loadHistory(String userId);
}
