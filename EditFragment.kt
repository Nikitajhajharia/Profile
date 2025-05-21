package com.example.andazkumar

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts

class EditFragment : Fragment() {

    private lateinit var profileImageView: ImageView
    private lateinit var changeImageButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var sinceEditText: EditText
    private lateinit var saveButton: Button

    private var selectedImageUri: Uri? = null

    companion object {
        const val ARG_NAME = "arg_name"
        const val ARG_SINCE = "arg_since"
        const val ARG_IMAGE_URI = "arg_image_uri"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit, container, false)

        profileImageView = view.findViewById(R.id.edit_profile_image)
        changeImageButton = view.findViewById(R.id.photoButton)
        nameEditText = view.findViewById(R.id.edit_name)
        sinceEditText = view.findViewById(R.id.editSinceEditText)
        saveButton = view.findViewById(R.id.save_button)

        // ✅ Enable clipping to the circular outline
        profileImageView.clipToOutline = true

        // Populate existing data if available
        val name = arguments?.getString(ARG_NAME)
        val since = arguments?.getString(ARG_SINCE)
        val imageUriString = arguments?.getString(ARG_IMAGE_URI)

        nameEditText.setText(name)
        sinceEditText.setText(since)

        if (imageUriString != null) {
            selectedImageUri = Uri.parse(imageUriString)
            profileImageView.setImageURI(selectedImageUri)
        }

        // Image picker
        changeImageButton.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Save button sends data back to previous fragment
        saveButton.setOnClickListener {
            val updatedName = nameEditText.text.toString()
            val updatedSince = sinceEditText.text.toString()

            val result = Bundle().apply {
                putString("name", updatedName)
                putString("since", updatedSince)
                selectedImageUri?.let { putString("imageUri", it.toString()) }
            }

            parentFragmentManager.setFragmentResult("editProfileResult", result)
            parentFragmentManager.popBackStack()
        }

        return view
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            profileImageView.setImageURI(it)
        }
    }
}

