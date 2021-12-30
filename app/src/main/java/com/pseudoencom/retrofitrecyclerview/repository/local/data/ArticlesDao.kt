import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.pseudoencom.retrofitrecyclerview.modules.main.model.ArticleModel

@Dao
interface ArticlesDao {

    @Query("SELECT * FROM articles")
    fun getArticles(): MutableList<ArticleModel>

    @Query("DELETE from articles WHERE id = :id")
    fun delete(id: Int)

    @Insert
    fun insert(article: ArticleModel)

}