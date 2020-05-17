package ar.edu.unq.pdes.myprivateblog.screens.posts_listing

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ar.edu.unq.pdes.myprivateblog.BaseFragment
import ar.edu.unq.pdes.myprivateblog.ColorUtils
import ar.edu.unq.pdes.myprivateblog.R
import ar.edu.unq.pdes.myprivateblog.data.BlogEntry
import ar.edu.unq.pdes.myprivateblog.data.EntityID
import kotlinx.android.synthetic.main.fragment_posts_listing.*


class PostsListingFragment : BaseFragment(R.layout.fragment_posts_listing) {
    private val viewModel by viewModels<PostsListingViewModel> { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMainActivity().hideKeyboard()

        context?.apply { applyStatusBarStyle(this.getColor(R.color.palette_pastel_yellow_02)) }
//      context?.apply { applyStatusBarStyle(ContextCompat.getColor(this, R.color.palette_pastel_yellow_02)) }     PARA API 21

        create_new_post.setOnClickListener {
            findNavController().navigate(PostsListingFragmentDirections.navActionCreatePost())
        }

        viewModel.posts.observe(viewLifecycleOwner, Observer { postList ->
            image_empty_background.isVisible = postList.isEmpty()
            text_empty_list.isVisible = postList.isEmpty()
            posts_list_recyclerview.adapter = PostsListAdapter(postList) {
                findNavController().navigate(PostsListingFragmentDirections.navActionOpenDetail(it))
            }
            posts_list_recyclerview.layoutManager = LinearLayoutManager(context)
        })

        val toolbar = view.findViewById(R.id.toolbar) as Toolbar?
        toolbar?.title = "Mi Blog Privado"
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
    }

/*
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){
        val menuInflater: MenuInflater = activity!!.getMenuInflater()
        menuInflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
*/
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
        holder.title.backgroundTintList = ColorStateList.valueOf(blogEntry.cardColor)
        holder.title.setTextColor(ColorUtils.findTextColorGivenBackgroundColor(blogEntry.cardColor))
        holder.itemView.setOnClickListener { onItemClicked(blogEntry.uid) }
    }

}