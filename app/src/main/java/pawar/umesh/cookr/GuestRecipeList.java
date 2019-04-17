package pawar.umesh.cookr;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GuestRecipeList extends AppCompatActivity {

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
        setContentView ( R.layout.activity_guest_recipe_list );


        recycleView=(RecyclerView)findViewById ( R.id.recycle_view );
        recycleView.setLayoutManager ( new LinearLayoutManager ( this ) );

        firebaseDatabase = FirebaseDatabase.getInstance ();
        databaseReference= firebaseDatabase.getReference ("EDMT_FIREBASE");
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
