package com.example.notes

import android.content.ContentValues
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_notes.*

class AddNotes : AppCompatActivity() {
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        try {
            var bundle: Bundle? = intent.extras
            id = bundle!!.getInt("ID", 0)
            if (id != 0) {
                btnAdd.text = "EDIT"
                txtTitle.setText(bundle.getString("Title"))
                txtContent.setText(bundle.getString("Description"))
            }
        } catch (ex: Exception) {
        }
        btnAdd.setOnClickListener {
            var dbManager = DBManager(this)
            var values = ContentValues()
            values.put("Title", txtTitle.text.toString())
            values.put("Description", txtContent.text.toString())
            if (id == 0) {
                val ID = dbManager.Insert(values)
                if (ID!! > 0) {
                    Toast.makeText(this, "Note Added Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Add note failed :(", Toast.LENGTH_SHORT).show()
                }
            } else {
                val ID = dbManager.update(values, "ID=?", arrayOf(id.toString()))
                if (ID!! > 0) {
                    Toast.makeText(this, "Note Updated Successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "update note failed :(", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}
