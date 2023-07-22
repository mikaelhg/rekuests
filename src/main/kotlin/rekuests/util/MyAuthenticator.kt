package rekuests.util

import java.net.Authenticator
import java.net.PasswordAuthentication

internal class MyAuthenticator(private val username: String, private val password: String) : Authenticator() {
    override fun getPasswordAuthentication() =
        PasswordAuthentication(username, password.toCharArray())
}