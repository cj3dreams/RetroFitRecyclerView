import androidx.room.*
import com.pseudoencom.retrofitrecyclerview.data.ArticlesEntity

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM articles ORDER BY id DESC")
    fun getArticles(): List<ArticlesEntity>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(article: ArticlesEntity?)

    @Delete
    fun delete(article: ArticlesEntity?)

    @Update
    fun update(article: ArticlesEntity?)

}
