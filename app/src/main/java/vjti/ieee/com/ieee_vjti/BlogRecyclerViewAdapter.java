package vjti.ieee.com.ieee_vjti;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sohamdeshmukh on 04/11/17.
 */

public class BlogRecyclerViewAdapter extends RecyclerView.Adapter<BlogRecyclerViewAdapter.ViewHolder>{

    private List<Blog> blogs;

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mDateButton;
        private TextView mTitle;
        private TextView mSubject;
        private Blog blog;

        public ViewHolder(View itemView) {
            super(itemView);
            mDateButton = (TextView) itemView.findViewById(R.id.list_item_blog_date_text_view);
            mTitle = (TextView) itemView.findViewById(R.id.list_item_blog_title_text_view);
            mSubject = (TextView) itemView.findViewById(R.id.list_item_blog_subject_text_view);
            itemView.setOnClickListener(this);
        }

        public void bind(Blog blog) {
            this.blog = blog;
            mDateButton.setText(blog.getDate());
            mTitle.setText(blog.getTitle());
            mSubject.setText(blog.getSubject());
            Log.e("subject",blog.getSubject());
            Log.e("title",blog.getTitle());
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
//            context.startActivity(BlogActivity.newInstance(context, blog));
        }
    }

    public BlogRecyclerViewAdapter(List<Blog> blogs) {
        this.blogs = blogs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_blog, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(blogs.get(position));
    }

    @Override
    public int getItemCount() {
        return blogs.size();
    }

    public void updateList(List<Blog> blogs) {
        // Allow recyclerview animations to complete normally if we already know about data changes
        if (blogs.size() != this.blogs.size() || !this.blogs.containsAll(blogs)) {
            this.blogs = blogs;
            notifyDataSetChanged();
        }
    }

    public void removeItem(int position) {
        blogs.remove(position);
        notifyItemRemoved(position);
    }

    public Blog getItem(int position) {
        return blogs.get(position);
    }

    public void animateTo(List<Blog> blogs) {
        applyAndAnimateRemovals(blogs);
        applyAndAnimateAdditions(blogs);
        applyAndAnimateMovedItems(blogs);
    }


    private void applyAndAnimateRemovals(List<Blog> newblogs) {
        for (int i = blogs.size() - 1; i >= 0; i--) {
            final Blog register = blogs.get(i);
            if (!blogs.contains(register)) {
                removeItem(i);
            }
        }
    }


    private void applyAndAnimateAdditions(List<Blog> newblogs) {
        for (int i = 0, count = newblogs.size(); i < count; i++) {
            final Blog blog = newblogs.get(i);
            if (!blogs.contains(blog)) {
                addItem(i, blog);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Blog> newblogs) {
        for (int toPosition = newblogs.size() - 1; toPosition >= 0; toPosition--) {
            final Blog blog = newblogs.get(toPosition);
            final int fromPosition = blogs.indexOf(blog);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public void addItem(int position, Blog blog) {
        blogs.add(position, blog);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Blog blog = blogs.remove(fromPosition);
        blogs.add(toPosition, blog);
        notifyItemMoved(fromPosition, toPosition);
    }
}
