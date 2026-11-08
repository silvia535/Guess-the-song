package es.uva.inf5g.psm.guessthesong.modelos.repository

import com.google.firebase.Firebase
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import es.uva.inf5g.psm.guessthesong.modelos.player.Player


object PlayerRepository {


    private val db = Firebase.firestore

    private val playersCollection =
        db.collection("players")



    fun addPlayer(
        player: Player,
        onResult:(Boolean,String?)->Unit
    ){

        val ref =
            playersCollection.document(player.id)


        ref.set(player)

            .addOnSuccessListener {

                onResult(true,null)

            }

            .addOnFailureListener {

                onResult(
                    false,
                    it.message
                )

            }

    }





    fun getPlayerById(
        playerId:String,
        onResult:(Player?)->Unit
    ){

        playersCollection
            .document(playerId)
            .get()

            .addOnSuccessListener {

                onResult(
                    it.toObject(Player::class.java)
                )

            }

            .addOnFailureListener {

                onResult(null)

            }

    }





    fun getPlayers(

        onResult:(List<Player>)->Unit,
        onError:(Exception)->Unit

    ){

        playersCollection
            .get()

            .addOnSuccessListener { snapshot ->


                val list =

                    snapshot.documents.mapNotNull {


                        it.toObject(
                            Player::class.java
                        )

                    }


                onResult(list)

            }


            .addOnFailureListener {

                onError(it)

            }

    }





    fun addPoints(
        playerId:String,
        points:Int,
        onResult:(Boolean,String?)->Unit
    ){

        val ref =
            playersCollection.document(playerId)



        db.runTransaction { transaction ->


            val snapshot =
                transaction.get(ref)


            val current =
                snapshot.getLong("score")
                    ?:0


            transaction.update(
                ref,
                "score",
                current + points
            )


        }

            .addOnSuccessListener {

                onResult(true,null)

            }

            .addOnFailureListener {

                onResult(
                    false,
                    it.message
                )

            }

    }






    fun updateScore(

        playerId:String,
        newScore:Int,
        onResult:(Boolean,String?)->Unit

    ){


        playersCollection
            .document(playerId)

            .update(
                "score",
                newScore
            )


            .addOnSuccessListener {

                onResult(true,null)

            }


            .addOnFailureListener {

                onResult(
                    false,
                    it.message
                )

            }

    }






    fun deletePlayer(

        playerId:String,
        onResult:(Boolean,String?)->Unit

    ){

        playersCollection
            .document(playerId)

            .delete()


            .addOnSuccessListener {

                onResult(true,null)

            }


            .addOnFailureListener {

                onResult(
                    false,
                    it.message
                )

            }


    }






    fun listenPlayer(

        playerId:String,

        onChange:(Player?)->Unit

    ):ListenerRegistration{


        return playersCollection

            .document(playerId)

            .addSnapshotListener { snapshot,error ->



                if(error!=null ||
                    snapshot==null ||
                    !snapshot.exists()
                ){

                    onChange(null)

                    return@addSnapshotListener

                }



                onChange(

                    snapshot.toObject(
                        Player::class.java
                    )

                )

            }

    }







    // 🔥 RANKING EN TIEMPO REAL

    fun listenRanking(

        onChange:(List<Player>)->Unit

    ):ListenerRegistration{


        return playersCollection


            .orderBy(
                "score",
                Query.Direction.DESCENDING
            )


            .limit(50)


            .addSnapshotListener { snapshot,error ->



                if(error!=null){

                    return@addSnapshotListener

                }



                val players =

                    snapshot
                        ?.documents
                        ?.mapNotNull {


                            it.toObject(
                                Player::class.java
                            )

                        }

                        ?:emptyList()



                onChange(players)


            }


    }


}