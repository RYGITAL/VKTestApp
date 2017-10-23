package com.rygital.vktestapp.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.rygital.vktestapp.data.models.user.User;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class Database {
    private static final String DB_NAME = "vktestdb";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "users";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_FIRST_NAME = "first";
    private static final String COLUMN_LAST_NAME = "last";
    private static final String COLUMN_SEX = "sex";
    private static final String COLUMN_PHOTO50 = "photo50";
    private static final String COLUMN_AMOUNT_OF_FRIENDS = "friends";
    private static final String DB_CREATE =
            "create table " + DB_TABLE + "(" +
                    COLUMN_ID + " integer primary key autoincrement, " +
                    COLUMN_USER_ID + " text, " +
                    COLUMN_FIRST_NAME + " text, " +
                    COLUMN_LAST_NAME + " text, " +
                    COLUMN_SEX + " integer, " +
                    COLUMN_PHOTO50 + " text, " +
                    COLUMN_AMOUNT_OF_FRIENDS + " integer" +
                    ");";

    private final Context mCtx;
    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    @Inject
    Database(Context ctx) {
        mCtx = ctx;
    }

    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    private ContentValues toContentValues(User item) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_USER_ID, item.getId());
        cv.put(COLUMN_FIRST_NAME, item.getFirstName());
        cv.put(COLUMN_LAST_NAME, item.getLastName());
        cv.put(COLUMN_SEX, item.getSex());
        cv.put(COLUMN_PHOTO50, item.getPhoto50());
        cv.put(COLUMN_AMOUNT_OF_FRIENDS, item.getCounters().getFriends());
        return cv;
    }

    public void addItem(User item) {
        mDB.insert(DB_TABLE, null, toContentValues(item));
    }

    public void updateItem(User item) {
        mDB.update(DB_TABLE,
                toContentValues(item),
                COLUMN_USER_ID + " = ?", new String[] {item.getId()});
    }

    public void removeItem(User item) {
        mDB.delete(DB_TABLE,
                COLUMN_USER_ID + " = ?", new String[] {item.getId()});
    }

    public Observable<List<User>> getUsers() {
        Cursor c = mDB.query(DB_TABLE,
                null,
                null, null,
                null, null,
                null);

        if(c.getCount() > 0) c.moveToFirst();
        else return Observable.just(new ArrayList<>());

        List<User> list = new ArrayList<>();
        do {
            list.add(new User(c.getString(c.getColumnIndex(COLUMN_USER_ID)),
                    c.getString(c.getColumnIndex(COLUMN_FIRST_NAME)),
                    c.getString(c.getColumnIndex(COLUMN_LAST_NAME)),
                    c.getInt(c.getColumnIndex(COLUMN_SEX)),
                    c.getString(c.getColumnIndex(COLUMN_PHOTO50)),
                    c.getInt(c.getColumnIndex(COLUMN_AMOUNT_OF_FRIENDS))));
        } while (c.moveToNext());
        c.close();

        return Observable.just(list);
    }

    public List<String> getIds() {
        Cursor c = mDB.query(DB_TABLE,
                new String[] { COLUMN_USER_ID },
                null, null,
                null, null,
                null);

        if(c.getCount() > 0) c.moveToFirst();
        else return null;

        List<String> list = new ArrayList<>();
        do {
            list.add(c.getString(c.getColumnIndex(COLUMN_USER_ID)));
        } while (c.moveToNext());
        c.close();

        return list;
    }

    private class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
            ContentValues cv = new ContentValues();
            cv.put(COLUMN_USER_ID, "86101454");
            cv.put(COLUMN_FIRST_NAME, "Anton");
            cv.put(COLUMN_LAST_NAME, "Zinakov");
            cv.put(COLUMN_SEX, 0);
            cv.put(COLUMN_PHOTO50, "");
            cv.put(COLUMN_AMOUNT_OF_FRIENDS, 0);
            db.insert(DB_TABLE, null, cv);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        }
    }
}
