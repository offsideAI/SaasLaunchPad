package app.saaslaunchpad.saaslaunchpadapp.data.room

import androidx.room.RoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

fun getRoomDatabase(
    builder: RoomDatabase.Builder<MemeDatabase>
): MemeDatabase {
    return builder
        .fallbackToDestructiveMigrationOnDowngrade(dropAllTables = true)
        // Remove the setDriver call since it's not available
        // .setDriver(SQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}