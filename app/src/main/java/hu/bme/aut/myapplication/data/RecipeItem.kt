package hu.bme.aut.myapplication.data


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import kotlinx.android.parcel.Parcelize


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


