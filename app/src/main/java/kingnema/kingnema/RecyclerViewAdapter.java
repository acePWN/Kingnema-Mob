package kingnema.kingnema;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private ArrayList<String> movieNames;
    private ArrayList<String> images;
    private ArrayList<String> movieIDs;
    private ArrayList<String> branchIDs;
    private ArrayList<String> cinemas;
    private ArrayList<String> watchIDs;
    private Context mContext;

    public RecyclerViewAdapter(Context context, ArrayList<String> movieNames, ArrayList<String> images,ArrayList<String> movieIDs,ArrayList<String> branchIDs,ArrayList<String> cinemas) {
        this.movieNames = movieNames;
        this.images = images;
        this.movieIDs=movieIDs;
        this.branchIDs=branchIDs;
        this.cinemas=cinemas;
        this.mContext =  context;
    }
    public RecyclerViewAdapter(Context context, ArrayList<String> movieNames, ArrayList<String> images,ArrayList<String> cinemas,ArrayList<String> watchIDs) {
        this.movieNames = movieNames;
        this.images = images;
        this.watchIDs = watchIDs;
        this.cinemas=cinemas;
        this.mContext =  context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.movieText.setText(movieNames.get(position));
        holder.cinemaText.setText(cinemas.get(position));
        Glide.with(mContext).asBitmap().load(mContext.getString(R.string.siteURL)+images.get(position)).into(holder.imageView);

        if(watchIDs==null){
            if(!movieIDs.get(position).equals("none")){
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(mContext, Movie.class);
                        i.putExtra("movie_title",movieIDs.get(position));
                        i.putExtra("branch_id", branchIDs.get(position));
                        i.putExtra("cinema_no", cinemas.get(position));

                        mContext.startActivity(i);
                    }
                });
            }
        }
        else{
            if(!watchIDs.get(position).equals(""))
            {
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(mContext, Confirmation.class);
                        i.putExtra("watch_id", watchIDs.get(position));

                        mContext.startActivity(i);
                    }
                });
            }
        }


    }

    @Override
    public int getItemCount() {
        return movieNames.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView movieText;
        TextView cinemaText;
        ImageView imageView ;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageMoviePoster);
            cinemaText=itemView.findViewById(R.id.txtCinemaNo);
            movieText=itemView.findViewById(R.id.txtMovieName);
        }
    }
}
