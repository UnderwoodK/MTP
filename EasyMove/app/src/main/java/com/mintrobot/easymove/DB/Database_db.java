package com.mintrobot.easymove.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Object_job.class, version = 3)
public abstract class Database_db extends RoomDatabase {

    private static Database_db INSTANCE;

    public abstract Dao_point dao_point();

    public static Database_db getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized (Database_db.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context, Database_db.class, "MintDB")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}