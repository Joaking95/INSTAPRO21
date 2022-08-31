package ht.bekend.instapro.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import ht.bekend.instapro.Adapter.PostAdapter;
import ht.bekend.instapro.Models.post;
import ht.bekend.instapro.R;

public class PostFragment extends Fragment {

      RecyclerView rvPosts;
      PostAdapter adapter;
      List<post> Allposts;
      Boolean mFirstLoad;
    SwipeRefreshLayout swipeContainer;

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);

        Allposts = new ArrayList<>();
        adapter = new PostAdapter(Allposts,getContext());
        rvPosts.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        mFirstLoad=true;
        swipeContainer = view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                queryposts();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        queryposts();

    }

    protected void queryposts() {
        ParseQuery<post> query = ParseQuery.getQuery(post.class);
        query.include(post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<post>() {
            @Override
            public void done(List<post> Posts, ParseException e) {

                for (post Post: Posts){

                    Log.i("ok", "post " +Post.getDescription() + ",username " +Post.getUser().getUsername());
                }

                if(e == null){
                    adapter.clear();
                    adapter.addAll(Posts);
                    swipeContainer.setRefreshing(false);
                }
                if(mFirstLoad){
                    rvPosts.scrollToPosition(0);
                    mFirstLoad= false;
                }
                else {
                    Log.e("post", "Error Loading post" + e);
                }

            }
        });
    }

}