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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Recipe_List extends AppCompatActivity {
    EditText edt_title,edt_content;
    Button btn_post,btn_update,btn_delete;
    RecyclerView recycleView;

    //Firebase
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<Post>  options;
    FirebaseRecyclerAdapter<Post,MyRecyclerViewHolder> adapter;

    Post selectedpost;
    String selectedKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_recipe__list );

        edt_title = (EditText)findViewById ( R.id.edt_title );
        edt_content = (EditText)findViewById ( R.id.edt_content );
        btn_post = (Button)findViewById ( R.id.btn_post );
        btn_update = (Button)findViewById ( R.id.btn_update );
        btn_delete = (Button)findViewById ( R.id.btn_delete );
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
        btn_post.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                postcomment();
            }
        } );

        btn_update.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
            databaseReference
                    .child ( selectedKey )
                    .setValue ( new Post ( edt_title.getText().toString (),edt_content.getText ().toString ()) )
                    .addOnSuccessListener ( new OnSuccessListener<Void> () {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText ( Recipe_List.this,"Updatted",Toast.LENGTH_LONG ).show ();
                        }
                    } ).addOnFailureListener ( new OnFailureListener () {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText ( Recipe_List.this,""+e.getMessage (),Toast.LENGTH_LONG ).show ();
                }
            } );
            }
        } );

        btn_delete.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View view) {
                databaseReference
                        .child ( selectedKey )
                        .removeValue ()
                        .addOnSuccessListener ( new OnSuccessListener<Void> () {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText ( Recipe_List.this,"Deleted",Toast.LENGTH_LONG ).show ();
                            }
                        } ).addOnFailureListener ( new OnFailureListener () {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText ( Recipe_List.this,""+e.getMessage (),Toast.LENGTH_LONG ).show ();
                    }
                } );
            }
        } );

        displayComment();
    }

    @Override
    protected void onStop() {
        if(adapter !=null)
            adapter.stopListening ();
        super.onStop ();
    }

    private void postcomment() {
        String title= edt_title.getText ().toString ();
        String content= edt_content.getText ().toString ();

        Post post =new Post(title,content);
        databaseReference.push ()//Use this methd tounique Comment
        .setValue ( post );

        adapter.notifyDataSetChanged ();
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
                            edt_content.setText ( model.getContent () );
                            edt_title.setText ( model.getTitle () );
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
