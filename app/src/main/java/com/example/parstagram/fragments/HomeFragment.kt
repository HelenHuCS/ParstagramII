package com.example.parstagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.parstagram.MainActivity
import com.example.parstagram.Post
import com.example.parstagram.PostAdapter
import com.example.parstagram.R
import com.parse.ParseQuery

open class HomeFragment : Fragment() {

    private lateinit var postRecycleView:RecyclerView
    lateinit var adapter:PostAdapter
    private lateinit var swipContainer:SwipeRefreshLayout

    var allPosts:MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postRecycleView = view.findViewById(R.id.postRecycleView)
        swipContainer = view.findViewById(R.id.swipContainer)
        swipContainer.setOnRefreshListener {
            queryPosts()
        }
        swipContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);


        adapter = PostAdapter(requireContext(),allPosts)
        postRecycleView.adapter = adapter

        postRecycleView.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()
    }

    open fun queryPosts() {
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER)
        query.addDescendingOrder("createdAt")
        query.limit = 20
        query.findInBackground { posts, e ->
            if (e != null) {
                e.printStackTrace()
            } else {
                if (posts != null) {
                    for (p in posts) {
                        Log.i(MainActivity.TAG, "done: " + p.getDerscription()+" usernamre="+p.getUser()?.username)
                    }
                    adapter.clear()
                    adapter.addAll(posts)
                    swipContainer.isRefreshing = false
                }
            }
        }
    }
}