package com.example.gamessarchend.frags;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gamessarchend.R;
import com.example.gamessarchend.addapters.RVsarchadapter;
import com.example.gamessarchend.supclasses.DataStruct;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class sarch_bar extends Fragment {

    private RecyclerView recyclerView;
    private RVsarchadapter adapter;
    private String userName;
    private ArrayList<DataStruct> gamesList = new ArrayList<>();
    private FirebaseFirestore db;
    private Button backToProfile;

    // onCreateView method remains the same

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sarch_bar, container, false);
        userName=getArguments().getString("username");
        backToProfile=view.findViewById(R.id.BacktoProfile);
        backToProfile.setOnClickListener(v -> backtoProfile(view));
        setupRecyclerView(view);
        setupButtons(view);
        return view;
    }
private void backtoProfile(View view)
{
    NavController nav= Navigation.findNavController(view);
    Bundle bun=new Bundle();
    bun.putString("username",userName);
    nav.navigate(R.id.action_sarch_bar_to_profile,bun);
}
    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.SB_RV); // Update ID accordingly
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RVsarchadapter(gamesList,getActivity(),userName,1);
        recyclerView.setAdapter(adapter);
        db = FirebaseFirestore.getInstance();
    }

    private void setupButtons(View view) {
        view.findViewById(R.id.SBcategory1).setOnClickListener(v -> queryGamesByCategory("Action"));
        view.findViewById(R.id.SBcategory2).setOnClickListener(v -> queryGamesByCategory("Strategy"));
        // Setup button for "RPG" category
        view.findViewById(R.id.SBcategory3).setOnClickListener(v -> queryGamesByCategory("RPG"));
        // Setup button for "Moba" category
        view.findViewById(R.id.SBcategory4).setOnClickListener(v -> queryGamesByCategory("Moba"));
        // Setup button for "MMORPG" category
        view.findViewById(R.id.SBcategory5).setOnClickListener(v -> queryGamesByCategory("MMORPG"));
        // Setup button for another "Strategy" (maybe meant to be different?)
        view.findViewById(R.id.SBcategory6).setOnClickListener(v -> queryGamesByCategory("Strategy"));

         view.findViewById(R.id.SBcofirmButt).setOnClickListener(v ->sarchBarActivity(view));

        // Repeat for other buttons/categories
    }
private void sarchBarActivity(View view) {
    String sarchBarText = ((TextView) (view.findViewById(R.id.SBsarchBarTextIn))).getText().toString();
    // CollectionReference collection=db.collection("Games");
    //CollectionReference Titlescollection=collection.select("title").get();
    CollectionReference gamesCollectionRef = db.collection("Games");
    Query sarchFor = gamesCollectionRef.whereArrayContains("keyWords", sarchBarText);
gamesList.clear();

    sarchFor.get().addOnSuccessListener(docs -> {
        for (QueryDocumentSnapshot document : docs) {
            // Convert the Firestore document into a DataStruct object
            DataStruct gameData = document.toObject(DataStruct.class);

            // Now you can use gameData as a fully populated object
            Log.d("SearchResult", "Game title: " + gameData.getTitle() +
                    ", Developer: " + gameData.getDeveloper()+ ", description"+gameData.getDescription()
            +"Ganer,"+gameData.getGenre());

            // Process your document further, e.g., display the game info in the UI

            gamesList.add(gameData);
        }
        adapter.notifyDataSetChanged();
    }).addOnFailureListener(e -> Log.d("SearchResult", "Error getting documents: ", e));

}
    private void queryGamesByCategory(String category) {
        db.collection("Games") // Use correct collection name
                .whereEqualTo("genre", category)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    gamesList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        DataStruct game = document.toObject(DataStruct.class);
                        gamesList.add(game);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("SearchBarFragment", "Error getting documents: ", e));
    }
}
