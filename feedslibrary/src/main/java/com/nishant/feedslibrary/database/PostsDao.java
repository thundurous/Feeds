package com.nishant.feedslibrary.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.nishant.feedslibrary.model.Posts;

import java.util.List;

@Dao
public interface PostsDao {

    @Query("Select * from feeds")
    List<Posts> getAllPosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertPost(Posts post);

    @Update
    void updatePost(Posts post);

    @Delete
    void deletePost(Posts post);
}
