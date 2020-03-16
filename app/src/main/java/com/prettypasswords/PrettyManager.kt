package com.prettypasswords

import com.prettypasswords.Utilities.Credential
import com.prettypasswords.Utilities.Encryption


// Singleton
object PrettyManager {

    const val sharedPreferenceKey = "PrettyManagerUserCredential"

    val e: Encryption = Encryption()
    var c: Credential? = null

}