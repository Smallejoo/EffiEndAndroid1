package com.example.gamessarchend.frags;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gamessarchend.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Subscribe#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Subscribe extends Fragment {
    private EditText usernameEditText, passwordEditText, emailEditText, creditCardEditText;
    private Button signUpButton;
    private FirebaseFirestore db;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Subscribe() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Subscribe.
     */
    // TODO: Rename and change types and number of parameters
    public static Subscribe newInstance(String param1, String param2) {
        Subscribe fragment = new Subscribe();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subscribe, container, false);
        initializeUI(view);
        db = FirebaseFirestore.getInstance();

        signUpButton.setOnClickListener(v -> signUp(view));
        return view;
    }

    private void initializeUI(View view) {
        usernameEditText = view.findViewById(R.id.signup_username);
        passwordEditText = view.findViewById(R.id.signup_password);
        emailEditText = view.findViewById(R.id.signup_email);
        creditCardEditText = view.findViewById(R.id.signup_credit_card);
        signUpButton = view.findViewById(R.id.signup_button);
    }

    private void signUp(View view) {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String creditCard = creditCardEditText.getText().toString().trim();
        // Assume a method to get the selected subscription duration
        String subscriptionDuration = getSelectedSubscriptionDuration();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || creditCard.isEmpty()) {
            Toast.makeText(getContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("password", password); // Consider security implications
        user.put("email", email);
        user.put("creditCard", creditCard);
        user.put("subscriptionDuration", subscriptionDuration);
        user.put("gamesLiked", new ArrayList<>());

        db.collection("users").add(user)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                    NavController nav= Navigation.findNavController(view);
                    nav.navigate(R.id.action_subscribe_to_log_in);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error adding user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private String getSelectedSubscriptionDuration() {
        // Implement your logic to retrieve the subscription duration from UI
        return "1 year"; // Placeholder
    }

}