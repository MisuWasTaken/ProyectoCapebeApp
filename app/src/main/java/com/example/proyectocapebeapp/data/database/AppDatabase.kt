package com.example.proyectocapebeapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.proyectocapebeapp.data.local.dao.UsuarioDao
import com.example.proyectocapebeapp.data.local.entity.UsuarioEntity
import com.example.proyectocapebeapp.data.local.dao.JornadaDao
import com.example.proyectocapebeapp.data.local.entity.JornadaEntity
import com.example.proyectocapebeapp.data.local.dao.InscripcionDao
import com.example.proyectocapebeapp.data.local.entity.InscripcionEntity

@Database(
    entities = [UsuarioEntity::class, JornadaEntity::class, InscripcionEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDao
    abstract fun jornadaDao(): JornadaDao
    abstract fun inscripcionDao(): InscripcionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val MIGRACION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users ADD COLUMN supabaseId TEXT")
                db.execSQL("UPDATE users SET password = ''")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "capebe_db.db"
                )
                    .addMigrations(MIGRACION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
