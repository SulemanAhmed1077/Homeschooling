package com.example.homeschooling;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HomeschoolingDB";
    private static final int DATABASE_VERSION = 5; // Version bumped to ensure all tables exist

    // --- TABLES ---
    public static final String TABLE_USERS = "users";
    public static final String TABLE_PARENT_PROFILE = "parent_profile";
    public static final String TABLE_TUTOR_PROFILE = "tutor_profile";
    public static final String TABLE_TUITION_REQUESTS = "tuition_requests";
    public static final String TABLE_ATTENDANCE = "attendance";
    public static final String TABLE_ESCROW_PAYMENTS = "escrow_payments";
    public static final String TABLE_MESSAGES = "messages";

    // --- COLUMNS ---
    public static final String COLUMN_ID = "req_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_NAME = "name";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_PHONE = "phone";
    public static final String COLUMN_USER_CITY = "city";
    public static final String COLUMN_USER_ROLE = "role";

    public static final String COLUMN_TUTOR_USER_ID = "tutor_user_id";
    public static final String COLUMN_TUTOR_SUBJECTS = "subjects";
    public static final String COLUMN_TUTOR_CLASS_LEVELS = "class_levels";
    public static final String COLUMN_TUTOR_HOURLY_FEE = "hourly_fee";
    public static final String COLUMN_TUTOR_EXPERIENCE = "experience";
    public static final String COLUMN_TUTOR_AVAILABILITY = "availability";

    public static final String COLUMN_REQ_ID = "req_id";
    public static final String COLUMN_REQ_PARENT_ID = "parent_id";
    public static final String COLUMN_REQ_CHILD_NAME = "req_child_name";
    public static final String COLUMN_REQ_STATUS = "req_status";
    public static final String COLUMN_REQ_TUTOR_ID = "assigned_tutor_id";

    public static final String COLUMN_ESCROW_REQ_ID = "escrow_req_id";
    public static final String COLUMN_ESCROW_PAYABLE = "payable_amount";

    public static final String COLUMN_SENDER_ID = "sender_id";
    public static final String COLUMN_RECEIVER_ID = "receiver_id";
    public static final String COLUMN_MESSAGE_TEXT = "message_text";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USER_NAME + " TEXT, " + COLUMN_USER_EMAIL + " TEXT UNIQUE, " + COLUMN_USER_PASSWORD + " TEXT, " + COLUMN_USER_PHONE + " TEXT, " + COLUMN_USER_CITY + " TEXT, " + COLUMN_USER_ROLE + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_PARENT_PROFILE + " (parent_profile_id INTEGER PRIMARY KEY AUTOINCREMENT, parent_user_id INTEGER, child_name TEXT, child_class TEXT, subjects TEXT, budget INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_TUTOR_PROFILE + " (tutor_profile_id INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TUTOR_USER_ID + " INTEGER, " + COLUMN_TUTOR_SUBJECTS + " TEXT, " + COLUMN_TUTOR_CLASS_LEVELS + " TEXT, " + COLUMN_TUTOR_HOURLY_FEE + " INTEGER, " + COLUMN_TUTOR_EXPERIENCE + " TEXT, " + COLUMN_TUTOR_AVAILABILITY + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_TUITION_REQUESTS + " (" + COLUMN_REQ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, parent_id INTEGER, req_child_name TEXT, req_class TEXT, req_subjects TEXT, req_fee INTEGER, req_days INTEGER, " + COLUMN_REQ_STATUS + " TEXT DEFAULT 'Pending', " + COLUMN_REQ_TUTOR_ID + " INTEGER DEFAULT -1)");
        db.execSQL("CREATE TABLE attendance (att_id INTEGER PRIMARY KEY AUTOINCREMENT, att_req_id INTEGER, att_date TEXT, att_status TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_ESCROW_PAYMENTS + " (escrow_id INTEGER PRIMARY KEY AUTOINCREMENT, escrow_req_id INTEGER, total_fee INTEGER, " + COLUMN_ESCROW_PAYABLE + " INTEGER DEFAULT 0, escrow_status TEXT DEFAULT 'Held')");
        db.execSQL("CREATE TABLE " + TABLE_MESSAGES + " (msg_id INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_SENDER_ID + " INTEGER, " + COLUMN_RECEIVER_ID + " INTEGER, " + COLUMN_MESSAGE_TEXT + " TEXT, " + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TUTOR_PROFILE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARENT_PROFILE);
        onCreate(db);
    }

    // --- PROFILE METHODS (FIX FOR YOUR ERROR) ---
    public Cursor getTutorProfile(int userId) {
        return getReadableDatabase().query(TABLE_TUTOR_PROFILE, null, COLUMN_TUTOR_USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
    }

    public Cursor getParentProfile(int userId) {
        return getReadableDatabase().query(TABLE_PARENT_PROFILE, null, "parent_user_id=?", new String[]{String.valueOf(userId)}, null, null, null);
    }

    // --- AUTH & USER ---
    public Cursor getUserByEmail(String email) {
        return getReadableDatabase().query(TABLE_USERS, null, COLUMN_USER_EMAIL + "=?", new String[]{email}, null, null, null);
    }

    public void updateUser(int userId, String name, String phone, String city) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_USER_NAME, name); v.put(COLUMN_USER_PHONE, phone); v.put(COLUMN_USER_CITY, city);
        db.update(TABLE_USERS, v, COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
    }

    // --- DASHBOARD STATS ---
    public int getTutorStudentCount(int tutorId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_TUITION_REQUESTS + " WHERE " + COLUMN_REQ_TUTOR_ID + "=? AND " + COLUMN_REQ_STATUS + "='Accepted'", new String[]{String.valueOf(tutorId)});
        int count = 0; if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close(); return count;
    }

    public int getTutorTotalEarnings(int tutorId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(" + COLUMN_ESCROW_PAYABLE + ") FROM " + TABLE_ESCROW_PAYMENTS + " e JOIN " + TABLE_TUITION_REQUESTS + " r ON e." + COLUMN_ESCROW_REQ_ID + " = r." + COLUMN_REQ_ID + " WHERE r." + COLUMN_REQ_TUTOR_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(tutorId)});
        int total = 0; if (cursor.moveToFirst()) total = cursor.getInt(0);
        cursor.close(); return total;
    }

    // --- CHAT ---
    public long sendMessage(int sId, int rId, String msg) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_SENDER_ID, sId); v.put(COLUMN_RECEIVER_ID, rId); v.put(COLUMN_MESSAGE_TEXT, msg);
        return db.insert(TABLE_MESSAGES, null, v);
    }

    public Cursor getChatMessages(int u1, int u2) {
        String q = "SELECT * FROM " + TABLE_MESSAGES + " WHERE (" + COLUMN_SENDER_ID + "=? AND " + COLUMN_RECEIVER_ID + "=?) OR (" + COLUMN_SENDER_ID + "=? AND " + COLUMN_RECEIVER_ID + "=?) ORDER BY " + COLUMN_TIMESTAMP + " ASC";
        return getReadableDatabase().rawQuery(q, new String[]{String.valueOf(u1), String.valueOf(u2), String.valueOf(u2), String.valueOf(u1)});
    }

    // --- REQUESTS ---
    public Cursor getTutorStudents(int tutorId) {
        return getReadableDatabase().rawQuery("SELECT r.*, u.name as parent_name FROM tuition_requests r JOIN users u ON r.parent_id = u.user_id WHERE r.assigned_tutor_id = ? AND r.req_status = 'Accepted'", new String[]{String.valueOf(tutorId)});
    }

    public boolean acceptTuitionRequest(int reqId, int tutorId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_REQ_TUTOR_ID, tutorId); v.put(COLUMN_REQ_STATUS, "Accepted");
        return db.update(TABLE_TUITION_REQUESTS, v, COLUMN_REQ_ID + "=?", new String[]{String.valueOf(reqId)}) > 0;
    }
}