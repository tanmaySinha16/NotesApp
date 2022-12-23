package com.example.notes.activities

import android.app.Activity
import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notes.R
import com.example.notes.adapter.NotesAdapter
import com.example.notes.databinding.ActivityMainBinding
import com.example.notes.db.NoteDatabase
import com.example.notes.models.Note
import com.example.notes.models.NoteViewModel

class MainActivity : AppCompatActivity(),NotesAdapter.NotesClickListener,PopupMenu.OnMenuItemClickListener{

    private lateinit var binding:ActivityMainBinding
    private lateinit var database:NoteDatabase
    private lateinit var viewModel: NoteViewModel
    lateinit var adapter: NotesAdapter
    lateinit var selectedNote: Note
    private val updateNote = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode== Activity.RESULT_OK)
        {
            val note = it.data?.getSerializableExtra("note") as Note
            if(note!=null)
            {
                viewModel.updateNode(note)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )

        initUI()
        viewModel=ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(NoteViewModel::class.java)

        viewModel.allnotes.observe(this, Observer { list ->
            list?.let {
                adapter.updateList(list)
            }
        })
        database = NoteDatabase.getDatabase(this)
    }

    private fun initUI() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager =StaggeredGridLayoutManager(2,LinearLayout.VERTICAL)
        adapter = NotesAdapter(this,this)
        binding.recyclerView.adapter = adapter

        val getContent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == Activity.RESULT_OK)
            {
                val note = it.data?.getSerializableExtra("note")as? Note
                if(note!=null)
                {
                    viewModel.insertNode(note)

                }
            }
        }

        binding.fbAdd.setOnClickListener{
            val intent = Intent(this,AddNote::class.java)
            getContent.launch(intent)
        }

        binding.searchView.setOnQueryTextListener(object:androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
               if(newText!=null)
               {
                   adapter.filterList(newText)
               }
                return true
            }

        })
    }

    override fun onItemClicked(note: Note) {
       val intent = Intent(this@MainActivity,AddNote::class.java)
        intent.putExtra("current_note",note)
        updateNote.launch(intent)
    }

    override fun onLongItemClicked(note: Note, cardView: CardView) {
        selectedNote = note
        popUpDisplay(cardView)
    }

    private fun popUpDisplay(cardView: CardView) {
        val popup = PopupMenu(this,cardView)
        popup.setOnMenuItemClickListener(this@MainActivity)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.deleteNote){
            viewModel.deleteNote(selectedNote)
            return true
        }
        return false
    }
}