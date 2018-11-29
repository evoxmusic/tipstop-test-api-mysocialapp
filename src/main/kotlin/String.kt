import java.security.MessageDigest

/**
 * Created by evoxmusic on 29/11/2018.
 */
fun String.toHash(): String {
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(this.toByteArray())
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}