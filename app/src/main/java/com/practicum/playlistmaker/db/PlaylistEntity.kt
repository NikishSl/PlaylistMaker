package com.practicum.playlistmaker.db

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val playlistId: Long = 0,
    val name: String,
    val description: String,
    val coverImageFilePath: String,
    val trackIds: String = "",
    val trackCount: Int = 0
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(playlistId)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(coverImageFilePath)
        parcel.writeString(trackIds)
        parcel.writeInt(trackCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PlaylistEntity> {
        override fun createFromParcel(parcel: Parcel): PlaylistEntity {
            return PlaylistEntity(parcel)
        }

        override fun newArray(size: Int): Array<PlaylistEntity?> {
            return arrayOfNulls(size)
        }
    }
}
