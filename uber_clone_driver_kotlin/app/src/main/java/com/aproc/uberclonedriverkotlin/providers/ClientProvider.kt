package com.aproc.uberclonedriverkotlin.providers

import com.aproc.uberclonedriverkotlin.models.Client
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore

class ClientProvider {

    val db = Firebase.firestore.collection("Clients")

    fun create(client: Client): Task<Void>{
        return db.document(client.id!!).set(client)
    }

    fun getClientById(id: String): Task<DocumentSnapshot> {
        return db.document(id).get()
    }
}