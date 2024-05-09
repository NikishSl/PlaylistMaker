package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PlaylistOpenFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistOpenFragment()
    }

    private val viewModel: PlaylistOpenViewModel by viewModel()
    private lateinit var backButton: ImageButton
    private lateinit var playlystOpenName: TextView
    private lateinit var playlistOpenDescription: TextView
    private lateinit var playlistOpenSumTime: TextView
    private lateinit var playlistOpenQuantityTracks: TextView
    private lateinit var playlistOpenImage: ImageView
    private lateinit var playlistRecyclerOpenBS: RecyclerView
    private lateinit var playlistOpenAdapter: PlaylistOpenAdapter

    private lateinit var playlistOpenShare: ImageView
    private lateinit var playlistOpenMenu: ImageView

    private lateinit var overlayPlaylistOpen: View
    private lateinit var playlistOpenBottomSheetMenu: LinearLayout
    private lateinit var playlistOpenImageBs: ImageView
    private lateinit var playlistOpenNameBs: TextView
    private lateinit var playlistOpenQuantityTracksBs: TextView
    private lateinit var playlistOpenShareBs: TextView
    private lateinit var playlistOpenEditBs: TextView
    private lateinit var playlistOpenDeleteBs: TextView

    private lateinit var playlistOpenEmptyList: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_playlist_open, container, false)
        backButton = view.findViewById(R.id.playlist_open_back_button)
        playlystOpenName = view.findViewById(R.id.playlyst_open_name)
        playlistOpenDescription = view.findViewById(R.id.playlist_open_description)
        playlistOpenSumTime = view.findViewById(R.id.playlist_open_sum_time)
        playlistOpenQuantityTracks = view.findViewById(R.id.playlist_open_quantity_tracks)
        playlistOpenImage = view.findViewById(R.id.playlist_open_image)
        playlistRecyclerOpenBS = view.findViewById(R.id.playlist_open_recycler_bottom_sheet)

        playlistOpenShare = view.findViewById(R.id.playlist_open_share)
        playlistOpenMenu = view.findViewById(R.id.playlist_open_more)

        overlayPlaylistOpen = view.findViewById(R.id.overlay_playlist_open)
        playlistOpenBottomSheetMenu = view.findViewById(R.id.playlist_open_bottom_sheet_menu)
        playlistOpenImageBs = view.findViewById(R.id.playlist_open_image_bs)
        playlistOpenNameBs = view.findViewById(R.id.playlyst_open_name_bs)
        playlistOpenQuantityTracksBs = view.findViewById(R.id.playlist_open_quantity_tracks_bs)
        playlistOpenShareBs = view.findViewById(R.id.playlist_open_share_bs)
        playlistOpenEditBs = view.findViewById(R.id.playlist_open_edit_bs)
        playlistOpenDeleteBs = view.findViewById(R.id.playlist_open_delete_bs)

        playlistOpenEmptyList = view.findViewById(R.id.playlist_open_empty_list)

        val bottomSheetBehaviorMenuBs = BottomSheetBehavior.from(playlistOpenBottomSheetMenu).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        playlistOpenAdapter = PlaylistOpenAdapter(emptyList(),
            onTrackClick = { track ->
                playlistOpenAdapter.navigateToPlaylistOpenFragment(track, findNavController())
            },
            onTrackLongClickListener = { track ->
            val dialogOpenFr = AlertDialog.Builder(requireContext())
                .setTitle("Удалить трек")
                .setMessage("Вы уверены, что хотите удалить трек ${track.trackName}?")
                .setNegativeButton("Нет", null)
                .setPositiveButton("Да") { dialog, which -> val playlistId = arguments?.getLong("playlistId") ?: -1
                    viewModel.deleteTrackFromPlaylist(playlistId, track.trackId)
                }
                val dialog = dialogOpenFr.show()
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.YPBlue))
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.YPBlue))
        })

        bottomSheetBehaviorMenuBs.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        overlayPlaylistOpen.visibility = View.GONE
                    }
                    else -> {
                        overlayPlaylistOpen.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        playlistOpenMenu.setOnClickListener {
            bottomSheetBehaviorMenuBs.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        playlistOpenDeleteBs.setOnClickListener {
            val context = view.context
            viewModel.playlist.value?.let { playlist ->
                val playlistId = playlist.playlistId
                AlertDialog.Builder(context)
                    .setTitle("Удалить плейлист")
                    .setMessage("Хотите удалить плейлист?")
                    .setNegativeButton("Нет") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Да") { dialog, _ ->
                        viewModel.deletePlaylist(playlistId)
                        findNavController().popBackStack()
                    }
                    .show()
                    .apply {
                        getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.YPBlue))
                        getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(ContextCompat.getColor(requireContext(), R.color.YPBlue))
                    }
            }
        }

        playlistOpenShare.setOnClickListener { sharePlaylistOnClick() }
        playlistOpenShareBs.setOnClickListener { sharePlaylistOnClick() }

        playlistOpenEditBs.setOnClickListener {
            val playlistToEdit = viewModel.playlist.value
            val navController = findNavController()
            navController.navigate(R.id.createPlaylistFragment, Bundle().apply {
                putParcelable("playlist", playlistToEdit)
            })
        }

        playlistRecyclerOpenBS.adapter = playlistOpenAdapter
        val layoutOpenManager = LinearLayoutManager(requireContext())
        playlistRecyclerOpenBS.layoutManager = layoutOpenManager


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.tracksOpen.observe(viewLifecycleOwner, Observer { tracksOpen ->
            playlistOpenAdapter.updatePlaylists(tracksOpen)
        })


        backButton.setOnClickListener {
                requireActivity().onBackPressed()
        }

        viewModel.backButtonClickedPL.observe(viewLifecycleOwner, Observer {
            requireActivity().onBackPressed()
        })

        viewModel.playlist.observe(viewLifecycleOwner, Observer { playlist ->
            playlystOpenName.text = playlist?.name
            playlistOpenNameBs.text = playlist?.name
            playlistOpenDescription.text = playlist?.description
            if (playlist != null) {
                playlistOpenQuantityTracks.text = changeTextTrackWithNumb(playlist.trackCount)
                playlistOpenQuantityTracksBs.text = changeTextTrackWithNumb(playlist.trackCount)
            }
            val file = File(playlist?.coverImageFilePath)

            Glide.with(requireContext())
                .load(file)
                .transform(CenterCrop())
                .placeholder(R.drawable.ic_placeholder_med)
                .into(playlistOpenImage)

            Glide.with(requireContext())
                .load(file)
                .transform(CenterCrop(),RoundedCorners(dpToPx(requireContext(), 2f)))
                .placeholder(R.drawable.ic_placeholder)
                .into(playlistOpenImageBs)
        })

        viewModel.formattedDuration.observe(viewLifecycleOwner, Observer { formattedTime ->
            playlistOpenSumTime.text = formattedTime
        })

        viewModel.tracksOpen.observe(viewLifecycleOwner, Observer { tracksOpen ->
            if (tracksOpen.isEmpty()) {
                toggleEmptyListVisibility(true)
            } else {
                toggleEmptyListVisibility(false)
                playlistOpenAdapter.updatePlaylists(tracksOpen)
            }
        })

        val playlistId = arguments?.getLong("playlistId") ?: -1
        viewModel.loadPlaylist(playlistId)
    }

    private fun sharePlaylistOnClick() {
        viewModel.playlist.value?.let { playlist ->
            viewModel.tracksOpen.value?.let { tracks ->
                viewModel.sharePlaylist(this@PlaylistOpenFragment, playlist, tracks)
            }
        }
    }

    private fun toggleEmptyListVisibility(isEmpty: Boolean) {
        if (isEmpty) {
            playlistOpenEmptyList.visibility = View.VISIBLE
            playlistRecyclerOpenBS.visibility = View.GONE
        } else {
            playlistOpenEmptyList.visibility = View.GONE
            playlistRecyclerOpenBS.visibility = View.VISIBLE
        }
    }

}