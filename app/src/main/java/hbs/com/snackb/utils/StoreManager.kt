package hbs.com.snackb.utils


import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseApp
import com.google.firebase.database.*


/** Helper class for Firebase storage of cloud anchor IDs.  */
class StorageManager(context: Context) {
    companion object {
        private val TAG = StorageManager::class.java.name
        private val KEY_ROOT_DIR = "shared_anchor_codelab_root"
        private val KEY_NEXT_SHORT_CODE = "next_short_code"
        private val KEY_PREFIX = "anchor;"
        private val INITIAL_SHORT_CODE = 142
    }
    private lateinit var rootRef: DatabaseReference

    /** Listener for a new Cloud Anchor ID from the Firebase Database.  */
    interface CloudAnchorIdListener {
        fun onCloudAnchorIdAvailable(cloudAnchorId: String?)
    }

    /** Listener for a new short code from the Firebase Database.  */
    interface ShortCodeListener {
        fun onShortCodeAvailable(shortCode: Int?)
    }

    init {
        FirebaseApp.initializeApp(context)?.let {
            rootRef = FirebaseDatabase.getInstance(it).reference.child(KEY_ROOT_DIR)
        }
        DatabaseReference.goOnline()
    }

    /** Gets a new short code that can be used to store the anchor ID.  */
    fun currentShortCode(listener: ShortCodeListener) {
        // Run a transaction on the node containing the next short code available. This increments the
        // value in the database and retrieves it in one atomic all-or-nothing operation.
        rootRef
            .child(KEY_NEXT_SHORT_CODE)
            .runTransaction(
                object : Transaction.Handler {
                    override fun doTransaction(currentData: MutableData): Transaction.Result {
                        var shortCode = currentData.getValue(Int::class.java)
                        if (shortCode == null) {
                            shortCode = INITIAL_SHORT_CODE - 1
                        }
                        currentData.value = shortCode
                        return Transaction.success(currentData)
                    }

                    override fun onComplete(
                        error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                        if (!committed) {
                            Log.e(TAG, "Firebase Error", error?.toException())
                            listener.onShortCodeAvailable(null)
                        } else {
                            listener.onShortCodeAvailable(currentData?.getValue(Int::class.java))
                        }
                    }
                })
    }

    /** Gets a new short code that can be used to store the anchor ID.  */
    fun nextShortCode(listener: ShortCodeListener) {
        // Run a transaction on the node containing the next short code available. This increments the
        // value in the database and retrieves it in one atomic all-or-nothing operation.
        rootRef
            .child(KEY_NEXT_SHORT_CODE)
            .runTransaction(
                object : Transaction.Handler {
                    override fun doTransaction(currentData: MutableData): Transaction.Result {
                        var shortCode = currentData.getValue(Int::class.java)
                        if (shortCode == null) {
                            shortCode = INITIAL_SHORT_CODE - 1
                        }
                        currentData.value = shortCode + 1
                        return Transaction.success(currentData)
                    }

                    override fun onComplete(
                        error: DatabaseError?, committed: Boolean, currentData: DataSnapshot?) {
                        if (!committed) {
                            Log.e(TAG, "Firebase Error", error?.toException())
                            listener.onShortCodeAvailable(null)
                        } else {
                            listener.onShortCodeAvailable(currentData?.getValue(Int::class.java))
                        }
                    }
                })
    }

    /** Stores the cloud anchor ID in the configured Firebase Database.  */
    fun storeUsingShortCode(shortCode: Int, cloudAnchorId: String?) {
        rootRef.child(KEY_PREFIX + shortCode).setValue(cloudAnchorId)
    }

    /**
     * Retrieves the cloud anchor ID using a short code. Returns an empty string if a cloud anchor ID
     * was not stored for this short code.
     */
    fun getCloudAnchorID(shortCode: Int, listener: CloudAnchorIdListener) {
        rootRef
            .child(KEY_PREFIX + shortCode)
            .addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        listener.onCloudAnchorIdAvailable(dataSnapshot.value.toString())
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(
                            TAG, "The database operation for getCloudAnchorID was cancelled.",
                            error.toException()
                        )
                        listener.onCloudAnchorIdAvailable(null)
                    }
                })
    }

    /*private fun setCloudAnchor(newAnchor: Anchor) {
        if (cloudAnchor != null) {
            cloudAnchor.detach()
        }

        cloudAnchor = newAnchor
        appAnchorState = AppAnchorState.NONE
        snackbarHelper.hide(this)
    }*/
}