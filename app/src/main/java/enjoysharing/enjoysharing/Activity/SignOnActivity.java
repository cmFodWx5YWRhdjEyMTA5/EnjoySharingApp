package enjoysharing.enjoysharing.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import enjoysharing.enjoysharing.Business.BusinessJSON;
import enjoysharing.enjoysharing.R;

public class SignOnActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_on);

        business = new BusinessJSON(SignOnActivity.this);

        Button btnBackToLogin = (Button) findViewById(R.id.btnBackToLogin);
        btnBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SwipeCloseRightActivity(SignOnActivity.this, LoginActivity.class);
            }
        });
    }
}
