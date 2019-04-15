package enjoysharing.enjoysharing.Activity;

import android.os.Bundle;
import android.widget.TextView;
import enjoysharing.enjoysharing.R;
import enjoysharing.enjoysharing.Business.BusinessBase;

public class ErrorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(ErrorActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        business = new BusinessBase(ErrorActivity.this);

        TextView txtError = (TextView) findViewById(R.id.txtError);
        txtError.setText("Errore A caso");
    }

    @Override
    public void onBackPressed() {
        StandardOnBackPressed();
    }

}
