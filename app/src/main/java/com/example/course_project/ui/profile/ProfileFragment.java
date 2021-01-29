package com.example.course_project.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.course_project.R;
import com.example.course_project.data.db.login.LoginDataSource;
import com.example.course_project.data.db.login.LoginRepository;
import com.example.course_project.event.SuccessfulLogin;
import com.example.course_project.ui.login.LoginFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ProfileFragment extends Fragment {
    private final LoginRepository loginRepository = LoginRepository.getInstance(new LoginDataSource());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view.findViewById(R.id.logout).setOnClickListener(v -> {
            loginRepository.logout();
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_container_view, new LoginFragment()).commit();
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onStop() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

        super.onStop();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onUserLoggedInSuccessfully(SuccessfulLogin event) {
        if (event.getLoggedInUser() != null) {
            ((TextView) requireActivity().findViewById(R.id.profile_name)).setText(event.getLoggedInUser().getDisplayName());
        }
    }
}
