package es.uva.inf5g.psm.guessthesong.modelos.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import es.uva.inf5g.psm.guessthesong.modelos.song.Song
import es.uva.inf5g.psm.guessthesong.modelos.song.SongDoc

object SongRepository {

    private val db = Firebase.firestore
    private val songsCollection = db.collection("songs")

    fun getSongsByLevel(
        level: String,
        onResult: (List<SongDoc>) -> Unit,
        onError: (Exception) -> Unit
    ) {

        songsCollection
            .whereEqualTo("level", level)
            .get()
            .addOnSuccessListener { snapshot ->

                val songs = snapshot.documents.mapNotNull { doc ->

                    val song = doc.toObject(Song::class.java)
                        ?: return@mapNotNull null

                    SongDoc(
                        id = doc.id,
                        data = song
                    )
                }
                    .sortedBy { it.data.order } // 🔥 ORDEN LOCAL (FIX)

                Log.d("SONGS_REPO", "✔ Songs loaded: ${songs.size}")

                onResult(songs)
            }
            .addOnFailureListener { e ->

                Log.e("SONGS_REPO", "💥 ERROR FIRESTORE", e)

                onError(e)
            }
    }

    fun listenSong(
        songId: String,
        onChange: (SongDoc?) -> Unit
    ): ListenerRegistration {

        return songsCollection
            .document(songId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    Log.e("SONGS_REPO", "Listener error", error)
                    onChange(null)
                    return@addSnapshotListener
                }

                if (snapshot == null || !snapshot.exists()) {
                    onChange(null)
                    return@addSnapshotListener
                }

                val song = snapshot.toObject(Song::class.java)

                onChange(
                    song?.let {
                        SongDoc(snapshot.id, it)
                    }
                )
            }
    }

    fun solveSongTransaction(
        songId:String,
        onResult:(Boolean,String?)->Unit
    ){

        val songRef =
            songsCollection.document(songId)



        db.runTransaction { transaction ->


            val snapshot =
                transaction.get(songRef)



            val solved =
                snapshot.getBoolean("solved")
                    ?: false



            if(solved){

                throw Exception(
                    "La canción ya ha sido resuelta"
                )

            }



            transaction.update(
                songRef,
                "solved",
                true
            )


        }

            .addOnSuccessListener {


                onResult(
                    true,
                    null
                )


            }

            .addOnFailureListener {


                onResult(
                    false,
                    it.message
                )


            }


    }
}