package com.example.contactroom.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.contactroom.data.ContactDao;
import com.example.contactroom.model.Contact;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactRoomDatabase extends RoomDatabase {

    public abstract ContactDao contactDao();
    public static final int NUMBER_OF_THREADS = 4;

    public static volatile ContactRoomDatabase INSTANCE;
    public static final ExecutorService  databaseWriterExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ContactRoomDatabase getDatabase(final  Context context){
        if (INSTANCE == null) {
             synchronized (ContactRoomDatabase.class){
                 if (INSTANCE == null) {
                     INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                             ContactRoomDatabase.class, "contact_database")
                             .addCallback(sRoomDataBaseCallback)
                             .build();
                 }
             }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDataBaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriterExecutor.execute(()->{
                        ContactDao contactDao = INSTANCE.contactDao();
                        contactDao.delectAll();

                        Contact contact = new Contact("John", "Student");
                       contactDao.insert(contact);

                        contact = new Contact("bond", "Spy");
                        contactDao.insert(contact);
                    });
                }
            };
}
