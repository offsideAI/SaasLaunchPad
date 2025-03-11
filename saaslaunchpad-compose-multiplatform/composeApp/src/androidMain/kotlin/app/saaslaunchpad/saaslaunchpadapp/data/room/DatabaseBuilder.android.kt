package app.saaslaunchpad.saaslaunchpadapp.data.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<MemeDatabase> {
    val dbFile = context.getDatabasePath("saaslaunchpad.db")
    return Room.databaseBuilder(
        context = context,
        klass = MemeDatabase::class.java,
        name = dbFile.absolutePath
    )
}