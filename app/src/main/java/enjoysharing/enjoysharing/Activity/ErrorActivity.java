package enjoysharing.enjoysharing.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import enjoysharing.enjoysharing.DataObject.ParameterCollection;
import enjoysharing.enjoysharing.R;
import enjoysharing.enjoysharing.Business.BusinessBase;

public class ErrorActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SetContext(ErrorActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        business = new BusinessBase(ErrorActivity.this);

        Intent i = getIntent();
        String errorString = (String)i.getSerializableExtra("ErrorString");
        TextView txtError = (TextView) findViewById(R.id.txtError);
        txtError.setText(errorString);
    }

    @Override
    public void onBackPressed() {
        StandardOnBackPressed();
    }

}
