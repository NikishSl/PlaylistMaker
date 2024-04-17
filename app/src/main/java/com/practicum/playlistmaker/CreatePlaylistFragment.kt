package com.practicum.playlistmaker

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


class CreatePlaylistFragment : Fragment() {

    companion object {
        fun newInstance() = CreatePlaylistFragment()
    }

    private val viewModel: CreatePlaylistViewModel by viewModel()
    private lateinit var backButton: ImageButton
    private lateinit var createButton: Button
    private lateinit var nameEditText: TextInputEditText
    private lateinit var imageCreateButton: ImageView
    private var hasUnsavedChanges: Boolean = false
    private var selectedImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_playlist, container, false)
        createButton = view.findViewById(R.id.buttonCreate)
        nameEditText = view.findViewById(R.id.editTextNameInput)
        backButton = view.findViewById(R.id.create_playlist_back_button)
        imageCreateButton = view.findViewById(R.id.imageCreateButton)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                selectedImageUri = uri
                Glide.with(this)
                    .load(uri)
                    .transform(CenterCrop(), RoundedCorners(20))
                    .into(imageCreateButton)
            }
        }

        backButton.setOnClickListener {
            if (hasUnsavedChanges) {
                showConfirmationDialog()
            } else {
                requireActivity().onBackPressed()
            }
        }

        createButton.setOnClickListener {
            val playlistName = nameEditText.text.toString()
            val playlistDescription = ""
            val playlistCoverImageUri = selectedImageUri

            val playlist = PlaylistEntity(
                name = playlistName,
                description = playlistDescription,
                coverImageFilePath = playlistCoverImageUri?.let { saveImageToPrivateStorage(it, playlistName) } ?: ""
            )
            viewModel.savePlaylist(playlist)

            val message = "Плейлист \"$playlistName\" создан"
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

            requireActivity().onBackPressed()
        }

        viewModel.backButtonClicked.observe(viewLifecycleOwner, Observer {
            requireActivity().onBackPressed()
        })

        viewModel.isCreateButtonEnabled.observe(viewLifecycleOwner, Observer { isEnabled ->
            createButton.isEnabled = isEnabled
            if (isEnabled) {
                createButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.YPBlue)
            } else {
                createButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.YPGray)
            }
        })


        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val isNameEmpty = s.isNullOrEmpty()
                viewModel.updateCreateButtonState(!isNameEmpty)
                hasUnsavedChanges = true
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        imageCreateButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            hasUnsavedChanges = true
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setPositiveButton("Завершить") { dialog, _ ->
                dialog.dismiss()
                requireActivity().onBackPressed()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun saveImageToPrivateStorage(uri: Uri, playlistName: String): String {
        uri ?: return ""
        val filePath = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "PlaylistMaker_AlbumImages")
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val imageName = "${playlistName.replace("\\s+".toRegex(), "")}_cover.jpg"
        val file = File(filePath, imageName)
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        BitmapFactory.decodeStream(inputStream).compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return file.absolutePath
    }
}