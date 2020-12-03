package hu.bme.aut.myapplication.data

import android.content.Intent
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.android.material.bottomnavigation.BottomNavigationView
import hu.bme.aut.myapplication.ListActivity
import hu.bme.aut.myapplication.R
import kotlinx.android.parcel.Parcelize
import java.net.URI

@Entity(tableName = "recipeitem")
@Parcelize
data class RecipeItem (
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "directions") val directions: String,
    @ColumnInfo(name = "ingredients") val ingredients: String,
    @ColumnInfo(name = "category") val category: Category,
    @ColumnInfo(name = "price") val price: String,
    @ColumnInfo(name = "estimated_cooking_time") val cookingTime : Int,
    @ColumnInfo(name = "isfavourite") val isFavourite: Boolean,
    @ColumnInfo(name = "picture_uri") val pictureURI: String
    ): Parcelable{
        enum class Category {
            APPETIZER, SOUP, MAINCOURSE, DESSERT;
            companion object {
                @JvmStatic
                @TypeConverter
                fun getByOrdinal(ordinal: Int): Category? {
                    var ret: Category? = null
                    for (cat in values()) {
                        if (cat.ordinal == ordinal) {
                            ret = cat
                            break
                        }
                    }
                    return ret
                }

                @JvmStatic
                @TypeConverter
                fun toInt(category: Category): Int {
                    return category.ordinal
                }
            }
        }
    }


//object Converters {
//    @TypeConverter
//    @JvmStatic
//    fun toType(ordinal: Int): Cell.Type? {
//        return Cell.Type.values().find {
//            it.ordinal == ordinal
//        }
//    }
//    @JvmStatic
//    @TypeConverter
//    fun toIntType(type: Cell.Type): Int {
//        return type.ordinal
//    }
//    @TypeConverter
//    @JvmStatic
//    fun toVisitState(ordinal: Int): Cell.VisitState? {
//        return Cell.VisitState.values().find {
//            it.ordinal == ordinal
//        }
//    }
//    @JvmStatic
//    @TypeConverter
//    fun toIntVisitState(type: Cell.VisitState): Int {
// json.deserialze(type)
//        return type.ordinal
//    }
//}
