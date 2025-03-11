package app.saaslaunchpad.saaslaunchpadapp.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import app.saaslaunchpad.saaslaunchpadapp.domain.model.Meme
import app.saaslaunchpad.saaslaunchpadapp.data.dao.MemeDao
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

@Database(
    entities = [Meme::class],
    version = 1,
    exportSchema = true
)
abstract class MemeDatabase: RoomDatabase() {
    abstract fun memeDao(): MemeDao
    
    companion object {
        // Factory method to create the database
        fun create(builder: RoomDatabase.Builder<MemeDatabase>): MemeDatabase {
            return builder.build()
        }
    }
}

class MemeTypeConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return Json.decodeFromString(
            ListSerializer(String.serializer()),
            value
        )
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return Json.encodeToString(
            ListSerializer(String.serializer()),
            list
        )
    }

}