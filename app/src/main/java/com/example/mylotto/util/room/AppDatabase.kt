package com.example.mylotto.util.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Lotto::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contactsDao(): ContactsDao

    companion object {
        private var instance: AppDatabase? = null

        var MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                with(database) {
                    // 변경될 테이블 컬럼 및 타입작성
                    // 단, 테이블이 정의되어있는 클래스와 타입 및 nullable 일치해야함
                    execSQL(
                        "CREATE TABLE IF NOT EXISTS new_Lotto (" +
                                "number TEXT NOT NULL, " +
                                "time TEXT NOT NULL, " +
                                "seq TEXT NOT NULL, " +
                                "checked INTEGER NOT NULL DEFAULT 0, " +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL)"

                    ) // 변경할 백업 테이블 생성

                    execSQL(
                        "INSERT INTO new_Lotto (number, time, seq, checked, id) " +
                                "SELECT value, date, seq, 'check', id FROM lotto"
                    ) // 백업 테이블 insert

                    execSQL("DROP TABLE lotto"); // 기존 테이블 삭제
                    execSQL("ALTER TABLE new_Lotto RENAME TO lotto"); // 테이블명변경
                    execSQL("ALTER TABLE lotto ADD COLUMN content TEXT DEFAULT NULL")
                    execSQL("ALTER TABLE lotto ADD COLUMN price INTEGER DEFAULT NULL")
                    execSQL("ALTER TABLE lotto ADD COLUMN grade INTEGER DEFAULT NULL")
                    // 컬럼추가
                }

            }

        }

//         var MIGRATION_2_3 = object : Migration(2, 3) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                //바뀐 스키마 내용
//                database.execSQL(
//                    "ALTER TABLE tb_contacts ADD COLUMN keyy TEXT NOT NULL DEFAULT ''")
//            }
//        }

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database-lotto"
                )
//                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
//                .fallbackToDestructiveMigration()
        }
    }
}