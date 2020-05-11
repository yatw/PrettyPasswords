package com.prettypasswords

import com.prettypasswords.controller.ContentManager
import com.prettypasswords.controller.Credential
import com.prettypasswords.controller.Encryption


// Singleton
object PrettyManager {

    const val sharedPreferenceKey = "PrettyManagerUserCredential"

    val e: Encryption = Encryption()
    var c: Credential? = null  // initialized during createUser or restoreCredentialFromFile
    var cm: ContentManager? = null  // initialized during createUser or restoreCredentialFromFile

    //having credential doesn't mean user is login, having sk means user is login

}