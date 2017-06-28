package map.dev.ipath;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import map.dev.ipath.R;
//import com.example.mapdemo.R;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * Created by adrian on 12.03.2017.
 */

public class CreateRouteActivity extends Activity implements View.OnClickListener{

    private ImageButton btnBack;

    private EditText etMyLocation;
    private EditText etDestination;

    private FancyButton btnRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);

        initWidget();
    }

    private void initWidget() {
        btnBack = (ImageButton) findViewById(R.id.btnBack);

        etMyLocation = (EditText) findViewById(R.id.etMyLocation);
        etDestination = (EditText) findViewById(R.id.etDestination);

        btnRoute = (FancyButton) findViewById(R.id.btnRoute);

        // -----------------------------------------------------------------------------------------

        btnBack.setOnClickListener(this);
        btnRoute.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnRoute:
                finish();
                break;
        }
    }
}
