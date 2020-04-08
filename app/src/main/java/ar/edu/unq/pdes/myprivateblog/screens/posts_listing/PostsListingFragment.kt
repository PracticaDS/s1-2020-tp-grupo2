package ar.edu.unq.pdes.myprivateblog.screens.posts_listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.unq.pdes.myprivateblog.BaseFragment
import ar.edu.unq.pdes.myprivateblog.R
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.data.EntityID
import kotlinx.android.synthetic.main.fragment_posts_listing.*
import timber.log.Timber

class PostsListingFragment : BaseFragment() {
    override val layoutId = R.layout.fragment_posts_listing

    private val viewModel by viewModels<PostsListingViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMainActivity().hideKeyboard()

        context?.apply { applyStatusBarStyle(this.getColor(R.color.palette_pastel_yellow_02)) }


        create_new_post.setOnClickListener {
            findNavController().navigate(PostsListingFragmentDirections.navActionCreatePost())
        }

        viewModel.posts.observe(viewLifecycleOwner, Observer { postList ->
            postList.forEach { Timber.d("POST LOADED: ${it.title} (body in ${it.bodyPath})") }

            posts_list_recyclerview.adapter = PostsListAdapter(postList) {
                findNavController().navigate(PostsListingFragmentDirections.navActionOpenDetail(it))
            }

            posts_list_recyclerview.layoutManager = LinearLayoutManager(context)
        })


    }
}

class BlogEntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val title: TextView = itemView.findViewById(R.id.item_title)
}

class PostsListAdapter(
    private val postList: List<BlogEntry>,
    private val onItemClicked: (EntityID) -> Unit
) : RecyclerView.Adapter<BlogEntryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogEntryViewHolder {
        val postViewItem =
            LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return BlogEntryViewHolder(postViewItem)
    }

    override fun getItemCount(): Int = postList.size

    override fun onBindViewHolder(holder: BlogEntryViewHolder, position: Int) {
        val blogEntry = postList[position]
        holder.title.text = blogEntry.title
        holder.itemView.setOnClickListener { onItemClicked(blogEntry.uid) }
    }

}