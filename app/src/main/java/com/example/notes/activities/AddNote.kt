package com.example.notes.activities

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.notes.R
import com.example.notes.databinding.ActivityAddNoteBinding
import com.example.notes.models.Note
import java.util.*
import java.util.logging.SimpleFormatter

class AddNote : AppCompatActivity() {

    private lateinit var binding:ActivityAddNoteBinding
    private lateinit var note: Note
    private lateinit var old_note:Note
    var isUpdate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        )

        try{
            old_note = intent.getSerializableExtra("current_note") as Note
            binding.etTitle.setText(old_note.title)
            binding.etText.setText(old_note.note)
            isUpdate = true
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }
        binding.imgCheck.setOnClickListener {
            val title = binding.etTitle.text.toString()
            val note_desc = binding.etText.text.toString()
            if(title.isNotEmpty() || note_desc.isNotEmpty())
            {
                val formatter = SimpleDateFormat("EEE, d MMM yyyy HH:mm a")
                if(isUpdate)
                {
                    note = Note(
                        old_note.id,
                        title,note_desc,formatter.format(Date())
                    )
                }else{
                    note = Note(null,title,note_desc,formatter.format(Date()))
                }

                val intent = Intent()
                intent.putExtra("note",note)
                setResult(Activity.RESULT_OK,intent)
                finish()

            }
            else{
                Toast.makeText(this@AddNote,"Please enter some data",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
        }
        binding.imgBackArrow.setOnClickListener {
            onBackPressed()
        }
    }
}