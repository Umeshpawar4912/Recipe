package pawar.umesh.cookr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInUserRecpiVewer extends AppCompatActivity {

    boolean fabExpanded = false;
    FloatingActionButton fabSettings;
    LinearLayout layoutFabSave;

    RecyclerView recycleView;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<Post> options;
    FirebaseRecyclerAdapter<Post,MyRecyclerViewHolder> adapter;

    Post selectedpost;
    String selectedKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_log_in_user_recpi_vewer );
        Toolbar toolbar = (Toolbar) findViewById ( R.id.toolbar );
        setSupportActionBar ( toolbar );

        recycleView=(RecyclerView)findViewById ( R.id.recycle_view );
        recycleView.setLayoutManager ( new LinearLayoutManager ( this ) );

        firebaseDatabase = FirebaseDatabase.getInstance ();
        databaseReference= firebaseDatabase.getReference ("EDMT_FIREBASE");
        fabSettings = (FloatingActionButton) this.findViewById(R.id.fabSetting);

        layoutFabSave = (LinearLayout) this.findViewById(R.id.layoutFabSave);

        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded == true){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });


        layoutFabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent ( LogInUserRecpiVewer.this ,Recipe_List.class );
                startActivity ( intent );
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById ( R.id.fab );
        fab.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                Snackbar.make ( view, "Replace with your own action", Snackbar.LENGTH_LONG )
                        .setAction ( "Action", null ).show ();
            }
        } );*/
        databaseReference.addValueEventListener ( new ValueEventListener () {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                displayComment ();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );


    }

    //closes FAB submenus
    private void closeSubMenusFab(){
        layoutFabSave.setVisibility(View.INVISIBLE);
        fabSettings.setImageResource(R.drawable.ic_add_black_24dp);
        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab(){
        layoutFabSave.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        fabSettings.setImageResource(R.drawable.ic_close_black_24dp);
        fabExpanded = true;
    }
    private void displayComment() {

        options =
                new FirebaseRecyclerOptions.Builder<Post> ().setQuery ( databaseReference,Post.class ).build ();
        adapter =
                new FirebaseRecyclerAdapter<Post, MyRecyclerViewHolder> (options) {
                    @Override
                    protected void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position, @NonNull final Post model) {
                        holder.txt_title.setText ( model.getTitle () );
                        holder.txt_content.setText ( model.getContent () );

                        holder.setiItemClickListener ( new IItemClickListener () {
                            @Override
                            public void onClick(View view, int position) {
                                selectedpost=model;
                                selectedKey = getSnapshots ().getSnapshot ( position ).getKey ();
                                Log.d ( "KEy Item", "onClick: "+selectedKey );

                                //Bind Data
                                // edt_content.setText ( model.getContent () );
                                //  edt_title.setText ( model.getTitle () );
                            }
                        } );
                    }

                    @NonNull
                    @Override
                    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View itemView = LayoutInflater.from ( getBaseContext () ).inflate ( R.layout.post_item,viewGroup,false );
                        return new MyRecyclerViewHolder ( itemView );
                    }
                };
        adapter.startListening ();
        recycleView.setAdapter ( adapter );
    }

}
