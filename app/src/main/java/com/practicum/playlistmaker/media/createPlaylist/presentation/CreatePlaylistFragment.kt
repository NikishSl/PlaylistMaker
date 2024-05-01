package com.practicum.playlistmaker.media.createPlaylist.presentation

import android.annotation.SuppressLint
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
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.textfield.TextInputEditText
import com.practicum.playlistmaker.db.PlaylistEntity
import com.practicum.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream


class CreatePlaylistFragment : Fragment() {

    companion object {
        fun newInstance(playlistToEdit: PlaylistEntity? = null): CreatePlaylistFragment {
            val fragment = CreatePlaylistFragment()
            val args = Bundle()
            args.putParcelable("playlist", playlistToEdit)
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: CreatePlaylistViewModel by viewModel()
    private lateinit var backButton: ImageButton
    private lateinit var createButton: Button
    private lateinit var nameEditText: TextInputEditText
    private lateinit var description: TextInputEditText
    private lateinit var imageCreateButton: ImageView
    private lateinit var createPlaylistTitle: TextView
    private var hasUnsavedChanges: Boolean = false
    private var selectedImageUri: Uri? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_playlist, container, false)
        createButton = view.findViewById(R.id.buttonCreate)
        nameEditText = view.findViewById(R.id.editTextNameInput)
        description = view.findViewById(R.id.editTextDescription)
        backButton = view.findViewById(R.id.create_playlist_back_button)
        createPlaylistTitle = view.findViewById(R.id.create_playlist_title)
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

        val playlistToEdit: PlaylistEntity? = arguments?.getParcelable("playlist")

        if (playlistToEdit != null) {
            nameEditText.setText(playlistToEdit.name)
            description.setText(playlistToEdit.description)
            if (playlistToEdit.coverImageFilePath.isNotEmpty()) {
                Glide.with(this)
                    .load(File(playlistToEdit.coverImageFilePath))
                    .transform(CenterCrop(), RoundedCorners(20))
                    .into(imageCreateButton)
            }
            createButton.text = getString(R.string.save)
            createPlaylistTitle.text = getString(R.string.edit)
        }

        backButton.setOnClickListener {
            if (playlistToEdit != null) {
                requireActivity().onBackPressed()
            } else {
                if (hasUnsavedChanges) {
                    showConfirmationDialog()
                } else {
                    requireActivity().onBackPressed()
                }
            }
        }

        createButton.setOnClickListener {
            val playlistName = nameEditText.text.toString()
            val playlistDescription = description.text.toString()
            val playlistCoverImageUri = selectedImageUri

            if (playlistToEdit != null) {
                val updatedPlaylist = playlistToEdit.copy(
                    name = playlistName,
                    description = playlistDescription,
                    coverImageFilePath = playlistCoverImageUri?.let { saveImageToPrivateStorage(it, playlistName) } ?: playlistToEdit.coverImageFilePath
                )
                viewModel.updatePlaylist(updatedPlaylist)
                val message = "Плейлист \"$playlistName\" обновлен"
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            } else {
                val newPlaylist = PlaylistEntity(
                    name = playlistName,
                    description = playlistDescription,
                    coverImageFilePath = playlistCoverImageUri?.let { saveImageToPrivateStorage(it, playlistName) } ?: ""
                )
                viewModel.savePlaylist(newPlaylist)
                val message = "Плейлист \"$playlistName\" создан"
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
            requireActivity().onBackPressed()
        }

        viewModel.backButtonClicked.observe(viewLifecycleOwner, Observer {
            requireActivity().onBackPressed()
        })

        viewModel.isCreateButtonEnabled.observe(viewLifecycleOwner, Observer { isEnabled ->
            if (playlistToEdit != null) {
                createButton.isEnabled = true
                createButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.YPBlue)
            } else {
                createButton.isEnabled = isEnabled
                if (isEnabled) {
                    createButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.YPBlue)
                } else {
                    createButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.YPGray)
                }
            }
        })


        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val isNameEmpty = s.isNullOrEmpty()
                viewModel.updateCreateButtonState(!isNameEmpty)
                hasUnsavedChanges = true
                if (playlistToEdit != null && isNameEmpty) {
                    createButton.isEnabled = false
                    createButton.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.YPGray)
                }
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