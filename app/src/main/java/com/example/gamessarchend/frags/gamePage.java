package com.example.gamessarchend.frags;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gamessarchend.R;
import com.example.gamessarchend.supclasses.DataStruct;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link gamePage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class gamePage extends Fragment {
    String YTK="AIzaSyAqvgy6wk-jESTCg8OtYxGNrEtI2n2ek60";
    TextView des;
    ImageView MainImage;
    TextView gameName;
    String UserName;
    TextView dev;
    Button backButt;
    WebView Web;
    Button BigLikeButton;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public gamePage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment gamePage.
     */
    // TODO: Rename and change types and number of parameters
    public static gamePage newInstance(String param1, String param2) {
        gamePage fragment = new gamePage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_game_page, container, false);
        DataStruct receivedData = getArguments().getParcelable("game");
        UserName=getArguments().getString("username");
        BigLikeButton=view.findViewById(R.id.LikeButton);
        BigLikeButton.setOnClickListener(v -> BigLikeHandler());
        des=view.findViewById(R.id.GamePageGameDescription);
        dev=view.findViewById(R.id.GamePageGameDev);
        gameName=view.findViewById(R.id.GamePageGameName);
        MainImage=view.findViewById(R.id.gameImage);
       Glide.with(this).load(receivedData.getImageURL()).into(MainImage);
        des.setText(receivedData.getDescription());
        dev.setText(receivedData.getDeveloper());
        gameName.setText(receivedData.getTitle());
        Web=view.findViewById(R.id.WebView);
        backButt=view.findViewById(R.id.GamePageBackButton);
        backButt.setOnClickListener(this::handleBack);
        getWeebWorking(receivedData.getTrailerURL());
        return  view;
    }
private void handleBack(View view)
{
    NavController nav= Navigation.findNavController(view);
    Bundle bun=new Bundle();
    bun.putString("username",UserName);
    nav.navigate(R.id.action_gamePage_to_sarch_bar,bun);
}
private void BigLikeHandler()
{
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String username = UserName;
    String gameId = gameName.getText().toString(); // ID of the game to add to the liked list

// Query for the user document by username
    Query query = db.collection("users").whereEqualTo("username", username);
    query.get().addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
            for (QueryDocumentSnapshot document : task.getResult()) {
                // Assuming username is unique and only one document will be returned
                DocumentReference userRef = document.getReference();

                // Add the game to the 'gamesLiked' array in the found user document
                userRef.update("gamesLiked", FieldValue.arrayUnion(gameId))
                        .addOnSuccessListener(aVoid -> {
                            Log.d("Firestore", "Game successfully added to user's liked list.");
                            // Additional actions such as UI update or notifications can be handled here
                        })
                        .addOnFailureListener(e -> Log.e("Firestore", "Error updating user's liked games", e));
            }
        } else {
            Log.e("Firestore", "Error fetching user document", task.getException());
        }
    });


}
public String ExtractYouT_ID(String URL)
{

    String vId = null;
    Pattern pattern = Pattern.compile(
            "https?://www\\.youtube\\.com/watch\\?v=([\\w-]{11})",
            Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(URL);
    if (matcher.find()){
        vId = matcher.group(1);
    }
    return vId;
}
    private void getWeebWorking(String videoUrl) {
        String videoId = ExtractYouT_ID(videoUrl); // Make sure this method returns the correct video ID.
        if (videoId == null) {
            // Handle error: the video ID couldn't be extracted
            return;
        }
        String frameVideo = "<html><body><iframe width=\"100%\" height=\"100%\" " +
                "src=\"https://www.youtube.com/embed/"
                + videoId + "\" frameborder=\"0\" allowfullscreen></iframe></body></html>";
        Web.getSettings().setJavaScriptEnabled(true);
        Web.getSettings().setLoadWithOverviewMode(true);
        Web.getSettings().setUseWideViewPort(true);
        Web.getSettings().setDomStorageEnabled(true);
        Web.loadData(frameVideo, "text/html", "utf-8");
    }

}