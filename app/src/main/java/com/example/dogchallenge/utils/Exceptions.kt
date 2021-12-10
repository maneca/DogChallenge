package com.example.dogchallenge.utils

import okio.IOException


class NoInternetConnectionException : IOException()

class UnknownException : IOException()

class ApiNotResponding : IOException()