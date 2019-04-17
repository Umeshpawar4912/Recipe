package pawar.umesh.cookr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Loginwindow extends AppCompatActivity {
        Button recipebox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_loginwindow );

        recipebox = (Button) findViewById ( R.id.bu );



        recipebox.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent ( Loginwindow.this ,LogInUserRecpiVewer.class );
                startActivity ( intent );
            }
        } );
    }
}
