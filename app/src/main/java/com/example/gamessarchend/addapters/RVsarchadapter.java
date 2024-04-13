package com.example.gamessarchend.addapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.gamessarchend.R;
import com.example.gamessarchend.supclasses.DataStruct;

import java.util.ArrayList;

public class RVsarchadapter extends RecyclerView.Adapter<RVsarchadapter.GameViewHolder> {

    private LayoutInflater MainInflater;
    private Context AppContext;
    private int Clickble=0;
    private static int ClickbleStatic=0;
    private  ArrayList<DataStruct> gamesList;
    private static ArrayList<DataStruct> gameListCoppyForHolder;
    private static String UserNameString;
    private String userName;

    public RVsarchadapter(ArrayList<DataStruct> gamesList,Context context,String username,int clickble)
    {
        this.MainInflater=LayoutInflater.from(context);
        this.AppContext=context;
        this.gamesList = gamesList;
        this.gameListCoppyForHolder=gamesList;
        this.userName=username;
        this.UserNameString=userName;
        this.Clickble=clickble;
        this.ClickbleStatic=clickble;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rv_sarchbar_card, parent, false);
GameViewHolder holder = new GameViewHolder(view);
return  holder;
        }catch (Exception e)
        {
            System.out.println(e);
        }
return  null    ;
    }

    @Override
    public void onBindViewHolder(GameViewHolder holder, int position) {
        DataStruct curgame = gamesList.get(position);
        holder.gameTitle.setText(curgame.getTitle());
        Glide.with(holder.gameImage.getContext())
                .load(curgame.getImageURL())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("ImageView", "Load failed for URL: " + curgame.getImageURL(), e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.d("ImageView", "Image loaded successfully for URL: " + curgame.getImageURL());
                        return false;
                    }
                })
                .into(holder.gameImage);

        // Set other views in the holder as needed

    }

    @Override
    public int getItemCount() {
        return gamesList.size();
    }
    public void updateGames(ArrayList<DataStruct> newGames) {
        gamesList.clear();
        gamesList.addAll(newGames);
        notifyDataSetChanged(); // Notify the adapter that the data set has changed
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        TextView gameTitle;
        ImageView gameImage;
        // Define other views in the item layout

        public GameViewHolder(View itemView) {
            super(itemView);
            gameTitle = itemView.findViewById(R.id.CardText);
            gameImage = itemView.findViewById(R.id.CardImage);
            // Adjust ID accordingly
            // Initialize other views in the item layout
            if(ClickbleStatic==1)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int curpos=getAdapterPosition();
                    DataStruct item = gameListCoppyForHolder.get(curpos);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("game", item);
                    bundle.putString("username",UserNameString);
                    // Navigation using the item view's context to find the NavController
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_sarch_bar_to_gamePage, bundle);
                }

            });
        }
    }

}
