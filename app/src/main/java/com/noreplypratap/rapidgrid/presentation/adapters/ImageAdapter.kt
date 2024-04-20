import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.noreplypratap.rapidgrid.R
import com.noreplypratap.rapidgrid.model.ImageData
import com.noreplypratap.rapidgrid.presentation.viewmodels.RemoteViewModel


class ImageAdapter(private val remoteViewModel: RemoteViewModel) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    private val handler = Handler()

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<ImageData>() {
        override fun areItemsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
            return oldItem.imageURL == newItem.imageURL
        }

        override fun areContentsTheSame(oldItem: ImageData, newItem: ImageData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val data = differ.currentList[position]
        val bitmap = data.bitmap
        if (bitmap != null) {
            holder.imageView.setImageBitmap(bitmap)
        } else {
            holder.imageView.setImageResource(R.drawable.baseline_image_24)
        }
    }

    override fun getItemCount() = differ.currentList.size

    // Attach scroll listener to the RecyclerView
    fun attachScrollListener(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Post a delayed runnable to log the visible range
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({
                    val layoutManager = recyclerView.layoutManager as? GridLayoutManager
                    val firstVisibleItemPosition =
                        layoutManager?.findFirstVisibleItemPosition() ?: 0
                    val lastVisibleItemPosition = layoutManager?.findLastVisibleItemPosition() ?: 0
                    remoteViewModel.loadVisibleImage(
                        firstVisibleItemPosition,
                        lastVisibleItemPosition
                    )
                }, 300)
            }
        })
    }
}
