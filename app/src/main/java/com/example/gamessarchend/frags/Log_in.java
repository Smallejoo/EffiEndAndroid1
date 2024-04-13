package com.example.gamessarchend.frags;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.gamessarchend.R;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


public class Log_in extends Fragment {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button subButt;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in, container, false);

        usernameEditText = view.findViewById(R.id.login_username);
        passwordEditText = view.findViewById(R.id.login_password);
        loginButton = view.findViewById(R.id.Log_in_button);
        db = FirebaseFirestore.getInstance();
        subButt = view.findViewById(R.id.Sub_Button);

        loginButton.setOnClickListener(v -> {
            loginUser(view);

        });

subButt.setOnClickListener(this::Sub);
        return view;
    }
private void Sub(View view)
{
    NavController navController = Navigation.findNavController(view);
    navController.navigate(R.id.action_log_in_to_subscribe);
}
    private void loginUser(View view) {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (!username.isEmpty() && !password.isEmpty()) {
            db.collection("users")
                    .whereEqualTo("username", username)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && task.getResult().size() > 0) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Login successful
                                    Toast.makeText(getActivity(), "Login successful!", Toast.LENGTH_SHORT).show();

                                    NavController nav= Navigation.findNavController(view);
                                    Bundle bundle=new Bundle();
                                    bundle.putString("username",username);
                                    nav.navigate(R.id.action_log_in_to_profile,bundle);
                                    break;
                                }
                            } else {
                                // User not found
                                Toast.makeText(getActivity(), "Invalid credentials!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Error getting documents
                            Toast.makeText(getActivity(), "Error logging in: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getActivity(), "Please enter all details", Toast.LENGTH_SHORT).show();
        }
    }



    public Log_in() {
    }

}