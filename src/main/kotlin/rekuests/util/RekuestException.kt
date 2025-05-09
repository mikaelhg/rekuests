package rekuests.util

import rekuests.Response
import java.io.IOException

class RekuestException(message: String, val response: Response) : IOException(message)
