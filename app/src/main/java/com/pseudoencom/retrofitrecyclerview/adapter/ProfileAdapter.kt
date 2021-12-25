import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pseudoencom.retrofitrecyclerview.R
import com.pseudoencom.retrofitrecyclerview.model.ProfileModel
import com.pseudoencom.retrofitrecyclerview.view.ProfileFragment

class ProfileAdapter(
    val myDataSet: ArrayList<ProfileModel>, val onClickListener: ProfileFragment,
    val context: Context
) : RecyclerView.Adapter<ProfileAdapter.MyViewHolder>() {

    class MyViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView
        var textViewTitle: TextView
        init {
            imageView = view.findViewById(R.id.itemPiCon)
            textViewTitle = view.findViewById(R.id.itemPtxName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount() = myDataSet.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemData = myDataSet[position]
        holder.textViewTitle.text = itemData.name
        holder.itemView.setOnClickListener(onClickListener)
        holder.itemView.tag = itemData
        holder.imageView.setImageResource(itemData.icon)
    }
}
