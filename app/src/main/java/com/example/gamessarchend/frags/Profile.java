package com.example.gamessarchend.frags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gamessarchend.R;

import com.example.gamessarchend.addapters.RVsarchadapter;
import com.example.gamessarchend.supclasses.DataStruct;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Profile extends Fragment {
    private TextView userName;
    private RecyclerView likedGamesRecyclerView;
    private RVsarchadapter gamesAdapter;

    public Profile() {
        // Required empty public constructor
    }

    public static Profile newInstance(String username) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        userName = view.findViewById(R.id.user_name);
        likedGamesRecyclerView = view.findViewById(R.id.liked_games_recycler);

        if (getArguments() != null) {
            String username = getArguments().getString("username");
            userName.setText(username);
            loadUserLikedGames(username);
        }
        Button toSearchButton = view.findViewById(R.id.ToSarchPage);
        Button toLoginButton = view.findViewById(R.id.BackToLogIn);



        setupRecyclerView();

        toSearchButton.setOnClickListener(v -> navigateToSearch());
        toLoginButton.setOnClickListener(v -> navigateToLogin());

        setupRecyclerView();
        return view;
    }
    private void navigateToSearch() {
        NavController navController = Navigation.findNavController(getView());
        Bundle bun=new Bundle();
        bun.putString("username",userName.getText().toString());
        navController.navigate(R.id.action_profile_to_sarch_bar,bun);
    }

    private void navigateToLogin() {
        NavController navController = Navigation.findNavController(getView());
        navController.navigate(R.id.action_profile_to_log_in);
    }
    private void setupRecyclerView() {
        likedGamesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        gamesAdapter = new RVsarchadapter(new ArrayList<DataStruct>(),getContext(),userName.getText().toString(),0);
        likedGamesRecyclerView.setAdapter(gamesAdapter);
    }

    private void loadUserLikedGames(String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        System.out.println("Querying for user: " + username); // Debug log
        db.collection("users")
                .whereEqualTo("username", username)
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            List<String> likedGames = (List<String>) document.get("gamesLiked");
                            if (likedGames != null) {
                                fetchGameDetails(likedGames);
                            } else {
                                System.out.println("No gamesLiked array found for user: " + username); // Debug log
                            }
                        }
                    } else {
                        System.out.println("Failed to fetch or no documents found for username: " + username); // Debug log
                        if (!task.isSuccessful()) {
                            System.out.println("Error: " + task.getException()); // Log actual error
                        }
                    }
                });
    }

    private void fetchGameDetails(List<String> gameNames) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<DataStruct> games = new ArrayList<>();

        for (String gameName : gameNames) {
            db.collection("Games").whereEqualTo("title", gameName).get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                DataStruct game = document.toObject(DataStruct.class);
                                games.add(game);
                            }
                            this.gamesAdapter.updateGames(games);
                        } else {
                            // Handle errors here
                        }
                    });
        }
    }
}
