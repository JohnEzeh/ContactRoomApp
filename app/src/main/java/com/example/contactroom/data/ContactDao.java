package com.example.contactroom.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.contactroom.model.Contact;

import java.util.List;

@Dao
public interface ContactDao {
    //CRUD
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Contact contact);

    @Query("DELETE FROM contact_table")
    void delectAll();

    @Query("SELECT * FROM CONTACT_TABLE ORDER BY name ASC ")
    LiveData<List<Contact>> getAllContacts();

    @Query("SELECT * FROM CONTACT_TABLE WHERE contact_table.id == :id")
    LiveData<Contact> get(int id);

    @Update
    void update(Contact contact);

    @Delete
    void delete(Contact contact);

}
