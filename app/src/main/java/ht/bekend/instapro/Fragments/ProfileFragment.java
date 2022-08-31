package ht.bekend.instapro.Fragments;

import android.util.Log;
import android.view.Menu;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import ht.bekend.instapro.Models.post;
import ht.bekend.instapro.R;

public class ProfileFragment extends PostFragment{

    @Override
    protected void queryposts() {
        super.queryposts();
        ParseQuery<post> query = ParseQuery.getQuery(post.class);
        query.include(post.KEY_USER);
        query.setLimit(20);
        query.addDescendingOrder(post.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<post>() {
            @Override
            public void done(List<post> Posts, ParseException e) {


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
