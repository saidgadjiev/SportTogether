package ru.mail.sporttogether.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import ru.mail.sporttogether.R;
import ru.mail.sporttogether.auth.core.SocialNetwork;
import ru.mail.sporttogether.auth.core.SocialNetworkManager;
import ru.mail.sporttogether.auth.facebook.FacebookSocialNetwork;
import ru.mail.sporttogether.auth.vk.VKSocialNetwork;
import ru.mail.sporttogether.databinding.ActivityTestBinding;

public class TestActivity extends AppCompatActivity {

    private Button vkShare;
    private Button facebookShare;
    private Button googleShare;
    private Button logout;
    private ActivityTestBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_test);
        vkShare = binding.vkShare;
        facebookShare = binding.facebookShare;
        googleShare = binding.googleShare;
        logout = binding.logout;
        vkShare.setOnClickListener(listener);
        facebookShare.setOnClickListener(listener);
        googleShare.setOnClickListener(listener);
        logout.setOnClickListener(logoutListener);
    }

    View.OnClickListener logoutListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Integer networkID = SocialNetworkManager.getInstance().getSharedPreferces().getInt(SocialNetworkManager.AUTH_ID, 0);
            SocialNetwork socialNetwork = SocialNetworkManager.getInstance().getSocialNetwork(networkID);

            socialNetwork.logout();
            startActivity(new Intent(TestActivity.this, LoginActivity.class));
        }
    };

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.vk_share:
                    SocialNetwork socialNetwork = SocialNetworkManager.getInstance().getSocialNetwork(VKSocialNetwork.ID);

                    socialNetwork.sharePost(TestActivity.this);
                    break;
                case R.id.facebook_share:
                    postFacebook();
                    break;
                case R.id.google_share:
                    break;
                default:
                    break;
            }
        }
    };

    private void postFacebook() {
        SocialNetwork socialNetwork = SocialNetworkManager.getInstance().getSocialNetwork(FacebookSocialNetwork.ID);

        socialNetwork.sharePost(this);
    }
}
