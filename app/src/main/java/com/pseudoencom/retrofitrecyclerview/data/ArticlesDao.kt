import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pseudoencom.retrofitrecyclerview.data.ArticlesEntity

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM articles ORDER BY id DESC")
    fun getArticles(): List<ArticlesEntity>?

    @Insert
    fun insert(article: ArticlesEntity?)

    @Query("DELETE from articles WHERE id = :id")
    fun delete(id: Int?)

}