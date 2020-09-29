package com.mintrobot.easymove.DB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface Dao_point {

    @Insert
    void insertData(Object_job job);

    @Query("SELECT * FROM jobs")
    List<Object_job> getAllData();

    @Query("DELETE FROM jobs WHERE jobNo = :jobNo")
    void deleteDataByNo(int jobNo);

    @Update
    void updateData(List<Object_job> jobs);

    @Query("DELETE FROM jobs")
    void deleteAllDatas();

}
