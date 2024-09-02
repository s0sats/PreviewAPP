package com.namoadigital.prj001.util

import java.io.IOException

class NetworkConnectionException(message: String?) : IOException(message)
class ValidateNewFormUseCaseException(message: String?) : IOException(message)
class RepositoryException(message: String?) : IOException(message)
class TripUserException(message: String? = null) : IOException(message)