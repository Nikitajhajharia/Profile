package com.example.andazkumar

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.andazkumar.databinding.ActivityProfileBinding
import com.bumptech.glide.Glide

class ProfileActivity : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var editSignImage: ImageView
    private lateinit var sinceTextView: TextView
    private lateinit var profileImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize views
        nameTextView = findViewById(R.id.name)
        editSignImage = findViewById(R.id.edit_sign)
        sinceTextView = findViewById(R.id.since)
        profileImageView = findViewById(R.id.profileImage)

        // Ensure clip to outline for circular image
        profileImageView.clipToOutline = true

        // Set initial name from intent (from MainActivity)
        val nameFromMain = intent.getStringExtra("USER_NAME")
        nameTextView.text = nameFromMain ?: "Your Name"

        // Listen for updated data from EditFragment
        supportFragmentManager.setFragmentResultListener("editProfileResult", this) { _, bundle ->
            val updatedName = bundle.getString("name")
            val updatedSince = bundle.getString("since")
            val updatedImageUri = bundle.getString("imageUri")

            updatedName?.let {
                nameTextView.text = it
            }

            updatedSince?.let {
                sinceTextView.text = it
            }

            updatedImageUri?.let {
                val uri = Uri.parse(it)

                // Use Glide for proper scaling and cropping
                Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .into(profileImageView)

                // Optionally tag it to retrieve later
                profileImageView.tag = uri
            }
        }

        // Open EditFragment with current data when edit icon is clicked
        editSignImage.setOnClickListener {
            val editFragment = EditFragment().apply {
                arguments = Bundle().apply {
                    putString(EditFragment.ARG_NAME, nameTextView.text.toString())
                    putString(EditFragment.ARG_SINCE, sinceTextView.text.toString())

                    val currentUri = profileImageView.tag as? Uri
                    currentUri?.let {
                        putString(EditFragment.ARG_IMAGE_URI, it.toString())
                    }
                }
            }

            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, editFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
