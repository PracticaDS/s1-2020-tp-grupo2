package ar.edu.unq.pdes.myprivateblog.screens.posts_listing

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

        if(!viewModel.isLoggedIn()){
            goToUserLogin()
        }

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
        createMenOptions()
    }

    private fun createMenOptions(){
        val toolbar = getMainActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.inflateMenu(R.menu.menu)
        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.menuLogout) {
                signOutCurrentUser()
            }
            true
        }
    }

    private fun signOutCurrentUser(){
        viewModel.logout()
        findNavController().navigate(PostsListingFragmentDirections.navActionLogin())
    }

    private fun goToUserLogin(){
        findNavController().navigate(PostsListingFragmentDirections.navActionLogin())
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
        holder.title.backgroundTintList = ColorStateList.valueOf(blogEntry.cardColor)
        holder.title.setTextColor(ColorUtils.findTextColorGivenBackgroundColor(blogEntry.cardColor))
        holder.itemView.setOnClickListener { onItemClicked(blogEntry.uid) }
    }

}